package com.calculadora.restservice;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.calculadora.restservice.dto.CalculadoraException;
import com.calculadora.restservice.dto.CalculadoraRequest;
import com.calculadora.restservice.dto.CalculadoraResponse;
import com.calculadora.restservice.dto.Greeting;
import com.calculadora.restservice.util.Util;


@RestController
public class GreetingController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") final String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}
	
	// TODO: Limpiar el codigo de comentarios extras
	
	// @GetMapping("/calculadora")
	// public CalculadoraRequest calcular(@RequestParam(value = "expresion", defaultValue = "_") String request) {
	@RequestMapping(value = "/calculadora", method = RequestMethod.POST)
	public CalculadoraResponse calcular(@RequestBody final CalculadoraRequest request) {

		// public InsertEcomTransactionsResponse imputePayment(@RequestBody final InsertEcomTransactionsRequest request)
		
		final String expresion = request.getEntrada();

		
		System.out.println("entrada -" + expresion + "-");
		
		try {
			// validar si los caracteres son validos 0-9, +-/*^()
				final String caractaresValidos = "0123456789./*-+()^ lognguardarsesionrecuperarsesionqt";
				int contadorCaracter = 0;
				
				final char[] arrCharRequest = expresion.toCharArray();
				for (final char c : arrCharRequest) {
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
				
				posicionApertura = expresion.indexOf('(');
				while( posicionApertura != -1){
					contadorApertura++;
					posicionApertura = expresion.indexOf('(', posicionApertura + 1);
				}
				
				// contar los )
				int posicionCierre = 0;
				int contadorCierre = 0;
				
				posicionCierre = expresion.indexOf(')');
				while( posicionCierre != -1){
					contadorCierre++;
					posicionCierre = expresion.indexOf(')', posicionCierre + 1);
				}
				
				// validar que sean la misma cantidad
				if( contadorApertura > contadorCierre)
					throw new CalculadoraException("Hay mas aperturas de ( que cierres");
				if( contadorCierre > contadorApertura)
					throw new CalculadoraException("Hay mas cierres de ) que aperturas");

			
			// dividir en expresiones
			double resultado;
			try{
				resultado = Util.eval(expresion);
			}
			catch( Exception e ){
				throw new CalculadoraException("Error en la expresion");
			}


			// guardar la sesion en una base
			
			
			// return new CalculadoraResponse("Cantidad de ) " + contadorCierre);

			return new CalculadoraResponse("Resultado: " +  resultado);
				
		}
		catch( final CalculadoraException e) {
			return new CalculadoraResponse(e.getmensaje());
		}
		
	}
}
