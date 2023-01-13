package com.github.jscancella.conformance.deserialize;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.jscancella.conformance.profile.BagitProfileBuilder;

/**
 * responsible for deserializing "Accept-Serialization" node from bagit profile
 */
public enum SerializationAcceptedNodeDeserializer {
  ;// using enum to ensure singleton
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  private static final Logger logger = LoggerFactory.getLogger(SerializationAcceptedNodeDeserializer.class);
  
  /**
   * @param node the json node to parse
   * @param builder the bagit profile builder
   */
  public static void parseAcceptableSerializationFormats(final JsonNode node, final BagitProfileBuilder builder){
    final JsonNode serialiationFormats = node.get("Accept-Serialization");
    if(serialiationFormats != null && !(serialiationFormats instanceof NullNode)) {
      for (final JsonNode serialiationFormat : serialiationFormats) {
        builder.addAcceptableMIMESerializationType(serialiationFormat.asText());
      }
    }
    logger.debug(messages.getString("acceptable_serialization_mime_types"), builder.getAcceptableMIMETypes());
  }
}
