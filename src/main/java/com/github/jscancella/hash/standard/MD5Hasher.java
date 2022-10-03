package com.github.jscancella.hash.standard;

/**
 * A default implementation for the MD5 checksum
 */
public final class MD5Hasher extends AbstractMessageDigestHasher{
  /**
   * the bagit algorithm name
   */
  public static final String BAGIT_ALGORITHM_NAME = "md5";

  /**
   * A default implementation for the MD5 checksum that maps to the md5 bagit algorithm name
   */
  public MD5Hasher() {
    super("MD5", BAGIT_ALGORITHM_NAME);
  }

}
