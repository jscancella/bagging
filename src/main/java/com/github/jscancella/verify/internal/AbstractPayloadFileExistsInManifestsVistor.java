package com.github.jscancella.verify.internal;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jscancella.internal.PathUtils;

/**
 * Implements {@link SimpleFileVisitor} to ensure that the encountered file is in one of the manifests.
 */
@SuppressWarnings({"PMD.BeanMembersShouldSerialize"})
abstract public class AbstractPayloadFileExistsInManifestsVistor extends SimpleFileVisitor<Path> {
  private static final Logger logger = LoggerFactory.getLogger(AbstractPayloadFileExistsInManifestsVistor.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  /**
   * Should hidden files be ignored
   */
  protected final boolean ignoreHiddenFiles;

  /**
   * constructor must be called before using!
   * 
   * @param ignoreHiddenFiles Should hidden files be ignored
   */
  public AbstractPayloadFileExistsInManifestsVistor(final boolean ignoreHiddenFiles) {
    super();
    this.ignoreHiddenFiles = ignoreHiddenFiles;
  }
  
  @Override
  public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
    FileVisitResult result = FileVisitResult.CONTINUE;
    
    if(ignoreHiddenFiles && PathUtils.isHidden(dir)){
      logger.debug(messages.getString("skipping_hidden_file"), dir);
      result = FileVisitResult.SKIP_SUBTREE;
    }
    
    return result;
  }
}
