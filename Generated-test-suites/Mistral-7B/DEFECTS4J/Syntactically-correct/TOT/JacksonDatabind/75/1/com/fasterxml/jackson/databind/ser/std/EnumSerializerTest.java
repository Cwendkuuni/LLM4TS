import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeEnumWithIndex() throws JsonGenerationException, IOException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.construct(MyEnum.class), true);
 ObjectNode node = mapper.createObjectNode();
 node.put("myEnumKey", serializer);
 JsonGenerator gen = mapper.getFactory().createGenerator(node, ObjectNode.class);
 MyEnum value = MyEnum.FIRST;
 serializer.serialize(value, gen, mapper.getSerializationConfig());
 gen.flush();
 assertEquals(0, node.get("myEnumKey").get(0).asInt());
 }

 @Test
 public void testSerializeEnumWithName() throws JsonGenerationException, IOException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.construct(MyEnum.class), false);
 ObjectNode node = mapper.createObjectNode();
 node.put("myEnumKey", serializer);
 JsonGenerator gen = mapper.getFactory().createGenerator(node, ObjectNode.class);
 MyEnum value = MyEnum.SECOND;
 serializer.serialize(value, gen, mapper.getSerializationConfig());
 gen.flush();
 assertEquals("SECOND", node.get("myEnumKey").asText());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeUnknownEnum() throws JsonGenerationException, IOException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.construct(MyEnum.class), false);
 ObjectNode node = mapper.createObjectNode();
 node.put("myEnumKey", serializer);
 JsonGenerator gen = mapper.getFactory().createGenerator(node, ObjectNode.class);
 Object unknownEnum = new Object();
 serializer.serialize(unknownEnum, gen, mapper.getSerializationConfig());
 }

 @Test
 public void testSerializeJsonFormat() throws JsonGenerationException, IOException {
 MyEnum value = MyEnum.FIRST;
 ObjectNode node = mapper.createObjectNode();
 node.put("myEnumKey", new EnumSerializer(EnumValues.construct(MyEnum.class), false));
 node.put("format", mapper.createObjectNode().put("shape", "STRING"));
 JsonGenerator gen = mapper.getFactory().createGenerator(node, ObjectNode.class);
 serializer.serialize(value, gen, mapper.getSerializationConfig());
 gen.flush();
 assertEquals("FIRST", node.get("myEnumKey").asText());
 }

 @Test
 public void testSerializeJsonFormatWithIndex() throws JsonGenerationException, IOException {
 MyEnum value = MyEnum.FIRST;
 ObjectNode node = mapper.createObjectNode();
 node.put("myEnumKey", new EnumSerializer(EnumValues.construct(MyEnum.class), true));
 node.put("format", mapper.createObjectNode().put("shape", "INT"));
 JsonGenerator gen = mapper.getFactory().createGenerator(node, ObjectNode.class);
 serializer.serialize(value, gen, mapper.getSerializationConfig());
 gen.flush();
 assertEquals(0, node.get("myEnumKey").get(0).asInt());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeJsonFormatWithInvalidShape() throws JsonGenerationException, IOException {
 MyEnum value = MyEnum.FIRST;
 ObjectNode node = mapper.createObjectNode();
 node.put("myEnumKey", new EnumSerializer(EnumValues.construct(MyEnum.class), false));
 node.put("format", mapper.createObjectNode().put("shape", "INVALID"));
 JsonGenerator gen = mapper.getFactory().createGenerator(node, ObjectNode.class);
 serializer.serialize(value, gen, mapper.getSerializationConfig());
 }
}

enum MyEnum {
 @JsonFormat(shape = Shape.STRING)
 FIRST,
 @JsonFormat(shape = Shape.INT)
 SECOND
}