package com.github.jscancella.conformance.profile.deserialize;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.jscancella.conformance.profile.BagitProfileBuilder;

/**
 * responsible for deserializing "Tag-Manifests-Allowed" node from bagit profile
 */
public enum AllowedTagmanifestTypesNodeDeserializer {
  ;// using enum to ensure singleton
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  private static final Logger logger = LoggerFactory.getLogger(AllowedTagmanifestTypesNodeDeserializer.class);
  
  /**
   * @param node the json node to parse
   * @param builder the bagit profile builder
   */
  public static void parseAllowedTagmanifestTypes(final JsonNode node, final BagitProfileBuilder builder){
    final JsonNode tagManifestsAllowedNodes = node.get("Tag-Manifests-Allowed");
    if (tagManifestsAllowedNodes != null && !(tagManifestsAllowedNodes instanceof NullNode)) {
      for (final JsonNode tagManifestsRequiredNode : tagManifestsAllowedNodes) {
        builder.addTagManifestTypeAllowed(tagManifestsRequiredNode.asText());
      }
    }
    logger.debug(messages.getString("required_tagmanifest_types"), builder.getTagManifestTypesRequired());
  }
}
