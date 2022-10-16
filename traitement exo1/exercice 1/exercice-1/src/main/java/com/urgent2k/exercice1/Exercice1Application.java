package com.urgent2k.exercice1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.transaction.Transactional;

@SpringBootApplication
@Transactional
public class Exercice1Application {

	public static void main(String[] args) {
		SpringApplication.run(Exercice1Application.class, args);
	}

}
