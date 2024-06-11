import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

 private static ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testFindUUIDDeserializer() {
 UUID expected = UUID.fromString("123e4567-e89b-12d3-a456-426655440000");
 Object result = JdkDeserializers.find(UUID.class, UUID.class.getName());
 JsonDeserializer<UUID> deserializer = (JsonDeserializer<UUID>) result;
 JsonNode node = new ObjectNode(JSON_FACTORY).put("value", "123e4567-e89b-12d3-a456-426655440000");
 UUID actual = deserializer.deserialize(node, UUID.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testFindNullDeserializerForUnknownClass() {
 Class<?> unknownClass = new Object().getClass();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(unknownClass, unknownClass.getName());
 assertNull(deserializer);
 }

 @Test
 public void testFindStackTraceElementDeserializer() {
 StackTraceElement expected = new StackTraceElement("JdkDeserializersTest.testFindStackTraceElementDeserializer", "testFindStackTraceElementDeserializer", "JdkDeserializersTest.java", 54);
 Object result = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
 JsonDeserializer<StackTraceElement> deserializer = (JsonDeserializer<StackTraceElement>) result;
 JsonNode node = new ObjectNode(JSON_FACTORY).put("className", "JdkDeserializersTest").put("methodName", "testFindStackTraceElementDeserializer")
 .put("file", "JdkDeserializersTest.java").put("lineNumber", 54);
 StackTraceElement actual = deserializer.deserialize(node, StackTraceElement.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testFindAtomicBooleanDeserializer() {
 AtomicBoolean expected = new AtomicBoolean(true);
 ObjectNode node = new ObjectNode(JSON_FACTORY).put("value", true);
 JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
 AtomicBoolean actual = deserializer.deserialize(node, AtomicBoolean.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testFindByteBufferDeserializer() {
 ByteBuffer expected = ByteBuffer.wrap(new byte[] { 1, 2, 3 });
 ObjectNode node = new ObjectNode(JSON_FACTORY).put("byteValue", "1, 2, 3");
 JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
 ByteBuffer actual = deserializer.deserialize(node, new TypeReference<ByteBuffer>() {});
 assertEquals(expected, actual);
 }

 @Test
 public void testFindFromStringDeserializerForUnknownClass() {
 Class<?> unknownClass = new Object().getClass();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(unknownClass, unknownClass.getName());
 assertNotNull(deserializer); // FromStringDeserializer is a default deserializer for unknown classes
 }

 @Test
 public void testFindDeserializerForPrimitives() {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(int.class, "java.lang.Integer");
 assertTrue(deserializer instanceof FromStringDeserializer.NumberDeserializer);
 }

}