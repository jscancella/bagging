package com.github.jscancella.domain.internal;

import java.nio.file.Path;

public final class PathPair {

  private final Path payloadFile;
  private final String relativeLocation;
  
  public PathPair(final Path payloadFile, final String relativeLocation) {
    this.payloadFile = payloadFile;
    this.relativeLocation = relativeLocation;
  }

  public Path getPayloadFile(){
    return payloadFile;
  }

  public String getRelativeLocation(){
    return relativeLocation;
  }
}
