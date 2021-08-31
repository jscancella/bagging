package com.github.jscancella.verify;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import com.github.jscancella.hash.Hasher;
import com.github.jscancella.hash.HasherFactory;

public enum SHA3Hasher implements HasherFactory {
  INSTANCE;// using enum to enforce singleton
  
  private static final int _64_KB = 1024 * 64;
  private static final int CHUNK_SIZE = _64_KB;
  private static final String MESSAGE_DIGEST_NAME = "SHA3-256";

  @Override
  public Hasher createHasher() {
    return new InnerHasher();
  }

  @Override
  public String getBagitAlgorithmName() {
    return "sha3256";
  }

  static class InnerHasher implements Hasher {
    private MessageDigest messageDigestInstance;

    @Override
    public String hash(Path path) throws IOException {
      reset();
      updateMessageDigest(path, messageDigestInstance);
      return formatMessageDigest(messageDigestInstance);
    }

    @Override
    public void update(byte[] bytes) {
      messageDigestInstance.update(bytes, 0, bytes.length);
    }

    @Override
    public String getHash() {
      return formatMessageDigest(messageDigestInstance);
    }

    @Override
    public void reset() {
      messageDigestInstance.reset();
    }

    @Override
    public String getBagitAlgorithmName() {
      return "sha3256";
    }

    private static void updateMessageDigest(final Path path, final MessageDigest messageDigest) throws IOException {
      try (final InputStream is = new BufferedInputStream(Files.newInputStream(path, StandardOpenOption.READ))) {
        final byte[] buffer = new byte[CHUNK_SIZE];
        int read = is.read(buffer);

        while (read != -1) {
          messageDigest.update(buffer, 0, read);
          read = is.read(buffer);
        }
      }
    }

    private static String formatMessageDigest(final MessageDigest messageDigest) {
      try (final Formatter formatter = new Formatter()) {
        for (final byte b : messageDigest.digest()) {
          formatter.format("%02x", b);
        }

        return formatter.toString();
      }
    }

    @Override
    public void initialize() throws NoSuchAlgorithmException {
      messageDigestInstance = MessageDigest.getInstance(MESSAGE_DIGEST_NAME);
    }
  }
}
