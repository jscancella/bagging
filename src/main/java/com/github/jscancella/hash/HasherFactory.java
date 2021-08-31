package com.github.jscancella.hash;

/**
 * Interface for creating {@link Hasher} instances
 */
public interface HasherFactory {

  /**
   * Create a new {@link Hasher} instance
   *
   * @return a new hasher
   */
  Hasher createHasher();

  /**
   * @return the bagit formatted version of the algorithm name. For example if the hasher implements MD5, it would return md5 as the name.
   */
  String getBagitAlgorithmName();

}
