package com.github.jscancella.hash;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

/**
 * Responsible for mapping between the bagit algorithm name and the actual implementation of that checksum.
 * By default this includes implementations from {@link StandardHasher}. 
 * To override a default implementation, simple add the same bagit algorithm name and new {@link Hasher} implementation. 
 * Example:
 * {@code BagitChecksumNameMapping.add("md5", new MyNewMD5Hasher());} 
 */
public enum BagitChecksumNameMapping {
  INSTANCE; //using enum to ensure singleton
  
  private final Map<String, Hasher> map = new HashMap<>();

  private BagitChecksumNameMapping() {
    final Logger logger = LoggerFactory.getLogger(BagitChecksumNameMapping.class);
    
    for(final Hasher hasher : StandardHasher.values()) {
      try {
        hasher.initialize();
        map.put(hasher.getBagitAlgorithmName(), hasher);
      }
      catch(NoSuchAlgorithmException e) {
        final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
        logger.error(messages.getString("failed_to_init_hasher"), hasher.getBagitAlgorithmName(), e);
      }
    }
  }
  
  public static boolean add(final String bagitAlgorithmName, final Hasher implementation) {
    try {
      implementation.initialize();
      INSTANCE.map.put(bagitAlgorithmName, implementation);
      return true;
    }
    catch(NoSuchAlgorithmException e) {
      final Logger logger = LoggerFactory.getLogger(BagitChecksumNameMapping.class);
      final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
      logger.error(messages.getString("hasher_setup"), implementation.getBagitAlgorithmName(), e);
    }
    return false;
  }
  
  public static void clear(final String bagitAlgorithmName) {
    INSTANCE.map.remove(bagitAlgorithmName);
  }
  
  public static Hasher get(final String bagitAlgorithmName) throws NoSuchAlgorithmException {
    if(!INSTANCE.map.containsKey(bagitAlgorithmName)) {
      final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
      final String message = MessageFormatter.format(messages.getString("no_implementation_error"), bagitAlgorithmName, INSTANCE.toString()).getMessage();
      throw new NoSuchAlgorithmException(message);
    }
    return INSTANCE.map.get(bagitAlgorithmName);
  }
}
