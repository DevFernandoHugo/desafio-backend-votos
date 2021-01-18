package com.desafio.sessao.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.desafio.sessao.entity.Pauta;
import com.desafio.sessao.repository.IPautaRepository;
import com.desafio.sessao.constant.MensagemVotacaoEnum;
import com.desafio.sessao.exception.PautaException;

import reactor.core.publisher.Mono;

@Service
public class PautaService implements IPautaService {

	private static final Logger LOG = LoggerFactory.getLogger(PautaService.class);

	private IPautaRepository pautaRepository;

	@Autowired
	public PautaService(IPautaRepository pautaRepository) {
		this.pautaRepository = pautaRepository;
	}

	@Override
	public Mono<List<Pauta>> consultarPautas() {
		return Mono.just(pautaRepository.findAll())
				.flatMap(list -> !list.isEmpty() ? Mono.just(list)
						: Mono.error(new PautaException(MensagemVotacaoEnum.PAUTA_NAO_ENCONTRADA.getValor(),
								HttpStatus.NOT_FOUND.value())));
	}
}
