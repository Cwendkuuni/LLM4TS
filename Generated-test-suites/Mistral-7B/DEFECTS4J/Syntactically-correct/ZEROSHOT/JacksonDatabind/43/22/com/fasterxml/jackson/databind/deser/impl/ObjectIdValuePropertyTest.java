package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

/**
 * Unit tests for {@link ObjectIdValueProperty} class.
 */
public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @Before
 public void setUp() {
 objectIdReader = Mockito.mock(ObjectIdReader.class);
 ObjectIdGenerator idGenerator = Mockito.mock(ObjectIdGenerator.class);
 ObjectIdResolver idResolver = Mockito.mock(ObjectIdResolver.class);
 SimpleBeanPropertyFilter idPropertyFilter = new SimpleBeanPropertyFilter();
 ObjectIdDeserializer objectIdDeserializer = new ObjectIdDeserializer();
 objectIdDeserializer.setPropertyFilter(idPropertyFilter);
 objectIdDeserializer.setContentConverter(new ToStringSerializer(String.class));
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 objectIdValueProperty.setValueDeserializer(objectIdDeserializer);
 }

 @Test
 public void testBasic() throws IOException {
 Object id = "123";
 JsonParser jsonParser = Mockito.mock(JsonParser.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 Object instance = new Object();
 Object expectedId = "expectedId";
 when(objectIdReader.getIdType()).thenReturn(String.class);
 when(objectIdReader.propertyName.getName()).thenReturn("id");
 when(objectIdReader.idProperty).thenReturn(new PropertyName("expectedProperty"));

 // Test deserializeAndSet
 objectIdValueProperty.deserializeAndSet(jsonParser, context, instance);
 assertEquals(expectedId, objectIdReader.getIdResolver().resolveId(instance));

 // Test deserializeSetAndReturn
 Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, context, instance);
 assertEquals(instance, result);
 assertEquals(expectedId, objectIdReader.getIdResolver().resolveId(instance));

 // Test set
 objectIdValueProperty.set(instance, id);
 assertEquals(expectedId, objectIdReader.getIdResolver().resolveId(instance));

 // Test setAndReturn
 Object returnValue = objectIdValueProperty.setAndReturn(instance, id);
 assertEquals(instance, returnValue);
 assertEquals(expectedId, objectIdReader.getIdResolver().resolveId(instance));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithoutIdProperty() throws IOException {
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 Object instance = new Object();
 Object id = "123";
 objectIdValueProperty.set(instance, id);
 fail("Should not call set() on ObjectIdProperty that has no SettableBeanProperty");
 }

 @Test
 public void testGetAnnotation() {
 Class<? extends Annotation> annotationType = Mockito.mock(Annotation.class);
 assertNull(objectIdValueProperty.getAnnotation(annotationType));
 }

 @Test
 public void testGetMember() {
 assertNull(objectIdValueProperty.getMember());
 }
}