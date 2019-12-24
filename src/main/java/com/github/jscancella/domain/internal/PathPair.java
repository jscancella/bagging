package com.github.jscancella.domain.internal;

import java.nio.file.Path;

public final class PathPair {

  public final Path payloadFile;
  public final String relativeLocation;
  
  public PathPair(final Path payloadFile, final String relativeLocation) {
    this.payloadFile = payloadFile;
    this.relativeLocation = relativeLocation;
  }
}
