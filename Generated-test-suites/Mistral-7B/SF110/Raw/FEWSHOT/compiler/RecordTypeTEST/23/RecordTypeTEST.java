package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class RecordTypeTest {

  private JSTypeRegistry registry = new JSTypeRegistry();

  @Test
  public void testRecordTypeIsSubtypeOfRecordType() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("x", new RecordProperty("x", registry.getNullType()));
    RecordType recordTypeA = new RecordType(registry, properties, true);

    properties.clear();
    properties.put("x", new RecordProperty("x", registry.getNumberType()));
    RecordType recordTypeB = new RecordType(registry, properties, true);

    assertTrue(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testRecordTypeIsNotSubtypeOfRecordTypeWithFewerProperties() {
    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("x", new RecordProperty("x", registry.getNumberType()));
    RecordType recordTypeA = new RecordType(registry, propertiesA, true);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("x", new RecordProperty("x", registry.getBooleanType()));
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    assertFalse(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testRecordTypeIsSubtypeOfRecordTypeWithInvariantProperties() {
    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("x", new RecordProperty("x", registry.getNumberType()));
    RecordType recordTypeA = new RecordType(registry, propertiesA, true);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("x", new RecordProperty("x", registry.getNumberType()));
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    assertTrue(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testRecordTypeIsNotSubtypeOfRecordTypeWithNonInvariantProperties() {
    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("x", new RecordProperty("x", registry.getNumberType()));
    RecordType recordTypeA = new RecordType(registry, propertiesA, true);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("x", new RecordProperty("x", registry.getBooleanType()));
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    assertFalse(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testRecordTypeIsNotSubtypeOfRecordTypeWithNullProperties() {
    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("x", new RecordProperty("x", registry.getNumberType()));
    RecordType recordTypeA = new RecordType(registry, propertiesA, true);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    RecordType recordTypeB = new RecordType(registry, propertiesB, false);

    assertFalse(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testRecordTypeIsNotSubtypeOfRecordTypeWithDifferentProperties() {
    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("x", new RecordProperty("x", registry.getNumberType()));
    RecordType recordTypeA = new RecordType(registry, propertiesA, true);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("y", new RecordProperty("y", registry.getNumberType()));
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    assertFalse(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testRecordTypeIsSubtypeOfObjectType() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("x", new RecordProperty("x", registry.getNumberType()));
    RecordType recordType = new RecordType(registry, properties, true);

    assertTrue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).isSubtype(recordType));
  }

  @Test
  public void testRecordTypeIsNotSubtypeOfRecordTypeWithFrozenProperties() {
    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("x", new RecordProperty("x", registry.getNumberType()));
    RecordType recordTypeA = new RecordType(registry, propertiesA, true);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("x", new RecordProperty("x", registry.getNumberType()));
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);
    recordTypeB.freeze();

    assertFalse(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testRecordTypeIsSubtypeOfRecordTypeWithSamePropertiesButDifferentOrder() {
    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("y", new RecordProperty("y", registry.getNumberType()));
    propertiesA.put("x", new RecordProperty("x", registry.getNumberType()));
    RecordType recordTypeA = new RecordType(registry, propertiesA, true);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("x", new RecordProperty("x", registry.getNumberType()));
    propertiesB.put("y", new RecordProperty("y", registry.getNumberType()));
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    assertTrue(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testRecordTypeIsSubtypeOfRecordTypeWithDifferentPropertyTypes() {
    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("x", new RecordProperty("x", registry.getNumberType()));
    RecordType recordTypeA = new RecordType(registry, propertiesA, true);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("x", new RecordProperty("x", registry.getBooleanType()));
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    assertFalse(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testRecordTypeIsSubtypeOfRecordTypeWithSynthesizedProperties() {
    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("x", new RecordProperty("x", registry.getNumberType()));
    RecordType recordTypeA = new RecordType(registry, propertiesA, false);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("x", new RecordProperty("x", registry.getNumberType()));
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    assertTrue(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testRecordTypeGetOwnPropertyNames() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("x", new RecordProperty("x", registry.getNumberType()));
    properties.put("y", new RecordProperty("y", registry.getBooleanType()));
    RecordType recordType = new RecordType(registry, properties, true);

    Set<String> propertyNames = recordType.getOwnPropertyNames();

    assertTrue(propertyNames.containsAll(Arrays.asList("x", "y")));
  }

  @Test
  public void testRecordTypeGetPropertyType() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("x", new RecordProperty("x", registry.getNumberType()));
    RecordType recordType = new RecordType(registry, properties, true);

    JSType type = recordType.getPropertyType("x");

    assertTrue(type instanceof NumberType);
  }

  @Test
  public void testRecordTypeGetPropertyTypeNull() {
    Map<String, RecordProperty> properties = new HashMap<>();
    RecordType recordType = new RecordType(registry, properties, false);

    assertNull(recordType.getPropertyType("y"));
  }

  @Test
  public void testRecordTypeGetPropertyNode() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("x", new RecordProperty("x", registry.getNumberType()));
    RecordType recordType = new RecordType(registry, properties, true);

    Node propertyNode = recordType.getPropertyNode("x");

    assertNotNull(propertyNode);
  }

  @Test
  public void testRecordTypeGetPropertyNodeNull() {
    Map<String, RecordProperty> properties = new HashMap<>();
    RecordType recordType = new RecordType(registry, properties, false);

    assertNull(recordType.getPropertyNode("y"));
  }
}