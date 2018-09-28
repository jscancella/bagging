package com.github.jscancella.verify;

import com.github.jscancella.domain.Bag;

public enum BagVerifier {;//using enum to ensure singleton
  /**
   * Quickly verify by comparing the number of files and the total number of bytes expected. Returns true if unable to quickly verify.
   */
  public static boolean quicklyVerify(final Bag bag) {
    //TODO
    return false;
  }
  
  /**
   * See <a href="https://tools.ietf.org/html/draft-kunze-bagit#section-3">https://tools.ietf.org/html/draft-kunze-bagit#section-3</a><br>
   *  A bag is <b>valid</b> if the bag is complete and every checksum has been 
   *  verified against the contents of its corresponding file.
   */
  public static boolean verify(final Bag bag) {
    //TODO
    return false;
  }
  
  /**
   * See <a href="https://tools.ietf.org/html/draft-kunze-bagit#section-3">https://tools.ietf.org/html/draft-kunze-bagit#section-3</a><br>
   * A bag is <b>complete</b> if <br>
   * <ul>
   * <li>every element is present
   * <li>every file in the payload manifest(s) are present
   * <li>every file in the tag manifest(s) are present. Tag files not listed in a tag manifest may be present.
   * <li>every file in the data directory must be listed in at least one payload manifest
   * <li>each element must comply with the bagit spec
   * </ul>
   */
  public static boolean isComplete(final Bag bag) {
    //TODO
    return false;
  }
}
