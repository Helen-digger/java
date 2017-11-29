package com.vaadin.dashboard;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = Counter.COLLECTION_NAME)

/**
 *
 */
public class Counter implements Serializable{

    public static final String COLLECTION_NAME = "counter";

    @Id
    public String id;

    private String page;
    public String value;

    /**
     *
     * @param page
     * @param value
     */
    public Counter(String page, String value){
        this.page = page;
        this.value = value;

    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return String.format(
                "Counter[id=%s, page='%s', value='%s']",
                id, page, value);
    }

}
