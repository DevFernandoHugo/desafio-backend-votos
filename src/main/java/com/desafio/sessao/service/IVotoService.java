package com.desafio.sessao.service;

import java.util.List;

import com.desafio.sessao.entity.Voto;

import reactor.core.publisher.Mono;

public interface IVotoService {

	Mono<Boolean> criarVoto(Voto voto);
	
	Mono<Boolean> verificarVotoAssociadoPauta(Voto voto);

	Mono<List<Voto>> buscarVotosPorPauta(Long codigoPauta);

}
