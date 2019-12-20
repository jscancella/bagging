package com.github.jscancella.writer.internal;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.jscancella.TempFolderTest;

public class PayloadWriterTest extends TempFolderTest {
  
  @Test
  public void testWritePayloadFiles() throws IOException, URISyntaxException{
    Path testFile = Paths.get(getClass().getClassLoader().getResource("bags/v0_97/bag/data/dir1/test3.txt").toURI());
    Set<Path> paths = new HashSet<>(Arrays.asList(testFile));
    Path outputDir = createDirectory("writePayloadFiles");
    Path copiedFile = outputDir.resolve("data/dir1/test3.txt");
    
    Assertions.assertFalse(Files.exists(copiedFile) || Files.exists(copiedFile.getParent()));
    PayloadWriter.writePayloadFiles(paths, outputDir);
    Assertions.assertTrue(Files.exists(copiedFile) || Files.exists(copiedFile.getParent()));
  }
}
