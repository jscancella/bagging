package com.github.jscancella.hash;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PayloadOxumGeneratorTest {

  @Test
  public void testGeneratePayloadOxum() throws IOException {
    String expected = "6.1"; //6 bytes and 1 file
    
    Path dataDir = Paths.get("src", "test", "resources", "bags", "v1_0", "bag", "data");
    String actual = PayloadOxumGenerator.generatePayloadOxum(dataDir);
    
    Assertions.assertEquals(expected, actual);
  }
}
