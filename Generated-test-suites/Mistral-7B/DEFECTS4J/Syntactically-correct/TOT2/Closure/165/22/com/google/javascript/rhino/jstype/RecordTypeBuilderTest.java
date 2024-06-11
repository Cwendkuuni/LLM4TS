package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;
import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.JSTypeRegistryImpl;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

public class RecordTypeBuilderTest {
  private static final JSTypeRegistry registry = new JSTypeRegistryImpl();

  @Test
  public void testAddProperty() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    String name = "testProperty";
    JSType type = JSType.STRING;
    Node propertyNode = new Node();

    RecordProperty recordProperty = recordTypeBuilder.addProperty(name, type, propertyNode);
    assertEquals(type, recordProperty.getType());
    assertFalse(recordTypeBuilder.isEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddDuplicateProperty() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    String name = "testProperty";
    JSType type = JSType.STRING;
    Node propertyNode = new Node();

    recordTypeBuilder.addProperty(name, type, propertyNode);
    recordTypeBuilder.addProperty(name, type, propertyNode);
  }

  @Test(expected = NullPointerException.class)
  public void testAddPropertyWithNullName() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    String name = null;
    JSType type = JSType.STRING;
    Node propertyNode = new Node();

    recordTypeBuilder.addProperty(name, type, propertyNode);
  }

  @Test(expected = NullPointerException.class)
  public void testAddPropertyWithNullType() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    String name = "testProperty";
    JSType type = null;
    Node propertyNode = new Node();

    recordTypeBuilder.addProperty(name, type, propertyNode);
  }

  @Test(expected = NullPointerException.class)
  public void testAddPropertyWithNullPropertyNode() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    String name = "testProperty";
    JSType type = JSType.STRING;
    Node propertyNode = null;

    recordTypeBuilder.addProperty(name, type, propertyNode);
  }

  @Test
  public void testBuildEmptyRecord() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    JSType recordType = recordTypeBuilder.build();
    assertEquals(JSTypeNative.OBJECT_TYPE, recordType);
  }

  @Test
  public void testBuildRecordWithAddedProperties() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    String name1 = "testProperty1";
    String name2 = "testProperty2";
    JSType type1 = JSType.NUMBER;
    JSType type2 = JSType.BOOLEAN;
    Node propertyNode1 = new Node();
    Node propertyNode2 = new Node();

    RecordProperty recordProperty1 = recordTypeBuilder.addProperty(name1, type1, propertyNode1);
    RecordProperty recordProperty2 = recordTypeBuilder.addProperty(name2, type2, propertyNode2);

    JSType recordType = recordTypeBuilder.build();
    RecordType expectedRecordType = new RecordType(registry, Maps.newHashMap());
    expectedRecordType.addProperty("testProperty1", type1);
    expectedRecordType.addProperty("testProperty2", type2);

    assertEquals(expectedRecordType, recordType);
  }
}