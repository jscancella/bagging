package com.github.jscancella.hash;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StandardHasherTest {
  
  @Test
  public void testResetBeforeHashingFile() throws Exception{
    Path testFile = Paths.get("src", "test", "resources", "md5Bag", "bagit.txt");
    Hasher hasher = BagitChecksumNameMapping.get("md5");
    
    String originalHash = hasher.hash(testFile);
    
    hasher.update("Some data that needs to be cleared from the hasher".getBytes());
    
    Assertions.assertEquals(originalHash, hasher.hash(testFile));
  }

  @Test
  public void testResetClearsState() throws Exception{
    Path testFile = Paths.get("src", "test", "resources", "md5Bag", "bagit.txt");
    Hasher hasher = BagitChecksumNameMapping.get("md5");
    hasher.update(Files.readAllBytes(testFile));
    String originalHash = hasher.getHash();
    
    hasher.update("Some data that needs to be cleared from the hasher".getBytes());
    
    hasher.reset(); //clear the state
    
    hasher.update(Files.readAllBytes(testFile));
    
    Assertions.assertEquals(originalHash, hasher.getHash());
  }
  
  @Test
  public void testHashDontMatchWithoutReset() throws Exception{
    Hasher hasher = BagitChecksumNameMapping.get("md5");
    
    Path testFile = Paths.get("src", "test", "resources", "md5Bag", "bagit.txt");
    hasher.update(Files.readAllBytes(testFile));
    String originalHash = hasher.getHash();
    
    hasher.update("some trash bytes that should have been removed".getBytes());
    
    hasher.update(Files.readAllBytes(testFile));
    
    Assertions.assertNotEquals(originalHash, hasher.getHash());
  }
  
  @Test
  public void testHashProducesSameOverMultipleRuns() throws Exception{
    Hasher hasher = BagitChecksumNameMapping.get("md5");
    Path testFile = Paths.get("src", "test", "resources", "md5Bag", "bagit.txt");
    
    Assertions.assertEquals(hasher.hash(testFile), hasher.hash(testFile));
  }
  
  @Test
  public void testHashProducesCorrectOutput() throws Exception{
    Hasher hasher = BagitChecksumNameMapping.get("md5");
    Path testFile = Paths.get("src", "test", "resources", "md5Bag", "bagit.txt");
    
    Assertions.assertEquals("9e5ad981e0d29adc278f6a294b8c2aca", hasher.hash(testFile));
  }
}
