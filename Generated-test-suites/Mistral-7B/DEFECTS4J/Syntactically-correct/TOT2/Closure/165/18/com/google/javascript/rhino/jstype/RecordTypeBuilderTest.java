
package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeBuilderTest {

  @Test
  public void testAddProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    String name = "name";
    JSType type = JSType.STRING;
    Node propertyNode = new Node();

    RecordProperty expectedRecordProperty = new RecordProperty(type, propertyNode);
    RecordProperty actualRecordProperty = recordTypeBuilder.addProperty(name, type, propertyNode).get(0);

    assertNotNull(actualRecordProperty);
    assertEquals(expectedRecordProperty.getType(), actualRecordProperty.getType());
  }

  @Test
  public void testAddPropertyDuplicateName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    String name = "name";
    JSType type = JSType.STRING;
    Node propertyNode = new Node();

    RecordProperty firstRecordProperty = recordTypeBuilder.addProperty(name, type, propertyNode).get(0);

    assertNull(recordTypeBuilder.addProperty(name, type, propertyNode));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddPropertyNullName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    String name = null;
    JSType type = JSType.STRING;
    Node propertyNode = new Node();

    recordTypeBuilder.addProperty(name, type, propertyNode);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddPropertyNullType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    String name = "name";
    JSType type = null;
    Node propertyNode = new Node();

    recordTypeBuilder.addProperty(name, type, propertyNode);
  }

  @Test
  public void testBuildEmptyRecord() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    JSType actualRecordType = recordTypeBuilder.build();

    JSType expectedRecordType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    assertEquals(expectedRecordType, actualRecordType);
  }

  @Test
  public void testBuildRecordWithOneProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    String name = "name";
    JSType type = JSType.STRING;
    Node propertyNode = new Node();

    RecordProperty expectedRecordProperty = new RecordProperty(type, propertyNode);
    RecordProperty actualRecordProperty = recordTypeBuilder.addProperty(name, type, propertyNode).get(0);

    JSType actualRecordType = recordTypeBuilder.build();

    RecordType expectedRecordType = new RecordType(
        registry, Collections.singletonMap(name, expectedRecordProperty));

    assertEquals(expectedRecordType, actualRecordType);
  }
}