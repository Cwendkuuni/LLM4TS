import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 @Test
 public void testBooleanDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "true";
 Object actual = deserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertTrue(actual instanceof Boolean);
 assertEquals(Boolean.TRUE, actual);
 }

 @Test
 public void testBooleanDeserializationWithFalse() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "false";
 Object actual = deserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertTrue(actual instanceof Boolean);
 assertEquals(Boolean.FALSE, actual);
 }

 @Test
 public void testByteDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "127";
 Object actual = deserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertTrue(actual instanceof Byte);
 assertEquals((byte) 127, actual);
 }

 @Test
 public void testByteDeserializationOutOfRange() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "256";
 Object actual = deserializer.deserializeKey(key, mapper.getDeserializationContext());
 fail("Expected Jackson to throw an IOException for out-of-range byte value");
 }

 @Test
 public void testShortDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "12345";
 Object actual = deserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertTrue(actual instanceof Short);
 assertEquals((short) 12345, actual);
 }

 @Test
 public void testShortDeserializationOutOfRange() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "32768";
 Object actual = deserializer.deserializeKey(key, mapper.getDeserializationContext());
 fail("Expected Jackson to throw an IOException for out-of-range short value");
 }

 @Test
 public void testCharDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "A";
 Object actual = deserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertTrue(actual instanceof Character);
 assertEquals('A', actual);
 }

 @Test
 public void testCharDeserializationInvalidLength() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "AB";
 Object actual = deserializer.deserializeKey(key, mapper.getDeserializationContext());
 fail("Expected Jackson to throw an IllegalArgumentException for invalid length character key");
 }

 @Test
 public void testIntDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "123456";
 Object actual = deserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertTrue(actual instanceof Integer);
 assertEquals(123456, actual);
 }

 @Test
 public void testLongDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "12345678901234567890";
 Object actual = deserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertTrue(actual instanceof Long);
 assertEquals(12345678901234567890L, actual);
 }

 @Test
 public void testFloatDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "3.14159265";
 Object actual = deserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertTrue(actual instanceof Float);
 assertEquals(3.14159265f, actual, 0.00001f);
 }

 @Test
 public void testDoubleDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "3.14159265358979323846";
 Object actual = deserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertTrue(actual instanceof Double);
 assertEquals(3.14159265358979323846, actual, 0.0000000000000001);
 }

 // Test for Enum deserialization
 // ...

 @Test
 public void testDateDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "2022-12-31";
 Object actual = deserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertTrue(actual instanceof Date);
 }

 @Test
 public void testCalendarDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "2022-12-31";
 Object actual = deserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertTrue(actual instanceof Calendar);
 }

 @Test
 public void testUUIDDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "00000000-0000-1000-8000-00805f9b34fb";
 Object actual = deserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertTrue(actual instanceof UUID);
 assertEquals(new UUID("00000000-0000-1000-8000-00805f9b34fb"), actual);
 }

 @Test
 public void testURIDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "https://www.example.com/path/to/file.ext";
 Object actual = deserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertTrue(actual instanceof URI);
 }

 @Test
 public void testURLDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "https://www.example.com/path/to/file.ext";
 Object actual = deserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertTrue(actual instanceof URL);
 }

 @Test
 public void testClassDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String[].class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "[Ljava.lang.String;";
 Object actual = deserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertTrue(actual instanceof String[]);
 }

 @Test
 public void testInvalidClassDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String[].class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "java.util.Date";
 Object actual = deserializer.deserializeKey(key, mapper.getDeserializationContext());
 fail("Expected Jackson to throw an IOException for invalid class key");
 }
}