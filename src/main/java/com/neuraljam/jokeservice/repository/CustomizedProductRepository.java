package com.neuraljam.jokeservice.repository;

import com.neuraljam.jokeservice.model.Joke;

import java.util.List;

public interface CustomizedProductRepository {

    List<Joke> search(String terms, int limit, int offset);
}
