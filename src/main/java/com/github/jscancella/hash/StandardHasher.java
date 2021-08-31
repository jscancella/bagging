package com.github.jscancella.hash;

/**
 * Constant definitions for the standard {@link Hasher}. Pretty much every computer will be able to implement these.
 */
@SuppressWarnings({"PMD.AvoidMessageDigestField"})
public enum StandardHasher implements HasherFactory {
  /**
   * The md5 checksum algorithm
   */
  MD5("MD5", "md5"),
  /**
   * The sha-1 checksum algorithm
   */
  SHA1("SHA-1", "sha1"),
  /**
   * The sha-2 checksum algorithm using 224 bits
   */
  SHA224("SHA-224", "sha224"),
  /**
   * The sha-2 checksum algorithm using 256 bits
   */
  SHA256("SHA-256", "sha256"),
  /**
   * The sha-2 checksum algorithm using 384 bits
   */
  SHA384("SHA-384", "sha384"),
  /**
   * The sha-2 checksum algorithm using 512 bits
   */
  SHA512("SHA-512", "sha512");

  private final String MESSAGE_DIGEST_NAME;
  private final String BAGIT_ALGORITHM_NAME;

  StandardHasher(final String digestName, final String bagitAlgorithmName) {
    this.MESSAGE_DIGEST_NAME = digestName;
    this.BAGIT_ALGORITHM_NAME = bagitAlgorithmName;
  }

  @Override
  public Hasher createHasher() {
    return new DefaultHasher(MESSAGE_DIGEST_NAME, BAGIT_ALGORITHM_NAME);
  }

  @Override
  public String getBagitAlgorithmName(){
    return BAGIT_ALGORITHM_NAME;
  }

}
