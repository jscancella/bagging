package com.github.jscancella.writer.internal;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jscancella.domain.Manifest;
import com.github.jscancella.domain.ManifestEntry;

/**
 * Responsible for writing out a {@link Manifest} to the filesystem
 */
public enum ManifestWriter {
  ;// using enum to enforce singleton
  private static final Logger logger = LoggerFactory.getLogger(ManifestWriter.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");

  /**
   * Write the payload <b>manifest(s)</b> to the output directory
   * 
   * @param manifests
   *          the payload{@link Manifest}s to write out
   * @param outputDir
   *          the root of where the manifest is being written to
   * @param charsetName
   *          the name of the encoding for the file
   * @return the set of payload manifests that were just created
   * 
   * @throws IOException
   *           if there was a problem writing a file
   */
  public static Set<Path> writePayloadManifests(final Set<Manifest> manifests, final Path outputDir,
      final Charset charsetName) throws IOException{
    return writeManifests(manifests, outputDir, "manifest-", charsetName);
  }

  /**
   * Write the tag <b>manifest(s)</b> to the output directory
   * 
   * @param tagManifests
   *          the tag{@link Manifest}s to write out
   * @param outputDir
   *          the root of where the manifest is being written to
   * @param charsetName
   *          the name of the encoding for the file
   * @return the set of tag manifests that were just created
   * 
   * @throws IOException
   *           if there was a problem writing a file
   */
  public static Set<Path> writeTagManifests(final Set<Manifest> tagManifests, final Path outputDir,
      final Charset charsetName) throws IOException{
    return writeManifests(tagManifests, outputDir, "tagmanifest-", charsetName);
  }

  /*
   * Generic method to write manifests
   */
  private static Set<Path> writeManifests(final Set<Manifest> manifests, final Path outputDir,
      final String filenameBase, final Charset charsetName) throws IOException{
    final Set<Path> manifestFiles = new HashSet<>();

    for(final Manifest manifest : manifests){
      final Path manifestPath = outputDir.resolve(filenameBase + manifest.getBagitAlgorithmName() + ".txt");
      manifestFiles.add(manifestPath);
      logger.debug(messages.getString("writing_manifest_to_path"), manifestPath);

      try(BufferedWriter writer = Files.newBufferedWriter(manifestPath, charsetName,
          StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)){
        for(final ManifestEntry entry : manifest.getEntries()){
          // there are 2 spaces between the checksum and the path so that the manifests
          // are compatible with the md5sum tools available on most unix systems.
          // This may cause problems on windows due to it being text mode, in which case
          // either replace with a * or try verifying in binary mode with --binary
          final String line = entry.getChecksum() + "  "
              + RelativePathWriter.formatRelativePathString(entry.getRelativeLocation());
          logger.debug(messages.getString("writing_line_to_file"), line, manifestPath);
          writer.write(line);
        }
      }
    }

    return manifestFiles;
  }
}
