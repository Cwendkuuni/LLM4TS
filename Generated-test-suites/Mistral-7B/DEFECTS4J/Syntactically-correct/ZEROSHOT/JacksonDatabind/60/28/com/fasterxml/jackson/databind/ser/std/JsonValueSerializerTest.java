package com.example.jsonvalueserializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.ser.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JsonValueSerializerTest")
class JsonValueSerializerTest {

 @Nested
 @DisplayName("Test Serialization")
 class SerializeTest {

 @DisplayName("Test serialization with annotated method and provided serializer")
 @Test
 void testSerializationWithAnnotatedMethodAndProvidedSerializer() throws JsonProcessingException {
 // Given
 JsonValueSerializer serializer = new JsonValueSerializer(
 new AnnotatedMethod(TestClassWithJsonValue.class, "getValueMethod", TestClassWithJsonValue.class),
 new StdSerializer<>(String.class)
 );
 // When
 Object testInstance = new TestClassWithJsonValue();
 String expectedResult = "ExpectedResult";

 // Then
 String actualResult = serializer.serialize(testInstance, new JsonGenerator(), new SerializerProvider());
 assertEquals(expectedResult, actualResult);
 }

 @DisplayName("Test serialization with annotated method and autodetected serializer")
 @Test
 void testSerializationWithAnnotatedMethodAndAutodetectedSerializer() throws JsonProcessingException {
 // Given
 JsonValueSerializer serializer = new JsonValueSerializer(
 new AnnotatedMethod(TestClassWithJsonValue.class, "getValueMethod", TestClassWithJsonValue.class),
 null
 );
 // When
 Object testInstance = new TestClassWithJsonValue();
 String expectedResult = "ExpectedResult";

 // Then
 String actualResult = serializer.serialize(testInstance, new JsonGenerator(), new SerializerProvider());
 assertEquals(expectedResult, actualResult);
 }

 @DisplayName("Test serialization with annotated method and non-autodetected serializer")
 @Test
 void testSerializationWithAnnotatedMethodAndNonAutodetectedSerializer() throws JsonProcessingException {
 // Given
 JsonValueSerializer<Integer> serializer = new JsonValueSerializer<>(
 new AnnotatedMethod(TestClassWithJsonValueAndIntValue.class, "getValueMethodForInt", TestClassWithJsonValueAndIntValue.class),
 new StdSerializer<>("")
 );
 // When
 Object testInstance = new TestClassWithJsonValueAndIntValue();
 Integer expectedResult = 42;

 // Then
 Integer actualResult = (Integer) serializer.serialize(testInstance, new JsonGenerator(), new SerializerProvider());
 assertEquals(expectedResult, actualResult);
 }
 }

 @Nested
 @DisplayName("Test Contextualization")
 class ContextualizationTest {

 @DisplayName("Test contextualization with annotated method and autodetected serializer")
 @Test
 void testContextualizationWithAnnotatedMethodAndAutodetectedSerializer() throws JsonProcessingException {
 // Given
 ObjectMapper mapper = new ObjectMapper();
 JsonValueSerializer serializer = mapper.findValueSerializer(TestClassWithJsonValue.class, null);

 // When
 Object testInstance = new TestClassWithJsonValue();

 // Then
 assertEquals(TestClassWithJsonValue.class, serializer.getHandlerType());
 assertEquals("getValueMethod", serializer.getPropertyName());
 }

 @DisplayName("Test contextualization with annotated method and provided serializer")
 @Test
 void testContextualizationWithAnnotatedMethodAndProvidedSerializer() throws JsonProcessingException {
 // Given
 ObjectMapper mapper = new ObjectMapper();
 JsonValueSerializer<String> serializer = new JsonValueSerializer<>(
 new AnnotatedMethod(TestClassWithJsonValue.class, "getValueMethod", TestClassWithJsonValue.class),
 new StdSerializer<>("")
 );

 // When
 Object testInstance = new TestClassWithJsonValue();

 // Then
 assertEquals(TestClassWithJsonValue.class, serializer.getHandlerType());
 assertEquals("getValueMethod", serializer.getPropertyName());
 assertEquals("", serializer.getValueSerializer().getClass().getName());
 }
 }

 @Nested
 @DisplayName("Test Schema Generation")
 class SchemaTest {

 @Test
 void testSchemaGenerationWithoutEnum() throws JsonProcessingException {
 // Given
 ObjectMapper mapper = new ObjectMapper();
 JsonValueSerializer serializer = mapper.findValueSerializer(TestClassWithJsonValue.class, null);

 // When
 JsonSchema schema = serializer.getSchema(new SerializerProvider(), null);

 // Then
 assertEquals(JsonSchema.Type.OBJECT, schema.getType());
 }

 @Test
 void testSchemaGenerationWithEnum() throws JsonProcessingException {
 // Given
 ObjectMapper mapper = new ObjectMapper();
 JsonValueSerializer serializer = mapper.findValueSerializer(TestClassWithJsonValueEnum.class, null);

 // When
 JsonSchema schema = serializer.getSchema(new SerializerProvider(), null);

 // Then
 assertEquals(JsonSchema.Type.OBJECT, schema.getType());
 assertEquals("$ref", schema.getProperties().get("testProperty").getSchema$ref$());
 }
 }

}

class TestClassWithJsonValue {
 @JsonSerialize(using = StdSerializer.class)
 final String getValueMethod() {
 return "ExpectedResult";
 }
}

class TestClassWithJsonValueAndIntValue {
 @JsonSerialize(using = StdSerializer.class)
 final Integer getValueMethodForInt() {
 return 42;
 }
}

class TestClassWithJsonValueEnum {
 @JsonSerialize
 enum TestEnum {
 @JsonValue
 EXAMPLE
 }
 final TestEnum testProperty;

 TestClassWithJsonValueEnum() {
 testProperty = TestEnum.EXAMPLE;
 }
}