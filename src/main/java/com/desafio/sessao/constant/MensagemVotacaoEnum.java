package com.desafio.sessao.constant;

import lombok.Getter;

@Getter
public enum MensagemVotacaoEnum {

	PAUTA_NAO_ENCONTRADA("Pauta(s) não encontrada(s)."),
	PAUTA_DESCRICAO_INVALIDA("Pauta descrição inválido,verifique o parâmetro de entrada."),
	PAUTA_NAO_ENCONTRADA_SESSAO("Pauta não encotrada, porfavor repasse um código de pauta existente."),
	PAUTA_SESSAO_NAO_ABERTA("Pauta não possui sessão aberta,favor abrir uma sessão para a mesma."),
	
	SESSAO_NAO_ENCONTRADA("Sessão(s) não encontrada(s)."),
	SESSAO_NAO_ENCONTRADA_NESTA_PAUTA("Esta pauta não possui sessão aberta,favor abrir uma sessão."),
	
	VOTO_ASSOCIADO_NAO_ELEGIVEL("Este usuário não está elegível para votar."),
	
	ERRO_GENERICO("Erro genérico ocorrido do bakcend.");

	private final String valor;

	MensagemVotacaoEnum(String valor) {
		this.valor = valor;
	}

}
