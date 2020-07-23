package com.github.jscancella.domain;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
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
    Path path = Paths.get("src", "test", "resources", "md5Bag", "data");
    ManifestEntry expectedManifestEntry = 
        new ManifestEntry(path.resolve("readme.txt"), path.getFileName().resolve("readme.txt"), "aee452eebfbd978228775bf7b0e808dc");
    
    sut.bagLocation(createDirectory("tempBagForTagFolderFiles"));
    sut.addAlgorithm("md5");
    sut.addTagFile(path);
    Set<Manifest> tagManifests = sut.createTagManifests();
    for(Manifest manifest : tagManifests) {
      Assertions.assertTrue(manifest.getEntries().contains(expectedManifestEntry));
    }
  }
}
