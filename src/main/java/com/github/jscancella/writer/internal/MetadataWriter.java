package com.github.jscancella.writer.internal;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jscancella.domain.Metadata;
import com.github.jscancella.domain.Version;

/**
 * Responsible for writing out the bag {@link Metadata} to the filesystem
 */
public enum MetadataWriter {;//using enum to enforce singleton
  private static final Logger logger = LoggerFactory.getLogger(MetadataWriter.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  
  /**
   * Write the bag-info.txt (or package-info.txt) file to the specified outputDir with specified encoding (charsetName)
   * 
   * @param metadata the key value pair info in the bag-info.txt file
   * @param version the version of the bag you are writing
   * @param outputDir the root of the bag
   * @param charsetName the name of the encoding for the file
   * 
   * @throws IOException if there was a problem writing a file
   */
  public static Path writeBagMetadata(final Metadata metadata, final Version version, final Path outputDir, final Charset charsetName) throws IOException{
    Path bagInfoFilePath = outputDir.resolve("bag-info.txt");
    if(version.isSameOrOlder(Version.VERSION_0_95())){
      bagInfoFilePath = outputDir.resolve("package-info.txt");
    }
    logger.debug(messages.getString("writing_metadata_to_path"), bagInfoFilePath.getFileName(), outputDir);

    try(BufferedWriter writer = Files.newBufferedWriter(bagInfoFilePath, charsetName, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)){
      for(final SimpleImmutableEntry<String, String> entry : metadata.getAll()){
        final String line = entry.getKey() + ": " + entry.getValue() + System.lineSeparator();
        logger.debug(messages.getString("writing_line_to_file"), line, bagInfoFilePath);
        writer.append(line);
      }
    }
    
    return bagInfoFilePath;
  }
}
