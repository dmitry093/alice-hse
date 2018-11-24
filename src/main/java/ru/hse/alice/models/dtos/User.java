package ru.hse.alice.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @JsonProperty("first_name")
    @JsonInclude(NON_NULL)
    private String firstName;
    @JsonProperty("last_name")
    @JsonInclude(NON_NULL)
    private String lastName;
    @JsonProperty("rating")
    private Number rating;
    @JsonProperty("is_admin")
    @JsonInclude(NON_NULL)
    private Boolean isAdmin;
}
