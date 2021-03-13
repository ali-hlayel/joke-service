package com.neuraljam.jokeservice.service;

import com.neuraljam.jokeservice.exceptions.EntityAlreadyExistsException;
import com.neuraljam.jokeservice.model.Joke;
import com.neuraljam.jokeservice.model.JokeUpdateModel;

import javax.persistence.NoResultException;
import java.util.List;

public interface JokeService {

    List<Joke> getAllJokes(int page, int limit);

    Joke getById(Long id) throws NoResultException;

    Joke getRandomJoke() throws NoResultException;

    void delete(Long id);

    Joke create(Joke joke) throws EntityAlreadyExistsException;

    List<Joke> getAllJokesByText(String text, int page, int limit);

    Joke update(Long id, JokeUpdateModel jokeUpdateModel);
}
