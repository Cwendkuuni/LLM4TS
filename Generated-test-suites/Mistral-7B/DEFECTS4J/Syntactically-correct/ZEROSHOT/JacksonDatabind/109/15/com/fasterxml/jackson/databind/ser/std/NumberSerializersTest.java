import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.std.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 @Test
 public void testNumberSerializers() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();

 // Test integer serialization
 Integer integer = 123;
 IntSerializer intSerializer = (IntSerializer) mapper.findValueSerializers().findValueSerializer(Integer.class);
 assertEquals("123", intSerializer.serializeValue(integer, null, null));

 // Test short serialization
 Short shortValue = 127;
 ShortSerializer shortSerializer = (ShortSerializer) mapper.findValueSerializers().findValueSerializer(Short.class);
 assertEquals("127", shortSerializer.serializeValue(shortValue, null, null));

 // Test long serialization
 Long longValue = 9876543210L;
 LongSerializer longSerializer = (LongSerializer) mapper.findValueSerializers().findValueSerializer(Long.class);
 assertEquals("9876543210", longSerializer.serializeValue(longValue, null, null));

 // Test float serialization
 Float floatValue = 3.14f;
 FloatSerializer floatSerializer = (FloatSerializer) mapper.findValueSerializers().findValueSerializer(Float.class);
 assertEquals("3.14", floatSerializer.serializeValue(floatValue, null, null));

 // Test double serialization
 Double doubleValue = 1.732050805d;
 DoubleSerializer doubleSerializer = (DoubleSerializer) mapper.findValueSerializers().findValueSerializer(Double.class);
 assertEquals("1.732050805", doubleSerializer.serializeValue(doubleValue, null, null));

 // Test JSON format visitor
 JsonFactory factory = new JsonFactory();
 SimpleModule module = new SimpleModule();
 NumberSerializers.addAll(module.getDefaultSerializers());
 mapper = new ObjectMapper(factory);
 mapper.registerModule(module);

 ObjectWriter writer = mapper.writer();
 ObjectWriter writerWithFormat = mapper.writer().with(new JsonFormat.Feature.WriteIntAsString());

 JsonNode rootNode = mapper.createObjectNode();
 rootNode.put("int", 123);
 rootNode.put("short", 127);
 rootNode.put("long", 9876543210L);
 rootNode.put("float", 3.14f);
 rootNode.put("double", 1.732050805d);

 // Test integer serialization with JSON format
 String intJson = writer.writeValueAsString(rootNode);
 assertEquals("{\"int\":123,\"short\":127,\"long\":9876543210,\"float\":3.14,\"double\":1.732050805}", intJson);

 JsonNode intRootWithFormat = mapper.readTree(intJson);
 JsonNode intNodeWithFormat = intRootWithFormat.get("int");
 JsonNode intNodeWithoutFormat = intRootWithFormat.get("int");
 assertNull(intNodeWithFormat.get("@class"));
 assertEquals("123", intNodeWithFormat.asText());
 assertNull(intNodeWithoutFormat.get("@class"));
 assertEquals("123", intNodeWithoutFormat.asText());

 // Test short serialization with JSON format
 JsonNode shortNodeWithFormat = intRootWithFormat.get("short");
 JsonNode shortNodeWithoutFormat = intRootWithFormat.get("short");
 assertNull(shortNodeWithFormat.get("@class"));
 assertEquals("127", shortNodeWithFormat.asText());
 assertNull(shortNodeWithoutFormat.get("@class"));
 assertEquals("127", shortNodeWithoutFormat.asText());

 // Test long serialization with JSON format
 JsonNode longNodeWithFormat = intRootWithFormat.get("long");
 JsonNode longNodeWithoutFormat = intRootWithFormat.get("long");
 assertNull(longNodeWithFormat.get("@class"));
 assertEquals("9876543210", longNodeWithFormat.asText());
 assertNull(longNodeWithoutFormat.get("@class"));
 assertEquals("9876543210", longNodeWithoutFormat.asText());

 // Test float serialization with JSON format
 JsonNode floatNodeWithFormat = intRootWithFormat.get("float");
 JsonNode floatNodeWithoutFormat = intRootWithFormat.get("float");
 assertNull(floatNodeWithFormat.get("@class"));
 assertEquals("3.14", floatNodeWithFormat.asText());
 assertNull(floatNodeWithoutFormat.get("@class"));
 assertEquals("3.14", floatNodeWithoutFormat.asText());

 // Test double serialization with JSON format
 JsonNode doubleNodeWithFormat = intRootWithFormat.get("double");
 JsonNode doubleNodeWithoutFormat = intRootWithFormat.get("double");
 assertNull(doubleNodeWithFormat.get("@class"));
 assertEquals("1.732050805", doubleNodeWithFormat.asText());
 assertNull(doubleNodeWithoutFormat.get("@class"));
 assertEquals("1.732050805", doubleNodeWithoutFormat.asText());

 // Test serialization with JSON format using writer
 JsonNode rootNodeWithFormat = writerWithFormat.writeValueAsTree(rootNode);
 assertEquals("{\"int\":\"123\",\"short\":\"127\",\"long\":\"9876543210\",\"float\":\"3.14\",\"double\":\"1.732050805\"}", rootNodeWithFormat.asText());
 }
}