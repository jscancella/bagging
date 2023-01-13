package com.github.jscancella.conformance.deserialize;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.jscancella.conformance.profile.BagitProfileBuilder;

/**
 * responsible for deserializing "Allow-Fetch.txt" node from bagit profile
 */
public enum FetchFileAllowedNodeDeserializer {
  ;// using enum to ensure singleton
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  private static final Logger logger = LoggerFactory.getLogger(FetchFileAllowedNodeDeserializer.class);
  
  /**
   * @param node the json node to parse
   * @param builder the bagit profile builder
   */
  public static void parseFetchFileAllowed(final JsonNode node, final BagitProfileBuilder builder) {
    final JsonNode fetchIsAllowed = node.get("Allow-Fetch.txt");
    if(fetchIsAllowed != null && !(fetchIsAllowed instanceof NullNode)) {
      builder.setFetchFileAllowed(fetchIsAllowed.asBoolean());
    }
    logger.debug(messages.getString("fetch_allowed"), builder.isFetchAllowed());
  }
}
