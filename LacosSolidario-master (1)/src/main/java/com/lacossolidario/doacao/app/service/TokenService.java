package com.lacossolidario.doacao.app.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.lacossolidario.doacao.domain.Usuario;


@Service
public class TokenService {

	@Value("{api.security.token.secret}")
	private String secret;

	public String gerarToken(Usuario usuario) {

		if (usuario == null) {
			return null;
		}

		try {
			Algorithm algorihtm = Algorithm.HMAC256(secret);
			return JWT.create().withIssuer("scheduling")
					.withSubject(usuario.getLogin())
					.withClaim("id", usuario.getId())
					.sign(algorihtm);

		} catch (JWTCreationException exception) {
			throw new RuntimeException("Error while generating token", exception);

		}
	}

	public String validateToken(String token) {

		try {
			Algorithm algorihtm = Algorithm.HMAC256(secret);
			return JWT.require(algorihtm)
					.withIssuer("scheduling")
					.build()
					.verify(token)
					.getSubject();

		} catch (JWTVerificationException exceptionVerification) {
			return "erro";

		}
	}
	
	
}
