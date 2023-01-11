package com.github.jscancella.verify.internal;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.jscancella.domain.Bag;
import com.github.jscancella.exceptions.FileNotInManifestException;
import com.github.jscancella.exceptions.FileNotInPayloadDirectoryException;

public class ManifestVerifierTest {

  private Path rootDir = Paths.get(new File("src/test/resources/bags/v0_97/bag").toURI());

  @Test
  public void testErrorWhenManifestListFileThatDoesntExist() throws Exception{
    rootDir = Paths.get(new File("src/test/resources/filesInManifestDontExist").toURI());
    Bag bag = Bag.read(rootDir);

    Assertions.assertThrows(FileNotInPayloadDirectoryException.class,
        () -> { ManifestVerifier.verifyManifests(bag, true); });
  }

  @Test
  public void testErrorWhenFileIsntInManifest() throws Exception{
    rootDir = Paths.get(new File("src/test/resources/filesInPayloadDirAreNotInManifest").toURI());
    Bag bag = Bag.read(rootDir);

    Assertions.assertThrows(FileNotInManifestException.class,
        () -> { ManifestVerifier.verifyManifests(bag, true); });
  }

  @Test
  public void testBagWithTagFilesInPayloadIsValid() throws Exception{
    rootDir = Paths.get(new File("src/test/resources/bags/v0_96/bag-with-tagfiles-in-payload-manifest").toURI());
    Bag bag = Bag.read(rootDir);

    ManifestVerifier.verifyManifests(bag, true);
  }

  @Test
  public void testNotAllFilesListedInAllManifestsThrowsException() throws Exception{
    Path bagDir = Paths.get(new File("src/test/resources/notAllFilesListedInAllManifestsBag").toURI());
    Bag bag = Bag.read(bagDir);
    Assertions.assertThrows(FileNotInManifestException.class,
        () -> { ManifestVerifier.verifyManifests(bag, true); });
  }
}
