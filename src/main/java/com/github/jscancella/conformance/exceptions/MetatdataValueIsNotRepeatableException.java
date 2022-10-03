package com.github.jscancella.conformance.exceptions;

import org.slf4j.helpers.MessageFormatter;

/**
 * Class to represent when a metadata's value is not to be repeated
 */
public class MetatdataValueIsNotRepeatableException extends Exception {
private static final long serialVersionUID = 1L;
  
/**
 * Class to represent when a metadata's value is not to be repeated
 * 
 * @param message error message for the user
 * @param metadataKey the metadata key
 */
  public MetatdataValueIsNotRepeatableException(final String message, final String metadataKey) {
    super(MessageFormatter.format(message, metadataKey).getMessage());
  }
}
