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
	public String calcular(@RequestParam(value = "expresion", defaultValue = "World") String request) {
		
		// TODO: validar si los caracteres son validos 0-9, +-/*^()
		final String caractaresValidos = "0123456789./*-+()^";
		final char[] caractaresValidos_c = {'0','1','2','3','4','5','6','7','8','9','.','/','*','-','+','(',')','^'};
		
		char[] arrCharRequest = request.toCharArray();
		try {
			for (char c : arrCharRequest) {
				System.out.println(c);
				if( caractaresValidos.indexOf(c) == -1 ) {
					throw new Exception("El caracter " + c + " NO es valido");
				}
			}	
		}
		catch( Exception e) {
			return e.getMessage().toString();
		}
		
				
				
		// TODO: si existen, validar que se cierren todos los parentesis
		
		// TODO: 
		
		return  String.format(template, request);
		
		// return "Calculando";
	}
}
