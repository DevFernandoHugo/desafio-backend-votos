package com.desafio.sessao.service.test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Assertions;

import com.desafio.sessao.repository.IPautaRepository;
import com.desafio.sessao.service.PautaService;
import com.desafio.sessao.service.IPautaService;
import com.desafio.sessao.service.SessaoService;
import com.desafio.sessao.constant.MensagemVotacaoEnum;
import com.desafio.sessao.exception.DatabaseException;
import com.desafio.sessao.exception.PautaException;
import com.desafio.sessao.entity.Pauta;
import com.desafio.sessao.model.PautaVo;

import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit-test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PautaServiceUnitTest {

	@Mock
	private IPautaRepository pautaRepository;

	@Mock
	private IPautaService mockPautaservice;

	@InjectMocks
	private PautaService pautaservice;

	@Mock
	private SessaoService sessaoService;

	private PautaVo pautaVo;

	private Pauta pautaEntity;

	private List<Pauta> listPautaEntity;

	@Before
	public void init() {
		MockitoAnnotations.openMocks(this);
		criaPautaVo();
		criaPautaEntity();
		criaListaPautaEntity();

	}

	private void criaPautaVo() {
		pautaVo = new PautaVo();
		pautaVo.setValor("ultima Pauta");
	}

	private void criaPautaEntity() {
		pautaEntity = new Pauta();
		pautaEntity.setCodigo(1L);
		pautaEntity.setDescricao("primeira Pauta");
	}

	private void criaListaPautaEntity() {
		Pauta pautaA = new Pauta();
		pautaA.setCodigo(1L);
		pautaA.setDescricao("primeira Pauta");

		Pauta pautaB = new Pauta();
		pautaB.setCodigo(2L);
		pautaB.setDescricao("segunda Pauta");

		Pauta pautaC = new Pauta();
		pautaC.setCodigo(3L);
		pautaC.setDescricao("terceira Pauta");

		listPautaEntity = new ArrayList<>();
		listPautaEntity.add(pautaA);
		listPautaEntity.add(pautaB);
		listPautaEntity.add(pautaC);
	}

	@Test
	public void testeConsultarPautasSucesso() {

		Mockito.when(pautaRepository.findAll()).thenReturn(listPautaEntity);

		pautaservice.consultarPautas().flatMap(list -> {
			Assertions.assertNotNull(list);
			Assertions.assertFalse(list.isEmpty());
			return Mono.empty();
		}).subscribe();

	}

	@Test
	public void testeConsultarPautasNotFound() {

		List<Pauta> listEmpty = new ArrayList<>();

		Mockito.when(pautaRepository.findAll()).thenReturn(listEmpty);

		pautaservice.consultarPautas().onErrorResume(error -> {
			PautaException erro = (PautaException) error;
			Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), erro.getStatusCode());
			return Mono.empty();
		}).subscribe();

	}

	@Test
	public void testeRegistrarPautaSucesso() {

		Pauta pautaD = new Pauta();
		pautaD.setDescricao("ultima Pauta");

		Pauta pautaE = new Pauta();
		pautaE.setCodigo(6L);
		pautaE.setDescricao("ultima Pauta");

		Mockito.when(pautaRepository.save(pautaD)).thenReturn(pautaE);

		pautaservice.registrarPauta(pautaVo).map(ok -> {
			Assertions.assertNotNull(ok);
			Assertions.assertEquals(true, ok);
			return Mono.empty();
		}).subscribe();
	}

	@Test
	public void testeRegistrarPautaErroGenerico() {

		Pauta pautaD = new Pauta();
		pautaD.setDescricao("ultima Pauta");

		Mockito.when(pautaRepository.save(pautaD)).thenThrow(new DatabaseException(
				MensagemVotacaoEnum.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR.value()));

		pautaservice.registrarPauta(pautaVo).onErrorResume(error -> {
			DatabaseException erro = (DatabaseException) error;
			Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), erro.getStatusCode());
			return Mono.empty();
		}).subscribe();
	}

	@Test
	public void testeConsultarPautaPorIdSucesso() {

		Mockito.when(pautaRepository.findById(1L)).thenReturn(Optional.of(pautaEntity));

		pautaservice.consultarPautaPorId(1L).flatMap(pauta -> {
			Assertions.assertNotNull(pauta);
			Assertions.assertTrue(Optional.of(pauta).isPresent());
			return Mono.empty();
		}).subscribe();
	}

	@Test
	public void testeConsultarPautaPorIdBadRequest() {

		Mockito.when(pautaRepository.findById(1L)).thenReturn(Optional.empty());

		pautaservice.consultarPautaPorId(1L).onErrorResume(error -> {
			PautaException erro = (PautaException) error;
			Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), erro.getStatusCode());
			return Mono.empty();
		}).subscribe();

	}

	@Test
	public void testeConsultarPautaPorIdErroGenerico() {

		Mockito.when(pautaRepository.findById(1L)).thenThrow(new PautaException(
				MensagemVotacaoEnum.PAUTA_NAO_ENCONTRADA.getValor(), HttpStatus.BAD_REQUEST.value()));

		pautaservice.consultarPautaPorId(1L).onErrorResume(error -> {
			PautaException erro = (PautaException) error;
			Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), erro.getStatusCode());
			return Mono.empty();
		}).subscribe();

	}

	@Test
	public void testeConsultarPautaExistenteSucesso() {

		Mockito.when(pautaRepository.findById(1L)).thenReturn(Optional.of(pautaEntity));

		pautaservice.consultarPautaExistente(1L).flatMap(ok -> {
			Assertions.assertNotNull(ok);
			Assertions.assertEquals(true, ok);
			return Mono.empty();
		}).subscribe();
	}

	@Test
	public void testeConsultarPautaExistenteBadRequest() {

		Mockito.when(pautaRepository.findById(1L)).thenReturn(Optional.empty());

		pautaservice.consultarPautaExistente(1L).onErrorResume(error -> {
			PautaException erro = (PautaException) error;
			Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), erro.getStatusCode());
			return Mono.empty();
		}).subscribe();
	}

	@Test
	public void testeConsultarPautaExistenteErroGenerico() {

		Mockito.when(pautaRepository.findById(1L)).thenThrow(new PautaException(
				MensagemVotacaoEnum.PAUTA_NAO_ENCONTRADA.getValor(), HttpStatus.BAD_REQUEST.value()));

		pautaservice.consultarPautaExistente(1L).onErrorResume(error -> {
			PautaException erro = (PautaException) error;
			Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), erro.getStatusCode());
			return Mono.empty();
		}).subscribe();

	}

	@Test
	public void testeConsultarPautasComSessaoAbertaSucesso() {

		List<Long> listaChaveSessao = new ArrayList<>();
		listaChaveSessao.add(1L);
		listaChaveSessao.add(2L);

		Mockito.when(sessaoService.recuperaSessoesAbertas()).thenReturn(Mono.just(listaChaveSessao));

		Mockito.when(pautaRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(listPautaEntity.get(0)))
				.thenReturn(Optional.of(listPautaEntity.get(1)));

		pautaservice.consultarPautasComSessaoAberta().flatMap(list -> {
			Assertions.assertNotNull(list);
			Assertions.assertFalse(list.isEmpty());
			return Mono.empty();
		}).subscribe();

	}

	@Test
	public void testeConsultarPautasComSessaoAbertaBadRequest() {

		List<Long> listaChaveSessao = new ArrayList<>();

		Mockito.when(sessaoService.recuperaSessoesAbertas()).thenReturn(Mono.just(listaChaveSessao));

		pautaservice.consultarPautasComSessaoAberta().onErrorResume(error -> {
			PautaException erro = (PautaException) error;
			Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), erro.getStatusCode());
			return Mono.empty();
		}).subscribe();

	}

	@Test
	public void testeVerificarPautaSessaoAbertaSucesso() {

		Mockito.when(sessaoService.verificarSessao("2")).thenReturn(Mono.just(Boolean.TRUE));

		pautaservice.verificarPautaSessaoAberta(2L).flatMap(ok -> {
			Assertions.assertNotNull(ok);
			Assertions.assertEquals(true, ok);
			return Mono.empty();
		}).subscribe();
	}

}
