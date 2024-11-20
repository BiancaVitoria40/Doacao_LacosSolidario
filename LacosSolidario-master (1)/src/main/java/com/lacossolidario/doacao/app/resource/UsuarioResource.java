package com.lacossolidario.doacao.app.resource;

import java.util.Optional;

import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.lacossolidario.doacao.app.dto.DadosAtualizacaoUsuario;
import com.lacossolidario.doacao.app.dto.DadosListagemUsuario;
import com.lacossolidario.doacao.domain.Usuario;
import com.lacossolidario.doacao.infra.model.DadosCadastroUsuario;
import com.lacossolidario.doacao.infra.model.DadosLogin;
import com.lacossolidario.doacao.infra.repository.UsuarioRepository;
import com.lacossolidario.doacao.app.service.DoadorService;
import com.lacossolidario.doacao.app.service.TokenService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuario")
public class UsuarioResource {

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private DoadorService usuarioService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenService tokenService;



	@PostMapping("/cadastro")
	@Transactional
	public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody @Valid DadosCadastroUsuario dados,
													UriComponentsBuilder uriBuilder) {
		Usuario usuario = usuarioService.criaUsuario(dados);

		var uri = uriBuilder.path("/usuario/{id}").buildAndExpand(usuario.getId()).toUri();
		return ResponseEntity.created(uri).body(usuario);
	}

	@PostMapping
	@RequestMapping("/login")
	@Transactional
	public ResponseEntity login(@RequestBody @Valid DadosLogin data) throws Exception {

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				data.login(), data.senha());

		Authentication auth = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);

		var token = tokenService.gerarToken((Usuario) auth.getPrincipal());

		if (token == null) {
			throw new Exception("Senha ou email invalidos");
			// return null;
		} else {
			Usuario usuario = (Usuario) auth.getPrincipal();
			Long userId = usuario.getId();

			return ResponseEntity.status(HttpStatus.OK).body(userId);
		}
	}


	@GetMapping("/{id}")
	public ResponseEntity detalharPorId(@PathVariable Long id) {
		Optional<Usuario> optionalUsuario = repository.findById(id);

		if (optionalUsuario.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Usuario usuario = optionalUsuario.get();

		if (usuario instanceof HibernateProxy) {
			usuario = (Usuario) ((HibernateProxy) usuario).getHibernateLazyInitializer().getImplementation();
		}

		return ResponseEntity.ok(new DadosListagemUsuario(usuario));
	}


	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Long id, @ModelAttribute @Valid DadosAtualizacaoUsuario dados,
									   @RequestParam(value = "imagem", required = false) MultipartFile imagem) {
		try {
			Optional<Usuario> optionalUsuario = repository.findById(id);

			if (optionalUsuario.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}

			Usuario usuario = optionalUsuario.get();

			if (usuario instanceof HibernateProxy) {
				usuario = (Usuario) ((HibernateProxy) usuario).getHibernateLazyInitializer().getImplementation();
			}

			usuario.atualizarDados(dados);

			// Salva o usuário atualizado
			repository.save(usuario);
			return ResponseEntity.ok(new DadosListagemUsuario(usuario));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao processar a imagem: " + e.getMessage());
		}
	}


	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity desativarUsuario(@PathVariable Long id) {
		var usuario = repository.getReferenceById(id);
		usuario.desativar();

		return ResponseEntity.noContent().build();

	}
}
