package com.github.jscancella.writer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.jscancella.TempFolderTest;
import com.github.jscancella.domain.Bag;
import com.github.jscancella.reader.BagReader;

public class BagWriterTest extends TempFolderTest {

//  @Test
//  public void testGetCorrectRelativeOuputPath() throws Exception{
//    Path root = createDirectory("newFolder");
//    Bag bag = BagCreator.bagInPlace(root, Arrays.asList(StandardSupportedAlgorithms.MD5), false);
//    
//    Path testFile = root.resolve("data").resolve("fooFile.txt");
//    Files.createFile(testFile);
//    Manifest manifest = (Manifest) bag.getPayLoadManifests().toArray()[0];
//    manifest.getFileToChecksumMap().put(testFile, "CHECKSUM");
//    bag.getPayLoadManifests().add(manifest);
//    
//    Path newRoot = createDirectory("newRoot");
//    BagWriter.write(bag, newRoot);
//    Assertions.assertTrue(Files.exists(newRoot.resolve("data").resolve("fooFile.txt")));
//  }
  
  @Test
  public void testWriteVersion93() throws Exception{
    Path rootDir = Paths.get(getClass().getClassLoader().getResource("bags/v0_93/bag").toURI());
    Bag bag = BagReader.read(rootDir); 
    Path bagitDirPath = createDirectory("version93");
    List<Path> expectedPaths = Arrays.asList(bagitDirPath.resolve("tagmanifest-md5.txt"),
        bagitDirPath.resolve("manifest-md5.txt"),
        bagitDirPath.resolve("bagit.txt"),
        bagitDirPath.resolve("package-info.txt"),
        bagitDirPath.resolve("data"),
        bagitDirPath.resolve("data").resolve("test1.txt"),
        bagitDirPath.resolve("data").resolve("test2.txt"),
        bagitDirPath.resolve("data").resolve("dir1"),
        bagitDirPath.resolve("data").resolve("dir2"), 
        bagitDirPath.resolve("data").resolve("dir1").resolve("test3.txt"),
        bagitDirPath.resolve("data").resolve("dir2").resolve("test4.txt"),
        bagitDirPath.resolve("data").resolve("dir2").resolve("dir3"),
        bagitDirPath.resolve("data").resolve("dir2").resolve("dir3").resolve("test5.txt"));
    
    BagWriter.write(bag, bagitDirPath);
    for(Path expectedPath : expectedPaths){
      Assertions.assertTrue(Files.exists(expectedPath), "Expected " + expectedPath + " to exist!");
    }
  }
  
  @Test
  public void testWriteVersion94() throws Exception{
    Path rootDir = Paths.get(getClass().getClassLoader().getResource("bags/v0_94/bag").toURI());
    Bag bag = BagReader.read(rootDir); 
    Path bagitDirPath = createDirectory("version94");
    List<Path> expectedPaths = Arrays.asList(bagitDirPath.resolve("tagmanifest-md5.txt"),
        bagitDirPath.resolve("manifest-md5.txt"),
        bagitDirPath.resolve("bagit.txt"),
        bagitDirPath.resolve("package-info.txt"),
        bagitDirPath.resolve("data"),
        bagitDirPath.resolve("data").resolve("test1.txt"),
        bagitDirPath.resolve("data").resolve("test2.txt"),
        bagitDirPath.resolve("data").resolve("dir1"),
        bagitDirPath.resolve("data").resolve("dir2"), 
        bagitDirPath.resolve("data").resolve("dir1").resolve("test3.txt"),
        bagitDirPath.resolve("data").resolve("dir2").resolve("test4.txt"),
        bagitDirPath.resolve("data").resolve("dir2").resolve("dir3"),
        bagitDirPath.resolve("data").resolve("dir2").resolve("dir3").resolve("test5.txt"));
    
    BagWriter.write(bag, bagitDirPath);
    for(Path expectedPath : expectedPaths){
      Assertions.assertTrue(Files.exists(expectedPath), "Expected " + expectedPath + " to exist!");
    }
  }
  
  @Test
  public void testWriteVersion95() throws Exception{
    Path rootDir = Paths.get(getClass().getClassLoader().getResource("bags/v0_95/bag").toURI());
    Bag bag = BagReader.read(rootDir); 
    Path bagitDirPath = createDirectory("version95");
    List<Path> expectedPaths = Arrays.asList(bagitDirPath.resolve("tagmanifest-md5.txt"),
        bagitDirPath.resolve("manifest-md5.txt"),
        bagitDirPath.resolve("bagit.txt"),
        bagitDirPath.resolve("package-info.txt"),
        bagitDirPath.resolve("data"),
        bagitDirPath.resolve("data").resolve("test1.txt"),
        bagitDirPath.resolve("data").resolve("test2.txt"),
        bagitDirPath.resolve("data").resolve("dir1"),
        bagitDirPath.resolve("data").resolve("dir2"), 
        bagitDirPath.resolve("data").resolve("dir1").resolve("test3.txt"),
        bagitDirPath.resolve("data").resolve("dir2").resolve("test4.txt"),
        bagitDirPath.resolve("data").resolve("dir2").resolve("dir3"),
        bagitDirPath.resolve("data").resolve("dir2").resolve("dir3").resolve("test5.txt"));
    
    BagWriter.write(bag, bagitDirPath);
    for(Path expectedPath : expectedPaths){
      Assertions.assertTrue(Files.exists(expectedPath), "Expected " + expectedPath + " to exist!");
    }
  }
  
