
package com.github.jscancella.domain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.github.jscancella.domain.internal.ManifestBuilderVistor;
import com.github.jscancella.hash.BagitChecksumNameMapping;
import com.github.jscancella.hash.Hasher;

/**
 * A manifest is a list of files,their corresponding checksum generated using {@link com.github.jscancella.hash.Hasher}, and their relative path in the bag
 */
public final class Manifest {
  private final String bagitAlgorithmName;
  private final List<ManifestEntry> entries;
  
  private Manifest(final String bagitAlgorithmName, final List<ManifestEntry> entries) {
    this.bagitAlgorithmName = bagitAlgorithmName;
    this.entries = Collections.unmodifiableList(entries);
  }

  public List<ManifestEntry> getEntries() {
    return entries;
  }

  public String getBagitAlgorithmName(){
    return bagitAlgorithmName;
  }
  
  @Override
  public String toString() {
    return "Manifest [algorithm=" + getBagitAlgorithmName() + ", Entries=" + entries + "]";
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(bagitAlgorithmName) + entries.hashCode();
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
    
    return Objects.equals(bagitAlgorithmName, other.getBagitAlgorithmName()) && entries.equals(other.getEntries()); 
  }
  
  public static final class ManifestBuilder {
    private String bagitAlgorithmName = null;
    private Hasher hasher;
    private List<ManifestEntry> entries = new ArrayList<>();
    
    public ManifestBuilder(String bagitAlgorithmName) throws NoSuchAlgorithmException {
      this.bagitAlgorithmName(bagitAlgorithmName);
    }
    
    /**
     * Set the bagit algorithm name (example "md5"). Any previous set name will be overwritten.
     * @param name
     * @return this builder for chaining
     * @throws NoSuchAlgorithmException if {@link BagitChecksumNameMapping} doesn't have an implmentation of that algorithm
     */
    public ManifestBuilder bagitAlgorithmName(final String name) throws NoSuchAlgorithmException {
      hasher = BagitChecksumNameMapping.get(bagitAlgorithmName);
      this.bagitAlgorithmName = name;
      
      return this;
    }
    
    public ManifestBuilder addEntry(final ManifestEntry entry) {
      this.entries.add(entry);
      return this;
    }
    
    /**
     * Add a file or directory on disk to this manifest
     * 
     * @param file the file or directory to add to the manifest
     * @throws RuntimeException if the bagitAlgorithmName is already set before calling this
     * @return the builder for chaining
     * @throws IOException if the file or directory can't be read
     */
    public ManifestBuilder addFile(final Path file) throws IOException {
      if(Files.isDirectory(file)) {
        Files.walkFileTree(file, new ManifestBuilderVistor(entries, file, hasher));
      }
      else {
        final Path physicalLocation = file.toAbsolutePath();
        final Path relativeLocation = file.getFileName();
        final String checksum = hasher.hash(physicalLocation);
        final ManifestEntry entry = new ManifestEntry(physicalLocation, relativeLocation, checksum);
        entries.add(entry);
      }
      
      return this;
    }
    
    //TODO add in ability to add file to a specific relative path
    
    public Manifest build() {
      return new Manifest(bagitAlgorithmName, entries);
    }
  }
}
