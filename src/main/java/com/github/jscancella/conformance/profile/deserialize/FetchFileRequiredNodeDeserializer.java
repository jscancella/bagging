package com.github.jscancella.conformance.profile.deserialize;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.jscancella.conformance.profile.BagitProfileBuilder;

/**
 * responsible for deserializing "Fetch.txt-Required" node from bagit profile
 */
public enum FetchFileRequiredNodeDeserializer {
  ;// using enum to ensure singleton
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  private static final Logger logger = LoggerFactory.getLogger(FetchFileRequiredNodeDeserializer.class);
  
  /**
   * @param node the json node to parse
   * @param builder the bagit profile builder
   */
  public static void parseFetchFileRequired(final JsonNode node, final BagitProfileBuilder builder) {
    final JsonNode fetchIsRequired = node.get("Fetch.txt-Required");
    if(fetchIsRequired != null && !(fetchIsRequired instanceof NullNode)) {
      builder.setFetchFileAllowed(fetchIsRequired.asBoolean());
    }
    logger.debug(messages.getString("fetch_required"), builder.isFetchRequired());
  }
}
