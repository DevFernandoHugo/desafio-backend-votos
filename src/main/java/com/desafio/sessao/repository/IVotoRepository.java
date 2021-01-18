package com.desafio.sessao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.desafio.sessao.entity.Voto;

@Repository
public interface IVotoRepository extends JpaRepository<Voto, Long> {

	@Query("select v from Voto v where v.cpf = :cpf and v.codigoPauta = :codigoPauta")
	Optional<Voto>  buscarVotosRealizadosPauta(@Param("cpf") String cpf, @Param("codigoPauta") Long codigoPauta);
	
	@Query("select v from Voto v where v.codigoPauta = :codigoPauta")
	List<Voto> buscarVotosPauta(@Param("codigoPauta") Long codigoPauta);

}