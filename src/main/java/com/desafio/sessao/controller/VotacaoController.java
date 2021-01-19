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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.sessao.model.VotacaoVo;
import com.desafio.sessao.service.IVotacaoService;

@RestController
@RequestMapping("/votacao")
@CrossOrigin(origins = "*")
public class VotacaoController {

	private static final Logger LOG = LoggerFactory.getLogger(VotacaoController.class);

	@Autowired
	private IVotacaoService votacaoService;

	@GetMapping("/resultado/{codigoPauta}")
	@ApiOperation(value = "Recuperar resultado votação")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "votação recuperada com sucesso"),
			@ApiResponse(code = 400, message = "Erro ao tentar Recuperar resultado votação"),
			@ApiResponse(code = 404, message = "Erro parâmetro inválido"),
			@ApiResponse(code = 500, message = "Erro inesperado") })
	public Mono<ResponseEntity<VotacaoVo>> consultaPautaPorCodigo(@PathVariable Long codigoPauta) {
		LOG.debug("Controlador envia pauta");
		return votacaoService.recuperarVotacaoPauta(codigoPauta).map(votacao -> {
			return ResponseEntity.status(HttpStatus.OK).body(votacao);
		});
	}

}
