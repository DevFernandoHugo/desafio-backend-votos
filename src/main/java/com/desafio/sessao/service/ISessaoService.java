package com.desafio.sessao.service;

import java.util.List;

import com.desafio.sessao.entity.Sessao;

import reactor.core.publisher.Mono;

public interface ISessaoService {

	Mono<Boolean> salvarSessao(Sessao sessao);

}
