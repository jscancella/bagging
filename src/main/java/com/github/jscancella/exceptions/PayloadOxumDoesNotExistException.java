package com.github.jscancella.exceptions;

import com.github.jscancella.domain.Bag;

/**
 * The {@link Bag} object should contain the Payload-Oxum metatdata key value pair, 
 * this class represents the error when trying to calculate the payload-oxum and it doesn't exist on the bag object.
 */
public class PayloadOxumDoesNotExistException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  /**
   * The {@link Bag} object should contain the Payload-Oxum metatdata key value pair, 
   * this class represents the error when trying to calculate the payload-oxum and it doesn't exist on the bag object.
   * 
   * @param message error message for the user
   */
  public PayloadOxumDoesNotExistException(final String message){
    super(message);
  }
}
