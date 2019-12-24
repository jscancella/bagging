package com.github.jscancella.domain;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A class to represent the bag-info.txt (and package-info.txt in older versions)
 */
@SuppressWarnings({"PMD.UseLocaleWithCaseConversions", "PMD.UseConcurrentHashMap"})
public class Metadata {
  private static final String PAYLOAD_OXUM = "Payload-Oxum";
  private final Map<String, List<String>> map;
  private final List<SimpleImmutableEntry<String, String>> list;
  private final String cachedString;
  
  private Metadata(final Map<String, List<String>> map, final List<SimpleImmutableEntry<String, String>> list) {
    this.map = Collections.unmodifiableMap(map);
    this.list = Collections.unmodifiableList(list);
    cachedString = String.join(",", list.stream().map(o -> o.toString()).collect(Collectors.toList()));
  }
  
  @Override
  public String toString() {
    return cachedString;
  }

  @Override
  public int hashCode() {
    return Objects.hash(list);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj){return true;}
    if (obj == null){return false;}
    if (!(obj instanceof Metadata)){return false;}
    
    final Metadata other = (Metadata) obj;
    return Objects.equals(this.list, other.list);
  }
  
  /**
   * all the metadata
   * 
   * @return return the order and case preserved metadata
   */
  public List<SimpleImmutableEntry<String, String>> getAll(){
    return list;
  }
  
  /**
   * get all the values for a specific label (key)
   * 
   * @param key the case insensitive label(key) in the metadata
   * 
   * @return the list of values for that label
   */
  public List<String> get(final String key){
    return map.get(key.toUpperCase());
  }
  
  /**
   * check if the metadata contains a particular label(key)
   * 
   * @param key the label to check
   * @return if the label exists
   */
  public boolean contains(final String key){
    return map.keySet().contains(key.toUpperCase());
  }
  
  /**
   * @return true if this metadata contains no entries
   */
  public boolean isEmpty(){
    return list.isEmpty();
  }
  
  /**
   * Programmatically and dynamically create metadata
   */
  public static final class MetadataBuilder {
    private final Map<String, List<String>> map = new HashMap<>();
    private List<SimpleImmutableEntry<String, String>> list = new ArrayList<>();
    
    /**
     * programmatically build the metadata
     */
    public MetadataBuilder() {
      //intentionally left empty
    }
    
    /**
     * programmatically build the metadata, but start off with the included metadata
     * @param metadata the data to start with when building
     */
    public MetadataBuilder(final Metadata metadata) {
      this.addAll(metadata.getAll());
    }
    /**
     * remove the label and all its values
     * 
     * @param key the label to remove along with its value(s)
     * @return this builder so to chain commands
     */
    public MetadataBuilder remove(final String key){
      map.remove(key.toUpperCase());
      final List<SimpleImmutableEntry<String, String>> newList = new ArrayList<>();
      
      for(final SimpleImmutableEntry<String, String> entry : list){
        if(!entry.getKey().equalsIgnoreCase(key)){
          newList.add(entry);
        }
      }
      list = newList;
      
      return this;
    }
    
    /**
     * add a entry into the metadata or append a value if the label already exists
     * 
     * @param key the label
     * @param value the value of the label
     * 
     * @return <tt>true</tt> (as specified by {@link Collection#add})
     */
    public MetadataBuilder add(final String key, final String value){
      if(PAYLOAD_OXUM.equalsIgnoreCase(key)){
        this.remove(PAYLOAD_OXUM);
      }
      
      final String upperCaseKey = key.toUpperCase();
      if(map.get(upperCaseKey) == null){
        map.put(upperCaseKey, new ArrayList<>());
      }
      map.get(upperCaseKey).add(value);
      list.add(new SimpleImmutableEntry<>(key, value));
      
      return this;
    }
    
    /**
     * add multiple metadata entries
     * 
     * @param data the metadata to add
     * @return this builder so to chain commands
     */
    public MetadataBuilder addAll(final List<SimpleImmutableEntry<String, String>> data){
      for(final SimpleImmutableEntry<String, String> entry : data){
        this.add(entry.getKey(), entry.getValue());
      }
      
      return this;
    }
    
    /**
     * @return create the metadata object
     */
    public Metadata build() {
      return new Metadata(map, list);
    }
  }
}
