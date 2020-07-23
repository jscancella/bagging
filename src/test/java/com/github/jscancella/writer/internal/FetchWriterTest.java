package com.github.jscancella.writer.internal;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.jscancella.TempFolderTest;
import com.github.jscancella.domain.FetchItem;

public class FetchWriterTest extends TempFolderTest {

  @Test
  public void testWriteFetchFile() throws Exception{
    Path rootPath = createDirectory("testWriteFetchFile");
    Path fetch = rootPath.resolve("fetch.txt");
    URI uri = URI.create("http://localhost:/foo/bar");
    List<FetchItem> itemsToFetch = Arrays.asList(new FetchItem(uri, -1l, rootPath.resolve("/data/foo/bar")),
        new FetchItem(uri, 100l, rootPath.resolve("/data/foo/bar")));
    
    
    Assertions.assertFalse(Files.exists(fetch));
    Path fetchFile = FetchWriter.writeFetchFile(itemsToFetch, rootPath, StandardCharsets.UTF_8);
    Assertions.assertTrue(Files.exists(fetchFile));
  }
  
  @Test
  public void testFetchFileIsFormattedCorrectly() throws Exception{
    Path rootPath = createDirectory("fetchFormatted");
    List<FetchItem> itemsToFetch = new ArrayList<>();
    
    itemsToFetch.add(new FetchItem(URI.create("http://localhost:8989/bags/v0_96/holey-bag/data/dir1/test3.txt"), null, rootPath.resolve("data/dir1/test3.txt")));
    itemsToFetch.add(new FetchItem(URI.create("http://localhost:8989/bags/v0_96/holey-bag/data/dir2/dir3/test5.txt"), null, rootPath.resolve("data/dir2/dir3/test5.txt")));
    itemsToFetch.add(new FetchItem(URI.create("http://localhost:8989/bags/v0_96/holey-bag/data/dir2/test4.txt"), null, rootPath.resolve("data/dir2/test4.txt")));
    itemsToFetch.add(new FetchItem(URI.create("http://localhost:8989/bags/v0_96/holey-bag/data/test%201.txt"), null, rootPath.resolve("data/test 1.txt")));
    itemsToFetch.add(new FetchItem(URI.create("http://localhost:8989/bags/v0_96/holey-bag/data/test2.txt"), null, rootPath.resolve("data/test2.txt")));
    
    Path fetch = FetchWriter.writeFetchFile(itemsToFetch, rootPath, StandardCharsets.UTF_8);
    
    List<String> expectedLines = Arrays.asList("http://localhost:8989/bags/v0_96/holey-bag/data/dir1/test3.txt - data/dir1/test3.txt", 
        "http://localhost:8989/bags/v0_96/holey-bag/data/dir2/dir3/test5.txt - data/dir2/dir3/test5.txt", 
        "http://localhost:8989/bags/v0_96/holey-bag/data/dir2/test4.txt - data/dir2/test4.txt", 
        "http://localhost:8989/bags/v0_96/holey-bag/data/test%201.txt - data/test 1.txt",
        "http://localhost:8989/bags/v0_96/holey-bag/data/test2.txt - data/test2.txt");
    List<String> actualLines = Files.readAllLines(fetch);
    
    Assertions.assertEquals(expectedLines, actualLines);
  }
  
  @Test
  public void testFormatFetchLineForNullLength() throws Exception{
    Path bagitRootDir = Paths.get("foo", "bar", "ham");
    FetchItem fetchItem = new FetchItem(new URI("https://www.hackaday.com/blog"), null, bagitRootDir.resolve("data").resolve("foo.txt"));
    String expected = "https://www.hackaday.com/blog - data/foo.txt" + System.lineSeparator();
    String actual = FetchWriter.formatFetchLine(fetchItem, bagitRootDir);
    
    Assertions.assertEquals(expected, actual);
  }
  
  @Test
  public void testFormatFetchLineForUnknownLength() throws Exception{
    Path bagitRootDir = Paths.get("foo", "bar", "ham");
    FetchItem fetchItem = new FetchItem(new URI("https://www.hackaday.com/blog"), -1l, bagitRootDir.resolve("data").resolve("foo.txt"));
    String expected = "https://www.hackaday.com/blog - data/foo.txt" + System.lineSeparator();
    String actual = FetchWriter.formatFetchLine(fetchItem, bagitRootDir);
    
    Assertions.assertEquals(expected, actual);
  }
  
  @Test
  public void testFormatFetchLineForKnownLength() throws Exception{
    Path bagitRootDir = Paths.get("foo", "bar", "ham");
    FetchItem fetchItem = new FetchItem(new URI("https://www.hackaday.com/blog"), 100l, bagitRootDir.resolve("data").resolve("foo.txt"));
    String expected = "https://www.hackaday.com/blog 100 data/foo.txt" + System.lineSeparator();
    String actual = FetchWriter.formatFetchLine(fetchItem, bagitRootDir);
    
    Assertions.assertEquals(expected, actual);
  }
  
  @Test
  public void testRelativePath(){
    Path parent = Paths.get("/foo");
    Path child = parent.resolve("bar/ham");
    String expectedRelativePath = "bar/ham" + System.lineSeparator();
    
    Assertions.assertEquals(expectedRelativePath, FetchWriter.formatRelativePathString(parent, child));
  }

  @Test
  public void testUsingBothRelativeAndAbsolutePaths(){
    Path parent = Paths.get("one/two");
    Path child = Paths.get("one/two/three").toAbsolutePath();
    String expectedRelativePath = "three" + System.lineSeparator();

    Assertions.assertEquals(expectedRelativePath, FetchWriter.formatRelativePathString(parent, child));
  }
}
