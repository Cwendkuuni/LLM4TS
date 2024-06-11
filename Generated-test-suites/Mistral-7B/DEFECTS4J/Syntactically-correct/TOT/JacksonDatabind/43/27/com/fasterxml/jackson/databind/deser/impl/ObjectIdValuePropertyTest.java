package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader mockObjectIdReader;
 private JsonDeserializer<Object> mockValueDeserializer;
 private ObjectIdValueProperty objectIdValueProperty;
 private JsonParser mockJsonParser;
 private DeserializationContext mockContext;

 @Before
 public void setup() {
 mockObjectIdReader = mock(ObjectIdReader.class);
 mockValueDeserializer = mock(JsonDeserializer.class);
 mockJsonParser = mock(JsonParser.class);
 mockContext = mock(DeserializationContext.class);

 objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, new PropertyMetadata());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object id = new Object();
 when(mockObjectIdReader.getIdType()).thenReturn(ObjectIdReader.Type.LONG);
 when(mockObjectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

 Object result = objectIdValueProperty.deserializeAndSet(mockJsonParser, mockContext, new Object());

 verify(mockContext, times(1)).findObjectId(eq(id), eq(any(ObjectIdGenerator.class)), eq(any(ObjectIdResolver.class)));
 verify(mockContext, times(1)).bindItem(eq(new Object()), eq(id));

 assertNull(result);
 }

 @Test
 public void testDeserializeSetAndReturnNullId() throws IOException {
 Object result = objectIdValueProperty.deserializeSetAndReturn(mockJsonParser, mockContext, new Object());

 assertNull(result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithoutIdProperty() throws IOException {
 Object instance = new Object();
 objectIdValueProperty.set(instance, new Object());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithNullInstance() throws IOException {
 objectIdValueProperty.set(null, new Object());
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object id = new Object();
 when(mockObjectIdReader.getIdType()).thenReturn(ObjectIdReader.Type.LONG);
 when(mockObjectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

 Object result = objectIdValueProperty.setAndReturn(new Object(), id);

 verify(mockObjectIdReader.idProperty, times(1)).setAndReturn(eq(new Object()), eq(id));
 assertEquals(id, result);
 }
}