package com.calculadora.restservice;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.calculadora.restservice.dto.Greeting;

@RestController
public class GreetingController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}
	
	
	@GetMapping("/calculadora")
	public Greeting calcular(@RequestParam(value = "expresion", defaultValue = "World") String request) {
		
		// TODO: validar si los caracteres son validos 0-9, +-/*^()
		
		// TODO: si existen, validar que se cierren todos los parentesis
		
		// TODO: 
		
		return new Greeting(counter.incrementAndGet(), String.format(template, request));
		
		// return "Calculando";
	}
}
