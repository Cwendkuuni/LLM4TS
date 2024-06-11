package com.example.recordtypebuilder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.common.collect.Maps;

import java.util.HashMap;

/**
 * Test class for RecordTypeBuilder
 */
public class RecordTypeBuilderTest {

  @Test
  public void testAddProperty() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    String name = "testProperty";
    JSType type = mock(JSType.class);
    Node propertyNode = mock(Node.class);

    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(type);

    assertNull(recordTypeBuilder.addProperty(name, type, propertyNode));
  }

  @Test
  public void testAddPropertyWithDuplicateName() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    String name = "testProperty";
    JSType type = mock(JSType.class);
    Node propertyNode = mock(Node.class);

    RecordProperty recordProperty = new RecordProperty(type, propertyNode);
    recordTypeBuilder.properties.put(name, recordProperty);

    assertNull(recordTypeBuilder.addProperty(name, type, propertyNode));
  }

  @Test
  public void testAddPropertyWithNotNull() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    String name = "testProperty";
    JSType type = mock(JSType.class);
    Node propertyNode = mock(Node.class);

    assertNotNull(recordTypeBuilder.addProperty(name, type, propertyNode));
  }

  @Test
  public void testBuildWithEmptyRecordType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    JSType result = recordTypeBuilder.build();

    JSType expected = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    assertEquals(expected, result);
  }

  @Test
  public void testBuildWithProperties() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    RecordProperty recordProperty1 = new RecordProperty(mock(JSType.class), mock(Node.class));
    RecordProperty recordProperty2 = new RecordProperty(mock(JSType.class), mock(Node.class));

    recordTypeBuilder.properties.put("property1", recordProperty1);
    recordTypeBuilder.properties.put("property2", recordProperty2);

    JSType result = recordTypeBuilder.build();

    RecordType expected = new RecordType(registry,
        Collections.unmodifiableMap(recordTypeBuilder.properties));

    assertEquals(expected, result);
  }
}