package com.desafio.sessao.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.desafio.sessao.constant.MensagemVotacaoEnum;
import com.desafio.sessao.entity.Voto;
import com.desafio.sessao.exception.DatabaseException;
import com.desafio.sessao.exception.VotacaoException;
import com.desafio.sessao.model.VotacaoVo;

import reactor.core.publisher.Mono;

/*
 * Classe onde serão implementadas as regras
 * de negócio para a manipulação dos dados da "Votação".
 * */
@Service
public class VotacaoService implements IVotacaoService {

	private IVotoService votoService;

	private IPautaService pautaService;

	public VotacaoService(IVotoService votoService, IPautaService pautaService) {
		this.votoService = votoService;
		this.pautaService = pautaService;
	}

	/**
	 * Retorna votação por pauta 
	 * @param codigoPauta ,codigo da pauta que será utilizada para retorna o resultado da votação
	 * @return retorna modelo de votação
	 */
	@Override
	public Mono<VotacaoVo> recuperarVotacaoPauta(Long codigoPauta) {

		return votoService.buscarVotosPorPauta(codigoPauta)
				.flatMap(votos -> montarVotacaoPauta(votos).doOnError(
						onError -> Mono.error(new VotacaoException(MensagemVotacaoEnum.ERRO_GENERICO.getValor(),
								HttpStatus.INTERNAL_SERVER_ERROR.value()))))
				.doOnError(onError -> Mono.error(new VotacaoException(MensagemVotacaoEnum.ERRO_GENERICO.getValor(),
						HttpStatus.INTERNAL_SERVER_ERROR.value())));

	}

	/**
	 * Constroi modelo de votação por pauta 
	 * @param listaVotos ,lista de votos apurados por pauta que sera utilizado para montar o modelo de votação
	 * @return retorna modelo de votação
	 */
	protected Mono<VotacaoVo> montarVotacaoPauta(List<Voto> listaVotos) {

		if (!listaVotos.isEmpty()) {

			return pautaService.consultarPautaPorId(listaVotos.get(0).getCodigoPauta()).flatMap(pauta -> {

				Long quantidadeVotos = (long) listaVotos.size();
				Long votosSim = listaVotos.stream().filter(voto -> Boolean.TRUE.equals(voto.getEscolha())).count();
				Long votosNao = quantidadeVotos - votosSim;

				return Mono.just(VotacaoVo.builder().codigoPauta(pauta.getCodigo()).DescricaoPauta(pauta.getDescricao())
						.totalNao(votosNao).totalSim(votosSim).totalVotos(quantidadeVotos).build());

			}).switchIfEmpty(Mono
					.defer(() -> Mono.error(new DatabaseException(MensagemVotacaoEnum.PAUTA_NAO_ENCONTRADA.getValor(),
							HttpStatus.BAD_REQUEST.value()))));
		} else {

			return Mono.error(new VotacaoException(MensagemVotacaoEnum.PAUTA_NAO_ENCONTRADA.getValor(),
					HttpStatus.BAD_REQUEST.value()));
		}
	}
}
