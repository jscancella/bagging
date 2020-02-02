package com.github.jscancella.conformance.internal;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jscancella.conformance.BagitWarning;
import com.github.jscancella.hash.internal.FileCountAndTotalSizeVistor;
import com.github.jscancella.internal.ManifestFilter;

/**
 * Part of the BagIt conformance suite. 
 * Responsible for checking if a bag is "large". 
 */
public enum LargeBagChecker { ; //using enum to enforce singleton
  private static final Logger logger = LoggerFactory.getLogger(LargeBagChecker.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  
  private static final long LARGE_FILE_COUNT = 1000; //FIXME What is a good number here?
  private static final int LARGE_MANIFEST_COUNT = 20; //FIXME What is a good number here?
  private static final int ONE_KILOBYTE = 1024;
  private static final int ONE_MEGABYTE = 1024 * ONE_KILOBYTE;
  private static final long ONE_GIGABYTE = 1024 * ONE_MEGABYTE;
  private static final long ONE_TERABYTE = 1024 * ONE_GIGABYTE;
  private static final long LARGE_PAYLOAD_SIZE = ONE_TERABYTE; 
  
  /**
   * Check if a bag is "large", which is: <br>
   * * A large number of files
   * * A large total size of the bag
   * * A large number of manifests
   * 
   * @param bagitDir The directory of the bag
   * @param warnings The list of warnings to add to if bag is large
   * @param warningsToIgnore The list of warnings to ignore
   * 
   * @throws IOException if there is a problem reading the bag
   */
  public static void checkForLargeBag(final Path bagitDir, final Set<BagitWarning> warnings, final Collection<BagitWarning> warningsToIgnore) throws IOException {
    final FileCountAndTotalSizeVistor visitor = new FileCountAndTotalSizeVistor();
    if(!warningsToIgnore.contains(BagitWarning.LARGE_BAG_SIZE) || !warningsToIgnore.contains(BagitWarning.LARGE_NUMBER_OF_FILES)) {
      Files.walkFileTree(bagitDir.resolve("data"), visitor);
    }
    
    if(!warningsToIgnore.contains(BagitWarning.LARGE_NUMBER_OF_FILES)){
      checkNumberOfPayloadFiles(visitor, warnings);
    }
    
    if(!warningsToIgnore.contains(BagitWarning.LARGE_BAG_SIZE)){
      checkBagSize(visitor, warnings);
    }
    
    if(!warningsToIgnore.contains(BagitWarning.LARGE_NUMBER_OF_MANIFESTS)){
      checkNumberOfManifests(bagitDir, warnings);
    }
  }
  
  private static void checkNumberOfPayloadFiles(final FileCountAndTotalSizeVistor visitor, final Set<BagitWarning> warnings) {
    logger.debug(messages.getString("check_more_than_x_files"), LARGE_FILE_COUNT);
    if(visitor.getCount() > LARGE_FILE_COUNT) {
      logger.warn(messages.getString("large_payload_directory"), LARGE_FILE_COUNT, visitor.getCount());
      warnings.add(BagitWarning.LARGE_NUMBER_OF_FILES);
    }
  }
  
  private static void checkBagSize(final FileCountAndTotalSizeVistor visitor, final Set<BagitWarning> warnings) {
    logger.debug(messages.getString("check_size"), LARGE_PAYLOAD_SIZE);
    if(visitor.getTotalSize() >= LARGE_PAYLOAD_SIZE) {
      logger.warn(messages.getString("large_payload_size"), LARGE_PAYLOAD_SIZE, visitor.getTotalSize());
      warnings.add(BagitWarning.LARGE_BAG_SIZE);
    }
  }
  
  private static void checkNumberOfManifests(final Path bagitDir, final Set<BagitWarning> warnings) throws IOException {
    long count = 0;
    try(DirectoryStream<Path> files = Files.newDirectoryStream(bagitDir, new ManifestFilter())){
      for(@SuppressWarnings("unused") final Path file : files){
        count++;
        if(count > LARGE_MANIFEST_COUNT){
          warnings.add(BagitWarning.LARGE_NUMBER_OF_MANIFESTS);
          return;
        }
      }
    }
  }
}
