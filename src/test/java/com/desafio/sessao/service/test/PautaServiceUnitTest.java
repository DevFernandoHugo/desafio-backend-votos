package com.desafio.sessao.service.test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import com.desafio.sessao.repository.IPautaRepository;
import com.desafio.sessao.service.PautaService;
import com.desafio.sessao.exception.PautaException;
import com.desafio.sessao.entity.Pauta;
import com.desafio.sessao.model.PautaVo;

import reactor.core.publisher.Mono;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

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

	@InjectMocks
	private PautaService pautaservice;
	
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
		pautaVo.setValor("Pauta Teste");		
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
	public void testConsultarPautas() {		
		
		Mockito.when(pautaRepository.findAll()).thenReturn(listPautaEntity);
		
		pautaservice.consultarPautas().flatMap(list -> {
			Assert.assertNotNull(list);
			Assert.assertFalse(list.isEmpty());
			return Mono.empty();
		}).subscribe();
	
	}
	
	@Test
	public void testConsultarPautasNotFound() {
		
		List<Pauta> listEmpty = new ArrayList<>();
		
		Mockito.when(pautaRepository.findAll()).thenReturn(listEmpty);
		
		pautaservice.consultarPautas().onErrorResume(error -> {
			PautaException erro = (PautaException) error;
			assertEquals(HttpStatus.NOT_FOUND.value(), erro.getStatusCode());
			return Mono.empty();
		}).subscribe();
	
	}

}
