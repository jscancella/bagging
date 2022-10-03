package com.github.jscancella.conformance.profile;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Used to create a bagit profile programmatically and incrementally
 */
@SuppressWarnings({"PMD.TooManyMethods", "PMD.TooManyFields", "PMD.BeanMembersShouldSerialize", "PMD.LinguisticNaming"})
public class BagitProfileBuilder {
  private URI bagitProfileIdentifier;
  private String sourceOrganization = "";
  private String externalDescription = "";
  private String version = "";
  private String bagitProfileVersion = "";
  
  //optional elements
  private String contactName = "";
  private String contactEmail = "";
  private String contactPhone = "";
  private final Map<String, BagInfoRequirement> bagInfoRequirements = new ConcurrentHashMap<>();
  private final List<String> manifestTypesRequired = new ArrayList<>();
  private final List<String> manifestTypesAllowed = new ArrayList<>();
  private boolean fetchFileAllowed; //defaults to false
  private Serialization serialization = Serialization.optional;
  private final List<String> acceptableMIMESerializationTypes = new ArrayList<>();
  private final List<String> acceptableBagitVersions = new ArrayList<>();
  private final List<String> tagManifestTypesRequired = new ArrayList<>();
  private final List<String> tagManifestTypesAllowed = new ArrayList<>();
  private final List<String> tagFilesRequired = new ArrayList<>();
  private final List<String> tagFilesAllowed = new ArrayList<>(); //glob style, defaults to ["*"] which is all
  
  /**
   * @return build the immuntable {@link BagitProfile}
   */
  public BagitProfile build() {
    return new BagitProfile(bagitProfileIdentifier, sourceOrganization, externalDescription, version, bagitProfileVersion, 
        contactName, contactEmail, contactPhone, bagInfoRequirements, manifestTypesRequired, manifestTypesAllowed, fetchFileAllowed, 
        serialization, acceptableMIMESerializationTypes, acceptableBagitVersions, tagManifestTypesRequired, tagManifestTypesAllowed, 
        tagFilesRequired, tagFilesAllowed);
  }
  
  /**
   * @param bagitProfileIdentifier the URI where the Profile file is available, and will have the same value as the "BagIt-Profile-Identifier" tag in bag-info.txt
   * @return this builder so that calls can be chained
   */
  public BagitProfileBuilder setBagitProfileIdentifier(final URI bagitProfileIdentifier) {
    this.bagitProfileIdentifier = bagitProfileIdentifier;
    return this;
  }
  
  /**
   * @param sourceOrganization taken from the reserved tags defined in [RFC8493] section 2.2.2. 
   * @return this builder so that calls can be chained
   */
  public BagitProfileBuilder setSourceOrganization(final String sourceOrganization) {
    this.sourceOrganization = sourceOrganization;
    return this;
  }
  
  /**
   * @param externalDescription taken from the reserved tags defined in [RFC8493] section 2.2.2. 
   * @return this builder so that calls can be chained
   */
  public BagitProfileBuilder setExternalDescription(final String externalDescription) {
    this.externalDescription = externalDescription;
    return this;
  }
  
  /**
   * @param version the version of the bagit profile specification. 
   * Since the tag was introduced after version [v1.1.0], any profile not explicitly defining BagIt-Profile-Version 
   * should be treated as conforming to version [1.1.0] of this specification.
   * @return this builder so that calls can be chained
   */
  public BagitProfileBuilder setVersion(final String version) {
    this.version = version;
    return this;
  }
  
  /**
   * @param bagitProfileVersion the version of this Profile
   * @return this builder so that calls can be chained
   */
  public BagitProfileBuilder setBagitProfileVersion(final String bagitProfileVersion) {
    this.bagitProfileVersion = bagitProfileVersion;
    return this;
  }
  
  /**
   * @param contactName as defined in [RFC8493] section 2.2.2
   * @return this builder so that calls can be chained
   */
  public BagitProfileBuilder seContactName(final String contactName) {
    this.contactName = contactName;
    return this;
  }
  
  /**
   * @param contactEmail as defined in [RFC8493] section 2.2.2
   * @return this builder so that calls can be chained
   */
  public BagitProfileBuilder setContactEmail(final String contactEmail) {
    this.contactEmail = contactEmail;
    return this;
  }
  
