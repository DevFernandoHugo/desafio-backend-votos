package com.desafio.sessao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@RedisHash("session")
public class Sessao implements Serializable {

	private static final long serialVersionUID = -8213633983476895970L;

	@Id
	private String pautaCodigo;
	private Integer prazo;
}