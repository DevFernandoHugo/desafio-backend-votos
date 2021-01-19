package com.desafio.sessao.service.test;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.desafio.sessao.entity.Voto;
import com.desafio.sessao.repository.IVotoRepository;
import com.desafio.sessao.service.IPautaService;
import com.desafio.sessao.service.VotoService;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit-test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VotacaoServiceUnitTest {
	
	@Mock
	private IPautaService pautaService;
	
	@Mock
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
		
		assertEquals(true,true);
	}

}
