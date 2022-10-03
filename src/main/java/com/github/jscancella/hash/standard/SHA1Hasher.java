package com.github.jscancella.hash.standard;

/**
 * A default implementation for the SHA1 checksum
 */
public final class SHA1Hasher extends AbstractMessageDigestHasher {
  /**
   * the bagit algorithm name
   */
  public static final String BAGIT_ALGORITHM_NAME = "sha1";

  /**
   * A default implementation for the SHA1 checksum
   */
  public SHA1Hasher() {
    super("SHA-1", BAGIT_ALGORITHM_NAME);
  }

}
