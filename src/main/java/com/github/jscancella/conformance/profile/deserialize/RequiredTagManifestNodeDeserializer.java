package com.github.jscancella.conformance.profile.deserialize;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.jscancella.conformance.profile.BagitProfileBuilder;

/**
 * responsible for deserializing "Tag-Manifests-Required" node from bagit profile
 */
public enum RequiredTagManifestNodeDeserializer {
  ;// using enum to ensure singleton
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  private static final Logger logger = LoggerFactory.getLogger(RequiredTagManifestNodeDeserializer.class);
  
  /**
   * @param node the json node to parse
   * @param builder the bagit profile builder
   */
  public static void parseRequiredTagmanifestTypes(final JsonNode node, final BagitProfileBuilder builder){
    final JsonNode tagManifestsRequiredNodes = node.get("Tag-Manifests-Required");
    if (tagManifestsRequiredNodes != null && !(tagManifestsRequiredNodes instanceof NullNode)) {
      for (final JsonNode tagManifestsRequiredNode : tagManifestsRequiredNodes) {
        builder.addTagManifestTypeRequired(tagManifestsRequiredNode.asText());
      }
    }
    logger.debug(messages.getString("required_tagmanifest_types"), builder.getTagManifestTypesRequired());
  }
}
