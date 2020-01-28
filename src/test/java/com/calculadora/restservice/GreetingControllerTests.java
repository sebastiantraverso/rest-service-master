/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.calculadora.restservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.calculadora.restservice.dto.CalculadoraRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class GreetingControllerTests {

	@Autowired
	private MockMvc mockMvc;


	@Test
	public void insertarExpresion() throws Exception {
		CalculadoraRequest expresion = new CalculadoraRequest("5+9");
				
		mockMvc.perform( MockMvcRequestBuilders
			.post("/calculadora/")
			.content(asJsonString( expresion ))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.output").exists())
			.andExpect(MockMvcResultMatchers.jsonPath("$.output").value("14.0"));
	}



	@Test
	public void validarInsertarExpresion() throws Exception {
		CalculadoraRequest expresion = new CalculadoraRequest("6+4");

		mockMvc.perform( MockMvcRequestBuilders
			.post("/calculadora/")
			.content(asJsonString( expresion ))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.output").value("10.0"));
	}


	@Test
	public void validarSesion() throws Exception {
		CalculadoraRequest expresion = new CalculadoraRequest("sesion1");

		mockMvc.perform( MockMvcRequestBuilders
			.post("/calculadora/")
			.content(asJsonString( expresion ))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.output").value("14.0"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.input").value("5+9"));
	}





	public static String asJsonString( final Object obj ){
		try{
			return new ObjectMapper().writeValueAsString(obj);
		}
		catch( Exception e){
			throw new RuntimeException();
		}
	}



}
