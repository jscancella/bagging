package com.github.jscancella.conformance.profile;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This class is used to define elements in a bag-info.txt file used by a
 * {@link BagitProfile}. Due to specification in version 1.1.0 all entries are
 * required=false and repeatable=true by default. 
 * @see <a href="https://github.com/bagit-profiles/bagit-profiles-specification/tree/1.1.0#implementation-details">BagIt Profiles Specification</a>
 */
public class BagInfoRequirement {
  private final boolean required; // false by default;
  private final List<String> acceptableValues;
  private final boolean repeatable;
  private final String description;
  
  /**
   * Constructs a new BagInfoRequirement.
   *
   * @param required Indicates whether or not the tag is required.
   * @param acceptableValues List of acceptable values.
   * @param repeatable Indicates whether or not the tag is repeatable.
   * @param description a description of the required field
   */
  public BagInfoRequirement(final boolean required, final List<String> acceptableValues, final boolean repeatable, final String description) {
    this.required = required;
    this.acceptableValues = Collections.unmodifiableList(acceptableValues);
    this.repeatable = repeatable;
    this.description = description;
  }

  @Override
  public boolean equals(final Object other) {
    boolean isEqual = false;
    if (other instanceof BagInfoRequirement) {
      final BagInfoRequirement castOther = (BagInfoRequirement) other;
      isEqual = Objects.equals(required, castOther.required)
              && Objects.equals(acceptableValues, castOther.acceptableValues)
              && Objects.equals(repeatable, castOther.repeatable)
              && Objects.equals(description, castOther.description);
    }
    return isEqual;
  }

  @Override
  public int hashCode() {
    return Objects.hash(required, acceptableValues, repeatable, description);
  }

  @Override
  public String toString(){
    return "BagInfoRequirement [required=" + required + ", acceptableValues=" + acceptableValues + ", repeatable="
        + repeatable + ", description=" + description + "]";
  }

  /**
   * @return if the key value pair is required to be present in the bag info file
   */
  public boolean isRequired(){
    return required;
  }

  /**
   * @return the list of acceptable values for the bag info key value pair
   */
  public List<String> getAcceptableValues(){
    return acceptableValues;
  }

  /**
   * @return if the key value pair is repeatable
   */
  public boolean isRepeatable(){
    return repeatable;
  }

  /**
   * @return the description for the key value pair so that users better understand what it is used for.
   */
  public String getDescription(){
    return description;
  }
}
