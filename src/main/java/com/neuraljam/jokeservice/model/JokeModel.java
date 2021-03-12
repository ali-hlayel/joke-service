package com.neuraljam.jokeservice.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class JokeModel {

    private String title;

    private String text;
}
