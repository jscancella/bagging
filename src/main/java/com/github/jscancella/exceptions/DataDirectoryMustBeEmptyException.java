package com.github.jscancella.exceptions;

import com.github.jscancella.domain.Bag;

/**
 * The {@link Bag} object must have a empty data directory, 
 * this class represents the error when the data directory contains multiple zero byte files OR any files that are bigger than zero bytes.
 */
public class DataDirectoryMustBeEmptyException extends Exception {
  private static final long serialVersionUID = 1L;

  /**
   * The {@link Bag} object must have a empty data directory, 
   * this class represents the error when the data directory contains multiple zero byte files OR any files that are bigger than zero bytes.
   * 
   * @param message error message for the user
   */
  public DataDirectoryMustBeEmptyException(final String message){
    super(message);
  }
}
