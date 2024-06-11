package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectMapper objectMapper;
 private JsonDeserializer<Object> objectIdDeserializer;
 private ObjectIdValueProperty property;
 private BeanPropertyDefinition beanPropertyDefinition;

 @Before
 public void setUp() throws Exception {
 ObjectIdReader mockObjectIdReader = mock(ObjectIdReader.class);
 objectIdReader = mockObjectIdReader;
 when(mockObjectIdReader.propertyName).thenReturn(PropertyName.construct("id"));
 when(mockObjectIdReader.getIdType()).thenReturn(Long.class);
 when(mockObjectIdReader.getDeserializer()).thenReturn(mock(JsonDeserializer.class));
 when(mockObjectIdReader.idProperty).thenReturn(null);

 ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
 objectMapper = mockObjectMapper;
 when(mockObjectMapper.findValueToDeserialize(any(JsonParser.class), anyInt(), anyInt()))
         .thenCallRealMethod();
 when(mockObjectMapper.findValueToDeserialize(any(JsonParser.class), anyString()))
         .thenCallRealMethod();

 JsonDeserializer<Object> mockDeserializer = mock(JsonDeserializer.class);
 objectIdDeserializer = mockDeserializer;
 when(mockDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
         .thenAnswer(invocation -> null);

 PropertyName propertyName = PropertyName.construct("myObjectIdProperty");
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 this.property = new ObjectIdValueProperty(property, new PropertyName("myObjectIdProperty"));
 this.beanPropertyDefinition = new BeanPropertyDefinition(propertyName, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 Object instance = new Object();
 long id = 1L;
 DeserializationContext ctx = mock(DeserializationContext.class);
 JsonParser parser = mock(JsonParser.class);
 ObjectReader reader = objectMapper.readerForUpdating(instance.getClass());
 ObjectIdReader objectIdReaderSpy = spy(objectIdReader);
 when(objectIdReaderSpy.getIdType()).thenReturn(Long.class);
 when(objectIdReaderSpy.getIdType()).thenReturn(Long.class);
 when(objectMapper.readValue(parser, JsonDeserializer.None.class)).thenReturn(null);
 when(objectMapper.readValue(parser, Long.class)).thenReturn(id);
 when(objectMapper.readValueAsArray(parser, Long[].class)).thenReturn(new Long[] { id });
 when(objectIdReaderSpy.getDeserializer()).thenReturn(objectIdDeserializer);
 reader = new ObjectIdReaderBasedDeserializer(reader, objectIdReaderSpy);

 property.deserializeAndSet(parser, ctx, instance);
 assertEquals(id, reader.getBeanProperty("myObjectIdProperty").getValue(instance));
 }

 @Test
 public void testDeserializeSetAndReturnNullId() throws Exception {
 Object instance = new Object();
 DeserializationContext ctx = mock(DeserializationContext.class);
 JsonParser parser = mock(JsonParser.class);
 ObjectReader reader = objectMapper.readerForUpdating(instance.getClass());
 ObjectIdReader objectIdReaderSpy = spy(objectIdReader);
 when(objectIdReaderSpy.getIdType()).thenReturn(Long.class);
 when(objectMapper.readValue(parser, JsonDeserializer.None.class)).thenReturn(null);
 when(objectMapper.readValue(parser, Long.class)).thenReturn(null);
 when(objectMapper.readValueAsArray(parser, Long[].class)).thenReturn(null);
 when(objectIdReaderSpy.getDeserializer()).thenReturn(objectIdDeserializer);
 reader = new ObjectIdReaderBasedDeserializer(reader, objectIdReaderSpy);

 property.deserializeSetAndReturn(parser, ctx, instance);
 assertNull(reader.getBeanProperty("myObjectIdProperty").getValue(instance));
 }

 @Test
 public void testSetAndReturn() throws Exception {
 Object instance = new Object();
 long id = 1L;
 SettableBeanProperty idProperty = mock(SettableBeanProperty.class);
 when(idProperty.setAndReturn(instance, id)).thenReturn(instance);
 ObjectIdReader objectIdReaderSpy = spy(objectIdReader);
 when(objectIdReaderSpy.getIdType()).thenReturn(Long.class);
 when(objectIdReaderSpy.getIdType()).thenReturn(Long.class);
 when(objectMapper.readValue(parser, JsonDeserializer.None.class)).thenReturn(null);
 when(objectMapper.readValue(parser, Long.class)).thenReturn(id);
 when(objectMapper.readValueAsArray(parser, Long[].class)).thenReturn(new Long[] { id });
 when(objectIdReaderSpy.getDeserializer()).thenReturn(objectIdDeserializer);
 when(objectIdReaderSpy.idProperty).thenReturn(idProperty);
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReaderSpy, new PropertyMetadata());
 property.set(instance, id);
 assertEquals(id, reader.getBeanProperty("myObjectIdProperty").getValue(instance));
 }

 @Test
 public void testSetAndReturnWithNullValue() throws Exception {
 Object instance = new Object();
 ObjectIdReader objectIdReaderSpy = spy(objectIdReader);
 when(objectIdReaderSpy.getIdType()).thenReturn(Long.class);
 when(objectIdReaderSpy.getIdType()).thenReturn(Long.class);
 when(objectMapper.readValue(parser, JsonDeserializer.None.class)).thenThrow(IOException.class);
 when(objectMapper.readValue(parser, Long.class)).thenReturn(null);
 when(objectMapper.readValueAsArray(parser, Long[].class)).thenReturn(null);
 when(objectIdReaderSpy.getDeserializer()).thenReturn(objectIdDeserializer);
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReaderSpy, new PropertyMetadata());
 property.set(instance, null);
 assertNull(reader.getBeanProperty("myObjectIdProperty").getValue(instance));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetAndReturnWithNoIdProperty() throws Exception {
 Object instance = new Object();
 ObjectIdReader objectIdReaderSpy = spy(objectIdReader);
 when(objectIdReaderSpy.getIdType()).thenReturn(Long.class);
 when(objectIdReaderSpy.getIdType()).thenReturn(Long.class);
 when(objectMapper.readValue(parser, JsonDeserializer.None.class)).thenReturn(null);
 when(objectMapper.readValue(parser, Long.class)).thenReturn(null);
 when(objectMapper.readValueAsArray(parser, Long[].class)).thenReturn(null);
 when(objectIdReaderSpy.getDeserializer()).thenReturn(objectIdDeserializer);
 when(objectIdReaderSpy.idProperty).thenReturn(null);
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReaderSpy, new PropertyMetadata());
 property.set(instance, 1L);
 }

 @Test
 public void testGetAnnotation() throws Exception {
 Object instance = new Object();
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 assertNull(property.getAnnotation(Annotation.class));
 }

 @Test
 public void testGetMember() throws Exception {
 Object instance = new Object();
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 assertNull(property.getMember());
 }
}

