package com.github.jscancella.verify.internal;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import com.github.jscancella.exceptions.FileNotInManifestException;

/**
 * Implements {@link SimpleFileVisitor} to ensure that the encountered file is in one of the manifests.
 */
@SuppressWarnings({"PMD.BeanMembersShouldSerialize"})
public final class PayloadFileExistsInAtLeastOneManifestVistor extends AbstractPayloadFileExistsInManifestsVistor {
  private static final Logger logger = LoggerFactory.getLogger(PayloadFileExistsInAtLeastOneManifestVistor.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  private final Set<Path> filesListedInManifests;

  /**
   * Implements {@link SimpleFileVisitor} to ensure that the encountered file is in one of the manifests.
   * 
   * @param filesListedInManifests the set of files listed in all the manifests
   * @param ignoreHiddenFiles if the checker should ignore hidden files or not
   */
  public PayloadFileExistsInAtLeastOneManifestVistor(final Set<Path> filesListedInManifests, final boolean ignoreHiddenFiles) {
    super(ignoreHiddenFiles);
    this.filesListedInManifests = new HashSet<>(filesListedInManifests);
  }

  @Override
  public FileVisitResult visitFile(final Path path, final BasicFileAttributes attrs)throws IOException{
	if(Files.isHidden(path) && ignoreHiddenFiles){
	  logger.debug(messages.getString("skipping_hidden_file"), path);
  }
	else {
	  if(Files.isRegularFile(path) && !filesListedInManifests.contains(path.toAbsolutePath())){
      final String formattedMessage = messages.getString("file_not_in_any_manifest_error");
      throw new FileNotInManifestException(MessageFormatter.format(formattedMessage, path).getMessage());
    }
    logger.debug(messages.getString("file_in_at_least_one_manifest"), path);
	}
	return FileVisitResult.CONTINUE;
  }

}
