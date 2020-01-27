package com.calculadora.restservice.dto;

import java.io.Serializable;

public class CalculadoraResponse implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 7440663485648990050L;
	private String output;
	private String input;


	public CalculadoraResponse(){}
	
	// public CalculadoraResponse( String output ){
	// 	new CalculadoraResponse("", output);
	// 	// this.output = output;
	// }

	public CalculadoraResponse( String input, String output ){
		this.input = input;
		this.output = output;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	
	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	@Override
	public String toString(){
		return "Input: " + this.input + " - Output: " + this.output;
	}
	
}


