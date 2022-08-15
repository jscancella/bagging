package com.github.jscancella.writer.internal;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jscancella.domain.FetchItem;
import com.github.jscancella.domain.Version;

/**
 * Responsible for writing out the list of {@link FetchItem} to the fetch.txt file on the filesystem
 */
public enum FetchWriter {;//using enum to enforce singleton
  private static final Logger logger = LoggerFactory.getLogger(FetchWriter.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  
  /**
   * Write the fetch.txt file to the outputDir with the specified encoding (charsetName)
   * 
   * @param itemsToFetch the list of {@link FetchItem}s to write into the fetch.txt
   * @param bagitRootDir the path to the root of the bag
   * @param version the version of the bag
   * @param charsetName the name of the encoding for the file
   * @return the path of the newly created fetch.txt file
   * 
   * @throws IOException if there was a problem writing a file
   */
  public static Path writeFetchFile(final List<FetchItem> itemsToFetch, final Path bagitRootDir, final Version version, final Charset charsetName) throws IOException{
    logger.debug(messages.getString("writing_fetch_file_to_path"), bagitRootDir);
    final Path fetchFilePath = bagitRootDir.resolve("fetch.txt");
    
    try(BufferedWriter writer = Files.newBufferedWriter(fetchFilePath, charsetName, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)){
      for(final FetchItem item : itemsToFetch){
        final String line = formatFetchLine(item, bagitRootDir, version, charsetName);
        logger.debug(messages.getString("writing_line_to_file"), line, fetchFilePath);
        writer.write(line);
      }
    }
    
    return fetchFilePath;
  }
  
  static String formatFetchLine(final FetchItem fetchItem, final Path bagitRootDir, final Version version, final Charset charset){
    final StringBuilder fetchLineBuilder = new StringBuilder();
    fetchLineBuilder.append(fetchItem.getUri()).append(' ');
    
    if(fetchItem.getLength() == null || fetchItem.getLength() < 0){
      fetchLineBuilder.append("- ");
    }
    else{
      fetchLineBuilder.append(fetchItem.getLength()).append(' ');
    }
    
    fetchLineBuilder.append(formatRelativePathString(bagitRootDir, fetchItem.getPath(), version, charset));
      
    return fetchLineBuilder.toString();
  }
  
  /**
   * Create a relative path that has \ (windows) path separator replaced with / and encodes newlines
   * 
   * @param relativeTo the path to remove from the entry
   * @param entry the path to make relative
   * @param version the version of the bag
   * @param the encoding that you wish to use when writing the fetch file
   * 
   * @return the relative path with only unix path separator
   */
  static String formatRelativePathString(final Path relativeTo, final Path entry, final Version version, final Charset charset){
    final String encodedPath = RelativePathWriter.encodeFilename(relativeTo.toAbsolutePath().relativize(entry.toAbsolutePath()), version, charset);
    return encodedPath.replace('\\', '/') + System.lineSeparator();
  }
}
