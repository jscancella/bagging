package com.github.jscancella.verify.internal;

import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.helpers.MessageFormatter;

import com.github.jscancella.domain.Manifest;
import com.github.jscancella.exceptions.FileNotInManifestException;

/**
 * Implements {@link SimpleFileVisitor} to ensure that the encountered file is in one of the manifests.
 */
public final class PayloadFileExistsInAllManifestsVistor extends AbstractPayloadFileExistsInManifestsVistor {
  private final Set<Manifest> manifests;
  private final Path rootDir;

  public PayloadFileExistsInAllManifestsVistor(final Set<Manifest> manifests, final Path rootDir, final boolean ignoreHiddenFiles) {
    super(ignoreHiddenFiles);
    this.manifests = manifests;
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
