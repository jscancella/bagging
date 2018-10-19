package com.github.jscancella.verify.internal;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import com.github.jscancella.domain.Bag;
import com.github.jscancella.domain.FetchItem;
import com.github.jscancella.exceptions.FileNotInPayloadDirectoryException;
import com.github.jscancella.exceptions.MissingBagitFileException;
import com.github.jscancella.exceptions.MissingPayloadDirectoryException;
import com.github.jscancella.exceptions.MissingPayloadManifestException;
import com.github.jscancella.internal.PathUtils;

/**
 * Responsible for checking all things related to mandatory files for the bagit specification
 */
public enum MandatoryVerifier {; //using enum to enforce singleton
  private static final Logger logger = LoggerFactory.getLogger(MandatoryVerifier.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");

  /**
   * make sure all the fetch items exist in the data directory
   * 
   * @param items the items that needed to be fetched for the bag to be complete
   * @param bagDir the root directory of the bag
   * 
   * @throws FileNotInPayloadDirectoryException if one or more of the fetch items don't exist
   */
  public static void checkFetchItemsExist(final List<FetchItem> items, final Path bagDir) throws FileNotInPayloadDirectoryException{
    logger.info(messages.getString("checking_fetch_items_exist"), items.size(), bagDir);
    for(final FetchItem item : items){
      if(!Files.exists(item.getPath())){
        final String formattedMessage = messages.getString("fetch_item_missing_error");
        throw new FileNotInPayloadDirectoryException(MessageFormatter.format(formattedMessage, item).getMessage());
      }
    }
  }
  
  /**
   * make sure the bagit.txt file exists
   * 
   * @param bag the bag to check
   * @throws MissingBagitFileException if the bag does not contain the bagit.txt file as required by the bagit specification
   */
  public static void checkBagitFileExists(final Bag bag) throws MissingBagitFileException{
    logger.info("Checking if bagit.txt file exists");
    final Path bagitFile = bag.getTagFileDir().resolve("bagit.txt");
    
    if(!Files.exists(bagitFile)){
      final String formattedMessage = messages.getString("file_should_exist_error");
      throw new MissingBagitFileException(MessageFormatter.format(formattedMessage, bagitFile).getMessage());
    }
  }
  
  /**
   * Make sure the payload directory exists
   * 
   * @param bag the bag to check
   * 
   * @throws MissingPayloadDirectoryException if the bag does not contain the payload directory
   */
  public static void checkPayloadDirectoryExists(final Bag bag) throws MissingPayloadDirectoryException{
    logger.info(messages.getString("checking_payload_directory_exists"));
    final Path dataDir = bag.getDataDir();
    
    if(!Files.exists(dataDir)){
      throw new MissingPayloadDirectoryException(messages.getString("file_should_exist_error"), dataDir);
    }
  }
  
  /**
   * Check to make sure the bag has at least one payload manifest
   * (manifest-[ALGORITHM].txt)
   * 
   * @param bag the bag to check
   * 
   * @throws MissingPayloadManifestException if there are no payload manifests in the bag
   * @throws IOException if there was an error reading a file
   */
  public static void checkIfAtLeastOnePayloadManifestsExist(final Bag bag) throws MissingPayloadManifestException, IOException{
    logger.info("Checking if there is at least one payload manifest in [{}]", bag.getRootDir());
    
    try(DirectoryStream<Path> directoryStream = Files.newDirectoryStream(bag.getTagFileDir())){
      for(final Path path : directoryStream){
        if(PathUtils.getFilename(path).startsWith("manifest-")){
          logger.debug(messages.getString("found_payload_manifest"), path.getFileName());
          return;
        }
      }
    }    
    
    throw new MissingPayloadManifestException(messages.getString("missing_payload_manifest_error"));
  }
}
