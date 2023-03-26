package com.post.exception;
/*
 * Exception thrown there is no post with the given id
*/
public class PostDoesNotExistsException extends RuntimeException {
	
	private String message;
	
	public PostDoesNotExistsException() {
		super();
	}

	public PostDoesNotExistsException(String message) {
		super();
		this.message = message;
	}

}
