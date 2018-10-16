package com.github.jscancella.internal;

import java.io.IOException;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.Path;

public class ManifestFilter implements Filter<Path> {
  @Override
  public boolean accept(final Path file) throws IOException {
    if(file == null || file.getFileName() == null){ return false;}
    final String filename = PathUtils.getFilename(file);
    return filename.startsWith("tagmanifest-") || filename.startsWith("manifest-");
  }
}
