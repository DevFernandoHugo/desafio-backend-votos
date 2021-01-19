package com.desafio.sessao.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.desafio.sessao.constant.MensagemVotacaoEnum;
import com.desafio.sessao.entity.Voto;
import com.desafio.sessao.exception.SessaoException;
import com.desafio.sessao.exception.VotoException;
import com.desafio.sessao.integration.IAssociadoService;
import com.desafio.sessao.repository.IVotoRepository;
import com.desafio.sessao.utils.ValidaCpfUtils;

import reactor.core.publisher.Mono;

/*
 * Classe onde serão implementadas as regras
 * de negócio para a manipulação dos dados do "Voto".
 * */
@Service
public class VotoService implements IVotoService {

	private IAssociadoService associadoService;

	private IPautaService pautaService;

	private IVotoRepository votoRepository;

	@Autowired
	public VotoService(IAssociadoService associadoService, IPautaService pautaService, IVotoRepository votoRepository) {
		this.associadoService = associadoService;
		this.pautaService = pautaService;
		this.votoRepository = votoRepository;
	}


	/**
	 * Persistir voto na base de dados
	 * @param voto , modelo entidade que será perssitida na base de dados
	 * @return se o voto foi corretamente perssistido
	 */
	@Override
	public Mono<Boolean> criarVoto(Voto voto) {

		String cpf = voto.getCpf();

		if (ValidaCpfUtils.isValid(cpf)) {

			return associadoService.verificarAssociadoElegivel(cpf).flatMap(able -> pautaService
					.consultarPautaExistente(voto.getCodigoPauta())
					.flatMap(existe -> this.verificarVotoAssociadoPauta(voto)
							.flatMap(votar -> pautaService.verificarPautaSessaoAberta(voto.getCodigoPauta())
									.flatMap(SessaoAberta -> Mono.just(votoRepository.save(voto))
											.map(votoCriado -> Boolean.TRUE))
									.doOnError(onError -> Mono.error(
											new SessaoException(MensagemVotacaoEnum.PAUTA_SESSAO_NAO_ABERTA.getValor(),
													HttpStatus.BAD_REQUEST.value()))))
							.doOnError(onError -> Mono
									.error(new VotoException(MensagemVotacaoEnum.VOTO_ASSOCIADO_VOTOU.getValor(),
											HttpStatus.BAD_REQUEST.value()))))
					.doOnError(
							onError -> Mono.error(new VotoException(MensagemVotacaoEnum.PAUTA_NAO_ENCONTRADA.getValor(),
									HttpStatus.NOT_FOUND.value()))))
					.doOnError(onError -> Mono
							.error(new VotoException(MensagemVotacaoEnum.VOTO_ASSOCIADO_NAO_ELEGIVEL.getValor(),
									HttpStatus.BAD_REQUEST.value())));
		} else {

			return Mono.error(new VotoException(MensagemVotacaoEnum.VOTO_CPF_INVALIDO.getValor(),
					HttpStatus.BAD_REQUEST.value()));

		}

	}

	/**
	 * verificar se associado já votou nesta pauta
	 * @param voto , modelo entidade que será utilizado para fazer a verificação.
	 * @return se o associado não votou nesta pauta
	 */
	@Override
	public Mono<Boolean> verificarVotoAssociadoPauta(Voto voto) {

		Optional<Voto> votoAssociado = votoRepository.buscarVotosRealizadosPauta(voto.getCpf(), voto.getCodigoPauta());

		if (votoAssociado.isPresent()) {
			return Mono.error(new VotoException(MensagemVotacaoEnum.VOTO_ASSOCIADO_VOTOU.getValor(),
					HttpStatus.BAD_REQUEST.value()));
		}

		return Mono.just(Boolean.TRUE);
	}

	/**
	 * Buscar todos os votos associados a uma pauta
	 * @param codigoPauta , codigo da pauta que será utilizada para buscar os votos.
	 * @return lista de votos associados a uma pauta
	 */
	@Override
	public Mono<List<Voto>> buscarVotosPorPauta(Long codigoPauta) {
		return Mono.just(votoRepository.buscarVotosPauta(codigoPauta))
				.flatMap(list -> !list.isEmpty() ? Mono.just(list)
						: Mono.error(new VotoException(MensagemVotacaoEnum.VOTO_PAUTA_NAO_ENCONTRADO.getValor(),
								HttpStatus.BAD_REQUEST.value())));
	}

}
