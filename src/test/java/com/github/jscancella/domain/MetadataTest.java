package com.github.jscancella.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.jscancella.conformance.profile.BagitProfile;
import com.github.jscancella.domain.Metadata.MetadataBuilder;

public class MetadataTest {

  @Test
  public void testLogicallySameObjectsAreEqual() {
    Metadata metadata = new MetadataBuilder().add("foo", "bar").build();
    Metadata identicalMetadata = new MetadataBuilder().add("foo", "bar").build();
    Assertions.assertEquals(metadata, identicalMetadata);
    
    Metadata differentMetadata = new MetadataBuilder().add("otherKey", "bar").build();
    Assertions.assertNotEquals(metadata, differentMetadata);
    
    differentMetadata = new MetadataBuilder().add("foo", "differentValue").build();
    Assertions.assertNotEquals(metadata, differentMetadata);
  }
  
  @Test
  public void testHashCodeIsSame() {
    Metadata metadata = new MetadataBuilder().add("foo", "bar").build();
    Metadata identicalMetadata = new MetadataBuilder().add("foo", "bar").build();
    Assertions.assertEquals(metadata.hashCode(), identicalMetadata.hashCode());
  }
  
  @Test
  public void testEmpty() {
    Metadata metadata = new MetadataBuilder().build();
    Assertions.assertTrue(metadata.isEmpty());
  }
  
  @Test
  public void testBuildString() {
    Metadata metadata = new MetadataBuilder().add("foo", "bar").build();
    Assertions.assertEquals("foo=bar", metadata.toString());
    
    metadata = new MetadataBuilder()
        .add("foo", "bar")
        .add("bar", "ham")
        .build();
    Assertions.assertEquals("foo=bar,bar=ham", metadata.toString());
  }
}
