package com.github.jscancella.hash;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

public class BagitChecksumNameMappingTest {

  @Test
  void addShouldReturnFalseGivenNotValidAlgorithm() {
    boolean result = BagitChecksumNameMapping.add("not-valid", new Hasher() {
      @Override
      public String hash(Path path) throws IOException {
        return "not-valid";
      }

      @Override
      public void initialize() throws NoSuchAlgorithmException {
        throw new NoSuchAlgorithmException("not-valid");
      }

      @Override
      public void update(byte[] bytes, int length) {

      }

      @Override
      public String getHash() {
        return "not-valid";
      }

      @Override
      public void reset() {

      }

      @Override
      public String getBagitAlgorithmName() {
        return "not-valid";
      }
    });
    Assertions.assertFalse(result);
  }
}
