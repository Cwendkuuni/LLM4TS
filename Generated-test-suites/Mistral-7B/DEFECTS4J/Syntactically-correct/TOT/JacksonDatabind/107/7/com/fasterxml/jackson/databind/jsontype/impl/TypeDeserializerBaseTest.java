import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testTypeDeserializerBase() throws IOException {
 // Given
 TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(
 Map.class, mapper.getTypeIdResolver(), "type", true, String.class) {
 @Override
 public TypeDeserializer forProperty(BeanProperty prop) {
 return new StubTypeDeserializer();
 }
 };
 ObjectNode node = mapper.createObjectNode();
 node.put("type", "Map");
 node.put("classKey", "String");
 node.put("valueType", "Integer");
 String json = mapper.writerWithTypeId().writeValueAsString(node);

 // When
 Object result = mapper.readValue(json, typeDeserializer.getType());

 // Then
 Map<String, Integer> actualMap = (Map<String, Integer>) result;
 assertEquals("Key1", actualMap.keySet().iterator().next());
 assertEquals(1, actualMap.size());
 assertTrue(actualMap.containsKey("Key1"));
 assertEquals(1, actualMap.get("Key1"));
 }

 @Test(expected = JsonMappingException.class)
 public void testTypeDeserializerBase_UnknownTypeId() throws IOException {
 // Given
 ObjectNode node = mapper.createObjectNode();
 node.put("type", "UnknownTypeId");
 String json = mapper.writerWithTypeId().writeValueAsString(node);
 TypeDeserializerBase typeDeserializer = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);

 // When & Then
 try {
 mapper.readValue(json, typeDeserializer.getType());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Pass
 assertEquals("Unknown type id 'UnknownTypeId' (for POJO property 'type'): not handling non-statically known type ids", e.getMessage());
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testTypeDeserializerBase_MissingTypeId() throws IOException {
 // Given
 ObjectNode node = mapper.createObjectNode();
 node.remove("type");
 String json = mapper.writerWithTypeId().writeValueAsString(node);
 TypeDeserializerBase typeDeserializer = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);

 // When & Then
 try {
 mapper.readValue(json, typeDeserializer.getType());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Pass
 assertEquals("Missing type id: type id property 'type' is not present", e.getMessage());
 }
 }
}

class StubTypeDeserializer extends TypeDeserializer {
 @Override
 public JsonTypeInfo.As getTypeInclusion() {
 return JsonTypeInfo.As.EXISTING_PROPERTY;
 }

 @Override
 public TypeDeserializer forProperty(BeanProperty prop) {
 return null;
 }
}