package com.github.jscancella.exceptions;

/**
 * Class to represent an error when the calculated total bytes or number of files for 
 * the payload-oxum is different than the supplied values.
 */
public class InvalidPayloadOxumException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  /**
   * Class to represent an error when the calculated total bytes or number of files for 
   * the payload-oxum is different than the supplied values.
   * 
   * @param message error message for the user
   */
  public InvalidPayloadOxumException(final String message){
    super(message);
  }
}
