package com.github.jscancella.conformance;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
import com.github.jscancella.conformance.internal.ManifestChecker;
import com.github.jscancella.conformance.internal.MetadataChecker;
import com.github.jscancella.conformance.internal.VersionChecker;
import com.github.jscancella.domain.Bag;
import com.github.jscancella.domain.Version;
import com.github.jscancella.exceptions.InvalidBagMetadataException;
import com.github.jscancella.exceptions.InvalidBagitFileFormatException;
import com.github.jscancella.exceptions.MaliciousPathException;
import com.github.jscancella.exceptions.UnparsableVersionException;
import com.github.jscancella.reader.internal.BagitTextFileReader;
import com.github.jscancella.reader.internal.KeyValueReader;
import com.github.jscancella.verify.BagVerifier;

public enum BagLinter {
  ; // using enum to ensure singleton
  private static final Logger logger = LoggerFactory.getLogger(BagLinter.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  private static final Version VERSION_1_0 = new Version(1,0);

  /**
   * Check a bag against a bagit-profile as described by <a href=
   * "https://github.com/ruebot/bagit-profiles">https://github.com/ruebot/bagit-profiles</a>
   * <br>
   * Note: <b> This implementation does not check the Serialization part of the
   * profile!</b>
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
   * bag. See {@link BagVerifier} instead.
   * 
   */
  public static Set<BagitWarning> lintBag(final Path rootDir) throws IOException, UnparsableVersionException, InvalidBagitFileFormatException, MaliciousPathException{
    return lintBag(rootDir, Collections.emptyList());
  }

  /**
   * The BagIt specification is very flexible in what it allows which leads to
   * situations where something may be technically allowed, but should be
   * discouraged. This method checks a bag for potential problems, or other items
   * that are allowed but discouraged. This <strong>does not</strong> validate a
   * bag. See {@link BagVerifier} instead.
   */
  public static Set<BagitWarning> lintBag(final Path rootDir, final Collection<BagitWarning> warningsToIgnore) throws IOException, UnparsableVersionException, InvalidBagitFileFormatException, MaliciousPathException{
    // TODO check number of manifests is < 50, check number of files in manifest is
    // < 1 million?
    // TODO
    final Set<BagitWarning> warnings = new HashSet<>();

    // @Incubating
    Path bagitDir = rootDir.resolve(".bagit");
    if (!Files.exists(bagitDir)){
      bagitDir = rootDir;
    }

    final Path bagitFile = bagitDir.resolve("bagit.txt");
    checkForExtraLines(bagitFile, warnings, warningsToIgnore);
    final SimpleImmutableEntry<Version, Charset> bagitInfo = BagitTextFileReader.readBagitTextFile(bagitFile);

    logger.info(messages.getString("checking_encoding_problems"));
    EncodingChecker.checkEncoding(bagitInfo.getValue(), warnings, warningsToIgnore);

    logger.info(messages.getString("checking_latest_version"));
    VersionChecker.checkVersion(bagitInfo.getKey(), warnings, warningsToIgnore);

    logger.info(messages.getString("checking_manifest_problems"));
    ManifestChecker.checkManifests(bagitInfo.getKey(), bagitDir, bagitInfo.getValue(), warnings, warningsToIgnore);

    logger.info(messages.getString("checking_metadata_problems"));
    MetadataChecker.checkBagMetadata(bagitDir, bagitInfo.getValue(), warnings, warningsToIgnore);

    return warnings;
  }
  
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
        //versions before 1.0 specified it must be exactly 2 lines
        if(pairs.size() > 2 && version.isOlder(VERSION_1_0)){
          logger.warn(messages.getString("extra_lines_warning"), pairs.size());
          warnings.add(BagitWarning.EXTRA_LINES_IN_BAGIT_FILES);
        }
      }
    }
  }
}
