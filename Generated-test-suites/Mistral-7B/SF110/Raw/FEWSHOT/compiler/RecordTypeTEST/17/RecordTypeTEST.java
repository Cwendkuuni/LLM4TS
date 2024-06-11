package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {
  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testIsSynthetic() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("prop1", new RecordProperty("prop1", Number.TYPE));
    RecordType recordType = new RecordType(registry, properties, false);
    assertTrue(recordType.isSynthetic());

    properties.clear();
    properties.put("prop1", new RecordProperty("prop1", Number.TYPE));
    RecordType declaredRecordType = new RecordType(registry, properties, true);
    assertFalse(declaredRecordType.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("prop1", new RecordProperty("prop1", Number.TYPE));
    RecordType recordType1 = new RecordType(registry, properties1, true);

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("prop1", new RecordProperty("prop1", Number.TYPE));
    properties2.put("prop2", new RecordProperty("prop2", String.TYPE));
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType1, EquivalenceMethod.IDENTITY));
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.IDENTITY));
    assertFalse(recordType1.checkRecordEquivalenceHelper(new ObjectType(registry, null), EquivalenceMethod.IDENTITY));

    RecordType recordType3 = new RecordType(registry, properties1, true);
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.IDENTITY));

    RecordType recordType4 = new RecordType(registry, properties2, true);
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType4, EquivalenceMethod.IDENTITY));
  }

  @Test
  public void testGetImplicitPrototype() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("prop1", new RecordProperty("prop1", Number.TYPE));
    RecordType recordType = new RecordType(registry, properties, true);

    ObjectType implicitPrototype = recordType.getImplicitPrototype();
    assertNotNull(implicitPrototype);
    assertEquals(JSTypeNative.OBJECT_TYPE, implicitPrototype.getTypeName());
  }

  @Test
  public void testDefineProperty() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("prop1", new RecordProperty("prop1", Number.TYPE));
    RecordType recordType = new RecordType(registry, properties, true);

    String propertyName = "prop2";
    JSType propertyType = String.TYPE;
    boolean inferred = false;
    Node propertyNode = null;

    assertFalse(recordType.defineProperty(propertyName, propertyType, inferred, propertyNode));

    recordType.isFrozen = true;
    assertFalse(recordType.defineProperty(propertyName, propertyType, inferred, propertyNode));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("prop1", new RecordProperty("prop1", Number.TYPE));
    RecordType recordType1 = new RecordType(registry, properties1, true);

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("prop1", new RecordProperty("prop1", Number.TYPE));
    properties2.put("prop2", new RecordProperty("prop2", String.TYPE));
    RecordType recordType2 = new RecordType(registry, properties2, true);

    Map<String, RecordProperty> properties3 = new HashMap<>();
    properties3.put("prop1", new RecordProperty("prop1", Number.TYPE));
    properties3.put("prop2", new RecordProperty("prop2", String.TYPE));
    properties3.put("prop3", new RecordProperty("prop3", Boolean.TYPE));
    RecordType recordType3 = new RecordType(registry, properties3, true);

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType1);
    assertEquals(recordType1, greatestSubtype);

    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertTrue(recordType1.isSubtype(recordType2));
    assertEquals(recordType2, greatestSubtype);

    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType3);
    assertFalse(recordType1.isSubtype(recordType3));
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), greatestSubtype);
  }
}