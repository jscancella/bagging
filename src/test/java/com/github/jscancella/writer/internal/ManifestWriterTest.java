package com.github.jscancella.writer.internal;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.jscancella.TempFolderTest;
import com.github.jscancella.domain.Manifest;

public class ManifestWriterTest extends TempFolderTest {
  
  @Test
  public void testWriteTagManifests() throws IOException{
    Set<Manifest> tagManifests = new HashSet<>();
    Manifest manifest = new Manifest("md5");
    manifest.getFileToChecksumMap().put(Paths.get("/foo/bar/ham/data/one/two/buckleMyShoe.txt"), "someHashValue");
    tagManifests.add(manifest);
    Path outputDir = createDirectory("tagManifests");
    Path tagManifest = outputDir.resolve("tagmanifest-md5.txt");
    
    Assertions.assertFalse(Files.exists(tagManifest));
    ManifestWriter.writeTagManifests(tagManifests, outputDir, Paths.get("/foo/bar/ham"), StandardCharsets.UTF_8);
    Assertions.assertTrue(Files.exists(tagManifest));
  }
  
  @Test
  public void testManifestsDontContainWindowsFilePathSeparator() throws IOException{
    Set<Manifest> tagManifests = new HashSet<>();
    Manifest manifest = new Manifest("md5");
    manifest.getFileToChecksumMap().put(Paths.get("/foo/bar/ham/data/one/two/buckleMyShoe.txt"), "someHashValue");
    tagManifests.add(manifest);
    Path outputDir = createDirectory("noWindowsPathSeparator");
    Path tagManifest = outputDir.resolve("tagmanifest-md5.txt");
    
    Assertions.assertFalse(Files.exists(tagManifest));
    ManifestWriter.writeTagManifests(tagManifests, outputDir, Paths.get("/foo/bar/ham"), StandardCharsets.UTF_8);
    
    List<String> lines = Files.readAllLines(tagManifest);
    for(String line : lines){
      Assertions.assertFalse(line.contains("\\"), 
          "Line [" + line + "] contains \\ which is not allowed by the bagit specification");
    }
  }
}
