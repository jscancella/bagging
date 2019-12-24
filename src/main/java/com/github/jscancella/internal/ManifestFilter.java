package com.github.jscancella.internal;

import java.io.IOException;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.Path;

/**
 * Responsible for filtering out files that aren't manifests 
 */
public class ManifestFilter implements Filter<Path> {
  @Override
  public boolean accept(final Path file) throws IOException {
    boolean isAccepted = false;
    
    if(file != null && file.getFileName() != null){
      final String filename = PathUtils.getFilename(file);
      isAccepted = filename.startsWith("tagmanifest-") || filename.startsWith("manifest-");
    }
    
    return isAccepted;
  }
}
