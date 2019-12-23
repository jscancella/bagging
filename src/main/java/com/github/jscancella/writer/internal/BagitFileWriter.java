package com.github.jscancella.writer.internal;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jscancella.domain.Version;

/**
 * Responsible for writing the bagit.txt to the filesystem
 */
public enum BagitFileWriter {
  ;// using enum to enforce singleton
  private static final Logger logger = LoggerFactory.getLogger(BagitFileWriter.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");

  /**
   * Write the bagit.txt file in required UTF-8 encoding.
   * 
   * @param version
   *          the version of the bag to write out
   * @param encoding
   *          the encoding of the tag files
   * @param outputDir
   *          the root of the bag
   * @return the path of the newly created bagit.txt file
   * 
   * @throws IOException
   *           if there was a problem writing the file
   */
  public static Path writeBagitFile(final Version version, final Charset encoding, final Path outputDir)
      throws IOException{
    final Path bagitPath = outputDir.resolve("bagit.txt");
    logger.debug(messages.getString("write_bagit_file_to_path"), outputDir);

    try(BufferedWriter writer = Files.newBufferedWriter(bagitPath,
        StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)){
      final String firstLine = "BagIt-Version: " + version + System.lineSeparator();
      logger.debug(messages.getString("writing_line_to_file"), firstLine, bagitPath);
      writer.append(firstLine);
      final String secondLine = "Tag-File-Character-Encoding: " + encoding + System.lineSeparator();
      logger.debug(messages.getString("writing_line_to_file"), secondLine, bagitPath);
      writer.append(secondLine);
    }
    
    return bagitPath;
  }
}
