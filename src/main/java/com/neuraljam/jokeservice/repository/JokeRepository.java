package com.neuraljam.jokeservice.repository;

import com.neuraljam.jokeservice.model.Joke;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JokeRepository extends JpaRepository<Joke, Long>, CustomizedProductRepository{

    @Query(value = "SELECT * FROM joke ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Joke findRandomJokes();

    boolean existsByText(String text);
}
