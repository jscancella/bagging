package com.github.jscancella.reader;

import java.nio.file.Path;

import com.github.jscancella.domain.Bag;

public enum BagReader {; //using enum to ensure singleton
  public static Bag read(Path bagDirectory) {
    
    return new Bag();
    
  }
}
