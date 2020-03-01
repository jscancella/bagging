package com.github.jscancella.domain;

import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class ManifestEntryTest {

  @Test
  public void testLogicallySameObjectsAreEqual() {
    ManifestEntry entry = new ManifestEntry(Paths.get(""), Paths.get(""), "ABCDEF");
    ManifestEntry identicalEntry = new ManifestEntry(Paths.get(""), Paths.get(""), "ABCDEF");
    Assertions.assertEquals(entry, identicalEntry);
    
    ManifestEntry differentEntry = new ManifestEntry(Paths.get("differentLocation"), Paths.get(""), "ABCDEF");
    Assertions.assertNotEquals(entry, differentEntry);
    
    differentEntry = new ManifestEntry(Paths.get(""), Paths.get("differentRelativeLocation"), "ABCDEF");
    Assertions.assertNotEquals(entry, differentEntry);
    
    differentEntry = new ManifestEntry(Paths.get(""), Paths.get(""), "differentHash");
    Assertions.assertNotEquals(entry, differentEntry);
  }
  
  @Test
  public void testHashCodeIsSame() {
    ManifestEntry entry = new ManifestEntry(Paths.get(""), Paths.get(""), "ABCDEF");
    ManifestEntry identicalEntry = new ManifestEntry(Paths.get(""), Paths.get(""), "ABCDEF");
    Assertions.assertEquals(entry.hashCode(), identicalEntry.hashCode());
  }
}
