package com.github.jscancella.domain;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * The main representation of the bagit spec.
 */
public final class Bag {
	
  private final Version version;
  private final Charset fileEncoding;
  private final Set<Manifest> payLoadManifests;
  private final Set<Manifest> tagManifests;
  private final List<FetchItem> itemsToFetch;
  private final Metadata metadata;
  private final Path rootDirectory;

  /**
   * @return the directory that contains the payload files
   */
  public Path getDataDir(){
    return rootDirectory.resolve("data");
  }
  
  /**
   * @return the directory that contains the tag files
   */
  public Path getTagFileDir(){
    return rootDirectory; //whenever .bagit comes around this will be very helpful
  }
  
  public Version getVersion(){
    return version;
  }

  public Set<Manifest> getPayLoadManifests() {
    return Collections.unmodifiableSet(payLoadManifests);
  }

  public Set<Manifest> getTagManifests() {
    return Collections.unmodifiableSet(tagManifests);
  }

  public List<FetchItem> getItemsToFetch() {
    return Collections.unmodifiableList(itemsToFetch);
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
    return rootDirectory;
  }

  public static class Builder{
	  private Version version = new Version(-1, -1);
	  
	  //from the bagit.txt or UTF-8 for new bags
	  private Charset fileEncoding = StandardCharsets.UTF_8;
	  
	  //equivalent to the manifest-<ALG>.txt files
	  private Set<Manifest> payLoadManifests = Collections.unmodifiableSet(new HashSet<>());
	  
	  //equivalent to the tagmanifest-<ALG>.txt  files
	  private Set<Manifest> tagManifests = Collections.unmodifiableSet(new HashSet<>());
	  
	  //equivalent to the fetch.txt
	  private List<FetchItem> itemsToFetch = Collections.unmodifiableList(new ArrayList<>());
	  
	  //equivalent to the bag-info.txt 
	  private Metadata metadata = new Metadata();
	  
	  //the current location of the bag on the filesystem
	  private Path rootDirectory;
	  
	  /**
	   * Builder with the specified bag version.
	   * 
	   * @param version the version of the bag
	   */
	  public  Builder version(final Version version) {
		  this.version = version;
		  return this;
	  }
	  
	  /**
	   * Builder with the specified file encoding.
	   * 
	   * @param fileEncoding the fileEncoding of the bag
	   */
	  public Builder fileEncoding(final Charset fileEncoding) {
		  this.fileEncoding = fileEncoding;
		  return this;
	  }
	  
	  /**
	   * Bag with the specified payload manifests.
	   * 
	   * @param payLoadManifests the pay load manifests of the bag
	   */
	  public Builder payLoadManifests(final Set<Manifest> payLoadManifests) {
		  this.payLoadManifests = payLoadManifests;
		  return this;
	  }
	  
	  /**
	   * Bag with the specified tag manifests.
	   * 
	   * @param tagManifests the tag manifests of the bag
	   */
	  public Builder tagManifests( final Set<Manifest> tagManifests) {
		  this.tagManifests = tagManifests;
		  return this;
	  }
	  
	  /**
	   * Bag with the specified items to fetch.
	   * 
	   * @param itemsToFetch the items to fetch of the bag
	   */
	  public Builder itemsToFetch(final List<FetchItem> itemsToFetch) {
		  this.itemsToFetch = itemsToFetch;
		  return this;
	  }
	  
	  /**
	   * Bag with the specified metadata.
	   * 
	   * @param metadata the metadata of the bag
	   */
	  public Builder metaData(final Metadata metadata) {
		  this.metadata = metadata;
		  return this;
	  }
	  
	  /**
	   * Bag with the specified root directory.
	   * 
	   * @param rootDirectory the root directory of the bag
	   */
	  public Builder rootDirectory(final Path rootDirectory) {
		  this.rootDirectory = rootDirectory;
		  return this;
	  }
	  
	  /**
	   * Bag with the specified root directory.
	   * 
	   * @param rootDirectory the root directory of the bag
	   */
	  public Builder bag(final Bag bag) {
		  this.version = bag.getVersion();
		  this.fileEncoding = bag.getFileEncoding();
		  this.payLoadManifests = bag.getPayLoadManifests();
		  this.tagManifests = bag.getTagManifests();
		  this.itemsToFetch = bag.getItemsToFetch();
		  this. metadata = bag.getMetadata();
		  this.rootDirectory = bag.getRootDir();
		  return this;
	  }
	  /**
	   * Builds a bag.
	   */
	  public Bag build() {
		  return new Bag(this);
	  }
  }
  
  private Bag(final Builder builder) {
	  version = builder.version;
	  fileEncoding = builder.fileEncoding;
	  payLoadManifests = builder.payLoadManifests;
	  tagManifests = builder.tagManifests;
	  itemsToFetch = builder.itemsToFetch;
	  metadata = builder.metadata;
	  rootDirectory = builder.rootDirectory;
  }
}
