package com.github.jscancella.conformance.internal;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jscancella.conformance.BagitWarning;
import com.github.jscancella.reader.internal.MetadataReader;

/**
 * Part of the BagIt conformance suite. 
 * This checker checks the bag metadata (bag-info.txt) for various problems.
 */
public enum MetadataChecker { ;// using enum to enforce singleton
  private static final Logger logger = LoggerFactory.getLogger(MetadataChecker.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  
  /**
   * Check the bag metadata (bag-info.txt) for various conformance
   * 
   * @param bagitDir The base directory of the bag
   * @param encoding The encoding of the bag tag files
   * @param warnings The list of warnings to add to if the bag does not conform
   * @param warningsToIgnore The list of warnings to ignore
   * 
   * @throws IOException If there is a problem reading the bag
   */
  public static void checkBagMetadata(final Path bagitDir, final Charset encoding, final Set<BagitWarning> warnings, 
      final Collection<BagitWarning> warningsToIgnore) throws IOException{
    checkForPayloadOxumMetadata(bagitDir, encoding, warnings, warningsToIgnore);
  }
  
  /*
   * Check that the metadata contains the Payload-Oxum key-value pair
   */
  private static void checkForPayloadOxumMetadata(final Path bagitDir, final Charset encoding, final Set<BagitWarning> warnings, 
      final Collection<BagitWarning> warningsToIgnore) throws IOException{
    if(!warningsToIgnore.contains(BagitWarning.PAYLOAD_OXUM_MISSING)){
      final List<SimpleImmutableEntry<String, String>> metadata = MetadataReader.readBagMetadata(bagitDir, encoding);
      boolean containsPayloadOxum = false;
      
      for(final SimpleImmutableEntry<String, String> pair : metadata){
        if("Payload-Oxum".equals(pair.getKey())){
          containsPayloadOxum = true;
        }
      }
      
      if(!containsPayloadOxum){
        logger.warn(messages.getString("missing_payload_oxum_warning"));
        warnings.add(BagitWarning.PAYLOAD_OXUM_MISSING);
      }
    }
  }
}
