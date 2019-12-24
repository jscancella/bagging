package com.github.jscancella.exceptions;

/**
 * Class to represent an error when a specific bag file does not conform to its bagit specfication format
 */
public class InvalidBagitFileFormatException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public InvalidBagitFileFormatException(final String message){
    super(message);
  }
  
  public InvalidBagitFileFormatException(final String message, final Exception exception){
    super(message, exception);
  }
}
