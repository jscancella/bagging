package com.github.jscancella.domain;

import java.net.URI;
import java.nio.file.Path;
import java.util.Objects;

/**
 * An individual item to fetch as specified by 
 * <a href="https://tools.ietf.org/html/draft-kunze-bagit-13#section-2.2.3">https://tools.ietf.org/html/draft-kunze-bagit-13#section-2.2.3</a>
 */
public final class FetchItem {
  /**
   * The uri from which the item can be downloaded
   */
  private final URI uri;
  
  /**
   * The length of the file in octets
   */
  private final Long length; 
  
  /**
   * The path where the fetched item should be put
   */
  private final Path path;
  
  private final String cachedString;
  
  /**
   * 
   * @param uri the {@link URI} of the file
   * @param length the file length in bytes, -1 or null to not specify the length
   * @param path the path in the bag where the file belongs
   */
  public FetchItem(final URI uri, final Long length, final Path path){
    this.uri = URI.create(uri.toString());
    this.length = length;
    this.path = path;
    this.cachedString = internalToString();
  }
  
  private String internalToString() {
    final StringBuilder builder = new StringBuilder();
    builder.append(uri).append(' ');
    
    if(length == null || length < 0){
      builder.append("- ");
    }
    else{
      builder.append(length).append(' ');
    }
    
    builder.append(path);
      
    return builder.toString();
  }

  @Override
  public String toString() {
    return cachedString;
  }

  /**
   * @return the URI part of a fetch item
   */
  public URI getUri() {
    return uri;
  }

  /**
   * @return the length in bytes of the file
   */
  public Long getLength() {
    return length;
  }

  /**
   * @return the path in the bag it should be downloaded to
   */
  public Path getPath() {
    return path;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(uri) + Objects.hash(length) + Objects.hash(path);
  }

  @Override
  public boolean equals(final Object obj) {
    boolean isEqual = false;
    if (obj instanceof FetchItem){
      final FetchItem other = (FetchItem) obj;
      isEqual = Objects.equals(uri, other.getUri()) && Objects.equals(length, other.getLength()) && Objects.equals(path, other.getPath()); 
    }
    
    return isEqual;
  }
}
