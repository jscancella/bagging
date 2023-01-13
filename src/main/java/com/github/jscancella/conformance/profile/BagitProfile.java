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
  private final boolean fetchFileAllowed; //defaults to true
  private final boolean fetchFileRequired; //defaults to false
  private final boolean dataDirMustBeEmpty; //defaults to false. i.e. only thing allowed is data/.keep
  private final Serialization serialization;
  private final List<String> acceptableMIMESerializationTypes;
  private final List<String> acceptableBagitVersions;
  private final List<String> tagManifestTypesRequired;
  private final List<String> tagManifestTypesAllowed;
  private final List<String> tagFilesRequired;
  private final List<String> tagFilesAllowed; //glob style, defaults to ["*"] which is all
  
  /**
   * An immutable object for all the bagit profile fields. 
   * see https://bagit-profiles.github.io/bagit-profiles-specification/#implementation-details
   * 
   * @param bagitProfileIdentifier the URI where the Profile file is available, and will have the same value as the "BagIt-Profile-Identifier" tag in bag-info.txt
   * @param sourceOrganization taken from the reserved tags defined in [RFC8493] section 2.2.2. 
   * @param externalDescription taken from the reserved tags defined in [RFC8493] section 2.2.2. 
   * @param version the version of the bagit profile specification. Since the tag was introduced after version [v1.1.0], any profile not explicitly defining BagIt-Profile-Version should be treated as conforming to version [1.1.0] of this specification. 
   * @param bagitProfileVersion the version of the Profile
   * @param contactName as defined in [RFC8493] section 2.2.2
   * @param contactEmail as defined in [RFC8493] section 2.2.2 
   * @param contactPhone as defined in [RFC8493] section 2.2.2 
   * @param bagInfoRequirements Specifies which tags are required, etc. in bag-info.txt
   * @param manifestTypesRequired Each manifest type in LIST is required
   * @param manifestTypesAllowed If specified, only the manifest types in LIST are permitted
   * @param fetchFileAllowed A fetch.txt file is allowed within the bag
   * @param fetchFileRequired a fetch.txt file is required to be within the bag
   * @param dataDirMustBeEmpty if the data directory must be empty
   * @param serialization Allow, forbid or require serialization of Bags
   * @param acceptableMIMESerializationTypes A list of MIME types acceptable as serialized formats
   * @param acceptableBagitVersions A list of BagIt version numbers that will be accepted
   * @param tagManifestTypesRequired Each tag manifest type in LIST is required
   * @param tagManifestTypesAllowed If specified, only the tag manifest types in LIST are permitted
   * @param tagFilesRequired A list of a tag files that must be included in a conformant Bag
   * @param tagFilesAllowed A list of tag files that MAY be included in a conformant Bag
   */
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
      final boolean fetchFileRequired,
      final boolean dataDirMustBeEmpty,
      final Serialization serialization,
      final List<String> acceptableMIMESerializationTypes, 
      final List<String> acceptableBagitVersions,
      final List<String> tagManifestTypesRequired, 
      final List<String> tagManifestTypesAllowed, 
      final List<String> tagFilesRequired,
      final List<String> tagFilesAllowed) {
    
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
    this.fetchFileRequired = fetchFileRequired;
    this.dataDirMustBeEmpty = dataDirMustBeEmpty;
    this.serialization = serialization;
    this.acceptableMIMESerializationTypes = new ArrayList<>(acceptableMIMESerializationTypes);
    this.acceptableBagitVersions = new ArrayList<>(acceptableBagitVersions);
    this.tagManifestTypesRequired = new ArrayList<>(tagManifestTypesRequired);
    this.tagManifestTypesAllowed = new ArrayList<>(tagManifestTypesAllowed);
    this.tagFilesRequired = new ArrayList<>(tagFilesRequired);
    this.tagFilesAllowed = new ArrayList<>(tagFilesAllowed);
  }

  /**
   * @return the URI where the Profile file is available, and will have the same value as the "BagIt-Profile-Identifier" tag in bag-info.txt
   */
  public URI getBagitProfileIdentifier(){
    return bagitProfileIdentifier;
  }

  /**
   * @return taken from the reserved tags defined in [RFC8493] section 2.2.2.
   */
  public String getSourceOrganization(){
    return sourceOrganization;
  }

  /**
   * @return taken from the reserved tags defined in [RFC8493] section 2.2.2.
   */
  public String getExternalDescription(){
    return externalDescription;
  }

  /**
   * @return the version of the bagit profile specification. 
   * Since the tag was introduced after version [v1.1.0], any profile not explicitly defining BagIt-Profile-Version 
   * should be treated as conforming to version [1.1.0] of this specification.
   */
  public String getVersion(){
    return version;
  }

  /**
   * @return the version of this profile
   */
  public String getBagitProfileVersion(){
    return bagitProfileVersion;
  }

  /**
   * @return as defined in [RFC8493] section 2.2.2,
   */
  public String getContactName(){
    return contactName;
  }

  /**
   * @return as defined in [RFC8493] section 2.2.2,
   */
  public String getContactEmail(){
    return contactEmail;
  }

  /**
   * @return as defined in [RFC8493] section 2.2.2,
   */
  public String getContactPhone(){
    return contactPhone;
  }

  /**
   * @return Specifies which tags are required, etc. in bag-info.txt
   */
  public Map<String, BagInfoRequirement> getBagInfoRequirements(){
    return Collections.unmodifiableMap(bagInfoRequirements);
  }

  /**
   * @return Each manifest type in LIST is required. The list contains the type of manifest (not the complete filename), e.g. ["sha1", "md5"]. 
   */
  public List<String> getManifestTypesRequired(){
    return Collections.unmodifiableList(manifestTypesRequired);
  }

  /**
   * @return  If specified, only the manifest types in LIST are permitted. 
   * The list contains the type of manifest (not the complete filename), e.g. ["sha1", "md5"]. 
   * When specified along with Manifests-Required, Manifests-Allowed must include at least all of the manifest types listed in Manifests-Required. 
   * If not specified, all manifest types are permitted. 
   */
  public List<String> getManifestTypesAllowed(){
    return Collections.unmodifiableList(manifestTypesAllowed);
  }

  /**
   * @return A fetch.txt file is allowed within the bag. Default: true
   */
  public boolean isFetchFileAllowed(){
    return fetchFileAllowed;
  }
  
  /**
   * @return the fetch.txt file is required to be in the bag
   */
  public boolean isFetchFileRequired() {
    return fetchFileRequired;
  }
  
  /**
   * @return the data directory in the bag must be empty. Default: false
   */
  public boolean isDataDirMustBeEmpty() {
	return dataDirMustBeEmpty;
  }

  /**
   * @return Allow, forbid or require serialization of Bags. Default is optional. 
   */
  public Serialization getSerialization(){
    return serialization;
  }

  /**
   * @return A list of MIME types acceptable as serialized formats. E.g. "application/zip". 
   * If serialization has a value of required or optional, at least one value is needed. 
   * If serialization is forbidden, this has no meaning. 
   */
  public List<String> getAcceptableMIMESerializationTypes(){
    return Collections.unmodifiableList(acceptableMIMESerializationTypes);
  }

  /**
   * @return A list of BagIt version numbers that will be accepted. At least one version is required. 
   */
  public List<String> getAcceptableBagitVersions(){
    return Collections.unmodifiableList(acceptableBagitVersions);
  }

  /**
   * @return Each tag manifest type in LIST is required. 
   * The list contains the type of manifest (not the complete filename), e.g. ["sha1", "md5"]. 
   */
  public List<String> getTagManifestTypesRequired(){
    return Collections.unmodifiableList(tagManifestTypesRequired);
  }

  /**
   * @return  If specified, only the tag manifest types in LIST are permitted. 
   * The list contains the type of manifest (not the complete filename), e.g. ["sha1", "md5"]. 
   * When specified along with Tag-Manifests-Required, Tag-Manifests-Allowed must include at least all of the tag manifest 
   * types listed in Tag-Manifests-Required. If not specified, all tag manifest types are permitted. 
   */
  public List<String> getTagManifestTypesAllowed(){
    return Collections.unmodifiableList(tagManifestTypesAllowed);
  }

  /**
   * @return  A list of a tag files that must be included in a conformant Bag. 
   * Entries are full path names relative to the Bag base directory. 
   * As per [RFC8493] section 2.2.4, these tag files need not be listed in tag manifiest files. 
   * Tag-Files-Required SHOULD NOT include bag-info.txt (which is always required), 
   * nor any required manifest files, which instead are required by Manifests-Required and Tag-Manifests-Required.
   * Every file in Tag-Files-Required must also be present in Tag-Files-Allowed. 
   */
  public List<String> getTagFilesRequired(){
    return Collections.unmodifiableList(tagFilesRequired);
  }

  /**
   * @return  A list of tag files that MAY be included in a conformant Bag. 
   * Entries are either full path names relative to the bag base directory or path name patterns in 
   * which asterisks can represent zero or more characters (c.f. glob(7)).
   * If Tag-Files-Allowed is not provided, its value is assumed to be ['*'], i.e. all tag files are allowed.
   * As per [RFC8493] section 2, these tag files need not be listed in tag manifest files. 
   * Tag-Files-Required SHOULD NOT include bag-info.txt (which is always required), nor any required manifest files, 
   * which instead are required by Manifests-Required and Tag-Manifests-Required.
   * At least all the tag files listed in Tag-Files-Required must be in included in Tag-Files-Allowed. 
   */
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
