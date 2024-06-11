package com.example;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonValueSerializerTest {

 private ObjectMapper objectMapper;
 private JsonValueSerializer jsonValueSerializer;

 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 JsonSerialize jsonSerialize = new JsonSerialize();
 jsonSerialize.using(new MySerializer());
 Class<MyClass> myClass = MyClass.class;
 JsonMethod myMethod = MyClass.class.getMethod("getValue");
 jsonValueSerializer = new JsonValueSerializer(myMethod, jsonSerialize);
 }

 @Test
 public void testSerialization() throws Exception {
 Object testObject = new MyClass();
 String expectedOutput = "{\"value\":42}";
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();
 jsonValueSerializer.serialize(testObject, jsonGenerator, objectMapper.getSerializerProvider());
 jsonGenerator.flush();
 String actualOutput = jsonGenerator.getOutputContent();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testSerializationWithType() throws Exception {
 Object testObject = new MyClass();
 String expectedOutput = "{\"value\":42}";
 ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter().withView(MyClass.class);
 JsonNode actualOutput = objectWriter.writeValueAsTree(testObject);
 assertEquals(expectedOutput, actualOutput.toString());
 }

 @Test
 public void testSerializationWithNullValue() throws Exception {
 Object testObject = new MyClass();
 String expectedOutput = "null";
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();
 jsonValueSerializer.serialize(testObject, jsonGenerator, objectMapper.getSerializerProvider());
 jsonGenerator.flush();
 String actualOutput = jsonGenerator.getOutputContent();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testContextualSerialization() throws Exception {
 Object testObject = new MyClass();
 ObjectMapper contextualObjectMapper = new ObjectMapper();
 JsonValueSerializer contextualJsonValueSerializer = contextualObjectMapper.findValueSerializer(MyClass.class, null);
 String expectedOutput = "{\"value\":42}";
 JsonGenerator jsonGenerator = contextualObjectMapper.getFactory().createGenerator();
 contextualJsonValueSerializer.serialize(testObject, jsonGenerator, contextualObjectMapper.getSerializerProvider());
 jsonGenerator.flush();
 String actualOutput = jsonGenerator.getOutputContent();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testContextualSerializationWithType() throws Exception {
 Object testObject = new MyClass();
 ObjectMapper contextualObjectMapper = new ObjectMapper();
 JsonNode actualOutput = contextualObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsTree(testObject);
 String expectedOutput = "{\"value\":42}";
 assertEquals(expectedOutput, actualOutput.toString());
 }

 @Test
 public void testSchema() throws Exception {
 Object testObject = new MyClass();
 JsonSchema schema = jsonValueSerializer.getSchema(objectMapper.getSerializerProvider(), MyClass.class);
 assertNotNull(schema);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationWithException() throws Exception {
 Object testObject = new MyClassWithException();
 String expectedOutput = "{\"value\":42}";
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();
 jsonValueSerializer.serialize(testObject, jsonGenerator, objectMapper.getSerializerProvider());
 jsonGenerator.flush();
 String actualOutput = jsonGenerator.getOutputContent();
 fail("Expected JsonMappingException to be thrown");
 }

 @Test
 public void testNaturalTypeSerialization() throws Exception {
 Object intObject = 42;
 JsonSerializer<Object> jsonSerializer = objectMapper.findValueSerializer(int.class, null);
 assertTrue(jsonSerializer instanceof JsonValueSerializer);
 }

 @Test
 public void testNaturalTypeSerializationWithString() throws Exception {
 String stringObject = "test";
 JsonSerializer<Object> jsonSerializer = objectMapper.findValueSerializer(String.class, null);
 assertTrue(jsonSerializer instanceof JsonValueSerializer);
 }

 @Test
 public void testNaturalTypeSerializationWithBoolean() throws Exception {
 boolean booleanObject = true;
 JsonSerializer<Object> jsonSerializer = objectMapper.findValueSerializer(boolean.class, null);
 assertTrue(jsonSerializer instanceof JsonValueSerializer);
 }

 @Test
 public void testNaturalTypeSerializationWithDouble() throws Exception {
 double doubleObject = 42.0;
 JsonSerializer<Object> jsonSerializer = objectMapper.findValueSerializer(double.class, null);
 assertTrue(jsonSerializer instanceof JsonValueSerializer);
 }

}

class MyClass {
 @JsonSerialize(using = MySerializer.class)
 private int value;

 public int getValue() {
 return value;
 }
}

class MyClassWithException {
 @JsonSerialize(using = MySerializer.class)
 private int value;

 public int getValue() throws Exception {
 throw new Exception();
 }
}

class MySerializer extends JsonSerializer<Object> {
 @Override
 public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
 jsonGenerator.writeString("value");
 jsonGenerator.writeNumber(value.hashCode());
 }
}