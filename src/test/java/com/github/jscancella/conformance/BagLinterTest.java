package com.github.jscancella.conformance;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.Security;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.IntStream;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.jscancella.domain.Bag;
import com.github.jscancella.hash.BagitChecksumNameMapping;
import com.github.jscancella.hash.StandardHasher;
import com.github.jscancella.verify.SHA3Hasher;

public class BagLinterTest {
  
  static {
    if (Security.getProvider("BC") == null) {
      Security.addProvider(new BouncyCastleProvider());
    }
  }

  private final Path rootDir = Paths.get("src","test","resources","linterBags");
  
  @Test
  public void testConformantBag() throws Exception{
    Path goodBag = Paths.get("src", "test", "resources", "bags", "v1_0", "bag");
    Set<BagitWarning> warnings = BagLinter.lintBag(goodBag);
    Assertions.assertTrue(warnings.size() == 0);
  }
  
  @Test
  public void testBagWithinABag() throws Exception{
    Set<BagitWarning> warnings =  BagLinter.lintBag(rootDir.resolve("bagWithinABag"));
    Assertions.assertTrue(warnings.contains(BagitWarning.BAG_WITHIN_A_BAG));
    Assertions.assertTrue(warnings.size() == 1);
  }
  
  @Test
  public void testManifestListedSameFileWithDifferentCase() throws Exception {
    Set<BagitWarning> warnings =  BagLinter.lintBag(rootDir.resolve("differentCase"));
    Assertions.assertTrue(warnings.contains(BagitWarning.DIFFERENT_CASE));
    Assertions.assertTrue(warnings.size() == 1);
  }
  
  @Test
  public void testBagitFileWithExtraLines() throws Exception {
    Set<BagitWarning> warnings =  BagLinter.lintBag(rootDir.resolve("extraLinesInBagitFile"));
    Assertions.assertTrue(warnings.contains(BagitWarning.EXTRA_LINES_IN_BAGIT_FILES));
    Assertions.assertTrue(warnings.size() == 1);
  }
  
  @Test
  public void testManifestWithLeadingDotSlash() throws Exception {
    Set<BagitWarning> warnings =  BagLinter.lintBag(rootDir.resolve("leadingDotSlash"));
    Assertions.assertTrue(warnings.contains(BagitWarning.LEADING_DOT_SLASH));
    Assertions.assertTrue(warnings.size() == 1);
  }
  
  @Test
  public void testNonStandardAlgorithm() throws Exception {
    boolean successful = BagitChecksumNameMapping.add("sha3", SHA3Hasher.INSTANCE);
    Assertions.assertTrue(successful, "couldn't add sha3, did you add bouncy castle?");
    
    Set<BagitWarning> warnings =  BagLinter.lintBag(rootDir.resolve("nonstandardAlgorithm"));
    Assertions.assertTrue(warnings.contains(BagitWarning.NON_STANDARD_ALGORITHM), "Should have contained BagitWarning.NON_STANDARD_ALGORITHM but instead had: " + warnings);
    Assertions.assertEquals(1, warnings.size());
    
    BagitChecksumNameMapping.clear("sha3256");
  }
  
  @Test
  public void testMD5SumGeneratedManifest() throws Exception {
    Set<BagitWarning> warnings =  BagLinter.lintBag(rootDir.resolve("MD5sumGenerateredManifest"), Arrays.asList(BagitWarning.WEAK_CHECKSUM_ALGORITHM));
    Assertions.assertTrue(warnings.contains(BagitWarning.MD5SUM_TOOL_GENERATED_MANIFEST));    
    Assertions.assertTrue(warnings.size() == 1);
  }
  
  @Test
  public void testMissingTagManifest() throws Exception {
    Set<BagitWarning> warnings =  BagLinter.lintBag(rootDir.resolve("missingTagManifest"));
    Assertions.assertTrue(warnings.contains(BagitWarning.MISSING_TAG_MANIFEST));
    Assertions.assertTrue(warnings.size() == 1);
  }
  
  @Test
  public void testOldBagitVersion() throws Exception {
    Set<BagitWarning> warnings =  BagLinter.lintBag(rootDir.resolve("oldBagitVersion"));
    Assertions.assertTrue(warnings.contains(BagitWarning.OLD_BAGIT_VERSION));
    Assertions.assertTrue(warnings.size() == 1);
  }
  
