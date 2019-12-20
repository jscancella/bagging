package com.github.jscancella;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import com.github.jscancella.domain.Bag;
import com.github.jscancella.domain.Bag.BagBuilder;

public class ReaderWriterVerifierIntegrationTest extends TempFolderTest {
  
  @Test
  public void testReaderWriterVersion93() throws Exception{
    Path rootDir = Paths.get(this.getClass().getClassLoader().getResource("bags/v0_93/bag").toURI());
    Path outputDir = folder.resolve("version93");
    
    BagBuilder bagBuilder = new BagBuilder();
    Bag bag = bagBuilder.read(rootDir);
    bag.isValid(true);
    
    bagBuilder.rootDir(outputDir);
    bag = bagBuilder.write();
    testBagsEqual(rootDir, outputDir);
    
    bag.isValid(true);
  }
  
  @Test
  public void testReaderWriterVersion94() throws Exception{
    Path rootDir = Paths.get(this.getClass().getClassLoader().getResource("bags/v0_94/bag").toURI());
    Path outputDir = folder.resolve("version94");
    
    BagBuilder bagBuilder = new BagBuilder();
    Bag bag = bagBuilder.read(rootDir);
    bag.isValid(true);
    
    bagBuilder.rootDir(outputDir);
    bag = bagBuilder.write();
    testBagsEqual(rootDir, outputDir);
    
    bag.isValid(true);
  }
  
  @Test
  public void testReaderWriterVersion95() throws Exception{
    Path rootDir = Paths.get(this.getClass().getClassLoader().getResource("bags/v0_95/bag").toURI());
    Path outputDir = folder.resolve("version95");
    
    BagBuilder bagBuilder = new BagBuilder();
    Bag bag = bagBuilder.read(rootDir);
    bag.isValid(true);
    
    bagBuilder.rootDir(outputDir);
    bag = bagBuilder.write();
    testBagsEqual(rootDir, outputDir);
    
    bag.isValid(true);
  }
  
  @Test
  public void testReaderWriterVersion96() throws Exception{
    Path rootDir = Paths.get(this.getClass().getClassLoader().getResource("bags/v0_96/bag").toURI());
    Path outputDir = folder.resolve("version96");
    
    BagBuilder bagBuilder = new BagBuilder();
    Bag bag = bagBuilder.read(rootDir);
    bag.isValid(true);
    
    bagBuilder.rootDir(outputDir);
    bag = bagBuilder.write();
    testBagsEqual(rootDir, outputDir);
    
    bag.isValid(true);
  }

  @Test
  public void testReaderWriterVersion97() throws Exception{
    Path rootDir = Paths.get(this.getClass().getClassLoader().getResource("bags/v0_97/bag").toURI());
    Path outputDir = folder.resolve("version97");
    
    BagBuilder bagBuilder = new BagBuilder();
    Bag bag = bagBuilder.read(rootDir);
    bag.isValid(true);
    
    bagBuilder.rootDir(outputDir);
    bag = bagBuilder.write();
    testBagsEqual(rootDir, outputDir);
    
    bag.isValid(true);
  }
  
  private void testBagsEqual(Path originalBag, Path newBag) throws IOException{
    Files.walkFileTree(originalBag, new FileExistsVistor(originalBag, newBag));
  }
}
