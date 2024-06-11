package com.google.javascript.rhino.jstype.test;

import static org.junit.Assert.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.RecordType.RecordProperty;

import java.util.HashMap;
import java.util.Map;

public class RecordTypeBuilderTest {

  private static JSTypeRegistry registry;

  @org.junit.BeforeClass
  public static void setUp() {
    registry = new JSTypeRegistry();
  }

  /**
   * Test method for {@link RecordTypeBuilder#addProperty(String, JSType, Node)}.
   */
  @org.junit.Test
  public void testAddProperty() {
    // Create a new record type builder and add two properties.
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    builder.addProperty("name", registry.getStringType(), new Node());
    builder.addProperty("age", registry.getNumberType(), new Node());

    // Build the record type and assert it has the expected properties.
    JSType recordType = builder.build();
    assertEquals(RecordType.class, recordType.getClass());
    Map<String, RecordProperty> properties = ((RecordType) recordType).getProperties();
    assertEquals(2, properties.size());
    RecordProperty nameProperty = properties.get("name");
    assertEquals(registry.getStringType(), nameProperty.getType());
    RecordProperty ageProperty = properties.get("age");
    assertEquals(registry.getNumberType(), ageProperty.getType());
  }

  /**
   * Test method for {@link RecordTypeBuilder#addProperty(String, JSType, Node)}.
   * This test case checks for a duplicate property name.
   */
  @org.junit.Test
  public void testAddDuplicateProperty() {
    // Create a new record type builder and add a property.
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    builder.addProperty("name", registry.getStringType(), new Node());

    // Attempt to add a property with the same name.
    RecordTypeBuilder duplicateBuilder = new RecordTypeBuilder(registry);
    duplicateBuilder.addProperty("name", registry.getStringType(), new Node());

    // Assert that the second builder returns null, indicating a failed add.
    assertNull(duplicateBuilder.build());
  }

  /**
   * Test method for {@link RecordTypeBuilder#build()}.
   * This test case checks that an empty record type builder returns the correct
   * object type.
   */
  @org.junit.Test
  public void testEmptyRecordType() {
    // Create a new record type builder and build an empty record type.
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType recordType = builder.build();

    // Assert that the record type is the native object type.
    assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getClass());
  }
}