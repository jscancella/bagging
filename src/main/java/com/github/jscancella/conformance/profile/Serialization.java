package com.github.jscancella.conformance.profile;

/**
 * The type of serialization required by a {@link BagitProfile}
 */
public enum Serialization {
  /**
   * That serialization is forbidden. i.e. no zip, gz, etc.
   */
  forbidden,
  /**
   * That the bag is required to be serialized. i.e. zipped, gzipped, etc.
   */
  required,
  /**
   * That serialization is optional, but not forbidden.
   */
  optional;
}
