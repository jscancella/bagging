package com.github.jscancella.verify.internal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.jscancella.TestUtils;
import com.github.jscancella.hash.internal.FileCountAndTotalSizeVistor;

/**
 * Tests the ignore of hidden files while walking the file tree. 
 */
public class FileCountAndTotalSizeVistorTest {
  
  private Path payloadDir = Paths.get(new File("src/test/resources/hiddenFoldersAndFiles").toURI());
  
  @BeforeEach
  public void setup() throws IOException{
    TestUtils.makeFilesHiddenOnWindows(payloadDir);
  }
  
  @Test
  public void testGeneratePayloadOxum() throws IOException{
    final FileCountAndTotalSizeVistor vistor = new FileCountAndTotalSizeVistor();
    Files.walkFileTree(payloadDir, vistor);
    Assertions.assertEquals(5, vistor.getCount());
    Assertions.assertEquals(101, vistor.getTotalSize());
  }
}
