package com.github.jscancella.hash.standard;
/**
 * A default implementation for the SHA1 with 512 bit checksum
 */
public final class SHA512Hasher extends AbstractMessageDigestHasher {
  /**
   * the bagit algorithm name
   */
  public static final String BAGIT_ALGORITHM_NAME = "sha512";

  /**
   * A default implementation for the SHA1 with 512 bit checksum
   */
  public SHA512Hasher() {
    super("SHA-512", BAGIT_ALGORITHM_NAME);
  }

}
