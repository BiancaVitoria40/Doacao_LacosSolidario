package com.lacossolidario.doacao.app.dto;

import com.lacossolidario.doacao.domain.Usuario;

public record DadosListagemUsuario(
         Long id,
         String nome,
         String login,
         String senha,
         String telefone,
         String cpf


) {


    public DadosListagemUsuario(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getLogin(),
                usuario.getSenha(),
                usuario.getTelefone(),
                usuario.getCpf()
        );
    }
}
