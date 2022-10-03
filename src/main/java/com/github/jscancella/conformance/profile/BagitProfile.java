package com.github.jscancella.conformance.profile;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * An immutable object for all the bagit profile fields. 
 * A bagit profile is used to ensure the bag contains all required elements and optional elements specified
 */
@SuppressWarnings({"PMD.UseConcurrentHashMap", "PMD.TooManyFields"})
public class BagitProfile {
  //required elements
  private final URI bagitProfileIdentifier;
  private final String sourceOrganization;
  private final String externalDescription;
  private final String version;
  private final String bagitProfileVersion;
  
  //optional elements
  private final String contactName;
  private final String contactEmail;
  private final String contactPhone;
  private final Map<String, BagInfoRequirement> bagInfoRequirements;
  private final List<String> manifestTypesRequired;
  private final List<String> manifestTypesAllowed;
  private final boolean fetchFileAllowed; //defaults to false
  private final Serialization serialization;
  private final List<String> acceptableMIMESerializationTypes;
  private final List<String> acceptableBagitVersions;
  private final List<String> tagManifestTypesRequired;
  private final List<String> tagManifestTypesAllowed;
  private final List<String> tagFilesRequired;
  private final List<String> tagFilesAllowed; //glob style, defaults to ["*"] which is all
  
  @SuppressWarnings("PMD.ExcessiveParameterList")
  public BagitProfile(final URI bagitProfileIdentifier, 
      final String sourceOrganization, 
      final String externalDescription, 
      final String version,
      final String bagitProfileVersion, 
      final String contactName, 
      final String contactEmail, 
      final String contactPhone,
      final Map<String, BagInfoRequirement> bagInfoRequirements, 
      final List<String> manifestTypesRequired,
      final List<String> manifestTypesAllowed, 
      final boolean fetchFileAllowed, 
      final Serialization serialization,
      final List<String> acceptableMIMESerializationTypes, 
      final List<String> acceptableBagitVersions,
      final List<String> tagManifestTypesRequired, 
      final List<String> tagManifestTypesAllowed, 
      final List<String> tagFilesRequired,
      final List<String> tagFilesAllowed) {
    
    super();
    this.bagitProfileIdentifier = bagitProfileIdentifier;
    this.sourceOrganization = sourceOrganization;
    this.externalDescription = externalDescription;
    this.version = version;
    this.bagitProfileVersion = bagitProfileVersion;
    this.contactName = contactName;
    this.contactEmail = contactEmail;
    this.contactPhone = contactPhone;
    this.bagInfoRequirements = new HashMap<>(bagInfoRequirements);
    this.manifestTypesRequired = new ArrayList<>(manifestTypesRequired);
    this.manifestTypesAllowed = new ArrayList<>(manifestTypesAllowed);
    this.fetchFileAllowed = fetchFileAllowed;
    this.serialization = serialization;
    this.acceptableMIMESerializationTypes = new ArrayList<>(acceptableMIMESerializationTypes);
    this.acceptableBagitVersions = new ArrayList<>(acceptableBagitVersions);
    this.tagManifestTypesRequired = new ArrayList<>(tagManifestTypesRequired);
    this.tagManifestTypesAllowed = new ArrayList<>(tagManifestTypesAllowed);
    this.tagFilesRequired = new ArrayList<>(tagFilesRequired);
    this.tagFilesAllowed = new ArrayList<>(tagFilesAllowed);
  }

  public URI getBagitProfileIdentifier(){
    return bagitProfileIdentifier;
  }

  public String getSourceOrganization(){
    return sourceOrganization;
  }

  public String getExternalDescription(){
    return externalDescription;
  }

  public String getVersion(){
    return version;
  }

  public String getBagitProfileVersion(){
    return bagitProfileVersion;
  }

  public String getContactName(){
    return contactName;
  }

  public String getContactEmail(){
    return contactEmail;
  }

  public String getContactPhone(){
    return contactPhone;
  }

  public Map<String, BagInfoRequirement> getBagInfoRequirements(){
    return Collections.unmodifiableMap(bagInfoRequirements);
  }

  public List<String> getManifestTypesRequired(){
    return Collections.unmodifiableList(manifestTypesRequired);
  }

