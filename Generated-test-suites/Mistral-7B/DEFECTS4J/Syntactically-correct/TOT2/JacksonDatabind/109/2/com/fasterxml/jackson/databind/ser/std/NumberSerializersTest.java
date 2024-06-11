import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NumberSerializersTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 void testIntegerSerializer() throws IOException, JsonProcessingException {
 Integer number = 123;
 Map<String, JsonSerializer<?>> serializers = new java.util.HashMap<>();
 NumberSerializers.addAll(serializers);
 String json = MAPPER.writeValueAsString(number, serializers.get(Integer.class));
 assertEquals("123", json);
 }

 @Test
 void testIntegerSerializerWithTypeSerialization() throws IOException, JsonProcessingException {
 Integer number = 123;
 Map<String, JsonSerializer<?>> serializers = new java.util.HashMap<>();
 NumberSerializers.addAll(serializers);
 String json = MAPPER.writeValueAsString(number, serializers.get(Integer.class), new TypeSerializer());
 assertEquals("123", json);
 }

 @Test
 void testIntegerSerializerNegativeNumber() throws IOException, JsonProcessingException {
 Integer number = -123;
 Map<String, JsonSerializer<?>> serializers = new java.util.HashMap<>();
 NumberSerializers.addAll(serializers);
 String json = MAPPER.writeValueAsString(number, serializers.get(Integer.class));
 assertEquals("-123", json);
 }

 @Test
 void testIntegerSerializerZero() throws IOException, JsonProcessingException {
 Integer number = 0;
 Map<String, JsonSerializer<?>> serializers = new java.util.HashMap<>();
 NumberSerializers.addAll(serializers);
 String json = MAPPER.writeValueAsString(number, serializers.get(Integer.class));
 assertEquals("0", json);
 }

 @Test
 void testIntegerSerializerMaxValue() throws IOException, JsonProcessingException {
 Integer number = Integer.MAX_VALUE;
 Map<String, JsonSerializer<?>> serializers = new java.util.HashMap<>();
 NumberSerializers.addAll(serializers);
 String json = MAPPER.writeValueAsString(number, serializers.get(Integer.class));
 assertEquals(String.valueOf(Integer.MAX_VALUE), json);
 }

 @Test
 void testIntegerSerializerMinValue() throws IOException, JsonProcessingException {
 Integer number = Integer.MIN_VALUE;
 Map<String, JsonSerializer<?>> serializers = new java.util.HashMap<>();
 NumberSerializers.addAll(serializers);
 String json = MAPPER.writeValueAsString(number, serializers.get(Integer.class));
 assertEquals(String.valueOf(Integer.MIN_VALUE), json);
 }

 @Test
 void testIntegerSerializerNull() {
 Map<String, JsonSerializer<?>> serializers = new java.util.HashMap<>();
 NumberSerializers.addAll(serializers);
 assertThrows(NullPointerException.class, () -> MAPPER.writeValueAsString((Object) null, serializers.get(Integer.class)));
 }

 @Test
 void testIntegerSerializerWrongType() {
 Long number = 123L;
 Map<String, JsonSerializer<?>> serializers = new java.util.HashMap<>();
 NumberSerializers.addAll(serializers);
 assertThrows(IllegalArgumentException.class, () -> MAPPER.writeValueAsString(number, serializers.get(Integer.class)));
 }

 @Test
 void testFloatSerializer() throws IOException, JsonProcessingException {
 Float number = 123.45f;
 Map<String, JsonSerializer<?>> serializers = new java.util.HashMap<>();
 NumberSerializers.addAll(serializers);
 String json = MAPPER.writeValueAsString(number, serializers.get(Float.class));
 assertEquals("123.45", json);
 }

 @Test
 void testFloatSerializerZero() throws IOException, JsonProcessingException {
 Float number = 0.0f;
 Map<String, JsonSerializer<?>> serializers = new java.util.HashMap<>();
 NumberSerializers.addAll(serializers);
 String json = MAPPER.writeValueAsString(number, serializers.get(Float.class));
 assertEquals("0.0", json);
 }

 @Test
 void testFloatSerializerNaN() throws IOException, JsonProcessingException {
 Float number = Float.NaN;
 Map<String, JsonSerializer<?>> serializers = new java.util.HashMap<>();
 NumberSerializers.addAll(serializers);
 String json = MAPPER.writeValueAsString(number, serializers.get(Float.class));
 assertEquals("NaN", json);
 }

 @Test
 void testFloatSerializerNegativeInfinity() throws IOException, JsonProcessingException {
 Float number = Float.NEGATIVE_INFINITY;
 Map<String, JsonSerializer<?>> serializers = new java.util.HashMap<>();
 NumberSerializers.addAll(serializers);
 String json = MAPPER.writeValueAsString(number, serializers.get(Float.class));
 assertEquals("-Infinity", json);
 }

 @Test
 void testFloatSerializerPositiveInfinity() throws IOException, JsonProcessingException {
 Float number = Float.POSITIVE_INFINITY;
 Map<String, JsonSerializer<?>> serializers = new java.util.HashMap<>();
 NumberSerializers.addAll(serializers);
 String json = MAPPER.writeValueAsString(number, serializers.get(Float.class));
 assertEquals("Infinity", json);
 }

 @Test
 void testFloatSerializerNull() {
 Map<String, JsonSerializer<?>> serializers = new java.util.HashMap<>();
 NumberSerializers.addAll(serializers);
 assertThrows(NullPointerException.class, () -> MAPPER.writeValueAsString((Object) null, serializers.get(Float.class)));
 }

 @Test
 void testFloatSerializerWrongType() {
 Double number = 123.45;
 Map<String, JsonSerializer<?>> serializers = new java.util.HashMap<>();
 NumberSerializers.addAll(serializers);
 assertThrows(IllegalArgumentException.class, () -> MAPPER.writeValueAsString(number, serializers.get(Float.class)));
 }

 @Test
 void testDoubleSerializer() throws IOException, JsonProcessingException {
 Double number = 123.45;
 Map<String, JsonSerializer<?>> serializers = new java.util.HashMap<>();
 NumberSerializers.addAll(serializers);
 String json = MAPPER.writeValueAsString(number, serializers.get(Double.class));
 assertEquals("123.45", json);
 }

 @Test
 void testDoubleSerializerZero() throws IOException, JsonProcessingException {
 Double number = 0.0;
 Map<String, JsonSerializer<?>> serializers = new java.util.HashMap<>();
 NumberSerializers.addAll(serializers);
 String json = MAPPER.writeValueAsString(number, serializers.get(Double.class));
 assertEquals("0.0", json);
 }

 @Test
 void testDoubleSerializerNaN() throws IOException, JsonProcessingException {
 Double number = Double.NaN;
 Map<String, JsonSerializer<?>> serializers = new java.util.HashMap<>();
 NumberSerializers.addAll(serializers);
 String json = MAPPER.writeValueAsString(number, serializers.get(Double.class));
 assertEquals("NaN", json);
 }

 @Test
 void testDoubleSerializerNegativeInfinity() throws IOException, JsonProcessingException {
 Double number = Double.NEGATIVE_INFINITY;
 Map<String, JsonSerializer<?>> serializers = new java.util.HashMap<>();
 NumberSerializers.addAll(serializers);
 String json = MAPPER.writeValueAsString(number, serializers.get(Double.class));
 assertEquals("-Infinity", json);
 }

 @Test
 void testDoubleSerializerPositiveInfinity() throws IOException, JsonProcessingException {
 Double number = Double.POSITIVE_INFINITY;
 Map<String, JsonSerializer<?>> serializers = new java.util.HashMap<>();
 NumberSerializers.addAll(serializers);
 String json = MAPPER.writeValueAsString(number, serializers.get(Double.class));
 assertEquals("Infinity", json);
 }

 @Test
 void testDoubleSerializerNull() {
 Map<String, JsonSerializer<?>> serializers = new java.util.HashMap<>();
 NumberSerializers.addAll(serializers);
 assertThrows(NullPointerException.class, () -> MAPPER.writeValueAsString((Object) null, serializers.get(Double.class)));
 }

 @Test
 void testDoubleSerializerWrongType() {
 Long number = 123L;
 Map<String, JsonSerializer<?>> serializers = new java.util.HashMap<>();
 NumberSerializers.addAll(serializers);
 assertThrows(IllegalArgumentException.class, () -> MAPPER.writeValueAsString(number, serializers.get(Double.class)));
 }

 @Test
 void testLongSerializer() throws IOException, JsonProcessingException {
 Long number = 123L;
 Map<String, JsonSerializer<?>> serializers = new java.util.HashMap<>();
 NumberSerializers.addAll(serializers);
 String json = MAPPER.writeValueAsString(number, serializers.get(Long.class));
 assertEquals("123", json);
 }

 @Test
 void testLongSerializerZero() throws IOException, JsonProcessingException {
 Long number = 0L;
 Map<String, JsonSerializer<?>> serializers = new java.util.HashMap<>();
 NumberSerializers.addAll(serializers);
 String json = MAPPER.writeValueAsString(number, serializers.get(Long.class));
 assertEquals("0", json);
 }

 @Test
 void testLongSerializerMinValue() throws IOException, JsonProcessingException {
 Long number = Long.MIN_VALUE;
 Map<String, JsonSerializer<?>> serializers = new java.util.HashMap<>();
 NumberSerializers.addAll(serializers);
 String json = MAPPER.writeValueAsString(number, serializers.get(Long.class));
 assertEquals(String.valueOf(Long.MIN_VALUE), json);
 }

 @Test
 void testLongSerializerMaxValue() throws IOException, JsonProcessingException {
 Long number = Long.MAX_VALUE;
 Map<String, JsonSerializer<?>> serializers = new java.util.HashMap<>();
 NumberSerializers.addAll(serializers);
 String json = MAPPER.writeValueAsString(number, serializers.get(Long.class));
 assertEquals(String.valueOf(Long.MAX_VALUE), json);
 }

 @Test
 void testLongSerializerNull() {
 Map<String, JsonSerializer<?>> serializers = new java.util.HashMap<>();
 NumberSerializers.addAll(serializers);
 assertThrows(NullPointerException.class, () -> MAPPER.writeValueAsString((Object) null, serializers.get(Long.class)));
 }

 @Test
 void testLongSerializerWrongType() {
 Integer number = 123;
 Map<String, JsonSerializer<?>> serializers = new java.util.HashMap<>();
 NumberSerializers.addAll(serializers);
 assertThrows(IllegalArgumentException.class, () -> MAPPER.writeValueAsString(number, serializers.get(Long.class)));
 }
}