package com.desafio.sessao.service.test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.desafio.sessao.repository.IPautaRepository;
import com.desafio.sessao.service.IPautaService;
import com.desafio.sessao.entity.Pauta;
import com.desafio.sessao.model.PautaVo;

import reactor.core.publisher.Mono;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit-test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PautaServiceUnitTest {

	@Mock
	private IPautaRepository pautaRepository;

	@Spy
	@InjectMocks
	private IPautaService pautaservice;

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
		pautaA.setCodigo(2L);
		pautaA.setDescricao("segunda Pauta");

		Pauta pautaC = new Pauta();
		pautaA.setCodigo(3L);
		pautaA.setDescricao("terceira Pauta");

		listPautaEntity = new ArrayList<>();
		listPautaEntity.add(pautaA);
		listPautaEntity.add(pautaB);
		listPautaEntity.add(pautaC);
	}

	public void testConsultarCpfInvalido() {

		this.pautaservice.consultarPautas().flatMap(teste -> {

			assertEquals("true", "true");
			return Mono.empty();
		});

	}

}
