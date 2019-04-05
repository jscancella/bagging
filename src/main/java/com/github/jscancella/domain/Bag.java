package com.github.jscancella.domain;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jscancella.domain.Manifest.ManifestBuilder;
import com.github.jscancella.domain.Metadata.MetadataBuilder;
import com.github.jscancella.exceptions.InvalidBagStateException;
import com.github.jscancella.hash.BagitChecksumNameMapping;
import com.github.jscancella.writer.internal.BagitFileWriter;
import com.github.jscancella.writer.internal.FetchWriter;
import com.github.jscancella.writer.internal.MetadataWriter;

/**
 * The main representation of the bagit spec. This is an immutable object, if you need to modify it look at {@link Bag.Builder}
 */
public final class Bag {
  //The original version of the bag
  private final Version version;
  
  //from the bagit.txt or UTF-8 for new bags
  private final Charset fileEncoding;// = StandardCharsets.UTF_8;
  
  //equivalent to the manifest-<ALG>.txt files
  private final Set<Manifest> payLoadManifests;
  
  //equivalent to the tagmanifest-<ALG>.txt  files
  private final Set<Manifest> tagManifests;
  
  //equivalent to the fetch.txt
  private final List<FetchItem> itemsToFetch;
  
  //equivalent to the bag-info.txt 
  private final Metadata metadata;
  
  //the current location of the bag on the filesystem
  private final Path rootDir;
  
  private Bag(final Version version, final Charset fileEncoding, final Set<Manifest> payloadManifests, 
      final Set<Manifest> tagManifests, final List<FetchItem> itemsToFetch, final Metadata metadata, final Path rootDir){
    this.version = version;
    this.fileEncoding = fileEncoding;
    this.payLoadManifests = Collections.unmodifiableSet(payloadManifests);
    this.tagManifests = Collections.unmodifiableSet(tagManifests);
    this.itemsToFetch = Collections.unmodifiableList(itemsToFetch);
    this.metadata = metadata;
    this.rootDir = rootDir;
  }
  
  
  /**
   * @return the directory that contains the payload files
   */
  public Path getDataDir(){
    return rootDir.resolve("data");
  }
  
  /**
   * @return the directory that contains the tag files
   */
  public Path getTagFileDir(){
    return rootDir; //whenever .bagit comes around this will be very helpful
  }
  
  public Version getVersion(){
    return version;
  }

  public Set<Manifest> getPayLoadManifests() {
    return payLoadManifests;
  }

  public Set<Manifest> getTagManifests() {
    return tagManifests;
  }

  public List<FetchItem> getItemsToFetch() {
    return itemsToFetch;
  }

  public Metadata getMetadata() {
    return metadata;
  }

  public Charset getFileEncoding() {
    return fileEncoding;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder(95);
    sb.append("Bag [version=").append(version)
    .append(", fileEncoding=").append(fileEncoding)
    .append(", payLoadManifests=[");
    for(final Manifest payloadManifest : payLoadManifests){
      sb.append(payloadManifest).append(' ');
    }
    sb.append("], tagManifests=[");
    for(final Manifest tagManifest : tagManifests){
      sb.append(tagManifest).append(' ');
    }
    sb.append("], itemsToFetch=").append(itemsToFetch)
    .append(", metadata=").append(metadata).append(']');
    
    return sb.toString();
  }

  @Override
  public int hashCode() {
    return Objects.hash(version) + Objects.hash(fileEncoding) + Objects.hash(payLoadManifests) + 
        Objects.hash(tagManifests) + Objects.hash(itemsToFetch) + Objects.hash(metadata);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj){
      return true;
    }
    if (obj == null){
      return false;
    }
    if (!(obj instanceof Bag)){
      return false;
    }
    
    final Bag other = (Bag) obj;
    return Objects.equals(this.version, other.getVersion()) && 
        Objects.equals(this.fileEncoding, other.getFileEncoding()) &&
        Objects.equals(this.payLoadManifests, other.getPayLoadManifests()) && 
        Objects.equals(this.tagManifests, other.getTagManifests()) &&
        Objects.equals(this.itemsToFetch, other.getItemsToFetch()) &&
        Objects.equals(this.metadata, other.getMetadata());
  }

  /**
   * @return the root directory of a bag, usually the folder name
   */
  public Path getRootDir() {
    return rootDir;
  }
  
  public static final class BagBuilder {
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
    
    public Bag write() throws InvalidBagStateException, NoSuchAlgorithmException, IOException{
      if(rootDir == null) {
        throw new InvalidBagStateException("Bags must have a root directory");
      }

      //TODO
      //since some of the tag files don't exist yet, bagWriter has to update this bagBuilder.
      //this ensures that when we call the constructor below it has the correct set of tag files already in the builder
      logger.info("Writing bag to [{}]", rootDir);
      Files.createDirectories(rootDir);
      
      final Path bagitPath = BagitFileWriter.writeBagitFile(version, fileEncoding, rootDir);
      this.addTagFile(bagitPath);
      
      final Metadata metadata = metadataBuilder.build();
      if(!metadata.isEmpty()){
        final Path metadataPath = MetadataWriter.writeBagMetadata(metadata, version, rootDir, fileEncoding);
        this.addTagFile(metadataPath);
      }
      if(!itemsToFetch.isEmpty()){
        final Path fetchFile = FetchWriter.writeFetchFile(itemsToFetch, rootDir, fileEncoding);
        this.addTagFile(fetchFile);
      }
      
      //TODO write payload files
        //TODO change from list<path> to list<manifest> entry so that we know where in the bag to put the file?
      //TODO write payload manifests
      //TODO write user defined tag files
      //TODO write tag manifests
      
      return new Bag(this.version, this.fileEncoding, createPayloadManifests(), createTagManifests(), this.itemsToFetch, metadataBuilder.build(), this.rootDir);
    }

    private Set<Manifest> createTagManifests() throws IOException, NoSuchAlgorithmException{
      final Set<Manifest> manifests = new HashSet<>();
      
      for(final String name : bagitAlgorithmNames) {
        final ManifestBuilder builder = new ManifestBuilder(name);
        for(final Path tagFile : tagFiles) {
          builder.addFile(tagFile);
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
          builder.addFile(payloadFile);
        }
        
        manifests.add(builder.build());
      }
      
      return manifests;
    }
  }
}
