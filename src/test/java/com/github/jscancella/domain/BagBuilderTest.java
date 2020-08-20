package com.github.jscancella.domain;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.jscancella.TempFolderTest;
import com.github.jscancella.exceptions.InvalidBagStateException;
import com.github.jscancella.exceptions.NoSuchBagitAlgorithmException;

public class BagBuilderTest extends TempFolderTest{

  @Test
  public void builderNotNullWhenAddingAlgorithm() {
    BagBuilder sut = new BagBuilder();
    sut.addAlgorithm("md5").addAlgorithm("sha1");
  }
  
  @Test
  public void builderNotNullWhenAddingItemToFetch() throws Exception {
    BagBuilder sut = new BagBuilder();
    FetchItem fetchItem = new FetchItem(new URI("http://www.hackaday.com/blog"), 0l, Paths.get(""));
    sut.addItemToFetch(fetchItem).addItemToFetch(fetchItem);
  }
  
  @Test
  public void builderNotNullWhenAddingMetadata() {
    BagBuilder sut = new BagBuilder();
    sut.addMetadata("foo", "bar").addMetadata("foo2", "bar2");
  }
  
  @Test
  public void builderNotNullWhenAddingPayloadFile() {
    BagBuilder sut = new BagBuilder();
    Path path = Paths.get("src", "test", "resources", "md5Bag", "bagit.txt");
    sut.addPayloadFile(path).addPayloadFile(path);
  }
  
  @Test
  public void builderNotNullWhenAddingTagFile() {
    BagBuilder sut = new BagBuilder();
    Path path = Paths.get("src", "test", "resources", "md5Bag", "bagit.txt");
    sut.addTagFile(path).addTagFile(path);
  }
  
  @Test
  public void builderNotNullWhenAddingRelativePayloadFile() {
    BagBuilder sut = new BagBuilder();
    Path path = Paths.get("src", "test", "resources", "md5Bag", "bagit.txt");
    sut.addPayloadFile(path, "foo/bar/ham").addPayloadFile(path, "foo/bar/ham");
  }
  
  @Test
  public void builderNotNullWhenSettingLocation() {
    BagBuilder sut = new BagBuilder();
    Path path = Paths.get("src", "test", "resources", "md5Bag");
    sut.bagLocation(path).bagLocation(path);
  }
  
  @Test
  public void builderNotNullWhenSettingEncoding() {
    BagBuilder sut = new BagBuilder();
    sut.fileEncoding(StandardCharsets.UTF_8).fileEncoding(StandardCharsets.UTF_8);
  }
  
  @Test
  public void builderNotNullWhenSettingVersion() {
    BagBuilder sut = new BagBuilder();
    Version version = new Version(1, 0);
    sut.version(version).version(version);
  }
  
  @Test
  public void builderNotNullWhenSettingVersionDirectly() {
    BagBuilder sut = new BagBuilder();
    sut.version(1, 0).version(1, 0);
  }
  
  @Test
  public void builderThrowsErrorWhenLocationIsNull() {
    BagBuilder sut = new BagBuilder();
    sut.addAlgorithm("md5");
    sut.addMetadata("foo", "bar");
    sut.addPayloadFile(Paths.get("src", "test", "resources", "md5Bag", "data", "readme.txt"));
    
    Assertions.assertThrows(InvalidBagStateException.class, () -> { sut.write(); }); 
  }
  
  @Test
  public void builderReturnsBagWhenWriting() throws Exception{
    BagBuilder sut = new BagBuilder();
    sut.addAlgorithm("md5");
    sut.addMetadata("foo", "bar");
    sut.addPayloadFile(Paths.get("src", "test", "resources", "md5Bag", "data", "readme.txt"));
    sut.bagLocation(createDirectory("tempBagForWriting"));
    
    Assertions.assertNotNull(sut.write());
  }
  
  @Test
  public void builderThrowsErrorNonSupportedHashAlgorithm() {
    BagBuilder sut = new BagBuilder();
    Assertions.assertThrows(NoSuchBagitAlgorithmException.class, () -> { sut.addAlgorithm("somethingMadeUp"); }); 
  }
  
  @Test
  public void builderHandlesAddingDirectoryAsTagFiles() throws IOException {
    BagBuilder sut = new BagBuilder();
    Path src = Paths.get("src", "test", "resources", "bagitProfiles");
    Path dst = createDirectory("tempBagForTagFolderFiles");
    
    sut.bagLocation(dst);
    sut.addAlgorithm("md5");
    sut.addTagFile(src);
    Set<Manifest> manifests = sut.createTagManifests();
    ManifestEntry expectedEntry = new ManifestEntry(src.resolve("allOptionalFieldsProfile_v1.2.0.json"), 
                                                          Paths.get("bagitProfiles").resolve("allOptionalFieldsProfile_v1.2.0.json"), 
                                                          "425068292657a960002af7890bed207c");
    for(Manifest manifest : manifests) {
      Assertions.assertTrue(manifest.getEntries().contains(expectedEntry));
    }
    
  }
  
  @Test
  /*
   * This is in response to found bug https://github.com/jscancella/bagging/issues/46
   */
  public void builderCorrectlyHandlesAddingFolderToDataDirectory() throws IOException {
    BagBuilder sut = new BagBuilder();
    Path src = Paths.get("src", "test", "resources", "baginfoFiles");
    Path dst = createDirectory("Bug46");
    
    sut.addAlgorithm("md5")
      .addPayloadFile(src)
      .bagLocation(dst)
      .write();
    Path expectedOutput = dst.resolve("data").resolve("baginfoFiles").resolve("bag-info.txt");
    Assertions.assertTrue(Files.exists(expectedOutput), "Expected "+ expectedOutput + " to exist but it doesn't!");
  }
  
  @Test
  /*
   * This is in response to found bug https://github.com/jscancella/bagging/issues/46
   */
  public void builderCorrectlyHandlesAddingFolderToDataDirectoryUsingRelativePath() throws IOException {
    BagBuilder sut = new BagBuilder();
    Path src = Paths.get("src", "test", "resources", "baginfoFiles");
    Path dst = createDirectory("Bug46-relative");
    
    sut.addAlgorithm("md5")
      .addPayloadFile(src, "data/foo")
      .bagLocation(dst)
      .write();
    Path expectedOutput = dst.resolve("data").resolve("foo").resolve("baginfoFiles").resolve("bag-info.txt");
    Assertions.assertTrue(Files.exists(expectedOutput), "Expected "+ expectedOutput + " to exist but it doesn't!");
  }
}
