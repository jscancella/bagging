package com.github.jscancella.exceptions;

/**
 * The bagit.txt file is a required file. This class represents the error if that file is not present.
 */
public class MissingBagitFileException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  /**
   * The bagit.txt file is a required file. This class represents the error if that file is not present.
   * 
   * @param message error message for the user
   */
  public MissingBagitFileException(final String message){
    super(message);
  }
}
