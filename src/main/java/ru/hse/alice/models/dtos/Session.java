package ru.hse.alice.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Session {
    @JsonProperty("session_id")
    private String sessionId;

    @JsonProperty("skill_id")
    private String skillId;

    @JsonProperty("user_id")
    private String userId;
}
