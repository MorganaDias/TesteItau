package com.api.transfer.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.api.transfer.util.AppMessage;


@ControllerAdvice
@ResponseBody
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
	@Autowired
	AppMessage AppMessage;

	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
		return new MethodValidationPostProcessor();
	}

	private ErrorResponse getErrorResponse(String mensage, HttpStatus status, List<ObjectError> errors) {
		return ErrorResponse.builder().message(mensage).code(status.value()).status(status.getReasonPhrase())
				.error(errors).build();
	}

	private List<ObjectError> getErrors(MethodArgumentNotValidException ex) {
		return ex.getBindingResult().getFieldErrors().stream()
				.map(error -> new ObjectError(error.getDefaultMessage(), error.getField(), error.getRejectedValue()))
				.collect(Collectors.toList());
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<ObjectError> errors = getErrors(ex);
		ErrorResponse errorResponse = getErrorResponse(AppMessage.getBadRequest(), status, errors);
		return new ResponseEntity<>(errorResponse, status);
	}

	@ExceptionHandler(LockException.class)
	public final ResponseEntity<ErrorResponse> handleLockException(LockException ex, WebRequest request) {
		ObjectError details = new ObjectError(ex.getMessage(), "", "");
		ErrorResponse errorResponse = getErrorResponse(AppMessage.getConflict(), HttpStatus.CONFLICT,
				Arrays.asList(details));
		return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(InternalException.class)
	public final ResponseEntity<ErrorResponse> handleInternalExecption(InternalException ex, WebRequest request) {
		ObjectError details = new ObjectError(ex.getMessage(), "", "");
		ErrorResponse errorResponse = getErrorResponse(AppMessage.getInternalServerError(), HttpStatus.INTERNAL_SERVER_ERROR,
				Arrays.asList(details));
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ClientException.class)
	public final ResponseEntity<ErrorResponse> handleClienteException(ClientException ex, WebRequest request) {
		return HandleEx(ex, HttpStatus.BAD_REQUEST, AppMessage.getBadRequest());
	}

	@ExceptionHandler(ClientNotFoundException.class)
	public final ResponseEntity<ErrorResponse> handleClienteNotFoundException(ClientNotFoundException ex,
			WebRequest request) {
		return HandleEx(ex, HttpStatus.NOT_FOUND, AppMessage.getNotFound());
	}

	private ResponseEntity<ErrorResponse> HandleEx(ClientException ex, HttpStatus status, String mensagem) {
		ObjectError details = new ObjectError(ex.getMessage(), ex.getField(), ex.getParametro());
		ErrorResponse errorResponse = getErrorResponse(mensagem, status, Arrays.asList(details));
		return new ResponseEntity<>(errorResponse, status);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public final ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex,
			WebRequest request) {
		List<ObjectError> details = ex.getConstraintViolations().parallelStream()
				.map(error -> new ObjectError(error.getMessage(), error.getPropertyPath().toString(),
						error.getInvalidValue()))
				.collect(Collectors.toList());

		ErrorResponse error = getErrorResponse(AppMessage.getBadRequest(), HttpStatus.BAD_REQUEST, details);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
}