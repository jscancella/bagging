package com.github.jscancella.conformance.profile;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.jscancella.domain.Version;

public abstract class AbstractBagitProfileTest {
  protected ObjectMapper mapper;
  
  @BeforeEach
  public void setup(){
    mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addDeserializer(BagitProfile.class, new BagitProfileDeserializer());
    mapper.registerModule(module);
  }
  
  protected BagitProfile createExpectedProfile(Version version){
    BagitProfileBuilder builder = new BagitProfileBuilder();
    
    try{
      builder.setBagitProfileIdentifier(new URI("http://canadiana.org/standards/bagit/tdr_ingest.json"))
      .setSourceOrganization("Candiana.org")
      .seContactName("William Wueppelmann")
      .setContactEmail("tdr@canadiana.com")      
      .setContactPhone("+1 613 907 7040")
      .setExternalDescription("BagIt profile for ingesting content into the C.O. TDR loading dock.")      
      .setVersion("1.2")
      .setBagitProfileVersion(version.toString() + ".0")
      .addManifestTypesRequired("md5")
      .setFetchFileAllowed(false)
      .setSerialization(Serialization.forbidden)
      .addAcceptableMIMESerializationType("application/zip")
      .addTagManifestTypeRequired("md5")
      .addTagFileRequired("DPN/dpnFirstNode.txt").addTagFileRequired("DPN/dpnRegistry")
      .addAcceptableBagitVersion("0.96");
    } catch(URISyntaxException e){
      throw new RuntimeException(e);
    }
    
    //starting with version 1.3.0 these were added
    if(new Version(1, 3).isSameOrOlder(version)) {
      builder.addManifestTypesAllowed("sha1").addManifestTypesAllowed("md5")
      .addTagManifestTypeAllowed("sha1").addTagManifestTypeAllowed("md5")
      .addTagFileAllowed("DPN/dpnFirstNode.txt").addTagFileAllowed("DPN/dpnRegistry");
      
      builder.addBagInfoRequirement("Source-Organization", new BagInfoRequirement(true, Arrays.asList("Simon Fraser University", "York University"), false, "the originator of the bag"));
    }
    else {
      builder.addBagInfoRequirement("Source-Organization", new BagInfoRequirement(true, Arrays.asList("Simon Fraser University", "York University"), false, ""));
    }
    
    //starting with version 1.4.0 these were added
    if(new Version(1,4).isSameOrOlder(version)) {
      builder.setFetchFileRequired(false)
      .setDataDirMustBeEmpty(false);
    }
    
    return builder.build();
  }
  
  protected BagitProfile createMinimalProfile(String version){
    BagitProfileBuilder builder = new BagitProfileBuilder();
    
    try{
      builder.setBagitProfileIdentifier(new URI("http://canadiana.org/standards/bagit/tdr_ingest.json"))
      .setSourceOrganization("Candiana.org")
      .setExternalDescription("BagIt profile for ingesting content into the C.O. TDR loading dock.")
      .setVersion("1.2")
      .setBagitProfileVersion(version)
      .addAcceptableBagitVersion("0.96");      
    } catch(URISyntaxException e){
      throw new RuntimeException(e);
    }
    
    return builder.build();
  }
}
