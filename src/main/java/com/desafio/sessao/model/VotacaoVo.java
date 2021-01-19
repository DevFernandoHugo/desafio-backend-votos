package com.desafio.sessao.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VotacaoVo implements Serializable {

	private static final long serialVersionUID = -5494881673086315733L;	
	
	private Long totalNao;
	private Long totalSim;
	private Long totalVotos;
	private Long codigoPauta;
	private String DescricaoPauta;
}
