package com.github.jscancella.exceptions;

import java.nio.file.Path;

import org.slf4j.helpers.MessageFormatter;

/**
 * The payload directory is a required file. This class represents the error if it is not found.
 */
public class MissingPayloadDirectoryException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  /**
   * The payload directory is a required file. This class represents the error if it is not found.
   * 
   * @param message error message for the user
   * @param path the missing path
   */
  public MissingPayloadDirectoryException(final String message, final Path path){
    super(MessageFormatter.format(message, path).getMessage());
  }
}
