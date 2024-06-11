package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

public class RecordTypeTest {

  @Test
  public void testIsSynthetic() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = new java.util.HashMap<>();
    RecordType recordType1 = new RecordType(registry, recordProperties, true);
    RecordType recordType2 = new RecordType(registry, recordProperties, false);

    assertTrue(recordType1.isSynthetic() == false);
    assertTrue(recordType2.isSynthetic() == true);
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordPropertiesA = new java.util.HashMap<>();
    recordPropertiesA.put("prop1", new RecordProperty(null, "prop1", null,
        null, null));
    RecordType recordTypeA = new RecordType(registry, recordPropertiesA, true);

    Map<String, RecordProperty> recordPropertiesB = new java.util.HashMap<>();
    recordPropertiesB.put("prop1", new RecordProperty(null, "prop1", null,
        null, null));
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB, true);

    Map<String, RecordProperty> recordPropertiesC = new java.util.HashMap<>();
    recordPropertiesC.put("prop1", new RecordProperty(null, "prop1", null,
        null, null));
    recordPropertiesC.put("prop2", new RecordProperty(null, "prop2", null,
        null, null));
    RecordType recordTypeC = new RecordType(registry, recordPropertiesC, true);

    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB,
        EquivalenceMethod.STRICT));
    assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeC,
        EquivalenceMethod.STRICT));
  }

  @Test
  public void testGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, new java.util.HashMap<>(),
        true);

    ObjectType expectedPrototype = registry.getNativeObjectType(
        JSTypeNative.OBJECT_TYPE);
    ObjectType actualPrototype = recordType.getImplicitPrototype();

    assertTrue(expectedPrototype.isPrototypeOf(actualPrototype));
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, new java.util.HashMap<>(),
        true);

    String propertyName = "prop1";
    JSType propertyType = registry.getNullType();
    Node propertyNode = new Node();

    boolean result = recordType.defineProperty(propertyName, propertyType,
        false, propertyNode);

    assertTrue(result);
  }

  @Test
  public void testDefinePropertyFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, new java.util.HashMap<>(),
        true);
    recordType.setFrozen(true);

    String propertyName = "prop1";
    JSType propertyType = registry.getNullType();
    Node propertyNode = new Node();

    boolean result = recordType.defineProperty(propertyName, propertyType,
        false, propertyNode);

    assertFalse(result);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordPropertiesA = new java.util.HashMap<>();
    recordPropertiesA.put("prop1", new RecordProperty(null, "prop1", null,
        null, null));
    RecordType recordTypeA = new RecordType(registry, recordPropertiesA, true);

    Map<String, RecordProperty> recordPropertiesB = new java.util.HashMap<>();
    recordPropertiesB.put("prop1", new RecordProperty(null, "prop1", null,
        null, null));
    recordPropertiesB.put("prop2", new RecordProperty(null, "prop2", null,
        null, null));
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB, true);

    UnionType expectedGreatestSubtype = new UnionType(registry, null);
    expectedGreatestSubtype.addAlternate(recordTypeA);
    expectedGreatestSubtype.addAlternate(recordTypeB);

    JSType actualGreatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);

    assertTrue(expectedGreatestSubtype.equals(actualGreatestSubtype));
  }

  @Test
  public void testGetGreatestSubtypeHelperEmptyObjectType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType expectedGreatestSubtype = registry.getNativeObjectType(
        JSTypeNative.OBJECT_TYPE);

    JSType actualGreatestSubtype = new RecordType(registry, new java.util.HashMap<>(),
        true).getGreatestSubtypeHelper(registry.getNativeObjectType(
            JSTypeNative.OBJECT_TYPE));

    assertTrue(expectedGreatestSubtype.equals(actualGreatestSubtype));
  }

  @Test
  public void testGetGreatestSubtypeHelperNoRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType expectedGreatestSubtype = registry.getNativeType(
        JSTypeNative.NO_OBJECT_TYPE);

    JSType actualGreatestSubtype = new RecordType(registry, new java.util.HashMap<>(),
        true).getGreatestSubtypeHelper(registry.getIntType());

    assertTrue(expectedGreatestSubtype.equals(actualGreatestSubtype));
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordPropertiesA = new java.util.HashMap<>();
    recordPropertiesA.put("prop1", new RecordProperty(null, "prop1", null,
        null, null));
    RecordType recordTypeA = new RecordType(registry, recordPropertiesA, true);

    Map<String, RecordProperty> recordPropertiesB = new java.util.HashMap<>();
    recordPropertiesB.put("prop1", new RecordProperty(null, "prop1", null,
        null, null));
    recordPropertiesB.put("prop2", new RecordProperty(null, "prop2", null,
        null, null));
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB, true);

    Map<String, RecordProperty> recordPropertiesC = new java.util.HashMap<>();
    recordPropertiesC.put("prop1", new RecordProperty(null, "prop1", null,
        null, null));
    recordPropertiesC.put("prop2", new RecordProperty(null, "prop2", null,
        null, null));
    recordPropertiesC.put("prop3", new RecordProperty(null, "prop3", null,
        null, null));
    RecordType recordTypeC = new RecordType(registry, recordPropertiesC, true);

    Map<String, RecordProperty> recordPropertiesD = new java.util.HashMap<>();
    recordPropertiesD.put("prop1", new RecordProperty(null, "prop1", null,
        null, null));
    RecordType recordTypeD = new RecordType(registry, recordPropertiesD, true);

    Map<String, RecordProperty> recordPropertiesE = new java.util.HashMap<>();
    recordPropertiesE.put("prop1", new RecordProperty(null, "prop1", null,
        null, null));
    recordPropertiesE.put("prop2", new RecordProperty(null, "prop2", null,
        null, null));
    recordPropertiesE.put("prop3", new RecordProperty(null, "prop3", null,
        null, null));
    recordPropertiesE.put("prop4", new RecordProperty(null, "prop4", null,
        null, null));
    RecordType recordTypeE = new RecordType(registry, recordPropertiesE, true);

    assertTrue(recordTypeA.isSubtype(recordTypeA));
    assertTrue(recordTypeA.isSubtype(registry.getNativeObjectType(
        JSTypeNative.OBJECT_TYPE)));
    assertTrue(recordTypeA.isSubtype(recordTypeB));
    assertFalse(recordTypeA.isSubtype(registry.getIntType()));
    assertFalse(recordTypeA.isSubtype(recordTypeC));
    assertFalse(recordTypeA.isSubtype(recordTypeD));
    assertFalse(recordTypeA.isSubtype(recordTypeE));

    assertTrue(recordTypeB.isSubtype(recordTypeA));
    assertTrue(recordTypeB.isSubtype(registry.getNativeObjectType(
        JSTypeNative.OBJECT_TYPE)));
    assertTrue(recordTypeB.isSubtype(recordTypeB));
    assertTrue(recordTypeB.isSubtype(recordTypeC));
    assertFalse(recordTypeB.isSubtype(recordTypeD));
    assertFalse(recordTypeB.isSubtype(recordTypeE));

    assertTrue(recordTypeC.isSubtype(recordTypeA));
    assertTrue(recordTypeC.isSubtype(recordTypeB));
    assertTrue(recordTypeC.isSubtype(recordTypeC));
    assertTrue(recordTypeC.isSubtype(recordTypeD));
    assertFalse(recordTypeC.isSubtype(recordTypeE));

    assertTrue(recordTypeD.isSubtype(recordTypeA));
    assertTrue(recordTypeD.isSubtype(recordTypeB));
    assertTrue(recordTypeD.isSubtype(recordTypeC));
    assertTrue(recordTypeD.isSubtype(recordTypeD));
    assertFalse(recordTypeD.isSubtype(recordTypeE));

    assertTrue(recordTypeE.isSubtype(recordTypeA));
    assertTrue(recordTypeE.isSubtype(recordTypeB));
    assertTrue(recordTypeE.isSubtype(recordTypeC));
    assertTrue(recordTypeE.isSubtype(recordTypeD));
    assertTrue(recordTypeE.isSubtype(recordTypeE));
  }

  @Test
  public void testToMaybeRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new java.util.HashMap<>();
    recordProperties.put("prop1", new RecordProperty(null, "prop1", null,
        null, null));
    RecordType recordType = new RecordType(registry, recordProperties, true);

    RecordType expectedRecordType = recordType;
    RecordType actualRecordType = recordType.toMaybeRecordType();

    assertTrue(expectedRecordType.equals(actualRecordType));
  }
}