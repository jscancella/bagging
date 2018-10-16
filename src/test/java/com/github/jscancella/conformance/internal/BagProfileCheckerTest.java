package com.github.jscancella.conformance.internal;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.jscancella.TempFolderTest;
import com.github.jscancella.conformance.exceptions.BagitVersionIsNotAcceptableException;
import com.github.jscancella.conformance.exceptions.FetchFileNotAllowedException;
import com.github.jscancella.conformance.exceptions.MetatdataValueIsNotAcceptableException;
import com.github.jscancella.conformance.exceptions.MetatdataValueIsNotRepeatableException;
import com.github.jscancella.conformance.exceptions.RequiredManifestNotPresentException;
import com.github.jscancella.conformance.exceptions.RequiredMetadataFieldNotPresentException;
import com.github.jscancella.conformance.exceptions.RequiredTagFileNotPresentException;
import com.github.jscancella.domain.Bag;
import com.github.jscancella.reader.BagReader;

public class BagProfileCheckerTest extends TempFolderTest {
  private static final Path profileJson = new File("src/test/resources/bagitProfiles/exampleProfile.json").toPath();
  
  @Test
  public void testBagConformsToProfile() throws Exception{
    Path bagRootPath = new File("src/test/resources/bagitProfileTestBags/profileConformantBag").toPath();
    Bag bag = BagReader.read(bagRootPath);
    
    try(InputStream inputStream = Files.newInputStream(profileJson, StandardOpenOption.READ)){
      BagProfileChecker.bagConformsToProfile(inputStream, bag);
    }
  }
  
  @Test
  public void testFetchFileNotAllowedException() throws Exception{
    Path bagRootPath = new File("src/test/resources/bagitProfileTestBags/failFetchBag").toPath();
    Bag bag = BagReader.read(bagRootPath);
    
    try(InputStream inputStream = Files.newInputStream(profileJson, StandardOpenOption.READ)){
      Assertions.assertThrows(FetchFileNotAllowedException.class, 
          () -> { BagProfileChecker.bagConformsToProfile(inputStream, bag); }); 
    }
  }
  
  @Test
  public void testRequiredMetadataFieldNotPresentException() throws Exception{
    Path bagRootPath = new File("src/test/resources/bagitProfileTestBags/emailFieldMissingBag").toPath();
    Bag bag = BagReader.read(bagRootPath);
    
    try(InputStream inputStream = Files.newInputStream(profileJson, StandardOpenOption.READ)){
      Assertions.assertThrows(RequiredMetadataFieldNotPresentException.class, 
          () -> { BagProfileChecker.bagConformsToProfile(inputStream, bag); });
    }
  }
  
  @Test
  public void testMetatdataValueIsNotAcceptableException() throws Exception{
    Path bagRootPath = new File("src/test/resources/bagitProfileTestBags/wrongValueForContactNameBag").toPath();
    Bag bag = BagReader.read(bagRootPath);
    
    try(InputStream inputStream = Files.newInputStream(profileJson, StandardOpenOption.READ)){
      Assertions.assertThrows(MetatdataValueIsNotAcceptableException.class, 
          () -> { BagProfileChecker.bagConformsToProfile(inputStream, bag); });
    }
  }
  
  @Test
  public void testMetadataValueIsNotRepeatableException() throws Exception{
    Path bagRootPath = new File("src/test/resources/bagitProfileTestBags/repeatedMetadataBag").toPath();
    Bag bag = BagReader.read(bagRootPath);
    
    try(InputStream inputStream = Files.newInputStream(profileJson, StandardOpenOption.READ)){
      Assertions.assertThrows(MetatdataValueIsNotRepeatableException.class, 
          () -> { BagProfileChecker.bagConformsToProfile(inputStream, bag); });
    }
  }
  
  @Test
  public void testRequiredPayloadManifestNotPresentException() throws Exception{
    Path bagRootPath = new File("src/test/resources/bagitProfileTestBags/missingRequiredPayloadManifestBag").toPath();
    Bag bag = BagReader.read(bagRootPath);
    
    try(InputStream inputStream = Files.newInputStream(profileJson, StandardOpenOption.READ)){
      Assertions.assertThrows(RequiredManifestNotPresentException.class, 
          () -> { BagProfileChecker.bagConformsToProfile(inputStream, bag); });
    }
  }
  
  @Test
  public void testRequiredTagManifestNotPresentException() throws Exception{
    Path bagRootPath = new File("src/test/resources/bagitProfileTestBags/missingRequiredTagManifestBag").toPath();
    Bag bag = BagReader.read(bagRootPath);
    
    try(InputStream inputStream = Files.newInputStream(profileJson, StandardOpenOption.READ)){
      Assertions.assertThrows(RequiredManifestNotPresentException.class, 
          () -> { BagProfileChecker.bagConformsToProfile(inputStream, bag); });
    }
  }
  
  @Test
  public void testRequiredTagFileNotPresentException() throws Exception{
    Path bagRootPath = new File("src/test/resources/bagitProfileTestBags/missingRequiredTagFileBag").toPath();
    Bag bag = BagReader.read(bagRootPath);
    
    try(InputStream inputStream = Files.newInputStream(profileJson, StandardOpenOption.READ)){
      Assertions.assertThrows(RequiredTagFileNotPresentException.class, 
          () -> { BagProfileChecker.bagConformsToProfile(inputStream, bag); });
    }
  }
  
  @Test
  public void testBagitVersionIsNotAcceptableException() throws Exception{
    Path bagRootPath = new File("src/test/resources/bagitProfileTestBags/wrongBagitVersionBag").toPath();
    Bag bag = BagReader.read(bagRootPath);
    
    try(InputStream inputStream = Files.newInputStream(profileJson, StandardOpenOption.READ)){
      Assertions.assertThrows(BagitVersionIsNotAcceptableException.class, 
          () -> { BagProfileChecker.bagConformsToProfile(inputStream, bag); });
    }
  }
}
