package ru.hse.alice.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public abstract class SkillMessage {
    @JsonProperty("version")
    private String version;
}
