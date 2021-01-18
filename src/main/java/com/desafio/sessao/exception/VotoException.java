package com.desafio.sessao.exception;

import lombok.Getter;

@Getter
public class VotoException extends VotacaoException {

	private static final long serialVersionUID = 317468440453126287L;

	public VotoException(String mensagem, int statusCode) {
		super(mensagem, statusCode);
	}

}
