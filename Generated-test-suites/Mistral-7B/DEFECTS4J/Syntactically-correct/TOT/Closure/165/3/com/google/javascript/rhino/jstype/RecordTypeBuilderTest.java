package com.google.javascript.rhino.jstype.test;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RecordTypeBuilderTest {
  private JSTypeRegistry registry;
  private RecordTypeBuilder builder;

  @Before
  public void setUp() {
    registry = mock(JSTypeRegistry.class);
    builder = new RecordTypeBuilder(registry);
  }

  @Test
  public void testEmptyRecord() {
    JSType record = builder.build();
    assertEquals(JSTypeNative.OBJECT_TYPE, record.getType());
  }

  @Test
  public void testAddProperty() {
    JSType intType = registry.getIntType();
    Node propertyNode = mock(Node.class);

    builder.addProperty("name", intType, propertyNode);
    RecordType record = (RecordType) builder.build();

    ImmutableMap<String, RecordProperty> properties = record.getProperties();
    RecordProperty property = properties.get("name");

    assertEquals(intType, property.getType());
    assertEquals(propertyNode, property.getPropertyNode());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddDuplicateProperty() {
    JSType intType = registry.getIntType();
    Node propertyNode = mock(Node.class);

    builder.addProperty("name", intType, propertyNode);
    builder.addProperty("name", intType, propertyNode);
    builder.build();
  }

  @Test
  public void testBuildWithProperties() {
    JSType stringType = registry.getStringType();
    Node stringPropertyNode = mock(Node.class);

    JSType intType = registry.getIntType();
    Node intPropertyNode = mock(Node.class);

    builder.addProperty("name", stringType, stringPropertyNode);
    builder.addProperty("age", intType, intPropertyNode);

    RecordType record = (RecordType) builder.build();
    ImmutableMap<String, RecordProperty> properties = record.getProperties();

    assertEquals(2, properties.size());
    assertEquals(stringType, properties.get("name").getType());
    assertEquals(stringPropertyNode, properties.get("name").getPropertyNode());
    assertEquals(intType, properties.get("age").getType());
    assertEquals(intPropertyNode, properties.get("age").getPropertyNode());
  }
}