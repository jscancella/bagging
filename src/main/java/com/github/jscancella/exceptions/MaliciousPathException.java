package com.github.jscancella.exceptions;

/**
 * Class to represent an error when the path in a manifest or fetch file has been crafted to point to a file or 
 * directory outside the bag. Most likely to try and overwrite an important system file.
 */
public class MaliciousPathException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  /**
   * Class to represent an error when the path in a manifest or fetch file has been crafted to point to a file or 
   * directory outside the bag. Most likely to try and overwrite an important system file.
   * 
   * @param message error message for the user
   */
  public MaliciousPathException(final String message){
    super(message);
  }
}
