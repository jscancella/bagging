package com.github.jscancella.verify.internal;

import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;

import org.slf4j.helpers.MessageFormatter;

import com.github.jscancella.domain.Manifest;
import com.github.jscancella.exceptions.FileNotInManifestException;

/**
 * Implements {@link SimpleFileVisitor} to ensure that the encountered file is in one of the manifests.
 */
public final class PayloadFileExistsInAllManifestsVistor extends AbstractPayloadFileExistsInManifestsVistor {
  private transient final Set<Manifest> manifests;

  public PayloadFileExistsInAllManifestsVistor(final Set<Manifest> manifests, final boolean ignoreHiddenFiles) {
    super(ignoreHiddenFiles);
    this.manifests = manifests;
  }

  @Override
  public FileVisitResult visitFile(final Path path, final BasicFileAttributes attrs)throws FileNotInManifestException{
    if(Files.isRegularFile(path)){
      for(final Manifest manifest : manifests){
        if(!manifest.getFileToChecksumMap().keySet().contains(path.normalize())){
          final String formattedMessage = messages.getString("file_not_in_manifest_error");
          throw new FileNotInManifestException(MessageFormatter.format(formattedMessage, path, manifest.getBagitAlgorithmName()).getMessage());
        }
      }
    }
    logger.debug(messages.getString("file_in_all_manifests"), path);
    return FileVisitResult.CONTINUE;
  }
}
