package com.desafio.sessao.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desafio.sessao.entity.Pauta;
import com.desafio.sessao.repository.IPautaRepository;

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

		return Mono.empty();
	}

}
