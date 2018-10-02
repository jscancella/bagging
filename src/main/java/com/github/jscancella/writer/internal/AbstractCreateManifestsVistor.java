package com.github.jscancella.writer.internal;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributes;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jscancella.domain.Manifest;
import com.github.jscancella.hash.Hasher;

/**
 * An implementation of the {@link SimpleFileVisitor} class that optionally avoids hidden files.
 * Mainly used in {@link BagCreator}
 */
public abstract class AbstractCreateManifestsVistor extends SimpleFileVisitor<Path>{
  private static final int _64_KB = 1024 * 64;
  private static final int CHUNK_SIZE = _64_KB;
  private static final Logger logger = LoggerFactory.getLogger(AbstractCreateManifestsVistor.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  
  protected transient final Map<Manifest, Hasher> manifestToHasherMap;
  protected transient final boolean includeHiddenFiles;
  
  public AbstractCreateManifestsVistor(final Map<Manifest, Hasher> manifestToHasherMap, final boolean includeHiddenFiles){
    this.manifestToHasherMap = manifestToHasherMap;
    this.includeHiddenFiles = includeHiddenFiles;
  }
  
  public FileVisitResult abstractPreVisitDirectory(final Path dir, final String directoryToIgnore) throws IOException {
    if(!includeHiddenFiles && isHidden(dir) && !dir.endsWith(Paths.get(".bagit"))){
      logger.debug(messages.getString("skipping_hidden_file"), dir);
      return FileVisitResult.SKIP_SUBTREE;
    }
    if(dir.endsWith(directoryToIgnore)){ 
      logger.debug(messages.getString("skipping_ignored_directory"), dir);
      return FileVisitResult.SKIP_SUBTREE;
    }
    
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFile(final Path path, final BasicFileAttributes attrs)throws IOException{
    try {
      if(!includeHiddenFiles && isHidden(path) && !path.endsWith(".keep")){
        logger.debug(messages.getString("skipping_hidden_file"), path);
      }
      else{
        streamFile(path);
        for(Entry<Manifest, Hasher> entry : manifestToHasherMap.entrySet()) {
          entry.getKey().getFileToChecksumMap().put(path, entry.getValue().getHash());
          entry.getValue().reset();
        }
      }
    }
    catch(NoSuchAlgorithmException e) {
      throw new IOException(e); //rethrow
    }
    
    return FileVisitResult.CONTINUE;
  }
  
  /**
   * Due to the way that windows handles hidden files vs. *nix 
   * we use this method to determine if a file or folder is really hidden
   * @param path the file or folder to check if hidden
   * @return if the file or folder is hidden
   * @throws IOException if there is an error reading the file/folder
   */
  private static boolean isHidden(final Path path) throws IOException{
    //cause Files.isHidden() doesn't work properly for windows if the file is a directory
    if (System.getProperty("os.name").contains("Windows")){
      return Files.readAttributes(path, DosFileAttributes.class).isHidden() || Files.isHidden(path);
    }

    return Files.isHidden(path);
  }
  
  private void streamFile(final Path path) throws IOException {
    try(final InputStream is = new BufferedInputStream(Files.newInputStream(path, StandardOpenOption.READ))){
      final byte[] buffer = new byte[CHUNK_SIZE];
      int read = is.read(buffer);

      while(read != -1){
        for(Hasher hasher : manifestToHasherMap.values()) {
          hasher.update(buffer, read);
        }
        read = is.read(buffer);
      }
    } catch(NoSuchAlgorithmException e){
      //rethrow as IO exception
      throw new IOException(e);
    }
  }
}
