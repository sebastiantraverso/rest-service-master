package com.calculadora.restservice.dto;

import java.io.Serializable;

public class CalculadoraRequest implements Serializable {

	/**
     *
     */
    private static final long serialVersionUID = -4816712773246799364L;
    private String entrada;


    public CalculadoraRequest() {}

	public CalculadoraRequest(String entrada) {
		this.entrada = entrada;
	}

    public String getEntrada() {
        return entrada;
    }

    public void setEntrada(String entrada) {
        this.entrada = entrada;
    }

}


