package com.calculadora.restservice;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.calculadora.restservice.dto.CalculadoraException;
import com.calculadora.restservice.dto.CalculadoraRequest;
import com.calculadora.restservice.dto.Greeting;

@RestController
public class GreetingController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}
	
	// TODO: Limpiar el codigo de comentarios extras
	
	@GetMapping("/calculadora")
	public CalculadoraRequest calcular(@RequestParam(value = "expresion", defaultValue = "_") String request) {
		
		System.out.println("entrada -" + request + "-");
		
		try {
			// validar si los caracteres son validos 0-9, +-/*^()
				final String caractaresValidos = "0123456789./*-+()^";
				int contadorCaracter = 0;
				// final char[] caractaresValidos_c = {'0','1','2','3','4','5','6','7','8','9','.','/','*','-','+','(',')','^'};
				
				char[] arrCharRequest = request.toCharArray();
				for (char c : arrCharRequest) {
					// System.out.println(c);
					contadorCaracter++;
					if( caractaresValidos.indexOf(c) == -1 ) {
						throw new CalculadoraException("El caracter '" + c + "' NO es valido - nro " + contadorCaracter+1);
					}
				}	


			// si existen, validar que se cierren todos los parentesis
				// contar los (
				int posicionApertura = 0;
				int contadorApertura = 0;
				
				posicionApertura = request.indexOf('(');
				while( posicionApertura != -1){
					contadorApertura++;
					posicionApertura = request.indexOf('(', posicionApertura + 1);
				}
				
				// contar los )
				int posicionCierre = 0;
				int contadorCierre = 0;
				
				posicionCierre = request.indexOf(')');
				while( posicionCierre != -1){
					contadorCierre++;
					posicionCierre = request.indexOf(')', posicionCierre + 1);
				}
				
				// validar que sean la misma cantidad
				if( contadorApertura > contadorCierre)
					throw new CalculadoraException("Hay mas aperturas de ( que cierres");
				if( contadorCierre > contadorApertura)
					throw new CalculadoraException("Hay mas cierres de ) que aperturas");

			
			
			return new CalculadoraRequest("Cantidad de ) " + contadorCierre);
				
		}
		catch( CalculadoraException e) {
			return new CalculadoraRequest(e.getmensaje());
		}
		
		
		// return new CalculadoraRequest("Resultado TODO");
	}
}
