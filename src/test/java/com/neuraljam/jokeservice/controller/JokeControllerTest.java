package com.neuraljam.jokeservice.controller;

import com.neuraljam.jokeservice.exceptions.EntityAlreadyExistsException;
import com.neuraljam.jokeservice.exceptions.GlobalExceptionHandler;
import com.neuraljam.jokeservice.model.Joke;
import com.neuraljam.jokeservice.model.JokeCreateModel;
import com.neuraljam.jokeservice.model.JokeUpdateModel;
import com.neuraljam.jokeservice.model.TestJokeFactory;
import com.neuraljam.jokeservice.service.JokeService;import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class JokeControllerTest {

    @Mock
    private JokeService jokeService;

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private final String LINK = "/joke-service";


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new JokeController(jokeService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        this.mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
    }

    @Test
    void getAllJokes() throws Exception {
        Joke firstJoke = TestJokeFactory.createJoke();
        firstJoke.setId(1L);
        Joke secondJoke = TestJokeFactory.createJoke();
        secondJoke.setId(2L);

        List<Joke> jokes = Arrays.asList(firstJoke, secondJoke);
        when(jokeService.getAllJokes(0, 2)).thenReturn(jokes);
        this.mockMvc.perform(get(LINK + "/jokes"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllJokesByText() {
    }

    @Test
    void createJoke() throws Exception {
        JokeCreateModel jokeCreateModel = TestJokeFactory.jokeCreateModel();
        when(jokeService.create(any(Joke.class))).thenReturn(TestJokeFactory.createJoke());
        this.mockMvc.perform(post(LINK + "/joke")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json(jokeCreateModel)))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateJokeWithAlreadyExistingText() throws Exception {
        Joke createModel = TestJokeFactory.createJoke();
        when(jokeService.create(any(Joke.class))).thenThrow(EntityAlreadyExistsException.class);
        this.mockMvc.perform(post(LINK + "/joke")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json(createModel)))
                .andExpect(status().isConflict());
    }

    @Test
    void getRandomJoke() {
    }

    @Test
    void getJokeById() throws Exception {
        Joke joke = TestJokeFactory.createJoke();
        joke.setId(1L);
        when(jokeService.getById(any(Long.class))).thenReturn(joke);
        this.mockMvc.perform(get(LINK + "/1").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        verify(jokeService).getById(1L);
    }

    @Test
    void testGetJokeByIdThrowNotFoundException() throws Exception {
        when(jokeService.getById(any(Long.class))).thenThrow(NoResultException.class);
        this.mockMvc.perform(get(LINK + "/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateJoke() throws Exception {
        JokeUpdateModel jokeUpdateModel = TestJokeFactory.jokeUpdateModel();
        Joke joke = TestJokeFactory.createJoke();
        joke.setId(1L);
        when(jokeService.update(any(Long.class), any(JokeUpdateModel.class))).thenReturn(joke);
        mockMvc.perform(patch(LINK + "/joke/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json(jokeUpdateModel)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateSpsThrowsNoResultException() throws Exception {
        JokeUpdateModel jokeUpdateModel = TestJokeFactory.jokeUpdateModel();
        when(jokeService.update(any(Long.class), any(JokeUpdateModel.class))).thenThrow(NoResultException.class);
        mockMvc.perform(patch(LINK + "/joke/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json(jokeUpdateModel)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteJoke() {
    }

    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}