package com.desafio.sessao.controller.test;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.desafio.sessao.entity.Sessao;
import com.desafio.sessao.service.SessaoService;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("controller-test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SessaoControllerUnitTest {	
	
	@Rule
	public MockitoRule rule = MockitoJUnit.rule().silent();

	@LocalServerPort
	private int port;
	
	@Mock
	private SessaoService sessaoService;
	
	private Sessao sessaoRedis;
	
	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
		criaSessao();
	}
	
	private void criaSessao() {
		sessaoRedis = new Sessao();
		sessaoRedis.setPautaCodigo("2");
		sessaoRedis.setPrazo(60);
	}
	
	@Test
	public void testeSalvarSessaoSucesso() {

		Mockito.lenient().when(sessaoService.salvarSessao(sessaoRedis)).thenReturn(Mono.just(Boolean.TRUE));

		WebClient.builder().build().post().uri("http://localhost:" + port + "/sessao")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromPublisher(Mono.just(sessaoRedis), Sessao.class)).retrieve()
				.bodyToMono(new ParameterizedTypeReference<Object>() {
				}).flatMap(response -> {
					Assertions.assertNotNull(response);
					Assertions.assertEquals(true, response);
					return Mono.empty();
				}).block();
	}

}
