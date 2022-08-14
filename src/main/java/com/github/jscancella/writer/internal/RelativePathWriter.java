package com.github.jscancella.writer.internal;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jscancella.domain.Version;

/**
 * util class to format strings correctly
 */
public enum RelativePathWriter { ; // enforce singleton
  private static final Logger logger = LoggerFactory.getLogger(RelativePathWriter.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");

  /**
   * format the relative path to ensure it conforms to bagit spec
   * @param path the path to format
   * @param version the version of the bag
   * @param charset the charset you which wish to use to write the path
   * @return a string formated correctly according to the bagit specification
   */
  public static String formatRelativePathString(final Path path, final Version version, final Charset charset) {
    return encodeFilename(path, version, charset).replace("\\", "/") + System.lineSeparator();
  }
  
  /**
   * as per https://github.com/jkunze/bagitspec/commit/152d42f6298b31a4916ea3f8f644ca4490494070 encode any new lines or carriage returns
   * 
   * @param path the path that you want to encode
   * @param version the version of the bag
   * @param charset the charset you which wish to use to write the path
   * @return percent encoded path
   */
  public static String encodeFilename(final Path path, final Version version, final Charset charset) {
    String encodedPath = URLEncoder.encode(path.toString(), charset).replace("+", "%20").replace("%5C", "/");
    if(version.isOlder(Version.VERSION_1_0())) {
      encodedPath = path.toString().replaceAll("\n", "%0A").replaceAll("\r", "%0D");
    }
    logger.debug(messages.getString("encoded_path"), path.toString(), encodedPath);
    return encodedPath;
  }

}
