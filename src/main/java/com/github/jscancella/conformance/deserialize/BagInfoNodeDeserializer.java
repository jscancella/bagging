package com.github.jscancella.conformance.deserialize;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.jscancella.conformance.profile.BagInfoRequirement;
import com.github.jscancella.conformance.profile.BagitProfileBuilder;

/**
 * responsible for deserializing "Bag-Info" node from bagit profile
 */
public enum BagInfoNodeDeserializer {
  ;// using enum to ensure singleton
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  private static final Logger logger = LoggerFactory.getLogger(BagInfoNodeDeserializer.class);
  
  /**
   * @param rootNode the json node to parse
   * @param builder the bagit profile builder
   */
  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  public static void parseBagInfo(final JsonNode rootNode, final BagitProfileBuilder builder){
    final JsonNode bagInfoNode = rootNode.get("Bag-Info");
    if(bagInfoNode != null && !(bagInfoNode instanceof NullNode)) {
      logger.debug(messages.getString("parsing_bag_info"));
      
      final Iterator<Entry<String, JsonNode>> nodes = bagInfoNode.fields(); //stuck in java 6...
      
      while(nodes.hasNext()){
        final Entry<String, JsonNode> node = nodes.next();
        final boolean isRequired = determineIsRequired(node);
        final boolean isRepeatable = determineIsRepeatable(node);
        final List<String> acceptableValues = determineAcceptableValues(node);
        final String description = determineDescription(node);
        
        final BagInfoRequirement entry = new BagInfoRequirement(isRequired, acceptableValues, isRepeatable, description);
        
        logger.debug(messages.getString("parsed_key_value"), node.getKey(), entry);
        builder.addBagInfoRequirement(node.getKey(), entry);
      }
    }
  }
  
  private static boolean determineIsRequired(final Entry<String, JsonNode> node) {
    final JsonNode requiredNode = node.getValue().get("required");
    boolean isRequired = false;
    
    if (requiredNode != null) {
      isRequired = requiredNode.asBoolean();
    }
    
    return isRequired;
  }
  
  private static boolean determineIsRepeatable(final Entry<String, JsonNode> node) {
    final JsonNode repeatableNode = node.getValue().get("repeatable");
    boolean isRepeatable = true;
    
    if (repeatableNode != null) {
      isRepeatable = repeatableNode.asBoolean();
    }
    
    return isRepeatable;
  }
  
  private static List<String> determineAcceptableValues(final Entry<String, JsonNode> node) {
    final JsonNode valuesNode = node.getValue().get("values");
    final List<String> acceptableValues = new ArrayList<>();
    
    if(valuesNode != null){
      for(final JsonNode value : valuesNode){
        acceptableValues.add(value.asText());
      }
    }
    
    return acceptableValues;
  }
  
  private static String determineDescription(final Entry<String, JsonNode> node) {
    final JsonNode descriptionNode = node.getValue().get("description");
    String description = "";
    
    if (descriptionNode != null) {
      description = descriptionNode.asText();
    }
    
    return description;
  }
}
