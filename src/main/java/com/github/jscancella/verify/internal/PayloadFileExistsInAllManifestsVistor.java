package com.github.jscancella.verify.internal;

import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import com.github.jscancella.domain.Manifest;
import com.github.jscancella.exceptions.FileNotInManifestException;

/**
 * Implements {@link SimpleFileVisitor} to ensure that the encountered file is in one of the manifests.
 */
@SuppressWarnings({"PMD.BeanMembersShouldSerialize"})
public final class PayloadFileExistsInAllManifestsVistor extends AbstractPayloadFileExistsInManifestsVistor {
  private static final Logger logger = LoggerFactory.getLogger(PayloadFileExistsInAllManifestsVistor.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  private final Set<Manifest> manifests;
  private final Path rootDir;

  /**
   * Implements {@link SimpleFileVisitor} to ensure that the encountered file is in one of the manifests.
   * 
   * @param manifests the set of manifests to check
   * @param rootDir the root directory of the bag
   * @param ignoreHiddenFiles if the checker should ignore hidden files or not
   */
  public PayloadFileExistsInAllManifestsVistor(final Set<Manifest> manifests, final Path rootDir, final boolean ignoreHiddenFiles) {
    super(ignoreHiddenFiles);
    this.manifests = new HashSet<>(manifests);
    this.rootDir = rootDir;
  }

  @Override
  public FileVisitResult visitFile(final Path path, final BasicFileAttributes attrs){
    if(Files.isRegularFile(path)){
      for(final Manifest manifest : manifests){
        final Set<Path> relativePaths = manifest
                                    .getEntries().stream()
                                    .map(entry -> entry.getRelativeLocation())
                                    .collect(Collectors.toSet());
        final Path relativePath = rootDir.relativize(path);
        
        if(!relativePaths.contains(relativePath)){
          final String formattedMessage = messages.getString("file_not_in_manifest_error");
          throw new FileNotInManifestException(MessageFormatter.format(formattedMessage, path, manifest.getBagitAlgorithmName()).getMessage());
        }
      }
    }
    logger.debug(messages.getString("file_in_all_manifests"), path);
    return FileVisitResult.CONTINUE;
  }
}
