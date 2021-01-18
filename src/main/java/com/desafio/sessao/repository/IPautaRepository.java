package com.desafio.sessao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desafio.sessao.entity.Pauta;

@Repository
public interface IPautaRepository extends JpaRepository<Pauta, Long> {
}
