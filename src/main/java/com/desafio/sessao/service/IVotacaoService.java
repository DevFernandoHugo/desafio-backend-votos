package com.desafio.sessao.service;

import com.desafio.sessao.model.VotacaoVo;

import reactor.core.publisher.Mono;

public interface IVotacaoService {

	Mono<VotacaoVo> recuperarVotacaoPauta(Long codigoPauta);


}
