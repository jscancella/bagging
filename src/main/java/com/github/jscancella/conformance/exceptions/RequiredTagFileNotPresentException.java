package com.github.jscancella.conformance.exceptions;

import org.slf4j.helpers.MessageFormatter;

/**
 * Class to represent when a specific tag file is not found
 */
public class RequiredTagFileNotPresentException extends Exception {
private static final long serialVersionUID = 1L;
  
/**
 * Class to represent when a specific tag file is not found
 * @param message error message for the user
 * @param requiredTagFilePath the required tag file path
 */
  public RequiredTagFileNotPresentException(final String message, final String requiredTagFilePath) {
    super(MessageFormatter.format(message, requiredTagFilePath).getMessage());
  }
}
