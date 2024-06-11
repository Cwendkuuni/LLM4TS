import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testFind_UUID() throws Exception {
 UUID expected = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
 JsonDeserializer<UUID> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
 Object result = mapper.readValue("\"123e4567-e89b-12d3-a456-426614174000\"", new TypeReference<UUID>() {});
 assertEquals(deserializer.getClass(), JdkDeserializers.UUIDDeserializer.class);
 assertEquals(expected, result);
 }

 @Test
 public void testFind_AtomicBoolean() throws Exception {
 AtomicBoolean expected = new AtomicBoolean(true);
 String jsonString = "true";
 JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
 Object result = mapper.readValue(jsonString, new TypeReference<AtomicBoolean>() {});
 assertEquals(deserializer.getClass(), JdkDeserializers.AtomicBooleanDeserializer.class);
 assertEquals(expected, result);
 }

 @Test
 public void testFind_StackTraceElement() throws Exception {
 StackTraceElement expected = new StackTraceElement("testClass", "testMethod", "testFile:123", 1);
 String jsonString = "{\"className\":\"testClass\", \"methodName\":\"testMethod\", \"file\":\"testFile:123\", \"line\":1}";
 JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
 Object result = mapper.readValue(jsonString, new TypeReference<StackTraceElement>() {});
 assertEquals(deserializer.getClass(), JdkDeserializers.StackTraceElementDeserializer.class);
 assertEquals(expected, result);
 }

 @Test
 public void testFind_ByteBuffer() throws Exception {
 byte[] expectedArray = {1, 2, 3, 4};
 ByteBuffer expected = ByteBuffer.wrap(expectedArray);
 String jsonString = "[\u0001,\u0002,\u0003,\u0004]";
 JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
 Object result = mapper.readValue(jsonString, new TypeReference<ByteBuffer>() {});
 assertEquals(deserializer.getClass(), JdkDeserializers.ByteBufferDeserializer.class);
 assertEquals(expected, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testFind_UnknownClass() throws Exception {
 String jsonString = "{\"className\":\"com.unknown.UnknownClass\"}";
 JsonDeserializer<Object> deserializer = JdkDeserializers.find(Object.class, "com.unknown.UnknownClass".getClass().getName());
 Object result = mapper.readValue(jsonString, new TypeReference<Object>() {});
 fail("Deserializer should return null for unknown classes");
 }

 @Test(expected = JsonProcessingException.class)
 public void testFind_InvalidJson() throws Exception {
 String jsonString = "\"invalidJson\"";
 JsonDeserializer<Object> deserializer = JdkDeserializers.find(Object.class, Object.class.getName());
 mapper.readValue(jsonString, new TypeReference<Object>() {});
 fail("Deserializer should throw an exception for invalid JSON");
 }
}