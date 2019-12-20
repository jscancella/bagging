package com.github.jscancella.conformance;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.jscancella.conformance.exceptions.BagitVersionIsNotAcceptableException;
import com.github.jscancella.conformance.exceptions.FetchFileNotAllowedException;
import com.github.jscancella.conformance.exceptions.MetatdataValueIsNotAcceptableException;
import com.github.jscancella.conformance.exceptions.MetatdataValueIsNotRepeatableException;
import com.github.jscancella.conformance.exceptions.RequiredManifestNotPresentException;
import com.github.jscancella.conformance.exceptions.RequiredMetadataFieldNotPresentException;
import com.github.jscancella.conformance.exceptions.RequiredTagFileNotPresentException;
import com.github.jscancella.conformance.internal.BagProfileChecker;
import com.github.jscancella.conformance.internal.EncodingChecker;
import com.github.jscancella.conformance.internal.LargeBagChecker;
import com.github.jscancella.conformance.internal.ManifestChecker;
import com.github.jscancella.conformance.internal.MetadataChecker;
import com.github.jscancella.conformance.internal.VersionChecker;
import com.github.jscancella.conformance.profile.BagitProfile;
import com.github.jscancella.domain.Bag;
import com.github.jscancella.domain.Version;
import com.github.jscancella.exceptions.InvalidBagMetadataException;
import com.github.jscancella.exceptions.InvalidBagitFileFormatException;
import com.github.jscancella.exceptions.MaliciousPathException;
import com.github.jscancella.exceptions.UnparsableVersionException;
import com.github.jscancella.reader.internal.BagitTextFileReader;
import com.github.jscancella.reader.internal.KeyValueReader;

