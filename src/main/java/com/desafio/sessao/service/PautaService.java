package com.desafio.sessao.service;

import java.util.ArrayList;
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
import com.desafio.sessao.service.ISessaoService;
import com.desafio.sessao.constant.MensagemVotacaoEnum;
import com.desafio.sessao.exception.DatabaseException;
import com.desafio.sessao.exception.PautaException;
import com.desafio.sessao.model.PautaVo;

import reactor.core.publisher.Mono;

/*
 * Classe onde serão implementadas as regras
 * de negócio para a manipulação dos dados da "Pauta".
 * */
@Service
public class PautaService implements IPautaService {

	private static final Logger LOG = LoggerFactory.getLogger(PautaService.class);

	private IPautaRepository pautaRepository;

	private ISessaoService sessaoService;

	@Autowired
	public PautaService(IPautaRepository pautaRepository, ISessaoService sessaoService) {
		this.pautaRepository = pautaRepository;
		this.sessaoService = sessaoService;
	}
	
	/**
	 * Consulta todas as pautas cadastras na base de dados
	 */
	@Override
	public Mono<List<Pauta>> consultarPautas() {
		return Mono.just(pautaRepository.findAll())
				.flatMap(list -> !list.isEmpty() ? Mono.just(list)
						: Mono.error(new PautaException(MensagemVotacaoEnum.PAUTA_NAO_ENCONTRADA.getValor(),
								HttpStatus.NOT_FOUND.value())));
	}

	/**
	 * Persistir pauta na base de dados
	 * @param descricao , descrição a ser aplicada na pauta que será perssistida 
	 * @return se o pauta foi corretamente perssistida
	 */
	@Override
	public Mono<Boolean> registrarPauta(PautaVo descricao) {

		return validarTexto(descricao).flatMap(transformer -> montarModeloEntrada(descricao)
				.flatMap(pauta -> Mono.just(pautaRepository.save(pauta)).map(p -> true).doOnError(
						onError -> Mono.error(new DatabaseException(MensagemVotacaoEnum.ERRO_GENERICO.getValor(),
								HttpStatus.INTERNAL_SERVER_ERROR.value())))));
	}

	/**
	 * buscar por pauta na base de dados
	 * @param id , identificador a ser aplicada na pauta que será perssistida 
	 * @return se o pauta foi corretamente perssistida
	 */
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

	/**
	 * verificar se existe pauta correspondente ao identificador na base de dados
	 * @param id , identificador a ser aplicada na busca da pauta
	 * @return se o pauta existe
	 */
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

	/**
	 * monta modelo de pauta que devera ser pessistido na base de dados
	 * @param descricao , descrição a ser aplicada na pauta que será perssistida 
	 * @return modelo criado
	 */
	protected Mono<Pauta> montarModeloEntrada(PautaVo descricao) {
		Pauta pauta = Pauta.builder().descricao(descricao.getValor()).build();
		return Mono.just(pauta);
	}
	
	/**
	 * valida descrição da pauta
	 * @param descricao , descrição a ser aplicada na pauta que será perssistida 
	 * @return se a descrição é valida
	 */
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

	/**
	 * Verificar se a pauta possui sessao aberta
	 * @param codigoPautaSessao , codigo utlizado para verificar se a pauta possui sessao aberta
	 * @return se a sessão foi encontrada
	 */
	@Override
	public Mono<Boolean> verificarPautaSessaoAberta(Long codigoPautaSessao) {
		return sessaoService.verificarSessao(codigoPautaSessao.toString()).doOnError(
				onError -> Mono.error(new PautaException(MensagemVotacaoEnum.ERRO_GENERICO.getValor(),
						HttpStatus.INTERNAL_SERVER_ERROR.value())));
	}
	
	/**
	 * Buscar se a pautas que possuem sessão aberta
	 * @return se a sessão foi encontrada retorna lsitade pautas
	 *  */
	@Override
	public Mono<List<Pauta>> consultarPautasComSessaoAberta() {

		List<Pauta> pautaDto = new ArrayList<>();

		return sessaoService.recuperaSessoesAbertas().flatMap(listaCodigoPauta -> {

			listaCodigoPauta.forEach(codigoPauta -> pautaDto.add(pautaRepository.findById(codigoPauta).get()));

			return !pautaDto.isEmpty() ? Mono.just(pautaDto)
					: Mono.error(new PautaException(MensagemVotacaoEnum.PAUTA_NAO_ENCONTRADA.getValor(),
							HttpStatus.BAD_REQUEST.value()));
		}).doOnError(onError -> Mono.error(new DatabaseException(MensagemVotacaoEnum.PAUTA_NAO_ENCONTRADA.getValor(),
				HttpStatus.INTERNAL_SERVER_ERROR.value())));

	}
}
