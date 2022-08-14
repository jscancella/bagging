package com.github.jscancella.writer.internal;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Iterator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.jscancella.domain.Version;

public class RelativePathWriterTest {
  @Test
  public void testFormattingNewline() {
    Path testPath = getFakePath("\\foo\n\\bar\n\\ham");
    
    String expectedFormatting = "/foo%0A/bar%0A/ham" + System.lineSeparator();
    
    Assertions.assertEquals(expectedFormatting, RelativePathWriter.formatRelativePathString(testPath, Version.VERSION_1_0(), StandardCharsets.UTF_8));
  }
  
  @Test
  public void testFormattingCariageReturn() {
    Path testPath = getFakePath("\\foo\r\\bar\r\\ham");
    
    String expectedFormatting = "/foo%0D/bar%0D/ham" + System.lineSeparator();
    
    Assertions.assertEquals(expectedFormatting, RelativePathWriter.formatRelativePathString(testPath, Version.VERSION_1_0(), StandardCharsets.UTF_8));
  }
  
  @Test
  public void testFormattingBackslashes() {
    Path testPath = getFakePath("\\foo\\bar\\ham");
    
    String expectedFormatting = "/foo/bar/ham" + System.lineSeparator();
    
    Assertions.assertEquals(expectedFormatting, RelativePathWriter.formatRelativePathString(testPath, Version.VERSION_1_0(), StandardCharsets.UTF_8));
  }
  
  //needed because you can't actually create these on windows...
  public Path getFakePath(String fakePath) {
    return new Path() {
      
      //This is the only method we actually care about
      @Override
      public String toString() {
        return fakePath;
      }
      
      @Override
      public URI toUri(){
        // TODO Auto-generated method stub
        return null;
      }
      
      @Override
      public Path toRealPath(LinkOption... options) throws IOException{
        // TODO Auto-generated method stub
        return null;
      }
      
      @Override
      public File toFile(){
        // TODO Auto-generated method stub
        return null;
      }
      
      @Override
      public Path toAbsolutePath(){
        // TODO Auto-generated method stub
        return null;
      }
      
      @Override
      public Path subpath(int beginIndex, int endIndex){
        // TODO Auto-generated method stub
        return null;
      }
      
      @Override
      public boolean startsWith(String other){
        // TODO Auto-generated method stub
        return false;
      }
      
      @Override
      public boolean startsWith(Path other){
        // TODO Auto-generated method stub
        return false;
      }
      
      @Override
      public Path resolveSibling(String other){
        // TODO Auto-generated method stub
        return null;
      }
      
      @Override
      public Path resolveSibling(Path other){
        // TODO Auto-generated method stub
        return null;
      }
      
      @Override
      public Path resolve(String other){
        // TODO Auto-generated method stub
        return null;
      }
      
      @Override
      public Path resolve(Path other){
        // TODO Auto-generated method stub
        return null;
      }
      
      @Override
      public Path relativize(Path other){
        // TODO Auto-generated method stub
        return null;
      }
      
      @Override
      public WatchKey register(WatchService watcher, Kind<?>[] events, Modifier... modifiers) throws IOException{
        // TODO Auto-generated method stub
        return null;
      }
      
      @Override
      public WatchKey register(WatchService watcher, Kind<?>... events) throws IOException{
        // TODO Auto-generated method stub
        return null;
      }
      
      @Override
      public Path normalize(){
        // TODO Auto-generated method stub
        return null;
      }
      
      @Override
      public Iterator<Path> iterator(){
        // TODO Auto-generated method stub
        return null;
      }
      
      @Override
      public boolean isAbsolute(){
        // TODO Auto-generated method stub
        return false;
      }
      
      @Override
      public Path getRoot(){
        // TODO Auto-generated method stub
        return null;
      }
      
      @Override
      public Path getParent(){
        // TODO Auto-generated method stub
        return null;
      }
      
      @Override
      public int getNameCount(){
        // TODO Auto-generated method stub
        return 0;
      }
      
      @Override
      public Path getName(int index){
        // TODO Auto-generated method stub
        return null;
      }
      
      @Override
      public FileSystem getFileSystem(){
        // TODO Auto-generated method stub
        return null;
      }
      
      @Override
      public Path getFileName(){
        // TODO Auto-generated method stub
        return null;
      }
      
      @Override
      public boolean endsWith(String other){
        // TODO Auto-generated method stub
        return false;
      }
      
      @Override
      public boolean endsWith(Path other){
        // TODO Auto-generated method stub
        return false;
      }
      
      @Override
      public int compareTo(Path other){
        // TODO Auto-generated method stub
        return 0;
      }
    };
  }
}
