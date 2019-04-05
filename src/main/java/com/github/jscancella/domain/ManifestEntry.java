package com.github.jscancella.domain;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * A Manifest Entry is a domain object meant to keep track of various parts needed so it can be listed in a manifest properly 
 */
public final class ManifestEntry {
  private final Path physicalLocation;
  private final Path relativeLocation; //relative to the data dir
  private final String checksum; //checksum of the physical location
  
  public ManifestEntry(final Path physicalLocation, final Path relativeLocation, final String checksum) {
    this.physicalLocation = Paths.get(physicalLocation.toAbsolutePath().toString());
    this.relativeLocation = Paths.get(relativeLocation.toString());
    this.checksum = checksum;
  }

  public Path getPhysicalLocation(){
    return physicalLocation;
  }

  public Path getRelativeLocation(){
    return relativeLocation;
  }

  public String getChecksum(){
    return checksum;
  }

  @Override
  public String toString(){
    return "ManifestEntry [physicalLocation=" + physicalLocation + ", relativeLocation=" + relativeLocation
        + ", checksum=" + checksum + "]";
  }

  @Override
  public int hashCode(){
    return Objects.hash(this.physicalLocation) + Objects.hash(this.relativeLocation) + Objects.hash(this.checksum);
  }

  @Override
  public boolean equals(Object obj){
    if (this == obj){
      return true;
    }
    if (obj == null){
      return false;
    }
    if (!(obj instanceof ManifestEntry)){
      return false;
    }
    
    final ManifestEntry other = (ManifestEntry) obj;
    
    return Objects.equals(physicalLocation, other.getPhysicalLocation()) 
        && Objects.equals(relativeLocation, other.getRelativeLocation()) 
        && Objects.equals(checksum, other.getChecksum()); 
  }
}