  @Test
  public void testOSSpecificFiles() throws Exception {
    Set<BagitWarning> warnings =  BagLinter.lintBag(rootDir.resolve("osSpecificFiles"));
    Assertions.assertTrue(warnings.contains(BagitWarning.OS_SPECIFIC_FILES));
    Assertions.assertTrue(warnings.size() == 1);
  }
  
  @Test
  public void testPayloadOxumMissing() throws Exception {
    Set<BagitWarning> warnings =  BagLinter.lintBag(rootDir.resolve("payloadOxumMissing"));
    Assertions.assertTrue(warnings.contains(BagitWarning.PAYLOAD_OXUM_MISSING));
    Assertions.assertTrue(warnings.size() == 1);
  }
  
  @Test
  public void testTagFileEncodingIsNotUTF8() throws Exception {
    Set<BagitWarning> warnings =  BagLinter.lintBag(rootDir.resolve("tagFileEncoding"));
    Assertions.assertTrue(warnings.contains(BagitWarning.TAG_FILES_ENCODING));
    Assertions.assertTrue(warnings.size() == 1);
  }
  
  @Test
  public void testMD5IsAWeakAlgorithm() throws Exception {
    Set<BagitWarning> warnings =  BagLinter.lintBag(rootDir.resolve("weakAlgorithm"));
    Assertions.assertTrue(warnings.contains(BagitWarning.WEAK_CHECKSUM_ALGORITHM));
    Assertions.assertTrue(warnings.size() == 1);
  }
  
  @Test
  public void testManifestsDiffer() throws Exception {
    //starting with version 1.0 manifests MUST contain the same list of files
    Set<BagitWarning> warnings =  BagLinter.lintBag(rootDir.resolve("manifestsDiffer"), Arrays.asList(BagitWarning.OLD_BAGIT_VERSION));
    Assertions.assertTrue(warnings.contains(BagitWarning.MANIFEST_SETS_DIFFER));
    Assertions.assertTrue(warnings.size() == 1);
  }
  
  @Test
  public void testTooManyManifests() throws Exception {
    //otherwise we will get an exception for not having an implementation of that algorithm
    IntStream.rangeClosed(3, 21).forEach(i -> BagitChecksumNameMapping.add("sha" + String.valueOf(i), StandardHasher.SHA1));
    
    //starting with version 1.0 manifests MUST contain the same list of files
    Set<BagitWarning> warnings =  BagLinter.lintBag(rootDir.resolve("largeNumberOfManifests"), Arrays.asList(BagitWarning.NON_STANDARD_ALGORITHM, BagitWarning.MISSING_TAG_MANIFEST));
    Assertions.assertTrue(warnings.contains(BagitWarning.LARGE_NUMBER_OF_MANIFESTS));
    Assertions.assertTrue(warnings.size() == 1);
  }
  
  // TODO LARGE_BAG_SIZE
  
  @Test
  public void testTooManyFiles() throws Exception {
    Set<BagitWarning> warnings =  BagLinter.lintBag(rootDir.resolve("largeNumberOfFiles"));
    Assertions.assertTrue(warnings.contains(BagitWarning.LARGE_NUMBER_OF_FILES));
    Assertions.assertTrue(warnings.size() == 1);
  }
  
  @Test
  public void testDifferentNormalization() throws Exception {
    Set<BagitWarning> warnings =  BagLinter.lintBag(rootDir.resolve("normalization"));
    Assertions.assertTrue(warnings.contains(BagitWarning.DIFFERENT_NORMALIZATION));
    Assertions.assertTrue(warnings.size() == 1);
  }
  
  @Test
  public void testCheckAgainstProfile() throws Exception{
    Path profileJson = new File("src/test/resources/bagitProfiles/OnlyRequiredFieldsProfile_v1.2.0.json").toPath();
    Path bagRootPath = new File("src/test/resources/bagitProfileTestBags/profileConformantBag").toPath();
    Bag bag = Bag.read(bagRootPath);
    
    try(InputStream inputStream = Files.newInputStream(profileJson, StandardOpenOption.READ)){
      BagLinter.checkAgainstProfile(inputStream, bag);
    }
  }
  
  @Test
  public void testIgnoreCheckForExtraLines() throws Exception{
    Set<BagitWarning> warnings = BagLinter.lintBag(rootDir.resolve("extraLinesInBagitFile"), Arrays.asList(BagitWarning.EXTRA_LINES_IN_BAGIT_FILES));
    Assertions.assertFalse(warnings.contains(BagitWarning.EXTRA_LINES_IN_BAGIT_FILES));
  }
}
