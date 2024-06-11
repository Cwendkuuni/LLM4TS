import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.tests.BaseMapTest;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@Test
public class StdKeySerializerTest extends BaseMapTest {

 @BeforeClass
 public static void setup() {
 JsonMapperFactory factory = new JsonMapperFactory();
 objectMapper = factory.getObjectMapper();
 }

 @Test
 public void testSerializeDate() throws JsonProcessingException {
 Date date = new Date();
 StdKeySerializer serializer = new StdKeySerializer();
 ObjectNode node = objectMapper.createObjectNode();
 node.put("key", "value");
 node.put("date", date);
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerSerializer(Date.class, new StdKeySerializer());
 String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
 assertEquals(jsonString, "{\"key\":\"value\",\"date\":\"" + date.toString() + "\"}");
 }

 @Test
 public void testSerializeString() throws JsonProcessingException {
 ObjectNode node = objectMapper.createObjectNode();
 node.put("key", "value");
 StdKeySerializer serializer = new StdKeySerializer();
 String jsonString = objectMapper.writeValueAsString(node, serializer);
 assertEquals(jsonString, "{\"key\":\"value\"}");
 }

 @Test
 public void testSerializeNull() throws JsonProcessingException {
 ObjectNode node = objectMapper.createObjectNode();
 node.putNull("key");
 StdKeySerializer serializer = new StdKeySerializer();
 String jsonString = objectMapper.writeValueAsString(node, serializer);
 assertEquals(jsonString, "null");
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 StdKeySerializer serializer = new StdKeySerializer();
 ObjectMapper mapper = new ObjectMapper();
 assertEquals(serializer.getSchema(mapper.getSerializerProvider(), Object.class.getType()), objectMapper.createSchemaNode("string"));
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new ObjectMapper().getTypeFactory().constructType(ObjectNode.class));
 StdKeySerializer serializer = new StdKeySerializer();
 serializer.acceptJsonFormatVisitor(visitor, Object.class.getType());
 assertNotNull(visitor.getFormatForSerializable(String.class));
 assertNotNull(visitor.getFormatForSerializable(Date.class));
 }

 @Test
 public void testSerializeInvalidType() throws JsonProcessingException, JsonMappingException {
 ObjectNode node = objectMapper.createObjectNode();
 node.put("key", new Integer(1));
 StdKeySerializer serializer = new StdKeySerializer();
 try {
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerSerializer(Integer.class, serializer);
 mapper.writeValueAsString(node);
 fail();
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testSerializeEmptyMap() throws JsonProcessingException {
 Map<String, Object> map = new HashMap<>();
 StdKeySerializer serializer = new StdKeySerializer();
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerSerializer(Map.class, serializer);
 String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
 assertEquals(jsonString, "{}");
 }

 @Test
 public void testSerializeMapWithNullValues() throws JsonProcessingException {
 Map<String, Object> map = new HashMap<>();
 map.put("key1", null);
 StdKeySerializer serializer = new StdKeySerializer();
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerSerializer(Map.class, serializer);
 String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
 assertEquals(jsonString, "{\"key1\":null}");
 }

 @Test
 public void testSerializeMapWithDates() throws JsonProcessingException {
 Map<String, Object> map = new HashMap<>();
 map.put("key1", new Date());
 map.put("key2", new Date());
 StdKeySerializer serializer = new StdKeySerializer();
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerSerializer(Map.class, serializer);
 String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
 assertEquals(jsonString, "{\"key1\":\"" + new Date().toString() + "\",\"key2\":\"" + new Date().toString() + "\"}");
 }

 @Test
 public void testSerializeMapWithObjects() throws JsonProcessingException {
 Map<String, Object> map = new HashMap<>();
 map.put("key1", new Object());
 map.put("key2", new Object());
 StdKeySerializer serializer = new StdKeySerializer();
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerSerializer(Map.class, serializer);
 String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
 assertEquals(jsonString, "{\"key1\":\"[object Object]\",\"key2\":\"[object Object]\"}");
 }
}