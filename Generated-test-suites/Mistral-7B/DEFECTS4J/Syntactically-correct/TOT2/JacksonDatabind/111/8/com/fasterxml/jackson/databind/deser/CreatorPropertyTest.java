import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public class CreatorPropertyTest {

 private CreatorProperty property;
 private ObjectMapper mapper;

 @Before
 public void setup() throws JsonProcessingException {
 mapper = new ObjectMapper();
 property = new CreatorProperty(PropertyName.construct("testProperty"),
 JavaType.construct(String.class), null, null, null, null, -1, null, null);
 }

 @Test
 public void testBasicFunctionality() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode node = mapper.createObjectNode();
 node.put("testProperty", "Hello World");
 Object result = mapper.readValue(node, Object.class);
 assertEquals("Hello World", property.get(result));
 }

 @Test
 public void testIgnoreProperty() throws IOException {
 property.markAsIgnorable();
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode node = mapper.createObjectNode();
 Object result = mapper.readValue(node, Object.class);
 assertNull(property.get(result));
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingSetter() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 property.setFallbackSetter(null);
 ObjectNode node = mapper.createObjectNode();
 node.put("testProperty", "Hello World");
 Object result = mapper.readValue(node, Object.class);
 property.inject(mapper.getDeserializationContext(), result);
 }

 @Test(expected = IOException.class)
 public void testInvalidJson() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode node = mapper.createObjectNode();
 node.put("testProperty", null);
 Object result = mapper.readValue(node, Object.class);
 try {
 property.inject(mapper.getDeserializationContext(), result);
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test(expected = InvalidDefinitionException.class)
 public void testMissingInjectableValueId() throws IOException {
 property.markAsIgnorable();
 property._injectableValueId = null;
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode node = mapper.createObjectNode();
 Object result = mapper.readValue(node, Object.class);
 property.inject(mapper.getDeserializationContext(), result);
 }

 @Test
 public void testFindInjectableValue() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 PropertyName idName = PropertyName.construct("id");
 ObjectNode node = mapper.createObjectNode();
 node.put("id", 1);
 node.put("testProperty", "Hello World");
 Object result = mapper.readerForUpdating(Object.class).readValue(node);
 assertEquals(1, property.findInjectableValue(mapper.getDeserializationContext(), result).hashCode());
 }
}