package com.desafio.sessao.exception;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class VotacaoException extends RuntimeException implements Serializable {

	private static final long serialVersionUID = 5736454152800894035L;
	private final int statusCode;

	public VotacaoException(String mensagem, int statusCode) {
		super(mensagem);
		this.statusCode = statusCode;

	}

	public VotacaoException(String mensagem, int statusCode, Throwable cause) {
		super(mensagem, cause);
		this.statusCode = statusCode;

	}
}