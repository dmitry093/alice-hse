package ru.domru.tv.alicehse.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SkillWebhookResponse extends SkillMessage {
    @JsonProperty("response")
    private Response response;

    @JsonProperty("session")
    private Session session;
}
