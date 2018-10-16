package com.github.jscancella.reader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap.SimpleImmutableEntry;

import com.github.jscancella.domain.Bag;
import com.github.jscancella.domain.Version;
import com.github.jscancella.exceptions.InvalidBagMetadataException;
import com.github.jscancella.exceptions.InvalidBagitFileFormatException;
import com.github.jscancella.exceptions.MaliciousPathException;
import com.github.jscancella.exceptions.UnparsableVersionException;
import com.github.jscancella.reader.internal.BagitTextFileReader;
import com.github.jscancella.reader.internal.FetchReader;
import com.github.jscancella.reader.internal.ManifestReader;
import com.github.jscancella.reader.internal.MetadataReader;

public enum BagReader {; //using enum to ensure singleton
  public static Bag read(final Path bagDirectory) 
      throws InvalidBagMetadataException, IOException, UnparsableVersionException, InvalidBagitFileFormatException, MaliciousPathException {
    
    final Bag bag = new Bag();
    final Path bagitFile = bagDirectory.resolve("bagit.txt");
    final SimpleImmutableEntry<Version, Charset> bagitInfo = BagitTextFileReader.readBagitTextFile(bagitFile);
    bag.setVersion(bagitInfo.getKey());
    bag.setFileEncoding(bagitInfo.getValue());
    bag.setRootDir(bagDirectory);
    
    ManifestReader.readAllManifests(bagDirectory, bag);
    
    bag.getMetadata().addAll(MetadataReader.readBagMetadata(bagDirectory, bag.getFileEncoding()));
    
    final Path fetchFile = bagDirectory.resolve("fetch.txt");
    if(Files.exists(fetchFile)){
      bag.getItemsToFetch().addAll(FetchReader.readFetch(fetchFile, bag.getFileEncoding(), bag.getRootDir()));
    }
    
    return bag;
  }
}
