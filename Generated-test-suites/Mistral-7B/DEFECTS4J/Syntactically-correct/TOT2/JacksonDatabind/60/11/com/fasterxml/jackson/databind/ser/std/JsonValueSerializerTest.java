import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class JsonValueSerializerTest {

 @BeforeAll
 static void initialize() {
 ObjectMapper objectMapper = new ObjectMapper();
 }

 @Test
 void testSerializationOfSimpleValue() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonValueSerializer serializer = objectMapper.findValueSerializer(Integer.class, null);
 int value = 123;
 String result = objectMapper.writeValueAsString(value, new JsonSerializer.StdSerializer<Object>(Object.class) {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeNumber(value.intValue());
 }
 });
 assertTrue(serializer instanceof JsonValueSerializer);
 JsonValueSerializer jsonValueSerializer = (JsonValueSerializer) serializer;
 Object deserializedValue = jsonValueSerializer.getValue(new Object());
 assertEquals(value, deserializedValue);
 }

 @Test
 void testSerializationOfBooleanValue() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonValueSerializer serializer = objectMapper.findValueSerializer(Boolean.class, null);
 boolean value = true;
 String result = objectMapper.writeValueAsString(value, new JsonSerializer.StdSerializer<Object>(Object.class) {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeBoolean(value.booleanValue());
 }
 });
 assertTrue(serializer instanceof JsonValueSerializer);
 JsonValueSerializer jsonValueSerializer = (JsonValueSerializer) serializer;
 Object deserializedValue = jsonValueSerializer.getValue(new Object());
 assertEquals(value, deserializedValue);
 }

 @Test
 void testSerializationOfStringValue() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonValueSerializer serializer = objectMapper.findValueSerializer(String.class, null);
 String value = "Test";
 String result = objectMapper.writeValueAsString(value, new JsonSerializer.StdSerializer<Object>(Object.class) {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeString(value.toString());
 }
 });
 assertTrue(serializer instanceof JsonValueSerializer);
 JsonValueSerializer jsonValueSerializer = (JsonValueSerializer) serializer;
 Object deserializedValue = jsonValueSerializer.getValue(new Object());
 assertEquals(value, deserializedValue);
 }

 @Test
 void testSerializationOfEnumValue() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 enum TestEnum {
 ONE, TWO, THREE
 }
 JsonValueSerializer serializer = objectMapper.findValueSerializer(TestEnum.class, null);
 TestEnum value = TestEnum.ONE;
 String result = objectMapper.writeValueAsString(value, new JsonSerializer.StdSerializer<Object>(Object.class) {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeString(value.name());
 }
 });
 assertTrue(serializer instanceof JsonValueSerializer);
 JsonValueSerializer jsonValueSerializer = (JsonValueSerializer) serializer;
 Object deserializedValue = jsonValueSerializer.getValue(new Object());
 assertEquals(value, deserializedValue);
 }

 @Test
 void testSerializationOfCustomClassValue() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 static class TestClass {
 @JsonValue
 public int getValue() {
 return 123;
 }
 }
 JsonValueSerializer serializer = objectMapper.findValueSerializer(TestClass.class, null);
 TestClass value = new TestClass();
 String result = objectMapper.writeValueAsString(value, new JsonSerializer.StdSerializer<Object>(Object.class) {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeNumber(value.hashCode());
 }
 });
 assertTrue(serializer instanceof JsonValueSerializer);
 JsonValueSerializer jsonValueSerializer = (JsonValueSerializer) serializer;
 Object deserializedValue = jsonValueSerializer.getValue(new Object());
 assertTrue(deserializedValue instanceof TestClass);
 assertEquals(value.getValue(), deserializedValue.getValue());
 }

 @Test
 void testSerializationOfNullValue() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonValueSerializer serializer = objectMapper.findValueSerializer(Integer.class, null);
 Object deserializedValue = serializer.getValue(null);
 assertNull(deserializedValue);
 }

 @Test
 void testSerializationThrowsExceptionWhenValueIsNull() throws JsonMappingException, IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonValueSerializer serializer = objectMapper.findValueSerializer(Integer.class, null);
 assertThrows(JsonMappingException.class, () -> serializer.serialize(null, null, null));
 }
}