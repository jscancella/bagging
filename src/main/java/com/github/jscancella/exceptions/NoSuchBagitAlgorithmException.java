package com.github.jscancella.exceptions;

import com.github.jscancella.hash.BagitChecksumNameMapping;

/**
 * A class to represent the bagit algorithm name is not mapped. 
 * See {@link BagitChecksumNameMapping}
 */
public class NoSuchBagitAlgorithmException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  /**
   * A class to represent the bagit algorithm name is not mapped. 
   * See {@link BagitChecksumNameMapping}
   * 
   * @param message error message for the user
   */
  public NoSuchBagitAlgorithmException(final String message) {
    super(message);
  }
}
