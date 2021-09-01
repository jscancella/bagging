package com.github.jscancella.hash.standard;

public final class MD5Hasher extends AbstractMessageDigestHasher{
  public static final String BAGIT_ALGORITHM_NAME = "md5";

  public MD5Hasher() {
    super("MD5", BAGIT_ALGORITHM_NAME);
  }

}
