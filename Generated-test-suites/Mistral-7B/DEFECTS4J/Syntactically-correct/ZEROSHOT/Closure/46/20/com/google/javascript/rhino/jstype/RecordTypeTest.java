package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.info.Type;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import com.google.javascript.rhino.jstype.testtype.SimpleType;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * RecordTypeTest.java
 */
public class RecordTypeTest {

  private static final JSTypeRegistry registry = new JSTypeRegistry();
  private static final ErrorReporter errorReporter = new ErrorReporter() {};

  @org.junit.BeforeClass
  public static void setup() {
    // Initialize the registry with some types.
    registry.registerBuiltins();
    registry.registerType(Type.STRING, new JSTypeNative.PrimitiveType(Type.STRING));
    registry.registerType(Type.NUMBER, new JSTypeNative.PrimitiveType(Type.NUMBER));
    registry.registerType(Type.BOOLEAN, new JSTypeNative.PrimitiveType(Type.BOOLEAN));
    registry.registerType(
        new SimpleType("SimpleType").setName("SimpleType"),
        new JSTypeNative.RecordType(registry, Maps.newHashMap(), false));
  }

  @org.junit.Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.NUMBER)));
    recordProperties.put("b", new RecordProperty("b", new SimpleType("SimpleType")));

    RecordType recordType = new RecordType(registry, recordProperties);
    assertNotNull(recordType);

    assertEquals(recordProperties.size(), recordType.getPropertyCount());
    assertEquals("{a: number, b: SimpleType}", recordType.getClassName());
    assertTrue(recordType.isRecordType());
  }

  @org.junit.Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.NUMBER)));
    recordProperties.put("b", new RecordProperty("b", registry.getNativeType(JSTypeNative.NUMBER)));

    RecordType recordType1 = new RecordType(registry, recordProperties);
    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.NUMBER)));
    recordProperties2.put("b", new RecordProperty("b", registry.getNativeType(JSTypeNative.NUMBER)));
    recordProperties2.put("c", new RecordProperty("c", registry.getNativeType(JSTypeNative.NUMBER)));

    RecordType recordType2 = new RecordType(registry, recordProperties2);

    // Test equality.
    assertTrue(recordType1.equals(recordType1));
    assertFalse(recordType1.equals(recordType2));

    // Test isEquivalentTo.
    assertTrue(recordType1.isEquivalentTo(recordType1));
    assertFalse(recordType1.isEquivalentTo(recordType2));

    // Test type equivalence.
    assertTrue(recordType1.isEquivalentTo(recordType1));
    assertFalse(recordType1.isEquivalentTo(recordType2));

    assertEquals(recordType1, recordType1.toMaybeRecordType());
    assertEquals(recordType1, recordType1.getLeastSupertype(recordType1));
    assertEquals(recordType1, recordType1.getGreatestSubtypeHelper(recordType1));

    // Test type inequalities.
    assertFalse(recordType1.isSubtype(recordType2));
    assertFalse(recordType2.isSubtype(recordType1));

    RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
    assertFalse(recordType1.isEquivalentTo(recordType3));
  }

  @org.junit.Test
  public void testRecordTypeSubtyping() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.NUMBER)));
    recordProperties1.put("b", new RecordProperty("b", registry.getNativeType(JSTypeNative.NUMBER)));

    RecordType recordType1 = new RecordType(registry, recordProperties1);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.NUMBER)));
    recordProperties2.put("b", new RecordProperty("b", registry.getNativeType(JSTypeNative.NUMBER)));
    recordProperties2.put("c", new RecordProperty("c", registry.getNativeType(JSTypeNative.NUMBER)));

    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertTrue(recordType1.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(recordType2));

    // Test subtyping with a type with a different name.
    assertTrue(recordType1.isSubtype(registry.getNativeType(JSTypeNative.OBJECT_TYPE)));
    assertFalse(registry.getNativeType(JSTypeNative.OBJECT_TYPE).isSubtype(recordType1));

    // Test subtyping with a type that doesn't have the same properties.
    assertFalse(recordType1.isSubtype(new SimpleType("SimpleType").setName("SimpleType")));
    assertFalse(new SimpleType("SimpleType").setName("SimpleType").isSubtype(recordType1));
  }

  @org.junit.Test
  public void testRecordTypeFreeze() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.NUMBER)));
    recordProperties.put("b", new RecordProperty("b", registry.getNativeType(JSTypeNative.NUMBER)));

    RecordType recordType = new RecordType(registry, recordProperties);
    assertFalse(recordType.isFrozen());

    recordType.defineProperty("c", registry.getNativeType(JSTypeNative.NUMBER), false, null);
    assertTrue(recordType.isFrozen());

    try {
      recordType.defineProperty("c", registry.getNativeType(JSTypeNative.NUMBER), false, null);
      assertFalse("Expected an IllegalStateException", false);
    } catch (IllegalStateException e) {
      // Expected.
    }
  }

  @org.junit.Test
  public void testRecordTypeUnion() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.NUMBER)));
    recordProperties1.put("b", new RecordProperty("b", registry.getNativeType(JSTypeNative.NUMBER)));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.NUMBER)));
    recordProperties2.put("c", new RecordProperty("c", registry.getNativeType(JSTypeNative.NUMBER)));

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    UnionTypeBuilder unionBuilder = new UnionTypeBuilder(registry);
    unionBuilder.addAlternate(recordType1);
    unionBuilder.addAlternate(recordType2);

    JSType unionType = unionBuilder.build();
    assertFalse(unionType.isRecordType());
    assertTrue(unionType.isSubtype(recordType1));
    assertTrue(unionType.isSubtype(recordType2));
  }
}