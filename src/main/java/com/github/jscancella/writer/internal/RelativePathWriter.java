package com.github.jscancella.writer.internal;

import java.nio.file.Path;

/**
 * util class to format strings correctly
 */
public enum RelativePathWriter { ; // enforce singleton

  /**
   * format the relative path to ensure it conforms to bagit spec
   * @param path the path to format
   * @return a string formated correctly according to the bagit specification
   */
  public static String formatRelativePathString(final Path path) {
    return encodeFilename(path).replace("\\", "/") + System.lineSeparator();
  }
  
  private static String encodeFilename(final Path path) {
    return path.toString()
            .replaceAll("%", "%25")
            .replaceAll("\n", "%0A")
            .replaceAll("\r", "%0D");
  }

}
