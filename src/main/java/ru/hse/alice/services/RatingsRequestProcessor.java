package ru.hse.alice.services;


import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.hse.alice.contracts.IRequestProcessor;
import ru.hse.alice.contracts.IUserService;
import ru.hse.alice.keywords.GoodByeInterpretations;
import ru.hse.alice.keywords.GreetingInterpretations;
import ru.hse.alice.keywords.ShowRatingInterpretations;
import ru.hse.alice.models.Payload;
import ru.hse.alice.models.User;
import ru.hse.alice.models.dtos.*;
import ru.hse.alice.phrases.*;

import javax.validation.constraints.NotNull;
import java.util.*;

import static ru.hse.alice.helpers.PhraseHelper.containsKeyWord;
import static ru.hse.alice.helpers.PhraseHelper.getRandom;


@Service
public class RatingsRequestProcessor implements IRequestProcessor {
    private static final String LOVE_DESC = "Игорь Николаев - Выпьем за любовь";
    private static final String LOVE_PICTURE_ID = "937455/ab900f879736d57b359a";
    private final IUserService userService;
    private Map<String, User> userSessions;

    public RatingsRequestProcessor(IUserService userService) {
        if (userService == null) {
            throw new IllegalArgumentException("RatingService  is null");
        }
        this.userService = userService;
        this.userSessions = new HashMap<>();
    }

    @NotNull
    private static SkillWebhookResponse buildResponse(
            @NotNull SkillWebhookRequest request,
            @NotNull String responseText,
            @Nullable List<Button> buttons,
            @Nullable Card card,
            Boolean endSession
    ) {
        SkillWebhookResponse result = new SkillWebhookResponse();
        Response response = new Response();
        response.setText(responseText);
        response.setButtons(buttons);
        response.setCard(card);
        response.setEndSession(endSession);

        result.setResponse(response);
        result.setSession(request.getSession());
        result.setVersion(request.getVersion());
        return result;
    }

    @Nullable
    private Map<String, String> getNameFromResponse(List<Entity> entities) {
        for (Entity entity : entities) {
            if (entity.getType().equals(EntityType.YANDEX_FIO)) {
                return (Map<String, String>) entity.getValue();
            }
        }
        return null;
    }

    private List<Button> generateButtons(Boolean forAdmin) {
        if (forAdmin) {
            return Collections.singletonList(new Button("Узнать свой рейтинг", new Payload("showrating")));

        } else {
            return Collections.singletonList(new Button("Узнать свой рейтинг", new Payload("showrating")));
        }
    }

