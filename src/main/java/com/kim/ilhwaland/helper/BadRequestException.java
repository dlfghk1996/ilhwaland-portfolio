package com.kim.ilhwaland.helper;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

/** 커스텀 예외 **/

@ResponseStatus(code=HttpStatus.BAD_REQUEST)
public class BadRequestException extends Exception {
	private static final long serialVersionUID = 1L;

	public BadRequestException() {
	}
}