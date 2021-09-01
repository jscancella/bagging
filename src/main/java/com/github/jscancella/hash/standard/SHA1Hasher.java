package com.github.jscancella.hash.standard;

public final class SHA1Hasher extends AbstractMessageDigestHasher {

  public static final String BAGIT_ALGORITHM_NAME = "sha1";

  public SHA1Hasher() {
    super("SHA-1", BAGIT_ALGORITHM_NAME);
  }

}
