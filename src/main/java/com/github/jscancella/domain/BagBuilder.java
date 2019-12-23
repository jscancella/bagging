package com.github.jscancella.domain;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jscancella.domain.Manifest.ManifestBuilder;
import com.github.jscancella.domain.Metadata.MetadataBuilder;
import com.github.jscancella.exceptions.InvalidBagStateException;
import com.github.jscancella.hash.BagitChecksumNameMapping;

/**
 * Used to conveniently create a bag programmatically and incrementally
 */
public final class BagBuilder {
  private static final Logger logger = LoggerFactory.getLogger(BagBuilder.class);
  private transient Version version = Version.LATEST_BAGIT_VERSION();
  private transient Charset fileEncoding = StandardCharsets.UTF_8;
  private transient final Set<Path> payloadFiles = new HashSet<>();
  private transient final Set<Path> tagFiles = new HashSet<>();
  private transient final Set<String> bagitAlgorithmNames = new HashSet<>();
  private transient final List<FetchItem> itemsToFetch = new ArrayList<>();
  private transient final MetadataBuilder metadataBuilder = new MetadataBuilder();
  //the current location of the bag on the filesystem
  private transient Path rootDir;
  
  /**
   * default encoding is UTF8
   */
  public BagBuilder() {
    //intentionally left empty
  }
  
  /**
   * Set the bagit specification version
   * 
   * @param major major version
   * @param minor minor version
   * 
   * @return this builder so as to chain commands
   */
  public BagBuilder version(final int major, final int minor) {
    this.version = new Version(major, minor);
    return this;
  }
  
  /**
   * Set the bagit specification version
   * 
   * @param version the version
   * @return this builder so as to chain commands
   */
  public BagBuilder version(final Version version) {
    this.version = new Version(version.major, version.minor);
    return this;
  }
  
  /**
   * Set the tag file encoding. Defaults to UTF8
   * @param fileEncoding the tag file encoding
   * @return this builder so as to chain commands
   */
  public BagBuilder fileEncoding(final Charset fileEncoding) {
    this.fileEncoding = Charset.forName(fileEncoding.name());
    return this;
  }
  
  //TODO change to allow writing to a particular directory
  /**
   * Add a file to the bag payload
   * @param payload a file to add
   * @return this builder so as to chain commands
   */
  public BagBuilder addPayloadFile(final Path payload) {
    this.payloadFiles.add(Paths.get(payload.toAbsolutePath().toString()));
    return this;
  }
  
  /**
   * Add a file to the bag tags
   * @param tag a tag file to add
   * @return this builder so as to chain commands
   */
  public BagBuilder addTagFile(final Path tag) {
    this.tagFiles.add(Paths.get(tag.toAbsolutePath().toString()));
    return this;
  }
  
  /**
   * add a bagit algorithm to use when computing the manifests
   * @param bagitAlgorithmName  the all lowercase name as specified in the bagit specification
   * @return this builder so as to chain commands
   */
  public BagBuilder addAlgorithm(final String bagitAlgorithmName) {
    if(BagitChecksumNameMapping.isSupported(bagitAlgorithmName)) {
      this.bagitAlgorithmNames.add(bagitAlgorithmName);
    }
    else {
      logger.error("[{}] is not supported so it will be ignored. "
          + "Please add an implementation to BagitChecksumNameMapping.java if you wish to use [{}]", bagitAlgorithmName, bagitAlgorithmName);
    }
    return this;
  }
  
  /**
   * Add an item to fetch. These fetch items must be downloaded before the bag is complete
   * @param fetchItem an item to fetch
   * @return this builder so as to chain commands
   */
  public BagBuilder addItemToFetch(final FetchItem fetchItem) {
    this.itemsToFetch.add(fetchItem);
    return this;
  }
  
  /**
   * Add a human understandable key value pair of information
   * @param key metadata key
   * @param value metadata value
   * @return this builder so as to chain commands
   */
  public BagBuilder addMetadata(final String key, final String value) {
    metadataBuilder.add(key, value);
    return this;
  }
  
  /**
   * Set the directory to use when creating a bag
   * @param dir the root dir of a bag
   * @return this builder so as to chain commands
   */
  public BagBuilder rootDir(final Path dir) {
    this.rootDir = Paths.get(dir.toAbsolutePath().toString());
    return this;
  }
  
  /**
   * Write the bag out to a physical location (on disk)
   * @return this builder so as to chain commands
   * @throws IOException if there is a problem reading a file
   */
  public Bag write() throws IOException{
    if(rootDir == null) {
      throw new InvalidBagStateException("Bags must have a root directory");
    }
    
    final Bag bag = new Bag(this.version, this.fileEncoding, createPayloadManifests(), createTagManifests(), this.itemsToFetch, metadataBuilder.build(), this.rootDir);
    bag.write(this.rootDir);
    
    return bag;
  }

  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  private Set<Manifest> createTagManifests() throws IOException{
    final Set<Manifest> manifests = new HashSet<>();
    
    for(final String name : bagitAlgorithmNames) {
      final ManifestBuilder builder = new ManifestBuilder(name);
      for(final Path tagFile : tagFiles) {
        builder.addFile(tagFile, rootDir);
      }
      
      manifests.add(builder.build());
    }
    
    return manifests;
  }

  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  private Set<Manifest> createPayloadManifests() throws IOException{
    final Set<Manifest> manifests = new HashSet<>();
    
    for(final String name : bagitAlgorithmNames) {
      final ManifestBuilder builder = new ManifestBuilder(name);
      for(final Path payloadFile : payloadFiles) {        
        builder.addFile(payloadFile, rootDir.resolve("data"));
      }
      
      manifests.add(builder.build());
    }
    
    return manifests;
  }

}
