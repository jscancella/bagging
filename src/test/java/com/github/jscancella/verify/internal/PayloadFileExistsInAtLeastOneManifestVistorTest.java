package com.github.jscancella.verify.internal;

import java.util.HashSet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.jscancella.TempFolderTest;
import com.github.jscancella.exceptions.FileNotInManifestException;
import com.github.jscancella.verify.internal.PayloadFileExistsInAtLeastOneManifestVistor;

public class PayloadFileExistsInAtLeastOneManifestVistorTest extends TempFolderTest {

  @Test
  public void testFileNotInManifestException() throws Exception{
    
    PayloadFileExistsInAtLeastOneManifestVistor sut = new PayloadFileExistsInAtLeastOneManifestVistor(new HashSet<>(), true);
    Assertions.assertThrows(FileNotInManifestException.class, 
        () -> { sut.visitFile(createFile("aNewFile"), null); });
  }
}
