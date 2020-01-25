package com.calculadora.restservice.dto;

public class CalculadoraException extends Exception {

	/**
     *
     */
    private static final long serialVersionUID = 1L;
    private final String mensaje;

	public CalculadoraException(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getmensaje() {
		return mensaje;
	}
}


