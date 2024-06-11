import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonDeserializer.BaseDeserializer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testFindUuidDeserializer() throws JsonMappingException {
 Class<UUID> rawType = UUID.class;
 String clsName = UUID.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertNotNull(deserializer);
 assertTrue(deserializer instanceof UUIDDeserializer);
 }

 @Test
 public void testFindFromStringDeserializer() throws JsonMappingException {
 Class<String> rawType = String.class;
 String clsName = FromStringDeserializer.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertNull(deserializer);
 }

 @Test
 public void testFindNotNullClassDeserializer() throws JsonMappingException {
 Class<Object> rawType = Object.class;
 String clsName = "java.util.Date.class"; // a random non-existent class name
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertNull(deserializer);
 }

 @Test
 public void testFindUUIDDeserializerWithInvalidJson() throws JsonMappingException {
 Class<UUID> rawType = UUID.class;
 String json = "{\"invalid\": \"json\"}";
 objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 JsonNode node = objectMapper.readTree(json);
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, null);
 assertNull(deserializer);
 }

 @Test
 public void testFindAtomicBooleanDeserializer() throws JsonMappingException {
 Class<AtomicBoolean> rawType = AtomicBoolean.class;
 String clsName = AtomicBoolean.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertNotNull(deserializer);
 assertTrue(deserializer instanceof AtomicBooleanDeserializer);
 }

 @Test
 public void testFindByteBufferDeserializer() throws JsonMappingException {
 Class<ByteBuffer> rawType = ByteBuffer.class;
 String clsName = ByteBuffer.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertNotNull(deserializer);
 assertTrue(deserializer instanceof ByteBufferDeserializer);
 }

 @Test
 public void testFindStackTraceElementDeserializer() throws JsonMappingException {
 Class<StackTraceElement> rawType = StackTraceElement.class;
 String clsName = StackTraceElement.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertNotNull(deserializer);
 assertTrue(deserializer instanceof StackTraceElementDeserializer);
 }

 @Test
 public void testFindNullDeserializer() throws JsonMappingException {
 Class<String> rawType = String.class;
 String clsName = null;
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertNull(deserializer);
 }

 @Test
 public void testFindDeserializerWithUnregisteredClass() throws JsonMappingException {
 Class<MyCustomClass> rawType = MyCustomClass.class;
 String clsName = MyCustomClass.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertNull(deserializer);
 }

 @Test
 public void testFindDeserializerWithArrayType() throws JsonMappingException {
 Class<UUID[]> rawType = UUID[].class;
 String clsName = UUID[].class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertNull(deserializer);
 }

 @Test
 public void testFindDeserializerWithListType() throws JsonMappingException {
 TypeReference<List<UUID>> typeRef = new TypeReference<List<UUID>>() {};
 String clsName = List.class.getName() + "<" + UUID.class.getName() + ">";
 JsonDeserializer<?> deserializer = JdkDeserializers.find(typeRef.getType(), clsName);
 assertNull(deserializer);
 }
}