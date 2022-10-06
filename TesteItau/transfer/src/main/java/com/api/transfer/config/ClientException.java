package com.api.transfer.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClientException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	@Getter
	protected final String field;
	@Getter
	protected final Object parametro;

	public ClientException(String message, Object parametro, String field) {
		super(message);
		this.parametro = parametro;
		this.field = field;


	}

}
