package com.neuraljam.jokeservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@Data
public class JokeUpdateModel {
    @NotBlank
    @Schema(required = true, example = "Joke")
    private String title;

    @NotBlank
    @Schema(required = true, example = "Joke create Model")
    private String text;
}
