package com.example.client_vaadin;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CounterRepository extends MongoRepository<Counter, String> {
    public Counter findByPage(String page);
}
