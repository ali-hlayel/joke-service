#Introduction
This is the challenge project of Ali Hlayel, created by the requirements defined in the sent challenge. 
It contains 1 microservice which is implemnted by me. 
I used IntelliJ IDE for development. 
Infrastructure is created over Spring Boot, Postgres,  and RESTFul services.

#Joke service
Article microservice is built as RESTFul API. it contains methods about joke details:

One is for retrieving all jokes.
Second one search a list of jokes for a text.
Third one is to get joke by Id. 
Forth one is to get a random joke.
Fifth one create a new joke.
Sixth update joke.
And last one is to delete an article. 
# Getting Started
The joke-service is built using docker. 
The root container contains the Dockerfile which java 11 container and copy the joke jar files inside. 
Also the docker-compose.yml describes the configuration of service with its postgress dependency.
The components will be run in a local environment. To run the service execute execute ./build-all.sh shell script in the root directory of project in the terminal.

Test
The code is already tested using Junit test. you can find the tests for the controller and the article serviceImpl. I also used swagger API V3 for testing the project at the run time process. 
you can test the service using swagger by entering the following link:http://localhost:8080/swagger-ui/index.html?url=/v3/api-docs

As shown in the attached figure, I built 7 controllers endpoints.

1. Create a Joke URL: http://localhost:8080/joke-service/joke
Method: Post
2. Update a Joke URL: http://localhost:8080/joke-service/joke/{id}
Method: Patch
3. Delete a Joke URL: http://localhost:8080/joke-service/joke/{id}
Method: Delete
4. Get all Jokes URL: http://localhost:8080/joke-service/jokes?limit=10
Method: Get
5: Get Joke by Id URL: http://localhost:8080/joke-service/{id}
Method: Get
6. Get Random Joke URL: http://localhost:8080/joke-service
Method: Get
7. Get Joke by searching text: http://localhost:8080/joke-service/jokes/{text}

