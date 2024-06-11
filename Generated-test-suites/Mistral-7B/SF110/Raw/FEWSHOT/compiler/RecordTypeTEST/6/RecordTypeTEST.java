package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class RecordTypeTest {
  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType typeA = new ArrayType(JSType.getObjectType(), 1);
    JSType typeB = new RecordType(registry, Arrays.asList(
        new RecordProperty("prop1", new NumberType(registry), null),
        new RecordProperty("prop2", new StringType(registry), null)).asMap());

    assertTrue(typeB.isSubtype(typeA));

    typeB = new RecordType(registry, Arrays.asList(
        new RecordProperty("prop1", new NumberType(registry), null),
        new RecordProperty("prop2", new NumberType(registry), null)).asMap());

    assertTrue(typeB.isSubtype(typeA));

    typeB = new RecordType(registry, Arrays.asList(
        new RecordProperty("prop1", new NumberType(registry), null),
        new RecordProperty("prop2", new StringType(registry), null)).asMap());
    typeA = new RecordType(registry, Arrays.asList(
        new RecordProperty("prop1", new NumberType(registry), null),
        new RecordProperty("prop2", new StringType(registry), null)).asMap());

    assertTrue(typeA.isSubtype(typeB));

    typeB = new RecordType(registry, Arrays.asList(
        new RecordProperty("prop1", new NumberType(registry), null),
        new RecordProperty("prop2", new NumberType(registry), null)).asMap());
    typeA = new RecordType(registry, Arrays.asList(
        new RecordProperty("prop1", new NumberType(registry), null),
        new RecordProperty("prop3", new StringType(registry), null)).asMap());

    assertFalse(typeA.isSubtype(typeB));

    typeB = new RecordType(registry, Arrays.asList(
        new RecordProperty("prop1", new NumberType(registry), null),
        new RecordProperty("prop2", new StringType(registry), null)).asMap());
    typeA = new RecordType(registry, Arrays.asList(
        new RecordProperty("prop1", new NumberType(registry), null),
        new RecordProperty("prop2", new NumberType(registry), null)).asMap());

    assertFalse(typeA.isSubtype(typeB));
  }

  @Test
  public void testIsSubtypeWithInvariantProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType typeA = new RecordType(registry, Arrays.asList(
        new RecordProperty("prop1", new NumberType(registry), null),
        new RecordProperty("prop2", new StringType(registry), null)).asMap());
    JSType typeB = new RecordType(registry, Arrays.asList(
        new RecordProperty("prop1", new NumberType(registry), null),
        new RecordProperty("prop2", new StringType(registry), null),
        new RecordProperty("prop3", new NumberType(registry), null)).asMap());

    assertFalse(typeB.isSubtype(typeA));

    typeB = new RecordType(registry, Arrays.asList(
        new RecordProperty("prop1", new NumberType(registry), true),
        new RecordProperty("prop2", new StringType(registry), null),
        new RecordProperty("prop3", new NumberType(registry), null)).asMap());

    assertTrue(typeB.isSubtype(typeA));
  }

  @Test
  public void testIsSubtypeWithNonInvariantProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType typeA = new RecordType(registry, Arrays.asList(
        new RecordProperty("prop1", new NumberType(registry), null),
        new RecordProperty("prop2", new StringType(registry), null)).asMap());
    JSType typeB = new RecordType(registry, Arrays.asList(
        new RecordProperty("prop1", new NumberType(registry), null),
        new RecordProperty("prop2", new NumberType(registry), null)).asMap());

    assertFalse(typeB.isSubtype(typeA));
  }

  @Test
  public void testIsSubtypeRecordToObject() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType typeA = new RecordType(registry, Arrays.asList(
        new RecordProperty("prop1", new NumberType(registry), null),
        new RecordProperty("prop2", new StringType(registry), null)).asMap());

    assertTrue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).isSubtype(typeA));
  }

  @Test
  public void testIsSubtypeObjectToRecord() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType typeB = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    JSType typeA = new RecordType(registry, Arrays.asList(
        new RecordProperty("prop1", new NumberType(registry), null),
        new RecordProperty("prop2", new StringType(registry), null)).asMap());

    assertFalse(typeA.isSubtype(typeB));
  }

  @Test
  public void testGetOwnPropertyNames() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType type = new RecordType(registry, Arrays.asList(
        new RecordProperty("prop1", new NumberType(registry), null),
        new RecordProperty("prop2", new StringType(registry), null),
        new RecordProperty("prop3", new BooleanType(registry), null)).asMap());

    Set<String> ownProperties = new HashSet<>();
    type.getOwnPropertyNames().forEach(ownProperties::add);

    Set<String> expectedProperties = new HashSet<>(Arrays.asList("prop1", "prop2", "prop3"));

    assertTrue(ownProperties.equals(expectedProperties));
  }
}