package com.github.jscancella.reader.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jscancella.domain.Bag;
import com.github.jscancella.domain.Manifest;
import com.github.jscancella.exceptions.InvalidBagitFileFormatException;
import com.github.jscancella.exceptions.MaliciousPathException;
import com.github.jscancella.internal.ManifestFilter;
import com.github.jscancella.internal.PathUtils;

/**
 * This class is responsible for reading and parsing manifest files on the filesystem
 */
public enum ManifestReader {;//using enum to enforce singleton
  private static final Logger logger = LoggerFactory.getLogger(ManifestReader.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  
  /**
   * Finds and reads all manifest files in the rootDir and adds them to the given bag.
   * 
   * @param rootDir the directory that contains the tag files of a bag
   * @param bagBuilder the builder to add the manifests to
   * 
   * @throws IOException if there is a problem reading a file
   * @throws MaliciousPathException if a path in the manifest points outside the bag
   * @throws InvalidBagitFileFormatException if one of the bagit files is not formatted correctly
   */
  public static void readAllManifests(final Path rootDir, final Bag.Builder bagBuilder) throws IOException, MaliciousPathException, InvalidBagitFileFormatException{
    logger.info(messages.getString("attempting_read_manifests"));
    
    
    try(final DirectoryStream<Path> manifests = Files.newDirectoryStream(rootDir, new ManifestFilter())){
      for (final Path path : manifests){
        final String filename = PathUtils.getFilename(path);
        
        if(filename.startsWith("tagmanifest-")){
          logger.debug(messages.getString("found_tagmanifest"), path);
          
          Manifest manifest = readManifest(path, bagBuilder.build().getRootDir(), bagBuilder.build().getFileEncoding());
          Set<Manifest> currentManifests = new HashSet<Manifest>(bagBuilder.build().getTagManifests());
          currentManifests.add(manifest);
          bagBuilder.tagManifests(currentManifests);
          
        }
        else if(filename.startsWith("manifest-")){
          logger.debug(messages.getString("found_payload_manifest"), path);
          
          Manifest manifest = readManifest(path, bagBuilder.build().getRootDir(), bagBuilder.build().getFileEncoding());
          Set<Manifest> currentManifests = new HashSet<Manifest>(bagBuilder.build().getPayLoadManifests());
          currentManifests.add(manifest);
          bagBuilder.tagManifests(currentManifests);
        }
      }
    }
  }
  
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
  public static Manifest readManifest(final Path manifestFile, final Path bagRootDir, final Charset charset) 
          throws IOException, MaliciousPathException, InvalidBagitFileFormatException{
    logger.debug(messages.getString("reading_manifest"), manifestFile);
    final String algorithm = PathUtils.getFilename(manifestFile).split("[-\\.]")[1];
    
    final Map<Path, String> fileToChecksumMap = readChecksumFileMap(manifestFile, bagRootDir, charset);
    
    return new Manifest(algorithm, fileToChecksumMap);
  }
  
  /*
   * read the manifest file into a map of files and checksums
   */
  static Map<Path, String> readChecksumFileMap(final Path manifestFile, final Path bagRootDir, final Charset charset) throws IOException, MaliciousPathException, InvalidBagitFileFormatException{
    final HashMap<Path, String> map = new HashMap<>();
    try(final BufferedReader br = Files.newBufferedReader(manifestFile, charset)){
      String line = br.readLine();
      while(line != null){
        final String[] parts = line.split("\\s+", 2);
        final Path file = TagFileReader.createFileFromManifest(bagRootDir, parts[1]);
        logger.debug("Read checksum [{}] and file [{}] from manifest [{}]", parts[0], file, manifestFile);
        map.put(file, parts[0]);
        line = br.readLine();
      }
    }
    
    return map;
  }
}
