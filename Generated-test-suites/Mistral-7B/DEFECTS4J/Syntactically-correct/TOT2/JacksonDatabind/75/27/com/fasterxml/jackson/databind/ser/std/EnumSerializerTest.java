import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnumSerializerTest {

 private static final ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testSerializeEnumAsIndex() throws JsonProcessingException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.allValues(MyEnum.class), true);
 Enum<?> myEnumValue = MyEnum.VALUE_1;
 ObjectWriter objectWriter = objectMapper.writerWithContextualSerializer(serializer.getClass(), MyEnum.class);
 ObjectNode result = objectWriter.writeValueAsTree(myEnumValue);
 assertEquals(String.valueOf(myEnumValue.ordinal()), result.get(0).asText());
 }

 @Test
 public void testSerializeEnumAsString() throws JsonProcessingException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.allValues(MyEnum.class), false);
 Enum<?> myEnumValue = MyEnum.VALUE_1;
 ObjectWriter objectWriter = objectMapper.writerWithContextualSerializer(serializer.getClass(), MyEnum.class);
 ObjectNode result = objectWriter.writeValueAsTree(myEnumValue);
 assertEquals("VALUE_1", result.get(0).asText());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeEnumWithCustomToString() throws JsonProcessingException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.allValues(MyEnumCustomToString.class), false);
 Enum<?> myEnumValue = MyEnumCustomToString.VALUE_2;
 ObjectWriter objectWriter = objectMapper.writerWithContextualSerializer(serializer.getClass(), MyEnumCustomToString.class);
 ObjectNode result = objectWriter.writeValueAsTree(myEnumValue);
 assertEquals("CUSTOM_TO_STRING_VALUE_2", result.get(0).asText());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeEnumWithShapeAnnotation() throws JsonProcessingException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.allValues(MyEnumShapeAnnotation.class), null);
 ObjectNode result = serializer.getSchema(objectMapper.getSerializerProvider(), MyEnumShapeAnnotation.class.getType());
 assertEquals("integer", result.get("type").asText());
 }

 @Test
 public void testDeserializeEnumAsIndex() throws JsonProcessingException {
 EnumValues enumValues = EnumValues.allValues(MyEnum.class);
 ObjectReader objectReader = objectMapper.readerForValueTypeOf(MyEnum.class);
 ObjectNode node = objectMapper.createObjectNode();
 node.put(0, "0");
 ObjectNode enumNode = node.putArray("enum");
 enumNode.add("VALUE_1");
 enumNode.add("VALUE_2");
 ObjectNode rootNode = objectMapper.readTree(node);
 ObjectReader reader = objectMapper.readerForValueTypeOf(MyEnum.class).withValueToBeNull(MyEnum.class);
 MyEnum deserializedEnum = reader.readValue(rootNode);
 assertEquals(MyEnum.VALUE_1, deserializedEnum);
 }

 @Test
 public void testDeserializeEnumAsString() throws JsonProcessingException {
 ObjectReader objectReader = objectMapper.readerForValueTypeOf(MyEnum.class);
 ObjectNode node = objectMapper.createObjectNode();
 node.put(0, "VALUE_1");
 ObjectNode rootNode = objectMapper.readTree(node);
 MyEnum deserializedEnum = objectReader.readValue(rootNode);
 assertEquals(MyEnum.VALUE_1, deserializedEnum);
 }

 @Test
 public void testDeserializeEnumWithCustomToString() throws JsonProcessingException {
 ObjectReader objectReader = objectMapper.readerForValueTypeOf(MyEnumCustomToString.class);
 ObjectNode node = objectMapper.createObjectNode();
 node.put(0, "CUSTOM_TO_STRING_VALUE_1");
 ObjectNode rootNode = objectMapper.readTree(node);
 MyEnumCustomToString deserializedEnum = objectReader.readValue(rootNode);
 assertEquals(MyEnumCustomToString.VALUE_1, deserializedEnum);
 }
}