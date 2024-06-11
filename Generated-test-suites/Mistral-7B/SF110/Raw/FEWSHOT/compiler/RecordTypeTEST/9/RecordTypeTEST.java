package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class RecordTypeTest {
  @Test
  public void testIsSubtype() {
    JSTypeNative objectType = JSTypeNative.OBJECT_TYPE;
    JSTypeNative recordType = JSTypeNative.RECORD_TYPE;
    JSTypeNative emptyRecordType = JSTypeNative.EMPTY_RECORD_TYPE;

    JSType recordType1 = registry.getInstance(recordType, new Object[] {
        new String[]{"a", "number"},
        new String[]{"b", "string"}
    });

    JSType recordType2 = registry.getInstance(recordType, new Object[] {
        new String[]{"a", "number"},
        new String[]{"b", "string"},
        new String[]{"c", "boolean"}
    });

    JSType recordType3 = registry.getInstance(recordType, new Object[] {
        new String[]{"a", "number"},
        new String[]{"b", "number"},
        new String[]{"c", "boolean"}
    });

    // RecordType is a subtype of ObjectType
    assertTrue(recordType1.isSubtype(objectType));

    // EmptyRecordType is a subtype of ObjectType
    assertTrue(emptyRecordType.isSubtype(objectType));

    // RecordType1 is a subtype of RecordType2 because RecordType2 has all
    // the properties of RecordType1 with the same types
    assertTrue(recordType1.isSubtype(recordType2));

    // RecordType1 is not a subtype of RecordType3 because RecordType3 has an
    // additional property 'c'
    assertFalse(recordType1.isSubtype(recordType3));

    // RecordType2 is not a subtype of RecordType1 because it does not have
    // property 'a' with the same type
    assertFalse(recordType2.isSubtype(recordType1));

    // RecordType3 is a subtype of RecordType2 because it has all the properties
    // of RecordType2 with the same types
    assertTrue(recordType3.isSubtype(recordType2));

    // RecordType1 is not a subtype of RecordType3 for the same reason as above
    assertFalse(recordType1.isSubtype(recordType3));

    // RecordType2 is not a subtype of RecordType1 for the same reason as above
    assertFalse(recordType2.isSubtype(recordType1));

    // RecordType3 is not a subtype of RecordType1 for the same reason as above
    assertFalse(recordType3.isSubtype(recordType1));
  }

  @Test
  public void testDefinePropertyWhenFrozen() {
    JSTypeNative objectType = JSTypeNative.OBJECT_TYPE;
    JSTypeNative recordType = JSTypeNative.RECORD_TYPE;

    RecordType record = new RecordType(registry, new HashMap<String, RecordProperty>() {{
      put("a", new RecordProperty("a", registry.getInstance(JSType.INT)));
    }}, true);

    assertFalse(record.defineProperty("a", registry.getInstance(JSType.STRING),
        false, null));
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    JSTypeNative objectType = JSTypeNative.OBJECT_TYPE;
    JSTypeNative recordType = JSTypeNative.RECORD_TYPE;

    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("a", new RecordProperty("a", registry.getInstance(JSType.INT)));
    properties1.put("b", new RecordProperty("b", registry.getInstance(JSType.STRING)));

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("a", new RecordProperty("a", registry.getInstance(JSType.INT)));
    properties2.put("c", new RecordProperty("c", registry.getInstance(JSType.STRING)));

    RecordType record1 = new RecordType(registry, properties1, true);
    RecordType record2 = new RecordType(registry, properties2, false);

    assertFalse(record1.checkRecordEquivalenceHelper(record2, EquivalenceMethod.IDENTICAL));
  }

  @Test
  public void testGetOwnPropertyNames() {
    JSTypeNative objectType = JSTypeNative.OBJECT_TYPE;
    JSTypeNative recordType = JSTypeNative.RECORD_TYPE;

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordProperty("a", registry.getInstance(JSType.INT)));
    properties.put("b", new RecordProperty("b", registry.getInstance(JSType.STRING)));

    RecordType record = new RecordType(registry, properties, true);

    Set<String> propertyNames = record.getOwnPropertyNames();

    assertTrue(propertyNames.containsAll(Arrays.asList("a", "b")));
  }
}