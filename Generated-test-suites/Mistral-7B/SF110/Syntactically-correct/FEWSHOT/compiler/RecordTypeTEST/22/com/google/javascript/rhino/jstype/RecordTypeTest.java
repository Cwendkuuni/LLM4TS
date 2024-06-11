package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

public class RecordTypeTest {
  @Test
  public void testDeclaredRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = ImmutableMap.of(
        "name", new RecordProperty("name", registry.getStringType()));
    RecordType recordType = new RecordType(registry, properties, true);

    assertTrue(recordType.isSynthetic());
    assertFalse(recordType.isFrozen());

    RecordType expected = new RecordType(registry, properties, true);
    assertTrue(recordType.isEquivalentTo(expected));
  }

  @Test
  public void testSynthesizedRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = ImmutableMap.of(
        "name", new RecordProperty("name", registry.getStringType()));
    RecordType recordType = new RecordType(registry, properties, false);

    assertFalse(recordType.isSynthetic());
    assertTrue(recordType.isFrozen());

    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    RecordType expected = builder.setSynthesized(true)
        .setRecordProperties(properties)
        .build();
    assertTrue(recordType.isEquivalentTo(expected));
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = ImmutableMap.of(
        "nameA", new RecordProperty("nameA", registry.getStringType()),
        "nameB", new RecordProperty("nameB", registry.getStringType()));
    Map<String, RecordProperty> propertiesB = ImmutableMap.of(
        "nameB", new RecordProperty("nameB", registry.getStringType()),
        "nameA", new RecordProperty("nameA", registry.getStringType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    RecordTypeBuilder builderA = new RecordTypeBuilder(registry);
    builderA.setRecordProperties(propertiesA);
    JSType typeA = builderA.build();

    RecordTypeBuilder builderB = new RecordTypeBuilder(registry);
    builderB.setRecordProperties(propertiesB);
    JSType typeB = builderB.build();

    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB,
        EquivalenceMethod.STRUCTURAL));
    assertTrue(recordTypeB.checkRecordEquivalenceHelper(recordTypeA,
        EquivalenceMethod.STRUCTURAL));

    assertTrue(typeA.isSubtype(typeB));
    assertTrue(typeB.isSubtype(typeA));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = ImmutableMap.of(
        "name", new RecordProperty("name", registry.getStringType()));

    RecordType recordTypeA = new RecordType(registry, properties, true);
    RecordType recordTypeB = new RecordType(registry, properties, true);

    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    builder.setRecordProperties(properties);
    JSType recordTypeC = builder.build();

    UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    UnionType unionTypeA = unionTypeBuilder.addAlternate(recordTypeA)
        .addAlternate(recordTypeB)
        .addAlternate(objectType)
        .build();

    JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
    assertEquals(unionTypeA, greatestSubtype);
  }
}