    @NotNull
    @Override
    public SkillWebhookResponse process(@NotNull SkillWebhookRequest request) {
        if (request.getSession().isNew()) {
            return buildResponse(
                    request,
                    getRandom(GreetingPhrases.phrases),
                    null,
                    null,
                    false
            );
        }

        String sessionId = request.getSession().getSessionId();

        RequestType requestType = request.getRequest().getType();
        if (requestType == RequestType.BUTTON_PRESSED) {
            Map<String, String> payload = (Map<String, String>) request.getRequest().getPayload();

            User currentUser = userSessions.get(sessionId);
            if (Objects.equals(payload.get("button_command"), "showrating")) {
                Double rating = currentUser.getRating();
                if (rating != null) {
                    return buildResponse(
                            request,
                            currentUser.getFirstName() + "!\nТвой рейтинг: " + currentUser.getRating() + "\nСделать что-то еще?",
                            generateButtons(currentUser.getIsAdmin()),
                            null,
                            true
                    );
                }
                else{
                    return buildResponse(
                            request,
                            currentUser.getFirstName() + "!\nТвой рейтинг еще не заполнен!\nПопроси у администратора заполнить журнал.\nСделать что-то еще?",
                            generateButtons(currentUser.getIsAdmin()),
                            null,
                            true
                    );
                }
            }

            return buildResponse(
                    request,
                    getRandom(GreetingPhrases.phrases),
                    null,
                    null,
                    false
            );
        } else {
            String command = request.getRequest().getCommand();

            if (command == null){
                return buildResponse(
                        request,
                        getRandom(CantUnderstandYouPhrases.phrases),
                        null,
                        null,
                        false
                );
            }

            if (testForLove(command)) {
                return buildResponse(
                        request,
                        getRandom(LovePhrases.phrases),
                        null,
                        new Card(CardType.BIG_IMAGE, LOVE_PICTURE_ID, getRandom(LovePhrases.phrases), LOVE_DESC),
                        false
                );
            }

            if (!userSessions.containsKey(sessionId)) { // not authorized user
                if (containsKeyWord(command, GreetingInterpretations.keywords)) {
                    return buildResponse(
                            request,
                            "Мы же вроде бы уже поздоровались?\nХотя, с хорошим человеком это можно делать весь день!\n" +
                                    getRandom(GreetingPhrases.phrases),
                            null,
                            null,
                            false
                    );
                }
                if (containsKeyWord(command, GoodByeInterpretations.keywords)) {
                    if (userSessions.containsKey(sessionId)) {
                        userSessions.remove(sessionId);
                    }
                    return buildResponse(
                            request,
                            getRandom(GoodByePhrases.phrases),
                            null,
                            null,
                            true
                    );
                }

                Map<String, String> nameEntity = getNameFromResponse(request.getRequest().getNlu().getEntities());
                if (nameEntity != null) {
                    String firstName = nameEntity.get("first_name");
                    String lastName = nameEntity.get("last_name");

                    if (firstName == null){
                        firstName = "";
                    }
                    if (lastName == null){
                        lastName = "";
                    }

                    if (!userService.userExists(firstName, lastName)) {
                        return buildResponse(
                                request,
                                "Тебя зовут " + firstName + " " + lastName + "?\n" + getRandom(CantFindYouPhrases.phrases),
                                null,
                                null,
                                false
                        );
                    } else {
                        User currentUser = userService.getUser(
                                nameEntity.get("first_name"),
                                nameEntity.get("last_name")
                        );
                        this.userSessions.put(request.getSession().getSessionId(), currentUser);
                        return buildResponse(
                                request,
                                currentUser.getFirstName() + " " + currentUser.getLastName() + ", я могу выполнить следующие задачи:",
                                generateButtons(currentUser.getIsAdmin()),
                                null,
                                false
                        );
                    }
                } else { // YANDEX.FIO not found in response
                    return buildResponse(
                            request,
                            command + "?\n" + getRandom(StrangeNamePhrases.phrases),
                            null,
                            null,
                            false
                    );
                }
            } else {
                User currentUser = userSessions.get(sessionId);
                if (containsKeyWord(command, ShowRatingInterpretations.keywords)) {
                    Double rating = currentUser.getRating();
                    if (rating != null) {
                        return buildResponse(
                                request,
                                currentUser.getFirstName() + "!\nТвой рейтинг: " + currentUser.getRating() + "\nСделать что-то еще?",
                                generateButtons(currentUser.getIsAdmin()),
                                null,
                                true
                        );
                    }
                    else{
                        return buildResponse(
                                request,
                                currentUser.getFirstName() + "!\nТвой рейтинг еще не заполнен!\nПопроси у администратора заполнить журнал.\nСделать что-то еще?",
                                generateButtons(currentUser.getIsAdmin()),
                                null,
                                true
                        );
                    }
                }

                if (containsKeyWord(command, GoodByeInterpretations.keywords)) {
                    if (userSessions.containsKey(sessionId)) {
                        userSessions.remove(sessionId);
                    }
                    return buildResponse(
                            request,
                            getRandom(GoodByePhrases.phrases),
                            null,
                            null,
                            true
                    );
                }

                return buildResponse(
                        request,
                        getRandom(CantUnderstandYouPhrases.phrases),
                        generateButtons(currentUser.getIsAdmin()),
                        null,
                        false
                );
            }
        }
    }

    private Boolean testForLove(@Nullable String command) {
        if (command == null) {
            return false;
        }
        return command.toLowerCase().contains("выпьем за любовь");
    }
}
