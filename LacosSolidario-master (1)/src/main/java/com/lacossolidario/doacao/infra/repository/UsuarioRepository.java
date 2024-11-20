package com.lacossolidario.doacao.infra.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lacossolidario.doacao.domain.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsUsuarioByCpf(String cpf);

    Usuario findByLogin(String login);
}

