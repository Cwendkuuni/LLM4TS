import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeEnum() throws JsonProcessingException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.valuesOf(MyEnum.class), null);
 MyEnum enumValue = MyEnum.VALUE_1;
 ObjectNode node = mapper.createObjectNode();
 JsonGenerator gen = mapper.getFactory().createGenerator(node);
 serializer.serialize(enumValue, gen, null);
 gen.flush();
 String serializedValue = node.get(0).asText();
 assertTrue(serializedValue.equals(enumValue.name()));
 }

 @Test
 public void testSerializeEnumAsIndex() throws JsonProcessingException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.valuesOf(MyEnum.class), true);
 MyEnum enumValue = MyEnum.VALUE_1;
 ObjectNode node = mapper.createObjectNode();
 JsonGenerator gen = mapper.getFactory().createGenerator(node);
 serializer.serialize(enumValue, gen, null);
 gen.flush();
 int serializedValue = node.get(0).asInt();
 assertEquals(enumValue.ordinal(), serializedValue);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeEnumNull() throws JsonProcessingException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.valuesOf(MyEnum.class), null);
 MyEnum enumValue = null;
 ObjectNode node = mapper.createObjectNode();
 JsonGenerator gen = mapper.getFactory().createGenerator(node);
 serializer.serialize(enumValue, gen, null);
 fail("Expected JsonMappingException but got none");
 }

 @Test
 public void testSerializeEnumCustomValues() throws JsonProcessingException {
 EnumSerializer serializer = new EnumSerializer(new EnumValues() {{
 add(new SerializableString("CUSTOM_VALUE_1", "customValue1"));
 add(new SerializableString("CUSTOM_VALUE_2", "customValue2"));
 }}, null);
 MyEnumCustom enumValue = MyEnumCustom.CUSTOM_VALUE_1;
 ObjectNode node = mapper.createObjectNode();
 JsonGenerator gen = mapper.getFactory().createGenerator(node);
 serializer.serialize(enumValue, gen, null);
 gen.flush();
 String serializedValue = node.get(0).asText();
 assertTrue(serializedValue.equals("customValue1"));
 }
}

enum MyEnum {
 VALUE_1,
 VALUE_2
}

enum MyEnumCustom {
 CUSTOM_VALUE_1,
 CUSTOM_VALUE_2
}