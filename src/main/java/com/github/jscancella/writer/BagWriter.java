package com.github.jscancella.writer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jscancella.domain.Bag;
import com.github.jscancella.domain.Manifest;
import com.github.jscancella.domain.Metadata;
import com.github.jscancella.domain.Version;
import com.github.jscancella.hash.BagitChecksumNameMapping;
import com.github.jscancella.hash.Hasher;
import com.github.jscancella.hash.PayloadOxumGenerator;
import com.github.jscancella.writer.internal.BagCreator;
import com.github.jscancella.writer.internal.BagitFileWriter;
import com.github.jscancella.writer.internal.FetchWriter;
import com.github.jscancella.writer.internal.ManifestWriter;
import com.github.jscancella.writer.internal.MetadataWriter;
import com.github.jscancella.writer.internal.PayloadWriter;

/**
 * responsible for writing out a {@link Bag} to the filesystem
 */
public enum BagWriter {; //using enum to ensure singleton
  private static final Logger logger = LoggerFactory.getLogger(BagWriter.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");

  
  /**
   * Write the bag out to the specified directory. 
   * If an error occurs some of the files may have been written out to the filesystem.
   * tag manifest(s) are updated prior to writing to ensure bag is valid after completion, 
   * it is therefore recommended if you are going to further interact with the bag to read it from specified outputDir path
   * 
   * @param bag the {@link Bag} object to write out
   * @param outputDir the output directory that will become the root of the bag
   * 
   * @throws IOException if there is a problem writing a file
   * @throws NoSuchAlgorithmException when trying to generate a {@link MessageDigest} which is used during update.
   */
  public static void write(final Bag bag, final Path outputDir) throws IOException, NoSuchAlgorithmException{
    logger.debug(messages.getString("writing_payload_files"));
    final Path bagitDir = PayloadWriter.writeVersionDependentPayloadFiles(bag, outputDir);
    
    logger.debug(messages.getString("upsert_payload_oxum"));
    final String payloadOxum = PayloadOxumGenerator.generatePayloadOxum(bag.getDataDir());
    bag.getMetadata().upsertPayloadOxum(payloadOxum);
    
    logger.debug(messages.getString("writing_bagit_file"));
    BagitFileWriter.writeBagitFile(bag.getVersion(), bag.getFileEncoding(), bagitDir);
    
    logger.debug(messages.getString("writing_payload_manifests"));
    ManifestWriter.writePayloadManifests(bag.getPayLoadManifests(), bagitDir, bag.getRootDir(), bag.getFileEncoding());

    if(!bag.getMetadata().isEmpty()){
      logger.debug(messages.getString("writing_bag_metadata"));
      MetadataWriter.writeBagMetadata(bag.getMetadata(), bag.getVersion(), bagitDir, bag.getFileEncoding());
    }
    if(bag.getItemsToFetch().size() > 0){
      logger.debug(messages.getString("writing_fetch_file"));
      FetchWriter.writeFetchFile(bag.getItemsToFetch(), bagitDir, bag.getRootDir(), bag.getFileEncoding());
    }
    if(bag.getTagManifests().size() > 0){
      logger.debug(messages.getString("writing_tag_manifests"));
      writeTagManifestFiles(bag.getTagManifests(), bagitDir, bag.getRootDir());
      final Set<Manifest> updatedTagManifests = updateTagManifests(bag, outputDir);
      bag.setTagManifests(updatedTagManifests);
      ManifestWriter.writeTagManifests(updatedTagManifests, bagitDir, outputDir, bag.getFileEncoding());
    }
  }
  
  /*
   * Update the tag manifest cause the checksum of the other tag files will have changed since we just wrote them out to disk
   */
  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  private static Set<Manifest> updateTagManifests(final Bag bag, final Path newBagRootDir) throws NoSuchAlgorithmException, IOException{
    final Set<Manifest> newManifests = new HashSet<>();
    
    for(final Manifest tagManifest : bag.getTagManifests()){
      final Manifest newManifest = new Manifest(tagManifest.getBagitAlgorithmName());
      
      for(final Path originalPath : tagManifest.getFileToChecksumMap().keySet()){
        final Path relativePath = bag.getRootDir().relativize(originalPath);
        final Path pathToUpdate = newBagRootDir.resolve(relativePath);
        final Hasher hasher = BagitChecksumNameMapping.get(tagManifest.getBagitAlgorithmName());
        final String newChecksum = hasher.hash(pathToUpdate);
        newManifest.getFileToChecksumMap().put(pathToUpdate, newChecksum);
      }
      
      newManifests.add(newManifest);
    }
    
    return newManifests;
  }
  
  /*
   * Write the tag manifest files
   */
  private static void writeTagManifestFiles(final Set<Manifest> manifests, final Path outputDir, final Path bagRootDir) throws IOException{
    for(final Manifest manifest : manifests){
      for(final Entry<Path, String> entry : manifest.getFileToChecksumMap().entrySet()){
        final Path relativeLocation = bagRootDir.relativize(entry.getKey());
        final Path writeTo = outputDir.resolve(relativeLocation);
        final Path writeToParent = writeTo.getParent();
        if(!Files.exists(writeTo) && writeToParent != null){
          Files.createDirectories(writeToParent);
          Files.copy(entry.getKey(), writeTo);
        }
      }
    }
  }
  
  /**
   * Creates a bag in place for {@link Version.LATEST_BAGIT_VERSION}.
   * This method moves and creates files, thus if an error is thrown during operation it may leave the filesystem 
   * in an unknown state of transition. Thus this is <b>not thread safe</b>
   * 
   * @param root the directory that will become the base of the bag and where to start searching for content
   * @param algorithms an collection of bagit algorithm names which will be used for creating manifests
   * @param includeHidden to include hidden files when generating the bagit files, like the manifests
   * 
   * @throws NoSuchAlgorithmException if {@link BagitCheckumNameMapping} can't find the algorithm
   * @throws IOException if there is a problem writing or moving file(s)
   * 
   * @return a {@link Bag} object representing the newly created bagit bag
   */
  public static Bag bagInPlace(final Path root, final Collection<String> algorithms, final boolean includeHidden) throws NoSuchAlgorithmException, IOException{
    return BagCreator.bagInPlace(root, algorithms, includeHidden);
  }
  
  /**
  * Creates a bag in place for version 0.97.
  * This method moves and creates files, thus if an error is thrown during operation it may leave the filesystem 
  * in an unknown state of transition. Thus this is <b>not thread safe</b>
  * 
  * @param root the directory that will become the base of the bag and where to start searching for content
  * @param algorithms an collection of bagit algorithm names which will be used for creating manifests
  * @param includeHidden to include hidden files when generating the bagit files, like the manifests
  * @param metadata the metadata to include when creating the bag. Payload-Oxum and Bagging-Date will be overwritten 
  * 
  * @throws NoSuchAlgorithmException if {@link MessageDigest} can't find the algorithm
  * @throws IOException if there is a problem writing or moving file(s)
  * 
  * @return a {@link Bag} object representing the newly created bagit bag
  */
 public static Bag bagInPlace(final Path root, final Collection<String> algorithms, final boolean includeHidden, final Metadata metadata) throws NoSuchAlgorithmException, IOException{
   return BagCreator.bagInPlace(root, algorithms, includeHidden, metadata);
 }
}
