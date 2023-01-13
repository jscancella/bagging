/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.jscancella.conformance.profile;

import java.io.File;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.jscancella.domain.Version;


public class BagitProfileDeserializerTest extends AbstractBagitProfileTest{

  @Test
  public void testDeserializeMinimalVersion1_2_0() throws Exception{
    BagitProfile expectedProfile = createMinimalProfile("1.2.0");
    
    BagitProfile profile = mapper.readValue(new File("src/test/resources/bagitProfiles/OnlyRequiredFieldsProfile_v1.2.0.json"), BagitProfile.class);
    
    Assertions.assertEquals(expectedProfile, profile);
  }
  
  @Test
  public void testDeserializeMinimalVersion1_3_0() throws Exception{
    BagitProfile expectedProfile = createMinimalProfile("1.3.0");
    
    BagitProfile profile = mapper.readValue(new File("src/test/resources/bagitProfiles/OnlyRequiredFieldsProfile_v1.3.0.json"), BagitProfile.class);
    
    Assertions.assertEquals(expectedProfile, profile);
  }
  
  @Test
  public void testDeserializeMinimalVersion1_4_0() throws Exception{
    BagitProfile expectedProfile = createMinimalProfile("1.4.0");
    
    BagitProfile profile = mapper.readValue(new File("src/test/resources/bagitProfiles/OnlyRequiredFieldsProfile_v1.4.0.json"), BagitProfile.class);
    
    Assertions.assertEquals(expectedProfile, profile);
  }
  
  @Test
  public void testDeserializeAllOptionsVersion1_2_0() throws Exception{
    BagitProfile expectedProfile = createExpectedProfile(new Version(1,2));
    
    BagitProfile profile = mapper.readValue(new File("src/test/resources/bagitProfiles/allOptionalFieldsProfile_v1.2.0.json"), BagitProfile.class);
    
    Assertions.assertEquals(expectedProfile, profile);
  }
  
  @Test
  public void testDeserializeAllOptionsVersion1_3_0() throws Exception{
    BagitProfile expectedProfile = createExpectedProfile(new Version(1,3));
    
    BagitProfile profile = mapper.readValue(new File("src/test/resources/bagitProfiles/allOptionalFieldsProfile_v1.3.0.json"), BagitProfile.class);
    
    Assertions.assertEquals(expectedProfile, profile);
  }
  
  @Test
  public void testDeserializeAllOptionsVersion1_4_0() throws Exception{
    BagitProfile expectedProfile = createExpectedProfile(new Version(1,4));
    
    BagitProfile profile = mapper.readValue(new File("src/test/resources/bagitProfiles/allOptionalFieldsProfile_v1.4.0.json"), BagitProfile.class);
    
    Assertions.assertEquals(expectedProfile, profile);
  }
  
}
