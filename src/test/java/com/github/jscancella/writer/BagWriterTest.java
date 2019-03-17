package com.github.jscancella.writer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.jscancella.TempFolderTest;
import com.github.jscancella.TestUtils;
import com.github.jscancella.domain.Bag;
import com.github.jscancella.domain.Manifest;
import com.github.jscancella.domain.Version;
import com.github.jscancella.internal.PathUtils;
import com.github.jscancella.reader.BagReader;
import com.github.jscancella.writer.internal.BagCreator;

public class BagWriterTest extends TempFolderTest {
  
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
	Bag bag = new Bag.Builder().rootDirectory(createDirectory("emptyBag")).build();
		  
    Path dataDir = bag.getRootDir().resolve("data");
    
    BagWriter.write(bag, bag.getRootDir());
    Assertions.assertTrue(Files.exists(dataDir));
  }
  
  @Test
  public void testBagInPlaceWithFileNamedData() throws IOException, NoSuchAlgorithmException{
    Path testFolder = createDirectory("someFolder");
    Path dataFile = testFolder.resolve("data");
    Files.createFile(dataFile);
    
    BagCreator.bagInPlace(testFolder, Arrays.asList("md5"), false);
    Assertions.assertTrue(Files.exists(testFolder.resolve("data").resolve("data")));
  }
  
  @Test
  public void testBagInPlace() throws IOException, NoSuchAlgorithmException{
    TestStructure structure = createTestStructure();
    
    Bag bag = BagCreator.bagInPlace(folder, Arrays.asList("md5"), false);
    
    Assertions.assertEquals(Version.LATEST_BAGIT_VERSION(), bag.getVersion());
    
    Path expectedManifest = folder.resolve("manifest-md5.txt");
    Assertions.assertTrue(Files.exists(expectedManifest));
    
    Path expectedTagManifest = folder.resolve("tagmanifest-md5.txt");
    Assertions.assertTrue(Files.exists(expectedTagManifest));
    
    Path bagitFile = folder.resolve("bagit.txt");
    Assertions.assertTrue(Files.exists(bagitFile));
    
    //make sure the hidden folder was not included in the data directory
    File hiddenFolder = new File(bag.getRootDir().resolve("data").toFile(), ".hiddenFolder");
    Assertions.assertFalse(hiddenFolder.exists());
    
    for(Manifest manifest : bag.getPayLoadManifests()){
      for(Path expectedPayloadFile : manifest.getFileToChecksumMap().keySet()){
        Assertions.assertTrue(structure.regularPayloadFiles.contains(expectedPayloadFile));
      }
    }
  }
  
  @Test
  public void testBagInPlaceIncludingHidden() throws IOException, NoSuchAlgorithmException{
    TestStructure structure = createTestStructure();
    
    Bag bag = BagCreator.bagInPlace(folder, Arrays.asList("md5"), true);
    
    Assertions.assertEquals(Version.LATEST_BAGIT_VERSION(), bag.getVersion());
    
    Path expectedManifest = folder.resolve("manifest-md5.txt");
    Assertions.assertTrue(Files.exists(expectedManifest));
    
    Path expectedTagManifest = folder.resolve("tagmanifest-md5.txt");
    Assertions.assertTrue(Files.exists(expectedTagManifest));
    
    Path bagitFile = folder.resolve("bagit.txt");
    Assertions.assertTrue(Files.exists(bagitFile));
    
    for(Manifest manifest : bag.getPayLoadManifests()){
      for(Path expectedPayloadFile : manifest.getFileToChecksumMap().keySet()){
        Assertions.assertTrue(structure.regularPayloadFiles.contains(expectedPayloadFile) || 
            structure.hiddenPayloadFiles.contains(expectedPayloadFile),
            expectedPayloadFile + " doesn't exist but it should!");
      }
    }
  }
  
  private TestStructure createTestStructure() throws IOException{
    TestStructure structure = new TestStructure();
    
    Path dataDir = createDirectory("data");
    
    Path file1 = createFile("file1.txt");
    createDirectory("folder1");
    Path file2 = createFile("file2.txt");
    
    Path hiddenFile = createFile(".hiddentFile.txt");
    Path hiddenDirectory = createDirectory(".hiddenFolder");
    
    TestUtils.makeFilesHiddenOnWindows(folder);
    
    Assertions.assertTrue(Files.isHidden(hiddenFile));
    //because the Files.isHidden() always returns false for windows if it is a directory
    Assertions.assertTrue(PathUtils.isHidden(hiddenDirectory)); 
    
    Path hiddenFile2 = hiddenDirectory.resolve(".hiddenFile2.txt");
    Files.createFile(hiddenFile2);
    Path file3 = hiddenDirectory.resolve("file3.txt");
    Files.createFile(file3);
    
    structure.regularPayloadFiles.add(dataDir.resolve(file1.getFileName()));
    structure.regularPayloadFiles.add(dataDir.resolve(file2.getFileName()));
    
    structure.hiddenPayloadFiles.add(dataDir.resolve(hiddenFile.getFileName()));
    Path hiddenDirPath = dataDir.resolve(hiddenDirectory.getFileName());
    Path hiddenFile2Path = hiddenDirPath.resolve(hiddenFile2.getFileName());
    structure.hiddenPayloadFiles.add(hiddenFile2Path);
    structure.hiddenPayloadFiles.add(hiddenDirPath.resolve(file3.getFileName()));
    return structure;
  }
  
  private class TestStructure{
    List<Path> regularPayloadFiles = new ArrayList<>();
    List<Path> hiddenPayloadFiles = new ArrayList<>();
  }
}
