
package com.github.jscancella.domain;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A manifest is a list of files and their corresponding checksum with the {@link com.github.jscancella.hash.Hasher} used to generate that checksum
 */
public final class Manifest {
  private final String bagitAlgorithmName;
  private final Map<Path, String> fileToChecksumMap;
  
  public Manifest(final String bagitAlgorithmName) {
    this.bagitAlgorithmName = bagitAlgorithmName;
    this.fileToChecksumMap = new HashMap<>();
  }
  
  public Manifest(final String bagitAlgorithmName, final Map<Path, String> fileToChecksumMap) {
    this.bagitAlgorithmName = bagitAlgorithmName;
    this.fileToChecksumMap = new HashMap<>();
    this.fileToChecksumMap.putAll(fileToChecksumMap);
  }

  public Map<Path, String> getFileToChecksumMap() {
    return fileToChecksumMap;
  }

  public String getBagitAlgorithmName(){
    return bagitAlgorithmName;
  }
  
  @Override
  public String toString() {
    return "Manifest [algorithm=" + getBagitAlgorithmName() + ", fileToChecksumMap=" + fileToChecksumMap + "]";
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(bagitAlgorithmName) + fileToChecksumMap.hashCode();
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj){
      return true;
    }
    if (obj == null){
      return false;
    }
    if (!(obj instanceof Manifest)){
      return false;
    }
    
    final Manifest other = (Manifest) obj;
    
    return Objects.equals(bagitAlgorithmName, other.getBagitAlgorithmName()) && fileToChecksumMap.equals(other.getFileToChecksumMap()); 
  }
}
