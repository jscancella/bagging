package com.github.jscancella.conformance.exceptions;

/**
 * Class to represent when a specific manifest type is not found, such as md5, sha1, etc (payload or tag)
 */
public class RequiredManifestNotPresentException extends Exception {
private static final long serialVersionUID = 1L;
  
/**
 * Class to represent when a specific manifest type is not found, such as md5, sha1, etc (payload or tag)
 * 
 * @param message error message for the user
 */
  public RequiredManifestNotPresentException(final String message) {
    super(message);
  }
}
