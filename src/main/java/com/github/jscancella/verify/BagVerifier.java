package com.github.jscancella.verify;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jscancella.domain.Bag;
import com.github.jscancella.domain.Manifest;
import com.github.jscancella.exceptions.CorruptChecksumException;
import com.github.jscancella.exceptions.FileNotInPayloadDirectoryException;
import com.github.jscancella.exceptions.InvalidBagitFileFormatException;
import com.github.jscancella.exceptions.InvalidPayloadOxumException;
import com.github.jscancella.exceptions.MaliciousPathException;
import com.github.jscancella.exceptions.MissingBagitFileException;
import com.github.jscancella.exceptions.MissingPayloadDirectoryException;
import com.github.jscancella.exceptions.MissingPayloadManifestException;
import com.github.jscancella.exceptions.PayloadOxumDoesNotExistException;
import com.github.jscancella.hash.BagitChecksumNameMapping;
import com.github.jscancella.hash.Hasher;
import com.github.jscancella.verify.internal.MandatoryVerifier;
import com.github.jscancella.verify.internal.ManifestVerifier;
import com.github.jscancella.verify.internal.QuickVerifier;

public enum BagVerifier { ;// using enum to ensure singleton
  private static final Logger logger = LoggerFactory.getLogger(QuickVerifier.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");

  /**
   * Quickly verify by comparing the number of files and the total number of bytes
   * expected. Returns false if unable to quickly verify.
   * 
   * @throws IOException if there is an error reading a file
   * @throws InvalidPayloadOxumException if either the total bytes or the number of files 
   * calculated for the payload directory of the bag is different than the supplied values
   * @throws PayloadOxumDoesNotExistException if the bag does not contain a payload-oxum.
   * 
   * @deprecated Since 6.0, not recommended to use since Payload-Oxum is kinda a hack. 
   * Bagit specification 2.0 might move this to part of bagit.txt, if that happens this will be updated and deprecation will be removed.
   */
  @Deprecated
  public static void quicklyVerify(final Bag bag) throws IOException, InvalidPayloadOxumException, PayloadOxumDoesNotExistException{
    QuickVerifier.quicklyVerify(bag);
  }

  /*
   * See <a href=
   * "https://tools.ietf.org/html/draft-kunze-bagit#section-3">https://tools.ietf.org/html/draft-kunze-bagit#section-3</a><br>
   * A bag is <b>valid</b> if the bag is complete and every checksum has been
   * verified against the contents of its corresponding file.
   * 
   */
  public static boolean isValid(final Bag bag, final boolean ignoreHiddenFiles)
      throws FileNotInPayloadDirectoryException, MissingBagitFileException, MissingPayloadDirectoryException,
      MissingPayloadManifestException, IOException, MaliciousPathException,
      InvalidBagitFileFormatException, NoSuchAlgorithmException, CorruptChecksumException{

    boolean isValid = true;

    logger.info(messages.getString("checking_bag_is_valid"), bag.getRootDir());
    isValid = isComplete(bag, ignoreHiddenFiles) && isValid;

    logger.debug(messages.getString("checking_payload_checksums"));
    for(final Manifest payloadManifest : bag.getPayLoadManifests()){
      isValid = checkHashes(payloadManifest) && isValid;
    }

    logger.debug(messages.getString("checking_tag_file_checksums"));
    for(final Manifest tagManifest : bag.getTagManifests()){
      isValid = checkHashes(tagManifest) && isValid;
    }

    return isValid;
  }

  private static boolean checkHashes(final Manifest manifest) throws CorruptChecksumException, NoSuchAlgorithmException, IOException{
    final Hasher hasher = BagitChecksumNameMapping.get(manifest.getBagitAlgorithmName());

    for(Entry<Path, String> entry : manifest.getFileToChecksumMap().entrySet()){
      if (Files.exists(entry.getKey())){
        logger.debug(messages.getString("checking_checksums"), entry.getKey(), entry.getValue());

        final String hash = hasher.hash(entry.getKey());
        logger.debug("computed hash [{}] for file [{}]", hash, entry.getKey());
        if (!hash.equals(entry.getValue())){
          throw new CorruptChecksumException(messages.getString("corrupt_checksum_error"), entry.getKey(),
              manifest.getBagitAlgorithmName(), entry.getValue(), hash);
        }
      }
    }

    return false;
  }

  /*
   * See <a href=
   * "https://tools.ietf.org/html/draft-kunze-bagit#section-3">https://tools.ietf.org/html/draft-kunze-bagit#section-3</a><br>
   * A bag is <b>complete</b> if <br>
   * <ul>
   * <li>every element is present
   * <li>every file in the payload manifest(s) are present
   * <li>every file in the tag manifest(s) are present. Tag files not listed in a
   * tag manifest may be present.
   * <li>every file in the data directory must be listed in at least one payload
   * manifest
   * <li>each element must comply with the bagit spec
   * </ul>
   */
  public static boolean isComplete(final Bag bag, final boolean ignoreHiddenFiles)
      throws FileNotInPayloadDirectoryException, MissingBagitFileException, MissingPayloadDirectoryException,
      MissingPayloadManifestException, IOException, MaliciousPathException,
      InvalidBagitFileFormatException{

    logger.info(messages.getString("checking_bag_is_complete"), bag.getRootDir());

    MandatoryVerifier.checkFetchItemsExist(bag.getItemsToFetch(), bag.getRootDir());
    MandatoryVerifier.checkBagitFileExists(bag);
    MandatoryVerifier.checkPayloadDirectoryExists(bag);
    MandatoryVerifier.checkIfAtLeastOnePayloadManifestsExist(bag);

    ManifestVerifier.verifyManifests(bag, ignoreHiddenFiles);

    return true;
  }
}
