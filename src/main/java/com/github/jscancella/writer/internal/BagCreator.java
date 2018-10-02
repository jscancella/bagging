package com.github.jscancella.writer.internal;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jscancella.domain.Bag;
import com.github.jscancella.domain.Manifest;
import com.github.jscancella.domain.Metadata;
import com.github.jscancella.domain.Version;
import com.github.jscancella.hash.BagitChecksumNameMapping;
import com.github.jscancella.hash.Hasher;
import com.github.jscancella.hash.PayloadOxumGenerator;

/**
 * Responsible for creating a bag in place.
 */
public enum BagCreator {; //Using Enum to enforce singleton
  private static final Logger logger = LoggerFactory.getLogger(BagCreator.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  private static final String DATE_FORMAT = "yyyy-MM-dd";
  
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
    return bagInPlace(root, algorithms, includeHidden, new Metadata());
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
    final Bag bag = new Bag(Version.LATEST_BAGIT_VERSION());
    logger.info(messages.getString("creating_bag"), bag.getVersion(), root);
    bag.setRootDir(root);
    
    movePayloadFilesToDataDir(bag, includeHidden);
    BagitFileWriter.writeBagitFile(bag.getVersion(), bag.getFileEncoding(), bag.getRootDir()); //create the bagit.txt file
    createPayloadManifests(bag, algorithms, includeHidden);
    createMetadataFile(bag, metadata);
    createTagManifests(bag, algorithms, includeHidden); //must come last since it needs to calculate checksums for other tag files
    
    return bag;
  }
  
  private static void movePayloadFilesToDataDir(final Bag bag, final boolean includeHidden) throws IOException {
    final Path tempDir = bag.getRootDir().resolve(System.currentTimeMillis() + ".temp");
    Files.createDirectory(tempDir);
    try(final DirectoryStream<Path> directoryStream = Files.newDirectoryStream(bag.getRootDir())){
      for(final Path path : directoryStream){
        if(!path.equals(tempDir) && (!isHidden(path) || includeHidden)){
          Files.move(path, tempDir.resolve(path.getFileName()));
        }
      }
    }
    Files.move(tempDir, bag.getDataDir());
  }
  
  private static void createPayloadManifests(final Bag bag, final Collection<String> algorithms, final boolean includeHidden) throws NoSuchAlgorithmException, IOException{
    logger.info(messages.getString("creating_payload_manifests"));
    
    Map<Manifest, Hasher> manifestToHasherMap = new HashMap<>();
    for(String algorithm : algorithms) {
      final Manifest manifest = new Manifest(algorithm);
      final Hasher hasher = BagitChecksumNameMapping.get(algorithm);
      manifestToHasherMap.put(manifest, hasher);
    }
    
    final CreatePayloadManifestsVistor payloadVisitor = new CreatePayloadManifestsVistor(manifestToHasherMap, includeHidden);
    Files.walkFileTree(bag.getDataDir(), payloadVisitor);
    
    bag.getPayLoadManifests().addAll(manifestToHasherMap.keySet());
    ManifestWriter.writePayloadManifests(bag.getPayLoadManifests(), bag.getTagFileDir(), bag.getRootDir(), bag.getFileEncoding());
  }
  
  private static void createMetadataFile(final Bag bag, final Metadata metadata) throws IOException{
    bag.setMetadata(metadata);
    
    logger.debug(messages.getString("calculating_payload_oxum"), bag.getDataDir());
    final String payloadOxum = PayloadOxumGenerator.generatePayloadOxum(bag.getDataDir());
    bag.getMetadata().upsertPayloadOxum(payloadOxum);
    
    bag.getMetadata().remove("Bagging-Date"); //remove the old bagging date if it exists so that there is only one
    bag.getMetadata().add("Bagging-Date", new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(new Date()));
    
    logger.info(messages.getString("creating_metadata_file"));
    MetadataWriter.writeBagMetadata(bag.getMetadata(), bag.getVersion(), bag.getRootDir(), bag.getFileEncoding());
  }
  
  private static void createTagManifests(final Bag bag, final Collection<String> algorithms, final boolean includeHidden) throws NoSuchAlgorithmException, IOException{
    logger.info(messages.getString("creating_tag_manifests"));
    
    Map<Manifest, Hasher> manifestToHasherMap = new HashMap<>();
    for(String algorithm : algorithms) {
      final Manifest manifest = new Manifest(algorithm);
      final Hasher hasher = BagitChecksumNameMapping.get(algorithm);
      manifestToHasherMap.put(manifest, hasher);
    }
    final CreateTagManifestsVistor tagVistor = new CreateTagManifestsVistor(manifestToHasherMap, includeHidden);
    Files.walkFileTree(bag.getTagFileDir(), tagVistor);
    
    bag.getTagManifests().addAll(manifestToHasherMap.keySet());
    ManifestWriter.writeTagManifests(bag.getTagManifests(), bag.getTagFileDir(), bag.getRootDir(), bag.getFileEncoding());
  }
  
  /**
   * Due to the way that windows handles hidden files vs. *nix 
   * we use this method to determine if a file or folder is really hidden
   * @param path the file or folder to check if hidden
   * @return if the file or folder is hidden
   * @throws IOException if there is an error reading the file/folder
   */
  private static boolean isHidden(final Path path) throws IOException{
    //cause Files.isHidden() doesn't work properly for windows if the file is a directory
    if (System.getProperty("os.name").contains("Windows")){
      return Files.readAttributes(path, DosFileAttributes.class).isHidden() || Files.isHidden(path);
    }

    return Files.isHidden(path);
  }
}
