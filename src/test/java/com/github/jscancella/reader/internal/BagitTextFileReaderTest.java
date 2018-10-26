package com.github.jscancella.reader.internal;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleImmutableEntry;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.jscancella.TempFolderTest;
import com.github.jscancella.domain.Version;
import com.github.jscancella.exceptions.InvalidBagitFileFormatException;
import com.github.jscancella.exceptions.UnparsableVersionException;

public class BagitTextFileReaderTest extends TempFolderTest {
  
  @Test
  public void testParseVersionWithBadVersion() {
    Assertions.assertThrows(UnparsableVersionException.class, 
        () -> { BagitTextFileReader.parseVersion("someVersionThatIsUnparsable"); });
  }
  
  @Test
  public void testParseKnownVersions() throws Exception{
    String[] knownVersions = new String[] {"0.93", "0.94", "0.95", "0.96", "0.97", "1.0"};
    for(String knownVersion : knownVersions){
      BagitTextFileReader.parseVersion(knownVersion);
    }
  }
  
  @Test
  public void testParseVersionsWithSpaces() throws Exception{
    BagitTextFileReader.parseVersion("1.0 ");
    BagitTextFileReader.parseVersion(" 1.0");
  }
  
  @Test
  public void testParsePartlyMissingVersion() throws Exception{
    Assertions.assertThrows(UnparsableVersionException.class, 
        () -> { BagitTextFileReader.parseVersion(".97"); });
  }
  
  @Test
  public void testReadBagitFile()throws Exception{
    Path bagitFile = Paths.get(new File("src/test/resources/bagitFiles/bagit-0.97.txt").toURI());
    SimpleImmutableEntry<Version, Charset> actualBagitInfo = BagitTextFileReader.readBagitTextFile(bagitFile);
    Assertions.assertEquals(new Version(0, 97), actualBagitInfo.getKey());
    Assertions.assertEquals(StandardCharsets.UTF_8, actualBagitInfo.getValue());
  }
  
  @Test
  public void testReadBagitFileWithBomShouldThrowException()throws Exception{
    Path bagitFile = Paths.get(new File("src/test/resources/bagitFiles/bagit-with-bom.txt").toURI());
    Assertions.assertThrows(InvalidBagitFileFormatException.class, 
        () -> { BagitTextFileReader.readBagitTextFile(bagitFile); });
  }
}
