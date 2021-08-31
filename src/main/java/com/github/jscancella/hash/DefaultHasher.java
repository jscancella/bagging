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
 * Default {@link Hasher} implementation that computes digests using Java's {@link MessageDigest}.
 */
class DefaultHasher implements Hasher {

  private static final int _64_KB = 1024 * 64;
  private static final int CHUNK_SIZE = _64_KB;
  private transient MessageDigest messageDigestInstance;
  private final transient String MESSAGE_DIGEST_NAME;
  private final transient String BAGIT_ALGORITHM_NAME;

  /* default */ DefaultHasher(final String digestName, final String bagitAlgorithmName) {
      MESSAGE_DIGEST_NAME = digestName;
      BAGIT_ALGORITHM_NAME = bagitAlgorithmName;
  }

  @Override
  public String hash(final Path path) throws IOException {
      reset();
      updateMessageDigest(path, messageDigestInstance);
      return formatMessageDigest(messageDigestInstance);
  }

  private static void updateMessageDigest(final Path path, final MessageDigest messageDigest) throws IOException{
      try(InputStream inputStream = new BufferedInputStream(Files.newInputStream(path, StandardOpenOption.READ))){
          final byte[] buffer = new byte[CHUNK_SIZE];
          int read = inputStream.read(buffer);

          while(read != -1){
              messageDigest.update(buffer, 0, read);
              read = inputStream.read(buffer);
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
  public String getHash(){
      return formatMessageDigest(messageDigestInstance);
  }

  @Override
  public void update(final byte[] bytes){
      messageDigestInstance.update(bytes);
  }

  @Override
  public void reset(){
      messageDigestInstance.reset();
  }

  @Override
  public String getBagitAlgorithmName(){
      return BAGIT_ALGORITHM_NAME;
  }

  @Override
  public void initialize() throws NoSuchAlgorithmException {
      messageDigestInstance = MessageDigest.getInstance(MESSAGE_DIGEST_NAME);
  }

}
