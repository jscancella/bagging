package com.github.jscancella.conformance.deserialize;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.jscancella.conformance.profile.BagitProfileBuilder;

/**
 * responsible for deserializing "Tag-Files-Required" node from bagit profile
 */
public enum RequiredTagFilesNodeDeserializer {
  ;// using enum to ensure singleton
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  private static final Logger logger = LoggerFactory.getLogger(RequiredTagFilesNodeDeserializer.class);
  
  /**
   * @param node the json node to parse
   * @param builder the bagit profile builder
   */
  public static void parseRequiredTagFiles(final JsonNode node, final BagitProfileBuilder builder){
    final JsonNode tagFilesRequiredNodes = node.get("Tag-Files-Required");

    if (tagFilesRequiredNodes != null && !(tagFilesRequiredNodes instanceof NullNode)) {
      for (final JsonNode tagFilesRequiredNode : tagFilesRequiredNodes) {
        builder.addTagFileRequired(tagFilesRequiredNode.asText());
      }
    }
    logger.debug(messages.getString("tag_files_required"), builder.getTagFilesRequired());
  }
}
