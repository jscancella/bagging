package com.github.jscancella.conformance.deserialize;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.jscancella.conformance.profile.BagitProfileBuilder;

/**
 * responsible for deserializing "Manifests-Allowe" node from bagit profile
 */
public enum ManifestTypesAllowedNodeDeserializer {
  ;// using enum to ensure singleton
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  private static final Logger logger = LoggerFactory.getLogger(ManifestTypesAllowedNodeDeserializer.class);
  
  /**
   * @param node the json node to parse
   * @param builder the bagit profile builder
   */
  public static void parseManifestTypesAllowed(final JsonNode node, final BagitProfileBuilder builder){
    final JsonNode manifests = node.get("Manifests-Allowed");
    if(manifests != null && !(manifests instanceof NullNode)) {
      for (final JsonNode manifestName : manifests) {
        builder.addManifestTypesAllowed(manifestName.asText());
      }
    }
    
    logger.debug(messages.getString("allowed_manifest_types"), builder.getManifestTypesRequired());
  }
}
