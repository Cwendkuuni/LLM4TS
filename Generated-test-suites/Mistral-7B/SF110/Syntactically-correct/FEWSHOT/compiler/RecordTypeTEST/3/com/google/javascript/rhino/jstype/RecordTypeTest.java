package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class RecordTypeTest {
  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @Test
  public void testRecordType() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordProperty(registry.getBooleanType(), null));
    properties.put("b", new RecordProperty(registry.getStringType(), null));
    RecordType recordType = new RecordType(registry, properties, true);

    assertEquals(registry.getBooleanType(), recordType.getPropertyType("a"));
    assertEquals(registry.getStringType(), recordType.getPropertyType("b"));

    RecordType recordType2 = new RecordType(registry, properties, false);
    assertEquals(registry.getBooleanType(), recordType2.getPropertyType("a"));
    assertEquals(registry.getStringType(), recordType2.getPropertyType("b"));

    RecordType recordType3 = new RecordType(registry, Arrays.asList("a", "b")
        .stream().map(name -> new RecordProperty(registry.getBooleanType(), null))
        .collect(Collectors.toMap(RecordProperty::getName, Function.identity()))
        .get(0), true);

    assertEquals(registry.getBooleanType(), recordType3.getPropertyType("a"));
    assertEquals(registry.getStringType(), recordType3.getPropertyType("b"));
  }

  @Test
  public void testIsSynthetic() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordProperty(registry.getBooleanType(), null));
    properties.put("b", new RecordProperty(registry.getStringType(), null));
    RecordType recordType = new RecordType(registry, properties, true);
    assertFalse(recordType.isSynthetic());

    properties.put("a", new RecordProperty(registry.getBooleanType(), null));
    properties.put("b", new RecordProperty(registry.getStringType(), null));
    RecordType recordType2 = new RecordType(registry, properties, false);
    assertTrue(recordType2.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("a", new RecordProperty(registry.getBooleanType(), null));
    properties1.put("b", new RecordProperty(registry.getStringType(), null));
    RecordType recordType1 = new RecordType(registry, properties1, true);

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("a", new RecordProperty(registry.getBooleanType(), null));
    properties2.put("b", new RecordProperty(registry.getStringType(), null));
    RecordType recordType2 = new RecordType(registry, properties2, true);

    RecordType recordType3 = new RecordType(registry, properties1, true);
    RecordType recordType4 = new RecordType(registry, properties2, true);
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.BY_VALUE));
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.BY_VALUE));
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType4, EquivalenceMethod.BY_VALUE));

    RecordType recordType5 = new RecordType(registry, properties1, true);
    RecordType recordType6 = new RecordType(registry, properties2, true);
    RecordType recordType7 = new RecordType(registry, Arrays.asList("a", "b")
        .stream().map(name -> new RecordProperty(registry.getBooleanType(), null))
        .collect(Collectors.toMap(RecordProperty::getName, Function.identity()))
        .get(0), true);
    assertFalse(recordType5.checkRecordEquivalenceHelper(recordType6, EquivalenceMethod.BY_VALUE));
    assertFalse(recordType5.checkRecordEquivalenceHelper(recordType7, EquivalenceMethod.BY_VALUE));

    Map<String, RecordProperty> properties3 = new HashMap<>();
    properties3.put("a", new RecordProperty(registry.getBooleanType(), null));
    properties3.put("b", new RecordProperty(registry.getStringType(), null));
    properties3.put("c", new RecordProperty(registry.getBooleanType(), null));
    RecordType recordType8 = new RecordType(registry, properties3, true);
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType8, EquivalenceMethod.BY_VALUE));
  }

  @Test(expected = IllegalStateException.class)
  public void testNullRecordProperty() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordProperty(registry.getBooleanType(), null));
    properties.put("b", null);
    new RecordType(registry, properties, true);
  }
}