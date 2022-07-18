package com.github.jscancella.verify.internal;

import java.util.HashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.jscancella.TempFolderTest;
import com.github.jscancella.exceptions.FileNotInManifestException;

public class PayloadFileExistsInAtLeastOneManifestVistorTest extends TempFolderTest {

  @Test
  public void testFileNotInManifestException() throws Exception{
    
    PayloadFileExistsInAtLeastOneManifestVistor sut = new PayloadFileExistsInAtLeastOneManifestVistor(new HashMap<>(), true);
    Assertions.assertThrows(FileNotInManifestException.class, 
        () -> { sut.visitFile(createFile("aNewFile"), null); });
  }
}
