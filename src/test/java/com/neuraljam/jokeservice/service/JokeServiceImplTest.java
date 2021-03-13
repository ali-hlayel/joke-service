package com.neuraljam.jokeservice.service;

import com.neuraljam.jokeservice.exceptions.EntityAlreadyExistsException;
import com.neuraljam.jokeservice.model.Joke;
import com.neuraljam.jokeservice.model.JokeUpdateModel;
import com.neuraljam.jokeservice.model.TestJokeFactory;
import com.neuraljam.jokeservice.repository.JokeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.NotAcceptableStatusException;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JokeServiceImplTest {

    @Mock
    private JokeRepository jokeRepository;

    @InjectMocks
    private JokeServiceImpl jokeService;

    @Test
    void testGetAllJokes() {
        PageRequest pageRequest = PageRequest.of(0, 2);
        Joke firstJoke = TestJokeFactory.createJoke();
        firstJoke.setId(1L);
        Joke secondJoke = TestJokeFactory.createJoke();
        secondJoke.setId(2L);
        List<Joke> jokes = new ArrayList<>();
        jokes.add(firstJoke);
        jokes.add(secondJoke);
        Page<Joke> jokesPage = new PageImpl<>(jokes, pageRequest, jokes.size());
        when(jokeRepository.findAll(pageRequest)).thenReturn(jokesPage);
        List<Joke> results = jokeService.getAllJokes(0, 2);
        assertEquals(2, results.size());
        assertEquals(firstJoke.getTitle(), results.get(0).getTitle());
        assertEquals(secondJoke.getTitle(), results.get(1).getTitle());
        verify(jokeRepository).findAll(pageRequest);
    }

    @Test
    void testGetAllJokesByText() {
        Joke firstJoke = TestJokeFactory.createJoke();
        firstJoke.setId(1L);
        Joke secondJoke = TestJokeFactory.createJoke();
        secondJoke.setId(2L);
        List<Joke> jokes = new ArrayList<>();
        jokes.add(firstJoke);
        jokes.add(secondJoke);
        String text = "guys";
        when(jokeRepository.findByTextContainingIgnoreCase(any(String.class))).thenReturn(jokes);
        List<Joke> results = jokeService.getAllJokesByText(text);
        assertEquals(2, results.size());
        assertEquals(firstJoke.getTitle(), results.get(0).getTitle());
        assertEquals(secondJoke.getTitle(), results.get(1).getTitle());
        verify(jokeRepository).findByTextContainingIgnoreCase(text);
    }

    @Test
    void testGetAllJokesByTextThrowsNotFoundException() {
        String text = "";
        when(jokeRepository.findByTextContainingIgnoreCase(any(String.class))).thenThrow(NoResultException.class);
        Assertions.assertThrows(NoResultException.class, () -> jokeService.getAllJokesByText(text));
        verify(jokeRepository).findByTextContainingIgnoreCase(text);
    }

    @Test
    public void testGetById() {
        Joke joke = TestJokeFactory.createJoke();
        joke.setId(1L);
        when(jokeRepository.findById(any(Long.class))).thenReturn(Optional.of(joke));
        Joke result = jokeService.getById(1L);
        assertEquals(joke.getText(), result.getText());
        verify(jokeRepository).findById(1L);
    }

    @Test
    public void testGetByIdThrowsNoResultException() {
        when(jokeRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(NoResultException.class, () -> jokeService.getById(1L));
        verify(jokeRepository).findById(1L);
    }

    @Test
    void testGetRandomJoke() {
        Joke joke = TestJokeFactory.createJoke();
        joke.setId(1L);
        when(jokeRepository.findRandomJokes()).thenReturn(joke);
        Joke result = jokeService.getRandomJoke();
        assertEquals(joke.getText(), result.getText());
        verify(jokeRepository).findRandomJokes();
    }

    @Test
    public void testCreate() throws EntityAlreadyExistsException {
        Joke joke = TestJokeFactory.createJoke();
        joke.setId(1L);
        when(jokeRepository.save(any(Joke.class))).thenReturn(joke);
        Joke result = jokeService.create(joke);
        assertEquals(joke.getText(), result.getText());
        assertEquals(joke.getTitle(), result.getTitle());
    }

    @Test
    public void testCreateThrowsEntityAlreadyExistsException() {
        Joke joke = TestJokeFactory.createJoke();
        joke.setId(1L);
        when(jokeRepository.existsByText(joke.getText())).thenReturn(true);
        assertThrows(EntityAlreadyExistsException.class, () -> jokeService.create(joke));
    }

    @Test
    public void testUpdate() {
        Joke joke = TestJokeFactory.createJoke();
        joke.setId(1L);
        JokeUpdateModel updatedJoke = TestJokeFactory.jokeUpdateModel();
        when(jokeRepository.findById(any(Long.class))).thenReturn(Optional.of(joke));
        when(jokeRepository.save(any(Joke.class))).thenReturn(joke);
        Joke result = jokeService.update(1L, updatedJoke);
        assertEquals(joke.getText(), result.getText());
        verify(jokeRepository).save(joke);
    }

    @Test
    public void testUpdateThrowsNoResultException() {
        when(jokeRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(NoResultException.class, () ->
                jokeService.update(1L, TestJokeFactory.jokeUpdateModel()));
    }

    @Test
    public void testDelete() throws NotAcceptableStatusException {
        Joke joke = TestJokeFactory.createJoke();
        joke.setId(1L);
        when(jokeRepository.findById(any(Long.class))).thenReturn(Optional.of(joke));
        jokeService.delete(1L);
        verify(jokeRepository).delete(joke);
    }

    @Test
    public void testDeleteThrowsNoResultException() {
        when(jokeRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(NoResultException.class, () -> jokeService.delete(1L));
    }
}