package com.github.jscancella.writer.internal;

import java.nio.file.Path;

/**
 * Convenience class for writing a relative path
 */
public enum RelativePathWriter { ;//using enum to enforce singleton
  
  /**
   * Create a relative path that has \ (windows) path separator replaced with / and encodes newlines
   * 
   * @param relativeTo the path to remove from the entry
   * @param entry the path to make relative
   * 
   * @return the relative path with only unix path separator
   */
  public static String formatRelativePathString(final Path relativeTo, final Path entry){
    final String encodedPath = encodeFilename(relativeTo.toAbsolutePath().relativize(entry.toAbsolutePath()));
    return encodedPath.replace('\\', '/') + System.lineSeparator();
  }
  
  /*
   * as per https://github.com/jkunze/bagitspec/commit/152d42f6298b31a4916ea3f8f644ca4490494070 encode any new lines or carriage returns
   */
  private static String encodeFilename(final Path path){
    return path.toString().replaceAll("\n", "%0A").replaceAll("\r", "%0D");
  }
}
