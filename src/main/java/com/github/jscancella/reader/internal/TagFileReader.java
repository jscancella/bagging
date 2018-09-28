package com.github.jscancella.reader.internal;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import com.github.jscancella.exceptions.InvalidBagitFileFormatException;
import com.github.jscancella.exceptions.MaliciousPathException;

/**
 * Convenience class for reading tag files from the filesystem
 */
public enum TagFileReader {;//using enum to enforce singleton
  private static final Logger logger = LoggerFactory.getLogger(TagFileReader.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  
  /*
   * Create the file and check it for various things, like starting with a *, or trying to access a file outside the bag
   */
  public static Path createFileFromManifest(final Path bagRootDir, final String path) throws MaliciousPathException, InvalidBagitFileFormatException{
    String fixedPath = path;
    if(path.charAt(0) == '*'){
      logger.warn(messages.getString("removing_asterisk"));
      fixedPath = path.substring(1); //remove the * from the path
    }
    
    if(path.contains("\\")){
      final String formattedMessage = messages.getString("blackslash_used_as_path_separator_error");
      throw new InvalidBagitFileFormatException(MessageFormatter.format(formattedMessage, path).getMessage());
    }
    
    if(path.contains("~/")){
      final String formattedMessage = messages.getString("malicious_path_error");
      throw new MaliciousPathException(MessageFormatter.format(formattedMessage, path).getMessage());
    }

    fixedPath = Reader.decodeFilname(fixedPath);
    Path file;
    if(fixedPath.startsWith("file://")){
      try {
        file = Paths.get(new URI(fixedPath));
      } catch (URISyntaxException e) {
        final String formattedMessage = messages.getString("invalid_url_format_error");
        throw new InvalidBagitFileFormatException(MessageFormatter.format(formattedMessage, path).getMessage(), e);
      }
    }
    else{
      file = bagRootDir.resolve(fixedPath).normalize();
    }
    
    if(!file.normalize().startsWith(bagRootDir)){
      final String formattedMessage = messages.getString("malicious_path_error");
      throw new MaliciousPathException(MessageFormatter.format(formattedMessage, file).getMessage());
    }
    
    return file;
  }
}
