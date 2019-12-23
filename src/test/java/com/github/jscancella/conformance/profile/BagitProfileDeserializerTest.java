/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.jscancella.conformance.profile;

import java.io.File;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class BagitProfileDeserializerTest extends AbstractBagitProfileTest{

  @Test
  public void testDeserialize() throws Exception{
    BagitProfile expectedProfile = createExpectedProfile();
    
    BagitProfile profile = mapper.readValue(new File("src/test/resources/bagitProfiles/exampleProfile.json"), BagitProfile.class);
    
    Assertions.assertTrue(expectedProfile.equals(profile));
  }
  
  @Test
  public void testDeserializeWithoutOptionalTags() throws Exception{
    BagitProfile minimalProfile = createMinimalProfile();
    
    BagitProfile profile = mapper.readValue(new File("src/test/resources/bagitProfiles/exampleProfileOnlyRequiredFields.json"), BagitProfile.class);
    Assertions.assertTrue(minimalProfile.equals(profile));
  }
  
  
}
