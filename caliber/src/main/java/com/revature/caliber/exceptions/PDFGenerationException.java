package com.revature.caliber.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Failed to generate PDF report. Most likely client side generated malformed HTML.")
public class PDFGenerationException extends RuntimeException{
	private static final long serialVersionUID = 4073491812380749518L;
}
