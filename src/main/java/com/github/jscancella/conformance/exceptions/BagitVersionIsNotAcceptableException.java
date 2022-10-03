package com.github.jscancella.conformance.exceptions;

import java.util.List;

import org.slf4j.helpers.MessageFormatter;

import com.github.jscancella.domain.Version;

/**
 * Class to represent when the bag's version is not in the acceptable list of versions
 */
public class BagitVersionIsNotAcceptableException extends Exception {
private static final long serialVersionUID = 1L;
  
/**
 * Class to represent when the bag's version is not in the acceptable list of versions
 * 
 * @param message the message to give to the user
 * @param version the version that was found
 * @param acceptableVersions the list of acceptable versions
 */
  public BagitVersionIsNotAcceptableException(final String message, final Version version, final List<String> acceptableVersions) {
    super(MessageFormatter.format(message, version, acceptableVersions).getMessage());
  }
}
