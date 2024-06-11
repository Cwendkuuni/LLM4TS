package com.example.json; // replace with your package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonSerializer.ValueType;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class JsonValueSerializerTest {

 private JsonValueSerializer jsonValueSerializer;
 private ObjectWriter objectWriter;
 private TypeSerializer typeSerializer;
 private SerializerProvider serializerProvider;
 private JsonGenerator jsonGenerator;

 @Before
 public void setUp() {
 Object testObject = new TestJsonValueObject();
 Class<Object> handledType = TestJsonValueObject.class;
 AnnotatedMethod accessorMethod = mock(AnnotatedMethod.class);
 when(accessorMethod.getName()).thenReturn("getValue");
 when(accessorMethod.getValue(testObject)).thenReturn("testValue");
 when(accessorMethod.getType()).thenReturn(handledType);
 JsonSerializer<Object> valueSerializer = mock(JsonSerializer.class);
 JsonValue jsonValue = mock(JsonValue.class);
 when(jsonValue.value()).thenReturn("testValue");
 when(valueSerializer.valueTypes()).thenReturn(ValueType.VALUE_STRING);
 when(valueSerializer.isEnabled(null)).thenReturn(true);
 when(valueSerializer.serializeValuesUsing(null)).thenReturn(jsonValue);
 when(valueSerializer.createContextual(null, null)).thenReturn(valueSerializer);
 when(valueSerializer.serialize(jsonValue, jsonGenerator, serializerProvider)).thenReturn(null);
 when(valueSerializer.serializeWithType(jsonValue, jsonGenerator, serializerProvider, typeSerializer)).thenReturn(null);
 when(valueSerializer.acceptJsonFormatVisitor(null, null)).thenReturn(null);

 jsonValueSerializer = new JsonValueSerializer(accessorMethod, valueSerializer);
 objectWriter = mock(ObjectWriter.class);
 typeSerializer = mock(TypeSerializer.class);
 serializerProvider = mock(SerializerProvider.class);
 jsonGenerator = mock(JsonGenerator.class);
 }

 @Test
 public void testSerialize() throws JsonMappingException, IOException {
 ArgumentCaptor<Object> objectCaptor = ArgumentCaptor.forClass(Object.class);
 ArgumentCaptor<JsonGenerator> jsonGeneratorCaptor = ArgumentCaptor.forClass(JsonGenerator.class);
 ArgumentCaptor<SerializerProvider> serializerProviderCaptor = ArgumentCaptor.forClass(SerializerProvider.class);
 ArgumentCaptor<TypeSerializer> typeSerializerCaptor = ArgumentCaptor.forClass(TypeSerializer.class);

 jsonValueSerializer.serialize(new TestJsonValueObject(), jsonGenerator, serializerProvider);
 assertEquals("testValue", jsonGeneratorCaptor.getValue().writeValueAsString());

 verify(valueSerializer).serialize(jsonValueCaptor.capture(JsonValue.class), jsonGeneratorCaptor.capture(JsonGenerator.class), serializerProviderCaptor.capture(SerializerProvider.class));
 JsonValue jsonValue = jsonValueCaptor.getValue();
 assertEquals("testValue", jsonValue.getValue());
 Object testValue = jsonValue.value();
 assertEquals("testValue", testValue);
 verify(valueSerializer).serialize(testValue, jsonGeneratorCaptor.capture(JsonGenerator.class), serializerProviderCaptor.capture(SerializerProvider.class));
 verify(valueSerializer).serializeWithType(testValue, jsonGeneratorCaptor.capture(JsonGenerator.class), serializerProviderCaptor.capture(SerializerProvider.class), typeSerializerCaptor.capture(TypeSerializer.class));
 verify(typeSerializer).writeTypePrefixForScalar(objectCaptor.capture(Object.class), jsonGeneratorCaptor.capture(JsonGenerator.class));
 verify(valueSerializer).serialize(testValue, jsonGeneratorCaptor.capture(JsonGenerator.class), serializerProviderCaptor.capture(SerializerProvider.class));
 verify(typeSerializer).writeTypeSuffixForScalar(objectCaptor.capture(Object.class), jsonGeneratorCaptor.capture(JsonGenerator.class));
 assertEquals(handledType, objectCaptor.getValue());
 }

 @Test
 public void testCreateContextual() throws JsonMappingException {
 JsonSerializer<Object> contextualSerializer = jsonValueSerializer.createContextual(serializerProvider, null);
 assertEquals(jsonValueSerializer, contextualSerializer);
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper jsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
 jsonValueSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);
 verify(jsonFormatVisitorWrapper).expectAnyFormat(TypeFactory.defaultInstance.constructType(ClassUtil.handleRawClass(handledType)));
 }

}

 class TestJsonValueObject {
 @JsonValue
 public String getValue() {
 return "testValue";
 }
 }