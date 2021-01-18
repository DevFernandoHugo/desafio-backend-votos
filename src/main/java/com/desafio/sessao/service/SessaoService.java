package com.desafio.sessao.service;

import java.util.List;

import static java.util.Objects.isNull;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.desafio.sessao.constant.MensagemVotacaoEnum;
import com.desafio.sessao.entity.Sessao;
import com.desafio.sessao.exception.DatabaseException;
import com.desafio.sessao.exception.PautaException;
import com.desafio.sessao.repository.IPautaRepository;

import reactor.core.publisher.Mono;

@Service
public class SessaoService implements ISessaoService {

	private static final String HASHKEY = "session";

	private final RedisTemplate<String, ?> redisTemplate;
	private final IPautaRepository pautaRepository;

	@Autowired
	public SessaoService(RedisTemplate<String, ?> redisTemplate, IPautaRepository pautaRepository) {
		this.redisTemplate = redisTemplate;
		this.pautaRepository = pautaRepository;
	}

	@Override
	public Mono<Boolean> salvarSessao(Sessao sessaoRedis) {

		
		return Mono.empty();
	}


}
