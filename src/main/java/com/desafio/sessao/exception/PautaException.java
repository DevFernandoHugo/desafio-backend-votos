package com.desafio.sessao.exception;

import lombok.Getter;

@Getter
public class PautaException extends VotacaoException {

	private static final long serialVersionUID = -9069354051685533360L;

	public PautaException(String mensagem, int statusCode) {
		super(mensagem, statusCode);
	}

}
