package com.github.jscancella.conformance.internal;

import java.util.Collection;
import java.util.ResourceBundle;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jscancella.conformance.BagitWarning;
import com.github.jscancella.domain.Version;

/**
 * Part of the BagIt conformance suite. 
 * This checker gives a warning if a bag is not using the latest bagit version
 */
public enum VersionChecker { ;//using enum to enforce singleton
  private static final Logger logger = LoggerFactory.getLogger(VersionChecker.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  
  /**
   * Check that the Bag is using the latest version.
   * 
   * @param version The version of the bag
   * @param warnings The list of warnings to add to if the bag does not conform
   * @param warningsToIgnore The list of warnings to ignore
   */
  public static void checkVersion(final Version version, final Set<BagitWarning> warnings, final Collection<BagitWarning> warningsToIgnore){
    if(!warningsToIgnore.contains(BagitWarning.OLD_BAGIT_VERSION) && version.isOlder(Version.LATEST_BAGIT_VERSION())){
      logger.warn(messages.getString("old_version_warning"), version, Version.LATEST_BAGIT_VERSION());
      warnings.add(BagitWarning.OLD_BAGIT_VERSION);
    }
  }
}
