package com.github.jscancella.writer.internal;

import java.nio.file.Path;

public interface Writer {
  /**
   * as per https://github.com/jkunze/bagitspec/commit/152d42f6298b31a4916ea3f8f644ca4490494070 encode any new lines or carriage returns
   * @param path the path to encode
   * @return the encoded filename
   */
  public static String encodeFilename(final Path path){
    return path.toString().replaceAll("\n", "%0A").replaceAll("\r", "%0D");
  }
}
