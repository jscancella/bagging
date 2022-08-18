package com.github.jscancella.reader.internal;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.jscancella.domain.Version;
import com.github.jscancella.exceptions.InvalidBagitFileFormatException;
import com.github.jscancella.exceptions.MaliciousPathException;

public class TagFileReaderTest {

  @Test
  public void testCreateFileFromManifest() throws Exception{
    Path bagRootDir = Paths.get("foo");
    Path path = TagFileReader.createFileFromManifest(bagRootDir, "data/bar/ham.txt", Version.VERSION_1_0(), StandardCharsets.UTF_8);
    Assertions.assertEquals(bagRootDir.resolve("data/bar/ham.txt"), path);
  }
  
  @Test
  public void testCreateFileFromManifestWithAsterisk() throws Exception{
    Path bagRootDir = Paths.get("foo");
    Path path = TagFileReader.createFileFromManifest(bagRootDir, "*data/bar/ham.txt", Version.VERSION_1_0(), StandardCharsets.UTF_8);
    Assertions.assertEquals(bagRootDir.resolve("data/bar/ham.txt"), path);
  }
  
  @Test
  public void testCreateFileFromManifestWithURISyntax() throws Exception{
    Path bagRootDir = Paths.get("/foo");
    String uri = "file:///foo/data/bar/ham.txt";
    Path path = TagFileReader.createFileFromManifest(bagRootDir, uri, Version.VERSION_1_0(), StandardCharsets.UTF_8);
    Assertions.assertEquals(bagRootDir.resolve("data/bar/ham.txt"), path);
  }
  
  @Test
  public void testBackslashThrowsException() throws Exception{
    Path bagRootDir = Paths.get("foo");
    Assertions.assertThrows(InvalidBagitFileFormatException.class, 
        () -> { TagFileReader.createFileFromManifest(bagRootDir, "data\\bar\\ham.txt", Version.VERSION_1_0(), StandardCharsets.UTF_8); });
  }
  
  @Test
  public void testOutsideDataDirReferenceThrowsException() throws Exception{
    Path bagRootDir = Paths.get("foo");
    Assertions.assertThrows(MaliciousPathException.class, 
        () -> { TagFileReader.createFileFromManifest(bagRootDir, "/bar/ham.txt", Version.VERSION_1_0(), StandardCharsets.UTF_8); });
  }
  
  @Test
  public void testRelativePathOutsideDataDirThrowsException() throws Exception{
    Path bagRootDir = Paths.get("foo");
    Assertions.assertThrows(MaliciousPathException.class, 
        () -> { TagFileReader.createFileFromManifest(bagRootDir, "../bar/ham.txt", Version.VERSION_1_0(), StandardCharsets.UTF_8); });
  }
  
  @Test
  public void testHomeDirReferenceThrowsException() throws Exception{
    Path bagRootDir = Paths.get("foo");
    Assertions.assertThrows(MaliciousPathException.class, 
        () -> { TagFileReader.createFileFromManifest(bagRootDir, "~/bar/ham.txt", Version.VERSION_1_0(), StandardCharsets.UTF_8); });
  }
  
  @Test
  public void testBadURIThrowsException() throws Exception{
    Path bagRootDir = Paths.get("foo");
    Assertions.assertThrows(InvalidBagitFileFormatException.class, 
        () -> { TagFileReader.createFileFromManifest(bagRootDir, "file://C:/foo^", Version.VERSION_1_0(), StandardCharsets.UTF_8); });
  }
  
  @Test
  public void testPercentDecode() throws Exception{
    String percentEncodedPath = "data/foo%0Afile%0Dnon%25sense%09something.txt";
    String expectedPath = "data/foo\nfile\rnon%sense%09something.txt"; //test a newline, carriage return, and the % sign which is used for percent encoding
    String actualPath = TagFileReader.decodeFilname(percentEncodedPath, Version.VERSION_1_0(), StandardCharsets.UTF_8);
    Assertions.assertEquals(expectedPath, actualPath);
  }
}
