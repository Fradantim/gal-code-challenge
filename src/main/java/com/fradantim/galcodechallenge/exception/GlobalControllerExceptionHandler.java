package com.fradantim.galcodechallenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST) // 400
	@ExceptionHandler(ElementAlreadyExists.class)
	public ProblemDetail handleConflict(ElementAlreadyExists ex) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
	}

	@ResponseStatus(HttpStatus.NOT_FOUND) // 404
	@ExceptionHandler(ElementNotFound.class)
	public ProblemDetail handleConflict(ElementNotFound ex) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
	}
}
