package com.github.jscancella.hash.standard;

public final class SHA224Hasher extends AbstractMessageDigestHasher {
  public static final String BAGIT_ALGORITHM_NAME = "sha224";

  public SHA224Hasher() {
    super("SHA-224", BAGIT_ALGORITHM_NAME);
  }

}
