package com.github.jscancella.reader.internal;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import com.github.jscancella.domain.Version;
import com.github.jscancella.exceptions.InvalidBagitFileFormatException;
import com.github.jscancella.exceptions.MaliciousPathException;

/**
 * Convenience class for reading tag files from the filesystem
 */
@SuppressWarnings("PMD.AvoidLiteralsInIfCondition")
public enum TagFileReader {;//using enum to enforce singleton
  private static final Logger logger = LoggerFactory.getLogger(TagFileReader.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  
  /**
   * Create the file and check it for various things, like starting with a *, or trying to access a file outside the bag
   * 
   * @param bagRootDir the base directory of the bag
   * 
   * @param path the path listed in the manifest
   * 
   * @param version the version of the bag
   * 
   * @param charset the encoding that was used to write this tag file
   * 
   * @return a {@link Path} object
   * 
   * @throws MaliciousPathException if the path is trying to reference a place outside the bag
   * @throws InvalidBagitFileFormatException if the path is invalid
   */
  public static Path createFileFromManifest(final Path bagRootDir, final String path, final Version version, final Charset charset) {
    checkPathSeparator(path);
    checkTildaMaliciousPath(path);

    String fixedPath = removeAsteriskIfExists(path);

    fixedPath = decodeFilname(fixedPath, version, charset);
    final Path file = createPath(fixedPath, bagRootDir);
    
    checkNormalizedPathIsInBag(file, bagRootDir);

    return file;
  }

  private static void checkPathSeparator(final String path){
    if(path.contains("\\")){
      final String formattedMessage = messages.getString("blackslash_used_as_path_separator_error");
      throw new InvalidBagitFileFormatException(MessageFormatter.format(formattedMessage, path).getMessage());
    }
  }

  private static void checkTildaMaliciousPath(final String path){
    if(path.contains("~/")){
      final String formattedMessage = messages.getString("malicious_path_error");
      throw new MaliciousPathException(MessageFormatter.format(formattedMessage, path).getMessage());
    }
  }

  private static String removeAsteriskIfExists(final String path){
    if(path.charAt(0) == '*'){
      logger.warn(messages.getString("removing_asterisk"));
    }
    return path.replaceFirst("\\*", "");
  }
  
  /**
   * as per https://github.com/jkunze/bagitspec/commit/152d42f6298b31a4916ea3f8f644ca4490494070 decode percent encoded filenames
   * 
   * @param encoded the encoded filename
   * @param version the bagit version
   * @param charset the charset the bag is using
   * @return the decoded filename
   */
  protected static String decodeFilname(final String encoded, final Version version, final Charset charset){
    String decoded = encoded.replaceAll("%0A", "\n").replaceAll("%0D", "\r");
    if(version.isSameOrNewer(Version.VERSION_1_0())) {
      decoded = decoded.replaceAll("%25", "%");
    }
    logger.debug(messages.getString("percent_encoded"), encoded, decoded);
    return decoded;
  }

  @SuppressWarnings("PMD.OnlyOneReturn")
  private static Path createPath(final String path, final Path bagRootDir){
    if(path.startsWith("file://")){
      try {
        return Paths.get(new URI(path));
      } catch (URISyntaxException e) {
        final String formattedMessage = messages.getString("invalid_url_format_error");
        throw new InvalidBagitFileFormatException(MessageFormatter.format(formattedMessage, path).getMessage(), e);
      }
    }
    return bagRootDir.resolve(path).normalize();
  }

  private static void checkNormalizedPathIsInBag(final Path file, final Path bagRootDir){
    if(!file.normalize().startsWith(bagRootDir)){
      final String formattedMessage = messages.getString("malicious_path_error");
      throw new MaliciousPathException(MessageFormatter.format(formattedMessage, file).getMessage());
    }
  }
}
