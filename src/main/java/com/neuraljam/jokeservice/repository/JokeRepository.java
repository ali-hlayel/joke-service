package com.neuraljam.jokeservice.repository;

import com.neuraljam.jokeservice.model.Joke;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JokeRepository extends JpaRepository<Joke, Long> {

    @Query(value = "SELECT * FROM joke ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Joke findRandomJokes();

    boolean existsByText(String text);

    //@Query(value = "SELECT jo.* FROM joke jo WHERE jo.text LIKE %:text%", nativeQuery = true)
    List<Joke> findByTextContainingIgnoreCase(String text);
}
