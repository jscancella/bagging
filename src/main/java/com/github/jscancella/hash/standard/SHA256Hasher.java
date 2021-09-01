package com.github.jscancella.hash.standard;

public final class SHA256Hasher extends AbstractMessageDigestHasher {
  public static final String BAGIT_ALGORITHM_NAME = "sha256";

  public SHA256Hasher() {
    super("SHA-256", BAGIT_ALGORITHM_NAME);
  }

}
