package com.desafio.sessao.exception;

import lombok.Getter;

@Getter
public class DatabaseException extends VotacaoException {

	private static final long serialVersionUID = -9069354051685533360L;

	public DatabaseException(String mensagem, int statusCode) {
		super(mensagem, statusCode);
	}

}
