package com.github.jscancella;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import com.github.jscancella.domain.Bag;
import com.github.jscancella.domain.BagBuilder;

/**
 * This class deals with testing that reading and then writing a bag are equal.
 * Or to put it another way, the bag I just read it, when I write it out, is equal to the original bag I read.
 */
public class ReaderWriterVerifierIntegrationTest extends TempFolderTest {
  
  @Test
  public void testReaderWriterVersion93() throws Exception{
    //create a test bag so that line endings match.
    final Path originalBagDir = folder.resolve("version93_original");
    BagBuilder bagBuilder = new BagBuilder();
    bagBuilder.version(0, 93)
    .addAlgorithm("md5")
    .addMetadata("foo", "bar")
    .addPayloadFile(Paths.get(this.getClass().getClassLoader().getResource("bags/v0_93/bag/data/test1.txt").toURI()))
    .bagLocation(originalBagDir)
    .write();
    
    Bag bag = Bag.read(originalBagDir);
    bag.isValid(true);

    Path outputDir = folder.resolve("version93");
    bag = bag.write(outputDir);
    testBagsEqual(originalBagDir, outputDir);
    
    bag.isValid(true);
  }
  
  @Test
  public void testReaderWriterVersion94() throws Exception{
  //create a test bag so that line endings match.
    final Path originalBagDir = folder.resolve("version94_original");
    BagBuilder bagBuilder = new BagBuilder();
    bagBuilder.version(0, 94)
    .addAlgorithm("md5")
    .addMetadata("foo", "bar")
    .addPayloadFile(Paths.get(this.getClass().getClassLoader().getResource("bags/v0_94/bag/data/test1.txt").toURI()))
    .bagLocation(originalBagDir)
    .write();
    
    Bag bag = Bag.read(originalBagDir);
    bag.isValid(true);

    Path outputDir = folder.resolve("version94");
    bag = bag.write(outputDir);
    testBagsEqual(originalBagDir, outputDir);
    
    bag.isValid(true);
  }
  
  @Test
  public void testReaderWriterVersion95() throws Exception{
  //create a test bag so that line endings match.
    final Path originalBagDir = folder.resolve("version95_original");
    BagBuilder bagBuilder = new BagBuilder();
    bagBuilder.version(0, 95)
    .addAlgorithm("md5")
    .addMetadata("foo", "bar")
    .addPayloadFile(Paths.get(this.getClass().getClassLoader().getResource("bags/v0_95/bag/data/test1.txt").toURI()))
    .bagLocation(originalBagDir)
    .write();
    
    Bag bag = Bag.read(originalBagDir);
    bag.isValid(true);

    Path outputDir = folder.resolve("version95");
    bag = bag.write(outputDir);
    testBagsEqual(originalBagDir, outputDir);
    
    bag.isValid(true);
  }
  
  @Test
  public void testReaderWriterVersion96() throws Exception{
  //create a test bag so that line endings match.
    final Path originalBagDir = folder.resolve("version96_original");
    BagBuilder bagBuilder = new BagBuilder();
    bagBuilder.version(0, 96)
    .addAlgorithm("md5")
    .addMetadata("foo", "bar")
    .addPayloadFile(Paths.get(this.getClass().getClassLoader().getResource("bags/v0_96/bag/data/test1.txt").toURI()))
    .bagLocation(originalBagDir)
    .write();
    
    Bag bag = Bag.read(originalBagDir);
    bag.isValid(true);

    Path outputDir = folder.resolve("version93");
    bag = bag.write(outputDir);
    testBagsEqual(originalBagDir, outputDir);
    
    bag.isValid(true);
  }

  @Test
  public void testReaderWriterVersion97() throws Exception{
  //create a test bag so that line endings match.
    final Path originalBagDir = folder.resolve("version97_original");
    BagBuilder bagBuilder = new BagBuilder();
    bagBuilder.version(0, 97)
    .addAlgorithm("md5")
    .addMetadata("foo", "bar")
    .addPayloadFile(Paths.get(this.getClass().getClassLoader().getResource("bags/v0_97/bag/data/test1.txt").toURI()))
    .bagLocation(originalBagDir)
    .write();
    
    Bag bag = Bag.read(originalBagDir);
    bag.isValid(true);

    Path outputDir = folder.resolve("version97");
    bag = bag.write(outputDir);
    testBagsEqual(originalBagDir, outputDir);
    
    bag.isValid(true);
  }
  
  @Test
  public void testReaderWriterVersion1_0() throws Exception{
  //create a test bag so that line endings match.
    final Path originalBagDir = folder.resolve("version1_0_original");
    BagBuilder bagBuilder = new BagBuilder();
    bagBuilder.version(1, 0)
    .addAlgorithm("md5")
    .addMetadata("foo", "bar")
    .addPayloadFile(Paths.get(this.getClass().getClassLoader().getResource("bags/v1_0/bag/data/foo.txt").toURI()))
    .bagLocation(originalBagDir)
    .write();
    
    Bag bag = Bag.read(originalBagDir);
    bag.isValid(true);

    Path outputDir = folder.resolve("version1_0");
    bag = bag.write(outputDir);
    testBagsEqual(originalBagDir, outputDir);
    
    bag.isValid(true);
  }
  
  private void testBagsEqual(Path originalBag, Path newBag) throws IOException{
    Files.walkFileTree(originalBag, new FileExistsVistor(originalBag, newBag));
  }
}
