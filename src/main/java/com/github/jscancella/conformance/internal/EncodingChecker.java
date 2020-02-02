package com.github.jscancella.conformance.internal;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jscancella.conformance.BagitWarning;

/**
 * Part of the BagIt conformance suite. 
 * This checker gives a warning if a file is not using UTF-8 encoding which is standard on most filesystems today.
 */
public enum EncodingChecker {;// using enum to enforce singleton
  private static final Logger logger = LoggerFactory.getLogger(EncodingChecker.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  
  /**
   * It is now normal for all files to be UTF-8
   * 
   * @param encoding the encoding to check
   * @param warnings the list of warnings to add to if the encoding doesn't conform
   * @param warningsToIgnore the list of warnings to ignore
   */
  public static void checkEncoding(final Charset encoding, final Set<BagitWarning> warnings, final Collection<BagitWarning> warningsToIgnore){
    if(!warningsToIgnore.contains(BagitWarning.TAG_FILES_ENCODING) && !StandardCharsets.UTF_8.equals(encoding)){
      logger.warn(messages.getString("tag_files_not_encoded_with_utf8_warning"), encoding);
      warnings.add(BagitWarning.TAG_FILES_ENCODING);
    }
  }
}
