import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private SerializationConfig config;
 private BeanDescription beanDesc;
 private PropertyBuilder propertyBuilder;

 @Before
 public void setup() {
 ObjectNode node = ObjectMapperBuilder.json().buildValueTreeFactory().objectNode();
 node.put("name", "John Doe");
 node.put("age", 30);
 node.put("isMarried", false);
 ObjectMapper mapper = ObjectMapperBuilder.json().build();
 try {
 config = mapper.getSerializationConfig();
 beanDesc = mapper.findBeanDescription(Object.class, null);
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 } catch (JsonMappingException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testBuildWriter_DefaultProperties() throws JsonMappingException {
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, Object.class.getType(), null, null, null, null, false);
 assertNotNull(writer);
 }

 @Test
 public void testBuildWriter_NonDefaultProperties() throws JsonMappingException {
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, Object.class.getType(), null, null, null, null, false);
 Object defaultBean = propertyBuilder.getDefaultBean();
 Object nameValue = defaultBean != null ? propertyBuilder.getPropertyDefaultValue("name", null, Object.class) : null;
 Object ageValue = propertyBuilder.getDefaultValue(Object.class.getType());
 Object isMarriedValue = JsonInclude.Include.NON_DEFAULT;

 writer.setDefaultValue(nameValue);
 writer.setInclude(JsonInclude.Include.NON_DEFAULT);
 writer.setDefaultSerializer(null);
 writer.setNameTransformer(null);

 ObjectNode node = mapper.createObjectNode();
 node.set("name", "Jane Doe");
 node.set("age", 35);
 node.set("isMarried", true);
 writer.writeValueAsString(node);

 ObjectNode result = mapper.readValue(writer.getValueAsString(), ObjectNode.class);
 assertEquals("Jane Doe", result.get("name").asText());
 assertEquals(35, result.get("age").asInt());
 assertTrue(result.get("isMarried").asBoolean());
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildWriter_NullContentTypeSerializer() throws JsonMappingException {
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, Object.class.getType(), null, null, null, null, false);
 TypeSerializer contentTypeSer = null;
 BeanPropertyWriter newWriter = propertyBuilder.buildWriter(null, null, Object.class.getType(), null, null, null, null, false);
 newWriter = newWriter.copy();
 newWriter.setContentTypeSerializer(contentTypeSer);
 fail("Expected IllegalStateException due to null contentTypeSerializer");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildWriter_InvalidContentTypeSerializer() throws JsonMappingException {
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, Object.class.getType(), null, null, null, null, false);
 TypeSerializer contentTypeSer = ObjectMapperBuilder.json().getTypeFactory().constructType(int[].class);
 BeanPropertyWriter newWriter = propertyBuilder.buildWriter(null, null, Object.class.getType(), null, null, null, null, false);
 newWriter = newWriter.copy();
 newWriter.setContentTypeSerializer(contentTypeSer);
 fail("Expected IllegalArgumentException due to invalid contentTypeSerializer");
 }

 @Test
 public void testBuildWriter_SuppressNulls() throws JsonMappingException {
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, Object.class.getType(), null, null, null, null, true);
 ObjectNode node = mapper.createObjectNode();
 node.set("name", null);
 node.set("age", null);
 node.set("isMarried", null);
 writer.writeValueAsString(node);

 ObjectNode result = mapper.readValue(writer.getValueAsString(), ObjectNode.class);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testBuildWriter_SupprressEmptyCollections() throws JsonMappingException {
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, java.util.ArrayList.class, null, null, null, null, false);
 ArrayList<String> list = new ArrayList<>();
 writer.setDefaultValue(list);
 ObjectNode node = mapper.createObjectNode();
 node.set("items", list);
 writer.writeValueAsString(node);

 ObjectNode result = mapper.readValue(writer.getValueAsString(), ObjectNode.class);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testBuildWriter_SerializingNullValue() throws JsonMappingException {
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, java.lang.String.class, null, null, null, null, false);
 writer.setSerializer(null);
 ObjectNode node = mapper.createObjectNode();
 node.set("name", null);
 writer.writeValueAsString(node);

 ObjectNode result = mapper.readValue(writer.getValueAsString(), ObjectNode.class);
 assertNull(result.get("name"));
 }

 @Test
 public void testBuildWriter_SerializingEmptyStringValue() throws JsonMappingException {
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, java.lang.String.class, null, null, null, null, false);
 ObjectNode node = mapper.createObjectNode();
 node.set("name", "");
 writer.writeValueAsString(node);

 ObjectNode result = mapper.readValue(writer.getValueAsString(), ObjectNode.class);
 assertEquals("", result.get("name").asText());
 }

 @Test
 public void testBuildWriter_SerializingEmptyCollectionValue() throws JsonMappingException {
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, java.util.ArrayList.class, null, null, null, null, false);
 ArrayList<String> list = new ArrayList<>();
 ObjectNode node = mapper.createObjectNode();
 node.set("items", list);
 writer.writeValueAsString(node);

 ObjectNode result = mapper.readValue(writer.getValueAsString(), ObjectNode.class);
 assertEquals(0, result.size());
 }

 @Test
 public void testBuildWriter_SerializingEmptyMapValue() throws JsonMappingException {
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, java.util.LinkedHashMap.class, null, null, null, null, false);
 ObjectNode node = mapper.createObjectNode();
 ObjectNode mapNode = node.putObject("map");
 mapNode.putNull("key1");
 mapNode.putNull("key2");
 writer.writeValueAsString(node);

 ObjectNode result = mapper.readValue(writer.getValueAsString(), ObjectNode.class);
 assertEquals(0, result.size());
 }

 @Test
 public void testBuildWriter_SerializingEmptyArrayValue() throws JsonMappingException {
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, int[].class, null, null, null, null, false);
 ObjectNode node = mapper.createObjectNode();
 ObjectNode arrayNode = node.putArray("array");
 writer.writeValueAsString(node);

 ObjectNode result = mapper.readValue(writer.getValueAsString(), ObjectNode.class);
 assertEquals(0, result.size());
 }

 @Test
 public void testBuildWriter_SerializingEmptyObjectValue() throws JsonMappingException {
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, MyEmptyClass.class, null, null, null, null, false);
 ObjectNode node = mapper.createObjectNode();
 writer.writeValueAsString(node);

 ObjectNode result = mapper.readValue(writer.getValueAsString(), ObjectNode.class);
 assertEquals(0, result.size());
 }

 public static class MyEmptyClass {
 }
}