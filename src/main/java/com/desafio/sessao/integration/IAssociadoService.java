package com.desafio.sessao.integration;

import reactor.core.publisher.Mono;

public interface IAssociadoService {

	Mono<Boolean> verificarAssociadoElegivel(String cpf);

}
