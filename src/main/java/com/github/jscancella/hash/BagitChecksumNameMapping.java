package com.github.jscancella.hash;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import com.github.jscancella.hash.defaults.MD5Hasher;
import com.github.jscancella.hash.defaults.SHA1Hasher;
import com.github.jscancella.hash.defaults.SHA224Hasher;
import com.github.jscancella.hash.defaults.SHA256Hasher;
import com.github.jscancella.hash.defaults.SHA384Hasher;
import com.github.jscancella.hash.defaults.SHA512Hasher;

/**
 * Responsible for mapping between the bagit algorithm name and the actual implementation of that checksum.
 * By default this includes implementations for MD5, SHA1, SHA224, SHA256, SHA384, and SHA512. 
 * To override a default implementation, simple add the same bagit algorithm name and new {@link Hasher} implementation. 
 * Example:
 * {@code BagitChecksumNameMapping.add("md5", new MyNewMD5Hasher());} 
 */
public enum BagitCheckumNameMapping {
  INSTANCE; //using enum to ensure singleton
  private Map<String, Hasher> map = new HashMap<>();

  private BagitCheckumNameMapping() {
    map.put("md5", MD5Hasher.INSTANCE);
    map.put("sha1", SHA1Hasher.INSTANCE);
    map.put("sha224", SHA224Hasher.INSTANCE);
    map.put("sha256", SHA256Hasher.INSTANCE);
    map.put("sha384", SHA384Hasher.INSTANCE);
    map.put("sha512", SHA512Hasher.INSTANCE);
  }
  
  public static void add(final String bagitAlgorithmName, final Hasher implementation) {
    INSTANCE.map.put(bagitAlgorithmName, implementation);
  }
  
  public static Hasher get(final String bagitAlgorithmName) throws NoSuchAlgorithmException {
    if(!INSTANCE.map.containsKey(bagitAlgorithmName)) {
      throw new NoSuchAlgorithmException("No implementation of " + bagitAlgorithmName + " was found. Did you remember to add it to " + INSTANCE.toString() + "?");
    }
    return INSTANCE.map.get(bagitAlgorithmName);
  }
}
