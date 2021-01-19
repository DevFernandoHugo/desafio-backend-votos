package com.desafio.sessao.controller.test;

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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.desafio.sessao.entity.Voto;
import com.desafio.sessao.service.IVotoService;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("controller-test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VotoControllerUnitTest {
	
	@LocalServerPort
	private int port;
	
	@MockBean
	private IVotoService votoService;	
	
	private Voto voto;
	
	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
		criaVotoModelo();
	}
	
	private void criaVotoModelo() {
		voto = new Voto();
		voto.setCodigo(2L);
		voto.setCpf("68869572013");
		voto.setEscolha(true);
	}
	
	@Test
	public void testeCriarVotoSessaoSucesso() {

		Mockito.when(votoService.criarVoto(voto)).thenReturn(Mono.just(Boolean.TRUE));

		WebClient.builder().build().post().uri("http://localhost:" + port + "/voto")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromPublisher(Mono.just(voto), Voto.class)).retrieve()
				.bodyToMono(new ParameterizedTypeReference<Object>() {
				}).flatMap(response -> {
					Assert.assertNotNull(response);
					Assert.assertEquals(true, response);
					return Mono.empty();
				}).block();
	}

	

}
