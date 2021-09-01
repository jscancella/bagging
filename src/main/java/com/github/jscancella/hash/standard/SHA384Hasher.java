package com.github.jscancella.hash.standard;

public final class SHA384Hasher extends AbstractMessageDigestHasher {
  public static final String BAGIT_ALGORITHM_NAME = "sha384";

  public SHA384Hasher() {
    super("SHA-384", BAGIT_ALGORITHM_NAME);
  }

}
