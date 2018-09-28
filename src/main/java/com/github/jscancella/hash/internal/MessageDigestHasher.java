package com.github.jscancella.hash.internal;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.util.Formatter;

/**
 * Shared utilities for Hashers that use {@link MessageDigest}
 */
public enum MessageDigestHasher {; //using enum to enforce singleton
  private static final int _64_KB = 1024 * 64;
  private static final int CHUNK_SIZE = _64_KB;

  public static void updateMessageDigest(final Path path, final MessageDigest messageDigest) throws IOException{
    try(final InputStream is = new BufferedInputStream(Files.newInputStream(path, StandardOpenOption.READ))){
      final byte[] buffer = new byte[CHUNK_SIZE];
      int read = is.read(buffer);

      while(read != -1){
        messageDigest.update(buffer, 0, read);
        read = is.read(buffer);
      }
    }
  }
  
  public static String formatMessageDigest(final MessageDigest messageDigest){
    try(final Formatter formatter = new Formatter()){
      for (final byte b : messageDigest.digest()) {
        formatter.format("%02x", b);
      }
      
      return formatter.toString();
    }
  }
}
