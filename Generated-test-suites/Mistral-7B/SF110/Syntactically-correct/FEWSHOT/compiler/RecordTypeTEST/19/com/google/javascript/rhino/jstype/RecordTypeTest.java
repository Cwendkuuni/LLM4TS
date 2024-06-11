package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class RecordTypeTest {

  @Test
  public void testRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = ImmutableMap.of(
        "foo", new RecordProperty("foo", registry.getNativeStringType()),
        "bar", new RecordProperty("bar", registry.getNativeNumberType()));

    RecordType recordType = new RecordType(registry, properties, true);

    assertNull(recordType.getImplicitPrototype());
    assertTrue(recordType.isSynthetic());

    assertEquals(properties.size(), recordType.getOwnPropertyNames().size());
    assertSame(properties.get("foo"), recordType.getProperty("foo"));
    assertSame(properties.get("bar"), recordType.getProperty("bar"));

    assertEquals(recordType.getClass(), RecordType.class);
  }

  @Test
  public void testRecordType_isFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = ImmutableMap.of(
        "foo", new RecordProperty("foo", registry.getNativeStringType()),
        "bar", new RecordProperty("bar", registry.getNativeNumberType()));

    RecordType recordType = new RecordType(registry, properties, true);

    assertFalse(recordType.isFrozen());

    recordType.freeze();

    assertTrue(recordType.isFrozen());
  }

  @Test
  public void testRecordType_isSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = ImmutableMap.of(
        "foo", new RecordProperty("foo", registry.getNativeStringType()),
        "bar", new RecordProperty("bar", registry.getNativeNumberType()));
    RecordType type1 = new RecordType(registry, properties1, true);

    Map<String, RecordProperty> properties2 = ImmutableMap.of(
        "foo", new RecordProperty("foo", registry.getNativeStringType()),
        "bar", new RecordProperty("bar", registry.getNativeNumberType()),
        "baz", new RecordProperty("baz", registry.getNativeStringType()));
    RecordType type2 = new RecordType(registry, properties2, true);

    Map<String, RecordProperty> properties3 = ImmutableMap.of(
        "baz", new RecordProperty("baz", registry.getNativeStringType()));
    RecordType type3 = new RecordType(registry, properties3, true);

    Map<String, RecordProperty> properties4 = ImmutableMap.of(
        "qux", new RecordProperty("qux", registry.getNativeStringType()));
    RecordType type4 = new RecordType(registry, properties4, true);

    Map<String, RecordProperty> properties5 = ImmutableMap.of(
        "foo", new RecordProperty("foo", registry.getNativeStringType()),
        "bar", new RecordProperty("bar", registry.getNativeNumberType()),
        "baz", new RecordProperty("baz", registry.getNativeStringType()));
    RecordType type5 = new RecordType(registry, properties5, true);

    Map<String, RecordProperty> properties6 = ImmutableMap.of(
        "foo", new RecordProperty("foo", registry.getNativeStringType()));
    RecordType type6 = new RecordType(registry, properties6, true);

    Map<String, RecordProperty> properties7 = ImmutableMap.of();
    RecordType type7 = new RecordType(registry, properties7, true);

    Map<String, RecordProperty> properties8 = ImmutableMap.of(
        "foo", new RecordProperty("foo", registry.getNativeStringType()),
        "bar", new RecordProperty("bar", registry.getNativeNumberType()));
    RecordType type8 = new RecordType(registry, properties8, false);

    assertFalse(type1.isSubtype(registry.getNativeObjectType()));
    assertFalse(registry.getNativeObjectType().isSubtype(type1));
    assertFalse(type1.isSubtype(type2));
    assertFalse(type2.isSubtype(type1));
    assertFalse(type1.isSubtype(type3));
    assertFalse(type3.isSubtype(type1));
    assertFalse(type1.isSubtype(type4));
    assertFalse(type4.isSubtype(type1));
    assertFalse(type1.isSubtype(type5));
    assertFalse(type5.isSubtype(type1));
    assertFalse(type1.isSubtype(type6));
    assertFalse(type6.isSubtype(type1));
    assertFalse(type1.isSubtype(type7));
    assertFalse(type7.isSubtype(type1));
    assertFalse(type1.isSubtype(type8));
    assertFalse(type8.isSubtype(type1));

    assertTrue(type1.isSubtype(type1));
    assertTrue(type1.isSubtype(type1));

    assertTrue(type1.isSubtype(type1.toMaybeRecordType()));
    assertTrue(type1.toMaybeRecordType().isSubtype(type1));

    assertTrue(type2.isSubtype(type1));
    assertTrue(type1.isSubtype(type2));

    assertTrue(type3.isSubtype(type1));
    assertTrue(type1.isSubtype(type3));

    assertTrue(type4.isSubtype(type1));
    assertTrue(type1.isSubtype(type4));

    assertTrue(type5.isSubtype(type1));
    assertTrue(type1.isSubtype(type5));

    assertTrue(type6.isSubtype(type1));
    assertTrue(type1.isSubtype(type6));

    assertTrue(type7.isSubtype(type1));
    assertTrue(type1.isSubtype(type7));

    assertTrue(type8.isSubtype(type1));
    assertTrue(type1.isSubtype(type8));

    assertTrue(type1.isSubtype(type1.getGreatestSubtype(type2)));
    assertTrue(type1.getGreatestSubtype(type2).isSubtype(type1));

    assertTrue(type1.isSubtype(type1.getGreatestSubtype(type3)));
    assertTrue(type1.getGreatestSubtype(type3).isSubtype(type1));

    assertTrue(type1.isSubtype(type1.getGreatestSubtype(type4)));
    assertTrue(type1.getGreatestSubtype(type4).isSubtype(type1));

    assertTrue(type1.isSubtype(type1.getGreatestSubtype(type5)));
    assertTrue(type1.getGreatestSubtype(type5).isSubtype(type1));

    assertTrue(type1.isSubtype(type1.getGreatestSubtype(type6)));
    assertTrue(type1.getGreatestSubtype(type6).isSubtype(type1));

    assertTrue(type1.isSubtype(type1.getGreatestSubtype(type7)));
    assertTrue(type1.getGreatestSubtype(type7).isSubtype(type1));

    assertTrue(type1.isSubtype(type1.getGreatestSubtype(type8)));
    assertTrue(type1.getGreatestSubtype(type8).isSubtype(type1));
  }

  @Test
  public void testRecordType_checkRecordEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = ImmutableMap.of(
        "foo", new RecordProperty("foo", registry.getNativeStringType()),
        "bar", new RecordProperty("bar", registry.getNativeNumberType()));
    RecordType type1 = new RecordType(registry, properties1, true);

    Map<String, RecordProperty> properties2 = ImmutableMap.of(
        "foo", new RecordProperty("foo", registry.getNativeStringType()),
        "bar", new RecordProperty("bar", registry.getNativeNumberType()));
    RecordType type2 = new RecordType(registry, properties2, true);

    Map<String, RecordProperty> properties3 = ImmutableMap.of(
        "foo", new RecordProperty("foo", registry.getNativeStringType()),
        "bar", new RecordProperty("bar", registry.getNativeNumberType()),
        "baz", new RecordProperty("baz", registry.getNativeStringType()));
    RecordType type3 = new RecordType(registry, properties3, true);

    assertTrue(type1.checkRecordEquivalenceHelper(type2, EquivalenceMethod.IDENTICAL));
    assertTrue(type1.checkRecordEquivalenceHelper(type2, EquivalenceMethod.SUBTYPE));
    assertTrue(type1.checkRecordEquivalenceHelper(type2, EquivalenceMethod.SUPERTYPE));

    assertFalse(type1.checkRecordEquivalenceHelper(type3, EquivalenceMethod.IDENTICAL));
    assertFalse(type1.checkRecordEquivalenceHelper(type3, EquivalenceMethod.SUBTYPE));
    assertFalse(type1.checkRecordEquivalenceHelper(type3, EquivalenceMethod.SUPERTYPE));
  }

  @Test
  public void testRecordType_defineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = ImmutableMap.of(
        "foo", new RecordProperty("foo", registry.getNativeStringType()),
        "bar", new RecordProperty("bar", registry.getNativeNumberType()));
    RecordType recordType = new RecordType(registry, properties, true);

    String propertyName = "qux";
    JSType propertyType = registry.getNativeStringType();
    Node propertyNode = new Node_StringLiteral("qux");

    assertFalse(recordType.defineProperty(propertyName, propertyType, false, propertyNode));
    assertFalse(recordType.hasProperty(propertyName));

    recordType.freeze();

    assertFalse(recordType.defineProperty(propertyName, propertyType, false, propertyNode));
    assertFalse(recordType.hasProperty(propertyName));

    recordType.unfreeze();

    assertTrue(recordType.defineProperty(propertyName, propertyType, false, propertyNode));
    assertTrue(recordType.hasProperty(propertyName));
  }

  @Test
  public void testRecordType_getGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = ImmutableMap.of(
        "foo", new RecordProperty("foo", registry.getNativeStringType()),
        "bar", new RecordProperty("bar", registry.getNativeNumberType()));
    RecordType type1 = new RecordType(registry, properties1, true);

    Map<String, RecordProperty> properties2 = ImmutableMap.of(
        "foo", new RecordProperty("foo", registry.getNativeStringType()),
        "bar", new RecordProperty("bar", registry.getNativeNumberType()),
        "baz", new RecordProperty("baz", registry.getNativeStringType()));
    RecordType type2 = new RecordType(registry, properties2, true);

    assertSame(type1, type1.getGreatestSubtypeHelper(type1));
    assertSame(type1, type1.getGreatestSubtypeHelper(type2));
    assertSame(type1, type1.getGreatestSubtypeHelper(type2.toMaybeRecordType()));

    assertSame(type2, type2.getGreatestSubtypeHelper(type1));
    assertSame(type2, type2.getGreatestSubtypeHelper(type2));
    assertSame(type2, type2.getGreatestSubtypeHelper(type2.toMaybeRecordType()));

    assertSame(type1, type1.getGreatestSubtypeHelper(type2.toMaybeRecordType()));
    assertSame(type1, type1.getGreatestSubtypeHelper(type1.toMaybeRecordType()));

    RecordType type3 = new RecordType(registry, Arrays.asList("foo", "bar").stream().collect(ImmutableMap.toImmutableMap(
        Map.Entry::getKey,
        e -> new RecordProperty(e.getKey(), registry.getNativeNumberType())
    )), true);

    assertSame(type1, type1.getGreatestSubtypeHelper(type3));
    assertSame(type1, type1.getGreatestSubtypeHelper(type3.toMaybeRecordType()));

    assertSame(type3, type3.getGreatestSubtypeHelper(type1));
    assertSame(type3, type3.getGreatestSubtypeHelper(type3));
    assertSame(type3, type3.getGreatestSubtypeHelper(type3.toMaybeRecordType()));

    Map<String, RecordProperty> properties4 = ImmutableMap.of(
        "baz", new RecordProperty("baz", registry.getNativeStringType()));
    RecordType type4 = new RecordType(registry, properties4, true);

    UnionType unionType = new UnionTypeBuilder(registry).build();

    assertSame(unionType, type1.getGreatestSubtypeHelper(registry.getNativeAnyType()));
    assertSame(registry.getNativeAnyType(), type1.getGreatestSubtypeHelper(unionType));

    assertSame(registry.getNativeObjectType(), type1.getGreatestSubtypeHelper(registry.getNativeObjectType()));
    assertSame(registry.getNativeObjectType(), type1.getGreatestSubtypeHelper(type7));

    assertNull(type1.getGreatestSubtypeHelper(registry.getNativeNullType()));
    assertNull(type1.getGreatestSubtypeHelper(registry.getNativeBooleanType()));
    assertNull(type1.getGreatestSubtypeHelper(registry.getNativeNumberType()));
    assertNull(type1.getGreatestSubtypeHelper(registry.getNativeStringType()));
  }
}