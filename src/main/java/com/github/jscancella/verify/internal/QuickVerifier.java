package com.github.jscancella.verify.internal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import com.github.jscancella.domain.Bag;
import com.github.jscancella.exceptions.InvalidPayloadOxumException;
import com.github.jscancella.exceptions.PayloadOxumDoesNotExistException;
import com.github.jscancella.hash.internal.FileCountAndTotalSizeVistor;
import com.github.jscancella.verify.BagVerifier;

/**
 * responsible for all things related to quick verification. Quick verification does not
 * mean that a Bag is valid, only that a cursory check has been made. For a full verification
 * see {@link BagVerifier}
 */
public enum QuickVerifier {;//using enum to enforce singleton
  private static final Logger logger = LoggerFactory.getLogger(QuickVerifier.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  private static final String PAYLOAD_OXUM_REGEX = "\\d+\\.\\d+";
  
  /*
   * Get the Payload-Oxum value from the key value pairs
   */
  private static String getPayloadOxum(final Bag bag){
    for(final SimpleImmutableEntry<String,String> keyValue : bag.getMetadata().getAll()){
      if("Payload-Oxum".equals(keyValue.getKey())){
        return keyValue.getValue();
      }
    }
    return null;
  }
  
  /**
   * Quickly verify by comparing the number of files and the total number of bytes expected
   * 
   * @param bag the bag to verify by payload-oxum
   * 
   * @throws IOException if there is an error reading a file
   * @throws InvalidPayloadOxumException if either the total bytes or the number of files 
   * calculated for the payload directory of the bag is different than the supplied values
   * @throws PayloadOxumDoesNotExistException if the bag does not contain a payload-oxum.
   * 
   * @deprecated the Payload-Oxum is a hack of the bag metadata. In the future the BagIt specification should instead include this information in the bagit.txt
   */
  @Deprecated
  public static void quicklyVerify(final Bag bag) throws IOException, InvalidPayloadOxumException, PayloadOxumDoesNotExistException{
    final String payloadOxum = getPayloadOxum(bag);
    if(payloadOxum == null || !payloadOxum.matches(PAYLOAD_OXUM_REGEX)){
      throw new PayloadOxumDoesNotExistException(messages.getString("payload_oxum_missing_error"));
    }

    final String[] parts = payloadOxum.split("\\.");
    logger.debug(messages.getString("parse_size_in_bytes"), parts[0]);
    final long totalSize = Long.parseLong(parts[0]);
    logger.debug(messages.getString("parse_number_of_files"), parts[1]);
    final long numberOfFiles = Long.parseLong(parts[1]);
    
    final Path payloadDir = bag.getDataDir();
    final FileCountAndTotalSizeVistor vistor = new FileCountAndTotalSizeVistor();
    Files.walkFileTree(payloadDir, vistor);
    logger.debug(messages.getString("compare_payload_oxums"), payloadOxum, vistor.getTotalSize(), vistor.getCount(), payloadDir);
    
    if(totalSize != vistor.getTotalSize()){
      final String formattedMessage = messages.getString("invalid_total_size_error");
      throw new InvalidPayloadOxumException(MessageFormatter.format(formattedMessage, totalSize, vistor.getTotalSize()).getMessage());
    }
    if(numberOfFiles != vistor.getCount()){
      final String formattedMessage = messages.getString("invalid_file_cound_error");
      throw new InvalidPayloadOxumException(MessageFormatter.format(formattedMessage, numberOfFiles, vistor.getCount()).getMessage());
    }
  }
}
