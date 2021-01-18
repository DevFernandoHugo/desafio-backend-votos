package com.desafio.sessao.integration;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import com.desafio.sessao.constant.MensagemVotacaoEnum;
import com.desafio.sessao.exception.DatabaseException;
import com.desafio.sessao.exception.VotoException;

import reactor.core.publisher.Mono;

@Service
public class AssociadoService implements IAssociadoService {

	@Value("${integration.associado.url}")
	private String hostUrl;

	@Autowired
	public AssociadoService() {
	}

	@Override
	public Mono<Boolean> verificarAssociadoElegivel(String cpf) {

		WebClient client = WebClient.create(hostUrl);

		try {
			Map associado = client.get().uri("/users/" + cpf)
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(Map.class).block();

			String status = associado.get("status").toString();
			
			return status.equals("ABLE_TO_VOTE") ? Mono.just(Boolean.TRUE)
					: Mono.error(new VotoException(MensagemVotacaoEnum.VOTO_ASSOCIADO_NAO_ELEGIVEL.getValor(),
							HttpStatus.NOT_FOUND.value()));
		} catch (Exception e) {
			return Mono.error(new DatabaseException(MensagemVotacaoEnum.PAUTA_NAO_ENCONTRADA.getValor(),
					HttpStatus.INTERNAL_SERVER_ERROR.value()));
		}

	}

}
