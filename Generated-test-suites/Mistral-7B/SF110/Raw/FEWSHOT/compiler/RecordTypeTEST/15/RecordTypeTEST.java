package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class RecordTypeTest {
  @Test
  public void testRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, createProperties());
    assertNotNull(recordType);
    assertTrue(recordType instanceof RecordType);
    assertTrue(recordType.isFrozen());
  }

  @Test
  public void testIsSynthetic() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, createProperties(), false);
    assertFalse(recordType.isSynthetic());

    RecordType synthesizedRecordType = new RecordType(registry, createProperties());
    assertTrue(synthesizedRecordType.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType1 = new RecordType(registry, createProperties());
    RecordType recordType2 = new RecordType(registry, createProperties());
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.IDENTITY));
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, createProperties());
    String propertyName = "testProperty";
    JSType jstype = registry.getNullType();
    Node propertyNode = new Node_Property();

    assertFalse(recordType.defineProperty(propertyName, jstype, false, propertyNode));
    assertFalse(recordType.hasProperty(propertyName));

    recordType.defineProperty(propertyName, jstype, true, propertyNode);
    assertTrue(recordType.hasProperty(propertyName));
  }

  @Test
  public void testGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, createProperties());
    ObjectType implicitPrototype = recordType.getImplicitPrototype();
    assertEquals(JSTypeNative.OBJECT_TYPE, implicitPrototype.getClassName());
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType1 = new RecordType(registry, createProperties());
    RecordType recordType2 = new RecordType(registry, createProperties());
    RecordType recordType3 = new RecordType(registry, createProperties());

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertEquals(JSTypeNative.OBJECT_TYPE, greatestSubtype);

    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType3);
    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType1);
    assertEquals(recordType1, greatestSubtype);
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType1 = new RecordType(registry, createProperties());
    RecordType recordType2 = new RecordType(registry, createProperties());
    RecordType recordType3 = new RecordType(registry, createProperties());

    assertTrue(recordType1.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(recordType2));
    assertTrue(recordType1.isSubtype(recordType3));
    assertTrue(recordType2.isSubtype(recordType1));
    assertTrue(recordType2.isSubtype(recordType2));
    assertTrue(recordType2.isSubtype(recordType3));
    assertTrue(recordType3.isSubtype(recordType1));
    assertTrue(recordType3.isSubtype(recordType2));
    assertTrue(recordType3.isSubtype(recordType3));

    assertFalse(recordType1.isSubtype(registry.getNativeBooleanType()));
    assertFalse(recordType1.isSubtype(registry.getNativeNumberType()));
    assertFalse(recordType1.isSubtype(registry.getNativeStringType()));
    assertFalse(registry.getNativeObjectType(JSTypeNative.ARRAY_TYPE)
        .isSubtype(recordType1));
  }

  @Test
  public void testIsSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType1 = new RecordType(registry, createProperties());
    RecordType recordType2 = new RecordType(registry, createProperties());
    RecordType recordType3 = new RecordType(registry, createProperties());
    RecordType recordType4 = new RecordType(registry, createProperties());
    RecordType recordType5 = new RecordType(registry, createProperties());

    assertTrue(recordType1.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(recordType2));
    assertTrue(recordType1.isSubtype(recordType3));
    assertTrue(recordType1.isSubtype(recordType4));
    assertTrue(recordType1.isSubtype(recordType5));

    assertFalse(recordType1.isSubtype(registry.getNativeBooleanType()));
    assertFalse(recordType1.isSubtype(registry.getNativeNumberType()));
    assertFalse(recordType1.isSubtype(registry.getNativeStringType()));
    assertFalse(registry.getNativeObjectType(JSTypeNative.ARRAY_TYPE)
        .isSubtype(recordType1));

    assertFalse(recordType2.isSubtype(recordType1));
    assertFalse(recordType2.isSubtype(recordType3));
    assertFalse(recordType2.isSubtype(recordType4));
    assertFalse(recordType2.isSubtype(recordType5));

    assertFalse(recordType3.isSubtype(recordType1));
    assertFalse(recordType3.isSubtype(recordType2));
    assertFalse(recordType3.isSubtype(recordType4));
    assertFalse(recordType3.isSubtype(recordType5));

    assertFalse(recordType4.isSubtype(recordType1));
    assertFalse(recordType4.isSubtype(recordType2));
    assertFalse(recordType4.isSubtype(recordType3));
    assertFalse(recordType4.isSubtype(recordType5));

    assertFalse(recordType5.isSubtype(recordType1));
    assertFalse(recordType5.isSubtype(recordType2));
    assertFalse(recordType5.isSubtype(recordType3));
    assertFalse(recordType5.isSubtype(recordType4));
  }

  private static Map<String, RecordProperty> createProperties() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordProperty("property1", registry.getNativeStringType()));
    properties.put("property2", new RecordProperty("property2", registry.getNativeNumberType()));
    return properties;
  }
}