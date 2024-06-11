package com.example.recordtypebuilder;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class RecordTypeBuilderTest {

  @Test
  public void testConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    assertEquals(true, recordTypeBuilder.isEmpty);
  }

  @Test
  public void testAddProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    String name = "testProperty";
    JSType type = new IntType();
    Node propertyNode = new Node();

    RecordProperty recordProperty = recordTypeBuilder.addProperty(name, type, propertyNode);
    assertFalse(recordTypeBuilder.isEmpty);
    assertNotNull(recordProperty);
  }

  @Test
  public void testBuild() {
    JSTypeRegistry registry = Mockito.mock(JSTypeRegistry.class);
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    String name = "testProperty";
    JSType type = new IntType();
    Node propertyNode = new Node();
    RecordProperty recordProperty = recordTypeBuilder.addProperty(name, type, propertyNode);
    JSType recordType = recordTypeBuilder.build();

    Mockito.verify(registry).registerType(Mockito.any(JSType.class));
    assertEquals(1, recordType.getProperties().size());
    assertEquals(recordProperty.getType(), recordType.getProperty(name));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithNullRegistry() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(null);
  }

  @Test
  public void testAddPropertyWithDuplicateName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    String name = "testProperty";
    JSType type = new IntType();
    Node propertyNode = new Node();
    RecordProperty firstRecordProperty = recordTypeBuilder.addProperty(name, type, propertyNode);
    RecordProperty secondRecordProperty = recordTypeBuilder.addProperty(name, type, propertyNode);

    assertNull(secondRecordProperty);
  }

  @Test
  public void testBuildWithEmptyProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    JSType result = recordTypeBuilder.build();

    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), result);
  }
}