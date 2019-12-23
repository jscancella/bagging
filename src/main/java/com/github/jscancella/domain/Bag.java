package com.github.jscancella.domain;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.github.jscancella.domain.Manifest.ManifestBuilder;
import com.github.jscancella.domain.Metadata.MetadataBuilder;
import com.github.jscancella.exceptions.CorruptChecksumException;
import com.github.jscancella.exceptions.FileNotInPayloadDirectoryException;
import com.github.jscancella.exceptions.InvalidBagitFileFormatException;
import com.github.jscancella.exceptions.MaliciousPathException;
import com.github.jscancella.exceptions.MissingBagitFileException;
import com.github.jscancella.exceptions.MissingPayloadDirectoryException;
import com.github.jscancella.exceptions.MissingPayloadManifestException;
import com.github.jscancella.hash.BagitChecksumNameMapping;
import com.github.jscancella.hash.Hasher;
import com.github.jscancella.internal.ManifestFilter;
import com.github.jscancella.internal.PathUtils;
import com.github.jscancella.reader.internal.BagitTextFileReader;
import com.github.jscancella.reader.internal.FetchReader;
import com.github.jscancella.reader.internal.ManifestReader;
import com.github.jscancella.reader.internal.MetadataReader;
import com.github.jscancella.verify.internal.BagitTextFileVerifier;
import com.github.jscancella.verify.internal.MandatoryVerifier;
import com.github.jscancella.verify.internal.ManifestVerifier;
import com.github.jscancella.writer.internal.BagitFileWriter;
import com.github.jscancella.writer.internal.FetchWriter;
import com.github.jscancella.writer.internal.ManifestWriter;
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
  
  public Bag(final Version version, final Charset fileEncoding, final Set<Manifest> payloadManifests, 
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
  
  /**
   * @return the root directory of a bag, usually the folder name
   */
  public Path getRootDir() {
    return rootDir;
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
   * See <a href=
   * "https://tools.ietf.org/html/draft-kunze-bagit#section-3">https://tools.ietf.org/html/draft-kunze-bagit#section-3</a><br>
   * A bag is <b>valid</b> if the bag is complete and every checksum has been
   * verified against the contents of its corresponding file.
   * 
   * @param bag the bag to check
   * @param ignoreHiddenFiles to include hidden files when checking
   * 
   * @return true if the bag is valid or throws an exception
   * 
   */
  public boolean isValid(final boolean ignoreHiddenFiles) throws InvalidBagitFileFormatException, IOException, NoSuchAlgorithmException, CorruptChecksumException, FileNotInPayloadDirectoryException, MissingBagitFileException, MissingPayloadDirectoryException, MissingPayloadManifestException {
    boolean isValid = true;
    
    BagitTextFileVerifier.checkBagitTextFile(this);
    isValid = isComplete(ignoreHiddenFiles) && isValid;
    
    for(final Manifest payloadManifest : payLoadManifests){
      isValid = checkHashes(payloadManifest) && isValid;
    }
    
    for(final Manifest tagManifest : tagManifests){
      isValid = checkHashes(tagManifest) && isValid;
    }
    
    return isValid;
  }
  
  private boolean checkHashes(final Manifest manifest) throws CorruptChecksumException, NoSuchAlgorithmException, IOException{
    final Hasher hasher = BagitChecksumNameMapping.get(manifest.getBagitAlgorithmName());
    
    for(final ManifestEntry entry : manifest.getEntries()) {
      if(Files.exists(entry.getPhysicalLocation())) {
        final String hash = hasher.hash(entry.getPhysicalLocation());
        if (!hash.equals(entry.getChecksum())){
          throw new CorruptChecksumException("File [{}] is suppose to have a [{}] hash of [{}] but was computed [{}].", entry.getPhysicalLocation(), //entry.getRelativeLocation(),
              manifest.getBagitAlgorithmName(), entry.getChecksum(), hash);
        }
      }
    }

    return true;
  }
  
  /**
   * See <a href=
   * "https://tools.ietf.org/html/draft-kunze-bagit#section-3">https://tools.ietf.org/html/draft-kunze-bagit#section-3</a><br>
   * A bag is <b>complete</b> if <br>
   * <ul>
   * <li>every element is present
   * <li>every file in the payload manifest(s) are present
   * <li>every file in the tag manifest(s) are present. Tag files not listed in a
   * tag manifest may be present.
   * <li>every file in the data directory must be listed in at least one payload
   * manifest
   * <li>each element must comply with the bagit spec
   * </ul>
   * 
   * @param bag the bag to check
   * @param ignoreHiddenFiles when checking to ignore hidden files
   * 
   * @return true or throws an exception
   */
  public boolean isComplete(final boolean ignoreHiddenFiles) throws FileNotInPayloadDirectoryException, MissingBagitFileException, MissingPayloadDirectoryException, MissingPayloadManifestException, IOException, MaliciousPathException, InvalidBagitFileFormatException, NoSuchAlgorithmException {
    MandatoryVerifier.checkFetchItemsExist(itemsToFetch, rootDir);
    MandatoryVerifier.checkBagitFileExists(this);
    MandatoryVerifier.checkPayloadDirectoryExists(this);
    MandatoryVerifier.checkIfAtLeastOnePayloadManifestsExist(this);

    ManifestVerifier.verifyManifests(this, ignoreHiddenFiles);
    
    return true;
  }
  
  //TODO cleanup exceptions
  public Bag write(final Path writeTo) throws Exception {
    if(Files.exists(rootDir) && writeTo.equals(rootDir)) {
      //TODO warn writing to same location, so we will not do anything
      throw new RuntimeException("Why are you trying to write to the location where it already exists?");
    }
    
//    logger.info("Writing bag to [{}]", rootDir);
    Files.createDirectories(writeTo);
    
    BagitFileWriter.writeBagitFile(version, fileEncoding, writeTo);
    
    if(!metadata.isEmpty()) {
      MetadataWriter.writeBagMetadata(metadata, version, writeTo, fileEncoding);
    }
    
    if(!itemsToFetch.isEmpty()){
      FetchWriter.writeFetchFile(itemsToFetch, writeTo, fileEncoding);
    }
    
    final Set<Manifest> newPayloadManifests = writeManifests(writeTo, payLoadManifests);
    ManifestWriter.writePayloadManifests(newPayloadManifests, writeTo, fileEncoding);
    
    final Set<Manifest> newTagManifests = writeManifests(writeTo, tagManifests);
    ManifestWriter.writeTagManifests(newTagManifests, writeTo, fileEncoding);
    
    return new Bag(version, fileEncoding, newPayloadManifests, newTagManifests, itemsToFetch, metadata, writeTo);
  }
  
  private Set<Manifest> writeManifests(final Path writeTo, final Set<Manifest> manifests) throws Exception{
    final Set<Manifest> newTagManifests = new HashSet<>();

    for(final Manifest manifest : manifests) {
      final ManifestBuilder manifestBuilder = new ManifestBuilder(manifest.getBagitAlgorithmName());
      
      for(final ManifestEntry entry : manifest.getEntries()) {
        updateEntry(writeTo, manifestBuilder, entry);
      }
      
      newTagManifests.add(manifestBuilder.build());
    }
    return newTagManifests;
  }
  
  private void updateEntry(final Path writeTo, final ManifestBuilder manifestBuilder, final ManifestEntry entry) throws IOException {
    //update physical location in new manifest entry
    final ManifestEntry newEntry = new ManifestEntry(writeTo.resolve(entry.getRelativeLocation()), entry.getRelativeLocation(), entry.getChecksum());
    manifestBuilder.addEntry(newEntry);
    //copy to new location
    final Path newParentLocation = newEntry.getPhysicalLocation().getParent();
    if(!Files.exists(newParentLocation)) {
      Files.createDirectories(newParentLocation);
    }
    Files.copy(entry.getPhysicalLocation(), newEntry.getPhysicalLocation(), StandardCopyOption.REPLACE_EXISTING);
  }
  
  public static BagBuilder getBuilder() {
    return new BagBuilder();
  }
  
  //TODO cleanup exceptions
  public static Bag read(final Path rootDir) throws Exception {
    
    final Path bagitFile = rootDir.resolve("bagit.txt");
    final SimpleImmutableEntry<Version, Charset> bagitInfo = BagitTextFileReader.readBagitTextFile(bagitFile);
    final Version version = bagitInfo.getKey();
    final Charset encoding = bagitInfo.getValue();
    
    final List<SimpleImmutableEntry<String, String>> metadataLines = MetadataReader.readBagMetadata(rootDir, encoding);
    final MetadataBuilder metadataBuilder = new MetadataBuilder();
    metadataBuilder.addAll(metadataLines);
    
    final Path fetchFile = rootDir.resolve("fetch.txt");
    final List<FetchItem> itemsToFetch = new ArrayList<>();
    if(Files.exists(fetchFile)){
      itemsToFetch.addAll(FetchReader.readFetch(fetchFile, encoding, rootDir));
    }
    
    final Set<Manifest> payloadManifests = new HashSet<>();
    final Set<Manifest> tagManifests = new HashSet<>();
    try(final DirectoryStream<Path> manifests = Files.newDirectoryStream(rootDir, new ManifestFilter())){
      for (final Path path : manifests){
        final String filename = PathUtils.getFilename(path);
        
        if(filename.startsWith("tagmanifest-")){
          final Manifest tagManifest = ManifestReader.readManifest(path, rootDir, encoding);
          tagManifests.add(tagManifest);
        }
        else if(filename.startsWith("manifest-")){
          final Manifest payloadManifest = ManifestReader.readManifest(path, rootDir, encoding);
          payloadManifests.add(payloadManifest);
        }
      }
    }    
    
    return new Bag(version, encoding, payloadManifests, tagManifests, itemsToFetch, metadataBuilder.build(), rootDir);
  }
}
