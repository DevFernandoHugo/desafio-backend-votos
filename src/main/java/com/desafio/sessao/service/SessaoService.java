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
import com.desafio.sessao.exception.SessaoException;

import reactor.core.publisher.Mono;

/*
 * Classe onde serão implementadas as regras
 * de negócio para a manipulação dos dados da "Sessao".
 * */
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

	/**
	 * Armazenar sessão no cache
	 * @param sessaoRedis , modelo de sessão usada no armazenamento da sessão
	 * @return se o sessão foi corretamente armazenada
	 */
	@Override
	public Mono<Boolean> salvarSessao(Sessao sessaoRedis) {

		try {

			Long code = Long.parseLong(sessaoRedis.getPautaCodigo());

			if (this.pautaRepository.findById(code).isPresent()) {

				Integer prazo = isNull(sessaoRedis.getPrazo()) || sessaoRedis.getPrazo() == 0 ? 60
						: sessaoRedis.getPrazo();

				redisTemplate.opsForHash().put(sessaoRedis.getPautaCodigo(), HASHKEY, sessaoRedis);
				redisTemplate.expire(sessaoRedis.getPautaCodigo(), prazo, TimeUnit.SECONDS);

				return Mono.just(Boolean.TRUE);
			} else {
				return Mono.error(new PautaException(MensagemVotacaoEnum.PAUTA_NAO_ENCONTRADA_SESSAO.getValor(),
						HttpStatus.BAD_REQUEST.value()));
			}

		} catch (SessaoException e) {
			return Mono.error(new SessaoException(MensagemVotacaoEnum.ERRO_GENERICO.getValor(),
					HttpStatus.INTERNAL_SERVER_ERROR.value()));
		}
	}

	/**
	 * Consulta todas as sessões abertas
	 *  @return retorna lista de sessões abertas
	 */
	@Override
	public Mono<List<Long>> recuperaSessoesAbertas() {

		try {
			return Mono.just((List<Long>) redisTemplate.keys("*").stream()
					.map(sessao -> Long.parseLong((String) sessao)).collect(Collectors.toList()));
		} catch (DatabaseException e) {
			return Mono.error(new DatabaseException(MensagemVotacaoEnum.SESSAO_NAO_ENCONTRADA.getValor(),
					HttpStatus.INTERNAL_SERVER_ERROR.value()));
		}

	}

	/**
	 * Verificar se a sessão repassada existe
	 * @param chave , chave utlizada para verificar se a sessão existe
	 * @return se a sessão foi encontrada
	 */
	@Override
	public Mono<Boolean> verificarSessao(String chave) {
		try {
			Object sessao = redisTemplate.opsForHash().get(chave, HASHKEY);
			return !isNull(sessao) ? Mono.just(Boolean.TRUE)
					: Mono.error(new SessaoException(MensagemVotacaoEnum.SESSAO_NAO_ENCONTRADA_NESTA_PAUTA.getValor(),
							HttpStatus.BAD_REQUEST.value()));
		} catch (DatabaseException e) {
			return Mono.error(new DatabaseException(MensagemVotacaoEnum.ERRO_GENERICO.getValor(),
					HttpStatus.INTERNAL_SERVER_ERROR.value()));
		}
	}

}
