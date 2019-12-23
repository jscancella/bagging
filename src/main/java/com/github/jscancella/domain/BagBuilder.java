package com.github.jscancella.domain;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
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

public final class BagBuilder {

  private static final Logger logger = LoggerFactory.getLogger(BagBuilder.class);
  private Version version = Version.LATEST_BAGIT_VERSION();
  private Charset fileEncoding = StandardCharsets.UTF_8;
  private Set<Path> payloadFiles = new HashSet<>();
  private Set<Path> tagFiles = new HashSet<>();
  private Set<String> bagitAlgorithmNames = new HashSet<>();
  private List<FetchItem> itemsToFetch = new ArrayList<>();
  private final MetadataBuilder metadataBuilder = new MetadataBuilder();
  //the current location of the bag on the filesystem
  private Path rootDir = null;
  
  public BagBuilder() {
    //intentionally left empty
  }
  
  public BagBuilder version(final int major, final int minor) {
    this.version = new Version(major, minor);
    return this;
  }
  
  public BagBuilder version(final Version version) {
    this.version = new Version(version.major, version.minor);
    return this;
  }
  
  public BagBuilder fileEncoding(final Charset fileEncoding) {
    this.fileEncoding = Charset.forName(fileEncoding.name());
    return this;
  }
  
  //TODO change to allow writing to a particular directory
  public BagBuilder addPayloadFile(final Path payload) {
    this.payloadFiles.add(Paths.get(payload.toAbsolutePath().toString()));
    return this;
  }
  
  public BagBuilder addTagFile(final Path tag) {
    this.tagFiles.add(Paths.get(tag.toAbsolutePath().toString()));
    return this;
  }
  
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
  
  public BagBuilder addItemToFetch(final FetchItem fetchItem) {
    this.itemsToFetch.add(fetchItem);
    return this;
  }
  
  public BagBuilder addMetadata(final String key, final String value) {
    metadataBuilder.add(key, value);
    return this;
  }
  
  public BagBuilder rootDir(final Path dir) {
    this.rootDir = Paths.get(dir.toAbsolutePath().toString());
    return this;
  }
  
  public Bag write() throws Exception{
    if(rootDir == null) {
      throw new InvalidBagStateException("Bags must have a root directory");
    }
    
    final Bag bag = new Bag(this.version, this.fileEncoding, createPayloadManifests(), createTagManifests(), this.itemsToFetch, metadataBuilder.build(), this.rootDir);
    bag.write(this.rootDir);
    
    return bag;
  }

  private Set<Manifest> createTagManifests() throws IOException, NoSuchAlgorithmException{
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

  private Set<Manifest> createPayloadManifests() throws NoSuchAlgorithmException, IOException{
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
