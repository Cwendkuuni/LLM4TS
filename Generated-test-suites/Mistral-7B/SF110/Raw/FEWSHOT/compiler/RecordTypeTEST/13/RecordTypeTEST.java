package com.google.javascript.rhino.jstype;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import com.google.javascript.rhino.jstype.values.JSTypeNative;

import java.util.Map;
import java.util.Set;

public class RecordTypeTest {

  @Test
  public void testRecordTypeConstruction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties =
        ImmutableMap.<String, RecordTypeBuilder.RecordProperty>builder()
            .put("foo", new RecordTypeBuilder.RecordProperty(JSType.STRING_TYPE, null))
            .put("bar", new RecordTypeBuilder.RecordProperty(JSType.NUMBER_TYPE, null))
            .build();

    RecordType recordType = new RecordType(registry, properties, true);
    RecordType syntheticRecordType = new RecordType(registry, properties, false);

    assertEquals(RecordType.class, recordType.getClass());
    assertEquals(true, recordType.isDeclared());
    assertFalse(recordType.isFrozen());

    assertEquals(RecordType.class, syntheticRecordType.getClass());
    assertEquals(false, syntheticRecordType.isDeclared());
    assertFalse(syntheticRecordType.isFrozen());
  }

  @Test
  public void testIsSynthetic() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties =
        ImmutableMap.<String, RecordTypeBuilder.RecordProperty>builder()
            .put("foo", new RecordTypeBuilder.RecordProperty(JSType.STRING_TYPE, null))
            .build();

    RecordType recordType = new RecordType(registry, properties, true);
    RecordType syntheticRecordType = new RecordType(registry, properties, false);

    assertTrue(recordType.isDeclared());
    assertFalse(recordType.isSynthetic());

    assertFalse(syntheticRecordType.isDeclared());
    assertTrue(syntheticRecordType.isSynthetic());
  }

  @Test
  public void testIsFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties =
        ImmutableMap.<String, RecordTypeBuilder.RecordProperty>builder()
            .put("foo", new RecordTypeBuilder.RecordProperty(JSType.STRING_TYPE, null))
            .build();

    RecordType recordType = new RecordType(registry, properties, true);
    RecordType frozenRecordType = new RecordType(registry, properties, true);
    recordType.freeze();

    assertFalse(recordType.isFrozen());
    assertTrue(frozenRecordType.isFrozen());
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties =
        ImmutableMap.<String, RecordTypeBuilder.RecordProperty>builder()
            .put("foo", new RecordTypeBuilder.RecordProperty(JSType.STRING_TYPE, null))
            .build();

    RecordType recordType = new RecordType(registry, properties, true);

    assertFalse(recordType.hasProperty("bar"));
    assertFalse(recordType.defineProperty("bar", JSType.NUMBER_TYPE, false, null));

    RecordType frozenRecordType = new RecordType(registry, properties, true);
    frozenRecordType.freeze();

    assertFalse(frozenRecordType.defineProperty("bar", JSType.NUMBER_TYPE, false, null));

    recordType.freeze();
    assertFalse(recordType.defineProperty("bar", JSType.NUMBER_TYPE, false, null));
  }

  @Test
  public void testGetGreatestSubtypeWithSameRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType recordType = new RecordType(registry, ImmutableMap.of(), true);
    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(recordType);
    assertEquals(recordType, greatestSubtype);
  }

  @Test
  public void testGetGreatestSubtypeWithDifferentRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordTypeBuilder.RecordProperty> propertiesA =
        ImmutableMap.<String, RecordTypeBuilder.RecordProperty>builder()
            .put("foo", new RecordTypeBuilder.RecordProperty(JSType.STRING_TYPE, null))
            .put("bar", new RecordTypeBuilder.RecordProperty(JSType.NUMBER_TYPE, null))
            .build();

    Map<String, RecordTypeBuilder.RecordProperty> propertiesB =
        ImmutableMap.<String, RecordTypeBuilder.RecordProperty>builder()
            .put("foo", new RecordTypeBuilder.RecordProperty(JSType.STRING_TYPE, null))
            .put("bar", new RecordTypeBuilder.RecordProperty(JSType.BOOLEAN_TYPE, null))
            .build();

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);

    assertTrue(JSType.isNativeType(greatestSubtype));
    assertEquals(JSTypeNative.NO_OBJECT_TYPE, greatestSubtype);
  }

  @Test
  public void testGetGreatestSubtypeWithRecordTypeAndObjectType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordTypeBuilder.RecordProperty> properties =
        ImmutableMap.<String, RecordTypeBuilder.RecordProperty>builder()
            .put("foo", new RecordTypeBuilder.RecordProperty(JSType.STRING_TYPE, null))
            .put("bar", new RecordTypeBuilder.RecordProperty(JSType.NUMBER_TYPE, null))
            .build();

    RecordType recordType = new RecordType(registry, properties, true);

    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(
        registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));

    assertTrue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
        .isSubtype(greatestSubtype));
  }

  @Test
  public void testGetGreatestSubtypeWithRecordTypeAndSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordTypeBuilder.RecordProperty> propertiesA =
        ImmutableMap.<String, RecordTypeBuilder.RecordProperty>builder()
            .put("foo", new RecordTypeBuilder.RecordProperty(JSType.STRING_TYPE, null))
            .put("bar", new RecordTypeBuilder.RecordProperty(JSType.NUMBER_TYPE, null))
            .build();

    Map<String, RecordTypeBuilder.RecordProperty> propertiesB =
        ImmutableMap.<String, RecordTypeBuilder.RecordProperty>builder()
            .put("foo", new RecordTypeBuilder.RecordProperty(JSType.STRING_TYPE, null))
            .put("bar", new RecordTypeBuilder.RecordProperty(JSType.BOOLEAN_TYPE, null))
            .build();

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);

    UnionType unionType = new UnionTypeBuilder(registry).build();
    unionType.addAlternate(recordTypeB);

    assertEquals(unionType, greatestSubtype);
  }

  @Test
  public void testGetGreatestSubtypeWithRecordTypeAndNull() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordTypeBuilder.RecordProperty> properties =
        ImmutableMap.<String, RecordTypeBuilder.RecordProperty>builder()
            .put("foo", new RecordTypeBuilder.RecordProperty(JSType.STRING_TYPE, null))
            .put("bar", new RecordTypeBuilder.RecordProperty(JSType.NUMBER_TYPE, null))
            .build();

    RecordType recordType = new RecordType(registry, properties, true);

    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(null);

    assertTrue(JSType.isNativeType(greatestSubtype));
    assertEquals(JSTypeNative.NO_OBJECT_TYPE, greatestSubtype);
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordTypeBuilder.RecordProperty> propertiesA =
        ImmutableMap.<String, RecordTypeBuilder.RecordProperty>builder()
            .put("foo", new RecordTypeBuilder.RecordProperty(JSType.STRING_TYPE, null))
            .put("bar", new RecordTypeBuilder.RecordProperty(JSType.NUMBER_TYPE, null))
            .build();

    Map<String, RecordTypeBuilder.RecordProperty> propertiesB =
        ImmutableMap.<String, RecordTypeBuilder.RecordProperty>builder()
            .put("foo", new RecordTypeBuilder.RecordProperty(JSType.STRING_TYPE, null))
            .put("bar", new RecordTypeBuilder.RecordProperty(JSType.NUMBER_TYPE, null))
            .build();

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, EquivalenceMethod.ALWAYS));

    Map<String, RecordTypeBuilder.RecordProperty> propertiesC =
        ImmutableMap.<String, RecordTypeBuilder.RecordProperty>builder()
            .put("foo", new RecordTypeBuilder.RecordProperty(JSType.STRING_TYPE, null))
            .put("bar", new RecordTypeBuilder.RecordProperty(JSType.BOOLEAN_TYPE, null))
            .build();

    RecordType recordTypeC = new RecordType(registry, propertiesC, true);

    assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeC, EquivalenceMethod.ALWAYS));
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordTypeBuilder.RecordProperty> propertiesA =
        ImmutableMap.<String, RecordTypeBuilder.RecordProperty>builder()
            .put("foo", new RecordTypeBuilder.RecordProperty(JSType.STRING_TYPE, null))
            .put("bar", new RecordTypeBuilder.RecordProperty(JSType.NUMBER_TYPE, null))
            .build();

    Map<String, RecordTypeBuilder.RecordProperty> propertiesB =
        ImmutableMap.<String, RecordTypeBuilder.RecordProperty>builder()
            .put("foo", new RecordTypeBuilder.RecordProperty(JSType.STRING_TYPE, null))
            .put("bar", new RecordTypeBuilder.RecordProperty(JSType.BOOLEAN_TYPE, null))
            .build();

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    RecordType recordTypeC = new RecordType(registry, propertiesA, true);
    RecordType recordTypeD = new RecordType(registry, propertiesB, true);

    RecordType recordTypeE = new RecordType(registry, ImmutableMap.of(), true);

    assertTrue(recordTypeA.isSubtype(recordTypeC));
    assertTrue(recordTypeA.isSubtype(recordTypeE));
    assertTrue(recordTypeC.isSubtype(recordTypeA));
    assertTrue(recordTypeC.isSubtype(recordTypeE));
    assertTrue(recordTypeA.isSubtype(recordTypeB));
    assertFalse(recordTypeB.isSubtype(recordTypeA));
    assertFalse(recordTypeA.isSubtype(recordTypeD));
    assertFalse(recordTypeB.isSubtype(recordTypeC));
    assertFalse(recordTypeB.isSubtype(recordTypeD));
    assertFalse(recordTypeC.isSubtype(recordTypeD));
  }

  enum EquivalenceMethod {
    ALWAYS,
    STRICT,
  }
}