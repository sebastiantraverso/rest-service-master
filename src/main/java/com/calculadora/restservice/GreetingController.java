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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;






@RestController
public class GreetingController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	
	Connection connection = null;
	private static AtomicLong contadorExpresiones =  new AtomicLong();


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
			System.out.println("Llamando a connect");
			connect();
			System.out.println("Fin de connect");
			
			System.out.println("contadorExpresiones: " + contadorExpresiones.get());
			if( contadorExpresiones.get() < 1 ) {
				limpiarBase();
			}

			
			
			// validar si los caracteres son validos 0-9, +-/*^()
				final String caractaresValidos = "0123456789./*-+()^ lognguardarsesionrecuperarsesionqt";
				int contadorCaracter = 0;
				
				final char[] arrCharRequest = expresion.toCharArray();
				for (final char c : arrCharRequest) {
					// System.out.println(c);
					contadorCaracter++;
					if( caractaresValidos.indexOf(c) == -1 ) {
						throw new CalculadoraException( request.getEntrada(), "El caracter '" + c + "' NO es valido - nro " + contadorCaracter+1);
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
					throw new CalculadoraException( request.getEntrada(), "Hay mas aperturas de ( que cierres" );
				if( contadorCierre > contadorApertura)
					throw new CalculadoraException( request.getEntrada(), "Hay mas cierres de ) que aperturas" );
	

			// validar si la expresion es "sesionXX" para buscar en la base la misma
			double resultado = 0L;
			CalculadoraResponse out;
			if( expresion.startsWith("sesion") ){
				try{
					out = cargarCalculo(expresion);
				}
				catch( Exception e ){
					throw new CalculadoraException( request.getEntrada(), "Error al buscar la sesion");
				}
				return new CalculadoraResponse( out.getInput(), out.getOutput() );
			} else {				
				// dividir en expresiones			
				contadorExpresiones.incrementAndGet();	
				try{
					resultado = Util.eval(expresion);
				}
				catch( Exception e ){
					throw new CalculadoraException( request.getEntrada(), "Error en la expresion");
				}
				
				// guardar la sesion en una base
				String nombreSesion = "sesion"+contadorExpresiones.get();
				guardarCalculo( nombreSesion, expresion,  Double.toString(resultado) );
				
				return new CalculadoraResponse( expresion, Double.toString(resultado) );
			}

		}
		catch( final CalculadoraException e) {
			return new CalculadoraResponse(expresion, e.getmensaje());
		}
		
	}


	private void connect() {
		try {
			String url = "jdbc:sqlite:calcularoda.db";
			this.connection = DriverManager.getConnection(url);
			System.out.println("Conexion con SQLite OK");

		} catch (SQLException e) {
			System.out.println("Conexion con SQLite ERROR, " + e.getErrorCode() + " - " + e.getMessage());
		}
	}


	private void limpiarBase() {
		// TODO: borrar los datos de la tabla
		String sql = "DELETE FROM sessiones";

		try (Connection connection = this.connection;
			PreparedStatement prStatement = connection.prepareStatement(sql)){
			prStatement.executeUpdate();
			System.out.println("tabla sessiones limpia");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	private void guardarCalculo( String nombre, String expresion, String resultado){
		connect();    
		String sql = "INSERT INTO sessiones (nombre, input, output) VALUES (?, ?, ?)";
		
		try (Connection connection = this.connection;
			PreparedStatement prStatement = connection.prepareStatement(sql)){
			prStatement.setString(1, nombre);
			prStatement.setString(2, expresion);
			prStatement.setString(3, resultado);
			prStatement.executeUpdate();
			System.out.println("Transaccion " + nombre + " guardada!!");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}


	private CalculadoraResponse cargarCalculo( String nombreSesion) throws Exception{
		
		CalculadoraResponse out = new CalculadoraResponse ("", "");
		connect();
		String sql = "SELECT input, output FROM sessiones WHERE nombre LIKE '" + nombreSesion + "'";
		System.out.println("sql: " + sql);
		
		try {
			Connection connection = this.connection;
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			
			if( rs.next() ){
				out.setInput(rs.getString("input")); 
				out.setOutput(rs.getString("output")); 
			} else {
				throw new CalculadoraException( "", "No Existe una sesion con ese nombre");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		
		return out;
	}


	private void disconnect() {        
		try {
        	if (connection != null) {
                connection.close();
                System.out.println("Connection to SQLite has been closed.");
            }
        } catch (SQLException ex) {
        	System.out.println("ConeXion con SQLite FINALIZADA.");
        }
    }
}