  public List<String> getManifestTypesAllowed(){
    return Collections.unmodifiableList(manifestTypesAllowed);
  }

  public boolean isFetchFileAllowed(){
    return fetchFileAllowed;
  }

  public Serialization getSerialization(){
    return serialization;
  }

  public List<String> getAcceptableMIMESerializationTypes(){
    return Collections.unmodifiableList(acceptableMIMESerializationTypes);
  }

  public List<String> getAcceptableBagitVersions(){
    return Collections.unmodifiableList(acceptableBagitVersions);
  }

  public List<String> getTagManifestTypesRequired(){
    return Collections.unmodifiableList(tagManifestTypesRequired);
  }

  public List<String> getTagManifestTypesAllowed(){
    return Collections.unmodifiableList(tagManifestTypesAllowed);
  }

  public List<String> getTagFilesRequired(){
    return Collections.unmodifiableList(tagFilesRequired);
  }

  public List<String> getTagFilesAllowed(){
    return Collections.unmodifiableList(tagFilesAllowed);
  }
  
  @Override
  public String toString(){
    return "BagitProfile [bagitProfileIdentifier=" + bagitProfileIdentifier + ", sourceOrganization="
        + sourceOrganization + ", externalDescription=" + externalDescription + ", version=" + version
        + ", bagitProfileVersion=" + bagitProfileVersion + ", contactName=" + contactName + ", contactEmail="
        + contactEmail + ", contactPhone=" + contactPhone + ", bagInfoRequirements=" + bagInfoRequirements
        + ", manifestTypesRequired=" + manifestTypesRequired + ", manifestTypesAllowed=" + manifestTypesAllowed
        + ", fetchFileAllowed=" + fetchFileAllowed + ", serialization=" + serialization
        + ", acceptableMIMESerializationTypes=" + acceptableMIMESerializationTypes + ", acceptableBagitVersions="
        + acceptableBagitVersions + ", tagManifestTypesRequired=" + tagManifestTypesRequired
        + ", tagManifestTypesAllowed=" + tagManifestTypesAllowed + ", tagFilesRequired=" + tagFilesRequired
        + ", tagFilesAllowed=" + tagFilesAllowed + "]";
  }

  @Override
  public int hashCode(){
    return Objects.hash(acceptableBagitVersions, acceptableMIMESerializationTypes, bagInfoRequirements,
        bagitProfileIdentifier, bagitProfileVersion, contactEmail, contactName, contactPhone, externalDescription,
        fetchFileAllowed, manifestTypesAllowed, manifestTypesRequired, serialization, sourceOrganization,
        tagFilesAllowed, tagFilesRequired, tagManifestTypesAllowed, tagManifestTypesRequired, version);
  }

  @Override
  public boolean equals(final Object obj){
    boolean isEqual = false;
    
    if(obj instanceof BagitProfile) {
      final BagitProfile other = (BagitProfile) obj;
      isEqual = Objects.equals(acceptableBagitVersions, other.acceptableBagitVersions)
        && Objects.equals(acceptableMIMESerializationTypes, other.acceptableMIMESerializationTypes)
        && Objects.equals(bagInfoRequirements, other.bagInfoRequirements)
        && Objects.equals(bagitProfileIdentifier, other.bagitProfileIdentifier)
        && Objects.equals(bagitProfileVersion, other.bagitProfileVersion)
        && Objects.equals(contactEmail, other.contactEmail) && Objects.equals(contactName, other.contactName)
        && Objects.equals(contactPhone, other.contactPhone)
        && Objects.equals(externalDescription, other.externalDescription) && fetchFileAllowed == other.fetchFileAllowed
        && Objects.equals(manifestTypesAllowed, other.manifestTypesAllowed)
        && Objects.equals(manifestTypesRequired, other.manifestTypesRequired) && serialization == other.serialization
        && Objects.equals(sourceOrganization, other.sourceOrganization)
        && Objects.equals(tagFilesAllowed, other.tagFilesAllowed)
        && Objects.equals(tagFilesRequired, other.tagFilesRequired)
        && Objects.equals(tagManifestTypesAllowed, other.tagManifestTypesAllowed)
        && Objects.equals(tagManifestTypesRequired, other.tagManifestTypesRequired)
        && Objects.equals(version, other.version);
    }
    return isEqual;
  }
}
