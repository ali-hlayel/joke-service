package com.neuraljam.jokeservice.controller;

import com.neuraljam.jokeservice.exceptions.BadRequestException;
import com.neuraljam.jokeservice.exceptions.ConflictException;
import com.neuraljam.jokeservice.exceptions.EntityAlreadyExistsException;
import com.neuraljam.jokeservice.exceptions.ServiceResponseException;
import com.neuraljam.jokeservice.exceptions.NotFoundException;

import com.neuraljam.jokeservice.model.Joke;
import com.neuraljam.jokeservice.model.JokeCreateModel;
import com.neuraljam.jokeservice.model.JokeModel;
import com.neuraljam.jokeservice.model.JokeUpdateModel;
import com.neuraljam.jokeservice.service.JokeService;

import io.swagger.v3.oas.annotations.Operation;
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
import javax.validation.constraints.Min;
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

    @Operation(summary = "Get all jokes by text")
    @GetMapping(value = "/jokes/{text}")
    public ResponseEntity<List<Joke>> getAllJokesByText(@Valid @PathVariable String text) throws ServiceResponseException {
        try {
            List<Joke> results = jokeService.getAllJokesByText(text);
            return new ResponseEntity<>(results, HttpStatus.OK);
        } catch (NoResultException e) {
            String message = "Could not get a Joke: " + e.getMessage();
            LOGGER.error(message, e);
            throw new NotFoundException(message, e);
        }

    }

    @Operation(summary = "Creates new joke")
    @PostMapping("/joke")
    public ResponseEntity<Joke> createJoke(@Valid @RequestBody JokeCreateModel jokeCreateModel) throws ServiceResponseException {
        Joke joke = new Joke();
        BeanUtils.copyProperties(jokeCreateModel, joke);
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
            String message = "Could not get a Joke: " + e.getMessage();
            LOGGER.error(message, e);
            throw new NotFoundException(message, e);
        }
    }

    @Operation(summary = "Get Joke by Id")
    @GetMapping("/{id}")
    public ResponseEntity<JokeModel> getJokeById(@Min(value = 1) @PathVariable Long id) throws ServiceResponseException {
        Joke result;
        JokeModel jokeModel;
        try {
            result = jokeService.getById(id);
            ModelMapper modelMapper = new ModelMapper();
            jokeModel = modelMapper.map(result, JokeModel.class);
            return new ResponseEntity<>(jokeModel, HttpStatus.OK);
        } catch (NoResultException e) {
            String message = "Could not find joke with id " + id + ": " + e.getMessage();
            LOGGER.error(message, e);
            throw new NotFoundException(message, e);
        }
    }

    @Operation(summary = "Update a single joke")
    @PatchMapping("/joke/{id}")
    public ResponseEntity<Joke> updateJoke(@Min(value = 1) @PathVariable Long id, @Valid @RequestBody JokeUpdateModel jokeUpdateModel
    ) throws ServiceResponseException {
        Joke result;
        try {
            result = jokeService.update(id, jokeUpdateModel);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (NoResultException e) {
            String message = "Could not update a joke: " + e.getMessage();
            LOGGER.error(message, e);
            throw new BadRequestException(message, e);
        }
    }

    @Operation(summary = "Deletes a single joke by Id")
    @DeleteMapping("/joke/{id}")
    public ResponseEntity<Joke> deleteJoke(@Min(value = 1) @PathVariable Long id) throws ServiceResponseException {

        try {
            jokeService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NoResultException e) {
            String message = "Could not delete joke with id " + id + ": " + e.getMessage();
            LOGGER.error(message, e);
            throw new BadRequestException(message, e);
        }

    }
}
