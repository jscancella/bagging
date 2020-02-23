package com.github.jscancella.domain;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.jscancella.domain.Manifest.ManifestBuilder;

public class ManifestTest {

  @Test
  public void testLogicallySameObjectsAreEqual() throws Exception{
    Path file = Paths.get("src", "test", "resources", "md5Bag", "bagit.txt");
    Path relative = Paths.get("");
    
    Manifest manifest = new ManifestBuilder("md5").addFile(file, relative).build();
    Manifest sameManifest = new ManifestBuilder("md5").addFile(file, relative).build();
    
    Assertions.assertEquals(manifest, sameManifest);
  }
  
  @Test
  public void testHashCodesAreSame() throws Exception{
    Path file = Paths.get("src", "test", "resources", "md5Bag", "bagit.txt");
    Path relative = Paths.get("");
    
    Manifest manifest = new ManifestBuilder("md5").addFile(file, relative).build();
    Manifest sameManifest = new ManifestBuilder("md5").addFile(file, relative).build();
    
    Assertions.assertEquals(manifest.hashCode(), sameManifest.hashCode());
  }
}
