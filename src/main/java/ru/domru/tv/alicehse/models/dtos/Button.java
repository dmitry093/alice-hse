package ru.domru.tv.alicehse.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
public class Button {
    @JsonProperty("title")
    private String title;

    @JsonProperty("payload")
    @JsonInclude(NON_NULL)
    private Object payload;

    @JsonProperty("url")
    @JsonInclude(NON_NULL)
    private String url;

    @JsonProperty("hide")
    @JsonInclude(NON_NULL)
    private Boolean hide;

    Button() { }

    public Button(@NonNull String title) {
        this.title = title;
    }
}
