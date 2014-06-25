package com.runner.architecture.util;

/**
 * Exception thrown when there has been an error with util component's 
 * input parameters. 
 *
 * @author keshavprasad.akasam
 *
 */
public class IncorrectInputParametersException extends RuntimeException {

	/**
	 * Constructor with the exception message as parameter
	 * 
	 * @param message
	 */
	public IncorrectInputParametersException(String message) {
		super(message);
	}
	
	/**
	 * Constructor with the exception message and the cause as parameters
	 * 
	 * @param message
	 * @param cause
	 */
	public IncorrectInputParametersException(String message, Throwable cause) {
		super(message, cause);
	}
}
