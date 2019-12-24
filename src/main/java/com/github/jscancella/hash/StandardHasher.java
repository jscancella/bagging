package com.github.jscancella.hash;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * Constant definitions for the standard {@link Hasher}. Pretty much every computer will be able to implement these.
 */
public enum StandardHasher implements Hasher {
  MD5("MD5", "md5"),
  SHA1("SHA-1", "sha1"),
  SHA224("SHA-224", "sha224"),
  SHA256("SHA-256", "sha256"),
  SHA384("SHA-384", "sha384"),
  SHA512("SHA-512", "sha512");
  
  private static final int _64_KB = 1024 * 64;
  private static final int CHUNK_SIZE = _64_KB;
  private MessageDigest messageDigestInstance;
  private final String MESSAGE_DIGEST_NAME;
  private final String BAGIT_ALGORITHM_NAME;
  
  StandardHasher(final String digestName, final String bagitAlgorithmName) {
    MESSAGE_DIGEST_NAME = digestName;
    BAGIT_ALGORITHM_NAME = bagitAlgorithmName;
  }
  
  @Override
  public String hash(final Path path) throws IOException{
    reset();
    updateMessageDigest(path, messageDigestInstance);
    return formatMessageDigest(messageDigestInstance);
  }

  @Override
  public void update(final byte[] bytes, final int length){
    messageDigestInstance.update(bytes, 0, length);
  }

  @Override
  public String getHash(){
    return formatMessageDigest(messageDigestInstance);
  }

  @Override
  public void reset(){
    messageDigestInstance.reset();
  }

  @Override
  public String getBagitAlgorithmName(){
    return BAGIT_ALGORITHM_NAME;
  }
  
  private static void updateMessageDigest(final Path path, final MessageDigest messageDigest) throws IOException{
    try(InputStream is = new BufferedInputStream(Files.newInputStream(path, StandardOpenOption.READ))){
      final byte[] buffer = new byte[CHUNK_SIZE];
      int read = is.read(buffer);

      while(read != -1){
        messageDigest.update(buffer, 0, read);
        read = is.read(buffer);
      }
    }
  }
  
  private static String formatMessageDigest(final MessageDigest messageDigest){
    try(Formatter formatter = new Formatter()){
      for (final byte b : messageDigest.digest()) {
        formatter.format("%02x", b);
      }
      
      return formatter.toString();
    }
  }

  @Override
  public void initialize() throws NoSuchAlgorithmException{
    messageDigestInstance = MessageDigest.getInstance(MESSAGE_DIGEST_NAME);
  }

}
