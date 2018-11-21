package ru.hse.alice.services;


import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.hse.alice.actions.GoodByeInterpretations;
import ru.hse.alice.actions.ShowRatingInterpretations;
import ru.hse.alice.contracts.IRequestProcessor;
import ru.hse.alice.contracts.IUserService;
import ru.hse.alice.models.User;
import ru.hse.alice.models.dtos.*;
import ru.hse.alice.phrases.CantFindYouPhrases;
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
    private GoodByeInterpretations goodByeInterpretations;
    private CantFindYouPhrases cantFindYouPhrases;

    private Map<String, User> userSessions;

    public RatingsRequestProcessor(IUserService userService) {
        if (userService == null) {
            throw new IllegalArgumentException("RatingService  is null");
        }
        this.userService = userService;
        this.greetingPhrases = new GreetingPhrases();
        this.goodByePhrases = new GoodByePhrases();

        this.showRatingInterpretations = new ShowRatingInterpretations();
        this.goodByeInterpretations = new GoodByeInterpretations();
        this.cantFindYouPhrases = new CantFindYouPhrases();

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

        Payload payload = request.getRequest().getPayload();
        String sessionId = request.getSession().getSessionId();

//        if (payload == null) {
            String command = request.getRequest().getCommand();


            if (showRatingInterpretations.contains(command)) {
                return buildResponse(
                        request,
                        "тут будет рейтинг",
                        null,
                        false
                );
            }

            if (goodByeInterpretations.contains(command)) {
                if (userSessions.containsKey(sessionId)){
                    userSessions.remove(sessionId);
                }
                return buildResponse(request, goodByePhrases.getRandom(), null, true);
            }

            if (!userService.userExists(command)) {
                return buildResponse(request, cantFindYouPhrases.getRandom(), null, false);
            } else {
                User currentUser = userService.getUser(command);
                this.userSessions.put(request.getSession().getSessionId(), currentUser);
                if (!currentUser.getIsAdmin()) {
                    return buildResponse(
                            request,
                            currentUser.getName() + ", ты можешь через меня:",
                            Collections.singletonList(new Button("Узнать свой рейтинг", new Payload("showrating"))),
                            false
                    );
                }
            }

//        }
        return null;
    }
}
