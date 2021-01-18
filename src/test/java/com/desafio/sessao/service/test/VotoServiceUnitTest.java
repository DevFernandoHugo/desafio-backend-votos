package com.desafio.sessao.service.test;

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
import org.springframework.test.context.ActiveProfiles;

import com.desafio.sessao.entity.Voto;
import com.desafio.sessao.integration.IAssociadoService;
import com.desafio.sessao.model.PautaVo;
import com.desafio.sessao.repository.IPautaRepository;
import com.desafio.sessao.repository.IVotoRepository;
import com.desafio.sessao.service.IPautaService;
import com.desafio.sessao.service.PautaService;
import com.desafio.sessao.service.SessaoService;

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

		Assert.assertEquals(true, true);

	}

}
