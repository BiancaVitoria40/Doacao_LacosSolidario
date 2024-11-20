package com.lacossolidario.doacao.app.service;

import com.lacossolidario.doacao.domain.Instituicao;
import com.lacossolidario.doacao.infra.model.DadosCadastroUsuario;
import com.lacossolidario.doacao.infra.repository.InstituicaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.lacossolidario.doacao.domain.Usuario;
import com.lacossolidario.doacao.infra.repository.UsuarioRepository;

@Service
public class DoadorService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

    @Autowired
    private InstituicaoRepository instituicaoRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    public Usuario criaUsuario(DadosCadastroUsuario dados) {
        Usuario novoUsuario = new Usuario(dados);

        if (usuarioRepository.existsUsuarioByCpf(novoUsuario.getCpf())) {
            throw new IllegalArgumentException("Já temos um usuário registrado com este CPF. Por favor, verifique os dados e tente novamente!");
        }

        String encryptedPassword = passwordEncoder.encode(novoUsuario.getSenha());
        novoUsuario.setSenha(encryptedPassword);

        return usuarioRepository.save(novoUsuario);
    }


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return usuarioRepository.findByLogin(username);
	}

    public Instituicao obterInstituicao() {
        return instituicaoRepository.findById(1L) // Assume que a única instituição tem ID 1
                .orElseThrow(() -> new RuntimeException("Instituição não encontrada"));
    }




}
