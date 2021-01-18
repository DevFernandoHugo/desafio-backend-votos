package com.desafio.sessao.service.test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import com.desafio.sessao.constant.MensagemVotacaoEnum;
import com.desafio.sessao.entity.Sessao;
import com.desafio.sessao.exception.DatabaseException;
import com.desafio.sessao.exception.PautaException;
import com.desafio.sessao.model.PautaVo;
import com.desafio.sessao.repository.IPautaRepository;
import com.desafio.sessao.service.IPautaService;
import com.desafio.sessao.service.PautaService;
import com.desafio.sessao.service.SessaoService;
import com.desafio.sessao.entity.Pauta;
import com.desafio.sessao.exception.SessaoException;

import reactor.core.publisher.Mono;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
	private SessaoService sessaoService;

	private Sessao sessaoRedis;

	private Pauta pautaEntity;

	private RedisTemplate<String, ?> redisTemplate;

	private static final String HASHKEY = "session";

	@MockBean
	private ValueOperations valueOperations;

	@Before
	public void init() {
		MockitoAnnotations.openMocks(this);
		criaSessao();
		criaPautaEntity();
	}

	private void criaSessao() {
		sessaoRedis = new Sessao();
		sessaoRedis.setPautaCodigo("2");
		sessaoRedis.setPrazo(60);
	}

	private void criaPautaEntity() {
		pautaEntity = new Pauta();
		pautaEntity.setCodigo(1L);
		pautaEntity.setDescricao("primeira Pauta");
	}

	@Test
	public void testeSalvarSessaoSucesso() {

		Long code = Long.parseLong(sessaoRedis.getPautaCodigo());

		redisTemplate = Mockito.mock(RedisTemplate.class);
		RedisConnectionFactory connectionFactory = Mockito.mock(RedisConnectionFactory.class);
		RedisConnection connection = Mockito.mock(RedisConnection.class);

		Mockito.when(redisTemplate.getConnectionFactory()).thenReturn(connectionFactory);
		Mockito.when(connectionFactory.getConnection()).thenReturn(connection);

		Mockito.when(redisTemplate.opsForSet()).thenReturn(Mockito.mock(SetOperations.class));
		Mockito.when(redisTemplate.opsForHash()).thenReturn(Mockito.mock(HashOperations.class));
		ReflectionTestUtils.setField(sessaoService, "redisTemplate", redisTemplate);

		Mockito.when(pautaRepository.findById(code)).thenReturn(Optional.of(pautaEntity));

		sessaoService.salvarSessao(sessaoRedis).map(ok -> {
			Assert.assertNotNull(ok);
			Assert.assertEquals(true, ok);
			return Mono.empty();
		}).subscribe();

	}

	@Test
	public void testeSalvarSessaoComTempoPadraoSucesso() {

		Sessao sessaoRedisPrazo = new Sessao();
		sessaoRedisPrazo.setPautaCodigo("2");

		Long code = Long.parseLong(sessaoRedisPrazo.getPautaCodigo());

		redisTemplate = Mockito.mock(RedisTemplate.class);
		RedisConnectionFactory connectionFactory = Mockito.mock(RedisConnectionFactory.class);
		RedisConnection connection = Mockito.mock(RedisConnection.class);

		Mockito.when(redisTemplate.getConnectionFactory()).thenReturn(connectionFactory);
		Mockito.when(connectionFactory.getConnection()).thenReturn(connection);

		Mockito.when(redisTemplate.opsForSet()).thenReturn(Mockito.mock(SetOperations.class));
		Mockito.when(redisTemplate.opsForHash()).thenReturn(Mockito.mock(HashOperations.class));
		ReflectionTestUtils.setField(sessaoService, "redisTemplate", redisTemplate);

		Mockito.when(pautaRepository.findById(code)).thenReturn(Optional.of(pautaEntity));

		sessaoService.salvarSessao(sessaoRedisPrazo).map(ok -> {
			Assert.assertNotNull(ok);
			Assert.assertEquals(true, ok);
			return Mono.empty();
		}).subscribe();
	}

	@Test
	public void testeSalvarSessaoComTempoZeroSucesso() {

		Sessao sessaoRedisPrazo = new Sessao();
		sessaoRedisPrazo.setPautaCodigo("2");
		sessaoRedisPrazo.setPrazo(0);

		Long code = Long.parseLong(sessaoRedisPrazo.getPautaCodigo());

		redisTemplate = Mockito.mock(RedisTemplate.class);
		RedisConnectionFactory connectionFactory = Mockito.mock(RedisConnectionFactory.class);
		RedisConnection connection = Mockito.mock(RedisConnection.class);

		Mockito.when(redisTemplate.getConnectionFactory()).thenReturn(connectionFactory);
		Mockito.when(connectionFactory.getConnection()).thenReturn(connection);

		Mockito.when(redisTemplate.opsForSet()).thenReturn(Mockito.mock(SetOperations.class));
		Mockito.when(redisTemplate.opsForHash()).thenReturn(Mockito.mock(HashOperations.class));
		ReflectionTestUtils.setField(sessaoService, "redisTemplate", redisTemplate);

		Mockito.when(pautaRepository.findById(code)).thenReturn(Optional.of(pautaEntity));

		sessaoService.salvarSessao(sessaoRedisPrazo).map(ok -> {
			Assert.assertNotNull(ok);
			Assert.assertEquals(true, ok);
			return Mono.empty();
		}).subscribe();

	}

	@Test
	public void testeSalvarSessaoComTempoBadRequest() {

		Mockito.when(pautaRepository.findById(2L)).thenReturn(Optional.empty());

		sessaoService.salvarSessao(sessaoRedis).onErrorResume(error -> {
			PautaException erro = (PautaException) error;
			assertEquals(HttpStatus.BAD_REQUEST.value(), erro.getStatusCode());
			return Mono.empty();
		}).subscribe();

	}

	@Test
	public void testeSalvarSessaoComErroGenerico() {

		Long code = Long.parseLong(sessaoRedis.getPautaCodigo());

		Mockito.when(pautaRepository.findById(code)).thenThrow(new SessaoException(
				MensagemVotacaoEnum.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR.value()));

		sessaoService.salvarSessao(sessaoRedis).onErrorResume(error -> {
			SessaoException erro = (SessaoException) error;
			assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), erro.getStatusCode());
			return Mono.empty();
		}).subscribe();

	}

	@Test
	public void testeRecuperaSessoesAbertasSucesso() {

		redisTemplate = Mockito.mock(RedisTemplate.class);
		RedisConnectionFactory connectionFactory = Mockito.mock(RedisConnectionFactory.class);
		RedisConnection connection = Mockito.mock(RedisConnection.class);

		Mockito.when(redisTemplate.getConnectionFactory()).thenReturn(connectionFactory);
		Mockito.when(connectionFactory.getConnection()).thenReturn(connection);

		Set<String> MockSet = (Set<String>) Mockito.mock(Set.class);
		MockSet.add("2");

		Mockito.when(redisTemplate.keys("*")).thenReturn(MockSet);
		Mockito.when(redisTemplate.opsForHash()).thenReturn(Mockito.mock(HashOperations.class));
		ReflectionTestUtils.setField(sessaoService, "redisTemplate", redisTemplate);

		sessaoService.recuperaSessoesAbertas().map(keys -> {
			Assert.assertNotNull(keys);
			return Mono.empty();
		}).subscribe();

	}

	@Test
	public void testeRecuperaSessoesAbertasErroGenerico() {

		redisTemplate = Mockito.mock(RedisTemplate.class);
		RedisConnectionFactory connectionFactory = Mockito.mock(RedisConnectionFactory.class);
		RedisConnection connection = Mockito.mock(RedisConnection.class);

		Mockito.when(redisTemplate.getConnectionFactory()).thenReturn(connectionFactory);
		Mockito.when(connectionFactory.getConnection()).thenReturn(connection);

		Set<String> MockSet = (Set<String>) Mockito.mock(Set.class);
		MockSet.add("2");

		ReflectionTestUtils.setField(sessaoService, "redisTemplate", redisTemplate);

		Mockito.when(redisTemplate.keys("*")).thenThrow(new DatabaseException(
				MensagemVotacaoEnum.SESSAO_NAO_ENCONTRADA.getValor(), HttpStatus.INTERNAL_SERVER_ERROR.value()));

		Mockito.when(redisTemplate.opsForHash()).thenReturn(Mockito.mock(HashOperations.class));

		sessaoService.recuperaSessoesAbertas().onErrorResume(error -> {
			DatabaseException erro = (DatabaseException) error;
			assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), erro.getStatusCode());
			return Mono.empty();
		}).subscribe();

	}

	@Test
	public void testeVerificarSeSessaoAbertaSucesso() {

		String chave = "2";

		redisTemplate = Mockito.mock(RedisTemplate.class);
		RedisConnectionFactory connectionFactory = Mockito.mock(RedisConnectionFactory.class);
		RedisConnection connection = Mockito.mock(RedisConnection.class);

		Mockito.when(redisTemplate.getConnectionFactory()).thenReturn(connectionFactory);
		Mockito.when(connectionFactory.getConnection()).thenReturn(connection);

		Mockito.when(redisTemplate.opsForHash()).thenReturn(Mockito.mock(HashOperations.class));
		Mockito.when(redisTemplate.opsForHash().get(chave, HASHKEY)).thenReturn(Mockito.mock(Object.class));

		ReflectionTestUtils.setField(sessaoService, "redisTemplate", redisTemplate);

		sessaoService.verificarSessao(chave).map(ok -> {
			Assert.assertNotNull(ok);
			Assert.assertEquals(true, ok);
			return Mono.empty();
		}).subscribe();

	}

	@Test
	public void testeVerificarSeSessaoAbertaBadRequest() {

		String chave = "2";

		redisTemplate = Mockito.mock(RedisTemplate.class);
		RedisConnectionFactory connectionFactory = Mockito.mock(RedisConnectionFactory.class);
		RedisConnection connection = Mockito.mock(RedisConnection.class);

		Mockito.when(redisTemplate.getConnectionFactory()).thenReturn(connectionFactory);
		Mockito.when(connectionFactory.getConnection()).thenReturn(connection);

		Mockito.when(redisTemplate.opsForHash()).thenReturn(Mockito.mock(HashOperations.class));

		ReflectionTestUtils.setField(sessaoService, "redisTemplate", redisTemplate);

		sessaoService.verificarSessao(chave).onErrorResume(error -> {
			SessaoException erro = (SessaoException) error;
			assertEquals(HttpStatus.BAD_REQUEST.value(), erro.getStatusCode());
			return Mono.empty();
		}).subscribe();

	}

	@Test
	public void testeVerificarSeSessaoAbertaErroGenerico() {

		String chave = "2";

		redisTemplate = Mockito.mock(RedisTemplate.class);
		RedisConnectionFactory connectionFactory = Mockito.mock(RedisConnectionFactory.class);
		RedisConnection connection = Mockito.mock(RedisConnection.class);

		Mockito.when(redisTemplate.getConnectionFactory()).thenReturn(connectionFactory);
		Mockito.when(connectionFactory.getConnection()).thenReturn(connection);

		Mockito.when(redisTemplate.opsForHash()).thenThrow(new DatabaseException(
				MensagemVotacaoEnum.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR.value()));

		ReflectionTestUtils.setField(sessaoService, "redisTemplate", redisTemplate);

		sessaoService.verificarSessao(chave).onErrorResume(error -> {
			DatabaseException erro = (DatabaseException) error;
			assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), erro.getStatusCode());
			return Mono.empty();
		}).subscribe();

	}

}
