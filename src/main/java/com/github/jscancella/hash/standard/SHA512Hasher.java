package com.github.jscancella.hash.standard;

public final class SHA512Hasher extends AbstractMessageDigestHasher {
  public static final String BAGIT_ALGORITHM_NAME = "sha512";

  public SHA512Hasher() {
    super("SHA-512", BAGIT_ALGORITHM_NAME);
  }

}
