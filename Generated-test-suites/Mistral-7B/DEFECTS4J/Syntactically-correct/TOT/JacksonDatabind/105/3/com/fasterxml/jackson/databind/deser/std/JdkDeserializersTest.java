import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.JdkDeserializers;
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
 public void testFindForUUID() throws JsonProcessingException {
 UUID expected = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
 JsonDeserializer<UUID> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
 UUID actual = objectMapper.readValue(new ObjectMapper().writeValueAsString(expected), deserializer.getType());
 assertEquals(expected, actual);
 }

 @Test
 public void testFindForAtomicBoolean() {
 AtomicBoolean expected = new AtomicBoolean(true);
 JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
 AtomicBoolean actual = objectMapper.readValue(new ObjectMapper().writeValueAsString(true), deserializer.getType());
 assertEquals(expected, actual);
 }

 @Test
 public void testFindForStackTraceElement() throws JsonProcessingException {
 String className = "JdkDeserializersTest";
 String methodName = "testFindForStackTraceElement";
 StackTraceElement expected = new StackTraceElement("JdkDeserializersTest.java", methodName, className, 1);
 JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
 StackTraceElement actual = objectMapper.readValue(new ObjectMapper().writeValueAsString(expected), deserializer.getType());
 assertEquals(expected, actual);
 }

 @Test
 public void testFindForByteBuffer() throws JsonProcessingException {
 byte[] byteArray = { 0x01, 0x02, 0x03, 0x04, 0x05 };
 ByteBuffer expected = ByteBuffer.wrap(byteArray);
 JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
 ByteBuffer actual = objectMapper.readValue(new ObjectMapper().writeValueAsString(expected), deserializer.getType());
 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForUnknownClass() throws JsonProcessingException {
 JsonDeserializer<String> deserializer = JdkDeserializers.find(String.class, "UnknownClass.class.getName()");
 objectMapper.readValue(new ObjectMapper().writeValueAsString("Test string"), deserializer.getType());
 }

 @Test
 public void testFindForNullClass() {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(null, null);
 assertNull(deserializer);
 }

 @Test
 public void testFindForNullClassName() {
 Class<?> rawType = String.class;
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, null);
 assertNull(deserializer);
 }
}