package com.github.jscancella.domain;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import com.github.jscancella.domain.Manifest.ManifestBuilder;

public class ManifestBuilderTest {

  @Test
  public void builderNotNullWhenAddingEntry() {
    ManifestBuilder sut = new ManifestBuilder("md5");
    Path path = Paths.get("src", "test", "resources", "md5Bag", "bagit.txt");
    ManifestEntry entry = new ManifestEntry(path, path, null);
    sut.addEntry(entry).addEntry(entry);
  }
  
  @Test
  public void builderNotNullWhenAddingFile() throws Exception {
    ManifestBuilder sut = new ManifestBuilder("md5");
    Path file = Paths.get("src", "test", "resources", "md5Bag", "bagit.txt");
    Path relative = Paths.get("data", "fooDir", "bagit.txt");
    sut.addFile(file, relative).addFile(file, relative);
  }
  
  @Test
  public void builderNotNullWhenAddingAlgorithm() {
    ManifestBuilder sut = new ManifestBuilder("md5");
    sut.bagitAlgorithmName("md5").bagitAlgorithmName("sha1");
  }
  
  @Test
  public void builderReturnsManifest() {
    ManifestBuilder sut = new ManifestBuilder("md5");
    Assertions.assertNotNull(sut.build());
  }
}
