package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordTypeConstruction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("prop1", new RecordProperty("prop1", new IntType()));
    properties.put("prop2", new RecordProperty("prop2", new StringType()));
    RecordType recordType = new RecordType(registry, properties, true);

    assertNotNull(recordType);
    assertEquals("Object", recordType.getName());
    assertEquals(2, recordType.getPropertyNames().size());
    assertTrue(recordType.hasProperty("prop1"));
    assertTrue(recordType.hasProperty("prop2"));
    assertEquals(IntType.getInstance(), recordType.getPropertyType("prop1"));
    assertEquals(StringType.getInstance(), recordType.getPropertyType("prop2"));
  }

  @Test
  public void testRecordTypeConstructionWithSynthetic() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("prop1", new RecordProperty("prop1", new IntType()));
    properties.put("prop2", new RecordProperty("prop2", new StringType()));
    RecordType recordType = new RecordType(registry, properties, false);

    assertNotNull(recordType);
    assertEquals("Object", recordType.getName());
    assertEquals(2, recordType.getPropertyNames().size());
    assertTrue(recordType.hasProperty("prop1"));
    assertTrue(recordType.hasProperty("prop2"));
    assertEquals(IntType.getInstance(), recordType.getPropertyType("prop1"));
    assertEquals(StringType.getInstance(), recordType.getPropertyType("prop2"));
    assertTrue(recordType.isSynthetic());
  }

  @Test
  public void testRecordTypeEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("prop1", new RecordProperty("prop1", new IntType()));
    propertiesA.put("prop2", new RecordProperty("prop2", new IntType()));
    RecordType recordTypeA = new RecordType(registry, propertiesA, true);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("prop1", new RecordProperty("prop1", new IntType()));
    propertiesB.put("prop2", new RecordProperty("prop2", new IntType()));
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    Map<String, RecordProperty> propertiesC = new HashMap<>();
    propertiesC.put("prop1", new RecordProperty("prop1", new IntType()));
    propertiesC.put("prop3", new RecordProperty("prop3", new IntType()));
    RecordType recordTypeC = new RecordType(registry, propertiesC, true);

    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, EquivalenceMethod.IDENTICAL));
    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeA, EquivalenceMethod.IDENTICAL));
    assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeC, EquivalenceMethod.IDENTICAL));
  }

  @Test
  public void testRecordTypeGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("prop1", new RecordProperty("prop1", new IntType()));
    RecordType recordType = new RecordType(registry, properties, true);
    ObjectType implicitPrototype = recordType.getImplicitPrototype();

    assertEquals(JSTypeNative.OBJECT_TYPE, implicitPrototype.getName());
  }

  @Test
  public void testRecordTypeDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("prop1", new RecordProperty("prop1", new IntType()));
    RecordType recordType = new RecordType(registry, properties, true);

    assertFalse(recordType.defineProperty("prop1", new IntType(), false, null));
    assertTrue(recordType.defineProperty("prop2", new IntType(), false, null));

    Set<String> propertyNames = recordType.getPropertyNames();
    assertTrue(propertyNames.contains("prop1"));
    assertTrue(propertyNames.contains("prop2"));
  }

  @Test
  public void testRecordTypeGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("prop1", new RecordProperty("prop1", new IntType()));
    RecordType recordTypeA = new RecordType(registry, propertiesA, true);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("prop1", new RecordProperty("prop1", new IntType()));
    propertiesB.put("prop2", new RecordProperty("prop2", new StringType()));
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    Map<String, RecordProperty> propertiesC = new HashMap<>();
    propertiesC.put("prop1", new RecordProperty("prop1", new IntType()));
    propertiesC.put("prop2", new RecordProperty("prop2", new StringType()));
    propertiesC.put("prop3", new RecordProperty("prop3", new IntType()));
    RecordType recordTypeC = new RecordType(registry, propertiesC, true);

    JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
    assertNotNull(greatestSubtype);
    assertEquals(recordTypeB.getName(), greatestSubtype.getName());

    greatestSubtype = recordTypeB.getGreatestSubtypeHelper(recordTypeC);
    assertNotNull(greatestSubtype);
    assertEquals(recordTypeC.getName(), greatestSubtype.getName());

    greatestSubtype = recordTypeA.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    assertNotNull(greatestSubtype);
    assertEquals(JSTypeNative.OBJECT_TYPE, greatestSubtype.getName());

    greatestSubtype = recordTypeA.getGreatestSubtypeHelper(new VoidType());
    assertNull(greatestSubtype);
  }

  @Test
  public void testRecordTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("prop1", new RecordProperty("prop1", new IntType()));
    RecordType recordTypeA = new RecordType(registry, propertiesA, true);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("prop1", new RecordProperty("prop1", new IntType()));
    propertiesB.put("prop2", new RecordProperty("prop2", new IntType()));
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    Map<String, RecordProperty> propertiesC = new HashMap<>();
    propertiesC.put("prop1", new RecordProperty("prop1", new IntType()));
    propertiesC.put("prop3", new RecordProperty("prop3", new IntType()));
    RecordType recordTypeC = new RecordType(registry, propertiesC, true);

    Map<String, RecordProperty> propertiesD = new HashMap<>();
    propertiesD.put("prop2", new RecordProperty("prop2", new StringType()));
    RecordType recordTypeD = new RecordType(registry, propertiesD, true);

    Map<String, RecordProperty> propertiesE = new HashMap<>();
    propertiesE.put("prop1", new RecordProperty("prop1", new IntType()));
    propertiesE.put("prop2", new RecordProperty("prop2", new StringType()));
    propertiesE.put("prop3", new RecordProperty("prop3", new IntType()));
    RecordType recordTypeE = new RecordType(registry, propertiesE, true);

    assertTrue(recordTypeA.isSubtype(recordTypeB));
    assertTrue(recordTypeB.isSubtype(recordTypeA));
    assertTrue(recordTypeA.isSubtype(recordTypeC));
    assertTrue(recordTypeC.isSubtype(recordTypeA));
    assertFalse(recordTypeA.isSubtype(recordTypeD));
    assertTrue(recordTypeE.isSubtype(recordTypeA));
    assertTrue(recordTypeA.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    assertFalse(recordTypeA.isSubtype(new VoidType()));
  }

  @Test
  public void testRecordTypeIsSubtypeWithSynthetic() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("prop1", new RecordProperty("prop1", new IntType()));
    RecordType recordTypeA = new RecordType(registry, propertiesA, false);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("prop1", new RecordProperty("prop1", new IntType()));
    propertiesB.put("prop2", new RecordProperty("prop2", new IntType()));
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    Map<String, RecordProperty> propertiesC = new HashMap<>();
    propertiesC.put("prop1", new RecordProperty("prop1", new IntType()));
    propertiesC.put("prop2", new RecordProperty("prop2", new IntType()));
    propertiesC.put("prop3", new RecordProperty("prop3", new IntType()));
    RecordType recordTypeC = new RecordType(registry, propertiesC, true);

    Map<String, RecordProperty> propertiesD = new HashMap<>();
    propertiesD.put("prop2", new RecordProperty("prop2", new StringType()));
    RecordType recordTypeD = new RecordType(registry, propertiesD, true);

    Map<String, RecordProperty> propertiesE = new HashMap<>();
    propertiesE.put("prop1", new RecordProperty("prop1", new IntType()));
    propertiesE.put("prop2", new RecordProperty("prop2", new StringType()));
    propertiesE.put("prop3", new RecordProperty("prop3", new IntType()));
    RecordType recordTypeE = new RecordType(registry, propertiesE, true);

    assertTrue(recordTypeA.isSubtype(recordTypeB));
    assertTrue(recordTypeB.isSubtype(recordTypeA));
    assertTrue(recordTypeA.isSubtype(recordTypeC));
    assertTrue(recordTypeC.isSubtype(recordTypeA));
    assertFalse(recordTypeA.isSubtype(recordTypeD));
    assertTrue(recordTypeE.isSubtype(recordTypeA));
    assertTrue(recordTypeA.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    assertFalse(recordTypeA.isSubtype(new VoidType()));
  }

  @Test
  public void testRecordTypeIsSubtypeWithInvariant() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("prop1", new RecordProperty("prop1", new IntType()));
    propertiesA.put("prop2", new RecordProperty("prop2", new StringType()));
    RecordType recordTypeA = new RecordType(registry, propertiesA, true);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("prop1", new RecordProperty("prop1", new IntType()));
    propertiesB.put("prop2", new RecordProperty("prop2", new IntType()));
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    Map<String, RecordProperty> propertiesC = new HashMap<>();
    propertiesC.put("prop1", new RecordProperty("prop1", new IntType()));
    propertiesC.put("prop2", new RecordProperty("prop2", new IntType()));
    propertiesC.put("prop3", new RecordProperty("prop3", new IntType()));
    RecordType recordTypeC = new RecordType(registry, propertiesC, true);

    Map<String, RecordProperty> propertiesD = new HashMap<>();
    propertiesD.put("prop1", new RecordProperty("prop1", new IntType()));
    propertiesD.put("prop2", new RecordProperty("prop2", new StringType()));
    RecordType recordTypeD = new RecordType(registry, propertiesD, true);

    Map<String, RecordProperty> propertiesE = new HashMap<>();
    propertiesE.put("prop1", new RecordProperty("prop1", new IntType()));
    propertiesE.put("prop2", new RecordProperty("prop2", new IntType()));
    propertiesE.put("prop3", new RecordProperty("prop3", new IntType()));
    RecordType recordTypeE = new RecordType(registry, propertiesE, true);

    assertFalse(recordTypeA.isSubtype(recordTypeB));
    assertFalse(recordTypeB.isSubtype(recordTypeA));
    assertFalse(recordTypeA.isSubtype(recordTypeC));
    assertFalse(recordTypeC.isSubtype(recordTypeA));
    assertFalse(recordTypeA.isSubtype(recordTypeD));
    assertFalse(recordTypeE.isSubtype(recordTypeA));
    assertTrue(recordTypeA.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    assertFalse(recordTypeA.isSubtype(new VoidType()));

    JSType invariantTypeA = recordTypeA.getPropertyType("prop2");
    JSType invariantTypeB = recordTypeB.getPropertyType("prop2");
    JSType invariantTypeC = recordTypeC.getPropertyType("prop2");
    JSType invariantTypeD = recordTypeD.getPropertyType("prop2");

    assertTrue(invariantTypeA.isInvariant(invariantTypeB));
    assertFalse(invariantTypeA.isInvariant(invariantTypeC));
    assertFalse(invariantTypeA.isInvariant(invariantTypeD));

    assertTrue(recordTypeA.isSubtype(recordTypeB, new InvariantEquivalenceMethod(invariantTypeA, invariantTypeB)));
    assertTrue(recordTypeB.isSubtype(recordTypeA, new InvariantEquivalenceMethod(invariantTypeB, invariantTypeA)));
    assertFalse(recordTypeA.isSubtype(recordTypeC, new InvariantEquivalenceMethod(invariantTypeA, invariantTypeC)));
    assertFalse(recordTypeC.isSubtype(recordTypeA, new InvariantEquivalenceMethod(invariantTypeC, invariantTypeA)));
    assertFalse(recordTypeA.isSubtype(recordTypeD, new InvariantEquivalenceMethod(invariantTypeA, invariantTypeD)));
    assertFalse(recordTypeE.isSubtype(recordTypeA, new InvariantEquivalenceMethod(invariantTypeA, invariantTypeE)));
    assertTrue(recordTypeA.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), new InvariantEquivalenceMethod(JSTypeNative.OBJECT_TYPE, JSTypeNative.OBJECT_TYPE)));
    assertFalse(recordTypeA.isSubtype(new VoidType(), new InvariantEquivalenceMethod(JSTypeNative.VOID_TYPE, JSTypeNative.VOID_TYPE)));
  }

  private static class InvariantEquivalenceMethod implements EquivalenceMethod {
    private final JSType typeA;
    private final JSType typeB;

    public InvariantEquivalenceMethod(JSType typeA, JSType typeB) {
      this.typeA = typeA;
      this.typeB = typeB;
    }

    @Override
    public boolean checkEquivalence(JSType typeC, JSType typeD) {
      return typeA.isInvariant(typeB) && typeC.isEquivalentTo(typeD) &&
             typeC.isInvariant(typeA) && typeD.isInvariant(typeB);
    }
  }
}