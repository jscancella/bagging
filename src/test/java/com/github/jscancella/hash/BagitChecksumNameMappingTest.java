package com.github.jscancella.hash;

import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.jscancella.exceptions.HasherInitializationException;

public class BagitChecksumNameMappingTest {

  @Test
  void addShouldReturnFalseGivenNotValidAlgorithm() {
    BagitChecksumNameMapping.add("not-valid", NonValidHasher.class);
    
    Assertions.assertThrows(RuntimeException.class, () -> {
      BagitChecksumNameMapping.get("not-valid");
    });
  }
}

class NonValidHasher implements Hasher{
  @Override
  public String hash(Path path) throws IOException {
    return "not-valid";
  }

  @Override
  public void initialize() throws HasherInitializationException {
    throw new HasherInitializationException(new NoSuchAlgorithmException("not-valid"));
  }

  @Override
  public void update(byte[] bytes) {

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
}
