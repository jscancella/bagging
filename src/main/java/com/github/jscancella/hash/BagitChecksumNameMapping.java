package com.github.jscancella.hash;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import com.github.jscancella.exceptions.NoSuchBagitAlgorithmException;

/**
 * Responsible for mapping between the bagit algorithm name and the actual implementation of that checksum.
 * By default this includes implementations from {@link StandardHasher}. 
 * To override a default implementation, simply add the same bagit algorithm name and new {@link HasherFactory} implementation.
 * Example:
 * {@code BagitChecksumNameMapping.add("md5", new MyNewMD5HasherFactory());}
 */
@SuppressWarnings("PMD.MoreThanOneLogger")
public enum BagitChecksumNameMapping {
  INSTANCE; //using enum to ensure singleton
  
  private static final Logger logger = LoggerFactory.getLogger(BagitChecksumNameMapping.class);
  private final Map<String, HasherFactory> map = new ConcurrentHashMap<>();

  BagitChecksumNameMapping() {
    final Logger logger = LoggerFactory.getLogger(BagitChecksumNameMapping.class);
    for(final HasherFactory hasherFactory : StandardHasher.values()) {
      try {
        // verify the algorithm exists
        hasherFactory.createHasher().initialize();
        map.put(hasherFactory.getBagitAlgorithmName(), hasherFactory);
      }
      catch(NoSuchAlgorithmException e) {
        final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
        logger.error(messages.getString("failed_to_init_hasher"), hasherFactory.getBagitAlgorithmName(), e);
      }
    }
  }
  
  /**
   * map an implementation to the bagit algorithm name 
   * @param bagitAlgorithmName the all lowercase name as defined in the specification
   * @param implementation the implementation that will be used to compute the checksum
   * @return if the implementation was successfully added
   */
  public static boolean add(final String bagitAlgorithmName, final HasherFactory implementation) {
    boolean addedSuccessfully = false;
    try {
      // verify the algorithm exists
      implementation.createHasher().initialize();
      INSTANCE.map.put(bagitAlgorithmName, implementation);
      addedSuccessfully = true;
    }
    catch(NoSuchAlgorithmException e) {
      final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
      logger.error(messages.getString("hasher_setup"), implementation.getBagitAlgorithmName(), e);
    }
    return addedSuccessfully;
  }
  
  /**
   * remove a particular implementation
   * @param bagitAlgorithmName the name of the algorithm
   */
  public static void clear(final String bagitAlgorithmName) {
    INSTANCE.map.remove(bagitAlgorithmName);
  }
  
  /**
   * check if a bagit algorithm is supported
   * @param bagitAlgorithmName the name of the algorithm
   * @return true if a bagit algorithm is supported
   */
  public static boolean isSupported(final String bagitAlgorithmName) {
    return INSTANCE.map.containsKey(bagitAlgorithmName);
  }
  
  /**
   * Get a specific implementation associated with the bagit algorithm name
   * @param bagitAlgorithmName the name of the algorithm
   * @return specific implementation associated with the bagit algorithm name
   */
  public static Hasher get(final String bagitAlgorithmName){
    if(!INSTANCE.map.containsKey(bagitAlgorithmName)) {
      final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
      final String message = MessageFormatter.format(messages.getString("no_implementation_error"), bagitAlgorithmName, INSTANCE.toString()).getMessage();
      throw new NoSuchBagitAlgorithmException(message);
    }

    final HasherFactory factory = INSTANCE.map.get(bagitAlgorithmName);
    final Hasher hasher = factory.createHasher();
    try {
      hasher.initialize();
    } catch (NoSuchAlgorithmException e) {
      // This should never happen because only factories that were tested will appear in the map
      throw new IllegalStateException(e);
    }

    return hasher;
  }

}
