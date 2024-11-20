package com.lacossolidario.doacao.infra.repository;


import com.lacossolidario.doacao.domain.Doacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoacaoRepository extends JpaRepository<Doacao, Long> {


}
