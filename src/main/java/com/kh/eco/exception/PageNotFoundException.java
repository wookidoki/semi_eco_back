package com.kh.eco.exception;

public class PageNotFoundException extends RuntimeException {
	
	public PageNotFoundException(String message) {
		super(message);
	}
}
