package ru.hse.alice.contracts;

import ru.hse.alice.models.dtos.SkillWebhookRequest;
import ru.hse.alice.models.dtos.SkillWebhookResponse;

import javax.validation.constraints.NotNull;

public interface IRequestProcessor{
    SkillWebhookResponse process(@NotNull SkillWebhookRequest request);
}