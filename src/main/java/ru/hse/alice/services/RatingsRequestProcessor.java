package ru.hse.alice.services;


import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.hse.alice.actions.GoodByeInterpretations;
import ru.hse.alice.actions.ShowRatingInterpretations;
import ru.hse.alice.contracts.IRequestProcessor;
import ru.hse.alice.contracts.IUserService;
import ru.hse.alice.models.dtos.Button;
import ru.hse.alice.models.dtos.Response;
import ru.hse.alice.models.dtos.SkillWebhookRequest;
import ru.hse.alice.models.dtos.SkillWebhookResponse;
import ru.hse.alice.phrases.DontUnderstandPhrases;
import ru.hse.alice.phrases.GoodByePhrases;
import ru.hse.alice.phrases.GreetingPhrases;
import ru.hse.alice.phrases.SayYourNamePhrases;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;


@Service
public class RatingsRequestProcessor implements IRequestProcessor {
    private final IUserService userService;
    private GreetingPhrases greetingPhrases;
    private GoodByePhrases goodByePhrases;
    private SayYourNamePhrases sayYourNamePhrases;
    private ShowRatingInterpretations showRatingInterpretations;
    private GoodByeInterpretations goodByeInterpretations;
    private DontUnderstandPhrases dontUnderstandPhrases;

    public RatingsRequestProcessor(IUserService userService) {
        if (userService == null) {
            throw new IllegalArgumentException("RatingService  is null");
        }
        this.userService = userService;
        this.greetingPhrases = new GreetingPhrases();
        this.goodByePhrases = new GoodByePhrases();
        this.sayYourNamePhrases = new SayYourNamePhrases();

        this.showRatingInterpretations = new ShowRatingInterpretations();
        this.goodByeInterpretations = new GoodByeInterpretations();
        this.dontUnderstandPhrases = new DontUnderstandPhrases();
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
                    Arrays.asList(new Button("Узнать свой рейтинг")),
                    false
            );
        }

        String command = request.getRequest().getCommand();

        if (showRatingInterpretations.contains(command)) {
            return buildResponse(request, sayYourNamePhrases.getRandom(), null, false);
        }


        if (goodByeInterpretations.contains(command)) {
            return buildResponse(request, goodByePhrases.getRandom(), null, true);
        }

        return buildResponse(
                request,
                dontUnderstandPhrases.getRandom(),
                Arrays.asList(new Button("Узнать свой рейтинг")),
                false
        );

    }
}