class ObjectIdReaderBasedDeserializer extends BeanDeserializerBase {
 private final ObjectIdReader objectIdReader;

 ObjectIdReaderBasedDeserializer(ObjectReader baseDeserializer, ObjectIdReader objectIdReader) {
 super(baseDeserializer, null);
 this.objectIdReader = objectIdReader;
 }

 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 Object id = null;
 if (p.isExpectedStartArrayToken()) {
 id = objectMapper.readValueAsArray(p, Long[].class)[0];
 } else {
 id = objectMapper.readValue(p, Long.class);
 }
 Object idProperty = ctxt.findBeanProperty("id");
 Object objectId = _valueDeserializer.deserialize(p, ctxt);
 Object result = null;
 if (id != null) {
 ObjectIdReader objectIdReaderSpy = spy(objectIdReader);
 when(objectIdReaderSpy.getIdType()).thenReturn(Long.class);
 when(objectIdReaderSpy.getIdType()).thenReturn(Long.class);
 when(objectMapper.readValue(p, JsonDeserializer.None.class)).thenCallRealMethod();
 when(objectMapper.readValue(p, Long.class)).thenReturn(id);
 when(objectMapper.readValueAsArray(p, Long[].class)).thenReturn(new Long[] { id });
 when(objectIdReaderSpy.getDeserializer()).thenReturn(objectIdDeserializer);
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReaderSpy, new PropertyMetadata());
 result = property.deserializeSetAndReturn(p, ctxt, null);
 } else {
 result = _valueDeserializer.deserialize(p, ctxt);
 }
 return result;
 }
}