package com.github.jscancella.writer.internal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jscancella.domain.Bag;
import com.github.jscancella.domain.FetchItem;
import com.github.jscancella.domain.Manifest;

/**
 * Responsible for writing out the bag payload to the filesystem
 */
public enum PayloadWriter {;//using enum to enforce singleton
  private static final Logger logger = LoggerFactory.getLogger(PayloadWriter.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  
  /*
   * Write the payload files in the data directory or under the root directory depending on the version
   */
  public static void writeVersionDependentPayloadFiles(final Bag bag, final Path outputDir) throws IOException{
    final Path dataDir = outputDir.resolve("data");
    Files.createDirectories(dataDir);
    writePayloadFiles(bag.getPayLoadManifests(), bag.getItemsToFetch(), dataDir, bag.getRootDir().resolve("data"));
  }
  
  /**
  * Write the payload <b>file(s)</b> to the output directory
  * 
  * @param payloadManifests the set of objects representing the payload manifests
  * @param fetchItems the list of items to exclude from writing in the output directory because they will be fetched
  * @param outputDir the data directory of the bag
  * @param bagDataDir the data directory of the bag
  * 
  * @throws IOException if there was a problem writing a file
  */
 public static void writePayloadFiles(final Set<Manifest> payloadManifests, final List<FetchItem> fetchItems, final Path outputDir, final Path bagDataDir) throws IOException{
   logger.info(messages.getString("writing_payload_files"));
   final Set<Path> fetchPaths = getFetchPaths(fetchItems, bagDataDir);
   
   for(final Manifest payloadManifest : payloadManifests){
     for(final Path payloadFile : payloadManifest.getFileToChecksumMap().keySet()){
       final Path relativePayloadPath = bagDataDir.relativize(payloadFile);
       
       if(fetchPaths.contains(relativePayloadPath.normalize())) {
         logger.info(messages.getString("skip_fetch_item_when_writing_payload"), payloadFile);
       }
       else {
         final Path writeToPath = outputDir.resolve(relativePayloadPath);
         logger.debug(messages.getString("writing_payload_file_to_path"), payloadFile, writeToPath);
         final Path parent = writeToPath.getParent();
         if(parent != null){
           Files.createDirectories(parent);
         }
         Files.copy(payloadFile, writeToPath, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
       }
     }
   }
 }
 
  private static Set<Path> getFetchPaths(final List<FetchItem> fetchItems, final Path bagDataDir) {
    final Set<Path> fetchPaths = new HashSet<>();
    for (final FetchItem fetchItem : fetchItems) {
      final Path parent = bagDataDir.getParent();
      if(parent != null){
        fetchPaths.add(bagDataDir.relativize(parent.resolve(fetchItem.getPath())));
      }
    }
    return fetchPaths;
 }
}
