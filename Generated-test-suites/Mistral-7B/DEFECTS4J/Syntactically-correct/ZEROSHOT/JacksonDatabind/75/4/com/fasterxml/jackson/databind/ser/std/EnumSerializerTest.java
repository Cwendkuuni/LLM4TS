package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EnumSerializerTest {

 private EnumSerializer enumSerializer;
 private EnumValues enumValues;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 // Create EnumValues mock
 enumValues = mock(EnumValues.class);
 when(enumValues.getEnumClass()).thenReturn(TestEnum.class);
 when(enumValues.getValuesAsArray()).thenReturn(new Enum[]{TestEnum.ONE, TestEnum.TWO, TestEnum.THREE});
 when(enumValues.valuesAsStringArray()).thenReturn(new String[]{"ONE", "TWO", "THREE"});
 when(enumValues.serializedValueFor(TestEnum.ONE)).thenReturn("ONE_VALUE");
 when(enumValues.serializedValueFor(TestEnum.TWO)).thenReturn("TWO_VALUE");
 when(enumValues.serializedValueFor(TestEnum.THREE)).thenReturn("THREE_VALUE");

 // Create ObjectMapper mock
 objectMapper = mock(ObjectMapper.class);
 when(objectMapper.getSerializationConfig()).thenReturn(new SerializationConfig());
 when(objectMapper.findValueToFormat(any(Class.class), any(BeanProperty.class), any(JsonFormat.Value.class)))
 .thenReturn(null);
 when(objectMapper.getFactory().createSerializer(TestEnum.class)).thenReturn(enumSerializer);

 // Create EnumSerializer
 enumSerializer = new EnumSerializer(enumValues, true);
 }

 @Test
 public void testSerializeAsIndex() throws JsonProcessingException {
 JsonGenerator gen = mock(JsonGenerator.class);
 ObjectSerializerCollection serializers = mock(ObjectSerializerCollection.class);
 when(objectMapper.getSerializerProvider().getSerializerProvider()).thenReturn(serializers);
 when(serializers.findValueSerializer(TestEnum.class)).thenReturn(enumSerializer);

 // Test serialization as index
 when(enumSerializer._serializeAsIndex(serializers)).thenReturn(true);
 enumSerializer.serialize(TestEnum.ONE, gen, objectMapper.getSerializerProvider());
 verify(gen).writeNumber(0);
 }

 @Test
 public void testSerializeToString() throws JsonProcessingException {
 JsonGenerator gen = mock(JsonGenerator.class);
 ObjectSerializerCollection serializers = mock(ObjectSerializerCollection.class);
 when(objectMapper.getSerializerProvider().getSerializerProvider()).thenReturn(serializers);
 when(serializers.findValueSerializer(TestEnum.class)).thenReturn(enumSerializer);

 // Test serialization using toString()
 when(enumSerializer._serializeAsIndex(serializers)).thenReturn(false);
 when(objectMapper.getSerializationConfig().isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);
 enumSerializer.serialize(TestEnum.ONE, gen, objectMapper.getSerializerProvider());
 verify(gen).writeString("ONE");
 }

 @Test
 public void testSerializeByName() throws JsonProcessingException {
 JsonGenerator gen = mock(JsonGenerator.class);
 ObjectSerializerCollection serializers = mock(ObjectSerializerCollection.class);
 when(objectMapper.getSerializerProvider().getSerializerProvider()).thenReturn(serializers);
 when(serializers.findValueSerializer(TestEnum.class)).thenReturn(enumSerializer);

 // Test serialization using name()
 when(enumSerializer._serializeAsIndex(serializers)).thenReturn(false);
 when(objectMapper.getSerializationConfig().isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);
 enumSerializer.serialize(TestEnum.ONE, gen, objectMapper.getSerializerProvider());
 verify(gen).writeString("ONE_VALUE");
 }

 @Test
 public void testGetSchema() throws JsonProcessingException {
 JsonNode schema = enumSerializer.getSchema(objectMapper.getSerializerProvider(), TestEnum.class.getType());
 assertEquals("{\"type\":\"object\",\"properties\":{\"enum\":[\"ONE\",\"TWO\",\"THREE\"]}}", schema.toString());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 EnumSerializer serializer = new EnumSerializer(enumValues, false);
 serializer.acceptJsonFormatVisitor(visitor, TestEnum.class.getType());
 assertTrue(visitor.wasVisited(JsonFormat.Value.class));
 assertEquals("{\"enum\":[\"ONE\",\"TWO\",\"THREE\"]}", visitor.getSerializableValueAsString());
 }

 @Test
 public void testCreateContextualSerializer() throws JsonProcessingException {
 JsonGenerator gen = mock(JsonGenerator.class);
 ObjectSerializerCollection serializers = mock(ObjectSerializerCollection.class);
 ObjectNode node = mock(ObjectNode.class);
 BeanProperty property = mock(BeanProperty.class);
 when(objectMapper.getSerializerProvider().getSerializerProvider()).thenReturn(serializers);
 when(serializers.findValueSerializer(TestEnum.class)).thenReturn(enumSerializer);
 when(property.getType().getRawClass()).thenReturn(TestEnum.class);

 // Test creating contextual serializer with default configuration
 when(objectMapper.findValueToFormat(TestEnum.class, property, null)).thenReturn(null);
 EnumSerializer serializer = (EnumSerializer) enumSerializer.createContextual(objectMapper.getSerializerProvider(), property);
 assertEquals(enumSerializer, serializer);

 // Test creating contextual serializer with custom configuration
 JsonFormat jsonFormat = mock(JsonFormat.Value.class);
 when(objectMapper.findValueToFormat(TestEnum.class, property, jsonFormat)).thenReturn(jsonFormat);
 when(jsonFormat.getShape()).thenReturn(JsonFormat.Shape.STRING);
 EnumSerializer serializer2 = (EnumSerializer) enumSerializer.createContextual(objectMapper.getSerializerProvider(), property);
 assertNotEquals(enumSerializer, serializer2);
 assertEquals(false, serializer2._serializeAsIndex);
 }
}

class TestEnum {
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 public enum TestEnum {
 ONE,
 TWO,
 THREE
 }
}