package ru.domru.tv.alicehse.controllers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.domru.tv.alicehse.contracts.IRequestProcessor;
import ru.domru.tv.alicehse.models.dtos.SkillWebhookRequest;
import ru.domru.tv.alicehse.models.dtos.SkillWebhookResponse;

@RestController
@RequiredArgsConstructor
public class AliceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AliceController.class);

    private IRequestProcessor processor;

    public AliceController(IRequestProcessor processor) {
        if (processor == null) {
            throw new IllegalArgumentException("RequestProcessor is null");
        }
        this.processor = processor;
    }

    @PostMapping(value = "/webhook")
    public SkillWebhookResponse processWebHook(@RequestBody SkillWebhookRequest request) {
        LOGGER.info("Processing webhook {}", request);
        return processor.process(request);
    }
}
