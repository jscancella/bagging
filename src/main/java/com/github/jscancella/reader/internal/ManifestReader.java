package com.github.jscancella.reader.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jscancella.domain.Manifest;
import com.github.jscancella.domain.Manifest.ManifestBuilder;
import com.github.jscancella.domain.ManifestEntry;
import com.github.jscancella.exceptions.InvalidBagitFileFormatException;
import com.github.jscancella.exceptions.MaliciousPathException;
import com.github.jscancella.internal.PathUtils;

/**
 * This class is responsible for reading and parsing manifest files on the filesystem
 */
public enum ManifestReader {;//using enum to enforce singleton
  private static final Logger logger = LoggerFactory.getLogger(ManifestReader.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  
  /**
   * Reads a manifest file and converts it to a {@link Manifest} object.
   * 
   * @param manifestFile the path to the manifest file to read
   * @param bagRootDir the root directory of the bag
   * @param charset what encoding to use when reading the manifest file
   * 
   * @return a manifest
   * 
   * @throws IOException if there is a problem reading a file
   * @throws MaliciousPathException if the manifest has a path that is outside the bag
   * @throws InvalidBagitFileFormatException if the manifest is not formatted correctly
   */
  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  public static Manifest readManifest(final Path manifestFile, final Path bagRootDir, final Charset charset) 
          throws IOException, MaliciousPathException, InvalidBagitFileFormatException{
    logger.debug(messages.getString("reading_manifest"), manifestFile);
    final String algorithm = PathUtils.getFilename(manifestFile).split("[-\\.]")[1];
    
    final ManifestBuilder manifestBuilder = new ManifestBuilder(algorithm);
    
    try(final BufferedReader br = Files.newBufferedReader(manifestFile, charset)){
      String line = br.readLine();
      while(line != null){
        final String[] parts = line.split("\\s+", 2);
        final Path file = TagFileReader.createFileFromManifest(bagRootDir, parts[1]);
        final Path relative = bagRootDir.relativize(file);
        final ManifestEntry entry = new ManifestEntry(file, relative, parts[0]);
        manifestBuilder.addEntry(entry);
        line = br.readLine();
      }
    }
    
    return manifestBuilder.build();
  }
}
