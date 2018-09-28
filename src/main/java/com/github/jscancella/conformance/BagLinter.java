package com.github.jscancella.conformance;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.github.jscancella.domain.Bag;
import com.github.jscancella.verify.BagVerifier;

public enum BagLinter {; //using enum to ensure singleton

  /**
   * Check a bag against a bagit-profile as described by 
   * <a href="https://github.com/ruebot/bagit-profiles">https://github.com/ruebot/bagit-profiles</a>
   * <br>Note: <b> This implementation does not check the Serialization part of the profile!</b>
   */
  public static boolean checkAgainstProfile(final InputStream jsonProfile, final Bag bag) {
    //TODO
    return false;
  }
  
  /**
   * The BagIt specification is very flexible in what it allows which leads to situations 
   * where something may be technically allowed, but should be discouraged.
   * This method checks a bag for potential problems, or other items that are allowed but discouraged.
   * This <strong>does not</strong> validate a bag. See {@link BagVerifier} instead.
   */
  public static Set<BagitWarning> lintBag(final Path rootDir){
    return lintBag(rootDir, Collections.emptyList());
  }
  
  /**
   * The BagIt specification is very flexible in what it allows which leads to situations 
   * where something may be technically allowed, but should be discouraged.
   * This method checks a bag for potential problems, or other items that are allowed but discouraged.
   * This <strong>does not</strong> validate a bag. See {@link BagVerifier} instead.
   */
  public static Set<BagitWarning> lintBag(final Path rootDir, final Collection<BagitWarning> warningsToIgnore){
    //TODO
    return new HashSet<>();
  }
}
