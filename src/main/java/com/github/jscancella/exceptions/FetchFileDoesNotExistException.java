package com.github.jscancella.exceptions;

import java.nio.file.Path;

import org.slf4j.helpers.MessageFormatter;

import com.github.jscancella.domain.Bag;

/**
 * The {@link Bag} object should contain the fetch.txt file, 
 * this class represents the error when fetch.txt doesn't exist.
 */
public class FetchFileDoesNotExistException extends Exception {
  private static final long serialVersionUID = 1L;

  /**
   * The {@link Bag} object should contain the fetch.txt file, 
   * this class represents the error when fetch.txt doesn't exist.
   * 
   * @param message error message for the user
   * @param rootDir the bag root directory
   */
  public FetchFileDoesNotExistException(final String message, final Path rootDir){
    super(MessageFormatter.format(message, rootDir).getMessage());
  }
}
