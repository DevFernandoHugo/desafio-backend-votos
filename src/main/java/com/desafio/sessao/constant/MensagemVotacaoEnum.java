package com.desafio.sessao.constant;

import lombok.Getter;

@Getter
public enum MensagemVotacaoEnum {

	PAUTA_NAO_ENCONTRADA("Pauta(s) não encontrada(s)."),
	PAUTA_DESCRICAO_INVALIDA("Pauta descrição inválido,verifique o parâmetro de entrada."),
	
	ERRO_GENERICO("Conta bloqueada por excesso de tentativas de acesso com erro,"
			+ "para desbloquear sua conta acesse o 'esqueci minha senha'.");

	private final String valor;

	MensagemVotacaoEnum(String valor) {
		this.valor = valor;
	}

}
