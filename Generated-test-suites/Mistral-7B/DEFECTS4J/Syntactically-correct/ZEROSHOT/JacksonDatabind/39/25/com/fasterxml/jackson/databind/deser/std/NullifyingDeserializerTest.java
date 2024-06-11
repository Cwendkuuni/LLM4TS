package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullifyingDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testDeserializeNullifyingObject() throws JsonProcessingException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{}");
 Object result = objectMapper.readValue(jsonParser, NullifyingDeserializer.class.getName());
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingArray() throws JsonProcessingException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("[{}]");
 Object result = objectMapper.readValue(jsonParser, NullifyingDeserializer.class.getName());
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingObjectWithType() throws JsonProcessingException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{ \"type\": \"java.lang.String\" }");
 ObjectDeserializer stringDeserializer = objectMapper.findValueDeserializer(String.class);
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructValueType(Object.class, String.class);
 Object result = objectMapper.readValue(jsonParser, NullifyingDeserializer.class.getName(), typeDeserializer, stringDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingArrayWithType() throws JsonProcessingException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("[{ \"type\": \"java.lang.String\" }]");
 ObjectDeserializer stringDeserializer = objectMapper.findValueDeserializer(String.class);
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructArrayType(Object.class, String.class);
 Object result = objectMapper.readValue(jsonParser, NullifyingDeserializer.class.getName(), typeDeserializer, stringDeserializer);
 assertNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeNullifyingWithUnexpectedToken() throws JsonProcessingException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("true");
 Object result = objectMapper.readValue(jsonParser, NullifyingDeserializer.class.getName());
 fail("Expected JsonMappingException: Unexpected token (START_BOOLEAN) at [Source: (StringReader); line: 1, column: 1]");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeNullifyingWithStartArray() throws JsonProcessingException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("[{}]");
 ObjectDeserializer arrayDeserializer = objectMapper.findValueDeserializer(Object[].class);
 Object result = objectMapper.readValue(jsonParser, NullifyingDeserializer.class.getName(), arrayDeserializer);
 fail("Expected JsonMappingException: Unexpected token (START_ARRAY): expected a String or a Number or a Boolean or null or an Array or an Object");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeNullifyingWithStartObject() throws JsonProcessingException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"name\":\"John\"}");
 ObjectDeserializer basicMapDeserializer = objectMapper.findValueDeserializer(Map.class);
 Object result = objectMapper.readValue(jsonParser, NullifyingDeserializer.class.getName(), basicMapDeserializer);
 fail("Expected JsonMappingException: Unexpected token (START_OBJECT), expected a String or a Number or a Boolean or null or an Array or an Object or a Map");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeNullifyingWithFieldName() throws JsonProcessingException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{ \"name\":\"John\" }");
 ObjectDeserializer fieldNameDeserializer = objectMapper.findValueDeserializer(String.class);
 Object result = objectMapper.readValue(jsonParser, NullifyingDeserializer.class.getName(), null, fieldNameDeserializer);
 fail("Expected JsonMappingException: Unexpected token (FIELD_NAME) : cannot construct value of type [simple type, class Object] from JSON String: null");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeNullifyingWithCustomTypeId() throws JsonProcessingException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{ \"type\": \"MyCustomType\" }");
 JavaType customType = objectMapper.getTypeFactory().constructType("MyCustomType");
 ObjectDeserializer customTypeDeserializer = objectMapper.findValueDeserializer(customType);
 Object result = objectMapper.readValue(jsonParser, NullifyingDeserializer.class.getName(), customTypeDeserializer);
 fail("Expected JsonMappingException: Unexpected token (START_OBJECT): cannot construct value of type [simple type, class MyCustomType] from JSON String: null");
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeNullifyingWithBase64() throws JsonProcessingException {
 JsonParser jsonParser = objectMapper.getFactory().createParser(Base64Variants.STANDARD.encodeBase64("{}"));
 Object result = objectMapper.readValue(jsonParser, NullifyingDeserializer.class.getName());
 fail("Expected JsonProcessingException: Can not deserialize instance of com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer out of VALUE_STRING text: Can not construct instance of com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer, problem: serialization of NullifyingDeserializer to JSON is not supported (missing default constructor or no empty constructor or doesn't have an acceptable default constructor); no setter/field for type [simple type, class Object]");
 }
}