package com.github.jscancella.writer.internal;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.jscancella.TempFolderTest;

public class RelativePathWriterTest extends TempFolderTest {
  
  @Test
  public void testRelativePath(){
    Path parent = Paths.get("/foo");
    Path child = parent.resolve("bar/ham");
    String expectedRelativePath = "bar/ham" + System.lineSeparator();
    
    Assertions.assertEquals(expectedRelativePath, RelativePathWriter.formatRelativePathString(parent, child));
  }

  @Test
  public void testUsingBothRelativeAndAbsolutePaths(){
    Path parent = Paths.get("one/two");
    Path child = Paths.get("one/two/three").toAbsolutePath();
    String expectedRelativePath = "three" + System.lineSeparator();

    Assertions.assertEquals(expectedRelativePath, RelativePathWriter.formatRelativePathString(parent, child));
  }
}
