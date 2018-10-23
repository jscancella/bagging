package com.github.jscancella;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import com.github.jscancella.domain.Bag;
import com.github.jscancella.reader.BagReader;
import com.github.jscancella.verify.BagVerifier;
import com.github.jscancella.writer.BagWriter;

public class ReaderWriterVerifierIntegrationTest extends TempFolderTest {
  
  @Test
  public void testReaderWriterVersion93() throws Exception{
    Path rootDir = Paths.get(this.getClass().getClassLoader().getResource("bags/v0_93/bag").toURI());
    Path outputDir = folder.resolve("version93");
    
    Bag bag = BagReader.read(rootDir);
    BagVerifier.isValid(bag, true);
    
    BagWriter.write(bag, outputDir);
    testBagsEqual(rootDir, outputDir);
    
    BagVerifier.isValid(BagReader.read(outputDir), true);
  }
  
  @Test
  public void testReaderWriterVersion94() throws Exception{
    Path rootDir = Paths.get(this.getClass().getClassLoader().getResource("bags/v0_94/bag").toURI());
    Bag bag = BagReader.read(rootDir);
    Path outputDir = folder.resolve("version94");
    
    BagWriter.write(bag, outputDir);
    
    testBagsEqual(rootDir, outputDir);
    BagVerifier.isValid(BagReader.read(outputDir), true);
  }
  
  @Test
  public void testReaderWriterVersion95() throws Exception{
    Path rootDir = Paths.get(this.getClass().getClassLoader().getResource("bags/v0_95/bag").toURI());
    Bag bag = BagReader.read(rootDir);
    Path outputDir = folder.resolve("version95");
    
    BagWriter.write(bag, outputDir);
    
    testBagsEqual(rootDir, outputDir);
    BagVerifier.isValid(BagReader.read(outputDir), true);
  }
  
  @Test
  public void testReaderWriterVersion96() throws Exception{
    Path rootDir = Paths.get(this.getClass().getClassLoader().getResource("bags/v0_96/bag").toURI());
    Bag bag = BagReader.read(rootDir);
    Path outputDir = folder.resolve("version96");
    
    BagWriter.write(bag, outputDir);
    
    testBagsEqual(rootDir, outputDir);
    BagVerifier.isValid(BagReader.read(outputDir), true);
  }

  @Test
  public void testReaderWriterVersion97() throws Exception{
    Path rootDir = Paths.get(this.getClass().getClassLoader().getResource("bags/v0_97/bag").toURI());
    Bag bag = BagReader.read(rootDir);
    Path outputDir = folder.resolve("version97");
    
    BagWriter.write(bag, outputDir);
    
    testBagsEqual(rootDir, outputDir);
    BagVerifier.isValid(BagReader.read(outputDir), true);
  }
  
  private void testBagsEqual(Path originalBag, Path newBag) throws IOException{
    Files.walkFileTree(originalBag, new FileExistsVistor(originalBag, newBag));
  }
}
