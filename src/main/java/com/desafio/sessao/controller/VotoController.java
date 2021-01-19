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

import com.desafio.sessao.entity.Voto;
import com.desafio.sessao.service.IVotoService;

@RestController
@RequestMapping("/voto")
@CrossOrigin(origins = "*")
public class VotoController {

	private static final Logger LOG = LoggerFactory.getLogger(VotoController.class);

	@Autowired
	private IVotoService votoService;

	@PostMapping
	@ApiOperation(value = "Criar voto")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "voto criado com sucesso"),
			@ApiResponse(code = 400, message = "Erro ao tentar criar voto"),
			@ApiResponse(code = 404, message = "Erro parâmetro inválido"),
			@ApiResponse(code = 500, message = "Erro inesperado") })
	public Mono<ResponseEntity<Boolean>> criaVoto(@RequestBody Voto voto) {
		LOG.debug("Controlador envia pauta");
		return votoService.criarVoto(voto).map(votoRealizado -> {
			return ResponseEntity.status(HttpStatus.OK).body(votoRealizado);
		});
	}

}