public enum BagLinter {
  ; // using enum to ensure singleton
  private static final Logger logger = LoggerFactory.getLogger(BagLinter.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");

  /**
   * Check a bag against a bagit-profile as described by <a href=
   * "https://github.com/ruebot/bagit-profiles">https://github.com/ruebot/bagit-profiles</a>
   * <br>
   * Note: <b> This implementation does not check the Serialization part of the
   * profile!</b>
   * 
   * 
   * @param jsonProfile the conformance profile to check the bag against
   * @param bag the bag to check against the conformance profile
   * 
   * @return true if the bag meets the conformance profile
   * 
   * @throws JsonParseException if there is an error parsing the conformance profile
   * @throws JsonMappingException if there is an error mapping the parsed profile to a {@link BagitProfile}
   * @throws IOException If there is an error while reading the bag
   * 
   * @throws FetchFileNotAllowedException If the bag contains a fetch file but the profile forbids it
   * @throws RequiredMetadataFieldNotPresentException If the bag is missing a required metadata field
   * @throws MetatdataValueIsNotAcceptableException if the metadata value present in the bag is not in the list of acceptable values from the profile
   * @throws RequiredManifestNotPresentException if the manifest present doesn't use the required checksum algorithm from the profile
   * @throws BagitVersionIsNotAcceptableException if the bag is too old
   * @throws RequiredTagFileNotPresentException if a tag file is missing
   * @throws MetatdataValueIsNotRepeatableException if there is a repeat of metadata in the bag
   */
  public static boolean checkAgainstProfile(final InputStream jsonProfile, final Bag bag)
      throws JsonParseException, JsonMappingException, IOException, FetchFileNotAllowedException,
      RequiredMetadataFieldNotPresentException, MetatdataValueIsNotAcceptableException,
      RequiredManifestNotPresentException,
      BagitVersionIsNotAcceptableException, RequiredTagFileNotPresentException, MetatdataValueIsNotRepeatableException{
    BagProfileChecker.bagConformsToProfile(jsonProfile, bag);

    return true;
  }

  /**
   * The BagIt specification is very flexible in what it allows which leads to
   * situations where something may be technically allowed, but should be
   * discouraged. This method checks a bag for potential problems, or other items
   * that are allowed but discouraged. This <strong>does not</strong> validate a
   * bag.
   * 
   * @param rootDir the directory that contains a bag
   * 
   * @return a set of warnings that were found in the bag
   * 
   * @throws IOException if there was a problem reading a bag file
   * @throws UnparsableVersionException if there was a problem parsing the version of the bag
   * @throws InvalidBagitFileFormatException if a file is not formatted correctly
   * @throws MaliciousPathException if the bag is trying to be malicious
   * @throws NoSuchAlgorithmException 
   */
  public static Set<BagitWarning> lintBag(final Path rootDir) throws IOException, UnparsableVersionException, InvalidBagitFileFormatException, MaliciousPathException, NoSuchAlgorithmException{
    return lintBag(rootDir, Collections.emptyList());
  }

  /**
   * The BagIt specification is very flexible in what it allows which leads to
   * situations where something may be technically allowed, but should be
   * discouraged. This method checks a bag for potential problems, or other items
   * that are allowed but discouraged. This <strong>does not</strong> validate a
   * bag. See {@link BagVerifier} instead.
   * 
   * @param bagitDir the firectory that contains a bag
   * @param warningsToIgnore a collection of warnings you would like the linter to ignore
   * 
   * @return a set of warnings that were found in the bag
   * 
   * @throws IOException if there was a problem reading a bag file
   * @throws UnparsableVersionException if there was a problem parsing the version of the bag
   * @throws InvalidBagitFileFormatException if a file is not formatted correctly
   * @throws MaliciousPathException if the bag is trying to be maliciou
   * @throws NoSuchAlgorithmException 
   */
  public static Set<BagitWarning> lintBag(final Path bagitDir, final Collection<BagitWarning> warningsToIgnore) throws IOException, UnparsableVersionException, InvalidBagitFileFormatException, MaliciousPathException, NoSuchAlgorithmException{
    final Set<BagitWarning> warnings = new HashSet<>();

    final Path bagitFile = bagitDir.resolve("bagit.txt");
    checkForExtraLines(bagitFile, warnings, warningsToIgnore);
    final SimpleImmutableEntry<Version, Charset> bagitInfo = BagitTextFileReader.readBagitTextFile(bagitFile);

    logger.info(messages.getString("checking_encoding_problems"));
    EncodingChecker.checkEncoding(bagitInfo.getValue(), warnings, warningsToIgnore);

    logger.info(messages.getString("checking_latest_version"));
    VersionChecker.checkVersion(bagitInfo.getKey(), warnings, warningsToIgnore);
    
    logger.info(messages.getString("checking_size"));
    LargeBagChecker.checkForLargeBag(bagitDir, warnings, warningsToIgnore);

    logger.info(messages.getString("checking_manifest_problems"));
    ManifestChecker.checkManifests(bagitDir, bagitInfo.getValue(), warnings, warningsToIgnore);

    logger.info(messages.getString("checking_metadata_problems"));
    MetadataChecker.checkBagMetadata(bagitDir, bagitInfo.getValue(), warnings, warningsToIgnore);

    return warnings;
  }
  
  /*
   * After version 1.0 the specification read that the bagit.txt MUST contain EXACTLY 2 lines
   */
  private static void checkForExtraLines(final Path bagitFile, final Collection<BagitWarning> warnings, final Collection<BagitWarning> warningsToIgnore) throws InvalidBagMetadataException, IOException, UnparsableVersionException {
    if(warningsToIgnore.contains(BagitWarning.EXTRA_LINES_IN_BAGIT_FILES)){
      logger.debug(messages.getString("skipping_check_extra_lines"));
      return;
    }
    
    logger.debug(messages.getString("checking_extra_lines"));
    final List<SimpleImmutableEntry<String, String>> pairs = KeyValueReader.readKeyValuesFromFile(bagitFile, ":", StandardCharsets.UTF_8);
     
    for(final SimpleImmutableEntry<String, String> pair : pairs){
      if("BagIt-Version".equals(pair.getKey())){
        final Version version = BagitTextFileReader.parseVersion(pair.getValue());
        //versions after 1.0 specified it must be exactly 2 lines
        if(pairs.size() > 2 && version.isSameOrNewer(Version.VERSION_1_0())){
          logger.warn(messages.getString("extra_lines_warning"), pairs.size());
          warnings.add(BagitWarning.EXTRA_LINES_IN_BAGIT_FILES);
        }
      }
    }
  }
}
