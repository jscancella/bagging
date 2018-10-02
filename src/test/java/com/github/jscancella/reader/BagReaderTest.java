package com.github.jscancella.reader;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.jscancella.domain.Bag;
import com.github.jscancella.domain.FetchItem;
import com.github.jscancella.domain.Manifest;
import com.github.jscancella.domain.Metadata;
import com.github.jscancella.domain.Version;

public class BagReaderTest {
  
  @Test
  public void testReadBagWithinABag() throws Exception{
    Path rootDir = Paths.get(getClass().getClassLoader().getResource("bags/v0_96/bag-in-a-bag").toURI());
    Bag bag = BagReader.read(rootDir);
    Assertions.assertNotNull(bag);
  }
  
  @Test
  public void testReadBagWithEncodedNames() throws Exception{
    Path rootDir = Paths.get(getClass().getClassLoader().getResource("bags/v0_96/bag-with-encoded-names").toURI());
    Bag bag = BagReader.read(rootDir);
    Assertions.assertNotNull(bag);
    
    Path[] payloadFiles = new Path[]{rootDir.resolve("data/%7Edir2/test4.txt"),
        rootDir.resolve("data/%7Edir2/dir3/test5.txt"),
        rootDir.resolve("data/dir1/~test3.txt"),
        rootDir.resolve("data/%7Etest1.txt"),
        rootDir.resolve("data/%test2.txt")};
    Manifest payloadManifest = (Manifest) bag.getPayLoadManifests().toArray()[0];
    for(Path payloadFile : payloadFiles){
      Assertions.assertTrue(payloadManifest.getFileToChecksumMap().containsKey(payloadFile), payloadFile + " should be in the manifest, but it isn't!");
    }
  }
  
  @Test
  public void testReadBagWithEscapableCharacter() throws Exception{
    Path rootDir = Paths.get(getClass().getClassLoader().getResource("bags/v0_96/bag-with-escapable-characters").toURI());
    Bag bag = BagReader.read(rootDir);
    Assertions.assertNotNull(bag);
    
    Path[] payloadFiles = new Path[]{rootDir.resolve("data/dir1/test3.txt"),
        rootDir.resolve("data/dir2/dir3/test5.txt"),
        rootDir.resolve("data/test file with spaces.txt"),
        rootDir.resolve("data/test1.txt"),
        rootDir.resolve("data/test2.txt")};
    Manifest payloadManifest = (Manifest) bag.getPayLoadManifests().toArray()[0];
    for(Path payloadFile : payloadFiles){
      Assertions.assertTrue(payloadManifest.getFileToChecksumMap().containsKey(payloadFile), payloadFile + " should be in the manifest, but it isn't!");
    }
  }
  
  @Test
  public void testReadBagWithDotSlash() throws Exception{
    Path rootDir = Paths.get(getClass().getClassLoader().getResource("bags/v0_96/bag-with-leading-dot-slash-in-manifest").toURI());
    Bag bag = BagReader.read(rootDir);
    Assertions.assertNotNull(bag);
    
    Path[] payloadFiles = new Path[]{rootDir.resolve("data/dir1/test3.txt"),
        rootDir.resolve("data/dir2/dir3/test5.txt"),
        rootDir.resolve("data/dir2/test4.txt"),
        rootDir.resolve("data/test1.txt"),
        rootDir.resolve("data/test2.txt")};
    Manifest payloadManifest = (Manifest) bag.getPayLoadManifests().toArray()[0];
    for(Path payloadFile : payloadFiles){
      Assertions.assertTrue(payloadManifest.getFileToChecksumMap().containsKey(payloadFile), payloadFile + " should be in the manifest, but it isn't!");
    }
  }
  
  @Test
  public void testReadBagWithSpaceAsManifestDelimiter() throws Exception{
    Path rootDir = Paths.get(getClass().getClassLoader().getResource("bags/v0_96/bag-with-space").toURI());
    Bag bag = BagReader.read(rootDir);
    Assertions.assertNotNull(bag);
    
    Path[] payloadFiles = new Path[]{rootDir.resolve("data/dir1/test3.txt"),
        rootDir.resolve("data/dir2/dir3/test5.txt"),
        rootDir.resolve("data/dir2/test4.txt"),
        rootDir.resolve("data/test 1.txt"),
        rootDir.resolve("data/test2.txt")};
    Manifest payloadManifest = (Manifest) bag.getPayLoadManifests().toArray()[0];
    for(Path payloadFile : payloadFiles){
      Assertions.assertTrue(payloadManifest.getFileToChecksumMap().containsKey(payloadFile), payloadFile + " should be in the manifest, but it isn't!");
    }
  }
  