  @Test
  public void testWriteVersion96() throws Exception{
    Path rootDir = Paths.get(getClass().getClassLoader().getResource("bags/v0_96/bag").toURI());
    Bag bag = BagReader.read(rootDir); 
    Path bagitDirPath = createDirectory("version96");
    List<Path> expectedPaths = Arrays.asList(bagitDirPath.resolve("tagmanifest-md5.txt"),
        bagitDirPath.resolve("manifest-md5.txt"),
        bagitDirPath.resolve("bagit.txt"),
        bagitDirPath.resolve("bag-info.txt"),
        bagitDirPath.resolve("data"),
        bagitDirPath.resolve("data").resolve("test1.txt"),
        bagitDirPath.resolve("data").resolve("test2.txt"),
        bagitDirPath.resolve("data").resolve("dir1"),
        bagitDirPath.resolve("data").resolve("dir2"), 
        bagitDirPath.resolve("data").resolve("dir1").resolve("test3.txt"),
        bagitDirPath.resolve("data").resolve("dir2").resolve("test4.txt"),
        bagitDirPath.resolve("data").resolve("dir2").resolve("dir3"),
        bagitDirPath.resolve("data").resolve("dir2").resolve("dir3").resolve("test5.txt"));
    
    BagWriter.write(bag, bagitDirPath);
    for(Path expectedPath : expectedPaths){
      Assertions.assertTrue(Files.exists(expectedPath), "Expected " + expectedPath + " to exist!");
    }
  }
  
  @Test
  public void testWriteVersion97() throws Exception{
    Path rootDir = Paths.get(getClass().getClassLoader().getResource("bags/v0_97/bag").toURI());
    Bag bag = BagReader.read(rootDir); 
    Path bagitDirPath = createDirectory("version97");
    List<Path> expectedPaths = Arrays.asList(bagitDirPath.resolve("tagmanifest-md5.txt"),
        bagitDirPath.resolve("manifest-md5.txt"),
        bagitDirPath.resolve("bagit.txt"),
        bagitDirPath.resolve("bag-info.txt"),
        bagitDirPath.resolve("data"),
        bagitDirPath.resolve("data").resolve("test1.txt"),
        bagitDirPath.resolve("data").resolve("test2.txt"),
        bagitDirPath.resolve("data").resolve("dir1"),
        bagitDirPath.resolve("data").resolve("dir2"), 
        bagitDirPath.resolve("data").resolve("dir1").resolve("test3.txt"),
        bagitDirPath.resolve("data").resolve("dir2").resolve("test4.txt"),
        bagitDirPath.resolve("data").resolve("dir2").resolve("dir3"),
        bagitDirPath.resolve("data").resolve("dir2").resolve("dir3").resolve("test5.txt"),
        bagitDirPath.resolve("addl_tags"),
        bagitDirPath.resolve("addl_tags").resolve("tag1.txt"));
    
    BagWriter.write(bag, bagitDirPath);
    for(Path expectedPath : expectedPaths){
      Assertions.assertTrue(Files.exists(expectedPath), "Expected " + expectedPath + " to exist!");
    }
  }
  
  @Test
  public void testWriteVersion1_0() throws Exception{
    Path rootDir = Paths.get(getClass().getClassLoader().getResource("bags/v1_0/bag").toURI());
    Bag bag = BagReader.read(rootDir); 
    Path bagitDirPath = createDirectory("version1_0");
    List<Path> expectedPaths = Arrays.asList(bagitDirPath.resolve("manifest-sha512.txt"),
        bagitDirPath.resolve("tagmanifest-sha512.txt"),
        bagitDirPath.resolve("bagit.txt"),
        bagitDirPath.resolve("bag-info.txt"),
        bagitDirPath.resolve("data"),
        bagitDirPath.resolve("data").resolve("foo.txt"));
    
    BagWriter.write(bag, bagitDirPath);
    for(Path expectedPath : expectedPaths){
      Assertions.assertTrue(Files.exists(expectedPath), "Expected " + expectedPath + " to exist!");
    }
  }
  
  @Test
  public void testWriteHoley() throws Exception{
    Path rootDir = Paths.get(getClass().getClassLoader().getResource("bags/v0_96/holey-bag").toURI());
    Bag bag = BagReader.read(rootDir); 
    Path bagitDir = createDirectory("holyBag");
    
    BagWriter.write(bag, bagitDir);
    Assertions.assertTrue(Files.exists(bagitDir));
    
    Path fetchFile = bagitDir.resolve("fetch.txt");
    Assertions.assertTrue(Files.exists(fetchFile));
  }
  
  @Test
  public void testWriteEmptyBagStillCreatesDataDir() throws Exception{
    Bag bag = new Bag();
    bag.setRootDir(createDirectory("emptyBag"));
    Path dataDir = bag.getRootDir().resolve("data");
    
    BagWriter.write(bag, bag.getRootDir());
    Assertions.assertTrue(Files.exists(dataDir));
  }
}
