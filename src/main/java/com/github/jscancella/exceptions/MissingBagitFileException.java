package com.github.jscancella.exceptions;

/**
 * The bagit.txt file is a required file. This class represents the error if that file is not present.
 */
public class MissingBagitFileException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public MissingBagitFileException(final String message){
    super(message);
  }
}
