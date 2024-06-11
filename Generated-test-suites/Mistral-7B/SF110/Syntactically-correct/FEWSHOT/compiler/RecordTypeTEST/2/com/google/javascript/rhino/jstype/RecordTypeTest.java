package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class RecordTypeTest {
  @Test
  public void testRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
    properties.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    properties.put("prop2", new RecordProperty("prop2", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties, true);
    assertNotNull(recordType);

    assertEquals(2, recordType.getPropertyCount());

    assertTrue(recordType.isDeclared());
    assertFalse(recordType.isFrozen());

    RecordProperty prop1 = recordType.getProperty("prop1");
    assertNotNull(prop1);
    assertEquals(registry.getStringType(), prop1.getType());

    RecordProperty prop2 = recordType.getProperty("prop2");
    assertNotNull(prop2);
    assertEquals(registry.getNumberType(), prop2.getType());
  }

  @Test
  public void testRecordTypeWithSynthesizedProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
    properties.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    properties.put("prop2", new RecordProperty("prop2", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties, false);
    assertNotNull(recordType);

    assertEquals(2, recordType.getPropertyCount());

    assertFalse(recordType.isDeclared());
    assertFalse(recordType.isFrozen());

    RecordProperty prop1 = recordType.getProperty("prop1");
    assertNotNull(prop1);
    assertEquals(registry.getStringType(), prop1.getType());

    RecordProperty prop2 = recordType.getProperty("prop2");
    assertNotNull(prop2);
    assertEquals(registry.getNumberType(), prop2.getType());
  }

  @Test
  public void testRecordEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new java.util.LinkedHashMap<>();
    properties1.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    properties1.put("prop2", new RecordProperty("prop2", registry.getNumberType()));

    Map<String, RecordProperty> properties2 = new java.util.LinkedHashMap<>();
    properties2.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    properties2.put("prop3", new RecordProperty("prop3", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.STRICT));

    RecordType recordType3 = new RecordType(registry, properties1, true);
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.STRICT));
  }

  @Test
  public void testGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
    properties.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    properties.put("prop2", new RecordProperty("prop2", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties, true);

    ObjectType implicitPrototype = recordType.getImplicitPrototype();
    assertNotNull(implicitPrototype);
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), implicitPrototype);
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
    properties.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    properties.put("prop2", new RecordProperty("prop2", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties, true);

    assertFalse(recordType.defineProperty("newProp", registry.getStringType(), false, null));
    assertTrue(recordType.hasProperty("prop1"));
    assertTrue(recordType.hasProperty("prop2"));
    assertFalse(recordType.hasProperty("newProp"));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
    properties.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    properties.put("prop2", new RecordProperty("prop2", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties, true);

    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(registry.getNullType());
    assertNull(greastestSubtype);

    greatestSubtype = recordType.getGreatestSubtypeHelper(registry.getNumberType());
    assertNull(greastestSubtype);

    greatestSubtype = recordType.getGreatestSubtypeHelper(registry.getRecordType(properties));
    assertNotNull(greastestSubtype);
    assertEquals(recordType, greatestSubtype);

    greatestSubtype = recordType.getGreatestSubtypeHelper(registry.getRecordType(properties, true, false));
    assertNotNull(greastestSubtype);
    assertEquals(recordType, greatestSubtype);

    greatestSubtype = recordType.getGreatestSubtypeHelper(registry.getRecordType(new java.util.LinkedHashMap<>()));
    assertNull(greastestSubtype);
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
    properties.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    properties.put("prop2", new RecordProperty("prop2", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, properties, true);
    RecordType recordType2 = new RecordType(registry, properties, true);
    RecordType recordType3 = new RecordType(registry, new java.util.LinkedHashMap<>(), true);
    RecordType recordType4 = new RecordType(registry, properties, false);

    ObjectType objType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    assertTrue(recordType1.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(recordType2));
    assertTrue(recordType1.isSubtype(objType));
    assertTrue(objType.isSubtype(recordType1));
    assertTrue(objType.isSubtype(recordType2));

    assertFalse(recordType1.isSubtype(registry.getRecordType(new java.util.LinkedHashMap<>(), true, false)));
    assertFalse(recordType1.isSubtype(registry.getRecordType(new java.util.LinkedHashMap<>(), true, true)));
    assertFalse(recordType1.isSubtype(registry.getRecordType(new java.util.LinkedHashMap<>(), false, false)));

    assertFalse(recordType1.isSubtype(recordType4));
    assertFalse(recordType4.isSubtype(recordType1));
  }

  @Test
  public void testIsSubtypeWithRecordTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new java.util.LinkedHashMap<>();
    properties1.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    properties1.put("prop2", new RecordProperty("prop2", registry.getNumberType()));

    Map<String, RecordProperty> properties2 = new java.util.LinkedHashMap<>();
    properties2.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    properties2.put("prop3", new RecordProperty("prop3", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    RecordType recordType3 = new RecordType(registry, properties1, false);
    RecordType recordType4 = new RecordType(registry, properties2, false);

    RecordType recordType5 = new RecordType(registry, properties1, true);

    assertTrue(recordType1.isSubtype(recordType5));
    assertTrue(recordType5.isSubtype(recordType1));

    assertFalse(recordType1.isSubtype(recordType2));
    assertFalse(recordType2.isSubtype(recordType1));

    assertFalse(recordType1.isSubtype(recordType3));
    assertFalse(recordType3.isSubtype(recordType1));

    assertFalse(recordType1.isSubtype(recordType4));
    assertFalse(recordType4.isSubtype(recordType1));
  }

  @Test
  public void testIsSubtypeWithRecordTypeAndObjectType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
    properties.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    properties.put("prop2", new RecordProperty("prop2", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties, true);

    ObjectType objType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    assertTrue(recordType.isSubtype(recordType));
    assertTrue(recordType.isSubtype(objType));
    assertTrue(objType.isSubtype(recordType));
  }

  @Test
  public void testIsSubtypeWithRecordTypeAndInterfaceType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
    properties.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    properties.put("prop2", new RecordProperty("prop2", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties, true);

    UnionType unionType = registry.getUnionType(
        registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        registry.getNativeObjectType(JSTypeNative.NULL_TYPE),
        registry.getRecordType(new java.util.LinkedHashMap<>()));

    assertFalse(recordType.isSubtype(unionType));
    assertFalse(unionType.isSubtype(recordType));
  }

  @Test
  public void testIsSubtypeWithRecordTypeAndRecordTypeWithDeclaredProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new java.util.LinkedHashMap<>();
    properties1.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    properties1.put("prop2", new RecordProperty("prop2", registry.getNumberType()));

    Map<String, RecordProperty> properties2 = new java.util.LinkedHashMap<>();
    properties2.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    properties2.put("prop3", new RecordProperty("prop3", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    RecordType recordType3 = new RecordType(registry, properties1, true);

    assertTrue(recordType1.isSubtype(recordType3));
    assertTrue(recordType3.isSubtype(recordType1));

    assertFalse(recordType1.isSubtype(recordType2));
    assertFalse(recordType2.isSubtype(recordType1));

    Set<String> keys1 = new HashSet<>(properties1.keySet());
    Set<String> keys2 = new HashSet<>(properties2.keySet());

    assertEquals(keys1, keys2);
  }
}