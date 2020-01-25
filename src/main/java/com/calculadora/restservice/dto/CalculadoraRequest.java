package com.calculadora.restservice.dto;

public class CalculadoraRequest {

	private final String mensaje;

	public CalculadoraRequest(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getmensaje() {
		return mensaje;
	}
}


