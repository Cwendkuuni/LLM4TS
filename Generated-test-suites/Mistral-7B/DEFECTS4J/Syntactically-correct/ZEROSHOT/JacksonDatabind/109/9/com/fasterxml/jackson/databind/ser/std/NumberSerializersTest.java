package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.Base;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Test class for {@link NumberSerializers}
 */
public class NumberSerializersTest {

 private JsonSerializerProvider serializerProvider;
 private JsonFactory jsonFactory;

 @Before
 public void setUp() {
 serializerProvider = new StandardSerializerFinder().findValueSerializers(null);
 jsonFactory = new JsonFactory();
 }

 @After
 public void tearDown() {
 serializerProvider = null;
 jsonFactory = null;
 }

 /**
 * Test method for {@link NumberSerializers#addAll(Map)}
 */
 @Test
 public void testAddAll() {
 Map<String, JsonSerializer<?>> allDeserializers = mock(Map.class);
 NumberSerializers.addAll(allDeserializers);

 verify(allDeserializers, atLeast(1)).put(eq(Integer.class.getName()), any(JsonSerializer.class));
 verify(allDeserializers, atLeast(1)).put(eq(Integer.TYPE.getName()), any(JsonSerializer.class));
 verify(allDeserializers, atLeast(1)).put(eq(Long.class.getName()), any(JsonSerializer.class));
 verify(allDeserializers, atLeast(1)).put(eq(Long.TYPE.getName()), any(JsonSerializer.class));

 verify(allDeserializers, atLeast(1)).put(eq(Byte.class.getName()), eq(NumberSerializers.IntLikeSerializer.instance));
 verify(allDeserializers, atLeast(1)).put(eq(Byte.TYPE.getName()), eq(NumberSerializers.IntLikeSerializer.instance));
 verify(allDeserializers, atLeast(1)).put(eq(Short.class.getName()), eq(ShortSerializer.instance));
 verify(allDeserializers, atLeast(1)).put(eq(Short.TYPE.getName()), eq(ShortSerializer.instance));

 verify(allDeserializers, atLeast(1)).put(eq(Double.class.getName()), any(JsonSerializer.class));
 verify(allDeserializers, atLeast(1)).put(eq(Double.TYPE.getName()), any(JsonSerializer.class));
 verify(allDeserializers, atLeast(1)).put(eq(Float.class.getName()), eq(FloatSerializer.instance));
 verify(allDeserializers, atLeast(1)).put(eq(Float.TYPE.getName()), eq(FloatSerializer.instance));
 }

 /**
 * Test method for {@link IntegerSerializer#serialize(Object, JsonGenerator, SerializerProvider)}
 */
 @Test
 public void testIntegerSerializerSerialize() throws JsonProcessingException, IOException {
 int value = 123;
 IntegerSerializer serializer = (IntegerSerializer) serializerProvider.findValueSerializer(Integer.class);
 JsonGenerator gen = mock(JsonGenerator.class);

 serializer.serialize(value, gen, serializerProvider);

 verify(gen, times(1)).writeNumber(eq(value));
 }

 /**
 * Test method for {@link ShortSerializer#serialize(Object, JsonGenerator, SerializerProvider)}
 */
 @Test
 public void testShortSerializerSerialize() throws JsonProcessingException, IOException {
 short value = 123;
 ShortSerializer serializer = (ShortSerializer) serializerProvider.findValueSerializer(Short.class);
 JsonGenerator gen = mock(JsonGenerator.class);

 serializer.serialize(value, gen, serializerProvider);

 verify(gen, times(1)).writeNumber(eq((int) value));
 }

 /**
 * Test method for {@link NumberSerializers.Base#getSchema(SerializerProvider, Type)}
 */
 @Test
 public void testBaseGetSchema() throws JsonProcessingException {
 Base<Object> base = (Base<Object>) serializerProvider.findValueSerializer(Short.class);
 Type type = new TypeReference<Number>() {}.getType();

 assertEquals("number", base.getSchema(serializerProvider, type).get("type").asText());
 }
}