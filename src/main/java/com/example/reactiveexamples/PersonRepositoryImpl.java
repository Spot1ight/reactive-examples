package com.example.reactiveexamples;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PersonRepositoryImpl implements PersonRepository{

    Person mike = new Person(1, "Mike");
    Person fiona = new Person(2, "Fiona");
    Person sem = new Person(3, "Sam");
    Person jessy = new Person(4, "Jessy");


    @Override
    public Mono<Person> getById(Integer id) {
        return Mono.just(mike);
    }

    @Override
    public Flux<Person> findAll() {
        return Flux.just(mike, fiona, sem, jessy);
    }
}
