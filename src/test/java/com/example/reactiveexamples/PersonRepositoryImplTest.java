package com.example.reactiveexamples;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersonRepositoryImplTest {

    PersonRepositoryImpl personRepository;

    @BeforeEach
    void setUp() {
        personRepository = new PersonRepositoryImpl();
    }

    @Test
    void getByIdBlock() {
        Mono<Person> personMono = personRepository.getById(1);
        Person person = personMono.block();
        System.out.println(person);
    }

    @Test
    void getByIdSubscribe() {
        Mono<Person> personMono = personRepository.getById(1);
        personMono.subscribe(System.out::println);
    }

    @Test
    void getByIdMapFunction() {
        Mono<Person> personMono = personRepository.getById(1);
        personMono.map(person -> {
            System.out.println(person);
            return person.getFirstName();
        }).subscribe(firstName -> {
            System.out.println("from map: " + firstName);
        });
    }

    @Test
    void fluxTestBlockFirst() {
        Flux<Person> personFlux = personRepository.findAll();
        Person person = personFlux.blockFirst();
        System.out.println(person);
    }


    @Test
    void testFluxSubscribe() {
        Flux<Person> personFlux = personRepository.findAll();
        personFlux.subscribe(System.out::println);
    }

    @Test
    void testFluxToListMono() {
        Flux<Person> personFlux = personRepository.findAll();
        Mono<List<Person>> personListMono = personFlux.collectList();
        personListMono.subscribe(list -> {
            list.forEach(System.out::println);
        });
    }

    @Test
    void testFindPersonById() {
        Flux<Person> personFlux = personRepository.findAll();
        final Integer id = 3;
        Mono<Person> personMono = personFlux.filter(person -> person.getId() == id).next();
        personMono.subscribe(System.out::println);
    }

    @Test
    void testFindPersonByIdNotFound() {
        Flux<Person> personFlux = personRepository.findAll();
        final Integer id = 8;
        Mono<Person> personMono = personFlux.filter(person -> person.getId() == id).next();
        personMono.subscribe(System.out::println);
    }

    @Test
    void testFindPersonByIdNotFoundWithException() {
        Flux<Person> personFlux = personRepository.findAll();
        final Integer id = 8;
        Mono<Person> personMono = personFlux.filter(person -> person.getId() == id).single();
        personMono.doOnError(throwable -> {
            System.out.println("I went boom");
        }).onErrorReturn(Person.builder().id(id).build()).subscribe(System.out::println);
    }
}