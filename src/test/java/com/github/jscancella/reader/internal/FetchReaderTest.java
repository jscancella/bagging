package com.github.jscancella.reader.internal;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import com.github.jscancella.TempFolderTest;
import com.github.jscancella.domain.FetchItem;
import com.github.jscancella.domain.Version;
import com.github.jscancella.exceptions.InvalidBagitFileFormatException;
import com.github.jscancella.exceptions.MaliciousPathException;

public class FetchReaderTest extends TempFolderTest {

  private static List<URI> uris;
  
  @BeforeAll
  public static void setup(){
    uris = Arrays.asList(URI.create("http://localhost/foo/data/dir1/test3.txt"), 
        URI.create("http://localhost/foo/data/dir2/dir3/test5.txt"),
        URI.create("http://localhost/foo/data/dir2/test4.txt"),
        URI.create("http://localhost/foo/data/test%201.txt"),
        URI.create("http://localhost/foo/data/test2.txt"));
  }
  
  @Test
  public void testReadFetchWithNoSizeSpecified() throws Exception{
    Path fetchFile = Paths.get(getClass().getClassLoader().getResource("fetchFiles/fetchWithNoSizeSpecified.txt").toURI());
    List<FetchItem> returnedItems = FetchReader.readFetch(fetchFile, StandardCharsets.UTF_8, fetchFile.getParent(), Version.VERSION_1_0());
    
    for(FetchItem item : returnedItems){
      Assertions.assertNotNull(item.getUri());
      Assertions.assertTrue(uris.contains(item.getUri()));
      
      Assertions.assertEquals(Long.valueOf(-1), item.getLength());
      
      Assertions.assertNotNull(item.getPath());
    }
  }
  
  @Test
  public void testReadFetchWithSizeSpecified() throws Exception{
    Path fetchFile = Paths.get(getClass().getClassLoader().getResource("fetchFiles/fetchWithSizeSpecified.txt").toURI());
    List<FetchItem> returnedItems = FetchReader.readFetch(fetchFile, StandardCharsets.UTF_8, Paths.get("/foo"), Version.VERSION_1_0());
    
    for(FetchItem item : returnedItems){
      Assertions.assertNotNull(item.getUri());
      Assertions.assertTrue(uris.contains(item.getUri()));
      
      Assertions.assertTrue(item.getLength() > 0);
      
      Assertions.assertNotNull(item.getPath());
    }
  }
  
  @Test
  public void testReadBlankLinesThrowsException() throws Exception{
    Path fetchFile = Paths.get(getClass().getClassLoader().getResource("fetchFiles/fetchWithBlankLines.txt").toURI());
    Assertions.assertThrows(InvalidBagitFileFormatException.class, 
        () -> { FetchReader.readFetch(fetchFile, StandardCharsets.UTF_8, Paths.get("/foo"), Version.VERSION_1_0()); });
  }
  
  @Test
  public void testReadWindowsSpecialDirMaliciousFetchThrowsException() throws Exception{
    Path fetchFile = Paths.get(getClass().getClassLoader().getResource("maliciousFetchFile/windowsSpecialDirectoryName.txt").toURI());
    Assertions.assertThrows(InvalidBagitFileFormatException.class, 
        () -> { FetchReader.readFetch(fetchFile, StandardCharsets.UTF_8, Paths.get("/foo"), Version.VERSION_1_0()); });
  }
  
  @Test
  public void testReadUpADirMaliciousFetchThrowsException() throws Exception{
    Path fetchFile = Paths.get(getClass().getClassLoader().getResource("maliciousFetchFile/upAdirectoryReference.txt").toURI());
    Assertions.assertThrows(MaliciousPathException.class, 
        () -> { FetchReader.readFetch(fetchFile, StandardCharsets.UTF_8, Paths.get("/bar"), Version.VERSION_1_0()); });
  }
  
  @Test
  public void testReadTildeFetchThrowsException() throws Exception{
    Path fetchFile = Paths.get(getClass().getClassLoader().getResource("maliciousFetchFile/tildeReference.txt").toURI());
    Assertions.assertThrows(MaliciousPathException.class, 
        () -> { FetchReader.readFetch(fetchFile, StandardCharsets.UTF_8, Paths.get("/bar"), Version.VERSION_1_0()); });
  }
  
  @Test
  @EnabledOnOs(OS.WINDOWS)
  public void testReadFileUrlMaliciousFetchThrowsException() throws Exception{
    Path fetchFile = Paths.get(getClass().getClassLoader().getResource("maliciousFetchFile/fileUrl.txt").toURI());
    Assertions.assertThrows(MaliciousPathException.class, 
        () -> { FetchReader.readFetch(fetchFile, StandardCharsets.UTF_8, Paths.get("/bar"), Version.VERSION_1_0()); });
  }
  
  @Test
  public void foo(){
    String regex = ".*[ \t]*(\\d*|-)[ \t]*.*";
    String test1 = "http://localhost/foo/data/test2.txt - ~/foo/bar/ham.txt";
    String test2 = "http://localhost/foo/data/dir1/test3.txt 100057 data/dir1/test3.txt";
    String test3 = "http://localhost/foo/data/dir1/test3.txt \t 100057 \t data/dir1/test3.txt";
    
    System.err.println(test1.matches(regex));
    System.err.println(test2.matches(regex));
    System.err.println(test3.matches(regex));
  }
}
