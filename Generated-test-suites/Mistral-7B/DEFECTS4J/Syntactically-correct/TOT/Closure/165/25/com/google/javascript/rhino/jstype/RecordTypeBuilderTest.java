package com.google.javascript.rhino.jstype.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

import org.junit.Test;

public class RecordTypeBuilderTest {

  @Test
  public void testRecordTypeBuilder() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    JSType stringType = registry.getStringType();
    JSType numberType = registry.getNumberType();

    // Test adding a property with String type
    RecordProperty stringProperty = recordTypeBuilder.addProperty(
        "property1", stringType, new Node());
    assertNotNull(stringProperty);
    assertEquals(stringType, stringProperty.getType());

    // Test adding a property with Number type
    RecordProperty numberProperty = recordTypeBuilder.addProperty(
        "property2", numberType, new Node());
    assertNotNull(numberProperty);
    assertEquals(numberType, numberProperty.getType());

    // Test adding a duplicate property
    assertNull(recordTypeBuilder.addProperty("property1", stringType, new Node()));

    // Test building the record type
    JSType recordType = recordTypeBuilder.build();
    assertNotNull(recordType);
    assertEquals(RecordType.class, recordType.getClass());
  }

  @Test(expected = IllegalStateException.class)
  public void testBuildEmptyRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    recordTypeBuilder.build();
  }
}