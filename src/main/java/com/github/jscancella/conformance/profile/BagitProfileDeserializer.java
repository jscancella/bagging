package com.github.jscancella.conformance.profile;

import java.io.IOException;
import java.net.URISyntaxException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.jscancella.conformance.deserialize.AcceptableVersionsNodeDeserializer;
import com.github.jscancella.conformance.deserialize.AllowedTagFilesNodeDeserializer;
import com.github.jscancella.conformance.deserialize.AllowedTagmanifestTypesNodeDeserializer;
import com.github.jscancella.conformance.deserialize.BagInfoNodeDeserializer;
import com.github.jscancella.conformance.deserialize.BagitProfileNodeDeserializer;
import com.github.jscancella.conformance.deserialize.DataDirEmptyNodeDeserializer;
import com.github.jscancella.conformance.deserialize.FetchFileAllowedNodeDeserializer;
import com.github.jscancella.conformance.deserialize.FetchFileRequiredNodeDeserializer;
import com.github.jscancella.conformance.deserialize.ManifestTypesAllowedNodeDeserializer;
import com.github.jscancella.conformance.deserialize.ManifestTypesRequiredNodeDeserializer;
import com.github.jscancella.conformance.deserialize.RequiredTagFilesNodeDeserializer;
import com.github.jscancella.conformance.deserialize.RequiredTagManifestNodeDeserializer;
import com.github.jscancella.conformance.deserialize.SerializationAcceptedNodeDeserializer;
import com.github.jscancella.conformance.deserialize.SerializationAllowedNodeDeserializer;

/**
 * Deserialize bagit profile json to a {@link BagitProfile} 
 */
public class BagitProfileDeserializer extends StdDeserializer<BagitProfile> {
  private static final long serialVersionUID = 1L;

  /**
   * Deserialize bagit profile json to a {@link BagitProfile} 
   */
  public BagitProfileDeserializer() {
    this(null);
  }

  /**
   * Deserialize bagit profile json to a {@link BagitProfile} 
   * @param valueClass Type of values this deserializer handles
   */
  public BagitProfileDeserializer(final Class<?> valueClass) {
    super(valueClass);
  }

  @Override
  public BagitProfile deserialize(final JsonParser jsonParser, final DeserializationContext context)
      throws IOException, JsonProcessingException {
    final JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    final BagitProfileBuilder builder = new BagitProfileBuilder();
    
    try{
      BagitProfileNodeDeserializer.parseBagitProfileInfo(node, builder);
    } catch(URISyntaxException e){
      throw new IOException(e);
    }
    
    BagInfoNodeDeserializer.parseBagInfo(node, builder);
    
    ManifestTypesRequiredNodeDeserializer.parseManifestTypesRequired(node, builder);
    
    ManifestTypesAllowedNodeDeserializer.parseManifestTypesAllowed(node, builder);
    
    FetchFileAllowedNodeDeserializer.parseFetchFileAllowed(node, builder);
    
    FetchFileRequiredNodeDeserializer.parseFetchFileRequired(node, builder);
    
    DataDirEmptyNodeDeserializer.parseDataDirEmpty(node, builder);
    
    SerializationAllowedNodeDeserializer.parseSerializationAllowed(node, builder);
    
    SerializationAcceptedNodeDeserializer.parseAcceptableSerializationFormats(node, builder);
    
    AcceptableVersionsNodeDeserializer.parseAcceptableVersions(node, builder);
    
    RequiredTagManifestNodeDeserializer.parseRequiredTagmanifestTypes(node, builder);
    
    AllowedTagmanifestTypesNodeDeserializer.parseAllowedTagmanifestTypes(node, builder);
    
    RequiredTagFilesNodeDeserializer.parseRequiredTagFiles(node, builder);
    
    AllowedTagFilesNodeDeserializer.parseAllowedTagFiles(node, builder);
    
    return builder.build();
  }
    
}
