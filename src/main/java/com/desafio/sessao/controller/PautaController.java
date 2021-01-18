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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.sessao.entity.Pauta;
import com.desafio.sessao.model.PautaVo;
import com.desafio.sessao.service.IPautaService;

import java.util.List;

@RestController
@RequestMapping("/pauta")
@CrossOrigin(origins = "*")
public class PautaController {

	private static final Logger LOG = LoggerFactory.getLogger(PautaController.class);

	@Autowired
	private IPautaService pautaService;

	@GetMapping
	@ApiOperation(value = "Recuperar pautas cadastradas")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "pauta recuperarda com sucesso"),
			@ApiResponse(code = 400, message = "Erro ao tentar recuperar pautas cadastradas"),
			@ApiResponse(code = 500, message = "Erro inesperado") })
	public Mono<ResponseEntity<List<Pauta>>> recuperaPautas() {
		LOG.debug("Controlador envia pauta");
		return pautaService.consultarPautas().map(pautas -> {
			return ResponseEntity.status(HttpStatus.OK).body(pautas);
		});
	}

	@PostMapping
	@ApiOperation(value = "Cadastrar pauta")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "pauta cadastrada com sucesso"),
			@ApiResponse(code = 400, message = "Erro ao tentar cadastrar pauta"),
			@ApiResponse(code = 500, message = "Erro inesperado") })
	public Mono<ResponseEntity<Boolean>> cadastraPauta(@RequestBody PautaVo pauta) {
		LOG.debug("Controlador envia pauta");
		return pautaService.registrarPauta(pauta).map(pautas -> {
			return ResponseEntity.status(HttpStatus.OK).body(pautas);
		});
	}

	@GetMapping("/codigo/{idPauta}")
	@ApiOperation(value = "Obter pauta por código")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Sucesso ao obter pauta"),
			@ApiResponse(code = 400, message = "Erro ao tentar recuperar pauta"),
			@ApiResponse(code = 500, message = "Erro genérico ocorrido do bakcend") })
	public Mono<ResponseEntity<Pauta>> consultaPautaPorCodigo(@PathVariable Long idPauta) {
		LOG.debug("Controlador envia pauta");
		return pautaService.consultarPautaPorId(idPauta).map(pauta -> {
			return ResponseEntity.status(HttpStatus.OK).body(pauta);
		});
	}

	@GetMapping("/sessao")
	@ApiOperation(value = "Recuperar pautas com sessão aberta")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "pauta(s) recuperarda(s) com sucesso"),
			@ApiResponse(code = 400, message = "Erro ao tentar recuperar pauta(s) com sessão aberta"),
			@ApiResponse(code = 500, message = "Erro inesperado") })
	public Mono<ResponseEntity<List<Pauta>>> recuperaPautasSessaoAberta() {
		LOG.debug("Controlador envia pauta");
		return pautaService.consultarPautasComSessaoAberta().map(pautas -> {
			return ResponseEntity.status(HttpStatus.OK).body(pautas);
		});
	}
}
