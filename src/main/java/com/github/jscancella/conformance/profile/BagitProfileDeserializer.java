package com.github.jscancella.conformance.profile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.NullNode;

/**
 * Deserialize bagit profile json to a {@link BagitProfile} 
 */
@SuppressWarnings("PMD.TooManyMethods") //TODO refactor into smaller classes instead of so many methods
public class BagitProfileDeserializer extends StdDeserializer<BagitProfile> {
  private static final long serialVersionUID = 1L;
  private static final Logger logger = LoggerFactory.getLogger(BagitProfileDeserializer.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");

  public BagitProfileDeserializer() {
    this(null);
  }

  public BagitProfileDeserializer(final Class<?> valueClass) {
    super(valueClass);
  }

  @Override
  public BagitProfile deserialize(final JsonParser jsonParser, final DeserializationContext context)
      throws IOException, JsonProcessingException {
    final JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    final BagitProfileBuilder builder = new BagitProfileBuilder();
    
    try{
      parseBagitProfileInfo(node, builder);
    } catch(URISyntaxException e){
      throw new IOException(e);
    }
    
    parseBagInfo(node, builder);
    
    parseManifestTypesRequired(node, builder);
    
    parseManifestTypesAllowed(node, builder);
    
    final JsonNode fetchIsAllowed = node.get("Allow-Fetch.txt");
    if(fetchIsAllowed != null && !(fetchIsAllowed instanceof NullNode)) {
      builder.setFetchFileAllowed(fetchIsAllowed.asBoolean());
    }
    logger.debug(messages.getString("fetch_allowed"), builder.isFetchAllowed());
    
    final JsonNode serialization = node.get("Serialization");
    if(serialization != null && !(serialization instanceof NullNode)) {
      builder.setSerialization(Serialization.valueOf(serialization.asText()));
    }
    logger.debug(messages.getString("serialization_allowed"),builder.getSerializationType());
    
    parseAcceptableSerializationFormats(node, builder);
    
    parseAcceptableVersions(node, builder);
    
    parseRequiredTagmanifestTypes(node, builder);
    
    parseAllowedTagmanifestTypes(node, builder);
    
    parseRequiredTagFiles(node, builder);
    
    parseAllowedTagFiles(node, builder);
    
    return builder.build();
  }
  
  private static void parseBagitProfileInfo(final JsonNode node, final BagitProfileBuilder builder) throws URISyntaxException{
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
  
  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  private static void parseBagInfo(final JsonNode rootNode, final BagitProfileBuilder builder){
    final JsonNode bagInfoNode = rootNode.get("Bag-Info");
    if(bagInfoNode != null && !(bagInfoNode instanceof NullNode)) {
      logger.debug(messages.getString("parsing_bag_info"));
      
      final Iterator<Entry<String, JsonNode>> nodes = bagInfoNode.fields(); //stuck in java 6...
      
      while(nodes.hasNext()){
        final Entry<String, JsonNode> node = nodes.next();
        boolean isRequired = false;
        boolean isRepeatable = true;
        final List<String> acceptableValues = new ArrayList<>();
        String description = "";
        
        final JsonNode requiredNode = node.getValue().get("required");
        if (requiredNode != null) {
          isRequired = requiredNode.asBoolean();
        }

        final JsonNode repeatableNode = node.getValue().get("repeatable");
        if (repeatableNode != null) {
          isRepeatable = repeatableNode.asBoolean();
        }
        
        final JsonNode descriptionNode = node.getValue().get("description");
        if (descriptionNode != null) {
          description = descriptionNode.asText();
        }
        
        final JsonNode valuesNode = node.getValue().get("values");
        if(valuesNode != null){
          for(final JsonNode value : valuesNode){
            acceptableValues.add(value.asText());
          }
        }
        
        
        final BagInfoRequirement entry = new BagInfoRequirement(isRequired, acceptableValues, isRepeatable, description);
        
        logger.debug(messages.getString("parsed_key_value"), node.getKey(), entry);
        builder.addBagInfoRequirement(node.getKey(), entry);
      }
    }
  }
  
  private static void parseManifestTypesRequired(final JsonNode node, final BagitProfileBuilder builder){
    final JsonNode manifests = node.get("Manifests-Required");
    if(manifests != null && !(manifests instanceof NullNode)) {
      for (final JsonNode manifestName : manifests) {
        builder.addManifestTypesRequired(manifestName.asText());
      }
    }
    
    logger.debug(messages.getString("required_manifest_types"), builder.getManifestTypesRequired());
  }
  
  private static void parseManifestTypesAllowed(final JsonNode node, final BagitProfileBuilder builder){
    final JsonNode manifests = node.get("Manifests-Allowed");
    if(manifests != null && !(manifests instanceof NullNode)) {
      for (final JsonNode manifestName : manifests) {
        builder.addManifestTypesAllowed(manifestName.asText());
      }
    }
    
    logger.debug(messages.getString("allowed_manifest_types"), builder.getManifestTypesRequired());
  }
  
  private static void parseAcceptableSerializationFormats(final JsonNode node, final BagitProfileBuilder builder){
    final JsonNode serialiationFormats = node.get("Accept-Serialization");
    if(serialiationFormats != null && !(serialiationFormats instanceof NullNode)) {
      for (final JsonNode serialiationFormat : serialiationFormats) {
        builder.addAcceptableMIMESerializationType(serialiationFormat.asText());
      }
    }
    logger.debug(messages.getString("acceptable_serialization_mime_types"), builder.getAcceptableMIMETypes());
  }
  
  private static void parseRequiredTagmanifestTypes(final JsonNode node, final BagitProfileBuilder builder){
    final JsonNode tagManifestsRequiredNodes = node.get("Tag-Manifests-Required");
    if (tagManifestsRequiredNodes != null && !(tagManifestsRequiredNodes instanceof NullNode)) {
      for (final JsonNode tagManifestsRequiredNode : tagManifestsRequiredNodes) {
        builder.addTagManifestTypeRequired(tagManifestsRequiredNode.asText());
      }
    }
    logger.debug(messages.getString("required_tagmanifest_types"), builder.getTagManifestTypesRequired());
  }
  
  private static void parseAllowedTagmanifestTypes(final JsonNode node, final BagitProfileBuilder builder){
    final JsonNode tagManifestsAllowedNodes = node.get("Tag-Manifests-Allowed");
    if (tagManifestsAllowedNodes != null && !(tagManifestsAllowedNodes instanceof NullNode)) {
      for (final JsonNode tagManifestsRequiredNode : tagManifestsAllowedNodes) {
        builder.addTagManifestTypeAllowed(tagManifestsRequiredNode.asText());
      }
    }
    logger.debug(messages.getString("required_tagmanifest_types"), builder.getTagManifestTypesRequired());
  }
  
  private static void parseRequiredTagFiles(final JsonNode node, final BagitProfileBuilder builder){
    final JsonNode tagFilesRequiredNodes = node.get("Tag-Files-Required");

    if (tagFilesRequiredNodes != null && !(tagFilesRequiredNodes instanceof NullNode)) {
      for (final JsonNode tagFilesRequiredNode : tagFilesRequiredNodes) {
        builder.addTagFileRequired(tagFilesRequiredNode.asText());
      }
    }
    logger.debug(messages.getString("tag_files_required"), builder.getTagFilesRequired());
  }
  
  private static void parseAllowedTagFiles(final JsonNode node, final BagitProfileBuilder builder){
    final JsonNode tagFilesAllowedNodes = node.get("Tag-Files-Allowed");

    if (tagFilesAllowedNodes != null && !(tagFilesAllowedNodes instanceof NullNode)) {
      for (final JsonNode tagFilesRequiredNode : tagFilesAllowedNodes) {
        builder.addTagFileAllowed(tagFilesRequiredNode.asText());
      }
    }
    logger.debug(messages.getString("tag_files_required"), builder.getTagFilesRequired());
  }
  
  //required
  private static void parseAcceptableVersions(final JsonNode node, final BagitProfileBuilder builder){
    final JsonNode acceptableVersionsNodes = node.get("Accept-BagIt-Version");
    
    for(final JsonNode acceptableVersionsNode : acceptableVersionsNodes){
      builder.addAcceptableBagitVersion(acceptableVersionsNode.asText());
    }
    logger.debug(messages.getString("acceptable_bagit_versions"), builder.getAcceptableBagitVersions());
  }
}
