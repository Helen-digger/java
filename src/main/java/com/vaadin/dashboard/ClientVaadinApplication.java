package com.vaadin.dashboard;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientVaadinApplication {
	private static final Logger logger = LogManager.getLogger();

	public static void main(String[] args)  {
		SpringApplication.run(ClientVaadinApplication.class, args);

	}
}
