package com.neuraljam.jokeservice.model;

import java.time.LocalDateTime;

public class TestJokeFactory {

    public static Joke createJoke() {
        Joke joke = new Joke();
        joke.setText("Two guys stole a calendar. They got six months each. —Submitted by Alex Del Bene");
        joke.setTitle("STOLEN CALENDAR");
        joke.setCreatedAt(LocalDateTime.of(2020,11,10,23,12));
        joke.setUpdatedAt(LocalDateTime.of(2020,11,18,23,12));
        return joke;
    }

    public static JokeCreateModel jokeCreateModel() {
        JokeCreateModel jokeCreateModel = new JokeCreateModel();
        jokeCreateModel.setText("Two guys stole a calendar. They got six months each. —Submitted by Alex Del Bene");
        jokeCreateModel.setTitle("STOLEN CALENDAR");
        return jokeCreateModel;
    }

    public static JokeUpdateModel jokeUpdateModel() {
        JokeUpdateModel jokeUpdateModel = new JokeUpdateModel();
        jokeUpdateModel.setTitle("updated text");
        return jokeUpdateModel;
    }
}
