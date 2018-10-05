package com.github.jscancella.hash;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for mapping between the bagit algorithm name and the actual implementation of that checksum.
 * By default this includes implementations for MD5, SHA1, SHA224, SHA256, SHA384, and SHA512. 
 * To override a default implementation, simple add the same bagit algorithm name and new {@link Hasher} implementation. 
 * Example:
 * {@code BagitChecksumNameMapping.add("md5", new MyNewMD5Hasher());} 
 */
public enum BagitChecksumNameMapping {
  INSTANCE; //using enum to ensure singleton
  private Map<String, Hasher> map = new HashMap<>();

  private BagitChecksumNameMapping() {
    map.put("md5", StandardHasher.MD5);
    map.put("sha1", StandardHasher.SHA1);
    map.put("sha224", StandardHasher.SHA224);
    map.put("sha256", StandardHasher.SHA256);
    map.put("sha384", StandardHasher.SHA384);
    map.put("sha512", StandardHasher.SHA512);
  }
  
  public static void add(final String bagitAlgorithmName, final Hasher implementation) {
    INSTANCE.map.put(bagitAlgorithmName, implementation);
  }
  
  public static void clear(final String bagitAlgorithmName) {
    INSTANCE.map.remove(bagitAlgorithmName);
  }
  
  public static Hasher get(final String bagitAlgorithmName) throws NoSuchAlgorithmException {
    if(!INSTANCE.map.containsKey(bagitAlgorithmName)) {
      throw new NoSuchAlgorithmException("No implementation of " + bagitAlgorithmName + " was found. Did you remember to add it to " + INSTANCE.toString() + "?");
    }
    return INSTANCE.map.get(bagitAlgorithmName);
  }
}
