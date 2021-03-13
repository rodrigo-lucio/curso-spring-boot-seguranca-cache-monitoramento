package br.com.alura.forum.controller.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TokenDto {

	private String token;
	private String tipo;
	private LocalDateTime dataHora;

	public TokenDto(String token, String tipo) {		
		this.token = token;
		this.tipo = tipo;
		this.dataHora = LocalDateTime.now();
	}

	public String getToken() {
		return token;
	}

	public String getTipo() {
		return tipo;
	}

	public LocalDateTime getDataHora() {
		return dataHora;
	}
	
	

}
