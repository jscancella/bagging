package com.github.jscancella.writer.internal;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.jscancella.TempFolderTest;
import com.github.jscancella.domain.Manifest;
import com.github.jscancella.domain.Manifest.ManifestBuilder;

public class ManifestWriterTest extends TempFolderTest {

  @Test
  public void testWritePayloadManifests() throws IOException {
    Path outputFolder = createDirectory("write_payload_manifests");
    
    ManifestBuilder builder = new ManifestBuilder("md5");
    builder.addFile(Paths.get("src","test","resources","bags", "v1_0", "bag", "data", "foo.txt"), outputFolder.resolve("data"));
    
    Set<Manifest> manifests = new HashSet<>();
    manifests.add(builder.build());
    
    final Path expectedFile = outputFolder.resolve("manifest-md5.txt");
    Assertions.assertTrue(!Files.exists(expectedFile), expectedFile + " should not exist yet, but it does. Something went wrong during setup?");
    Set<Path> expectedManifests = ManifestWriter.writePayloadManifests(manifests, outputFolder, StandardCharsets.UTF_8);
    for(Path expectedManifest : expectedManifests) {
      Assertions.assertTrue(Files.exists(expectedManifest), expectedManifest + " should exist but doesn't!");
      Assertions.assertTrue(Files.size(expectedManifest) > 0, expectedManifest + " should not be empty!");
    }
  }
  
  @Test
  public void testWriteTagManifests() throws IOException{
    //TODO
    Path outputFolder = createDirectory("write_tag_manifests");
    
    ManifestBuilder builder = new ManifestBuilder("md5");
    builder.addFile(Paths.get("src","test","resources","bags", "v1_0", "bag", "bagit.txt"), outputFolder);
    
    Set<Manifest> tagManifests = new HashSet<>();
    tagManifests.add(builder.build()); 
    
    final Path expectedFile = outputFolder.resolve("tagmanifest-md5.txt");
    Assertions.assertTrue(!Files.exists(expectedFile), expectedFile + " should not exist yet, but it does. Something went wrong during setup?");
    Set<Path> expectedManifests = ManifestWriter.writeTagManifests(tagManifests, outputFolder, StandardCharsets.UTF_8);
    for(Path expectedManifest : expectedManifests) {
      Assertions.assertTrue(Files.exists(expectedManifest), expectedManifest + " should exist but doesn't!");
      Assertions.assertTrue(Files.size(expectedManifest) > 0, expectedManifest + " should not be empty!");
    }
  }
}
