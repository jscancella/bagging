package com.github.jscancella.hash.standard;

/**
 * A default implementation for the SHA1 with 256 bit checksum
 */
public final class SHA256Hasher extends AbstractMessageDigestHasher {
  /**
   * the bagit algorithm name
   */
  public static final String BAGIT_ALGORITHM_NAME = "sha256";

  /**
   * A default implementation for the SHA1 with 256 bit checksum
   */
  public SHA256Hasher() {
    super("SHA-256", BAGIT_ALGORITHM_NAME);
  }

}
