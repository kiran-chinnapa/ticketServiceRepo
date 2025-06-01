package com.assesment.ticketserviceapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TicketServiceAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketServiceAppApplication.class, args);
	}

}
