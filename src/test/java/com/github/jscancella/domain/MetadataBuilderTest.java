package com.github.jscancella.domain;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.jscancella.domain.Metadata.MetadataBuilder;

public class MetadataBuilderTest {
  
  @Test
  public void testRemoveRemovesAllKeys() {
    MetadataBuilder sut = new MetadataBuilder();
    sut.add("foo", "bar"); //first value for foo
    sut.add("foo", "ham"); //second value for foo
    sut.remove("foo"); //should remove both bar and ham
    Metadata metadata = sut.build();
    
    Assertions.assertEquals(Collections.EMPTY_LIST, metadata.get("foo"));
  }
  
  @Test
  public void testRemoveRemovesAllKeysCaseInsensitive() {
    MetadataBuilder sut = new MetadataBuilder();
    sut.add("foo", "bar"); //first value for foo
    sut.add("foo", "ham"); //second value for foo
    sut.remove("FOO"); //should remove both bar and ham
    Metadata metadata = sut.build();
    
    Assertions.assertEquals(Collections.EMPTY_LIST, metadata.get("foo"));
    Assertions.assertEquals(0, metadata.getList().size());
    Assertions.assertEquals(0, metadata.getMap().size());
  }
  
  @Test
  public void testRemoveDoesntRemoveOtherKeys() {
    MetadataBuilder sut = new MetadataBuilder();
    sut.add("foo", "bar"); //first value for foo
    sut.add("foo", "ham"); //second value for foo
    sut.remove("bar"); //shouldn't remove anything
    Metadata metadata = sut.build();
    
    Assertions.assertEquals(2, metadata.getList().size());
    Assertions.assertEquals(1, metadata.getMap().size());
  }
  
  @Test
  public void testChainingMethodsDoesntReturnNullBuilder() {
    MetadataBuilder sut = new MetadataBuilder();
    Metadata metadata = sut.add("foo", "bar")
      .add("foo", "ham")
      .remove("foo")
      .remove("FOO")
      .addAll(Arrays.asList(new SimpleImmutableEntry<>("foo","bar")))
      .build();
    
    Assertions.assertNotNull(metadata);
  }
  
  @Test
  public void testAddingPayloadOxumMultipleTimesOverwrites() {
    MetadataBuilder sut = new MetadataBuilder();
    sut.add("Payload-Oxum", "bar");
    sut.add("Payload-Oxum", "ham"); //adding it again should overwrite the first entry
    Metadata metadata = sut.build();
    
    Assertions.assertEquals(1, metadata.get("Payload-Oxum").size());
    Assertions.assertEquals(Arrays.asList("ham"), metadata.get("Payload-Oxum"));
  }
}
