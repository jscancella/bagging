package com.github.jscancella.conformance.internal;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Checks for zero byte files and non zero byte files and records them for use in data directory must be empty bagit profile
 */
public class DataDirIsEmptyChecker extends SimpleFileVisitor<Path> {
  private final Set<Path> nonZeroByteFiles;
  private final Set<Path> zeroByteFiles;
  
  /**
   * Checks for zero byte files and non zero byte files and records them for use in data directory must be empty bagit profile
   */
  public DataDirIsEmptyChecker() {
    super();
    this.nonZeroByteFiles = new HashSet<>();
    this.zeroByteFiles = new HashSet<>();
  }

  @Override
  public FileVisitResult visitFile(final Path path, final BasicFileAttributes attrs) throws IOException{
    if(Files.size(path)>0) {
      this.nonZeroByteFiles.add(path);
    }
    else {
      this.zeroByteFiles.add(path);
    }
    
    return FileVisitResult.CONTINUE;
  }
  
  /**
   * @return the nonZeroByteFiles
   */
  public Set<Path> getNonZeroByteFiles() {
    return Collections.unmodifiableSet(nonZeroByteFiles);
  }

  /**
   * @return the zeroByteFiles
   */
  public Set<Path> getZeroByteFiles() {
    return Collections.unmodifiableSet(zeroByteFiles);
  }
}
