package com.github.jscancella.exceptions;

/**
 * Class to represent an generic exception that happened during verification.
 */
public class VerificationException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public VerificationException(final Exception exception){
    super(exception);
  }
}
