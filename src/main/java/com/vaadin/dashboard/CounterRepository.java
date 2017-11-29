package com.vaadin.dashboard;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * CounterRepository updates the field in mongodb
 * Spring data come with many findBy queries
 */
public interface CounterRepository extends MongoRepository<Counter, String> {
    public Counter findByPage(String page);
}
