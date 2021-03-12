package com.neuraljam.jokeservice.service;

import com.neuraljam.jokeservice.exceptions.EntityAlreadyExistsException;
import com.neuraljam.jokeservice.model.Joke;
import com.neuraljam.jokeservice.repository.JokeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.NoResultException;
import java.util.List;

@Service("jokeService")
public class JokeServiceImpl implements JokeService {

    @Resource
    private JokeRepository jokeRepository;

    public JokeServiceImpl(JokeRepository jokeRepository) {
        super();
        this.jokeRepository = jokeRepository;
    }

    @Override
    public List<Joke> getAllJokes(int page, int limit) {
        if (page > 0) page = page - 1;
        Pageable pageableRequest = PageRequest.of(page, limit);
        Page<Joke> personsPage = jokeRepository.findAll(pageableRequest);
        List<Joke> personsList = personsPage.getContent();
        return personsList;
    }

    @Override
    public List<Joke> getAllJokesByText(String text) {
        Page<Joke> personsPage = (Page<Joke>) jokeRepository.searchByTextLike(text);
        List<Joke> personsList = personsPage.getContent();
        return personsList;
    }
    @Override
    public Joke getById(Long id) throws NoResultException {
                Joke result = jokeRepository.findById(id).orElseThrow(
                () -> new NoResultException("No Jokes found with id: " + id)
        );
        return result;
    }

    @Override
    public Joke getRandomJoke() throws NoResultException {
        Joke result = jokeRepository.findRandomJokes();
        return result;
    }

    @Override
    public void delete(Long id) throws NoResultException {
        Joke existingJoke = getById(id);
        jokeRepository.delete(existingJoke);
    }

    @Transactional
    @Override
    public Joke create(Joke joke) throws EntityAlreadyExistsException {
        Joke result;
        if (jokeRepository.existsByText(joke.getText())) {
            throw new EntityAlreadyExistsException("Can't create Joke, Text: " + joke
                    .getText() + " is already in exist.");
        }
        result = jokeRepository.save(joke);
        return result;
    }
}
