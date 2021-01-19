package com.desafio.sessao.service.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.desafio.sessao.entity.Voto;
import com.desafio.sessao.service.IPautaService;
import com.desafio.sessao.service.VotoService;
import com.desafio.sessao.entity.Pauta;
import com.desafio.sessao.exception.VotacaoException;
import com.desafio.sessao.service.VotacaoService;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit-test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VotacaoServiceUnitTest {

	@Mock
	private IPautaService pautaService;

	@Mock
	private VotoService votoService;
	
	@InjectMocks
	private VotacaoService votacaoService;

	private Voto votoEntity;
	
	private Pauta pautaEntity;

	@Before
	public void init() {
		MockitoAnnotations.openMocks(this);
		criaVoto();
		criaPautaEntity();

	}

	private void criaVoto() {
		votoEntity = new Voto();
		votoEntity.setCodigoPauta(2L);
		votoEntity.setEscolha(true);
		votoEntity.setCpf("68869572013");
	}
	
	private void criaPautaEntity() {
		pautaEntity = new Pauta();
		pautaEntity.setCodigo(2L);
		pautaEntity.setDescricao("segunda Pauta");
	}

	@Test
	public void testeRecuperarVotacaoPautaSucesso() {

		Long codigoPauta = 2L;
		Mockito.when(votoService.buscarVotosPorPauta(codigoPauta)).thenReturn(Mono.just(Arrays.asList(votoEntity)));
		Mockito.when(pautaService.consultarPautaPorId(codigoPauta)).thenReturn(Mono.just(pautaEntity));
		
		votacaoService.recuperarVotacaoPauta(codigoPauta).map(votacao -> {
			Assert.assertNotNull(votacao);
			Assert.assertEquals(votacao.getDescricaoPauta(), "segunda Pauta");
			return Mono.empty();
		}).subscribe();
	}
	
	@Test
	public void testeRecuperarVotacaoPautaNotFound() {

		Long codigoPauta = 2L;
		Mockito.when(votoService.buscarVotosPorPauta(codigoPauta)).thenReturn(Mono.just(Arrays.asList()));
		Mockito.when(pautaService.consultarPautaPorId(codigoPauta)).thenReturn(Mono.just(pautaEntity));
		
		votacaoService.recuperarVotacaoPauta(codigoPauta).onErrorResume(error -> {
			VotacaoException erro = (VotacaoException) error;
			assertEquals(HttpStatus.BAD_REQUEST.value(), erro.getStatusCode());
			return Mono.empty();
		}).subscribe();
	}
	
	@Test
	public void testeRecuperarVotacaoPautaBadRequest() {

		Long codigoPauta = 2L;
		Mockito.when(votoService.buscarVotosPorPauta(codigoPauta)).thenReturn(Mono.just(Arrays.asList(votoEntity)));
		Mockito.when(pautaService.consultarPautaPorId(codigoPauta)).thenReturn(Mono.empty());
		
		votacaoService.recuperarVotacaoPauta(codigoPauta).onErrorResume(error -> {
			VotacaoException erro = (VotacaoException) error;
			assertEquals(HttpStatus.BAD_REQUEST.value(), erro.getStatusCode());
			return Mono.empty();
		}).subscribe();
	}

}
