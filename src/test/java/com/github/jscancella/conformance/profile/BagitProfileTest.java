package com.github.jscancella.conformance.profile;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BagitProfileTest extends AbstractBagitProfileTest {

  @Test
  public void testEquals() {
    // define parameters
    String asString = "foo";
    List<String> asList = Arrays.asList(asString);
    boolean asBoolean = true;
    Serialization asSerialization = Serialization.required;

    try {
      BagitProfile profile = createExpectedProfile();

      Assertions.assertFalse(profile.equals(null));

      BagitProfile identicalProfile = createExpectedProfile();
      Assertions.assertTrue(profile.equals(identicalProfile));

      Class testModelClass = BagitProfile.class;
      Method[] methods = testModelClass.getDeclaredMethods();
      for (Method method
              : methods) {
        BagitProfile otherProfile = createExpectedProfile();

        if (method.getName().startsWith("set")) {
          Type[] pType = method.getGenericParameterTypes();
          if (pType.length == 0) {
            continue;
          }
          /**
           * Create array for parameters
           */
          Object[] params = new Object[pType.length];

          for (int i = 0; i < pType.length; i++) {
            if (pType[i].equals(String.class)) {
              params[i] = asString;

            } else if (pType[i].getTypeName().equals("java.util.List<java.lang.String>")) {
              params[i] = asList;
            } else if (pType[i].equals(boolean.class)) {
              params[i] = asBoolean;
            } else if (pType[i].equals(Serialization.class)) {
              params[i] = asSerialization;
            }

          }
          method.setAccessible(true);
          method.invoke(otherProfile, params);

          Assertions.assertFalse(otherProfile.equals(profile));
        }
      }
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      Assertions.assertFalse(true);
    }
  }
}
