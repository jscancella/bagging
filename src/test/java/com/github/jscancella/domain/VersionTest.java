package com.github.jscancella.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VersionTest {
  private static Version versionZero = new Version(0, 0);
  private static Version sameVersion = new Version(0, 0);
  private static Version versionMinorUpgrade = new Version(0, 1);
  private static Version versionMajorUpgrade = new Version(1, 0);
  private static Version versionNewer = new Version(1, 1);

  @Test
  public void testLogicallySameObjectsAreEqual() {
    Assertions.assertEquals(versionZero, sameVersion);
  }
  
  @Test
  public void testSameOrNewer() {
    Assertions.assertTrue(sameVersion.isSameOrNewer(versionZero));
    Assertions.assertTrue(versionMinorUpgrade.isSameOrNewer(versionZero));
    Assertions.assertTrue(versionMajorUpgrade.isSameOrNewer(versionZero));
    Assertions.assertTrue(versionNewer.isSameOrNewer(versionZero));
  }
  
  @Test
  public void testNewer() {
    Assertions.assertFalse(sameVersion.isNewer(versionZero));
    Assertions.assertTrue(versionMinorUpgrade.isNewer(versionZero));
    Assertions.assertTrue(versionMajorUpgrade.isNewer(versionZero));
    Assertions.assertTrue(versionNewer.isNewer(versionZero));
  }
  
  @Test
  public void testSameOrOlder() {
    Assertions.assertTrue(versionZero.isSameOrOlder(sameVersion));
    Assertions.assertTrue(versionZero.isSameOrOlder(versionMinorUpgrade));
    Assertions.assertTrue(versionZero.isSameOrOlder(versionMajorUpgrade));
    Assertions.assertTrue(versionZero.isSameOrOlder(versionNewer));
  }
  
  @Test
  public void testOlder() {
    Assertions.assertFalse(versionZero.isOlder(sameVersion));
    Assertions.assertTrue(versionZero.isOlder(versionMinorUpgrade));
    Assertions.assertTrue(versionZero.isOlder(versionMajorUpgrade));
    Assertions.assertTrue(versionZero.isOlder(versionNewer));
  }
}
