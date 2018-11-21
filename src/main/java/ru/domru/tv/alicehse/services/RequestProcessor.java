package ru.domru.tv.alicehse.services;


import org.springframework.stereotype.Service;
import ru.domru.tv.alicehse.contracts.IRequestProcessor;
import ru.domru.tv.alicehse.contracts.IUserService;
import ru.domru.tv.alicehse.models.dtos.Button;
import ru.domru.tv.alicehse.models.dtos.Response;
import ru.domru.tv.alicehse.models.dtos.SkillWebhookRequest;
import ru.domru.tv.alicehse.models.dtos.SkillWebhookResponse;
import ru.domru.tv.alicehse.phrases.GoodByes;
import ru.domru.tv.alicehse.phrases.Greetings;

import javax.validation.constraints.NotNull;
import java.util.Arrays;


@Service
public class RequestProcessor implements IRequestProcessor<IUserService> {
    private final IUserService userService;
    private Greetings greetings;
    private GoodByes goodByes;

    public RequestProcessor(IUserService userService) {
        if (userService == null) {
            throw new IllegalArgumentException("RatingService  is null");
        }
        this.userService = userService;
        this.greetings = new Greetings();
        this.goodByes = new GoodByes();
    }

    @NotNull
    private static SkillWebhookResponse buildResponse(
            @NotNull SkillWebhookRequest request,
            @NotNull Response response
    ) {
        SkillWebhookResponse result = new SkillWebhookResponse();
        result.setResponse(response);
        result.setSession(request.getSession());
        result.setVersion(request.getVersion());
        return result;
    }

    @NotNull
    @Override
    public SkillWebhookResponse process(SkillWebhookRequest request) {
        if (request.getSession().isNew()) {
            Response greetingsResponse = new Response();
            greetingsResponse.setText(greetings.getRandom());
            greetingsResponse.setEndSession(false);
            greetingsResponse.setButtons(Arrays.asList(new Button("Узнать свой рейтинг")));
            return buildResponse(request, greetingsResponse);
        }

        Response response = new Response();
        response.setText(goodByes.getRandom());
        response.setEndSession(true);

        return buildResponse(request, response);
    }
}
