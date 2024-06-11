package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class RecordTypeTest {
  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @Test
  public void testRecordTypeCreation() {
    RecordType stringRecord = new RecordType(registry, getStringProperties());
    assertEquals("RecordType: [String]", stringRecord.getClass().getName());
    assertEquals("Record with String properties", stringRecord.getName());
    assertTrue(stringRecord.isSynthetic());

    RecordType stringFrozenRecord = new RecordType(registry, getStringProperties(), false);
    assertEquals("RecordType: [String]", stringFrozenRecord.getClass().getName());
    assertEquals("Record with String properties", stringFrozenRecord.getName());
    assertFalse(stringFrozenRecord.isSynthetic());
  }

  @Test
  public void testRecordEquivalenceHelper() {
    RecordType stringRecord1 = new RecordType(registry, getStringProperties());
    RecordType stringRecord2 = new RecordType(registry, getStringProperties());
    assertTrue(stringRecord1.checkRecordEquivalenceHelper(stringRecord2, EquivalenceMethod.DEFENSIVE));
    assertTrue(stringRecord2.checkRecordEquivalenceHelper(stringRecord1, EquivalenceMethod.DEFENSIVE));

    RecordType stringFrozenRecord1 = new RecordType(registry, getStringProperties(), false);
    RecordType stringFrozenRecord2 = new RecordType(registry, getStringProperties());
    assertFalse(stringFrozenRecord1.checkRecordEquivalenceHelper(stringFrozenRecord2, EquivalenceMethod.DEFENSIVE));
    assertFalse(stringFrozenRecord2.checkRecordEquivalenceHelper(stringFrozenRecord1, EquivalenceMethod.DEFENSIVE));

    RecordType stringRecord1WithFrozen = new RecordType(registry, getStringProperties());
    RecordType stringRecord2WithFrozen = new RecordType(registry, getStringProperties(), false);
    assertFalse(stringRecord1WithFrozen.checkRecordEquivalenceHelper(stringRecord2WithFrozen, EquivalenceMethod.DEFENSIVE));
    assertFalse(stringRecord2WithFrozen.checkRecordEquivalenceHelper(stringRecord1WithFrozen, EquivalenceMethod.DEFENSIVE));

    RecordType stringRecord1WithDifferentProperty = new RecordType(registry, getDifferentStringProperties());
    assertFalse(stringRecord1.checkRecordEquivalenceHelper(stringRecord1WithDifferentProperty, EquivalenceMethod.DEFENSIVE));
  }

  @Test
  public void testGetImplicitPrototype() {
    RecordType stringRecord = new RecordType(registry, getStringProperties());
    assertEquals(JSTypeNative.OBJECT_TYPE, stringRecord.getImplicitPrototype());
  }

  @Test
  public void testDefineProperty() {
    RecordType stringRecord = new RecordType(registry, getStringProperties());

    stringRecord.defineProperty("name", String.class, true, null);
    assertTrue(stringRecord.hasProperty("name"));
    assertEquals(String.class, stringRecord.getPropertyType("name"));

    stringRecord.defineProperty("age", Integer.class, true, null);
    assertTrue(stringRecord.hasProperty("age"));
    assertEquals(Integer.class, stringRecord.getPropertyType("age"));
  }

  @Test
  public void testDefinePropertyFrozen() {
    RecordType stringFrozenRecord = new RecordType(registry, getStringProperties(), false);

    stringFrozenRecord.defineProperty("name", String.class, true, null);
    assertTrue(stringFrozenRecord.hasProperty("name"));
    assertEquals(String.class, stringFrozenRecord.getPropertyType("name"));

    try {
      stringFrozenRecord.defineProperty("name", Integer.class, true, null);
      fail("Expected IllegalStateException due to RecordType being frozen");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    RecordType stringRecord1 = new RecordType(registry, getStringProperties());
    RecordType stringRecord2 = new RecordType(registry, getStringProperties());
    RecordType stringRecord3 = new RecordType(registry, getDifferentStringProperties());

    JSType greatestSubtype = stringRecord1.getGreatestSubtypeHelper(stringRecord2);
    assertEquals(stringRecord1, greatestSubtype);

    greatestSubtype = stringRecord1.getGreatestSubtypeHelper(stringRecord3);
    assertEquals(JSTypeNative.OBJECT_TYPE, greatestSubtype);

    greatestSubtype = stringRecord3.getGreatestSubtypeHelper(stringRecord1);
    assertEquals(JSTypeNative.OBJECT_TYPE, greatestSubtype);

    greatestSubtype = stringRecord3.getGreatestSubtypeHelper(stringRecord2);
    assertEquals(stringRecord2, greatestSubtype);
  }

  private static Map<String, RecordProperty> getStringProperties() {
    return new HashMap<String, RecordProperty>() {{
      put("name", new RecordProperty("name", String.class));
      put("age", new RecordProperty("age", Integer.class));
    }};
  }

  private static Map<String, RecordProperty> getDifferentStringProperties() {
    return new HashMap<String, RecordProperty>() {{
      put("name", new RecordProperty("name", String.class));
      put("age", new RecordProperty("age", Double.class));
    }};
  }
}