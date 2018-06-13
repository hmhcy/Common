package com.hxhy.config.exception;

public class LoginAttempFailureException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5708879678025194865L;

	public LoginAttempFailureException() {
		super("尝试登陆次数超出限制");
	}
	
	public LoginAttempFailureException(String s) {
		super(s);
	}
}
