package com.github.jscancella.domain.internal;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.jscancella.hash.BagitChecksumNameMapping;
import com.github.jscancella.hash.Hasher;

public class ManifestBuilderVistorTest {

  @Test
  public void testVisitorVisitsAllFoldersAndFiles() throws Exception{
    Path startingPoint = Paths.get("src", "test", "resources", "bags", "v0_97", "bag", "data");
    Hasher hasher = BagitChecksumNameMapping.get("md5");
    
    ManifestBuilderVistor vistor = new ManifestBuilderVistor(startingPoint, Paths.get("data"), hasher);
    Files.walkFileTree(startingPoint, vistor);
    Assertions.assertEquals(5, vistor.getEntries().size());
  }
}
