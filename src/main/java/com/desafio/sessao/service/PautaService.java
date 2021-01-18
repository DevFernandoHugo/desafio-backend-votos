package com.desafio.sessao.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.desafio.sessao.entity.Pauta;
import com.desafio.sessao.repository.IPautaRepository;
import com.desafio.sessao.constant.MensagemVotacaoEnum;
import com.desafio.sessao.exception.DatabaseException;
import com.desafio.sessao.exception.PautaException;
import com.desafio.sessao.model.PautaVo;

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

	@Override
	public Mono<Boolean> registrarPauta(PautaVo descricao) {

		return validarTexto(descricao).flatMap(transformer -> montarModeloEntrada(descricao)
				.flatMap(pauta -> Mono.just(pautaRepository.save(pauta)).map(p -> true).doOnError(
						onError -> Mono.error(new DatabaseException(MensagemVotacaoEnum.ERRO_GENERICO.getValor(),
								HttpStatus.INTERNAL_SERVER_ERROR.value())))));
	}

	@Override
	public Mono<Pauta> consultarPautaPorId(Long id) {
		try {
			Optional<Pauta> optPauta = this.pautaRepository.findById(id);
			if (optPauta.isPresent()) {
				Pauta pauta = optPauta.get();
				return Mono.just(pauta);
			}
			return Mono.error(new PautaException(MensagemVotacaoEnum.PAUTA_NAO_ENCONTRADA.getValor(),
					HttpStatus.BAD_REQUEST.value()));
		} catch (PautaException erroGenerico) {
			LOG.error(erroGenerico.getMessage());
			return Mono.error(new PautaException(MensagemVotacaoEnum.ERRO_GENERICO.getValor(),
					HttpStatus.INTERNAL_SERVER_ERROR.value()));
		}
	}

	@Override
	public Mono<Boolean> consultarPautaExistente(Long id) {
		try {

			Optional<Pauta> optPauta = this.pautaRepository.findById(id);

			return optPauta.isPresent() ? Mono.just(Boolean.TRUE)
					: Mono.error(new PautaException(MensagemVotacaoEnum.PAUTA_NAO_ENCONTRADA.getValor(),
							HttpStatus.BAD_REQUEST.value()));
		} catch (PautaException erroGenerico) {
			LOG.error(erroGenerico.getMessage());
			return Mono.error(new PautaException(MensagemVotacaoEnum.ERRO_GENERICO.getValor(),
					HttpStatus.INTERNAL_SERVER_ERROR.value()));
		}
	}

	protected Mono<Pauta> montarModeloEntrada(PautaVo descricao) {
		Pauta pauta = Pauta.builder().descricao(descricao.getValor()).build();
		return Mono.just(pauta);
	}

	private Mono<Boolean> validarTexto(PautaVo descricao) {
		if (descricao.getValor() != null) {
			String pautaDescricao = descricao.getValor();

			if (!StringUtils.isBlank(pautaDescricao)) {
				String texto = pautaDescricao.replaceAll("[^\\x00-\\x7F]", "");
				texto = texto.replaceAll("[^a-zA-Z\\s+]", "");

				if (texto.equals(pautaDescricao)) {
					return Mono.just(Boolean.TRUE);
				}
			}
		}
		return Mono.error(new PautaException(MensagemVotacaoEnum.PAUTA_DESCRICAO_INVALIDA.getValor(),
				HttpStatus.BAD_REQUEST.value()));
	}

}
