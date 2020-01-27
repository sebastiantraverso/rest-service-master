package com.calculadora.restservice.dto;

import java.io.Serializable;

public class CalculadoraResponse implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 7440663485648990050L;
	private String mensaje;


	public CalculadoraResponse(){}
	
	public CalculadoraResponse( String mensaje ){
		this.mensaje = mensaje;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
}


