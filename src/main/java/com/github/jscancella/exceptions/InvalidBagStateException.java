package com.github.jscancella.exceptions;

/**
 * For when a bag has somehow been created and is in a undefined state
 */
public class InvalidBagStateException extends RuntimeException{
  private static final long serialVersionUID = 1L;

  /**
   * For when a bag has somehow been created and is in a undefined state
   * 
   * @param message error message for user
   */
  public InvalidBagStateException(final String message) {
    super(message);
  }
}
