package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class RecordTypeTest {
  @Test
  public void testRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("foo", new RecordProperty(registry.getStringType(), null));
    properties.put("bar", new RecordProperty(registry.getNumberType(), null));

    RecordType recordType = new RecordType(registry, properties, true);
    assertNotNull(recordType);

    RecordType syntheticRecordType = new RecordType(registry, properties, false);
    assertNotNull(syntheticRecordType);

    assertEquals(recordType, recordType.toMaybeRecordType());
    assertEquals(syntheticRecordType, syntheticRecordType.toMaybeRecordType());

    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        recordType.getImplicitPrototype());
    assertNotSame(recordType, recordType.getImplicitPrototype());

    assertEquals(recordType, recordType.toMaybeRecordType());
    assertEquals(syntheticRecordType, syntheticRecordType.toMaybeRecordType());

    assertEquals(recordType.getClass(), syntheticRecordType.getClass());

    assertFalse(recordType.isSynthetic());
    assertTrue(syntheticRecordType.isSynthetic());

    RecordType recordType2 = new RecordType(registry, properties, true);
    assertEquals(recordType, recordType2);
    assertEquals(recordType.hashCode(), recordType2.hashCode());
  }

  @Test
  public void testGetOwnPropertyNames() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("foo", new RecordProperty(registry.getStringType(), null));
    properties.put("bar", new RecordProperty(registry.getNumberType(), null));

    RecordType recordType = new RecordType(registry, properties, true);

    Set<String> keys = recordType.getOwnPropertyNames();
    assertEquals(2, keys.size());
    assertTrue(keys.containsAll(Arrays.asList("foo", "bar")));
  }

  @Test
  public void testGetPropertyType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("foo", new RecordProperty(registry.getStringType(), null));
    properties.put("bar", new RecordProperty(registry.getNumberType(), null));

    RecordType recordType = new RecordType(registry, properties, true);

    JSType type = recordType.getPropertyType("foo");
    assertEquals(registry.getStringType(), type);

    type = recordType.getPropertyType("bar");
    assertEquals(registry.getNumberType(), type);

    type = recordType.getPropertyType("baz");
    assertNull(type);
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("foo", new RecordProperty(registry.getStringType(), null));
    properties.put("bar", new RecordProperty(registry.getNumberType(), null));

    RecordType recordType = new RecordType(registry, properties, true);

    recordType.defineProperty("baz", registry.getStringType(), false, null);

    assertTrue(recordType.hasProperty("baz"));
    assertNull(recordType.getPropertyNode("baz"));
  }

  @Test(expected = IllegalStateException.class)
  public void testDefinePropertyWithNullPropertyNode() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("foo", new RecordProperty(registry.getStringType(), null));
    properties.put("bar", new RecordProperty(registry.getNumberType(), null));

    RecordType recordType = new RecordType(registry, properties, true);

    recordType.defineProperty("baz", registry.getStringType(), false, null);
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType recordTypeA = new RecordType(registry, new HashMap<>(), true);
    RecordType recordTypeB = new RecordType(registry, new HashMap<>(), true);
    RecordType recordTypeC = new RecordType(registry, new HashMap<>(), true);
    RecordType recordTypeD = new RecordType(registry, new HashMap<>(), false);

    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    assertTrue(recordTypeA.isSubtype(recordTypeA));
    assertTrue(recordTypeA.isSubtype(objectType));
    assertTrue(objectType.isSubtype(recordTypeA));

    assertTrue(recordTypeA.isSubtype(recordTypeB));
    assertTrue(recordTypeB.isSubtype(recordTypeA));
    assertTrue(recordTypeA.isSubtype(recordTypeC));
    assertTrue(recordTypeC.isSubtype(recordTypeA));

    assertFalse(recordTypeA.isSubtype(recordTypeD));
    assertFalse(recordTypeD.isSubtype(recordTypeA));

    RecordType recordTypeE = new RecordType(registry, new HashMap<>(), true);
    RecordType recordTypeF = new RecordType(registry, new HashMap<>(), true);
    RecordType recordTypeG = new RecordType(registry, new HashMap<>(), false);

    recordTypeE.defineProperty("foo", registry.getStringType(), false, null);
    recordTypeF.defineProperty("foo", registry.getStringType(), false, null);

    assertTrue(recordTypeE.isSubtype(recordTypeF));
    assertTrue(recordTypeF.isSubtype(recordTypeE));

    recordTypeE.defineProperty("bar", registry.getNumberType(), false, null);

    assertFalse(recordTypeF.isSubtype(recordTypeE));
    assertTrue(recordTypeE.isSubtype(recordTypeF));

    recordTypeG.defineProperty("foo", registry.getStringType(), false, null);
    recordTypeG.defineProperty("bar", registry.getNumberType(), false, null);

    assertFalse(recordTypeE.isSubtype(recordTypeG));
    assertFalse(recordTypeG.isSubtype(recordTypeE));

    UnionType unionType = new UnionTypeBuilder(registry).build();
    RecordType recordTypeH = new RecordType(registry, new HashMap<>(), true);
    RecordType recordTypeI = new RecordType(registry, new HashMap<>(), true);

    recordTypeH.defineProperty("foo", registry.getStringType(), false, null);
    recordTypeI.defineProperty("foo", registry.getStringType(), false, null);

    recordTypeI.addAlternate(recordTypeH);

    assertEquals(recordTypeH, recordTypeI.toMaybeRecordType());
    assertEquals(recordTypeI, recordTypeH.toMaybeRecordType());

    assertTrue(recordTypeI.isSubtype(recordTypeH));
    assertTrue(recordTypeH.isSubtype(recordTypeI));

    assertFalse(recordTypeE.isSubtype(unionType));
    assertFalse(unionType.isSubtype(recordTypeE));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType recordTypeA = new RecordType(registry, new HashMap<>(), true);
    RecordType recordTypeB = new RecordType(registry, new HashMap<>(), true);
    RecordType recordTypeC = new RecordType(registry, new HashMap<>(), true);
    RecordType recordTypeD = new RecordType(registry, new HashMap<>(), false);

    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    JSType greatestSubtypeA = recordTypeA.getGreatestSubtypeHelper(objectType);
    assertEquals(objectType, greatestSubtypeA);

    greatestSubtypeA = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
    assertEquals(recordTypeB, greatestSubtypeA);

    greatestSubtypeA = recordTypeB.getGreatestSubtypeHelper(recordTypeA);
    assertEquals(recordTypeB, greatestSubtypeA);

    greatestSubtypeA = recordTypeB.getGreatestSubtypeHelper(recordTypeC);
    assertEquals(recordTypeC, greatestSubtypeA);

    greatestSubtypeA = recordTypeC.getGreatestSubtypeHelper(recordTypeB);
    assertEquals(recordTypeC, greatestSubtypeA);

    greatestSubtypeA = recordTypeC.getGreatestSubtypeHelper(recordTypeD);
    assertEquals(recordTypeD, greatestSubtypeA);

    greatestSubtypeA = recordTypeD.getGreatestSubtypeHelper(recordTypeC);
    assertEquals(recordTypeD, greatestSubtypeA);

    greatestSubtypeA = recordTypeD.getGreatestSubtypeHelper(recordTypeB);
    assertEquals(recordTypeD, greatestSubtypeA);

    greatestSubtypeA = recordTypeD.getGreatestSubtypeHelper(recordTypeA);
    assertEquals(recordTypeD, greatestSubtypeA);

    greatestSubtypeA = recordTypeD.getGreatestSubtypeHelper(objectType);
    assertEquals(objectType, greatestSubtypeA);

    RecordType recordTypeE = new RecordType(registry, new HashMap<>(), true);
    recordTypeE.defineProperty("foo", registry.getStringType(), false, null);

    RecordType recordTypeF = new RecordType(registry, new HashMap<>(), true);
    recordTypeF.defineProperty("foo", registry.getStringType(), false, null);

    UnionType unionType = new UnionTypeBuilder(registry).build();
    RecordType recordTypeG = new RecordType(registry, new HashMap<>(), true);
    RecordType recordTypeH = new RecordType(registry, new HashMap<>(), true);

    recordTypeG.defineProperty("foo", registry.getStringType(), false, null);
    recordTypeH.defineProperty("foo", registry.getStringType(), false, null);

    recordTypeH.addAlternate(recordTypeG);

    JSType greatestSubtypeG = recordTypeG.getGreatestSubtypeHelper(recordTypeH);
    assertEquals(recordTypeH, greatestSubtypeG);

    greatestSubtypeG = recordTypeG.getGreatestSubtypeHelper(recordTypeF);
    assertEquals(recordTypeF, greatestSubtypeG);

    greatestSubtypeG = recordTypeF.getGreatestSubtypeHelper(recordTypeG);
    assertEquals(recordTypeF, greatestSubtypeG);

    greatestSubtypeG = recordTypeF.getGreatestSubtypeHelper(recordTypeE);
    assertEquals(recordTypeE, greatestSubtypeG);

    greatestSubtypeG = recordTypeE.getGreatestSubtypeHelper(recordTypeF);
    assertEquals(recordTypeE, greatestSubtypeG);

    greatestSubtypeG = recordTypeE.getGreatestSubtypeHelper(unionType);
    assertEquals(recordTypeE, greatestSubtypeG);

    greatestSubtypeG = unionType.getGreatestSubtypeHelper(recordTypeE);
    assertEquals(recordTypeE, greatestSubtypeG);
  }
}