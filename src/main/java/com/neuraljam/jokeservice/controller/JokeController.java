package com.neuraljam.jokeservice.controller;

import com.neuraljam.jokeservice.exceptions.ConflictException;
import com.neuraljam.jokeservice.exceptions.EntityAlreadyExistsException;
import com.neuraljam.jokeservice.exceptions.ServiceResponseException;
import com.neuraljam.jokeservice.model.Joke;
import com.neuraljam.jokeservice.model.JokeModel;
import com.neuraljam.jokeservice.service.JokeService;

import io.swagger.v3.oas.annotations.Operation;
import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/joke-service", produces = MediaType.APPLICATION_JSON_VALUE)
public class JokeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JokeController.class);

    private JokeService jokeService;

    @Autowired
    public JokeController(JokeService jokeService) {
        this.jokeService = jokeService;
    }

    @Operation(summary = "Get all jokes")
    @GetMapping(value = "/jokes")
    public ResponseEntity<List<Joke>> getAllJokes(@RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<Joke> results = jokeService.getAllJokes(page, limit);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @Operation(summary = "Creates new joke")
    @PostMapping("/joke")
    public ResponseEntity<Joke> createJoke(@Valid @RequestBody JokeModel jokeModel) throws ServiceResponseException {
        Joke joke = new Joke();
        BeanUtils.copyProperties(jokeModel, joke);
        Joke result;
        try {
            result = jokeService.create(joke);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (EntityAlreadyExistsException e) {
            String message = "Could not create joke: " + e.getMessage();
            LOGGER.error(message, e);
            throw new ConflictException(message, e);
        }
    }

    @Operation(summary = "Get random Joke")
    @GetMapping()
    public ResponseEntity<JokeModel> getRandomJoke() throws NotFoundException {
        Joke result;
        JokeModel jokeModel;
        try {
            result = jokeService.getRandomJoke();
            ModelMapper modelMapper = new ModelMapper();
            jokeModel = modelMapper.map(result, JokeModel.class);
            return new ResponseEntity<>(jokeModel, HttpStatus.OK);
        } catch (NoResultException e) {
            String message = "Could not get a Book: " + e.getMessage();
            LOGGER.error(message, e);
            throw new NotFoundException(message, e);
        }
    }
}
