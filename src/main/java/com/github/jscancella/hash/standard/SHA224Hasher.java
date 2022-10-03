package com.github.jscancella.hash.standard;

/**
 * A default implementation for the SHA1 with 224 bit checksum
 */
public final class SHA224Hasher extends AbstractMessageDigestHasher {
  /**
   * the bagit algorithm name
   */
  public static final String BAGIT_ALGORITHM_NAME = "sha224";

  /**
   * A default implementation for the SHA1 with 224 bit checksum
   */
  public SHA224Hasher() {
    super("SHA-224", BAGIT_ALGORITHM_NAME);
  }

}
