package com.github.jscancella.reader.internal;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import com.github.jscancella.TempFolderTest;
import com.github.jscancella.domain.Manifest;
import com.github.jscancella.exceptions.InvalidBagitFileFormatException;
import com.github.jscancella.exceptions.MaliciousPathException;

public class ManifestReaderTest extends TempFolderTest {  
  @Test
  public void testReadManifest() throws Exception {
    Path rootBag = Paths.get(getClass().getClassLoader().getResource("bags/v0_97/bag").toURI());
    Path manifestFile = rootBag.resolve("manifest-md5.txt");
    Manifest manifest = ManifestReader.readManifest(manifestFile, rootBag, StandardCharsets.UTF_8);
    Assertions.assertEquals(5, manifest.getEntries().size());
    Assertions.assertEquals("md5", manifest.getBagitAlgorithmName());
    
    manifestFile = rootBag.resolve("tagmanifest-md5.txt");
    manifest = ManifestReader.readManifest(manifestFile, rootBag, StandardCharsets.UTF_8);
    Assertions.assertEquals(4, manifest.getEntries().size());
    Assertions.assertEquals("md5", manifest.getBagitAlgorithmName());
  }
  
  @Test
  public void testReadUpDirectoryMaliciousManifestThrowsException() throws Exception{
    Path manifestFile = Paths.get(getClass().getClassLoader().getResource("maliciousManifestFile/upAdirectoryReference-md5.txt").toURI());
    Assertions.assertThrows(MaliciousPathException.class, 
        () -> { ManifestReader.readManifest(manifestFile, manifestFile.getParent(), StandardCharsets.UTF_8); });
  }
  
  @Test
  public void testReadTildeMaliciousManifestThrowsException() throws Exception{
    Path manifestFile = Paths.get(getClass().getClassLoader().getResource("maliciousManifestFile/tildeReference-md5.txt").toURI());
    Assertions.assertThrows(MaliciousPathException.class, 
        () -> { ManifestReader.readManifest(manifestFile, manifestFile.getParent(), StandardCharsets.UTF_8); });
  }
  
  @Test
  @EnabledOnOs(OS.WINDOWS)
  public void testReadFileUrlMaliciousManifestThrowsException() throws Exception{
    Path manifestFile = Paths.get(getClass().getClassLoader().getResource("maliciousManifestFile/fileUrl-md5.txt").toURI());
    Assertions.assertThrows(MaliciousPathException.class, 
        () -> {  ManifestReader.readManifest(manifestFile, Paths.get("/bar"), StandardCharsets.UTF_8); });
  }
  
  @Test
  public void testReadWindowsSpecialDirMaliciousManifestThrowsException() throws Exception{
    Path manifestFile = Paths.get(getClass().getClassLoader().getResource("maliciousManifestFile/windowsSpecialDirectoryName-md5.txt").toURI());
    Assertions.assertThrows(InvalidBagitFileFormatException.class, 
        () -> { ManifestReader.readManifest(manifestFile, manifestFile.getParent(), StandardCharsets.UTF_8); });
  }
}
