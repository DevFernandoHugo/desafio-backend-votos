package com.desafio.sessao.controller.test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.desafio.sessao.entity.Pauta;
import com.desafio.sessao.model.PautaVo;
import com.desafio.sessao.service.IPautaService;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("controller-test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PautaControllerUnitTest {

	@LocalServerPort
	private int port;

	@MockBean
	private IPautaService mockPautaservice;

	private PautaVo pautaVo;

	private Pauta pautaEntity;

	private List<Pauta> listPautaEntity;

	@BeforeEach
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
	public void tesRegistrarPautaSucesso() {

		Mockito.when(mockPautaservice.registrarPauta(pautaVo)).thenReturn(Mono.just(Boolean.TRUE));

		WebClient.builder().build().post().uri("http://localhost:" + port + "/pauta")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromPublisher(Mono.just(pautaVo), PautaVo.class)).retrieve()
				.bodyToMono(new ParameterizedTypeReference<Object>() {
				}).flatMap(response -> {
					Assert.assertNotNull(response);
					Assert.assertEquals(true, response);
					return Mono.just(response);
				}).block();
	}

	@Test
	public void tesRecuperaPautasSucesso() {

		Mockito.when(mockPautaservice.consultarPautas()).thenReturn(Mono.just(listPautaEntity));

		WebClient.builder().build().get().uri("http://localhost:" + port + "/pauta")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON)
				.retrieve().bodyToMono(new ParameterizedTypeReference<List<LinkedHashMap<?, ?>>>() {
				}).flatMap(response -> {
					Assert.assertNotNull(response);
					Assert.assertEquals("primeira Pauta", response.get(0).get("descricao").toString());
					return Mono.just(response);
				}).block();

	}

	@Test
	public void tesRecuperaPautaPorIdSucesso() {

		Long pautaId = 2L;
		Mockito.when(mockPautaservice.consultarPautaPorId(pautaId)).thenReturn(Mono.just(pautaEntity));

		WebClient.builder().build().get().uri("http://localhost:" + port + "/pauta/codigo/" + pautaId)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON)
				.retrieve().bodyToMono(new ParameterizedTypeReference<Object>() {
				}).flatMap(response -> {
					Assert.assertNotNull(response);
					return Mono.just(response);
				}).block();
	}
	
	@Test
	public void tesRecuperaPautasSessaoAbertaSucesso() {
		
		Mockito.when(mockPautaservice.consultarPautasComSessaoAberta()).thenReturn(Mono.just(listPautaEntity));

		WebClient.builder().build().get().uri("http://localhost:" + port + "/pauta/sessao")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON)
				.retrieve().bodyToMono(new ParameterizedTypeReference<List<LinkedHashMap<?, ?>>>() {
				}).flatMap(response -> {
					Assert.assertNotNull(response);				
					return Mono.just(response);
				}).block();
	}

}
