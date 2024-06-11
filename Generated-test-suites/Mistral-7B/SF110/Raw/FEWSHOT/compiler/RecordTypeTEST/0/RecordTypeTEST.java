package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Arrays.asMap(
        new RecordProperty("name", registry.StringType()),
        new RecordProperty("age", registry.NumberType()));

    RecordType recordType = new RecordType(registry, properties, true);
    assertNotNull(recordType);
  }

  @Test
  public void testIsSynthetic() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Arrays.asMap(
        new RecordProperty("name", registry.StringType()),
        new RecordProperty("age", registry.NumberType()));

    RecordType recordType = new RecordType(registry, properties, false);
    assertTrue(recordType.isSynthetic());

    RecordType declaredRecordType = new RecordType(registry, properties, true);
    assertFalse(declaredRecordType.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Arrays.asMap(
        new RecordProperty("name", registry.StringType()),
        new RecordProperty("age", registry.NumberType()));

    RecordType recordType1 = new RecordType(registry, properties, true);
    RecordType recordType2 = new RecordType(registry, properties, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.IDENTICAL));
  }

  @Test
  public void testGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, Collections.emptyMap(), true);
    ObjectType expectedPrototype = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    assertEquals(expectedPrototype, recordType.getImplicitPrototype());
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Arrays.asMap(
        new RecordProperty("name", registry.StringType()),
        new RecordProperty("age", registry.NumberType()));

    RecordType recordType = new RecordType(registry, properties, true);

    assertFalse(recordType.defineProperty("newProperty", registry.StringType(), false, null));
    assertTrue(recordType.defineProperty("name", registry.StringType(), false, null));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType stringType = registry.StringType();
    JSType numberType = registry.NumberType();
    UnionType numberOrString = new UnionTypeBuilder(registry).build();
    numberOrString.addAlternate(stringType);
    numberOrString.addAlternate(numberType);

    RecordType recordType1 = new RecordType(registry,
        Arrays.asMap(new RecordProperty("name", stringType),
            new RecordProperty("age", numberType)), true);

    RecordType recordType2 = new RecordType(registry,
        Arrays.asMap(new RecordProperty("name", numberType),
            new RecordProperty("age", stringType)), true);

    RecordType recordType3 = new RecordType(registry,
        Arrays.asMap(new RecordProperty("name", stringType),
            new RecordProperty("age", numberOrString)), true);

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertEquals(recordType3, greatestSubtype.toMaybeRecordType());

    greatestSubtype = recordType1.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), greatestSubtype);
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType recordType1 = new RecordType(registry,
        Arrays.asMap(new RecordProperty("name", registry.StringType()),
            new RecordProperty("age", registry.NumberType())), true);

    RecordType recordType2 = new RecordType(registry,
        Arrays.asMap(new RecordProperty("name", registry.StringType()),
            new RecordProperty("age", registry.NumberType()),
            new RecordProperty("gender", registry.StringType())), true);

    RecordType recordType3 = new RecordType(registry,
        Arrays.asMap(new RecordProperty("name", registry.StringType()),
            new RecordProperty("age", registry.NumberType())), true);

    RecordType recordType4 = new RecordType(registry,
        Arrays.asMap(new RecordProperty("name", registry.StringType()),
            new RecordProperty("age", registry.NumberType())), false);

    RecordType recordType5 = new RecordType(registry,
        Arrays.asMap(new RecordProperty("name", registry.NumberType()),
            new RecordProperty("age", registry.NumberType())), true);

    RecordType recordType6 = new RecordType(registry,
        Arrays.asMap(new RecordProperty("name", registry.BooleanType()),
            new RecordProperty("age", registry.NumberType())), true);

    assertTrue(recordType1.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(recordType3));
    assertTrue(recordType3.isSubtype(recordType1));
    assertFalse(recordType1.isSubtype(recordType2));
    assertFalse(recordType2.isSubtype(recordType1));
    assertFalse(recordType1.isSubtype(recordType4));
    assertFalse(recordType4.isSubtype(recordType1));
    assertFalse(recordType1.isSubtype(recordType5));
    assertFalse(recordType5.isSubtype(recordType1));
  }

  @Test
  public void testIsSubtypeStatic() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType recordType1 = new RecordType(registry,
        Arrays.asMap(new RecordProperty("name", registry.StringType()),
            new RecordProperty("age", registry.NumberType())), true);

    RecordType recordType2 = new RecordType(registry,
        Arrays.asMap(new RecordProperty("name", registry.StringType()),
            new RecordProperty("age", registry.NumberType()),
            new RecordProperty("gender", registry.StringType())), true);

    RecordType recordType3 = new RecordType(registry,
        Arrays.asMap(new RecordProperty("name", registry.StringType()),
            new RecordProperty("age", registry.NumberType())), true);

    RecordType recordType4 = new RecordType(registry,
        Arrays.asMap(new RecordProperty("name", registry.StringType()),
            new RecordProperty("age", registry.NumberType())), false);

    RecordType recordType5 = new RecordType(registry,
        Arrays.asMap(new RecordProperty("name", registry.NumberType()),
            new RecordProperty("age", registry.NumberType())), true);

    RecordType recordType6 = new RecordType(registry,
        Arrays.asMap(new RecordProperty("name", registry.BooleanType()),
            new RecordProperty("age", registry.NumberType())), true);

    assertTrue(RecordType.isSubtype(recordType1, recordType1));
    assertTrue(RecordType.isSubtype(recordType1, recordType3));
    assertTrue(RecordType.isSubtype(recordType3, recordType1));
    assertFalse(RecordType.isSubtype(recordType1, recordType2));
    assertFalse(RecordType.isSubtype(recordType2, recordType1));
    assertFalse(RecordType.isSubtype(recordType1, recordType4));
    assertFalse(RecordType.isSubtype(recordType4, recordType1));
    assertFalse(RecordType.isSubtype(recordType1, recordType5));
    assertFalse(RecordType.isSubtype(recordType5, recordType1));
  }
}