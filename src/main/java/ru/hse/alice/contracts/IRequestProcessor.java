package ru.hse.alice.contracts;

import ru.hse.alice.models.dtos.User;
import ru.hse.alice.models.dtos.SkillWebhookRequest;
import ru.hse.alice.models.dtos.SkillWebhookResponse;

import javax.validation.constraints.NotNull;
import java.util.Map;

public interface IRequestProcessor{
    SkillWebhookResponse process(@NotNull SkillWebhookRequest request);
    Map<String,User> getAllSessions();
}