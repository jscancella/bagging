package com.github.jscancella.conformance.profile;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
  private Map<String, BagInfoRequirement> bagInfoRequirements = new ConcurrentHashMap<>();
  private List<String> manifestTypesRequired = new ArrayList<>();
  private List<String> manifestTypesAllowed = new ArrayList<>();
  private boolean fetchFileAllowed; //defaults to false
  private Serialization serialization = Serialization.optional;
  private List<String> acceptableMIMESerializationTypes = new ArrayList<>();
  private List<String> acceptableBagitVersions = new ArrayList<>();
  private List<String> tagManifestTypesRequired = new ArrayList<>();
  private List<String> tagManifestTypesAllowed = new ArrayList<>();
  private List<String> tagFilesRequired = new ArrayList<>();
  private List<String> tagFilesAllowed = new ArrayList<>(); //glob style, defaults to ["*"] which is all
  
  public BagitProfile build() {
    return new BagitProfile(bagitProfileIdentifier, sourceOrganization, externalDescription, version, bagitProfileVersion, 
        contactName, contactEmail, contactPhone, bagInfoRequirements, manifestTypesRequired, manifestTypesAllowed, fetchFileAllowed, 
        serialization, acceptableMIMESerializationTypes, acceptableBagitVersions, tagManifestTypesRequired, tagManifestTypesAllowed, 
        tagFilesRequired, tagFilesAllowed);
  }
  
  public BagitProfileBuilder bagitProfileIdentifier(final URI bagitProfileIdentifier) {
    this.bagitProfileIdentifier = bagitProfileIdentifier;
    return this;
  }
  
  public BagitProfileBuilder sourceOrganization(final String sourceOrganization) {
    this.sourceOrganization = sourceOrganization;
    return this;
  }
  
  public BagitProfileBuilder externalDescription(final String externalDescription) {
    this.externalDescription = externalDescription;
    return this;
  }
  
  public BagitProfileBuilder version(final String version) {
    this.version = version;
    return this;
  }
  
  public BagitProfileBuilder bagitProfileVersion(final String bagitProfileVersion) {
    this.bagitProfileVersion = bagitProfileVersion;
    return this;
  }
  
  public BagitProfileBuilder contactName(final String contactName) {
    this.contactName = contactName;
    return this;
  }
  
  public BagitProfileBuilder contactEmail(final String contactEmail) {
    this.contactEmail = contactEmail;
    return this;
  }
  
  public BagitProfileBuilder contactPhone(final String contactPhone) {
    this.contactPhone = contactPhone;
    return this;
  }
  
  public BagitProfileBuilder addBagInfoRequirement(final String name, final BagInfoRequirement requirement) {
    this.bagInfoRequirements.put(name, requirement);
    return this;
  }
  
  public BagitProfileBuilder addManifestTypesRequired(final String manifestType) {
    this.manifestTypesRequired.add(manifestType);
    return this;
  }
  
  public BagitProfileBuilder addManifestTypesAllowed(final String manifestType) {
    this.manifestTypesAllowed.add(manifestType);
    return this;
  }
  
  public BagitProfileBuilder fetchFileAllowed(final boolean isAllowed) {
    this.fetchFileAllowed = isAllowed;
    return this;
  }
  
  public BagitProfileBuilder serialization(final Serialization serialization) {
    this.serialization = serialization;
    return this;
  }
  
  public BagitProfileBuilder addAcceptableMIMESerializationType(final String type) {
    this.acceptableMIMESerializationTypes.add(type);
    return this;
  }
  
  public BagitProfileBuilder addAcceptableBagitVersion(final String version) {
    this.acceptableBagitVersions.add(version);
    return this;
  }
  
  public BagitProfileBuilder addTagManifestTypeRequired(final String type) {
    this.tagManifestTypesRequired.add(type);
    return this;
  }
  
  public BagitProfileBuilder addTagManifestTypeAllowed(final String type) {
    this.tagManifestTypesAllowed.add(type);
    return this;
  }
  
  public BagitProfileBuilder addTagFileRequired(final String file) {
    this.tagFilesRequired.add(file);
    return this;
  }
  
  public BagitProfileBuilder addTagFileAllowed(final String file) {
    this.tagFilesAllowed.add(file);
    return this;
  }

  public List<String> getManifestTypesRequired(){
    return manifestTypesRequired;
  }

  public boolean isFetchAllowed(){
    return fetchFileAllowed;
  }

  public Serialization getSerializationType(){
    return serialization;
  }

  public List<String> getAcceptableMIMETypes(){
    return acceptableMIMESerializationTypes;
  }

  public List<String> getAcceptableBagitVersions(){
    return acceptableBagitVersions;
  }

  public List<String> getTagManifestTypesRequired(){
    return tagManifestTypesRequired;
  }

  public List<String> getTagFilesRequired(){
    return tagFilesRequired;
  }
}
