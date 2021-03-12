package com.neuraljam.jokeservice.service;

import com.neuraljam.jokeservice.exceptions.EntityAlreadyExistsException;
import com.neuraljam.jokeservice.model.Joke;

import javax.persistence.NoResultException;
import java.util.List;

public interface JokeService {

    List<Joke> getAllJokes(int page, int limit);

    Joke getById(Long id) throws NoResultException;

    Joke getRandomJoke() throws NoResultException;

    void delete(Long id) throws NoResultException;

    Joke create(Joke joke) throws EntityAlreadyExistsException;

    List<Joke> getAllJokesByText(String text);
}
