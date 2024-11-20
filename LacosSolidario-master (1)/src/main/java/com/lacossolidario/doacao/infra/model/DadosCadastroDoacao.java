package com.lacossolidario.doacao.infra.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroDoacao (
        @NotBlank
        String categoria,
        @NotBlank
        String descricao,

        String data,

        @NotNull
        @Valid
        DadosEndereco endereco

){
}