  /**
   * @param contactPhone as defined in [RFC8493] section 2.2.2
   * @return this builder so that calls can be chained
   */
  public BagitProfileBuilder setContactPhone(final String contactPhone) {
    this.contactPhone = contactPhone;
    return this;
  }
  
  /**
   * Specifies which tags are required, etc. in bag-info.txt
   * 
   * @param name the name of tag file
   * @param requirement the type of requirement for the tag file
   * @return this builder so that calls can be chained
   */
  public BagitProfileBuilder addBagInfoRequirement(final String name, final BagInfoRequirement requirement) {
    this.bagInfoRequirements.put(name, requirement);
    return this;
  }
  
  /**
   * @param manifestType Each manifest type in LIST is required
   * @return this builder so that calls can be chained
   */
  public BagitProfileBuilder addManifestTypesRequired(final String manifestType) {
    this.manifestTypesRequired.add(manifestType);
    return this;
  }
  
  /**
   * @param manifestType If specified, only the manifest types in LIST are permitted
   * @return this builder so that calls can be chained
   */
  public BagitProfileBuilder addManifestTypesAllowed(final String manifestType) {
    this.manifestTypesAllowed.add(manifestType);
    return this;
  }
  
  /**
   * @param isAllowed is fetch.txt file is allowed within the bag?
   * @return this builder so that calls can be chained
   */
  public BagitProfileBuilder setFetchFileAllowed(final boolean isAllowed) {
    this.fetchFileAllowed = isAllowed;
    return this;
  }
  
  /**
   * @param serialization Allow, forbid or require serialization of Bags
   * @return this builder so that calls can be chained
   */
  public BagitProfileBuilder setSerialization(final Serialization serialization) {
    this.serialization = serialization;
    return this;
  }
  
  /**
   * @param type A MIME type that is acceptable as serialized formats
   * @return this builder so that calls can be chained
   */
  public BagitProfileBuilder addAcceptableMIMESerializationType(final String type) {
    this.acceptableMIMESerializationTypes.add(type);
    return this;
  }
  
  /**
   * @param version A BagIt version number that will be accepted
   * @return this builder so that calls can be chained
   */
  public BagitProfileBuilder addAcceptableBagitVersion(final String version) {
    this.acceptableBagitVersions.add(version);
    return this;
  }
  
  /**
   * @param type Each tag manifest type in LIST is required
   * @return this builder so that calls can be chained
   */
  public BagitProfileBuilder addTagManifestTypeRequired(final String type) {
    this.tagManifestTypesRequired.add(type);
    return this;
  }
  
  /**
   * @param type If specified, only the tag manifest types in LIST are permitted
   * @return this builder so that calls can be chained
   */
  public BagitProfileBuilder addTagManifestTypeAllowed(final String type) {
    this.tagManifestTypesAllowed.add(type);
    return this;
  }
  
  /**
   * @param file A tag file that must be included in a conformant Bag
   * @return this builder so that calls can be chained
   */
  public BagitProfileBuilder addTagFileRequired(final String file) {
    this.tagFilesRequired.add(file);
    return this;
  }
  
  /**
   * @param file A tag file that MAY be included in a conformant Bag
   * @return this builder so that calls can be chained
   */
  public BagitProfileBuilder addTagFileAllowed(final String file) {
    this.tagFilesAllowed.add(file);
    return this;
  }

  /**
   * @return the current list of manifest types that are required
   */
  public List<String> getManifestTypesRequired(){
    return new ArrayList<>(manifestTypesRequired);
  }

  /**
   * @return if fetch files are currently allowed
   */
  public boolean isFetchAllowed(){
    return fetchFileAllowed;
  }

  /**
   * @return the type of serialization that is currently configured
   */
  public Serialization getSerializationType(){
    return serialization;
  }

  /**
   * @return the list of acceptable MIME types currently configured
   */
  public List<String> getAcceptableMIMETypes(){
    return new ArrayList<>(acceptableMIMESerializationTypes);
  }

  /**
   * @return the list of acceptable bagit versions that are currently configured
   */
  public List<String> getAcceptableBagitVersions(){
    return new ArrayList<>(acceptableBagitVersions);
  }

  /**
   * @return the tag manifest types that are required that are currently configured
   */
  public List<String> getTagManifestTypesRequired(){
    return new ArrayList<>(tagManifestTypesRequired);
  }

  /**
   * @return the tag files that are required that are currently configured
   */
  public List<String> getTagFilesRequired(){
    return new ArrayList<>(tagFilesRequired);
  }
}
