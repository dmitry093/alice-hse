package ru.domru.tv.alicehse.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @see <a href="https://tech.yandex.ru/dialogs/alice/doc/protocol-docpage/">Yandex specs</a>
 */
@Data
public class SkillWebhookRequest extends SkillMessage {
    @JsonProperty("meta")
    private Meta meta;

    @JsonProperty("request")
    private Request request;

    @JsonProperty("session")
    private RequestSession session;
}
