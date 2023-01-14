package com.github.jscancella.domain;

import java.util.Objects;

import com.github.jscancella.domain.internal.EclipseGenerated;

/**
 * The version of the bagit specification used to create the bag.
 */
public final class Version implements Comparable<Version>{
  /**
   * the major version of the bagit specification
   */
  public final int major;
  /**
   * the minor version of the bagit specification
   */
  public final int minor;
  private transient String cachedToString;
  
  /**
   * @param major the major version of the bagit specification
   * @param minor the minor version of the bagit specification
   */
  public Version(final int major, final int minor){
    this.major = major;
    this.minor = minor;
    this.cachedToString = major + "." + minor;
  }
  
  /**
   * @return the latest version of the BagIt specification
   */
  public static Version LATEST_BAGIT_VERSION() {
    return Version.VERSION_1_0();
  }
  
  /**
   * @return the version that corresponds with version 1.0 of the specification
   */
  public static Version VERSION_1_0() {
    return new Version(1, 0);
  }
  
  /**
   * @return the version that corresponds with version 0.95 of the specification. 0.95 was the last version where particular tag files were named differently than now 
   */
  public static Version VERSION_0_95() {
    return new Version(0, 95);
  }

  @EclipseGenerated
  @Override
  public String toString() {
    if(cachedToString == null) {
      cachedToString = major + "." + minor;
    }
    return cachedToString;
  }

  @Override
  public int compareTo(final Version other) {
    int returnValue = -1;
    //a negative integer - this is less than specified object
    //zero - equal to specified object
    //positive - greater than the specified object
    if(major > other.major || major == other.major && minor > other.minor){
      returnValue = 1;
    }
    if(major == other.major && minor == other.minor){
      returnValue = 0;
    }
    
    return returnValue;
  }

  @EclipseGenerated
  @Override
  public int hashCode() {
    return Objects.hash(major, minor);
  }

  @EclipseGenerated
  @Override
  public boolean equals(final Object obj) {
    boolean isEqual = false;
    if (obj instanceof Version){
      final Version other = (Version) obj;
      
      isEqual = Objects.equals(major, other.major) && Objects.equals(minor, other.minor); 
    }
    
    return isEqual;
  }
  
  /**
   * @param version the version to compare to
   * @return true if this version is newer than the supplied version
   */
  public boolean isNewer(final Version version){
    return this.compareTo(version) > 0;
  }
  
  /**
   * @param version the version to compare to
   * @return  true if this version is same or newer than the supplied version
   */
  public boolean isSameOrNewer(final Version version){
    return this.compareTo(version) >= 0;
  }
  
  /**
   * @param version the version to compare to
   * @return  true if this version is older than the supplied version
   */
  public boolean isOlder(final Version version){
    return this.compareTo(version) < 0;
  }
  
  /**
   * @param version the version to compare to
   * @return  true if this version is same or older than the supplied version
   */
  public boolean isSameOrOlder(final Version version){
    return this.compareTo(version) <= 0;
  }

  /**
   * @return the major version
   */
  public int getMajor() {
    return major;
  }

  /**
   * @return the minor version
   */
  public int getMinor() {
    return minor;
  }
}
