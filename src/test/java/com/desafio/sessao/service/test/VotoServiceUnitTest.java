package com.desafio.sessao.service.test;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runners.MethodSorters;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import com.desafio.sessao.constant.MensagemVotacaoEnum;
import com.desafio.sessao.entity.Voto;
import com.desafio.sessao.exception.SessaoException;
import com.desafio.sessao.exception.VotoException;
import com.desafio.sessao.integration.IAssociadoService;
import com.desafio.sessao.repository.IVotoRepository;
import com.desafio.sessao.service.IPautaService;
import com.desafio.sessao.service.VotoService;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit-test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VotoServiceUnitTest {

	@Mock
	private IAssociadoService associadoService;

	@Mock
	private IPautaService pautaService;

	@Mock
	private IVotoRepository votoRepository;

	@Mock
	private VotoService mockVotoService;

	@InjectMocks
	private VotoService votoService;

	private Voto votoEntity;

	@Before
	public void init() {
		MockitoAnnotations.openMocks(this);
		criaVoto();

	}

	private void criaVoto() {
		votoEntity = new Voto();
		votoEntity.setCodigoPauta(2L);
		votoEntity.setEscolha(true);
		votoEntity.setCpf("68869572013");
	}

	@Test
	public void testeCriarVotoSucesso() {

		Mockito.when(associadoService.verificarAssociadoElegivel(votoEntity.getCpf()))
				.thenReturn(Mono.just(Boolean.TRUE));

		Mockito.when(pautaService.consultarPautaExistente(votoEntity.getCodigoPauta()))
				.thenReturn(Mono.just(Boolean.TRUE));

		Mockito.when(mockVotoService.verificarVotoAssociadoPauta(votoEntity)).thenReturn(Mono.just(Boolean.TRUE));

		Mockito.when(pautaService.verificarPautaSessaoAberta(votoEntity.getCodigoPauta()))
				.thenReturn(Mono.just(Boolean.TRUE));

		Mockito.when(votoRepository.buscarVotosRealizadosPauta(votoEntity.getCpf(), votoEntity.getCodigoPauta()))
				.thenReturn(Optional.empty());

		Mockito.when(votoRepository.save(votoEntity)).thenReturn(votoEntity);

		votoService.criarVoto(votoEntity).map(ok -> {
			Assertions.assertNotNull(ok);
			Assertions.assertEquals(true, ok);
			return Mono.empty();
		}).subscribe();

	}

	@Test
	public void testeCriarVotoCpfInvalido() {

		votoEntity.setCpf("156456");
		Mockito.when(associadoService.verificarAssociadoElegivel(votoEntity.getCpf()))
				.thenReturn(Mono.just(Boolean.TRUE));

		Mockito.when(pautaService.consultarPautaExistente(votoEntity.getCodigoPauta()))
				.thenReturn(Mono.just(Boolean.TRUE));

		Mockito.when(mockVotoService.verificarVotoAssociadoPauta(votoEntity)).thenReturn(Mono.just(Boolean.TRUE));

		Mockito.when(pautaService.verificarPautaSessaoAberta(votoEntity.getCodigoPauta()))
				.thenReturn(Mono.just(Boolean.TRUE));

		Mockito.when(votoRepository.buscarVotosRealizadosPauta(votoEntity.getCpf(), votoEntity.getCodigoPauta()))
				.thenReturn(Optional.empty());

		Mockito.when(votoRepository.save(votoEntity)).thenReturn(votoEntity);

		votoService.criarVoto(votoEntity).onErrorResume(error -> {
			VotoException erro = (VotoException) error;
			Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), erro.getStatusCode());
			return Mono.empty();
		}).subscribe();

	}

	@Test
	public void testeCriarVotoAssociadoNaoLegivel() {

		Mockito.when(associadoService.verificarAssociadoElegivel(votoEntity.getCpf()))
				.thenReturn(Mono.error(new VotoException(MensagemVotacaoEnum.VOTO_ASSOCIADO_NAO_ELEGIVEL.getValor(),
						HttpStatus.BAD_REQUEST.value())));

		Mockito.when(pautaService.consultarPautaExistente(votoEntity.getCodigoPauta()))
				.thenReturn(Mono.just(Boolean.TRUE));

		Mockito.when(mockVotoService.verificarVotoAssociadoPauta(votoEntity)).thenReturn(Mono.just(Boolean.TRUE));

		Mockito.when(pautaService.verificarPautaSessaoAberta(votoEntity.getCodigoPauta()))
				.thenReturn(Mono.just(Boolean.TRUE));

		Mockito.when(votoRepository.buscarVotosRealizadosPauta(votoEntity.getCpf(), votoEntity.getCodigoPauta()))
				.thenReturn(Optional.empty());

		Mockito.when(votoRepository.save(votoEntity)).thenReturn(votoEntity);

		votoService.criarVoto(votoEntity).onErrorResume(error -> {
			VotoException erro = (VotoException) error;
			Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), erro.getStatusCode());
			return Mono.empty();
		}).subscribe();

	}

	@Test
	public void testeCriarVotoPautaNaoEncontrada() {

		Mockito.when(associadoService.verificarAssociadoElegivel(votoEntity.getCpf()))
				.thenReturn(Mono.just(Boolean.TRUE));

		Mockito.when(pautaService.consultarPautaExistente(votoEntity.getCodigoPauta())).thenReturn(Mono.error(
				new VotoException(MensagemVotacaoEnum.PAUTA_NAO_ENCONTRADA.getValor(), HttpStatus.NOT_FOUND.value())));

		Mockito.when(mockVotoService.verificarVotoAssociadoPauta(votoEntity)).thenReturn(Mono.just(Boolean.TRUE));

		Mockito.when(pautaService.verificarPautaSessaoAberta(votoEntity.getCodigoPauta()))
				.thenReturn(Mono.just(Boolean.TRUE));

		Mockito.when(votoRepository.buscarVotosRealizadosPauta(votoEntity.getCpf(), votoEntity.getCodigoPauta()))
				.thenReturn(Optional.empty());

		Mockito.when(votoRepository.save(votoEntity)).thenReturn(votoEntity);

		votoService.criarVoto(votoEntity).onErrorResume(error -> {
			VotoException erro = (VotoException) error;
			Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), erro.getStatusCode());
			return Mono.empty();
		}).subscribe();

	}

	@Test
	public void testeCriarVotoAssociadoJaVotou() {

		Mockito.when(associadoService.verificarAssociadoElegivel(votoEntity.getCpf()))
				.thenReturn(Mono.just(Boolean.TRUE));

		Mockito.when(pautaService.consultarPautaExistente(votoEntity.getCodigoPauta()))
				.thenReturn(Mono.just(Boolean.TRUE));

		Mockito.when(mockVotoService.verificarVotoAssociadoPauta(votoEntity))
				.thenReturn(Mono.error(new VotoException(MensagemVotacaoEnum.VOTO_ASSOCIADO_VOTOU.getValor(),
						HttpStatus.BAD_REQUEST.value())));

		Mockito.when(pautaService.verificarPautaSessaoAberta(votoEntity.getCodigoPauta()))
				.thenReturn(Mono.just(Boolean.TRUE));

		Mockito.when(votoRepository.buscarVotosRealizadosPauta(votoEntity.getCpf(), votoEntity.getCodigoPauta()))
				.thenReturn(Optional.of(votoEntity));

		Mockito.when(votoRepository.save(votoEntity)).thenReturn(votoEntity);

		votoService.criarVoto(votoEntity).onErrorResume(error -> {
			VotoException erro = (VotoException) error;
			Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), erro.getStatusCode());
			return Mono.empty();
		}).subscribe();

	}

	@Test
	public void testeCriarVotoAssociadoNaoPossuiVotacaoAberta() {

		Mockito.when(associadoService.verificarAssociadoElegivel(votoEntity.getCpf()))
				.thenReturn(Mono.just(Boolean.TRUE));

		Mockito.when(pautaService.consultarPautaExistente(votoEntity.getCodigoPauta()))
				.thenReturn(Mono.just(Boolean.TRUE));

		Mockito.when(mockVotoService.verificarVotoAssociadoPauta(votoEntity)).thenReturn(Mono.just(Boolean.TRUE));

		Mockito.when(pautaService.verificarPautaSessaoAberta(votoEntity.getCodigoPauta()))
				.thenReturn(Mono.error(new SessaoException(MensagemVotacaoEnum.PAUTA_SESSAO_NAO_ABERTA.getValor(),
						HttpStatus.BAD_REQUEST.value())));

		Mockito.when(votoRepository.buscarVotosRealizadosPauta(votoEntity.getCpf(), votoEntity.getCodigoPauta()))
				.thenReturn(Optional.empty());

		Mockito.when(votoRepository.save(votoEntity)).thenReturn(votoEntity);

		votoService.criarVoto(votoEntity).onErrorResume(error -> {
			SessaoException erro = (SessaoException) error;
			Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), erro.getStatusCode());
			return Mono.empty();
		}).subscribe();

	}

	@Test
	public void testeBuscarVotosPorPautaSucesso() {

		Long codigo = 2L;
		Mockito.when(votoRepository.buscarVotosPauta(codigo)).thenReturn(Arrays.asList(votoEntity));

		votoService.buscarVotosPorPauta(codigo).flatMap(list -> {
			Assertions.assertNotNull(list);
			Assertions.assertFalse(list.isEmpty());
			return Mono.empty();
		}).subscribe();

	}

	@Test
	public void testeBuscarVotosPorPautaBadRequest() {

		Long codigo = 2L;
		Mockito.when(votoRepository.buscarVotosPauta(codigo)).thenReturn(Arrays.asList());

		votoService.buscarVotosPorPauta(codigo).onErrorResume(error -> {
			VotoException erro = (VotoException) error;
			Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), erro.getStatusCode());
			return Mono.empty();
		}).subscribe();

	}

}
