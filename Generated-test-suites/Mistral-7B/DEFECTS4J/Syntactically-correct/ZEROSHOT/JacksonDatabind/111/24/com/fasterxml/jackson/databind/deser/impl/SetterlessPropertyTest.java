package com.fasterxml.jackson.databind.deser.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SetterlessPropertyTest {

 @Mock
 private JsonParser mockJsonParser;
 @Mock
 private DeserializationContext mockDeserializationContext;
 @Mock
 private Object mockInstance;
 @Mock
 private TypeDeserializer mockTypeDeserializer;
 @Mock
 private Annotations mockContextAnnotations;
 @Mock
 private AnnotatedMethod mockAnnotatedMethod;
 @Mock
 private JsonDeserializer<?> mockJsonDeserializer;
 @Mock
 private NullValueProvider mockNullValueProvider;

 @InjectMocks
 private SetterlessProperty setterlessProperty;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 when(mockJsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
 when(mockDeserializationContext.isEnabled(any(MapperFeature.class))).thenReturn(true);
 when(mockAnnotatedMethod.getAnnotated()).thenReturn(mockContextAnnotations);
 }

 @After
 public void tearDown() {
 reset(mockJsonParser, mockDeserializationContext, mockInstance, mockTypeDeserializer, mockContextAnnotations, mockAnnotatedMethod, mockJsonDeserializer, mockNullValueProvider);
 }

 @Test
 public void testGetter() {
 Object propertyValue = setterlessProperty.getGetter().invoke(mockInstance, (Object[]) null);
 assertNotNull(propertyValue);
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 setterlessProperty.deserializeAndSet(mockJsonParser, mockDeserializationContext, mockInstance);
 // Assume getter returns an empty collection or map for JSON null
 // and _valueDeserializer modifies the returned object correctly.
 verify(mockInstance, times(1)).invokeMethod(_getter.getName(), (Object[]) null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() throws IOException {
 setterlessProperty.set(mockInstance, new Object());
 }
}