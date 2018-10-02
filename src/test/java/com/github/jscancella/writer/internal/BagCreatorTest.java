package com.github.jscancella.writer.internal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
import com.github.jscancella.hash.defaults.MD5Hasher;

public class BagCreatorTest extends TempFolderTest {
  
  @Test
  public void testBagInPlaceWithFileNamedData() throws IOException, NoSuchAlgorithmException{
    Path testFolder = createDirectory("someFolder");
    Path dataFile = testFolder.resolve("data");
    Files.createFile(dataFile);
    
    BagCreator.bagInPlace(testFolder, Arrays.asList(MD5Hasher.INSTANCE.getBagitAlgorithmName()), false);
    Assertions.assertTrue(Files.exists(testFolder.resolve("data").resolve("data")));
  }
  
  @Test
  public void testBagInPlace() throws IOException, NoSuchAlgorithmException{
    TestStructure structure = createTestStructure();
    
    Bag bag = BagCreator.bagInPlace(folder, Arrays.asList(MD5Hasher.INSTANCE.getBagitAlgorithmName()), false);
    
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
    
    Bag bag = BagCreator.bagInPlace(folder, Arrays.asList(MD5Hasher.INSTANCE.getBagitAlgorithmName()), true);
    
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
    
    Path dataDir = folder.resolve("data");
    
    Path file1 = createFile("file1.txt");
    createDirectory("folder1");
    Path file2 = createFile("file2.txt");
    
    Path hiddenFile = createFile(".hiddentFile.txt");
    Path hiddenDirectory = createDirectory(".hiddenFolder");
    
    TestUtils.makeFilesHiddenOnWindows(folder);
    
    Assertions.assertTrue(Files.isHidden(hiddenFile));
    //because the Files.isHidden() always returns false for windows if it is a directory
//    Assertions.assertTrue(PathUtils.isHidden(hiddenDirectory)); 
    
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
