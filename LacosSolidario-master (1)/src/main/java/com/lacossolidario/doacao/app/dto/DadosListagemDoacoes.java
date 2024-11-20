package com.lacossolidario.doacao.app.dto;


import com.lacossolidario.doacao.domain.Doacao;
import com.lacossolidario.doacao.domain.Endereco;

public record DadosListagemDoacoes (

        Long id,
        String categoria,
        String descricao,
        String data

) {

        public DadosListagemDoacoes(Doacao doacao) {
                this(doacao.getId(), doacao.getCategoria(), doacao.getDescricao(), doacao.getData());
        }
}

