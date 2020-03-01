package com.github.jscancella.domain;

import java.net.URI;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FetchItemTest {

  @Test
  public void testLogicallySameObjectsAreEqualWithNullLength() throws Exception{
    FetchItem item1 = new FetchItem(new URI("https://www.hackaday.com/blog"), null, Paths.get("foo", "bar"));
    FetchItem item2 = new FetchItem(new URI("https://www.hackaday.com/blog"), null, Paths.get("foo", "bar"));
    
    Assertions.assertEquals(item1, item2);
    Assertions.assertEquals(item1.hashCode(), item2.hashCode());
  }
  
  @Test
  public void testLogicallySameObjectsAreEqualWithUnknownLength() throws Exception{
    FetchItem item1 = new FetchItem(new URI("https://www.hackaday.com/blog"), -1l, Paths.get("foo", "bar"));
    FetchItem item2 = new FetchItem(new URI("https://www.hackaday.com/blog"), -1l, Paths.get("foo", "bar"));
    
    Assertions.assertEquals(item1, item2);
    Assertions.assertEquals(item1.hashCode(), item2.hashCode());
  }
  
  @Test
  public void testLogicallySameObjectsAreEqualWithKnownLength() throws Exception{
    FetchItem item1 = new FetchItem(new URI("https://www.hackaday.com/blog"), 100l, Paths.get("foo", "bar"));
    FetchItem item2 = new FetchItem(new URI("https://www.hackaday.com/blog"), 100l, Paths.get("foo", "bar"));
    
    Assertions.assertEquals(item1, item2);
    Assertions.assertEquals(item1.hashCode(), item2.hashCode());
  }
  
  @Test
  public void testToString() throws Exception{
    FetchItem item1 = new FetchItem(new URI("https://www.hackaday.com/blog"), -1l, Paths.get("foo", "bar"));
    Assertions.assertEquals("https://www.hackaday.com/blog - foo/bar", item1.toString());
    
    item1 = new FetchItem(new URI("https://www.hackaday.com/blog"), null, Paths.get("foo", "bar"));
    Assertions.assertEquals("https://www.hackaday.com/blog - foo/bar", item1.toString());
    
    item1 = new FetchItem(new URI("https://www.hackaday.com/blog"), 100l, Paths.get("foo", "bar"));
    Assertions.assertEquals("https://www.hackaday.com/blog 100 foo/bar", item1.toString());
    
    item1 = new FetchItem(new URI("https://www.hackaday.com/blog"), 0l, Paths.get("foo", "bar"));
    Assertions.assertEquals("https://www.hackaday.com/blog 0 foo/bar", item1.toString());
  }
  
  
}
