package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for StringArrayDeserializer
 */
public class StringArrayDeserializerTest {

private ObjectMapper objectMapper;

@Before
public void setUp() {
objectMapper = new ObjectMapper();
objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
}

@Test
public void testDeserializeEmptyArray() throws Exception {
JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
DeserializationContext ctxt = objectMapper.getDeserializationContext();
StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
String[] result = deserializer.deserialize(jsonParser, ctxt);
assertArrayEquals(new String[0], result);
}

@Test
public void testDeserializeStringArray() throws Exception {
JsonParser jsonParser = objectMapper.getFactory().createParser("[\"abc\",\"def\",\"ghi\"]");
DeserializationContext ctxt = objectMapper.getDeserializationContext();
StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
String[] result = deserializer.deserialize(jsonParser, ctxt);
assertArrayEquals(new String[]{"abc", "def", "ghi"}, result);
}

@Test
public void testDeserializeCustomElementDeserializer() throws Exception {
JsonParser jsonParser = objectMapper.getFactory().createParser("[\"1\",\"2\",\"3\"]");
DeserializationContext ctxt = objectMapper.getDeserializationContext();
ObjectReader reader = objectMapper.readerForArrayOf(String.class);
TypeDeserializer typeDeserializer = reader.getTypeDeserializer();
StringArrayDeserializer deserializer = new StringArrayDeserializer(typeDeserializer);
String[] result = deserializer.deserialize(jsonParser, ctxt);
assertArrayEquals(new String[]{"1", "2", "3"}, result);
}

@Test
public void testDeserializeNullValue() throws Exception {
JsonParser jsonParser = objectMapper.getFactory().createParser("null");
DeserializationContext ctxt = objectMapper.getDeserializationContext();
StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
String[] result = deserializer.deserialize(jsonParser, ctxt);
assertArrayEquals(new String[0], result);
}

@Test
public void testDeserializeInvalidJson() throws Exception {
JsonParser jsonParser = objectMapper.getFactory().createParser("{");
DeserializationContext ctxt = objectMapper.getDeserializationContext();
StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
try {
deserializer.deserialize(jsonParser, ctxt);
fail("Expected JsonMappingException");
} catch (JsonMappingException e) {
assertEquals("Cannot construct instance of [Ljava.lang.String; from JSON String: <Root>: unexpected character ('{' (code 123)): expected a VALUE_STRING or a START_ARRAY", e.getMessage());
}
}

@Test
public void testDeserializeEmptyStringAsNull() throws Exception {
JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
DeserializationContext ctxt = objectMapper.getDeserializationContext();
ctxt.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
String[] result = deserializer.deserialize(jsonParser, ctxt);
assertEquals(null, result);
}

@Test
public void testDeserializeSingleStringAsArray() throws Exception {
JsonParser jsonParser = objectMapper.getFactory().createParser("\"abc\"");
DeserializationContext ctxt = objectMapper.getDeserializationContext();
ctxt.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
String[] result = deserializer.deserialize(jsonParser, ctxt);
assertArrayEquals(new String[]{"abc"}, result);
}

@Test
public void testCreateContextual() throws Exception {
ObjectReader reader = objectMapper.readerForArrayOf(String.class);
TypeDeserializer typeDeserializer = reader.getTypeDeserializer();
StringArrayDeserializer deserializer = new StringArrayDeserializer(typeDeserializer);
DeserializationContext ctxt = objectMapper.getDeserializationContext();
BeanProperty property = new BeanProperty("propertyName", String[].class);
JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);
assertEquals(StringArrayDeserializer.class, result.getClass());
}
}