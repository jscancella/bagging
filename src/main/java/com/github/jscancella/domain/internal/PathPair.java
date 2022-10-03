package com.github.jscancella.domain.internal;

import java.nio.file.Path;

/**
 * a convenience class for representing a file path and what its relative path should be
 */
public final class PathPair {

  private final Path payloadFile;
  private final String relativeLocation;
  
  /**
   * a convenience class for representing a file path and what its relative path should be
   * 
   * @param payloadFile the actual full path
   * @param relativeLocation the relative path representation
   */
  public PathPair(final Path payloadFile, final String relativeLocation) {
    this.payloadFile = payloadFile;
    this.relativeLocation = relativeLocation;
  }

  /**
   * @return the full path
   */
  public Path getPayloadFile(){
    return payloadFile;
  }

  /**
   * @return the relative path
   */
  public String getRelativeLocation(){
    return relativeLocation;
  }
}
