package com.github.jscancella.conformance.profile.deserialize;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.jscancella.conformance.profile.BagitProfileBuilder;

/**
 * responsible for deserializing "Data-Empty" node from bagit profile
 */
public enum DataDirEmptyNodeDeserializer {
  ;// using enum to ensure singleton
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  private static final Logger logger = LoggerFactory.getLogger(DataDirEmptyNodeDeserializer.class);
  
  /**
   * @param node the json node to parse
   * @param builder the bagit profile builder
   */
  public static void parseDataDirEmpty(final JsonNode node, final BagitProfileBuilder builder) {
    final JsonNode dataDirMustBeEmpty = node.get("Data-Empty");
    if(dataDirMustBeEmpty != null && !(dataDirMustBeEmpty instanceof NullNode)) {
      builder.setDataDirMustBeEmpty(dataDirMustBeEmpty.asBoolean());
    }
    logger.debug(messages.getString("data_empty"), builder.isDataDirEmpty());
  }
}
