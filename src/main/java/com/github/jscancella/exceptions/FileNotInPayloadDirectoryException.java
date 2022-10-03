package com.github.jscancella.exceptions;

/**
 * Class to represent an error when a file is not in the payload directory but is listed in a manifest.
 * Opposite to {@link FileNotInManifestException}
 */
public class FileNotInPayloadDirectoryException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  /**
   * Class to represent an error when a file is not in the payload directory but is listed in a manifest.
   * Opposite to {@link FileNotInManifestException}
   *
   * @param message error message for the user
   */
  public FileNotInPayloadDirectoryException(final String message){
    super(message);
  }
}
