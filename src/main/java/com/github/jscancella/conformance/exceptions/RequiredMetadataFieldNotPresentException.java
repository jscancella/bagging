package com.github.jscancella.conformance.exceptions;

import org.slf4j.helpers.MessageFormatter;

/**
 * Class to represent when a specific metadata field is not found
 */
public class RequiredMetadataFieldNotPresentException extends Exception {
private static final long serialVersionUID = 1L;
  
/**
 * Class to represent when a specific metadata field is not found
 * @param message error message for the user
 * @param bagInfoEntryRequirementKey the key in the bag metadata list of key value pairs
 */
  public RequiredMetadataFieldNotPresentException(final String message, final String bagInfoEntryRequirementKey) {
    super(MessageFormatter.format(message, bagInfoEntryRequirementKey).getMessage());
  }
}
