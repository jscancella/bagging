package com.github.jscancella.conformance;

import java.util.ResourceBundle;

import com.github.jscancella.domain.Version;

/**
 * The BagIt specification is very flexible in what it allows. 
 * This leads to situations where something may be technically allowed, but should be discouraged. 
 * This class is for that purpose, to allow reporting of these allowed but discouraged situations to the user.
 */
public enum BagitWarning {
  /**
   * A bag contains another bag within its data directory
   */
  BAG_WITHIN_A_BAG("bag_within_a_bag"),
  /**
   * The data directory contains the same file name but with a different case. It may cause problems on Mac OS which ignores case.
   */
  DIFFERENT_CASE("different_case"),
  /**
   * The manifest contains a path that differs from the file in the data directory due to normalization. 
   * Often they look identical, but differ in the actual bytes.
   */
  DIFFERENT_NORMALIZATION("different_normalization"),
  /**
   * After version 1.0 the specification read that the bagit.txt MUST contain EXACTLY 2 lines
   */
  EXTRA_LINES_IN_BAGIT_FILES("extra_lines_in_bagit_files"),
  /**
   * Is the path relative. Often used to create malicious paths. 
   */
  LEADING_DOT_SLASH("leading_dot_slash"),
  /**
   * You are using a non-standard checksum algorithm which may cause problems on other systems
   */
  NON_STANDARD_ALGORITHM("non_standard_algorithm"),
  /**
   * The manifest looks to be generated from using md5sum linux utility which can cause some compatibility problems.
   */
  MD5SUM_TOOL_GENERATED_MANIFEST("md5sum_tool_generated_manifest"),
  /**
   * There is no tag manifest.
   */
  MISSING_TAG_MANIFEST("missing_tag_manifest"),
  /**
   * The version of the bag is older than the latest version. See {@link Version#LATEST_BAGIT_VERSION()}
   */
  OLD_BAGIT_VERSION("old_bagit_version"),
  /**
   * The bag contains OS specific files which may cause compatibility issues.
   */
  OS_SPECIFIC_FILES("os_specific_files"),
  /**
   * The bag-info.txt file does contain the payload-oum which is used to do a quick verify.
   */
  PAYLOAD_OXUM_MISSING("payload_oxum_missing"),
  /**
   * The tag files do not use UTF-8 for encoding which will cause compatibility issues
   */
  TAG_FILES_ENCODING("tag_files_encoding"),
  /**
   * The manifest is using an checksum algorithm that is known to be weak.
   */
  WEAK_CHECKSUM_ALGORITHM("weak_checksum_algorithm"),
  /**
   * Starting with version 1.0 all manifest types (tag, payload) MUST list the same set of files, but for older versions it SHOULD list all files
   */
  MANIFEST_SETS_DIFFER("manifest_file_sets_differ_between_algorithms"),
  /**
   * The bag payload contains a large number of files. This may cause validation to take long time.
   */
  LARGE_NUMBER_OF_FILES("payload_contains_large_number_of_files"),
  /**
   * The bag total size is very large. This may cause validation to take a long time.
   */
  LARGE_BAG_SIZE("bag_is_very_large"),
  /**
   * The bag contains a large number of manifests which may cause validation to take a long time.
   */
  LARGE_NUMBER_OF_MANIFESTS("large_number_of_manifests");
  
  private final String messageBundleKey;
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  
  BagitWarning(final String reason){
    this.messageBundleKey = reason;
  }

  public String getReason() {
    return messages.getString(messageBundleKey);
  }
}
