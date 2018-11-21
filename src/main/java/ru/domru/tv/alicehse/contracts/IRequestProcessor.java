package ru.domru.tv.alicehse.contracts;

import ru.domru.tv.alicehse.models.dtos.SkillWebhookRequest;
import ru.domru.tv.alicehse.models.dtos.SkillWebhookResponse;

import javax.validation.constraints.NotNull;

public interface IRequestProcessor<IUserService> {
    SkillWebhookResponse process(@NotNull SkillWebhookRequest request);
}
