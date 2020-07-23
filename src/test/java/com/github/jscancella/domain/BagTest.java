package com.github.jscancella.domain;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.jscancella.TempFolderTest;
import com.github.jscancella.hash.StandardHasher;

public class BagTest extends TempFolderTest{

  @Test
  public void testReadObjectsAreEqual() throws Exception {
    Path rootDir = Paths.get("src", "test", "resources", "md5Bag");
    Bag bag = Bag.read(rootDir);
    Bag sameBag = Bag.read(rootDir);
    
    Assertions.assertEquals(bag, sameBag);
  }
  
  @Test
  public void testLogicallySameObjectsAreEqual() throws Exception {
    Path file = Paths.get("src", "test", "resources", "md5Bag", "bagit.txt");
    Path folder = createDirectory("tempBag");
    
    Bag bag = new BagBuilder().addAlgorithm("md5")
      .addMetadata("foo", "bar")
      .addPayloadFile(file)
      .bagLocation(folder)
      .write();
    
    Bag sameBag = new BagBuilder().addAlgorithm("md5")
        .addMetadata("foo", "bar")
        .addPayloadFile(file)
        .bagLocation(folder)
        .write();
    
    Assertions.assertEquals(bag, sameBag);
  }
  
  @Test
  public void testBagCreation() throws Exception{
    Path rootDir = createDirectory("builtBag");
    Path payloadFile = Paths.get("src", "test", "resources", "bags", "v1_0", "bag", "data", "foo.txt");
    Path tagFile = payloadFile;
    FetchItem fetchItem = new FetchItem(new URI("https://hackaday.com"), 0l, Paths.get(""));
    
    BagBuilder builder = new BagBuilder();
    builder.addAlgorithm("md5")
      .addMetadata("foo", "bar")
      .addPayloadFile(payloadFile)
      .addTagFile(tagFile)
      .addItemToFetch(fetchItem)
      .bagLocation(rootDir)
      .write();

    Path expectedBagitFile = rootDir.resolve("bagit.txt");
    Path expectedMetadataFile = rootDir.resolve("bag-info.txt");
    Path expectedTagmanifestFile = rootDir.resolve("tagmanifest-md5.txt");
    Path expectedManifestFile = rootDir.resolve("manifest-md5.txt");
    Path expectedTagFile = rootDir.resolve("foo.txt"); 
    Path expectedFetchFile = rootDir.resolve("fetch.txt");
    
    Assertions.assertTrue(Files.exists(expectedBagitFile));
    String bagitFileHash = StandardHasher.MD5.hash(expectedBagitFile);
    Assertions.assertEquals(
        Arrays.asList("BagIt-Version: 1.0","Tag-File-Character-Encoding: UTF-8"), Files.readAllLines(expectedBagitFile));
    
    Assertions.assertTrue(Files.exists(expectedMetadataFile));
    String metadataFileHash = StandardHasher.MD5.hash(expectedMetadataFile);
    Assertions.assertEquals(
        Arrays.asList("foo: bar"), Files.readAllLines(expectedMetadataFile));
    
    Assertions.assertTrue(Files.exists(expectedManifestFile));
    String manifestFileHash = StandardHasher.MD5.hash(expectedManifestFile);
    Assertions.assertEquals(
        Arrays.asList("b1946ac92492d2347c6235b4d2611184  data/foo.txt"), Files.readAllLines(expectedManifestFile));
    
    Assertions.assertTrue(Files.exists(expectedFetchFile));
    String fetchFileHash = StandardHasher.MD5.hash(expectedFetchFile);
    
    Assertions.assertTrue(Files.exists(expectedTagmanifestFile));
    Assertions.assertEquals(
        Arrays.asList("b1946ac92492d2347c6235b4d2611184  foo.txt",
        		      bagitFileHash + "  bagit.txt",
        		      metadataFileHash + "  bag-info.txt",
        		      fetchFileHash + "  fetch.txt",
        		      manifestFileHash + "  manifest-md5.txt"), 
        Files.readAllLines(expectedTagmanifestFile));
    
    Assertions.assertTrue(Files.exists(expectedTagFile));
    Assertions.assertArrayEquals(Files.readAllBytes(tagFile), Files.readAllBytes(expectedTagFile));
    
    
  }
}
