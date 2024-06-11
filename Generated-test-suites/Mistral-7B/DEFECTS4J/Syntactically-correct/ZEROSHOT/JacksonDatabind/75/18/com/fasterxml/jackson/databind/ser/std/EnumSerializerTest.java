import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class EnumSerializerTest {

 private ObjectMapper objectMapper;
 private EnumSerializer enumSerializer;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 enumSerializer = objectMapper.findValueSerializer(MyEnum.class);
 }

 @Test
 public void testSerializeAsIndex() throws IOException {
 JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializer(Enum.class).getFactory()
 .createGenerator(new ByteArrayOutputStream(), null);
 ObjectNode objectNode = objectMapper.createObjectNode();
 ObjectWriter objectWriter = objectMapper.writerWithRootValue(objectNode);

 // Test serializing as index (true)
 JsonSerializer serializer = objectMapper.findValueSerializer(MyEnum.class, true);
 MyEnum myEnum = MyEnum.VALUE1;
 Object writeValueAs = serializer.serializeValue(myEnum, jsonGenerator, null);
 int index = jsonGenerator.getOutputBuffer().readInt();
 assertEquals(MyEnum.VALUE1.ordinal(), index);

 // Test serializing as text (false)
 serializer = objectMapper.findValueSerializer(MyEnum.class, false);
 MyEnum myEnumText = MyEnum.VALUE2;
 Object writeValueB = serializer.serializeValue(myEnumText, jsonGenerator, null);
 String value = jsonGenerator.getOutputBuffer().readUTF();
 assertEquals(MyEnum.VALUE2.name(), value);
 }

 @Test
 public void testSerializeUsingToString() throws IOException {
 JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializer(Enum.class).getFactory()
 .createGenerator(new ByteArrayOutputStream(), null);
 ObjectNode objectNode = objectMapper.createObjectNode();
 ObjectWriter objectWriter = objectMapper.writerWithRootValue(objectNode);

 ObjectMapper objectMapperToString = new ObjectMapper();
 objectMapperToString.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
 ObjectSerializer serializerToString = objectMapperToString.findValueSerializer(MyEnum.class);
 MyEnum myEnumToString = MyEnum.VALUE3;
 Object writeValueC = serializerToString.serializeValue(myEnumToString, jsonGenerator, null);
 String valueToString = jsonGenerator.getOutputBuffer().readUTF();
 assertEquals(MyEnum.VALUE3.toString(), valueToString);
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormat.Value format = new JsonFormat.Value();
 JsonFormatVisitorWrapper visitor = objectMapper.getSerializationConfig().findFormatValueVisitor(MyEnum.class, format);
 ArrayNode enumNode = visitor.expectArrayFormat(null);
 List<String> enums = enumNode.getValuesAs(String.class);
 assertEquals(3, enums.size());
 assertEquals(MyEnum.VALUE1.name(), enums.get(0));
 assertEquals(MyEnum.VALUE2.name(), enums.get(1));
 assertEquals(MyEnum.VALUE3.name(), enums.get(2));
 }

 @Test
 public void testGetSchema() throws IOException {
 ObjectNode schemaNode = (ObjectNode) enumSerializer.getSchema(objectMapper.getSerializationConfig(), null);
 ArrayNode enumNodeSchema = schemaNode.path("enum");
 assertEquals(3, enumNodeSchema.size());
 assertEquals(MyEnum.VALUE1.name(), enumNodeSchema.get(0).asText());
 assertEquals(MyEnum.VALUE2.name(), enumNodeSchema.get(1).asText());
 assertEquals(MyEnum.VALUE3.name(), enumNodeSchema.get(2).asText());
 }
}

 enum MyEnum {
 @JsonFormat(shape = Shape.STRING)
 VALUE1,
 @JsonFormat(shape = Shape.NUMBER)
 VALUE2,
 @JsonFormat(shape = Shape.STRING, serializableAs = MyEnumSerializer.class)
 VALUE3;
}

 class MyEnumSerializer extends StdScalarSerializer<MyEnum> {
 @Override
 protected void _serialize(MyEnum value, JsonGenerator gen, SerializerProvider serializers)
 throws IOException {
 gen.writeString(value.name());
 }
 }