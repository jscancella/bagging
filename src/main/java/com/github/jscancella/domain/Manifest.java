
package com.github.jscancella.domain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

  /**
   * @return the entries that this manifest specifies
   */
  public List<ManifestEntry> getEntries() {
    return entries;
  }

  /**
   * @return the bagit specification all lowercase name
   */
  public String getBagitAlgorithmName(){
    return bagitAlgorithmName;
  }
  
  @Override
  public String toString() {
    return "Manifest [algorithm=" + getBagitAlgorithmName() + ", Entries=" + entries + "]";
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(bagitAlgorithmName, entries);
  }

  @Override
  public boolean equals(final Object obj) {
    boolean isEqual = false;
    if (obj instanceof Manifest){
      final Manifest other = (Manifest) obj;
      isEqual = Objects.equals(bagitAlgorithmName, other.getBagitAlgorithmName()) && entries.equals(other.getEntries()); 
    }
    
    return isEqual;
  }
  
  /**
   * Programmatically build a manifest
   */
  @SuppressWarnings({"PMD.BeanMembersShouldSerialize"})
  public static final class ManifestBuilder {
    private String algorithmName;
    private Hasher hasher;
    private final List<ManifestEntry> entries;
    
    /**
     * @param bagitAlgorithmName the bagit algorithm name
     */
    public ManifestBuilder(final String bagitAlgorithmName){
      this.bagitAlgorithmName(bagitAlgorithmName);
      this.entries = new ArrayList<>();
    }
    
    /**
     * because a manifest is immutable, if you want to clone and edit you must return a manifest builder
     * @param manifestToClone the manifest you wish to clone and optionally change
     */
    public ManifestBuilder(final Manifest manifestToClone) {
    	this.bagitAlgorithmName(manifestToClone.getBagitAlgorithmName());
    	this.entries = new ArrayList<>(manifestToClone.getEntries());
    }
    
    /**
     * Set the bagit algorithm name (example "md5"). Any previous set name will be overwritten.
     * @param name the name of the algorithm
     * @return this builder for chaining
     */
    public ManifestBuilder bagitAlgorithmName(final String name){
      this.hasher = BagitChecksumNameMapping.get(name);
      this.algorithmName = name;
      
      return this;
    }
    
    /**
     * a convenience method for adding an entry from another manifest
     * @param entry the entry
     * @return this builder so as to chain commands
     */
    public ManifestBuilder addEntry(final ManifestEntry entry) {
      this.entries.add(entry);
      return this;
    }
    
    /**
     * Add a file or directory on disk to this manifest
     * 
     * @param file the file or directory to add to the manifest
     * @param relative the relative path to put this in the bag
     * @throws RuntimeException if the bagitAlgorithmName is already set before calling this
     * @return the builder for chaining
     * @throws IOException if the file or directory can't be read
     */
    public ManifestBuilder addFile(final Path file, final Path relative) throws IOException {
      if(Files.isDirectory(file)) {
        final ManifestBuilderVistor vistor = new ManifestBuilderVistor(file, relative, hasher);
        Files.walkFileTree(file, vistor);
        entries.addAll(vistor.getEntries());
      }
      else {
        final Path physicalLocation = file.toAbsolutePath();
        final Path relativeLocation = relative.resolve(file.getFileName());
        final String checksum = hasher.hash(physicalLocation);
        final ManifestEntry entry = new ManifestEntry(physicalLocation, relativeLocation, checksum);
        entries.add(entry);
      }
      
      return this;
    }
    
    /**
     * @return the manifest
     */
    public Manifest build() {
      return new Manifest(algorithmName, entries);
    }

    /**
     * @return the checksum algorithm name
     */
    public String getAlgorithmName(){
      return algorithmName;
    }
  }
}
