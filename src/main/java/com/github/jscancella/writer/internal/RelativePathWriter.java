package com.github.jscancella.writer.internal;

import java.nio.file.Path;

public enum RelativePathWriter { ; // enforce singleton

  /**
   * format the relative path to ensure it conforms to bagit spec
   */
  public static String formatRelativePathString(final Path path) {
    return encodeFilename(path).replace("\\", "/") + System.lineSeparator();
  }
  
  private static String encodeFilename(final Path path) {
    return path.toString().replaceAll("\n", "%0A").replaceAll("\r", "%0D");
  }

}
