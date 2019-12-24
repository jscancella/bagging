package com.github.jscancella.exceptions;

/**
 * A bagit bag needs at least one payload manifest. This class represents the error if at least one payload manifest isn't found.
 */
public class MissingPayloadManifestException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public MissingPayloadManifestException(final String message){
    super(message);
  }
}
