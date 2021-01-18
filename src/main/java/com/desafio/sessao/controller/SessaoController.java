package com.desafio.sessao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Mono;
import io.swagger.annotations.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.sessao.entity.Sessao;
import com.desafio.sessao.service.ISessaoService;

@RestController
@RequestMapping("/sessao")
@CrossOrigin(origins = "*")
public class SessaoController {

	private static final Logger LOG = LoggerFactory.getLogger(SessaoController.class);

	@Autowired
	private ISessaoService sessaoService;

	@PostMapping
	@ApiOperation(value = "Abrir sessão")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "pauta sessao aberta com sucesso"),
			@ApiResponse(code = 400, message = "Erro ao tentar abrir sessão"),
			@ApiResponse(code = 500, message = "Erro inesperado") })
	public Mono<ResponseEntity<Boolean>> abrirSessao(@RequestBody Sessao sessao) {
		LOG.debug("Controlador envia pauta");
		return sessaoService.salvarSessao(sessao).map(pautas -> {
			int a =1;
			return ResponseEntity.status(HttpStatus.OK).body(pautas);
		});
	}

}
