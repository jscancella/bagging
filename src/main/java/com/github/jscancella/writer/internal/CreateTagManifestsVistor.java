package com.github.jscancella.writer.internal;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;

import com.github.jscancella.domain.Manifest;
import com.github.jscancella.hash.Hasher;

/**
 * Creates the tag manifests by walking the tag files and calculating their checksums
 * Mainly used in {@link BagCreator}
 */
public final class CreateTagManifestsVistor extends AbstractCreateManifestsVistor{
  
  public CreateTagManifestsVistor(final Map<Manifest, Hasher> manifestToHasherMap, final boolean includeHiddenFiles){
    super(manifestToHasherMap, includeHiddenFiles);
  }
  
  @Override
  public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
    return abstractPreVisitDirectory(dir, "data");
  }
}
