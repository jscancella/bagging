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
  private final Hasher hasher;
  
  /**
   * Create a manifest from the starting point
   * 
   * @param startingPoint The place to use when creating a relative path
   * @param hasher the hashing implementation
   */
  public ManifestBuilderVistor(final Path startingPoint, final Hasher hasher) {
    super();
    this.entries =  new ArrayList<>();
    this.startingPoint = Paths.get(startingPoint.toAbsolutePath().toString());
    this.hasher = hasher;
  }

  @Override
  public FileVisitResult visitFile(final Path path, final BasicFileAttributes attrs) throws IOException{
    final Path physicalLocation = path.toAbsolutePath();
    Path relativeStartingLocation = startingPoint.getParent();
    if(relativeStartingLocation == null) {
      relativeStartingLocation = startingPoint;
    }
    final Path relativeLocation = relativeStartingLocation.relativize(physicalLocation);
    final String checksum = hasher.hash(physicalLocation);
    final ManifestEntry entry = new ManifestEntry(physicalLocation, relativeLocation, checksum);
    
    logger.debug(messages.getString("adding_manifest_entry"), entry);
    
    entries.add(entry);
    
    return FileVisitResult.CONTINUE;
  }

  public List<ManifestEntry> getEntries(){
    return entries;
  }

  public Path getStartingPoint(){
    return startingPoint;
  }

  public Hasher getHasher(){
    return hasher;
  }
}
