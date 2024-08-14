package com.fradantim.galcodechallenge.exception;

public class ElementAlreadyExists extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ElementAlreadyExists(String message) {
		super(message);
	}
}
