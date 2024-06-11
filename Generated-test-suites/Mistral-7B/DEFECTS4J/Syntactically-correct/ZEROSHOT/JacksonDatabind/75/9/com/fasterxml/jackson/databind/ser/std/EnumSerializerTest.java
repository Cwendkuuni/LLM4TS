import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

 private EnumSerializer serializer;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 serializer = mapper.findValueSerializer(MyEnum.class);
 }

 @Test
 public void testSerializeAsEnumValue() throws JsonProcessingException {
 MyEnum enumValue = MyEnum.VALUE_ONE;
 ObjectNode result = mapper.writeValueAsTree(enumValue);
 assertEquals(MyEnum.VALUE_ONE.toString(), result.get(0).asText());
 }

 @Test
 public void testSerializeAsIndex() throws JsonProcessingException {
 MyEnum enumValue = MyEnum.VALUE_ONE;
 JsonGenerator gen = mapper.getPolymorphicSerializer(MyEnum.class).getSerializerProvider().getGenerator();
 gen.setOutputBoundType(MyEnum.class);
 serializer.serialize(enumValue, gen, null);
 assertEquals(0, gen.getCurrentIndex());
 }

 @Test
 public void testSerializeAsEnumValueWithToString() throws JsonProcessingException {
 MyEnum enumValue = MyEnum.VALUE_ONE;
 ObjectMapper objectMapperWithToString = new ObjectMapper();
 objectMapperWithToString.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
 ObjectNode result = objectMapperWithToString.writeValueAsTree(enumValue);
 assertEquals(MyEnum.VALUE_ONE.toString(), result.get(0).asText());
 }

 @Test
 public void testSerializeWithJsonFormat() throws JsonProcessingException {
 MyEnumWithJsonFormat enumWithJsonFormat = MyEnumWithJsonFormat.VALUE_TWO;
 ObjectNode result = mapper.writeValueAsTree(enumWithJsonFormat);
 assertEquals("VALUE_TWO_STRING", result.get(0).asText());
 }

 @Test
 public void testSerializeWithJsonFormatAsArray() throws JsonProcessingException {
 MyEnumWithJsonFormat enumWithJsonFormat = MyEnumWithJsonFormat.VALUE_TWO;
 ObjectMapper objectMapperWithArrayFormat = new ObjectMapper();
 objectMapperWithArrayFormat.findValueSerializer(MyEnumWithJsonFormat.class)
 .acceptJsonFormatVisitor(new JsonFormatVisitorWrapper(objectMapperWithArrayFormat.getSerializerProvider()) {
 @Override
 public JsonNode getSchema(SerializerProvider serializers, Type typeHint) {
 return createSchemaNode("array", true);
 }
 });
 ObjectNode result = objectMapperWithArrayFormat.writeValueAsTree(enumWithJsonFormat);
 assertTrue(result.isArray());
 }

 @Test
 public void testContextualSerialization() throws JsonProcessingException {
 MyContextualEnum contextualEnum = new MyContextualEnum("contextual_value");
 ObjectNode result = mapper.writeValueAsTree(contextualEnum);
 assertEquals("contextual_value", result.get(0).asText());
 }

 @Test
 public void testContextualSerializationWithIndex() throws JsonProcessingException {
 MyContextualEnum contextualEnum = new MyContextualEnum("contextual_value");
 JsonGenerator gen = mapper.getPolymorphicSerializer(MyContextualEnum.class).getSerializerProvider().getGenerator();
 gen.setOutputBoundType(MyContextualEnum.class);
 serializer.createContextual(mapper.getSerializerProvider(), null).serialize(contextualEnum, gen, null);
 assertEquals(1, gen.getCurrentIndex());
 }

 private static ObjectNode createSchemaNode(String type, boolean nullable) {
 ObjectNode schemaNode = JsonFactory.instance.createObjectNode();
 schemaNode.put("type", type);
 schemaNode.put("nullable", nullable);
 return schemaNode;
 }

 @JsonFormat(shape = Shape.STRING)
 enum MyEnum {
 VALUE_ONE("VALUE_ONE_STRING"),
 VALUE_TWO("VALUE_TWO_STRING");

 @JsonValue
 public String toString() {
 return this.name();
 }
 }

 enum MyEnumWithJsonFormat {
 @JsonFormat(shape = Shape.STRING, value = "VALUE_TWO_STRING")
 VALUE_ONE("VALUE_ONE_STRING"),
 VALUE_TWO("VALUE_TWO_STRING");
 }

 class MyContextualEnum {
 String value;

 public MyContextualEnum(String value) {
 this.value = value;
 }
 }
}