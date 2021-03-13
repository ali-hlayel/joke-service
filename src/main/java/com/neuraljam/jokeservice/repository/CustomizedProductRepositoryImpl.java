package com.neuraljam.jokeservice.repository;

import com.neuraljam.jokeservice.model.Joke;
import org.hibernate.search.mapper.orm.Search;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class CustomizedProductRepositoryImpl implements CustomizedProductRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Joke> search(String terms, int limit, int offset) {
        return Search.session(em).search(Joke.class)
                .where(f -> f.match()
                        .fields("text")
                        .matching(terms))
                .fetchHits(offset, limit);
    }
}
