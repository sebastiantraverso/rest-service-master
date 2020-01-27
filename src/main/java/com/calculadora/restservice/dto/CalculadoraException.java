package com.calculadora.restservice.dto;

public class CalculadoraException extends Exception {

	/**
     *
     */
    private static final long serialVersionUID = 1L;
	private final String mensaje;
	private String input;

	public CalculadoraException(String input, String mensaje) {
		this.mensaje = mensaje;
		this.input = input;
	}

	public String getmensaje() {
		return mensaje;
	}


	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}
}


