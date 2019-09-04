package com.revature.caliber.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST)
public class RevProIntegrationException extends RuntimeException{

	private static final long serialVersionUID = 5516550110589099501L;
	
	public RevProIntegrationException() {
		super();
	}
	
	public RevProIntegrationException(String message) {
		super(message);
	}
	
}
