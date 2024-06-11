package com.example.jackson.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests for EnumSerializer class")
public class EnumSerializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Nested
 @DisplayName("Tests for default constructor")
 class DefaultConstructorTests {

 @Test
 void testDefaultConstructor() {
 EnumSerializer serializer = new EnumSerializer(null);
 assertNull(serializer.getEnumValues());
 assertNull(serializer._serializeAsIndex);
 }
 }

 @Nested
 @DisplayName("Tests for constructor with EnumValues")
 class ConstructorWithEnumValuesTests {

 @Test
 void testConstructorWithEnumValues() throws Exception {
 EnumValues enumValues = new EnumValues(MyEnum.class);
 EnumSerializer serializer = new EnumSerializer(enumValues, true);
 assertEquals(MyEnum.class, serializer.handledType());
 assertSame(enumValues, serializer.getEnumValues());
 assertTrue(serializer._serializeAsIndex.booleanValue());
 }
 }

 @Nested
 @DisplayName("Tests for factory method")
 class FactoryMethodTests {

 @Test
 void testFactoryMethod() throws Exception {
 Class<MyEnum> enumClass = MyEnum.class;
 JsonFormat.Value jsonFormat = new JsonFormat.Value().shape(JsonFormat.Shape.STRING);
 EnumSerializer serializer = EnumSerializer.construct(enumClass, MAPPER.getSerializationConfig(), null, jsonFormat);
 assertEquals(enumClass, serializer.handledType());
 assertNull(serializer.getEnumValues());
 assertFalse(serializer._serializeAsIndex.booleanValue());
 }
 }

 @Nested
 @DisplayName("Tests for createContextual method")
 class CreateContextualMethodTests {

 @Test
 void testCreateContextualMethodWithNullProperty() throws Exception {
 SerializerProvider serializers = MAPPER.getSerializerProvider();
 EnumSerializer serializer = (EnumSerializer) serializers.findValueSerializer(MyEnum.class, null);
 assertSame(serializer, serializer.createContextual(serializers, null));
 }

 @Test
 void testCreateContextualMethodWithProperty() throws Exception {
 BeanDescription beanDescription = new BeanDescription(MyEnum.class);
 SerializerProvider serializers = MAPPER.getSerializerProvider();
 JsonFormat.Value jsonFormat = new JsonFormat.Value().shape(JsonFormat.Shape.STRING);
 EnumSerializer expectedSerializer = new EnumSerializer(new EnumValues(MyEnum.class), false);
 EnumSerializer actualSerializer = (EnumSerializer) serializers.findValueSerializer(MyEnum.class, null);
 EnumSerializer serializedSerializer = actualSerializer.createContextual(serializers, beanDescription.forProperty("testProperty"));
 assertEquals(expectedSerializer, serializedSerializer);
 }
 }

 @Nested
 @DisplayName("Tests for serialize method")
 class SerializeMethodTests {

 @Test
 void testSerializeAsIndex() throws Exception {
 SerializerProvider serializers = MAPPER.getSerializerProvider();
 EnumSerializer serializer = new EnumSerializer(new EnumValues(MyEnum.class), true);
 MyEnum myEnum = MyEnum.RED;
 ObjectWriter objectWriter = MAPPER.writerWith(serializers);
 ObjectNode resultNode = objectWriter.writeValueAsTree(myEnum);
 assertEquals(String.valueOf(myEnum.ordinal()), resultNode.get(0).asText());
 }

 @Test
 void testSerializeAsString() throws Exception {
 SerializerProvider serializers = MAPPER.getSerializerProvider();
 EnumSerializer serializer = new EnumSerializer(new EnumValues(MyEnum.class), false);
 MyEnum myEnum = MyEnum.RED;
 ObjectWriter objectWriter = MAPPER.writerWith(serializers);
 ObjectNode resultNode = objectWriter.writeValueAsTree(myEnum);
 assertEquals("RED", resultNode.get(0).asText());
 }
 }

 @Nested
 @DisplayName("Tests for getSchema method")
 class GetSchemaMethodTests {

 @Test
 void testGetSchemaWithNullTypeHint() throws Exception {
 SerializerProvider serializers = MAPPER.getSerializerProvider();
 EnumSerializer serializer = new EnumSerializer(new EnumValues(MyEnum.class), false);
 JsonNode schemaNode = serializer.getSchema(serializers, null);
 ObjectNode schemaObject = (ObjectNode) schemaNode;
 assertTrue(schemaObject.has("type"));
 assertEquals("string", schemaObject.get("type").asText());
 }

 @Test
 void testGetSchemaWithTypeHint() throws Exception {
 SerializerProvider serializers = MAPPER.getSerializerProvider();
 EnumSerializer serializer = new EnumSerializer(new EnumValues(MyEnum.class), false);
 JavaType javaType = MAPPER.getTypeFactory().constructType(MyEnum.class);
 JsonNode schemaNode = serializer.getSchema(serializers, javaType);
 ObjectNode schemaObject = (ObjectNode) schemaNode;
 assertTrue(schemaObject.has("type"));
 assertEquals("string", schemaObject.get("type").asText());
 assertTrue(schemaObject.has("enum"));
 ArrayNode enumArray = schemaObject.get("enum");
 for (JsonNode enumNode : enumArray) {
 assertEquals("RED", enumNode.asText());
 assertEquals("GREEN", enumNode.asText());
 assertEquals("BLUE", enumNode.asText());
 }
 }
 }

 @Nested
 @DisplayName("Tests for acceptJsonFormatVisitor method")
 class AcceptJsonFormatVisitorMethodTests {

 @Test
 void testAcceptJsonFormatVisitorWithNullTypeHint() throws Exception {
 SerializerProvider serializers = MAPPER.getSerializerProvider();
 EnumSerializer serializer = new EnumSerializer(new EnumValues(MyEnum.class), false);
 JsonFormatVisitorWrapper jsonFormatVisitorWrapper = MAPPER.acceptJsonFormatVisitor(serializers, null);
 assertNull(jsonFormatVisitorWrapper.getSchemaProvider().findSchema(JsonFormat.Value.class));
 }

 @Test
 void testAcceptJsonFormatVisitorWithTypeHint() throws Exception {
 SerializerProvider serializers = MAPPER.getSerializerProvider();
 EnumSerializer serializer = new EnumSerializer(new EnumValues(MyEnum.class), false);
 JsonFormat.Value jsonFormat = new JsonFormat.Value();
 JsonFormatVisitorWrapper jsonFormatVisitorWrapper = MAPPER.acceptJsonFormatVisitor(serializers, jsonFormat);
 ObjectNode schemaNode = (ObjectNode) jsonFormatVisitorWrapper.getSchemaProvider().findSchema(JsonFormat.Value.class);
 assertEquals("string", schemaNode.get("type").asText());
 }
 }

 @AfterEach
 void tearDown() {
 MAPPER = null;
 }
}