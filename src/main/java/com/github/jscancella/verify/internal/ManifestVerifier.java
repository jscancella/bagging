package com.github.jscancella.verify.internal;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import com.github.jscancella.domain.Bag;
import com.github.jscancella.domain.Manifest;
import com.github.jscancella.domain.Version;
import com.github.jscancella.exceptions.FileNotInPayloadDirectoryException;
import com.github.jscancella.exceptions.InvalidBagitFileFormatException;
import com.github.jscancella.exceptions.MaliciousPathException;
import com.github.jscancella.internal.ManifestFilter;
import com.github.jscancella.reader.internal.ManifestReader;

/**
 * Responsible for all things related to the manifest during verification.
 */
public enum ManifestVerifier {; //using enum to enforce singleton
  private static final Logger logger = LoggerFactory.getLogger(ManifestVerifier.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");

  /**
   * Verify that all the files in the payload directory are listed in the payload manifest and 
   * all files listed in all manifests exist.
   * 
   * @param bag the bag which contains the manifests to check
   * @param ignoreHiddenFiles to include hidden files when checking
   * 
   * @throws IOException if there is an error while reading a file from the filesystem
   * @throws MaliciousPathException if a path is outside the bag
   * @throws InvalidBagitFileFormatException if a manifest is not formatted correctly
   * @throws FileNotInPayloadDirectoryException if a file listed in a manifest is not in the payload directory
   */
  public static void verifyManifests(final Bag bag, final boolean ignoreHiddenFiles)throws IOException{
    
    final Set<Path> allFilesListedInManifests = getAllFilesListedInManifests(bag);
    checkAllFilesListedInManifestExist(allFilesListedInManifests);

    if (bag.getVersion().isOlder(Version.VERSION_1_0())) {
      checkAllFilesInPayloadDirAreListedInAtLeastOneAManifest(allFilesListedInManifests, bag.getDataDir(), ignoreHiddenFiles);
    } else {
      CheckAllFilesInPayloadDirAreListedInAllManifests(bag, ignoreHiddenFiles);
    }
  }

  /*
   * get the full path (absolute) of all the files listed in all the manifests
   */
  private static Set<Path> getAllFilesListedInManifests(final Bag bag) throws IOException {
    logger.debug(messages.getString("all_files_in_manifests"));
    
    final Set<Path> filesListedInManifests = new HashSet<>();

    try(DirectoryStream<Path> directoryStream = Files.newDirectoryStream(bag.getTagFileDir(), new ManifestFilter())){
      for(final Path path : directoryStream) {
        logger.debug(messages.getString("get_listing_in_manifest"), path);
        final Manifest manifest = ManifestReader.readManifest(path, bag.getRootDir(),bag.getFileEncoding());
        filesListedInManifests.addAll(manifest.getEntries().stream().map(entry -> entry.getPhysicalLocation()).collect(Collectors.toList()));
      }
    }

    return filesListedInManifests;
  }

  /*
   * Make sure all the listed files actually exist
   */
  private static void checkAllFilesListedInManifestExist(final Set<Path> files) {
    logger.info(messages.getString("check_all_files_in_manifests_exist"));
    
    for (final Path file : files) {
      if(!Files.exists(file)){
        if(existsNormalized(file)){
          logger.warn(messages.getString("different_normalization_on_filesystem_warning"), file);
        }
        else{
          final String formattedMessage = messages.getString("missing_payload_files_error");
          throw new FileNotInPayloadDirectoryException(MessageFormatter.format(formattedMessage, file).getMessage());
        }
      }
    }
  }
  
  /**
   * if a file is parially normalized or of a different normalization then the manifest specifies it will fail the existence test.
   * This method checks for that by normalizing what is on disk with the normalized filename and see if they match.
   * 
   * @return true if the normalized filename matches one on disk in the specified folder
   */
  private static boolean existsNormalized(final Path file){
    boolean existsNormalized = false;
    final String normalizedFile = Normalizer.normalize(file.toString(), Normalizer.Form.NFD);
    final Path parent = file.getParent();
    if(parent != null){
      try(DirectoryStream<Path> files = Files.newDirectoryStream(parent)){
        for(final Path fileToCheck : files){
          final String normalizedFileToCheck = Normalizer.normalize(fileToCheck.toString(), Normalizer.Form.NFD);
          if(normalizedFile.equals(normalizedFileToCheck)){
            existsNormalized = true;
            break;
          }
        }
      }
      catch(IOException e){
        logger.error(messages.getString("error_reading_normalized_file"), parent, normalizedFile, e);
      }
    }
    
    return existsNormalized;
  }

  /*
   * Make sure all files in the directory are in at least 1 manifest
   */
  private static void checkAllFilesInPayloadDirAreListedInAtLeastOneAManifest(final Set<Path> filesListedInManifests, final Path payloadDir, final boolean ignoreHiddenFiles) throws IOException {
    logger.debug(messages.getString("checking_file_in_at_least_one_manifest"), payloadDir);
    if (Files.exists(payloadDir)) {
      Files.walkFileTree(payloadDir, new PayloadFileExistsInAtLeastOneManifestVistor(filesListedInManifests, ignoreHiddenFiles));
    }
  }

  /*
   * as per the bagit-spec 1.0+ all files have to be listed in all manifests
   */
  private static void CheckAllFilesInPayloadDirAreListedInAllManifests(final Bag bag, final boolean ignoreHiddenFiles) throws IOException {
    logger.debug(messages.getString("checking_file_in_all_manifests"), bag.getDataDir());
    if (Files.exists(bag.getDataDir())) {
      Files.walkFileTree(bag.getDataDir(), new PayloadFileExistsInAllManifestsVistor(bag.getPayLoadManifests(), bag.getRootDir(), ignoreHiddenFiles));
    }
  }
}
