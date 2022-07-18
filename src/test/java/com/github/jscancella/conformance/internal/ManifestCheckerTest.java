package com.github.jscancella.conformance.internal;

import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Security;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.github.jscancella.TempFolderTest;
import com.github.jscancella.conformance.BagitWarning;
import com.github.jscancella.exceptions.InvalidBagitFileFormatException;
import com.github.jscancella.hash.BagitChecksumNameMapping;
import com.github.jscancella.verify.SHA3Hasher;

public class ManifestCheckerTest extends TempFolderTest{
  
  static {
    if (Security.getProvider("BC") == null) {
      Security.addProvider(new BouncyCastleProvider());
    }
  }
  
  private final Path rootDir = Paths.get("src","test","resources","linterTestBag");
  
  @BeforeAll
  public static void setup() {
    boolean successful = BagitChecksumNameMapping.add("sha3", SHA3Hasher.class);
    Assertions.assertTrue(successful);
  }
  
  @AfterAll
  public static void teardown() {
    BagitChecksumNameMapping.clear("sha3");
  }
  
  @Test
  public void testCheckManifests() throws Exception{
    Set<BagitWarning> warnings = new HashSet<>();

    ManifestChecker.checkManifests(rootDir, StandardCharsets.UTF_16, warnings, Collections.emptyList());
    
    Assertions.assertTrue(warnings.contains(BagitWarning.WEAK_CHECKSUM_ALGORITHM));
    Assertions.assertTrue(warnings.contains(BagitWarning.DIFFERENT_CASE));
    if(FileSystems.getDefault().getClass().getName().equals("sun.nio.fs.MacOSXFileSystem")){ //don't test normalization on mac
      Assertions.assertTrue(warnings.contains(BagitWarning.DIFFERENT_NORMALIZATION));
    }
    Assertions.assertTrue(warnings.contains(BagitWarning.BAG_WITHIN_A_BAG));
    Assertions.assertTrue(warnings.contains(BagitWarning.LEADING_DOT_SLASH));
    Assertions.assertTrue(warnings.contains(BagitWarning.NON_STANDARD_ALGORITHM));
    Assertions.assertTrue(warnings.contains(BagitWarning.OS_SPECIFIC_FILES));
    Assertions.assertTrue(warnings.contains(BagitWarning.MISSING_TAG_MANIFEST));
  }
  
  @Test
  public void testPayloadManifestSetsShouldBeSame() throws Exception{
    Set<BagitWarning> warnings = new HashSet<>();
    Path manifestPath = Paths.get("src","test","resources","payloadManifestsDiffer");
    ManifestChecker.checkManifests(manifestPath, StandardCharsets.UTF_8, warnings, Collections.emptyList());
    
    Assertions.assertTrue(warnings.contains(BagitWarning.MANIFEST_SETS_DIFFER));
  }
  
  @Test
  public void testTagManifestSetsShouldBeSame() throws Exception{
    Set<BagitWarning> warnings = new HashSet<>();
    Path manifestPath = Paths.get("src","test","resources","payloadManifestsDiffer");
    ManifestChecker.checkManifests(manifestPath, StandardCharsets.UTF_8, warnings, Collections.emptyList());
    
    Assertions.assertTrue(warnings.contains(BagitWarning.MANIFEST_SETS_DIFFER));
  }
  
  @Test
  public void testCheckTagManifest() throws Exception{
    createFile("tagmanifest-md5.txt");
    Set<BagitWarning> warnings = new HashSet<>();

    ManifestChecker.checkManifests(folder, StandardCharsets.UTF_16, warnings, Collections.emptyList());
    Assertions.assertFalse(warnings.contains(BagitWarning.MISSING_TAG_MANIFEST));
  }
  
  @Test
  public void testLinterIgnoreWeakChecksum() throws Exception{
    Set<BagitWarning> warnings = new HashSet<>();

    ManifestChecker.checkManifests(rootDir, StandardCharsets.UTF_16, warnings, Arrays.asList(BagitWarning.WEAK_CHECKSUM_ALGORITHM));
    
    Assertions.assertFalse(warnings.contains(BagitWarning.WEAK_CHECKSUM_ALGORITHM));
  }
  
