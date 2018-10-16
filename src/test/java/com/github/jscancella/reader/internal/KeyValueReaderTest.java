package com.github.jscancella.reader.internal;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.jscancella.TempFolderTest;
import com.github.jscancella.exceptions.InvalidBagMetadataException;

public class KeyValueReaderTest extends TempFolderTest {

  @Test
  public void testReadInproperIndentedBagMetadataFileThrowsException() throws Exception{
    Path baginfo = Paths.get(getClass().getClassLoader().getResource("badBagMetadata/badIndent.txt").toURI());
    Assertions.assertThrows(InvalidBagMetadataException.class, 
        () -> { KeyValueReader.readKeyValuesFromFile(baginfo, ":", StandardCharsets.UTF_8); });
  }
  
  @Test
  public void testReadInproperBagMetadataKeyValueSeparatorThrowsException() throws Exception{
    Path baginfo = Paths.get(getClass().getClassLoader().getResource("badBagMetadata/badKeyValueSeparator.txt").toURI());
    Assertions.assertThrows(InvalidBagMetadataException.class, 
        () -> { KeyValueReader.readKeyValuesFromFile(baginfo, ":", StandardCharsets.UTF_8); });
  }
}
