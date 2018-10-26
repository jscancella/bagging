package com.github.jscancella.verify.internal;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.helpers.MessageFormatter;

import com.github.jscancella.domain.Bag;
import com.github.jscancella.domain.Version;
import com.github.jscancella.exceptions.InvalidBagitFileFormatException;

public enum BagitTextFileVerifier {; //using to enforce singleton
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  private static final String LINE1_REGEX = "(BagIt-Version: )\\d*\\.\\d*";
  private static final String LINE2_REGEX = "(Tag-File-Character-Encoding: )\\S*";
  
  public static void checkBagitTextFile(final Bag bag) throws IOException, InvalidBagitFileFormatException {
    if(Version.VERSION_1_0().isSameOrNewer(bag.getVersion())){
      final List<String> lines = Files.readAllLines(bag.getRootDir().resolve("bagit.txt"), StandardCharsets.UTF_8);
      throwErrorIfLinesDoNotMatchStrict(lines);
    }
  }

  /*
   * As per the specification, if version is 1.0+ it must only contain 2 lines of the form
   * BagIt-Version: <M.N>
   * Tag-File-Character-Encoding: <ENCODING>
   */
  static void throwErrorIfLinesDoNotMatchStrict(final List<String> lines) throws InvalidBagitFileFormatException{
    if(lines.size() > 2){
      final List<String> offendingLines = lines.subList(2, lines.size()-1);
      throw new InvalidBagitFileFormatException(MessageFormatter
          .format(messages.getString("strict_only_two_lines_error"), offendingLines).getMessage());
    }
    if(!lines.get(0).matches(LINE1_REGEX)){
      throw new InvalidBagitFileFormatException(MessageFormatter
          .format(messages.getString("strict_first_line_error"), lines.get(0)).getMessage());
    }
    if(!lines.get(1).matches(LINE2_REGEX)){
      throw new InvalidBagitFileFormatException(MessageFormatter
          .format(messages.getString("strict_second_line_error"), lines.get(0)).getMessage());
    }
  }
}
