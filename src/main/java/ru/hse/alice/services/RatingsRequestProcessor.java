package ru.hse.alice.services;


import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.hse.alice.contracts.IRequestProcessor;
import ru.hse.alice.contracts.IUserService;
import ru.hse.alice.keywords.GoodByeInterpretations;
import ru.hse.alice.keywords.GreetingInterpretations;
import ru.hse.alice.keywords.ShowRatingInterpretations;
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

import static ru.hse.alice.helpers.PhraseHelper.containsKeyWord;
import static ru.hse.alice.helpers.PhraseHelper.getRandom;


@Service
public class RatingsRequestProcessor implements IRequestProcessor {
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
                    false
            );
        }

        String command = request.getRequest().getCommand();
        String sessionId = request.getSession().getSessionId();
        Payload payload = request.getRequest().getPayload();

        if (!userSessions.containsKey(sessionId)) { // not authorized user
            if (containsKeyWord(command, GreetingInterpretations.keywords)) {
                return buildResponse(
                        request,
                        "Мы же вроде бы уже поздоровались?\nХотя, с хорошим человеком это можно делать весь день!\n" +
                                getRandom(GreetingPhrases.phrases),
                        null,
                        false);
            }
            if (containsKeyWord(command, GoodByeInterpretations.keywords)) {
                if (userSessions.containsKey(sessionId)) {
                    userSessions.remove(sessionId);
                }
                return buildResponse(request, getRandom(GoodByePhrases.phrases), null, true);
            }
            if (!userService.userExists(command)) {
                return buildResponse(
                        request,
                        "Тебя зовут " + command + "?\n" + getRandom(CantFindYouPhrases.phrases),
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
            if (containsKeyWord(command, ShowRatingInterpretations.keywords)) {
                return buildResponse(
                        request,
                        currentUser.getName() + "!\nТвой рейтинг: " + currentUser.getRating() + "\nСделать что-то еще?",
                        generateButtons(currentUser.getIsAdmin()),
                        true);
            }

            if (containsKeyWord(command, GoodByeInterpretations.keywords)) {
                if (userSessions.containsKey(sessionId)) {
                    userSessions.remove(sessionId);
                }
                return buildResponse(request, getRandom(GoodByePhrases.phrases), null, true);
            }

            return buildResponse(
                    request,
                    getRandom(CantUnderstandYouPhrases.phrases),
                    generateButtons(currentUser.getIsAdmin())
                    , false);
        }
    }
}