  @Test
  public void testLinterIgnoreCase() throws Exception{
    Set<BagitWarning> warnings = new HashSet<>();

    ManifestChecker.checkManifests(rootDir, StandardCharsets.UTF_16, warnings, Arrays.asList(BagitWarning.DIFFERENT_CASE));
    
    Assertions.assertFalse(warnings.contains(BagitWarning.DIFFERENT_CASE));
  }
  
  @Test
  public void testLinterNormalization() throws Exception{
    Set<BagitWarning> warnings = new HashSet<>();

    ManifestChecker.checkManifests(rootDir, StandardCharsets.UTF_16, warnings, Arrays.asList(BagitWarning.DIFFERENT_NORMALIZATION));
    
    Assertions.assertFalse(warnings.contains(BagitWarning.DIFFERENT_NORMALIZATION));
  }
  
  @Test
  public void testLinterIgnoreBagWithinABag() throws Exception{
    Set<BagitWarning> warnings = new HashSet<>();

    ManifestChecker.checkManifests(rootDir, StandardCharsets.UTF_16, warnings, Arrays.asList(BagitWarning.BAG_WITHIN_A_BAG));
    
    Assertions.assertFalse(warnings.contains(BagitWarning.BAG_WITHIN_A_BAG));
  }
  
  @Test
  public void testLinterIgnoreRelativePath() throws Exception{
    Set<BagitWarning> warnings = new HashSet<>();

    ManifestChecker.checkManifests(rootDir, StandardCharsets.UTF_16, warnings, Arrays.asList(BagitWarning.LEADING_DOT_SLASH));
    
    Assertions.assertFalse(warnings.contains(BagitWarning.LEADING_DOT_SLASH));
  }
  
  @Test
  public void testLinterIgnoreNonStandardChecksumAlgorithm() throws Exception{
    Set<BagitWarning> warnings = new HashSet<>();

    ManifestChecker.checkManifests(rootDir, StandardCharsets.UTF_16, warnings, Arrays.asList(BagitWarning.NON_STANDARD_ALGORITHM));
    
    Assertions.assertFalse(warnings.contains(BagitWarning.NON_STANDARD_ALGORITHM));
  }
  
  @Test
  public void testLinterIgnoreOSSpecificFiles() throws Exception{
    Set<BagitWarning> warnings = new HashSet<>();

    ManifestChecker.checkManifests(rootDir, StandardCharsets.UTF_16, warnings, Arrays.asList(BagitWarning.OS_SPECIFIC_FILES));
    
    Assertions.assertFalse(warnings.contains(BagitWarning.OS_SPECIFIC_FILES));
  }

  @Test
  public void testOSSpecificFilesRegex(){
    String regex = ManifestChecker.getOsFilesRegex();
    String[] osFilesToTest = new String[]{"data/Thumbs.db", "data/.DS_Store", "data/.Spotlight-V100", "data/.Trashes", 
        "data/._.Trashes", "data/.fseventsd"};
    
    for(String osFileToTest : osFilesToTest){
      Assertions.assertTrue(osFileToTest.matches(regex), osFileToTest + " should match regex but it doesn't");
    }
  }
  
  @Test
  public void testParsePath(){
    Assertions.assertThrows(InvalidBagitFileFormatException.class, 
        () -> { ManifestChecker.parsePath("foobarham"); });
  }
  
  @Test
  public void testCheckAlgorthm(){
    Set<BagitWarning> warnings;
    String[] algorithms = new String[]{"md5", "sha1"};
    for(String algorithm : algorithms){
     warnings = new HashSet<>();
     ManifestChecker.checkAlgorthm(algorithm, warnings, Collections.emptyList());
       Assertions.assertTrue(warnings.contains(BagitWarning.WEAK_CHECKSUM_ALGORITHM));
    }
  }
}
