package com.github.jscancella.conformance.profile.deserialize;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.jscancella.conformance.profile.BagitProfileBuilder;

/**
 * responsible for deserializing "Tag-Files-Allowed" node from bagit profile
 */
public enum AllowedTagFilesNodeDeserializer {
  ;// using enum to ensure singleton
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  private static final Logger logger = LoggerFactory.getLogger(AllowedTagFilesNodeDeserializer.class);
  
  /**
   * @param node the json node to parse
   * @param builder the bagit profile builder
   */
  public static void parseAllowedTagFiles(final JsonNode node, final BagitProfileBuilder builder){
    final JsonNode tagFilesAllowedNodes = node.get("Tag-Files-Allowed");

    if (tagFilesAllowedNodes != null && !(tagFilesAllowedNodes instanceof NullNode)) {
      for (final JsonNode tagFilesRequiredNode : tagFilesAllowedNodes) {
        builder.addTagFileAllowed(tagFilesRequiredNode.asText());
      }
    }
    logger.debug(messages.getString("tag_files_required"), builder.getTagFilesRequired());
  }
}
