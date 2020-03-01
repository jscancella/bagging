package com.github.jscancella.hash;

import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

/**
 * The interface that any supported checksum algorithm must implement.
 */
public interface Hasher {

  /**
   * Create a HEX formatted string checksum hash of the file
   * 
   * @param path the {@link Path} (file) to hash
   * 
   * @return the hash as a hex formated string
   * 
   * @throws IOException if there is a problem reading the file
   */
  String hash(final Path path) throws IOException;
  
  /**
   * Used to do any pre hashing initialization.
   * 
   * @throws NoSuchAlgorithmException if there is a problem during initialization
   */
  void initialize() throws NoSuchAlgorithmException;
  
  /**
   * For calculating large file checksums it is more efficient to stream the file, thus the need to be able to update a checksum.
   * <b>NOT THREAD SAFE</b> 
   * 
   * @param bytes the bytes with which to update the checksum
   */
  void update(final byte[] bytes);
  
  /**
   * @return the checksum of the streamed file. If no file has been streamed, returns a default hash.
   */
  String getHash();
  
  /**
   * When streaming a file, we have no way of knowing when we are done updating. This method allows for a reset of the current stream.
   */
  void reset();
  
  /**
   * @return the bagit formatted version of the algorithm name. For example if the hasher implements MD5, it would return md5 as the name. 
   */
  String getBagitAlgorithmName();
}
