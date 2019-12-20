package com.github.jscancella.writer.internal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Responsible for writing out the bag payload to the filesystem
 */
public enum PayloadWriter {;//using enum to enforce singleton
  private static final Logger logger = LoggerFactory.getLogger(PayloadWriter.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  
  public static void writePayloadFiles(final Set<Path> payloadFiles, final Path outputDir) throws IOException {
    final Path dataDir = outputDir.resolve("data");
    for(Path payloadFile : payloadFiles) {
      final Path writeToPath = dataDir.resolve(payloadFile.getFileName()); 
      logger.debug("Writing payload file [{}] to [{}]", payloadFile, writeToPath);
      Files.copy(payloadFile, writeToPath, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
    }   
  }
}
