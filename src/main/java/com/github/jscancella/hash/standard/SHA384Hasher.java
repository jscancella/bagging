package com.github.jscancella.hash.standard;

/**
 * A default implementation for the SHA1 with 384 bit checksum
 */
public final class SHA384Hasher extends AbstractMessageDigestHasher {
  /**
   * the bagit algorithm name
   */
  public static final String BAGIT_ALGORITHM_NAME = "sha384";

  /**
   * A default implementation for the SHA1 with 384 bit checksum
   */
  public SHA384Hasher() {
    super("SHA-384", BAGIT_ALGORITHM_NAME);
  }

}
