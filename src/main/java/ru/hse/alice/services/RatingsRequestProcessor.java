package ru.hse.alice.services;


import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.hse.alice.actions.GoodByeInterpretations;
import ru.hse.alice.actions.GreetingInterpretations;
import ru.hse.alice.actions.ShowRatingInterpretations;
import ru.hse.alice.contracts.IRequestProcessor;
import ru.hse.alice.contracts.IUserService;
import ru.hse.alice.models.User;
import ru.hse.alice.models.dtos.*;
import ru.hse.alice.phrases.CantFindYouPhrases;
import ru.hse.alice.phrases.CantUnderstandYouPhrases;
import ru.hse.alice.phrases.GoodByePhrases;
import ru.hse.alice.phrases.GreetingPhrases;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class RatingsRequestProcessor implements IRequestProcessor {
    private final IUserService userService;
    private GreetingPhrases greetingPhrases;
    private GoodByePhrases goodByePhrases;

    private ShowRatingInterpretations showRatingInterpretations;
    private GreetingInterpretations greetingInterpretations;
    private GoodByeInterpretations goodByeInterpretations;
    private CantFindYouPhrases cantFindYouPhrases;
    private CantUnderstandYouPhrases cantUnderstandYouPhrases;

    private Map<String, User> userSessions;

    public RatingsRequestProcessor(IUserService userService) {
        if (userService == null) {
            throw new IllegalArgumentException("RatingService  is null");
        }
        this.userService = userService;
        this.greetingPhrases = new GreetingPhrases();
        this.goodByePhrases = new GoodByePhrases();

        this.showRatingInterpretations = new ShowRatingInterpretations();
        this.greetingInterpretations = new GreetingInterpretations();
        this.goodByeInterpretations = new GoodByeInterpretations();
        this.cantFindYouPhrases = new CantFindYouPhrases();
        this.cantUnderstandYouPhrases = new CantUnderstandYouPhrases();

        this.userSessions = new HashMap<>();
    }

    @NotNull
    private static SkillWebhookResponse buildResponse(
            @NotNull SkillWebhookRequest request,
            @NotNull String responseText,
            @Nullable List<Button> buttons,
            Boolean endSession
    ) {
        SkillWebhookResponse result = new SkillWebhookResponse();
        Response response = new Response();
        response.setText(responseText);
        response.setButtons(buttons);
        response.setEndSession(endSession);

        result.setResponse(response);
        result.setSession(request.getSession());
        result.setVersion(request.getVersion());
        return result;
    }

    private List<Button> generateButtons(Boolean forAdmin) {
        if (forAdmin) {
            return null;
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
                    greetingPhrases.getRandom(),
                    null,
                    false
            );
        }

        String command = request.getRequest().getCommand();
        String sessionId = request.getSession().getSessionId();
        Payload payload = request.getRequest().getPayload();

        if (!userSessions.containsKey(sessionId)) { // not authorized user
            if (greetingInterpretations.contains(command)) {
                return buildResponse(
                        request,
                        "Мы же вроде бы уже поздоровались? Хотя, почему нет.\n" + greetingPhrases.getRandom(),
                        null,
                        false);
            }
            if (!userService.userExists(command)) {
                return buildResponse(
                        request,
                        "Тебя зовут " + command + "?\n" + cantFindYouPhrases.getRandom(),
                        null,
                        false);
            } else {
                User currentUser = userService.getUser(command);
                this.userSessions.put(request.getSession().getSessionId(), currentUser);
                return buildResponse(
                        request,
                        currentUser.getName() + ", я могу выполнить следующие задачи:",
                        generateButtons(currentUser.getIsAdmin()),
                        false
                );
            }
        } else {
            User currentUser = userSessions.get(sessionId);
            if (showRatingInterpretations.contains(command)) {
                return buildResponse(
                        request,
                        currentUser.getName() + "!\nТвой рейтинг: " + currentUser.getRating() + "\nСделать что-то еще?",
                        generateButtons(currentUser.getIsAdmin()),
                        true);
            }

            if (goodByeInterpretations.contains(command)) {
                if (userSessions.containsKey(sessionId)) {
                    userSessions.remove(sessionId);
                }
                return buildResponse(request, goodByePhrases.getRandom(), null, true);
            }

            return buildResponse(
                    request,
                    cantUnderstandYouPhrases.getRandom(),
                    generateButtons(currentUser.getIsAdmin())
                    , false);
        }
    }
}
