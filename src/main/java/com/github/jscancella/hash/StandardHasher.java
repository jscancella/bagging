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
  
  private StandardHasher(String digestName, final String bagitAlgorithmName) {
    MESSAGE_DIGEST_NAME = digestName;
    BAGIT_ALGORITHM_NAME = bagitAlgorithmName;
  }
  
  @Override
  public String hash(Path path) throws IOException, NoSuchAlgorithmException{
    final MessageDigest messageDigest = MessageDigest.getInstance(MESSAGE_DIGEST_NAME);
    updateMessageDigest(path, messageDigest);
    return formatMessageDigest(messageDigest);
  }

  @Override
  public void update(byte[] bytes, final int length) throws NoSuchAlgorithmException{
    if(messageDigestInstance == null) {
      messageDigestInstance = MessageDigest.getInstance(MESSAGE_DIGEST_NAME);
    }
    messageDigestInstance.update(bytes, 0, length);
  }

  @Override
  public String getHash() throws NoSuchAlgorithmException{
    if(messageDigestInstance == null) {
      messageDigestInstance = MessageDigest.getInstance(MESSAGE_DIGEST_NAME);
    }
    return formatMessageDigest(messageDigestInstance);
  }

  @Override
  public void reset(){
    messageDigestInstance = null;
  }

  @Override
  public String getBagitAlgorithmName(){
    return BAGIT_ALGORITHM_NAME;
  }
  
  private static void updateMessageDigest(final Path path, final MessageDigest messageDigest) throws IOException{
    try(final InputStream is = new BufferedInputStream(Files.newInputStream(path, StandardOpenOption.READ))){
      final byte[] buffer = new byte[CHUNK_SIZE];
      int read = is.read(buffer);

      while(read != -1){
        messageDigest.update(buffer, 0, read);
        read = is.read(buffer);
      }
    }
  }
  
  private static String formatMessageDigest(final MessageDigest messageDigest){
    try(final Formatter formatter = new Formatter()){
      for (final byte b : messageDigest.digest()) {
        formatter.format("%02x", b);
      }
      
      return formatter.toString();
    }
  }

}
