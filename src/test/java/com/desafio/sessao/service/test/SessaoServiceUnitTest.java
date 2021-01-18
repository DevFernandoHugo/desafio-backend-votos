package com.desafio.sessao.service.test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import com.desafio.sessao.constant.MensagemVotacaoEnum;
import com.desafio.sessao.entity.Sessao;
import com.desafio.sessao.exception.DatabaseException;
import com.desafio.sessao.exception.PautaException;
import com.desafio.sessao.model.PautaVo;
import com.desafio.sessao.repository.IPautaRepository;
import com.desafio.sessao.service.IPautaService;
import com.desafio.sessao.service.PautaService;

import reactor.core.publisher.Mono;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit-test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SessaoServiceUnitTest {

	
	@Mock
	private IPautaRepository pautaRepository;
	
	@InjectMocks
	private SessaoService sessaoservice;
	

	@Before
	public void init() {
		MockitoAnnotations.openMocks(this);


	}


}
