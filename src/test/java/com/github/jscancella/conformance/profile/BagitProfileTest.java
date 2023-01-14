package com.github.jscancella.conformance.profile;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.jscancella.domain.Version;

public class BagitProfileTest extends AbstractBagitProfileTest {
	
  @Test
  public void testLogicallySameObjectsAreEqual() {
    Version version = new Version(1,4); //needs to always be the latest version
    BagitProfile profile = createExpectedProfile(version);
    BagitProfile identicalProfile = createExpectedProfile(version);
    Assertions.assertEquals(profile, identicalProfile);
    
    //test individual equals for code coverage
    Assertions.assertEquals(profile.getBagitProfileVersion(), identicalProfile.getBagitProfileVersion());
    Assertions.assertEquals(profile.getContactEmail(), identicalProfile.getContactEmail());
    Assertions.assertEquals(profile.getContactName(), identicalProfile.getContactName());
    Assertions.assertEquals(profile.getContactPhone(), identicalProfile.getContactPhone());
    Assertions.assertEquals(profile.getExternalDescription(), identicalProfile.getExternalDescription());
    Assertions.assertEquals(profile.getSourceOrganization(), identicalProfile.getSourceOrganization());
    Assertions.assertEquals(profile.getVersion(), identicalProfile.getVersion());
    Assertions.assertEquals(profile.getAcceptableBagitVersions(), identicalProfile.getAcceptableBagitVersions());
    Assertions.assertEquals(profile.getAcceptableMIMESerializationTypes(), identicalProfile.getAcceptableMIMESerializationTypes());
    Assertions.assertEquals(profile.getBagInfoRequirements(), identicalProfile.getBagInfoRequirements());
    Assertions.assertEquals(profile.getBagitProfileIdentifier(), identicalProfile.getBagitProfileIdentifier());
    Assertions.assertEquals(profile.getManifestTypesAllowed(), identicalProfile.getManifestTypesAllowed());
    Assertions.assertEquals(profile.getManifestTypesRequired(), identicalProfile.getManifestTypesRequired());
    Assertions.assertEquals(profile.getSerialization(), identicalProfile.getSerialization());
    Assertions.assertEquals(profile.getTagFilesAllowed(), identicalProfile.getTagFilesAllowed());
    Assertions.assertEquals(profile.getTagFilesRequired(), identicalProfile.getTagFilesRequired());
    Assertions.assertEquals(profile.getTagManifestTypesAllowed(), identicalProfile.getTagManifestTypesAllowed());
    Assertions.assertEquals(profile.getTagManifestTypesRequired(), identicalProfile.getTagManifestTypesRequired());
  }
  
  @Test
  public void testToStringContainsAllFields() {
    Version version = new Version(1,4); //needs to always be the latest version
    String sut = createExpectedProfile(version).toString();
    
    Class<BagitProfile> testModelClass = BagitProfile.class;
    Field[] fields = testModelClass.getDeclaredFields();
    for(Field field : fields) {
      if(field.getName().equalsIgnoreCase("$jacocoData")) {
        continue; //skip as this is only applicable during testing time
      }
      Assertions.assertTrue(sut.contains(field.getName()), "toString() does not contain field: " + field.getName());
    }
  }

  /*
   * We test that the .equals() method uses all fields for comparing
   * by using reflection to change the set field.
   */
  @Test
  public void testEveryVariableIsIncludedInEqualsMethod() throws Exception {
    Version version = new Version(1,4); //needs to always be the latest version
    
    BagitProfile sut = createExpectedProfile(version);
    BagitProfile otherProfile = createExpectedProfile(version);
    
    Class<BagitProfile> testModelClass = BagitProfile.class;
    Field[] fields = testModelClass.getDeclaredFields();
    for(Field field : fields) {
      field.setAccessible(true);
      
      if(String.class.isAssignableFrom(field.getType())) {
        field.set(otherProfile, "");
        Assertions.assertNotEquals(field.get(sut), "", "did you forget to set it in AbstractBagitProfileTest.createExpectedProfile()?"); //make sure the string isn't the same in sut and otherProfile
      }
      else if(Map.class.isAssignableFrom(field.getType())) {
        field.set(otherProfile, Collections.emptyMap());
        Assertions.assertNotEquals(field.get(sut), Collections.emptyMap(), "did you forget to set it in AbstractBagitProfileTest.createExpectedProfile()?"); //make sure the string isn't the same in sut and otherProfile
      }
      else if(boolean.class.isAssignableFrom(field.getType())) {
        field.set(otherProfile, !field.getBoolean(sut));
      }
      else if(List.class.isAssignableFrom(field.getType())) {
        field.set(otherProfile, Collections.emptyList());
        Assertions.assertNotEquals(field.get(sut), Collections.emptyList(), "did you forget to set it in AbstractBagitProfileTest.createExpectedProfile()?"); //make sure the string isn't the same in sut and otherProfile
      }
      else if(Serialization.class.isAssignableFrom(field.getType())) {
        field.set(otherProfile, Serialization.optional);
      }
      else if(URI.class.isAssignableFrom(field.getType())) {
        field.set(otherProfile, null);
        Assertions.assertNotEquals(field.get(sut), null, "did you forget to set it in AbstractBagitProfileTest.createExpectedProfile()?"); //make sure the string isn't the same in sut and otherProfile
      }
      else if(field.getName().equalsIgnoreCase("$jacocoData")) {
        //skip as this is only applicable during testing time
        continue;
      }
      else {
        Assertions.fail("This test does not account for field type: [" + field.getGenericType() + "] of variable: [" + field.getName() + "]");
      }
      Assertions.assertNotEquals(sut, otherProfile, "Equals does not account for " + field.getName());
      //reset so that all the fields are the same except for the next one we want to test
      otherProfile = createExpectedProfile(version);
    }
  }
}
