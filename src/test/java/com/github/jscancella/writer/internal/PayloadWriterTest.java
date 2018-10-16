package com.github.jscancella.writer.internal;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.jscancella.TempFolderTest;
import com.github.jscancella.domain.FetchItem;
import com.github.jscancella.domain.Manifest;

public class PayloadWriterTest extends TempFolderTest {
  
  @Test
  public void testWritePayloadFiles() throws IOException, URISyntaxException{
    Path rootDir = Paths.get(getClass().getClassLoader().getResource("bags/v0_97/bag").toURI());
    Path testFile = Paths.get(getClass().getClassLoader().getResource("bags/v0_97/bag/data/dir1/test3.txt").toURI());
    Manifest manifest = new Manifest("md5");
    manifest.getFileToChecksumMap().put(testFile, "someHashValue");
    Set<Manifest> payloadManifests = new HashSet<>();
    payloadManifests.add(manifest);
    Path outputDir = createDirectory("writePayloadFiles");
    Path copiedFile = outputDir.resolve("data/dir1/test3.txt");
    
    Assertions.assertFalse(Files.exists(copiedFile) || Files.exists(copiedFile.getParent()));
    PayloadWriter.writePayloadFiles(payloadManifests, new ArrayList<>(), outputDir, rootDir);
    Assertions.assertTrue(Files.exists(copiedFile) || Files.exists(copiedFile.getParent()));
  }
  
  @Test
  public void testWritePayloadFilesMinusFetchFiles() throws IOException, URISyntaxException{
    Path rootDir = Paths.get(getClass().getClassLoader().getResource("bags/v0_97/bag").toURI());
    Path testFile = Paths.get(getClass().getClassLoader().getResource("bags/v0_97/bag/data/dir1/test3.txt").toURI());
    Manifest manifest = new Manifest("md5");
    manifest.getFileToChecksumMap().put(testFile, "someHashValue");
    Set<Manifest> payloadManifests = new HashSet<>();
    payloadManifests.add(manifest);
    Path outputDir = createDirectory("writePayloadWithoutFetch");
    Path copiedFile = outputDir.resolve("data/dir1/test3.txt");
    
    Assertions.assertFalse(Files.exists(copiedFile) || Files.exists(copiedFile.getParent()));
    PayloadWriter.writePayloadFiles(payloadManifests,
        Arrays.asList(new FetchItem(null, null, Paths.get("data/dir1/test3.txt"))), 
          outputDir,
          rootDir.resolve("data"));
    Assertions.assertFalse(Files.exists(copiedFile) || Files.exists(copiedFile.getParent()));
  }
}
