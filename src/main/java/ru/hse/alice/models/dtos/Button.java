package ru.hse.alice.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import ru.hse.alice.models.Payload;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
public class Button {
    @JsonProperty("title")
    private String title;

    @JsonProperty("payload")
    @JsonInclude(NON_NULL)
    private Payload payload;

    @JsonProperty("url")
    @JsonInclude(NON_NULL)
    private String url;

    @JsonProperty("hide")
    @JsonInclude(NON_NULL)
    private Boolean hide;

    public Button(@NonNull String title, @Nullable Payload payload) {
        this.title = title;
        this.payload = payload;
    }
}
