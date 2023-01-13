package com.github.jscancella.conformance.profile.deserialize;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.jscancella.conformance.profile.BagitProfileBuilder;

/**
 * responsible for deserializing "Accept-BagIt-Version" node from bagit profile
 */
public enum AcceptableVersionsNodeDeserializer {
  ;// using enum to ensure singleton
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  private static final Logger logger = LoggerFactory.getLogger(AcceptableVersionsNodeDeserializer.class);
  
  /**
   * @param node the json node to parse
   * @param builder the bagit profile builder
   */
  public static void parseAcceptableVersions(final JsonNode node, final BagitProfileBuilder builder){
    final JsonNode acceptableVersionsNodes = node.get("Accept-BagIt-Version");
    
    for(final JsonNode acceptableVersionsNode : acceptableVersionsNodes){
      builder.addAcceptableBagitVersion(acceptableVersionsNode.asText());
    }
    logger.debug(messages.getString("acceptable_bagit_versions"), builder.getAcceptableBagitVersions());
  }
}