  @Test
  public void testReadVersion0_93() throws Exception{
    Path rootDir = Paths.get(getClass().getClassLoader().getResource("bags/v0_93/bag").toURI());
    Bag bag = BagReader.read(rootDir);
    Assertions.assertEquals(new Version(0, 93), bag.getVersion());
    
    Path[] payloadFiles = new Path[]{rootDir.resolve("data/dir1/test3.txt"),
        rootDir.resolve("data/dir2/dir3/test5.txt"),
        rootDir.resolve("data/dir2/test4.txt"),
        rootDir.resolve("data/test1.txt"),
        rootDir.resolve("data/test2.txt")};
    Manifest payloadManifest = (Manifest) bag.getPayLoadManifests().toArray()[0];
    for(Path payloadFile : payloadFiles){
      Assertions.assertTrue(payloadManifest.getFileToChecksumMap().containsKey(payloadFile), payloadFile + " should be in the manifest, but it isn't!");
    }
    
    Assertions.assertEquals("25.5", bag.getMetadata().get("Payload-Oxum").get(0));
  }
  
  @Test
  public void testReadVersion0_94() throws Exception{
    Path rootDir = Paths.get(getClass().getClassLoader().getResource("bags/v0_94/bag").toURI());
    Bag bag = BagReader.read(rootDir);
    Assertions.assertEquals(new Version(0, 94), bag.getVersion());

    Path[] payloadFiles = new Path[]{rootDir.resolve("data/dir1/test3.txt"),
        rootDir.resolve("data/dir2/dir3/test5.txt"),
        rootDir.resolve("data/dir2/test4.txt"),
        rootDir.resolve("data/test1.txt"),
        rootDir.resolve("data/test2.txt")};
    Manifest payloadManifest = (Manifest) bag.getPayLoadManifests().toArray()[0];
    for(Path payloadFile : payloadFiles){
      Assertions.assertTrue(payloadManifest.getFileToChecksumMap().containsKey(payloadFile), payloadFile + " should be in the manifest, but it isn't!");
    }
    
    Assertions.assertEquals("25.5", bag.getMetadata().get("Payload-Oxum").get(0));
  }
  
  @Test
  public void testReadVersion0_95() throws Exception{
    Path rootDir = Paths.get(getClass().getClassLoader().getResource("bags/v0_95/bag").toURI());
    Bag bag = BagReader.read(rootDir);
    Assertions.assertEquals(new Version(0, 95), bag.getVersion());
    
    Path[] payloadFiles = new Path[]{rootDir.resolve("data/dir1/test3.txt"),
        rootDir.resolve("data/dir2/dir3/test5.txt"),
        rootDir.resolve("data/dir2/test4.txt"),
        rootDir.resolve("data/test1.txt"),
        rootDir.resolve("data/test2.txt")};
    Manifest payloadManifest = (Manifest) bag.getPayLoadManifests().toArray()[0];
    for(Path payloadFile : payloadFiles){
      Assertions.assertTrue(payloadManifest.getFileToChecksumMap().containsKey(payloadFile), payloadFile + " should be in the manifest, but it isn't!");
    }
    
    Assertions.assertEquals("260 GB", bag.getMetadata().get("Package-Size").get(0));
  }

