import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeEnumAsIndex() throws JsonProcessingException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.values(MyEnum.class), true);
 MyEnum enumValue = MyEnum.VALUE_1;
 ObjectNode result = mapper.writeValueAsTree(serializer, enumValue);
 assertEquals(Integer.toString(enumValue.ordinal()), result.toString());
 }

 @Test
 public void testSerializeEnumAsString() throws JsonProcessingException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.values(MyEnum.class), false);
 MyEnum enumValue = MyEnum.VALUE_1;
 ObjectNode result = mapper.writeValueAsTree(serializer, enumValue);
 assertEquals("VALUE_1", result.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testSerializeNullEnum() throws JsonProcessingException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.values(MyEnum.class), false);
 ObjectNode result = mapper.writeValueAsTree(serializer, null);
 fail("Expected JsonProcessingException when trying to serialize null Enum");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeUnknownEnum() throws JsonProcessingException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.values(MyEnum.class), false);
 ObjectNode result = mapper.writeValueAsTree(serializer, new Object());
 fail("Expected IllegalArgumentException when trying to serialize unknown Enum");
 }

 @Test
 public void testDeserializeEnumAsIndex() throws JsonProcessingException {
 ObjectNode jsonNode = mapper.readValue("\"0\"", ObjectNode.class);
 MyEnum enumValue = mapper
 .readerForUpdating(MyEnum.class)
 .readValue(jsonNode);
 assertEquals(MyEnum.VALUE_1, enumValue);
 }

 @Test
 public void testDeserializeEnumAsString() throws JsonProcessingException {
 ObjectNode jsonNode = mapper.readValue("\"VALUE_1\"", ObjectNode.class);
 MyEnum enumValue = mapper
 .readerForUpdating(MyEnum.class)
 .readValue(jsonNode);
 assertEquals(MyEnum.VALUE_1, enumValue);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeNullJsonNode() throws JsonProcessingException {
 ObjectNode jsonNode = null;
 MyEnum enumValue = mapper
 .readerForUpdating(MyEnum.class)
 .readValue(jsonNode);
 fail("Expected JsonMappingException when trying to deserialize null JsonNode");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJsonNode() throws JsonProcessingException {
 ObjectNode jsonNode = mapper.readValue("\"invalidJson\"", ObjectNode.class);
 try {
 MyEnum enumValue = mapper
 .readerForUpdating(MyEnum.class)
 .readValue(jsonNode);
 fail("Expected JsonMappingException when trying to deserialize invalid JsonNode");
 } catch (JsonMappingException e) {
 // expected
 }
}

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 SerializerProvider serializers = mapper.getSerializationConfig()
 .getDefaultSerializerProvider();
 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.STRING);
 JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getSchemaProvider().getFormatVisitor(serializers, new TypeReference<ObjectNode>() {});
 EnumSerializer serializer = new EnumSerializer(EnumValues.values(MyEnum.class), false);
 serializer.acceptJsonFormatVisitor(visitor, MyEnum.class.getType());
 assertEquals("string", visitor.getFormatName());
}

 @Test
 public void testGetSchema() throws JsonProcessingException {
 SerializerProvider serializers = mapper.getSerializationConfig()
 .getDefaultSerializerProvider();
 JsonNode schema = new EnumSerializer(EnumValues.values(MyEnum.class), false).getSchema(serializers, MyEnum.class.getType());
 assertEquals("string", schema.toString().split(":")[1]);
}

 @Test
 public void testCreateContextual() throws JsonMappingException {
 SerializerProvider serializers = mapper.getSerializationConfig().getDefaultSerializerProvider();
 BeanDescription beanDescription = new BeanDescription();
 MyEnum enumValue = MyEnum.VALUE_1;
 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.STRING);
 JsonSerializer<MyEnum> serializedEnum = new EnumSerializer(EnumValues.values(MyEnum.class), false).createContextual(serializers, null);
 assertEquals(serializedEnum.getClass(), EnumSerializer.class);
 MyEnum deserializedEnum = mapper.readerForUpdating(MyEnum.class).readValue(serializedEnum.serialize(enumValue, null, serializers));
 assertEquals(enumValue, deserializedEnum);
}
}