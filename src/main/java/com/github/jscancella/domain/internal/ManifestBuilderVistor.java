package com.github.jscancella.domain.internal;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jscancella.domain.ManifestEntry;
import com.github.jscancella.hash.Hasher;

/**
 * Creates a manifest from the supplied starting point
 */
public final class ManifestBuilderVistor extends SimpleFileVisitor<Path> {
  private static final Logger logger = LoggerFactory.getLogger(ManifestBuilderVistor.class);
  private final List<ManifestEntry> entries;
  private final Path startingPoint;
  private final Hasher hasher;
  
  /**
   * Create a manifest from the starting point
   * 
   * @param entries the {@link ManifestEntry} that corresponds to a particular file
   * @param startingPoint The place to use when creating a relative path
   * @param hasher the hashing implementation
   */
  public ManifestBuilderVistor(final List<ManifestEntry> entries, final Path startingPoint, final Hasher hasher) {
    this.entries = entries;
    this.startingPoint = Paths.get(startingPoint.toAbsolutePath().toString());
    this.hasher = hasher;
  }

  @Override
  public FileVisitResult visitFile(final Path path, final BasicFileAttributes attrs) throws IOException{
    final Path physicalLocation = path.toAbsolutePath();
    final Path relativeLocation = startingPoint.relativize(physicalLocation);
    final String checksum = hasher.hash(physicalLocation);
    final ManifestEntry entry = new ManifestEntry(physicalLocation, relativeLocation, checksum);
    
    logger.debug("Adding new manifest entry [{}] to manifest", entry);
    
    entries.add(entry);
    
    return FileVisitResult.CONTINUE;
  }
}
