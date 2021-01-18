package com.desafio.sessao.constant;

import lombok.Getter;

@Getter
public enum MensagemVotacaoEnum {

	PAUTA_NAO_ENCONTRADA("Pauta(s) não encontrada(s)."),
	PAUTA_DESCRICAO_INVALIDA("Pauta descrição inválido,verifique o parâmetro de entrada.");

	private final String valor;

	MensagemVotacaoEnum(String valor) {
		this.valor = valor;
	}

}
