package com.github.jscancella.exceptions;

import org.slf4j.helpers.MessageFormatter;

/**
 * If the version string in the bagit.txt file was not in the form &lt;MAJOR&gt;.&lt;MINOR&gt; 
 */
public class UnparsableVersionException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  /**
   * If the version string in the bagit.txt file was not in the form &lt;MAJOR&gt;.&lt;MINOR&gt;
   * 
   * @param message error message for the user
   * @param version the version that was unparsable
   */
  public UnparsableVersionException(final String message, final String version){
    super(MessageFormatter.format(message, version).getMessage());
  }
}
