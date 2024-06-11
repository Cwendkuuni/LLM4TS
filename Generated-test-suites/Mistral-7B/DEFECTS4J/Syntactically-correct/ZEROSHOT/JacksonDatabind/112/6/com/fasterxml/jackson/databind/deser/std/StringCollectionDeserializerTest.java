package com.example.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

@JacksonStdImpl
public class StringCollectionDeserializerTest {

 private static final JavaType STRING_COLLECTION_TYPE = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);

 @SuppressWarnings("unchecked")
 private static final StringCollectionDeserializer DESerializer_NULL_VALUE_PROVIDER = new StringCollectionDeserializer(STRING_COLLECTION_TYPE, null, null, null, null, null);

 @SuppressWarnings("unchecked")
 private static final StringCollectionDeserializer DESerializer_VALUE_DESERIALIZER = new StringCollectionDeserializer(STRING_COLLECTION_TYPE, null, null, mock(JsonDeserializer.class), null, null);

 @SuppressWarnings("unchecked")
 private static final StringCollectionDeserializer DESerializer_DELEGATE_DESERIALIZER = new StringCollectionDeserializer(STRING_COLLECTION_TYPE, null, mock(JsonDeserializer.class), null, null, null);

 @SuppressWarnings("unchecked")
 private static final StringCollectionDeserializer DESerializer_VALUE_INSTANTIATOR = new StringCollectionDeserializer(STRING_COLLECTION_TYPE, mock(ValueInstantiator.class), null, null, null, null);

 @SuppressWarnings("unchecked")
 private static final StringCollectionDeserializer DESerializer_CONTENT_TYPE_DESERIALIZER = new StringCollectionDeserializer(STRING_COLLECTION_TYPE, null, null, null, null, mock(JsonDeserializer.class));

 @SuppressWarnings("unchecked")
 private static final StringCollectionDeserializer DESerializer_NULL_FORMAT_FEATURE = new StringCollectionDeserializer(STRING_COLLECTION_TYPE, null, null, null, null, null);

 @SuppressWarnings("unchecked")
 private static final StringCollectionDeserializer DESerializer_REUSABLE = new StringCollectionDeserializer(STRING_COLLECTION_TYPE, null, null, null, null, null);

 @SuppressWarnings("unchecked")
 private static final StringCollectionDeserializer DESerializer_WRONG_TYPE = new StringCollectionDeserializer(TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, Integer.class), null, null, null, null, null);

 @org.junit.Test
 public void testIsCachable() throws Exception {
 // Test isCachable method
 List<String> result = DESerializer_NULL_VALUE_PROVIDER.deserialize(mock(JsonParser.class), mock(DeserializationContext.class));
 assertTrue(DESerializer_NULL_VALUE_PROVIDER.isCachable());

 result = DESerializer_VALUE_DESERIALIZER.deserialize(mock(JsonParser.class), mock(DeserializationContext.class));
 assertTrue(DESerializer_VALUE_DESERIALIZER.isCachable());

 result = DESerializer_DELEGATE_DESERIALIZER.deserialize(mock(JsonParser.class), mock(DeserializationContext.class));
 assertTrue(DESerializer_DELEGATE_DESERIALIZER.isCachable());

 result = DESerializer_VALUE_INSTANTIATOR.deserialize(mock(JsonParser.class), mock(DeserializationContext.class));
 assertTrue(DESerializer_VALUE_INSTANTIATOR.isCachable());

 result = DESerializer_CONTENT_TYPE_DESERIALIZER.deserialize(mock(JsonParser.class), mock(DeserializationContext.class));
 assertTrue(DESerializer_CONTENT_TYPE_DESERIALIZER.isCachable());

 result = DESerializer_NULL_FORMAT_FEATURE.deserialize(mock(JsonParser.class), mock(DeserializationContext.class));
 assertFalse(DESerializer_NULL_FORMAT_FEATURE.isCachable());

 result = DESerializer_REUSABLE.deserialize(mock(JsonParser.class), mock(DeserializationContext.class));
 assertTrue(DESerializer_REUSABLE.isCachable());

 result = DESerializer_WRONG_TYPE.deserialize(mock(JsonParser.class), mock(DeserializationContext.class));
 assertFalse(DESerializer_WRONG_TYPE.isCachable());
 }

 @org.junit.Test
 public void testCreateContextual() throws Exception {
 // Test createContextual method
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode node = mapper.createObjectNode();
 JsonNode rootNode = mapper.readTree(node.toString());

 DeserializationContext ctxt = mock(DeserializationContext.class);
 BeanProperty property = mock(BeanProperty.class);

 List<String> result = null;

 // Test null values provider
 result = DESerializer_NULL_VALUE_PROVIDER.createContextual(ctxt, property);
 assertNull(result);

 // Test null deserializer
 result = DESerializer_VALUE_DESERIALIZER.createContextual(ctxt, property);
 assertNull(result);

 // Test delegate deserializer
 result = DESerializer_DELEGATE_DESERIALIZER.createContextual(ctxt, property);
 assertNotNull(result);

 // Test value deserializer
 result = DESerializer_VALUE_INSTANTIATOR.createContextual(ctxt, property);
 assertNotNull(result);

 // Test content type deserializer
 result = DESerializer_CONTENT_TYPE_DESERIALIZER.createContextual(ctxt, property);
 assertNotNull(result);

 // Test null format feature
 result = DESerializer_NULL_FORMAT_FEATURE.createContextual(ctxt, property);
 assertNull(result);

 // Test reusable deserializer
 result = DESerializer_REUSABLE.createContextual(ctxt, property);
 assertNotNull(result);

 // Test deserializer with wrong type
 result = DESerializer_WRONG_TYPE.createContextual(ctxt, property);
 assertNull(result);

 // Test deserializer with JSON array
 JsonToken jsonToken = JsonToken.START_ARRAY;
 JsonParser parser = mock(JsonParser.class);
 when(parser.getCurrentToken()).thenReturn(jsonToken);
 when(parser.nextToken()).thenReturn(jsonToken);
 when(parser.getValueAsString()).thenReturn("test");

 result = DESerializer_DELEGATE_DESERIALIZER.createContextual(ctxt, property);
 List<String> expected = Arrays.asList("test");
 assertEquals(expected, result);
 }

 @org.junit.Test
 public void testDeserialize() throws Exception {
 // Test deserialize method
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode node = mapper.createObjectNode();
 JsonNode rootNode = mapper.readTree(node.toString());

 DeserializationContext ctxt = mock(DeserializationContext.class);
 JsonParser parser = mapper.treeToValues(rootNode, new TypeReference<Collection<String>>() {});

 List<String> result = null;

 // Test null values provider
 result = DESerializer_NULL_VALUE_PROVIDER.deserialize(parser, ctxt);
 assertEquals(Collections.emptyList(), result);

 // Test null deserializer
 result = DESerializer_VALUE_DESERIALIZER.deserialize(parser, ctxt);
 assertEquals(Collections.emptyList(), result);

 // Test delegate deserializer
 result = DESerializer_DELEGATE_DESERIALIZER.deserialize(parser, ctxt);
 assertEquals(Collections.singletonList("test"), result);

 // Test value deserializer
 result = DESerializer_VALUE_INSTANTIATOR.deserialize(parser, ctxt);
 assertEquals(Arrays.asList("test"), result);

 // Test content type deserializer
 result = DESerializer_CONTENT_TYPE_DESERIALIZER.deserialize(parser, ctxt);
 assertEquals(Arrays.asList("test"), result);

 // Test null format feature
 result = DESerializer_NULL_FORMAT_FEATURE.deserialize(parser, ctxt);
 assertEquals(Collections.emptyList(), result);

 // Test reusable deserializer
 result = DESerializer_REUSABLE.deserialize(parser, ctxt);
 assertEquals(Arrays.asList("test"), result);

 // Test deserializer with wrong type
 result = DESerializer_WRONG_TYPE.deserialize(parser, ctxt);
 assertEquals(Collections.emptyList(), result);
 }

 @org.junit.Test
 public void testDeserializeWithType() throws Exception {
 // Test deserializeWithType method
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode node = mapper.createObjectNode();
 JsonNode rootNode = mapper.readTree(node.toString());
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

 DeserializationContext ctxt = mock(DeserializationContext.class);
 JsonParser parser = mapper.treeToValues(rootNode, new TypeReference<Collection<String>>() {});

 List<String> result = null;

 result = DESerializer_DELEGATE_DESERIALIZER.deserializeWithType(parser, ctxt, typeDeserializer);
 assertEquals(Collections.singletonList("test"), result);
 }
}