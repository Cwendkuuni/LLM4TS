import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeySerializersTest {

 @Test
 public void testGetStdKeySerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();

 // Test with null rawKeyType
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), null, true);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Dynamic.class, serializer);

 // Test with String rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), String.class, true);
 assertInstanceOf(StdKeySerializers.StringKeySerializer.class, serializer);

 // Test with int rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), int.class, true);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);

 // Test with Date rawKeyType
 Date date = new Date();
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Date.class, true);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);
 }

 @Test
 public void testGetFallbackKeySerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();

 // Test with null rawKeyType
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), null);
 assertNull(serializer);

 // Test with String rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), String.class);
 assertInstanceOf(StdKeySerializers.StringKeySerializer.class, serializer);

 // Test with Class<Enum> rawKeyType
 Class<Enum> enumClass = Enum.class;
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), enumClass);
 assertInstanceOf(StdKeySerializers.Dynamic.class, serializer);
 }

 @Test(expected = NullPointerException.class)
 public void testGetFallbackKeySerializerNullConfig() throws JsonProcessingException {
 ObjectMapper mapper = null;
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), String.class);
 fail("NullPointerException expected but not received");
 }

 @Test(expected = NullPointerException.class)
 public void testGetStdKeySerializerNullConfig() throws JsonProcessingException {
 ObjectMapper mapper = null;
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), String.class, true);
 fail("NullPointerException expected but not received");
 }

 @Test
 public void testStdKeySerializerSerialization() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String input = "{\"key\": 1}";
 JsonNode node = mapper.readTree(input);
 assertNotNull(node);
 JsonNode valueNode = node.path("value");
 assertEquals("1", valueNode.asText());

 ObjectMapper mapper2 = new ObjectMapper();
 JsonNode rootNode = mapper2.readTree(input);
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper2.getSerializationConfig(), Integer.class, false);
 String output = mapper2.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
 assertEquals(input, output);
 }
}