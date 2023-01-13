package com.github.jscancella.conformance.deserialize;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.jscancella.conformance.profile.BagitProfileBuilder;
import com.github.jscancella.conformance.profile.Serialization;

/**
 * responsible for deserializing "Serialization" node from bagit profile
 */
public enum SerializationAllowedNodeDeserializer {
  ;// using enum to ensure singleton
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  private static final Logger logger = LoggerFactory.getLogger(SerializationAllowedNodeDeserializer.class);
  
  /**
   * @param node the json node to parse
   * @param builder the bagit profile builder
   */
  public static void parseSerializationAllowed(final JsonNode node, final BagitProfileBuilder builder) {
    final JsonNode serialization = node.get("Serialization");
    if(serialization != null && !(serialization instanceof NullNode)) {
      builder.setSerialization(Serialization.valueOf(serialization.asText()));
    }
    logger.debug(messages.getString("serialization_allowed"),builder.getSerializationType());
  }
}
