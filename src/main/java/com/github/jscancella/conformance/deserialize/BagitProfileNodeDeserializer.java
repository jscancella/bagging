package com.github.jscancella.conformance.deserialize;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.jscancella.conformance.profile.BagitProfileBuilder;

/**
 * responsible for deserializing "BagIt-Profile-Info" node from bagit profile
 */
public enum BagitProfileNodeDeserializer {
  ;// using enum to ensure singleton
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  private static final Logger logger = LoggerFactory.getLogger(BagitProfileNodeDeserializer.class);
  
  /**
   * @param node the json node to parse
   * @param builder the bagit profile builder
   * @throws URISyntaxException is thrown if "BagIt-Profile-Identifier" is incorrect
   */
  public static void parseBagitProfileInfo(final JsonNode node, final BagitProfileBuilder builder) throws URISyntaxException{
    logger.debug(messages.getString("parsing_bagit_profile_info_section"));

    final JsonNode bagitProfileInfoNode = node.get("BagIt-Profile-Info");
    parseMandatoryTagsOfBagitProfileInfo(bagitProfileInfoNode, builder);
    parseOptionalTagsOfBagitProfileInfo(bagitProfileInfoNode, builder);
  }
  
  /**
   * Parse required tags due to specification version 1.1.0 defined at
   * {@link "https://github.com/bagit-profiles/bagit-profiles-specification/tree/1.1.0"}
   * Note: If one of the tags is missing, a NullPointerException is thrown.
   *
   * @param bagitProfileInfoNode Root node of the bagit profile info section.
   * @param builder 
   * @throws URISyntaxException 
   */
  private static void parseMandatoryTagsOfBagitProfileInfo(final JsonNode bagitProfileInfoNode, final BagitProfileBuilder builder) throws URISyntaxException {
    logger.debug(messages.getString("parsing_mandatory_tags_of_bagit_profile_info_section"));
    
    // Read required tags
    // due to specification defined at https://github.com/bagit-profiles/bagit-profiles-specification/tree/1.1.0
    final String profileIdentifierText = bagitProfileInfoNode.get("BagIt-Profile-Identifier").asText();
    final URI profileIdentifier = new URI(profileIdentifierText);
    logger.debug(messages.getString("identifier"), profileIdentifierText);
    builder.setBagitProfileIdentifier(profileIdentifier);
    
    final String sourceOrg = bagitProfileInfoNode.get("Source-Organization").asText();
    logger.debug(messages.getString("source_organization"), sourceOrg);
    builder.setSourceOrganization(sourceOrg);
    
    final String extDescript = bagitProfileInfoNode.get("External-Description").asText();
    logger.debug(messages.getString("external_description"), extDescript);
    builder.setExternalDescription(extDescript);
    
    final String version = bagitProfileInfoNode.get("Version").asText();
    logger.debug(messages.getString("version"), version);
    builder.setVersion(version);
    
    //since version 1.2.0
    final String profileVersion = bagitProfileInfoNode.get("BagIt-Profile-Version").asText("1.1.0");
    logger.debug(messages.getString("profile_version"), profileVersion);
    builder.setBagitProfileVersion(profileVersion);
  }
  
  private static void parseOptionalTagsOfBagitProfileInfo(final JsonNode bagitProfileInfoNode, final BagitProfileBuilder builder) {
    logger.debug(messages.getString("parsing_optional_tags_of_bagit_profile_info_section"));

    final JsonNode contactNameNode = bagitProfileInfoNode.get("Contact-Name");
    if (contactNameNode != null && !(contactNameNode instanceof NullNode)) {
      final String contactName = contactNameNode.asText();
      logger.debug(messages.getString("contact_name"), contactName);
      builder.seContactName(contactName);
    }

    final JsonNode contactEmailNode = bagitProfileInfoNode.get("Contact-Email");
    if (contactEmailNode != null && !(contactEmailNode instanceof NullNode)) {
      final String contactEmail = contactEmailNode.asText();
      logger.debug(messages.getString("contact_email"), contactEmail);
      builder.setContactEmail(contactEmail);
    }

    final JsonNode contactPhoneNode = bagitProfileInfoNode.get("Contact-Phone");
    if (contactPhoneNode != null && !(contactPhoneNode instanceof NullNode)) {
      final String contactPhone = contactPhoneNode.asText();
      logger.debug(messages.getString("contact_phone"), contactPhone);
      builder.setContactPhone(contactPhone);
    }
  }
}
