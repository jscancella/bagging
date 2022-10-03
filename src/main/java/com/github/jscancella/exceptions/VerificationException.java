package com.github.jscancella.exceptions;

/**
 * Class to represent an generic exception that happened during verification.
 */
public class VerificationException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  /**
   * Class to represent an generic exception that happened during verification.
   * 
   * @param exception the exception that occurred
   */
  public VerificationException(final Exception exception){
    super(exception);
  }
}
