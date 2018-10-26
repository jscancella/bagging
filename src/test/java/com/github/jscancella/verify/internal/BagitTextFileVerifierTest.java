package com.github.jscancella.verify.internal;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.jscancella.exceptions.InvalidBagitFileFormatException;

public class BagitTextFileVerifierTest {

  @Test
  public void testLinesMatchesStrict() throws Exception{
    List<String> lines = Arrays.asList("BagIt-Version: 1.0", "Tag-File-Character-Encoding: UTF-8");
    BagitTextFileVerifier.throwErrorIfLinesDoNotMatchStrict(lines);
  }
  
  @Test
  public void testFirstLineMatchesStrict() throws Exception{
    //should fail because it has spaces before the colon
    List<String> lines = Arrays.asList("BagIt-Version    : 1.0", "Tag-File-Character-Encoding: UTF-8");
    Assertions.assertThrows(InvalidBagitFileFormatException.class, 
        () -> { BagitTextFileVerifier.throwErrorIfLinesDoNotMatchStrict(lines); });
  }
  
  @Test
  public void testSecondLineMatchesStrict() throws Exception{
    //should fail because it has spaces before the colon
    List<String> lines = Arrays.asList("BagIt-Version: 1.0", "Tag-File-Character-Encoding      : UTF-8");
    Assertions.assertThrows(InvalidBagitFileFormatException.class, 
        () -> { BagitTextFileVerifier.throwErrorIfLinesDoNotMatchStrict(lines); });
  }
  
  @Test
  public void testMatchesStrictWithTooManyLines() throws Exception{
    //should fail because it has 3 lines
    List<String> lines = Arrays.asList("BagIt-Version: 1.0", "Tag-File-Character-Encoding: UTF-8", "");
    Assertions.assertThrows(InvalidBagitFileFormatException.class, 
        () -> { BagitTextFileVerifier.throwErrorIfLinesDoNotMatchStrict(lines); });
  }
}
