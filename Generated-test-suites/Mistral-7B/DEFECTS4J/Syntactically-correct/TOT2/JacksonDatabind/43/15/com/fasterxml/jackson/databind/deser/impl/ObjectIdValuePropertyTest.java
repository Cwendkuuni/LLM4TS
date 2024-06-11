package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectIdNode;

import org.junit.*;

/**
 * Test cases for {@link ObjectIdValueProperty} class.
 */
public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectMapper mapper;

 @BeforeClass
 public static void setupClass() {
 ObjectIdReader objectIdReader = new SimpleObjectIdReader(null, null, null);
 ObjectMapper mapper = new ObjectMapper();
 }

 @Before
 public void setUp() {
 this.objectIdReader = objectIdReader;
 this.mapper = mapper;
 }

 /**
 * Test constructor with valid arguments.
 */
 @Test
 public void testConstructor() throws Exception {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STANDARD_OPTIONS);
 assertNotNull(property);
 }

 /**
 * Test constructor with null arguments.
 */
 @Test(expected = NullPointerException.class)
 public void testConstructorNull() throws Exception {
 new ObjectIdValueProperty(null, PropertyMetadata.STANDARD_OPTIONS);
 }

 /**
 * Test deserializeAndSet method with valid arguments.
 */
 @Test
 public void testDeserializeAndSet() throws Exception {
 Object id = ObjectIdNode.objectId("123").value();
 Object instance = new TestObject();
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STANDARD_OPTIONS);
 JsonParser p = mapper.getFactory().createParser("{\"id\":\"123\"}");
 property.deserializeAndSet(p, mapper.getDeserializationContext(), instance);
 assertEquals("123", instance.getId());
 }

 /**
 * Test deserializeAndSet method with null id.
 */
 @Test
 public void testDeserializeAndSetNullId() throws Exception {
 Object instance = new TestObject();
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STANDARD_OPTIONS);
 JsonParser p = mapper.getFactory().createParser("{\"id\":null}");
 property.deserializeAndSet(p, mapper.getDeserializationContext(), instance);
 assertNull(instance.getId());
 }

 /**
 * Test deserializeSetAndReturn method with valid arguments.
 */
 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 Object id = ObjectIdNode.objectId("123").value();
 Object instance = new TestObject();
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STANDARD_OPTIONS);
 JsonParser p = mapper.getFactory().createParser("{\"id\":\"123\"}");
 Object result = property.deserializeSetAndReturn(p, mapper.getDeserializationContext(), instance);
 assertEquals(instance, result);
 assertEquals("123", instance.getId());
 }

 /**
 * Test set method with valid arguments.
 */
 @Test
 public void testSet() throws Exception {
 TestObject instance = new TestObject();
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STANDARD_OPTIONS);
 property.set(instance, "123");
 assertEquals("123", instance.getId());
 }

 /**
 * Test set method with null value.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testSetNull() throws Exception {
 TestObject instance = new TestObject();
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STANDARD_OPTIONS);
 property.set(instance, null);
 fail("IllegalArgumentException expected but not thrown");
 }

 /**
 * Test getAnnotation method.
 */
 @Test
 public void testGetAnnotation() throws Exception {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STANDARD_OPTIONS);
 assertNull(property.getAnnotation(ObjectId.class));
 }

 /**
 * Test getMember method.
 */
 @Test
 public void testGetMember() throws Exception {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STANDARD_OPTIONS);
 assertNull(property.getMember());
 }

 /**
 * Test deserialization when idProperty is null.
 */
 @Test(expected = UnsupportedOperationException.class)
 public void testDeserializeNullIdProperty() throws Exception {
 ObjectIdReader objectIdReader = new SimpleObjectIdReader(null, null, null) {
 @Override
 public SettableBeanProperty idProperty() {
 return null;
 }
 };
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STANDARD_OPTIONS);
 Object instance = new TestObject();
 JsonParser p = mapper.getFactory().createParser("{\"id\":\"123\"}");
 property.deserializeAndSet(p, mapper.getDeserializationContext(), instance);
 fail("UnsupportedOperationException expected but not thrown");
 }
}

/**
 * Test object with id property.
 */
class TestObject {
 private String id;

 public String getId() {
 return id;
 }

 public void setId(String id) {
 this.id = id;
 }
}

class SimpleObjectIdReader implements ObjectIdReader {
 @Override
 public PropertyName propertyName() {
 return PropertyName.construct("id");
 }

 @Override
 public Class<?> idType() {
 return String.class;
 }

 @Override
 public ObjectIdGenerator idGenerator() {
 return null;
 }

 @Override
 public ObjectIdResolver idResolver() {
 return null;
 }

 @Override
 public SettableBeanProperty idProperty() {
 return null;
 }
}