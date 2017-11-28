package com.vaadin.dashboard;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CounterRepository extends MongoRepository<Counter, String> {
    public Counter findByPage(String page);
}
