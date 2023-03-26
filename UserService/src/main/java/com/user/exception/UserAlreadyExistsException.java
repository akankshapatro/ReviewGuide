package com.user.exception;

/*
 * Exception thrown to identify when user tries to register with a Email ID already registered before
*/
public class UserAlreadyExistsException extends RuntimeException {
	
	private String message;
	 
    public UserAlreadyExistsException() {}
 
    public UserAlreadyExistsException(String msg)
    {
        super(msg);
        this.message = msg;
    }

}
