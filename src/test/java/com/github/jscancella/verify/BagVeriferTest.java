package com.github.jscancella.verify;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.jscancella.TempFolderTest;
import com.github.jscancella.TestUtils;
import com.github.jscancella.domain.Bag;
import com.github.jscancella.exceptions.CorruptChecksumException;
import com.github.jscancella.exceptions.FileNotInManifestException;
import com.github.jscancella.exceptions.NoSuchBagitAlgorithmException;
import com.github.jscancella.hash.BagitChecksumNameMapping;

public class BagVeriferTest extends TempFolderTest {
  static {
    if (Security.getProvider("BC") == null) {
      Security.addProvider(new BouncyCastleProvider());
    }
  }

  @AfterAll
  public static void tearDown() {
    BagitChecksumNameMapping.clear("sha3256");
  }

  private Path rootDir = Paths.get(new File("src/test/resources/bags/v0_97/bag").toURI());

  @Test
  public void testValidWhenHiddenFolderNotIncluded() throws Exception{
    Path copyDir = copyBagToTempFolder(rootDir);
    Files.createDirectory(copyDir.resolve("data").resolve(".someHiddenFolder"));
    TestUtils.makeFilesHiddenOnWindows(copyDir);

    Bag bag = Bag.read(copyDir);
    bag.isValid(true);
  }

  @Test
  public void testValidWithHiddenFile() throws Exception{
    Path copyDir = copyBagToTempFolder(rootDir);
    Files.createFile(copyDir.resolve("data").resolve(".someHiddenFile"));
    TestUtils.makeFilesHiddenOnWindows(copyDir);

    Bag bag = Bag.read(copyDir);
    bag.isValid(true);
  }

  @Test
  public void testInvalidWithHiddenFile() throws Exception{
    Path copyDir = copyBagToTempFolder(rootDir);
    Files.createFile(copyDir.resolve("data").resolve(".someHiddenFile"));
    TestUtils.makeFilesHiddenOnWindows(copyDir);

    Bag bag = Bag.read(copyDir);
    Assertions.assertThrows(FileNotInManifestException.class, () -> { bag.isValid(false); });
  }

  @Test
  public void testValidWithAccentedCharactersInFilename() throws Exception{
    Path bagDir = Paths.get("src", "test", "resources", "bagWithAccentedCharactersInFilename");
    Bag bag = Bag.read(bagDir);
    bag.isValid(true);
  }

  @Test
  public void testMD5Bag() throws Exception{
    Path bagDir = Paths.get("src", "test", "resources", "md5Bag");
    Bag bag = Bag.read(bagDir);
    bag.isValid(true);
  }

  @Test
  public void testSHA1Bag() throws Exception{
    Path bagDir = Paths.get("src", "test", "resources", "sha1Bag");
    Bag bag = Bag.read(bagDir);
    bag.isValid(true);
  }

  @Test
  public void testSHA224Bag() throws Exception{
    Path bagDir = Paths.get("src", "test", "resources", "sha224Bag");
    Bag bag = Bag.read(bagDir);
    bag.isValid(true);
  }

  @Test
  public void testSHA256Bag() throws Exception{
    Path bagDir = Paths.get("src", "test", "resources", "sha256Bag");
    Bag bag = Bag.read(bagDir);
    bag.isValid(true);
  }

  @Test
  public void testSHA512Bag() throws Exception{
    Path bagDir = Paths.get("src", "test", "resources", "sha512Bag");
    Bag bag = Bag.read(bagDir);
    bag.isValid(true);
  }

  @Test
  public void testVersion0_97IsValid() throws Exception{
    Bag bag = Bag.read(rootDir);

    bag.isValid(true);
  }

  @Test
  public void testIsComplete() throws Exception{
    Bag bag = Bag.read(rootDir);

    bag.isComplete(true);
  }

  @Test
  public void testCorruptPayloadFile() throws Exception{
    rootDir = Paths.get(new File("src/test/resources/corruptPayloadFile").toURI());
    Bag bag = Bag.read(rootDir);

    Assertions.assertThrows(CorruptChecksumException.class, () -> { bag.isValid(true); });
  }

  @Test
  public void testCorruptTagFile() throws Exception{
    rootDir = Paths.get(new File("src/test/resources/corruptTagFile").toURI());
    Bag bag = Bag.read(rootDir);

    Assertions.assertThrows(CorruptChecksumException.class, () -> { bag.isValid(true); });
  }

  @Test
  public void testThrowsNoSuchAlgorithmExceptionWhenNotInHasherMap() throws Exception{
    Path sha3BagDir = Paths.get(getClass().getClassLoader().getResource("sha3Bag").toURI());

    Assertions.assertThrows(NoSuchBagitAlgorithmException.class, () -> { Bag.read(sha3BagDir); });
  }

  @Test
  public void testAddSHA3SupportViaInterface() throws Exception{
    boolean successful = BagitChecksumNameMapping.add("sha3256", SHA3Hasher.class);
    Assertions.assertTrue(successful);

    Path sha3BagDir = Paths.get(new File("src/test/resources/sha3Bag").toURI());
    Bag bag = Bag.read(sha3BagDir);
    Assertions.assertTrue(bag.isValid(true));
  }

  /*
   * Technically valid but highly discouraged
   */
  @Test
  public void testManifestsWithLeadingDotSlash() throws Exception{
    Path bagPath = Paths.get(new File("src/test/resources/bag-with-leading-dot-slash-in-manifest").toURI());
    Bag bag = Bag.read(bagPath);

    Assertions.assertTrue(bag.isValid(true));
  }
}
