package com.github.jscancella.exceptions;

import java.nio.file.Path;

import org.slf4j.helpers.MessageFormatter;

/**
 * Class to represent an error when the calculated checksum is different than the manifest specified checksum.
 */
public class CorruptChecksumException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  /**
   * Class to represent an error when the calculated checksum is different than the manifest specified checksum.
   * 
   * @param message the message to give to the user
   * @param path the path of the file
   * @param algorithm the checksum algorithm used
   * @param hash the expected hash
   * @param computedHash the actual hash
   */
  public CorruptChecksumException(final String message, final Path path, final String algorithm, final String hash, final String computedHash){
    super(MessageFormatter.arrayFormat(message, new Object[]{path, algorithm, hash, computedHash}).getMessage());
  }
}
