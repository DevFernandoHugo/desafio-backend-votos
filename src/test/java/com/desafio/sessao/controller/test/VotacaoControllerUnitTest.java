package com.desafio.sessao.controller.test;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.web.reactive.function.client.WebClient;

import com.desafio.sessao.model.VotacaoVo;
import com.desafio.sessao.service.IVotacaoService;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("controller-test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VotacaoControllerUnitTest {

	@LocalServerPort
	private int port;

	@MockBean
	private IVotacaoService votacaoService;

	private VotacaoVo votacao;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
		criaVotacaoModelo();
	}

	private void criaVotacaoModelo() {
		votacao = VotacaoVo.builder().codigoPauta(2L).DescricaoPauta("pauta teste").totalNao(2L).totalSim(3L)
				.totalVotos(5L).build();
	}

	@Test
	public void tesConsultaPautaPorCodigoSucesso() {

		Long codigoPauta = 2L;
		Mockito.when(votacaoService.recuperarVotacaoPauta(codigoPauta)).thenReturn(Mono.just(votacao));

		WebClient.builder().build().get().uri("http://localhost:" + port + "/votacao/resultado/" + codigoPauta)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON)
				.retrieve().bodyToMono(new ParameterizedTypeReference<Object>() {
				}).flatMap(response -> {
					Assertions.assertNotNull(response);
					return Mono.just(response);
				}).block();
	}

}
