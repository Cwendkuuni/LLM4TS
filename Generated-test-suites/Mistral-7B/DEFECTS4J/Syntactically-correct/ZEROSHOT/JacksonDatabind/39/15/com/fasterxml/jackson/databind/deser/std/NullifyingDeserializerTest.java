package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Test class for {@link NullifyingDeserializer}.
 */
public class NullifyingDeserializerTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testDeserializeNullifyingDeserializerObject() throws JsonProcessingException {
 Object result = mapper.readValue( "_null.json", NullifyingDeserializer.class );
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerArray() throws JsonProcessingException {
 String json = "[null]";
 Object result = mapper.readValue( json, NullifyingDeserializer[].class );
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerArrayWithType() throws JsonProcessingException {
 String json = "[1, null, 3]";
 ObjectReader reader = mapper.readerForArray(Integer.class);
 Object result = mapper.readValue( json, reader.with(NullifyingDeserializer.instance));
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerObjectWithType() throws JsonProcessingException {
 ObjectReader reader = mapper.readerForValue(String.class);
 String json = "{\"name\": null}";
 Object result = mapper.readValue( json, reader.with(NullifyingDeserializer.instance));
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerObjectWithTypeDeserializer() throws JsonProcessingException {
 ObjectReader reader = mapper.readerForValue(String.class);
 ObjectReader typeReader = mapper.readerForValue(String.class).with(new StdSerializer<String>(String.class) {
 @Override
 public String serializeWithType(String value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSerializers)
 throws IOException, JsonProcessingException {
 return null;
 }
 });
 String json = "{\"name\": null}";
 Object result = mapper.readValue( json, reader.with(NullifyingDeserializer.instance)
 .and(typeReader));
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerNullObject() throws JsonProcessingException {
 String json = "null";
 Object result = mapper.readValue( json, NullifyingDeserializer.class );
 assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeNullifyingDeserializerNonNullObject() throws JsonProcessingException {
 String json = "{\"name\": \"John Doe\"}";
 Object result = mapper.readValue( json, NullifyingDeserializer.class );
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerEmpty() throws JsonProcessingException {
 String json = "[]";
 Object result = mapper.readValue( json, NullifyingDeserializer.class );
 assertNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeNullifyingDeserializerUnexpectedToken() throws JsonProcessingException {
 String json = "[1, 2, 3]";
 ObjectReader reader = mapper.readerForValue(String.class);
 Object result = mapper.readValue( json, reader.with(NullifyingDeserializer.instance));
 assertNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeNullifyingDeserializerUnexpectedFieldName() throws JsonProcessingException {
 ObjectReader reader = mapper.readerForValue(String.class);
 String json = "{\"id\": null}";
 Object result = mapper.readValue( json, reader.with(NullifyingDeserializer.instance));
 assertNull(result);
 }
}