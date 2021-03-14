package com.neuraljam.jokeservice.repository;

import com.neuraljam.jokeservice.model.Joke;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class CustomizedProductRepositoryImpl implements CustomizedProductRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Joke> search(String text) {
        SearchResult<Joke> result = Search.session(entityManager).search(Joke.class)
                .where(f -> f.match()
                        .fields("text")
                        .matching(text))
                .fetchAll();
        List<Joke> jokes = result.hits();
        return jokes;
    }
}
