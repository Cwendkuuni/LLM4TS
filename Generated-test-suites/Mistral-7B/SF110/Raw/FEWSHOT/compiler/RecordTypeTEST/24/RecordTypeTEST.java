package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

public class RecordTypeTest {

  @Test
  public void testIsSynthetic() {
    RecordType record = new RecordType(new JSTypeRegistry(), ImmutableMap.of());
    assertTrue(record.isSynthetic());

    RecordType declaredRecord = new RecordType(new JSTypeRegistry(),
        ImmutableMap.of(), true);
    assertFalse(declaredRecord.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType record1 = new RecordType(registry, ImmutableMap.of());
    RecordType record2 = new RecordType(registry, ImmutableMap.of());

    assertTrue(record1.checkRecordEquivalenceHelper(record2, EquivalenceMethod.BY_VALUE));
    assertTrue(record1.checkRecordEquivalenceHelper(record2, EquivalenceMethod.BY_REFERENCE));

    RecordProperty prop1 = new RecordProperty("prop", registry.getNullType());
    RecordProperty prop2 = new RecordProperty("prop", registry.getBooleanType());

    RecordType recordWithProp1 = new RecordType(registry, ImmutableMap.of("prop", prop1));
    RecordType recordWithProp2 = new RecordType(registry, ImmutableMap.of("prop", prop2));

    assertFalse(recordWithProp1.checkRecordEquivalenceHelper(recordWithProp2, EquivalenceMethod.BY_VALUE));
    assertFalse(recordWithProp1.checkRecordEquivalenceHelper(recordWithProp2, EquivalenceMethod.BY_REFERENCE));
  }

  @Test
  public void testGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType record = new RecordType(registry, ImmutableMap.of());

    ObjectType implicitPrototype = record.getImplicitPrototype();

    assertTrue(implicitPrototype.isInstanceOf(JSTypeNative.OBJECT_TYPE));
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType record = new RecordType(registry, ImmutableMap.of());

    String propertyName = "prop";
    JSType propertyType = registry.getStringType();
    Node propertyNode = Node.NULL();

    boolean result = record.defineProperty(propertyName, propertyType, false, propertyNode);

    assertTrue(result);
  }

  @Test
  public void testDefinePropertyFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType record = new RecordType(registry, ImmutableMap.of());

    String propertyName = "prop";
    JSType propertyType = registry.getStringType();
    Node propertyNode = Node.NULL();

    record.setFrozen(true);

    boolean result = record.defineProperty(propertyName, propertyType, false, propertyNode);

    assertFalse(result);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType record1 = new RecordType(registry, ImmutableMap.of("prop1", registry.getIntType()));
    RecordType record2 = new RecordType(registry, ImmutableMap.of("prop1", registry.getIntType(), "prop2", registry.getStringType()));

    JSType greatestSubtype = record1.getGreatestSubtypeHelper(record2);

    assertTrue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
        .isSubtype(greatestSubtype));
  }

  @Test
  public void testGetGreatestSubtypeHelperWithInvariantProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType record1 = new RecordType(registry, ImmutableMap.of("prop1", registry.getIntType()));
    RecordType record2 = new RecordType(registry, ImmutableMap.of("prop1", registry.getIntType(), "prop2", registry.getBooleanType()));

    RecordProperty prop2 = new RecordProperty("prop2", registry.getBooleanType());
    prop2.setInvariant(true);

    RecordType record2WithInvariantProp = new RecordType(registry, ImmutableMap.of("prop1", registry.getIntType(), "prop2", prop2));

    JSType greatestSubtype = record1.getGreatestSubtypeHelper(record2WithInvariantProp);

    assertTrue(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE)
        .isSubtype(greatestSubtype));
  }

  @Test
  public void testGetGreatestSubtypeHelperWithNonInvariantProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType record1 = new RecordType(registry, ImmutableMap.of("prop1", registry.getIntType()));
    RecordType record2 = new RecordType(registry, ImmutableMap.of("prop1", registry.getIntType(), "prop2", registry.getStringType()));

    JSType greatestSubtype = record1.getGreatestSubtypeHelper(record2);

    assertTrue(registry.getNativeObjectType(JSTypeNative.NO_TYPE)
        .isSubtype(greatestSubtype));
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType record1 = new RecordType(registry, ImmutableMap.of("prop1", registry.getIntType()));
    RecordType record2 = new RecordType(registry, ImmutableMap.of("prop1", registry.getIntType(), "prop2", registry.getStringType()));

    RecordType record3 = new RecordType(registry, ImmutableMap.of("prop1", registry.getIntType(), "prop2", registry.getStringType(), "prop3", registry.getStringType()));

    assertTrue(record1.isSubtype(record1));
    assertTrue(record1.isSubtype(record2));
    assertFalse(record2.isSubtype(record1));
    assertTrue(record1.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    assertTrue(record2.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    assertFalse(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
        .isSubtype(record1));
    assertFalse(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
        .isSubtype(record2));
    assertTrue(record1.isSubtype(record3));
    assertTrue(record2.isSubtype(record3));
    assertTrue(record3.isSubtype(record1));
    assertTrue(record3.isSubtype(record2));
  }

  @Test
  public void testUnionTypeBuilder() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType record = new RecordType(registry, ImmutableMap.of("prop", registry.getStringType()));

    UnionTypeBuilder builder = new UnionTypeBuilder(registry);

    ObjectType objType1 = registry.getNativeType(JSTypeNative.STRING_TYPE);
    ObjectType objType2 = registry.getNativeType(JSTypeNative.NUMBER_TYPE);

    builder.addAlternate(record);
    builder.addAlternate(objType1);
    builder.addAlternate(objType2);

    JSType unionType = builder.build();

    ObjectType greatestSubtype = record.getGreatestSubtypeHelper(unionType);

    assertTrue(unionType.isSupertype(greatestSubtype));
  }
}