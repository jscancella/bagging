package com.github.jscancella.verify.internal;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.jscancella.TempFolderTest;
import com.github.jscancella.domain.Bag;
import com.github.jscancella.exceptions.InvalidPayloadOxumException;
import com.github.jscancella.exceptions.PayloadOxumDoesNotExistException;
import com.github.jscancella.reader.BagReader;
import com.github.jscancella.verify.internal.QuickVerifier;

public class QuickVerifierTest extends TempFolderTest {

  private Path rootDir = Paths.get(new File("src/test/resources/bags/v0_97/bag").toURI());
  
  @Test 
  @SuppressWarnings("deprecation")
  public void testQuickVerify() throws Exception{
    Path passingRootDir = Paths.get(new File("src/test/resources/bags/v0_94/bag").toURI());
    Bag bag = BagReader.read(passingRootDir);
    
    QuickVerifier.quicklyVerify(bag);
  }
  
  @Test
  @SuppressWarnings("deprecation")
  public void testExceptionIsThrownWhenPayloadOxumDoesntExist() throws Exception{
    Bag bag = BagReader.read(rootDir);
    Assertions.assertThrows(PayloadOxumDoesNotExistException.class, 
        () -> { QuickVerifier.quicklyVerify(bag); });
  }
  
  @Test
  @SuppressWarnings("deprecation")
  public void testInvalidByteSizeForQuickVerify() throws Exception{
    Path badRootDir = Paths.get(new File("src/test/resources/badPayloadOxumByteSize/bag").toURI());
    Bag bag = BagReader.read(badRootDir);
    
    Assertions.assertThrows(InvalidPayloadOxumException.class, 
        () -> { QuickVerifier.quicklyVerify(bag); });
  }
  
  @Test
  @SuppressWarnings("deprecation")
  public void testInvalidFileCountForQuickVerify() throws Exception{
    Path badRootDir = Paths.get(new File("src/test/resources/badPayloadOxumFileCount/bag").toURI());
    Bag bag = BagReader.read(badRootDir);
    
    Assertions.assertThrows(InvalidPayloadOxumException.class, 
        () -> { QuickVerifier.quicklyVerify(bag); });
  }
}
