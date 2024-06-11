package com.google.javascript.rhino.jstype.test;

import static org.junit.Assert.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

import java.util.Collections;
import java.util.HashMap;

public class RecordTypeBuilderTest {

  private JSTypeRegistry registry;
  private RecordTypeBuilder builder;

  @org.junit.Before
  public void setUp() {
    registry = new JSTypeRegistry();
    builder = new RecordTypeBuilder(registry);
  }

  @org.junit.Test
  public void testEmptyRecord() {
    JSType recordType = builder.build();
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType);
  }

  @org.junit.Test
  public void testRecordWithSingleProperty() {
    JSType numberType = registry.getNumberType();
    JSType recordType = builder
        .addProperty("name", numberType, null)
        .build();

    RecordProperty property = builder.properties.get("name");
    assertNotNull(property);
    assertEquals(numberType, property.getType());
  }

  @org.junit.Test
  public void testRecordWithDuplicateProperty() {
    JSType numberType = registry.getNumberType();
    builder.addProperty("name", numberType, null);
    assertNull(builder.addProperty("name", numberType, null));
  }

  @org.junit.Test
  public void testRecordWithMultipleProperties() {
    JSType numberType = registry.getNumberType();
    JSType stringType = registry.getStringType();

    RecordType recordType = (RecordType) builder
        .addProperty("name", numberType, null)
        .addProperty("age", stringType, null)
        .build();

    HashMap<String, RecordProperty> properties = recordType.getProperties();
    assertEquals(2, properties.size());

    RecordProperty nameProperty = properties.get("name");
    assertEquals(numberType, nameProperty.getType());

    RecordProperty ageProperty = properties.get("age");
    assertEquals(stringType, ageProperty.getType());
  }

  @org.junit.Test
  public void testRecordWithErrorScenario() {
    try {
      builder.addProperty(null, null, null);
      fail("Should not be able to add a null property name.");
    } catch (NullPointerException e) {
      // expected
    }

    try {
      builder.addProperty("name", null, null);
      fail("Should not be able to add a null property type.");
    } catch (NullPointerException e) {
      // expected
    }
  }
}