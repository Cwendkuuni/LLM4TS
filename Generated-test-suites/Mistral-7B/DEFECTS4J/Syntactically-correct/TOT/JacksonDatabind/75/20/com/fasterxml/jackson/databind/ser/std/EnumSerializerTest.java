import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeEnumViaIndex() throws JsonProcessingException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.values(MyEnum.class), true);
 MyEnum enumValue = MyEnum.VALUE_1;
 ObjectNode node = mapper.writeValueAsTree(enumValue, serializer);
 assertEquals(0, node.get(0).asInt());
 }

 @Test
 public void testSerializeEnumViaToString() throws JsonProcessingException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.values(MyEnum.class), false);
 MyEnum enumValue = MyEnum.VALUE_2;
 ObjectNode node = mapper.writeValueAsTree(enumValue, serializer);
 assertEquals("VALUE_2", node.get(0).asText());
 }

 @Test
 public void testSerializeEnumWithCustomValues() throws JsonProcessingException {
 EnumSerializer serializer = new EnumSerializer(new EnumValues() {
 @Override
 public SerializableString[] values() {
 return new SerializableString[] { new SerializableString("CUSTOM_VALUE_1"), new SerializableString("CUSTOM_VALUE_2") };
 }
 }, MyEnum.class, false);
 MyEnum enumValue = MyEnum.VALUE_3;
 ObjectNode node = mapper.writeValueAsTree(enumValue, serializer);
 assertEquals("CUSTOM_VALUE_3", node.get(0).asText());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeEnumWithInvalidShape() throws JsonProcessingException {
 EnumSerializer serializer = new EnumSerializer(MyEnum.class, null);
 MyEnum enumValue = MyEnum.VALUE_4;
 try {
 mapper.writeValueAsTree(enumValue, serializer);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}

enum MyEnum {
 @JsonValue("VALUE_1")
 VALUE_1,
 @JsonValue("VALUE_2")
 VALUE_2,
 @JsonValue("VALUE_3")
 VALUE_3,
 @JsonValue("VALUE_4")
 VALUE_4;
}