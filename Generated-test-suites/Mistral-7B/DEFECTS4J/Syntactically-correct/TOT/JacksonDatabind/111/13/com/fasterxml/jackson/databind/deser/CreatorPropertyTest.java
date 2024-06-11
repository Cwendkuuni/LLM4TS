import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CreatorPropertyTest {

 private ObjectNode rootNode;
 private ObjectMapper objectMapper;
 private CreatorProperty creatorProperty;

 @Before
 public void setup() throws JsonProcessingException {
 ObjectNode objNode = ObjectMapperFactory.getJsonObjectMapper().createObjectNode();
 rootNode = ObjectMapperFactory.getJsonObjectMapper().readValue(new ObjectMapper().writeValueAsString(objNode), new TypeReference<ObjectNode>() {});
 objectMapper = ObjectMapperFactory.getJsonObjectMapper();
 creatorProperty = new CreatorProperty(PropertyName.constructor, new BasicType(Object.class), null, null, null, null, 0, null, PropertyMetadata.empty());
 }

 @Test
 public void testDefaultConstructor() throws JsonProcessingException {
 ObjectNode actual = objectMapper.readTree(objectMapper.writeValueAsString(rootNode));
 actual.set("creatorProperty", null);
 ObjectNode input = objectMapper.readTree(objectMapper.writeValueAsString(new ObjectNode(ObjectMapperFactory.getJsonObjectMapper().createObjectNode().set("creatorProperty", null))));
 ObjectNode expected = objectMapper.readTree(objectMapper.writeValueAsString(new ObjectNode(ObjectMapperFactory.getJsonObjectMapper().createObjectNode().set("creatorProperty", null))));
 assertEquals(expected, actual);
 }

 @Test
 public void testConstructorWithInjectableValue() throws JsonProcessingException {
 ObjectNode actual = objectMapper.readTree(objectMapper.writeValueAsString(rootNode));
 actual.set("creatorProperty", createInjectableNode());
 ObjectNode input = objectMapper.readTree(objectMapper.writeValueAsString(new ObjectNode(ObjectMapperFactory.getJsonObjectMapper().createObjectNode().set("creatorProperty", createInjectableNode()))));
 ObjectNode expected = input;
 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingInjectableValue() throws JsonProcessingException {
 ObjectNode actual = objectMapper.readTree(objectMapper.writeValueAsString(rootNode));
 actual.remove("creatorProperty");
 ObjectNode input = objectMapper.readTree(objectMapper.writeValueAsString(new ObjectNode(ObjectMapperFactory.getJsonObjectMapper().createObjectNode().set("creatorProperty", null))));
 creatorProperty.findInjectableValue(null, null);
 }

 @Test(expected = IOException.class)
 public void testMissingSetter() throws JsonProcessingException {
 ObjectNode actual = objectMapper.readTree(objectMapper.writeValueAsString(rootNode));
 actual.set("creatorProperty", new ObjectNode(ObjectMapperFactory.getJsonObjectMapper().createObjectNode().set("value", createInjectableNode())));
 creatorProperty = new CreatorProperty(PropertyName.constructor, new BasicType(Object.class), null, null, null, null, 0, null, PropertyMetadata.empty());
 creatorProperty.inject(null, null);
 }

 private ObjectNode createInjectableNode() {
 return ObjectMapperFactory.getJsonObjectMapper().createObjectNode();
 }

}