  @Test
  public void testReadISO_8859_1Encoding() throws Exception{
    Metadata expectedMetaData = new Metadata();
    expectedMetaData.add("Bag-Software-Agent","bagit.py <http://github.com/libraryofcongress/bagit-python>");
    expectedMetaData.add("Bagging-Date","2016-02-26");
    expectedMetaData.add("Contact-Email","cadams@loc.gov");
    expectedMetaData.add("Contact-Name","Chris Adams");
    expectedMetaData.add("Payload-Oxum","58.2");
    
    Path bagPath = Paths.get(new File("src/test/resources/ISO-8859-1-encodedBag").toURI());
    Bag bag = BagReader.read(bagPath);
    
    Assertions.assertNotNull(bag);
    Assertions.assertEquals(StandardCharsets.ISO_8859_1, bag.getFileEncoding());
    Assertions.assertEquals(expectedMetaData, bag.getMetadata());
    
    Path[] payloadFiles = new Path[]{bagPath.resolve("data/bare-filename"), bagPath.resolve("data/text-file.txt")};
    Manifest payloadManifest = (Manifest) bag.getPayLoadManifests().toArray()[0];
    for(Path payloadFile : payloadFiles){
      Assertions.assertTrue(payloadManifest.getFileToChecksumMap().containsKey(payloadFile), payloadFile + " should be in the manifest, but it isn't!");
    }
  }
  
  @Test
  public void testReadUTF_16_Encoding() throws Exception{
    Metadata expectedMetaData = new Metadata();
    expectedMetaData.add("Bag-Software-Agent","bagit.py <http://github.com/libraryofcongress/bagit-python>");
    expectedMetaData.add("Bagging-Date","2016-02-26");
    expectedMetaData.add("Contact-Email","cadams@loc.gov");
    expectedMetaData.add("Contact-Name","Chris Adams");
    expectedMetaData.add("Payload-Oxum","58.2");
    
    Path bagPath = Paths.get(new File("src/test/resources/UTF-16-encoded-tag-files").toURI());
    
    List<FetchItem> expectedFetchItems = new ArrayList<>();
    expectedFetchItems.add(new FetchItem(new URL("http://localhost/foo/data/dir1/test3.txt"), -1l, bagPath.resolve("data/dir1/test3.txt")));
    
    Bag bag = BagReader.read(bagPath);
    Assertions.assertNotNull(bag);
    Assertions.assertEquals(StandardCharsets.UTF_16, bag.getFileEncoding());
    Assertions.assertEquals(expectedMetaData, bag.getMetadata());
    Assertions.assertEquals(expectedFetchItems, bag.getItemsToFetch());
  }
  
  @Test
  public void testReadVersion0_97Bag() throws Exception{
    Path rootBag = Paths.get(new File("src/test/resources/bags/v0_97/bag").toURI());
    Path[] payloadFiles = new Path[]{rootBag.resolve("data/dir1/test3.txt"), rootBag.resolve("data/dir2/dir3/test5.txt"), 
        rootBag.resolve("data/dir2/test4.txt"), rootBag.resolve("data/test1.txt"), rootBag.resolve("data/test2.txt")};
    
    Bag returnedBag = BagReader.read(rootBag);
    
    Assertions.assertNotNull(returnedBag);
    Assertions.assertEquals(new Version(0, 97), returnedBag.getVersion());
    Manifest payloadManifest = (Manifest) returnedBag.getPayLoadManifests().toArray()[0];
    for(Path payloadFile : payloadFiles){
      Assertions.assertTrue(payloadManifest.getFileToChecksumMap().containsKey(payloadFile));
    }
  }
  
  @Test
  public void testReadVersion1_0Bag() throws Exception{
    Path rootBag = Paths.get(new File("src/test/resources/bags/v1_0/bag").toURI());
    Path[] payloadFiles = new Path[]{rootBag.resolve("data/foo.txt")};
    
    Bag returnedBag = BagReader.read(rootBag);
    
    Assertions.assertNotNull(returnedBag);
    Assertions.assertEquals(new Version(1, 0), returnedBag.getVersion());
    
    Manifest payloadManifest = (Manifest) returnedBag.getPayLoadManifests().toArray()[0];
    for(Path payloadFile : payloadFiles){
      Assertions.assertTrue(payloadManifest.getFileToChecksumMap().containsKey(payloadFile));
    }
    
    Assertions.assertEquals("2018-07-22", returnedBag.getMetadata().get("Bagging-Date").get(0));
    Assertions.assertEquals("bagit.py v1.7.0 <https://github.com/LibraryOfCongress/bagit-python>",
        returnedBag.getMetadata().get("Bag-Software-Agent").get(0));
  }
}
