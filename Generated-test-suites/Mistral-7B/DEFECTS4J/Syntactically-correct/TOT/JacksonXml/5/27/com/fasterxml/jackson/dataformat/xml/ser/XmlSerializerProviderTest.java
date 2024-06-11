import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class XmlSerializerProviderTest {

 private XmlMapper mapper;
 private ObjectNode emptyObject;
 private ObjectNode jsonObject;

 @BeforeAll
 public static void setupClass() {
 mapper = new XmlMapper();
 }

 @BeforeEach
 public void setup() {
 emptyObject = ObjectMapperTestUtils.createEmptyObject();
 jsonObject = ObjectMapperTestUtils.createJsonObject();
 }

 @Test
 public void testSerializeNull() throws JsonProcessingException {
 JsonNode result = mapper.valueToTree(null, new XmlSerializerProvider(mapper.getRootValueSerializer()));
 assertEquals("null", result.get("name").asText());
 }

 @Test
 public void testSerializeEmptyObject() throws JsonProcessingException {
 JsonNode result = mapper.valueToTree(emptyObject, new XmlSerializerProvider(mapper.getRootValueSerializer()));
 ObjectNode root = (ObjectNode) result;
 assertEquals("", root.get("name").asText());
 assertEquals(0, root.size());
 }

 @Test
 public void testSerializeJsonObject() throws JsonProcessingException {
 JsonNode result = mapper.valueToTree(jsonObject, new XmlSerializerProvider(mapper.getRootValueSerializer()));
 ObjectNode root = (ObjectNode) result;
 assertEquals("root", root.get("name").asText());
 assertEquals(4, root.size());
 }

 @Test
 public void testSerializeJsonObjectWithCustomRootName() throws JsonProcessingException {
 SerializationConfig config = mapper.getSerializationConfig();
 config.setRootValueSerializer(mapper.getSerializerProvider().getSerializer(String.class));
 ObjectNode root = mapper.valueToTree(jsonObject, new XmlSerializerProvider(config));
 assertEquals("root", root.get("name").asText());
 }

 @Test
 public void testSerializeJsonObjectWithArrayRootElement() throws JsonProcessingException {
 SerializationConfig config = mapper.getSerializationConfig();
 ObjectNode arrayRoot = ObjectMapperTestUtils.createArrayNode("item");
 config.setRootValueSerializer(mapper.getSerializerProvider().getSerializer(ArrayNode.class));
 ObjectNode result = mapper.valueToTree(jsonObject, new XmlSerializerProvider(config, mapper.getSerializerProvider(), null));
 ObjectNode root = (ObjectNode) result;
 assertEquals("item", root.get("name").asText());
 assertEquals(1, root.size());
 JsonNode items = root.get("item");
 assertEquals(4, items.size());
 }

 @AfterEach
 public void teardown() {
 mapper = null;
 emptyObject = null;
 jsonObject = null;
 }

}