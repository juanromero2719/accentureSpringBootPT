package com.pt.accenture.api;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex) {
		HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
		String path = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
		
		ErrorResponse errorResponse = new ErrorResponse(
				Instant.now(),
				status.value(),
				status.getReasonPhrase(),
				ex.getReason() != null ? ex.getReason() : status.getReasonPhrase(),
				path
		);
		
		return ResponseEntity.status(status).body(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		String path = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
		
		ErrorResponse errorResponse = new ErrorResponse(
				Instant.now(),
				status.value(),
				status.getReasonPhrase(),
				"Error interno del servidor",
				path
		);
		
		return ResponseEntity.status(status).body(errorResponse);
	}
}

