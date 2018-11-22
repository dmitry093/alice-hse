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
    private static final String ADMIN_CODE_PHRASE = "вышка";
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
    private Number getRatingFromRequestEntities(List<Entity> entities) {
        for (Entity entity : entities) {
            if (entity.getType().equals(EntityType.YANDEX_NUMBER)) {
                return (Number) entity.getValue();
            }
        }
        return null;
    }

    @Nullable
    private Map<String, String> getNameFromRequestEntities(List<Entity> entities) {
        for (Entity entity : entities) {
            if (entity.getType().equals(EntityType.YANDEX_FIO)) {
                return (Map<String, String>) entity.getValue();
            }
        }
        return null;
    }

    private List<Button> generateButtons(Boolean forAdmin) {
        if (forAdmin) {
            return Arrays.asList(
                    new Button("Узнать свой рейтинг", new Payload("showrating")),
                    new Button("Открыть меню администратора", new Payload("adminmenu"))
            );

        } else {
            return Collections.singletonList(new Button("Узнать свой рейтинг", new Payload("showrating")));
        }
    }

    private SkillWebhookResponse processUtteranceForAuthorized(@NotNull SkillWebhookRequest request) {
        String sessionId = request.getSession().getSessionId();
        String command = request.getRequest().getCommand();
        User currentUser = userSessions.get(sessionId);
        if (containsKeyWord(command, ShowRatingInterpretations.keywords)) {
            return answerRating(request, currentUser);
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

        if (currentUser.getIsAdmin()){ // if user is admin
            List<String> tokens = request.getRequest().getNlu().getTokens();
            if (tokens.contains(ADMIN_CODE_PHRASE)){
                Map<String, String> nameEntity = getNameFromRequestEntities(request.getRequest().getNlu().getEntities());
                if (nameEntity == null) {
                    return buildResponse(
                            request,
                            currentUser.getFirstName() + ", не могу найти имя и фамилию пользователя, над которыми ты хочешь производить действия.",
                            generateButtons(currentUser.getIsAdmin()),
                            null,
                            false);
                }
                String firstName = nameEntity.get("first_name");
                String lastName = nameEntity.get("last_name");

                if (firstName == null) {
                    return buildResponse(
                            request,
                            currentUser.getFirstName() + ", при добавлении пользователя необходимо указать имя.",
                            generateButtons(currentUser.getIsAdmin()),
                            null,
                            false);
                }
                if (lastName == null) {
                    return buildResponse(
                            request,
                            currentUser.getFirstName() + ", при добавлении пользователя необходимо указать фамилию.",
                            generateButtons(currentUser.getIsAdmin()),
                            null,
                            false);
                }
                if (tokens.contains("добавь")){
                    userService.saveUser(new User(firstName, lastName, null, false));
                    return buildResponse(
                            request,
                            firstName + " " + lastName + " добавлен!",
                            generateButtons(currentUser.getIsAdmin()),
                            null,
                            false
                    );
                }
                if (tokens.contains("поставь")){
                    Number rating = getRatingFromRequestEntities(request.getRequest().getNlu().getEntities());
                    if (rating == null){
                        return buildResponse(
                                request,
                                currentUser.getFirstName() + ", при обновлении рейтинга пользователя необходимо указать новое значение рейтинга.",
                                generateButtons(currentUser.getIsAdmin()),
                                null,
                                false);
                    }
                    String response = "";
                    if (!userService.userExists(firstName, lastName)){
                        response = "Пользователь " + firstName + " " + lastName + " создан.\n";
                    }
                    userService.saveUser(new User(firstName, lastName, rating, false));
                    return buildResponse(
                            request,
                            response + "У " + firstName + " " + lastName + " рейтинг обновлен до " + rating + ".",
                            generateButtons(currentUser.getIsAdmin()),
                            null,
                            false
                    );
                }
                if (tokens.contains("назначь") && tokens.contains("администратором") || tokens.contains("админом")){
                    String response = "";
                    if (!userService.userExists(firstName, lastName)){
                        userService.saveUser(new User(firstName, lastName, null, true));
                        response = "Пользователь " + firstName + " " + lastName + " создан.\n";
                    }
                    else{
                        Number currentRating = userService.getUser(firstName, lastName).getRating();
                        userService.saveUser(new User(firstName, lastName, currentRating, true));
                    }
                    return buildResponse(
                            request,
                            response +  firstName + " " + lastName + "назначен администратором.",
                            generateButtons(currentUser.getIsAdmin()),
                            null,
                            false
                    );
                }
            }
        }

        return buildResponse(
                request,
                getRandom(CantUnderstandYouPhrases.phrases),
                generateButtons(currentUser.getIsAdmin()),
                null,
                false
        );
    }

    private SkillWebhookResponse processUtteranceForNonAuthorized(@NotNull SkillWebhookRequest request) {
        String sessionId = request.getSession().getSessionId();
        String command = request.getRequest().getCommand();
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

        Map<String, String> nameEntity = getNameFromRequestEntities(request.getRequest().getNlu().getEntities());
        if (nameEntity != null) {
            String firstName = nameEntity.get("first_name");
            String lastName = nameEntity.get("last_name");

            if (firstName == null) {
                firstName = "";
            }
            if (lastName == null) {
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
                        firstName,
                        lastName
                );
                this.userSessions.put(request.getSession().getSessionId(), currentUser);
                return buildResponse(
                        request,
                        currentUser.getFirstName() + ", я могу выполнить следующие задачи:",
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
    }

    private SkillWebhookResponse answerRating(@NotNull SkillWebhookRequest request, @NotNull User currentUser) {
        Number rating = currentUser.getRating();
        if (rating != null) {
            return buildResponse(
                    request,
                    currentUser.getFirstName() + "!\nТвой рейтинг: " + currentUser.getRating() + "\nСделать что-то еще?",
                    generateButtons(currentUser.getIsAdmin()),
                    null,
                    false
            );
        } else {
            return buildResponse(
                    request,
                    currentUser.getFirstName() + "!\nТвой рейтинг еще не заполнен!\nПопроси у администратора заполнить журнал.\nСделать что-то еще?",
                    generateButtons(currentUser.getIsAdmin()),
                    null,
                    false
            );
        }
    }

    private SkillWebhookResponse processPressedButton(@NotNull SkillWebhookRequest request) {
        Map<String, String> payload = (Map<String, String>) request.getRequest().getPayload();
        String sessionId = request.getSession().getSessionId();

        User currentUser = userSessions.get(sessionId);
        if (Objects.equals(payload.get("button_command"), "showrating")) {
            return answerRating(request, currentUser);
        }

        if (Objects.equals(payload.get("button_command"), "adminmenu")) {
            return buildResponse(
                    request,
                    currentUser.getFirstName() + ", как администратор ты можешь меня попросить:\n" +
                            "\"Добавь <имя> <фамилия>\" - я добавлю этого человека в список пользователей системы.\n\n" +
                            "\"Поставь <рейтинг> для <имя> <фамилия>\" - я обновлю рейтинг этого пользователя.\n\n" +
                            "\"Назначь <имя> <фамилия> администратором\" - я назначу этого пользователя администратором.\n\n" +
                            "И не забудь при этом назвать пароль администратора!",
                    null,
                    null,
                    false
            );
        }

        return buildResponse(
                request,
                currentUser.getFirstName() + ", я могу выполнить следующие задачи:",
                generateButtons(currentUser.getIsAdmin()),
                null,
                false
        );
    }

    @NotNull
    @Override
    public SkillWebhookResponse process(@NotNull SkillWebhookRequest request) {
        if (request.getSession().isNew()) { // send greeting to new client
            return buildResponse(
                    request,
                    getRandom(GreetingPhrases.phrases),
                    null,
                    null,
                    false
            );
        }

        RequestType requestType = request.getRequest().getType();
        if (requestType == RequestType.BUTTON_PRESSED) { // pressed button
            return processPressedButton(request);
        } else {
            String command = request.getRequest().getCommand();

            if (command == null) {
                return buildResponse(
                        request,
                        getRandom(CantUnderstandYouPhrases.phrases),
                        null,
                        null,
                        false
                );
            }

            if (testForLove(command)) { // what about drink for love?
                String randomLovePhrase = getRandom(LovePhrases.phrases);
                return buildResponse(
                        request,
                        randomLovePhrase,
                        null,
                        new Card(CardType.BIG_IMAGE, LOVE_PICTURE_ID, randomLovePhrase, LOVE_DESC),
                        false
                );
            }
            String sessionId = request.getSession().getSessionId();

            if (!userSessions.containsKey(sessionId)) { // not authorized user
                return processUtteranceForNonAuthorized(request);
            } else { // authorized
                return processUtteranceForAuthorized(request);
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
