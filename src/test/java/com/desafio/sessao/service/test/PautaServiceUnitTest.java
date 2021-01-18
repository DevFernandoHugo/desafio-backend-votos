package com.desafio.sessao.service.test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.desafio.sessao.repository.IPautaRepository;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit-test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PautaServiceUnitTest {
	
	@Mock
	private IPautaRepository pautaRepository;
	

}
