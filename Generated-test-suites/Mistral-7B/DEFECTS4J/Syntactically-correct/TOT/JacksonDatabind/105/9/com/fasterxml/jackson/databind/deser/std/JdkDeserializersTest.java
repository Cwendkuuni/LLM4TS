import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testFind_UUID() {
 String json = "\"some-uuid\"";
 Object result = JdkDeserializers.find(Object.class, UUID.class.getName())
 .deserialize(objectMapper, new ByteArrayInputStream(json.getBytes()), null);
 UUID expected = UUID.fromString("some-uuid");
 assertEquals(expected, result);
 }

 @Test
 public void testFind_AtomicBoolean_true() {
 String json = "true";
 Object result = JdkDeserializers.find(Object.class, AtomicBoolean.class.getName())
 .deserialize(objectMapper, new ByteArrayInputStream(json.getBytes()), null);
 AtomicBoolean expected = new AtomicBoolean(true);
 assertEquals(expected, result);
 }

 @Test
 public void testFind_AtomicBoolean_false() {
 String json = "false";
 Object result = JdkDeserializers.find(Object.class, AtomicBoolean.class.getName())
 .deserialize(objectMapper, new ByteArrayInputStream(json.getBytes()), null);
 AtomicBoolean expected = new AtomicBoolean(false);
 assertEquals(expected, result);
 }

 @Test
 public void testFind_StackTraceElement() {
 String json = "{\"className\": \"MyClass\", \"methodName\": \"myMethod\", \"file\": \"MyFile.java\", \"lineNumber\": 123, \"columnNumber\": 45}";
 Object result = JdkDeserializers.find(Object.class, StackTraceElement.class.getName())
 .deserialize(objectMapper, new ByteArrayInputStream(json.getBytes()), null);
 StackTraceElement expected = new StackTraceElement("MyClass", "myMethod", "MyFile.java", 123, 45);
 assertEquals(expected, result);
 }

 @Test
 public void testFind_ByteBuffer() {
 byte[] data = new byte[] { 1, 2, 3, 4, 5 };
 ByteBuffer byteBuffer = ByteBuffer.wrap(data);
 String json = new ObjectMapper().writeValueAsString(byteBuffer);
 Object result = JdkDeserializers.find(Object.class, ByteBuffer.class.getName())
 .deserialize(objectMapper, new ByteArrayInputStream(json.getBytes()), null);
 assertEquals(byteBuffer, result);
 }

 @Test
 public void testFind_UnknownClass() {
 String json = "{}";
 Object result = JdkDeserializers.find(Object.class, "com.unknown.UnknownClass".getClass().getName())
 .deserialize(objectMapper, new ByteArrayInputStream(json.getBytes()), null);
 assertNull(result);
 }
}