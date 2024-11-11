package com.cardinal.assessment.exception;

/**
 * Exception resulting from the tool rental checkout process
 */
public class CheckoutException extends Exception {

	public CheckoutException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 1L;
}
