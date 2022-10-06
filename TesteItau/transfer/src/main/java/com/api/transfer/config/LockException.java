package com.api.transfer.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.CONFLICT)
public class LockException extends RuntimeException {
	private static final long serialVersionUID = 1L;


	public LockException(String message) {
		super(message);



	}

}
