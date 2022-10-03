package com.github.jscancella.conformance.exceptions;

import java.util.List;

import org.slf4j.helpers.MessageFormatter;

/**
 * Class to represent when a metadata's value is not in the acceptable list of values
 */
public class MetatdataValueIsNotAcceptableException extends Exception {
private static final long serialVersionUID = 1L;
  
/**
 * Class to represent when a metadata's value is not in the acceptable list of values
 * 
 * @param message error message for the user
 * @param metadataKey the metadata key
 * @param acceptableValues the acceptable values associated with the key
 * @param actualValue the value that was found
 */
  public MetatdataValueIsNotAcceptableException(final String message, final String metadataKey, final List<String> acceptableValues, final String actualValue) {
    super(MessageFormatter.arrayFormat(message, new Object[]{metadataKey, acceptableValues, actualValue}).getMessage());
  }
}
