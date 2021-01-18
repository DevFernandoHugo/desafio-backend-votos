package com.desafio.sessao.service;

import java.util.List;

import com.desafio.sessao.entity.Pauta;
import com.desafio.sessao.model.PautaVo;

import reactor.core.publisher.Mono;

public interface IPautaService {

	Mono<List<Pauta>> consultarPautas();

	Mono<Boolean> consultarPautaExistente(Long id);

	Mono<Pauta> consultarPautaPorId(Long id);

	Mono<Boolean> registrarPauta(PautaVo descricao);

	Mono<Boolean> verificarPautaSessaoAberta(Long codigoPautaSessao);

	Mono<List<Pauta>> consultarPautasComSessaoAberta();
	
	

}
