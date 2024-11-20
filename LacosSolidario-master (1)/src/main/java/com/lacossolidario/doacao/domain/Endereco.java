package com.lacossolidario.doacao.domain;

import com.lacossolidario.doacao.infra.model.DadosEndereco;

import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {

    private String logradouro;
    private String cep;
    private String numero;
  
    public Endereco(DadosEndereco dados) {
        this.logradouro = dados.logradouro();
        this.cep = dados.cep();
        this.numero = dados.numero();
    }

    public void atualizarEndereco(DadosEndereco dados) {
        if(dados.logradouro() != null) {
            this.logradouro = dados.logradouro();
        }
        if(dados.cep() != null) {
            this.cep = dados.cep();
        }
        if(dados.numero() != null) {
            this.numero = dados.numero();
        }
    }
}
