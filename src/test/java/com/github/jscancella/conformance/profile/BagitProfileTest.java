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
    BagitProfile profile = createExpectedProfile(new Version(1,2));
    BagitProfile identicalProfile = createExpectedProfile(new Version(1,2));
    Assertions.assertEquals(profile, identicalProfile);
  }

  /*
   * We test that the .equals() method uses all fields for comparing
   * by using reflection to change the set field.
   */
  @Test
  public void testEveryVariableIsIncludedInEqualsMethod() throws Exception {
    BagitProfile sut = createExpectedProfile(new Version(1,2));
    BagitProfile otherProfile = createExpectedProfile(new Version(1,2));
    
    Class<BagitProfile> testModelClass = BagitProfile.class;
    Field[] fields = testModelClass.getDeclaredFields();
    for(Field field : fields) {
      field.setAccessible(true);
      
      if(String.class.isAssignableFrom(field.getType())) {
        field.set(otherProfile, "");
      }
      else if(Map.class.isAssignableFrom(field.getType())) {
        field.set(otherProfile, Collections.emptyMap());
      }
      else if(boolean.class.isAssignableFrom(field.getType())) {
        field.set(otherProfile, !field.getBoolean(sut));
      }
      else if(List.class.isAssignableFrom(field.getType())) {
        field.set(otherProfile, Collections.emptyList());
      }
      else if(Serialization.class.isAssignableFrom(field.getType())) {
        field.set(otherProfile, Serialization.optional);
      }
      else if(URI.class.isAssignableFrom(field.getType())) {
        field.set(otherProfile, null);
      }
      else if(field.getName().equalsIgnoreCase("$jacocoData")) {
        //skip as this is only applicable during testing time
        continue;
      }
      else {
        Assertions.fail("This test does not account for field type: [" + field.getGenericType() + "] of variable: [" + field.getName() + "]");
      }
      Assertions.assertNotEquals(sut, otherProfile);
    }
  }
}
