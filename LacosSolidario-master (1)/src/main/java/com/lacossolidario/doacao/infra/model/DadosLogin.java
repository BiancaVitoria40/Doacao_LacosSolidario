package com.lacossolidario.doacao.infra.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DadosLogin (

	 @Email
     String login,
     @NotBlank
     String senha
	
	
) {
	
	public String getsenha() {
		return senha;
	}


	public String login() {
		return login;
	}
	
}
