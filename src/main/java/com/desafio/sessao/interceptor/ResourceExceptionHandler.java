package com.desafio.sessao.interceptor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.sessao.exception.DatabaseException;
import com.desafio.sessao.exception.PautaException;

@ControllerAdvice
@RestController
public class ResourceExceptionHandler {

	@ExceptionHandler(PautaException.class)
	@ResponseBody
	public ResponseEntity<Object> handlePautaExceptionError(PautaException ex) {
		if (ex.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
			return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
		} else if (ex.getStatusCode() == HttpStatus.BAD_REQUEST.value()) {
			return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ExceptionHandler(DatabaseException.class)
	@ResponseBody
	public ResponseEntity<Object> handleInternalServerError(DatabaseException ex) {
		return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}



}