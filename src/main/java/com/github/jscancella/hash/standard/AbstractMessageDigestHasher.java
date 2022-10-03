package com.github.jscancella.hash.standard;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import com.github.jscancella.exceptions.HasherInitializationException;
import com.github.jscancella.hash.Hasher;

/**
 * A {@link MessageDigest} based hasher, where all you need to implement are the algorithm to bagit algorithm name mapping
 */
public abstract class AbstractMessageDigestHasher implements Hasher{
  private static final int _64_KB = 1024 * 64;
  private static final int CHUNK_SIZE = _64_KB;
  @SuppressWarnings("PMD.AvoidMessageDigestField") //unavoidable because we need to be able to stream big files to multiple hashers at once
  private transient MessageDigest messageDigestInstance;
  private final String messageDigestName;
  private final String BagitAlgorithmName;
  
  /**
   * You must call this constructor before using
   * 
   * @param messageDigestName the {@link MessageDigest} name
   * @param bagitAlgorithmName the bagit algorithm name that matches the messageDigestName
   */
  protected AbstractMessageDigestHasher(final String messageDigestName, final String bagitAlgorithmName) {
    this.messageDigestName = messageDigestName;
    this.BagitAlgorithmName = bagitAlgorithmName;
  }
  
  @Override
  public String hash(final Path path) throws IOException{
    reset();
    updateMessageDigest(path, messageDigestInstance);
    return formatMessageDigest(messageDigestInstance);
  }
  
  private static void updateMessageDigest(final Path path, final MessageDigest messageDigest) throws IOException{
    try(InputStream inputStream = new BufferedInputStream(Files.newInputStream(path, StandardOpenOption.READ))){
      final byte[] buffer = new byte[CHUNK_SIZE];
      int read = inputStream.read(buffer);

      while(read != -1){
        messageDigest.update(buffer, 0, read);
        read = inputStream.read(buffer);
      }
    }
  }
  
  private static String formatMessageDigest(final MessageDigest messageDigest){
    try(Formatter formatter = new Formatter()){
      for (final byte b : messageDigest.digest()) {
        formatter.format("%02x", b);
      }
      
      return formatter.toString();
    }
  }
  
  @Override
  public String getHash(){
    return formatMessageDigest(messageDigestInstance);
  }

  @Override
  public void update(final byte[] bytes){
    messageDigestInstance.update(bytes);
  }

  @Override
  public void reset(){
    messageDigestInstance.reset();
  }

  @Override
  public String getBagitAlgorithmName(){
    return BagitAlgorithmName;
  }
  
  @Override
  public void initialize(){
    try {
      messageDigestInstance = MessageDigest.getInstance(messageDigestName);
    } catch (NoSuchAlgorithmException e) {
      throw new HasherInitializationException(e);
    }
  }
}
