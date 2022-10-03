package com.github.jscancella.exceptions;

/**
 * Class to represent an error when a file is found in the payload directory but not in any manifest.
 * Opposite to {@link FileNotInPayloadDirectoryException}
 */
public class FileNotInManifestException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  /**
   * Class to represent an error when a file is found in the payload directory but not in any manifest.
   * Opposite to {@link FileNotInPayloadDirectoryException}
   * 
   * @param message error message for the user
   */
  public FileNotInManifestException(final String message){
    super(message);
  }
}
