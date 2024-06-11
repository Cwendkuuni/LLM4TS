package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testDeserializeEmptyArray() throws IOException {
 String[] result = mapper.readValue("[]", String[].class);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeSingleElementArray() throws IOException {
 String[] result = mapper.readValue("[\"hello\"]", String[].class);
 assertArrayEquals(new String[]{"hello"}, result);
 }

 @Test
 public void testDeserializeNullValueInArray() throws IOException {
 String[] result = mapper.readValue("[\"hello\", null]", String[].class);
 assertArrayEquals(new String[]{"hello", null}, result);
 }

 @Test
 public void testDeserializeCustomElementDeserializer() throws IOException {
 JsonDeserializer<String> elementDeserializer = mapper.initValueDeserializer(String.class, new MyCustomStringDeserializer());
 StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
 String[] result = mapper.readerForArrayOf(String[].class)
 .readValue(new CharArrayReader("[\"apple\", \"banana\", \"orange\"]"), deserializer);
 assertArrayEquals(new String[]{"apple", "banana", "orange"}, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeInvalidJson() throws IOException {
 mapper.readValue("{ \"key\": \"value\" }", String[].class);
 fail("Expected IOException but no exception was thrown");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeValueNotFoundInJson() throws IOException {
 mapper.readValue("[]", new TypeReference<String[]>() {}.getType());
 fail("Expected JsonMappingException but no exception was thrown");
 }

 @Test
 public void testDeserializeWithType() throws IOException {
 TypeDeserializer typeDeserializer = mapper.getTypeDeserializerForAnnotation(String[].class, JsonDeserialize.With.Type.class);
 String[] result = mapper.readerForArrayOf(String[].class)
 .readValue(new CharArrayReader("[\"apple\", \"banana\", \"orange\"]"), typeDeserializer);
 assertArrayEquals(new String[]{"apple", "banana", "orange"}, result);
 }

 @Test
 public void testDeserializeCustomContextualDeserializer() throws IOException {
 JsonDeserializer<String> contextualDeserializer = new MyCustomContextualStringDeserializer();
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 deserializer.setContentDeserializer(contextualDeserializer);
 String[] result = mapper.readerForArrayOf(String[].class)
 .readValue(new CharArrayReader("[\"apple\", \"banana\", \"orange\"]"), deserializer);
 assertArrayEquals(new String[]{"customApple", "customBanana", "customOrange"}, result);
 }

 @Test
 public void testDeserializeEmptyStringAsNull() throws IOException {
 JsonParser jp = mapper.getFactory().createParser("[\"\", \"hello\"]");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 Object[] result = deserializer.deserializeWithType(jp, ctxt, contextType);
 assertNull(result[0]);
 assertArrayEquals(new String[]{"hello"}, asList(result).subList(1, result.length).toArray(new String[0]));
 }

 @Test
 public void testDeserializeNonArray() throws IOException {
 JsonParser jp = mapper.getFactory().createParser("{\"key\": [\"apple\", \"banana\", \"orange\"]}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 try {
 String[] result = new StringArrayDeserializer().deserialize(jp, ctxt);
 fail("Expected IOException but no exception was thrown");
 } catch (IOException e) {
 // expected
 }
 }

 private static TypeReference<String[]> contextType = new TypeReference<String[]>() {}.getType();
}

class MyCustomStringDeserializer extends StdDeserializer<String> {
 @Override
 public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 return "custom" + jp.getValueAsString();
 }
}

class MyCustomContextualStringDeserializer extends StdDeserializer<String> implements ContextualDeserializer {
 @Override
 public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 return "custom" + jp.getValueAsString();
 }

 @Override
 public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
 return new MyCustomContextualStringDeserializer();
 }
}