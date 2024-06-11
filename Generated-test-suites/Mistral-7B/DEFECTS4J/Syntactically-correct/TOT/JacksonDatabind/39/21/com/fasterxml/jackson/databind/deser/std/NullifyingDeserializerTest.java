package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

/**
 * Test cases for NullifyingDeserializer class
 */
public class NullifyingDeserializerTest {

 @Test
 public void testDeserializeNullifyingObject() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser mockParser = createMockParser();
 StdDeserializer<Object> deserializer = new NullifyingDeserializer();

 mapper.readValue(new ByteArrayInputStream(new byte[0]), deserializer.getType());

 // Deserialize using mocked parser
 Object readValue = mapper.readerForUpdating(null).readValue(mockParser);

 // Assert that deserialization returns null
 assertNull(readValue);
 }

 @Test
 public void testDeserializeNullifyingArray() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser mockParser = createMockParser();
 JsonNode node = mapper.readTree("[1, 2, 3]");
 StdDeserializer<Object> deserializer = new NullifyingDeserializer();

 // Deserialize using mocked parser
 Object readValue = mapper.readerForUpdating(new Object[0]).readValue(mockParser, deserializer.getType());

 // Assert that deserialization returns empty array
 assertNull(readValue);
 }

 @Test
 public void testDeserializeNullifyingObjectWithType() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser mockParser = createMockParser();
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructType(String.class);
 StdDeserializer<Object> deserializer = new NullifyingDeserializer();

 // Deserialize using mocked parser
 Object readValue = mapper.readerForUpdating(new Object[0]).readValue(mockParser, deserializer.getType(typeDeserializer));

 // Assert that deserialization returns null
 assertNull(readValue);
 }

 @Test
 public void testDeserializeNullifyingObjectWithInvalidToken() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser mockParser = createMockParser();
 mockParser.nextToken(JsonTokenId.ID_VALUE_STRING); // Set token to ID_VALUE_STRING
 StdDeserializer<Object> deserializer = new NullifyingDeserializer();

 // Deserialize using mocked parser
 Object readValue = mapper.readerForUpdating(null).readValue(mockParser);

 // Assert that deserialization returns null, even with invalid token
 assertNull(readValue);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeNullifyingObjectWithTypeMismatch() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser mockParser = createMockParser();
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructType(Integer.class);
 StdDeserializer<Object> deserializer = new NullifyingDeserializer();

 // Deserialize using mocked parser
 Object readValue = mapper.readerForUpdating(null).readValue(mockParser, deserializer.getType(typeDeserializer));

 // Assert that deserialization fails with JsonMappingException due to type mismatch
 }

 // Helper method to create mock JsonParser
 private JsonParser createMockParser() {
 return mock(JsonParser.class);
 }

}