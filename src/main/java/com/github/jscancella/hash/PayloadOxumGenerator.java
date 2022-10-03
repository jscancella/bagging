package com.github.jscancella.hash;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.github.jscancella.hash.internal.FileCountAndTotalSizeVistor;

/**
 * a singleton used to generate the payload oxum for a bag
 */
public enum PayloadOxumGenerator {;//using enum to enforce singleton

  /**
   * Calculate the total file and byte count of the files in the payload directory
   * 
   * @param dataDir the directory to calculate the payload-oxum
   * 
   * @return the string representation of the payload-oxum value
   * 
   * @throws IOException if there is an error reading any of the files
   */
  public static String generatePayloadOxum(final Path dataDir) throws IOException{
    final FileCountAndTotalSizeVistor visitor = new FileCountAndTotalSizeVistor();
    
    Files.walkFileTree(dataDir, visitor);
    
    return visitor.getTotalSize() + "." + visitor.getCount();
  }
}
