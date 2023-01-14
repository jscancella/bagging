package com.github.jscancella.domain;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import com.github.jscancella.domain.internal.EclipseGenerated;

/**
 * A Manifest Entry is a domain object meant to keep track of various parts needed so it can be listed in a manifest properly 
 */
public final class ManifestEntry {
  private final Path physicalLocation;
  private final Path relativeLocation; //relative to the data dir
  private final String checksum; //checksum of the physical location
  
  /**
   * @param physicalLocation the location on disk
   * @param relativeLocation the location in the bag relative to the base directory
   * @param checksum the generated checksum
   */
  public ManifestEntry(final Path physicalLocation, final Path relativeLocation, final String checksum) {
    this.physicalLocation = Paths.get(physicalLocation.toAbsolutePath().toString());
    this.relativeLocation = Paths.get(relativeLocation.toString());
    this.checksum = checksum;
  }

  /**
   * @return the location on disk
   */
  public Path getPhysicalLocation(){
    return physicalLocation;
  }

  /**
   * @return the location in a manifest
   */
  public Path getRelativeLocation(){
    return relativeLocation;
  }

  /**
   * @return the generated checksum over the bytes in the file
   */
  public String getChecksum(){
    return checksum;
  }

  @EclipseGenerated
  @Override
  public String toString(){
    return "ManifestEntry [physicalLocation=" + physicalLocation + ", relativeLocation=" + relativeLocation
        + ", checksum=" + checksum + "]";
  }

  @EclipseGenerated
  @Override
  public int hashCode(){
    return Objects.hash(this.physicalLocation, this.relativeLocation, this.checksum);
  }

  @EclipseGenerated
  @Override
  public boolean equals(final Object obj){
    boolean isEqual = false;
    if (obj instanceof ManifestEntry){
      final ManifestEntry other = (ManifestEntry) obj;
      
      isEqual = Objects.equals(physicalLocation, other.getPhysicalLocation()) 
          && Objects.equals(relativeLocation, other.getRelativeLocation()) 
          && Objects.equals(checksum, other.getChecksum()); 
    }
    return isEqual;
    
  }
}
