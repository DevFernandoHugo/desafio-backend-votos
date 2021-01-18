package com.desafio.sessao.exception;

import lombok.Getter;

@Getter
public class SessaoException extends VotacaoException {

	private static final long serialVersionUID = 1928624726261800182L;

	public SessaoException(String mensagem, int statusCode) {
		super(mensagem, statusCode);
	}

}
