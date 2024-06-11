package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.BeanUtil;

public class SetterlessPropertyTest {

 private ObjectMapper objectMapper;
 private SettableBeanProperty<List<Integer>> property;

 @Before
 public void setUp() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 BeanPropertyDefinition def = new BeanPropertyDefinition("listProperty", List.class, null, null, null, null, null, null, null, null);
 property = new SetterlessProperty(def, null, null, null, mock(AnnotatedMethod.class));
 }

 @After
 public void tearDown() throws Exception {
 objectMapper = null;
 }

 @Test
 public void testEmptyJsonDeserialization() throws IOException {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 JsonParser jsonParser = objectMapper.treeAsTokens(jsonNode);
 DeserializationContext context = mock(DeserializationContext.class);
 when(context.findBeanProperty(PropertyName.construct("listProperty"))).thenReturn(property);
 Object instance = new ArrayList<>();
 property.deserializeAndSet(jsonParser, context, instance);
 assertEquals(Collections.emptyList(), instance);
 }

 @Test
 public void testJsonDeserialization() throws IOException {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("listProperty", "[1, 2, 3]");
 JsonParser jsonParser = objectMapper.treeAsTokens(jsonNode);
 DeserializationContext context = mock(DeserializationContext.class);
 when(context.findBeanProperty(PropertyName.construct("listProperty"))).thenReturn(property);
 Object instance = new ArrayList<>();
 property.deserializeAndSet(jsonParser, context, instance);
 List<Integer> expectedList = Arrays.asList(1, 2, 3);
 assertEquals(expectedList, instance);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetMethod() throws IOException {
 Object instance = new ArrayList<>();
 property.set(instance, Arrays.asList(1, 2, 3));
 }

 @Test
 public void testJsonDeserializationWithNullValue() throws IOException {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("listProperty", null);
 JsonParser jsonParser = objectMapper.treeAsTokens(jsonNode);
 DeserializationContext context = mock(DeserializationContext.class);
 when(context.findBeanProperty(PropertyName.construct("listProperty"))).thenReturn(property);
 Object instance = new ArrayList<>();
 property.deserializeAndSet(jsonParser, context, instance);
 assertNull(instance);
 }

 @Test(expected = JsonMappingException.class)
 public void testJsonDeserializationWithUnexpectedToken() throws IOException {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 JsonParser jsonParser = objectMapper.treeAsTokens(jsonNode);
 DeserializationContext context = mock(DeserializationContext.class);
 when(context.findBeanProperty(PropertyName.construct("listProperty"))).thenReturn(property);
 jsonParser.nextToken(); // consume START_OBJECT token
 property.deserializeAndSet(jsonParser, context, new ArrayList<>());
 }

 @Test(expected = IOException.class)
 public void testJsonDeserializationWithIOException() throws IOException {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getValueAsString()).thenThrow(IOException.class);
 DeserializationContext context = mock(DeserializationContext.class);
 when(context.findBeanProperty(PropertyName.construct("listProperty"))).thenReturn(property);
 Object instance = new ArrayList<>();
 property.deserializeAndSet(jsonParser, context, instance);
 }

 @Test
 public void testJsonDeserializationWithTypeDeserializer() throws IOException {
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 when(typeDeserializer.deserializeTypedFromObject(any(), any(), any())).thenAnswer(invocation -> Collections.singletonList(1));
 property = new SetterlessProperty(property, typeDeserializer, null);
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("listProperty", "[1]");
 JsonParser jsonParser = objectMapper.treeAsTokens(jsonNode);
 DeserializationContext context = mock(DeserializationContext.class);
 when(context.findBeanProperty(PropertyName.construct("listProperty"))).thenReturn(property);
 Object instance = new ArrayList<>();
 property.deserializeAndSet(jsonParser, context, instance);
 assertEquals(Collections.singletonList(1), instance);
 }
}