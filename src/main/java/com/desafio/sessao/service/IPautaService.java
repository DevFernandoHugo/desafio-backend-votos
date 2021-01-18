package com.desafio.sessao.service;

import java.util.List;

import com.desafio.sessao.entity.Pauta;

import reactor.core.publisher.Mono;

public interface IPautaService {

	Mono<List<Pauta>> consultarPautas();
	
	

}
