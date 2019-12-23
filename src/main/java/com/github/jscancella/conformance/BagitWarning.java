package com.github.jscancella.conformance;

import java.util.ResourceBundle;

/**
 * The BagIt specification is very flexible in what it allows. 
 * This leads to situations where something may be technically allowed, but should be discouraged. 
 * This class is for that purpose, to allow reporting of these allowed but discouraged situations to the user.
 */
public enum BagitWarning {
  BAG_WITHIN_A_BAG("bag_within_a_bag"),
  DIFFERENT_CASE("different_case"),
  DIFFERENT_NORMALIZATION("different_normalization"),
  EXTRA_LINES_IN_BAGIT_FILES("extra_lines_in_bagit_files"),
  LEADING_DOT_SLASH("leading_dot_slash"),
  NON_STANDARD_ALGORITHM("non_standard_algorithm"),
  MD5SUM_TOOL_GENERATED_MANIFEST("md5sum_tool_generated_manifest"),
  MISSING_TAG_MANIFEST("missing_tag_manifest"),
  OLD_BAGIT_VERSION("old_bagit_version"),
  OS_SPECIFIC_FILES("os_specific_files"),
  PAYLOAD_OXUM_MISSING("payload_oxum_missing"),
  TAG_FILES_ENCODING("tag_files_encoding"),
  WEAK_CHECKSUM_ALGORITHM("weak_checksum_algorithm"),
  MANIFEST_SETS_DIFFER("manifest_file_sets_differ_between_algorithms"),
  LARGE_NUMBER_OF_FILES("payload_contains_large_number_of_files"),
  LARGE_BAG_SIZE("bag_is_very_large"),
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
