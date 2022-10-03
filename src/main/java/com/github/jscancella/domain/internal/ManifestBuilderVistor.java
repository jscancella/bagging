package com.github.jscancella.domain.internal;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jscancella.domain.ManifestEntry;
import com.github.jscancella.hash.Hasher;

/**
 * Creates a manifest from the supplied starting point
 */
public final class ManifestBuilderVistor extends SimpleFileVisitor<Path> {
  private static final Logger logger = LoggerFactory.getLogger(ManifestBuilderVistor.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  
  private final List<ManifestEntry> entries;
  private final Path startingPoint;
  private final Path relative;
  private final Hasher hasher;
  
  /**
   * Create a manifest from the starting point
   * 
   * @param startingPoint used for determining the relative path
   * @param relative the relative place to start in the bag (must include data if a payload path)
   * @param hasher the hashing implementation
   */
  public ManifestBuilderVistor(final Path startingPoint, final Path relative, final Hasher hasher) {
    super();
    this.entries =  new ArrayList<>();
    final Path absoluteStartingPoint = Paths.get(startingPoint.toAbsolutePath().toString());
    if(absoluteStartingPoint.getParent() == null) {
      this.startingPoint = absoluteStartingPoint;
    }
    else {
      this.startingPoint = absoluteStartingPoint.getParent();
    }
    this.relative = relative;
    this.hasher = hasher;
  }

  @Override
  public FileVisitResult visitFile(final Path path, final BasicFileAttributes attrs) throws IOException{
    final Path physicalLocation = path.toAbsolutePath();
    final Path relativeLocation = relative.resolve(startingPoint.relativize(physicalLocation));
    final String checksum = hasher.hash(physicalLocation);
    final ManifestEntry entry = new ManifestEntry(physicalLocation, relativeLocation, checksum);
    
    logger.debug(messages.getString("adding_manifest_entry"), entry);
    
    entries.add(entry);
    
    return FileVisitResult.CONTINUE;
  }

  /**
   * @return the list of entries
   */
  public List<ManifestEntry> getEntries(){
    return new ArrayList<>(entries);
  }

  /**
   * @return the relative point to start at
   */
  public Path getStartingPoint(){
    return startingPoint;
  }

  /**
   * @return the specific hasher for this manifest
   */
  public Hasher getHasher(){
    return hasher;
  }

  /**
   * @return the relative path to the root of the bag
   */
  public Path getRelative() {
    return relative;
  }
}
