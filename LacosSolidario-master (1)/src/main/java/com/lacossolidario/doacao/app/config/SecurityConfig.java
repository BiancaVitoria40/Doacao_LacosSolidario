package com.lacossolidario.doacao.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.lacossolidario.doacao.infra.repository.SecurityFilter;

@Configuration
@EnableWebSecurity
@EnableWebMvc
public class SecurityConfig {

	@Autowired
	SecurityFilter securityFilter;



	public SecurityConfig(SecurityFilter securityFilter) {
		super();
		this.securityFilter = securityFilter;
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();

	}

	@SuppressWarnings("removal")
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable())
				.cors().and()
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(HttpMethod.POST, "/usuario/cadastro").permitAll()
						.requestMatchers(HttpMethod.OPTIONS, "//*").permitAll()
						.requestMatchers(HttpMethod.POST, "/usuario/login").permitAll()
						.requestMatchers(HttpMethod.POST, "/doacao/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/usuario/cadastro/*").permitAll()
						.requestMatchers(HttpMethod.GET, "usuario/**").permitAll()
						.requestMatchers(HttpMethod.PUT, "usuario/**").permitAll()
						.anyRequest().authenticated())

				.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class).build();

	}



}
