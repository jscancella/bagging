package com.github.jscancella.hash;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import com.github.jscancella.exceptions.HasherInitializationException;
import com.github.jscancella.exceptions.NoSuchBagitAlgorithmException;
import com.github.jscancella.hash.standard.MD5Hasher;
import com.github.jscancella.hash.standard.SHA1Hasher;
import com.github.jscancella.hash.standard.SHA224Hasher;
import com.github.jscancella.hash.standard.SHA256Hasher;
import com.github.jscancella.hash.standard.SHA384Hasher;
import com.github.jscancella.hash.standard.SHA512Hasher;

/**
 * Responsible for mapping between the bagit algorithm name and the actual implementation of that checksum.
 * By default this includes implementations {@link MD5Hasher}, {@link SHA1Hasher}, {@link SHA224Hasher}, {@link SHA256Hasher},
 *  {@link SHA384Hasher}, and {@link SHA512Hasher}.
 * To override a default implementation, simple add the same bagit algorithm name and new {@link Hasher} implementation. 
 * Example:
 * {@code BagitChecksumNameMapping.add("md5", new MyNewMD5Hasher());} 
 */
public enum BagitChecksumNameMapping {
  /**
   * using enum to ensure singleton
   */
  INSTANCE;
  
  private static final Logger logger = LoggerFactory.getLogger(BagitChecksumNameMapping.class);
  private final Map<String, Class<? extends Hasher>> map = new ConcurrentHashMap<>();

  BagitChecksumNameMapping() {
    map.put(MD5Hasher.BAGIT_ALGORITHM_NAME, MD5Hasher.class);
    map.put(SHA1Hasher.BAGIT_ALGORITHM_NAME, SHA1Hasher.class);
    map.put(SHA224Hasher.BAGIT_ALGORITHM_NAME, SHA224Hasher.class);
    map.put(SHA256Hasher.BAGIT_ALGORITHM_NAME, SHA256Hasher.class);
    map.put(SHA384Hasher.BAGIT_ALGORITHM_NAME, SHA384Hasher.class);
    map.put(SHA512Hasher.BAGIT_ALGORITHM_NAME, SHA512Hasher.class);
  }
  
  /**
   * map an implementation to the bagit algorithm name 
   * @param bagitAlgorithmName the all lowercase name as defined in the specification
   * @param implementation the implementation class that will be used to compute the checksum
   * @return if the implementation was successfully added
   */
  public static boolean add(final String bagitAlgorithmName, final Class<? extends Hasher> implementation) {
    INSTANCE.map.put(bagitAlgorithmName, implementation);
    return true;
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
    
    try {
      final Hasher hasher = INSTANCE.map.get(bagitAlgorithmName).getDeclaredConstructor().newInstance();
      hasher.initialize();
      return hasher;
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
      final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
      logger.error(messages.getString("failed_to_init_hasher"), bagitAlgorithmName, e);
      throw new HasherInitializationException(e);
    }
  }
}
