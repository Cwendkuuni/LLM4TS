import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeEnumAsString() throws JsonProcessingException {
 EnumSerializer serializer = mapper.findValueSerializer(MyEnum.class);
 MyEnum enumValue = MyEnum.VALUE_1;

 ObjectNode jsonNode = mapper.writeValueAsObject(enumValue, new ObjectNode());
 String serializedValue = jsonNode.get("value").asText();
 assertEquals("VALUE_1", serializedValue);
 }

 @Test
 public void testSerializeEnumAsIndex() throws JsonProcessingException {
 EnumSerializer serializer = mapper.findValueSerializer(MyEnum.class, new JsonSerializer.TypingHint[] {
 new JsonSerializer.TypingHint(Integer.class) });
 MyEnum enumValue = MyEnum.VALUE_2;

 ObjectNode jsonNode = mapper.writeValueAsObject(enumValue, new ObjectNode());
 int serializedValue = jsonNode.get("value").asInt();
 assertEquals(2, serializedValue);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeUnknownEnumValue() throws JsonProcessingException {
 ObjectNode node = new ObjectNode(mapper.getFactory());
 node.put("value", "UNKNOWN_VALUE");
 ObjectMapper mapper = new ObjectMapper();
 try {
 mapper.readValue(node, MyEnum.class);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testDeserializeEnumFromString() throws JsonProcessingException {
 ObjectNode jsonNode = mapper.createObjectNode();
 jsonNode.put("value", "VALUE_1");
 MyEnum deserializedEnum = mapper.readValue(jsonNode, MyEnum.class);
 assertEquals(MyEnum.VALUE_1, deserializedEnum);
 }

 @Test
 public void testDeserializeEnumFromIndex() throws JsonProcessingException {
 ObjectNode jsonNode = mapper.createObjectNode();
 jsonNode.put("value", "2");
 MyEnum deserializedEnum = mapper.readValue(jsonNode, MyEnum.class);
 assertEquals(MyEnum.VALUE_2, deserializedEnum);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeserializeUnknownEnumValue() throws JsonProcessingException {
 ObjectNode jsonNode = mapper.createObjectNode();
 jsonNode.put("value", "UNKNOWN_VALUE");
 try {
 mapper.readValue(jsonNode, MyEnum.class);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}

enum MyEnum {
 @JsonFormat(shape = Shape.STRING)
 VALUE_1,
 @JsonFormat(shape = Shape.NUMBER, serializeAs = JsonFormat.Serializers.IntegerSerializer.class)
 VALUE_2;
}