import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

 private ObjectMapper objectMapper;
 private EnumSerializer enumSerializer;

 @Before
 public void setup() throws JsonProcessingException {
 objectMapper = new ObjectMapper();
 EnumSerializer.construct(Integer.TYPE, objectMapper.getSerializationConfig(), null, null);
 enumSerializer = (EnumSerializer) objectMapper.findValueSerializer(Integer.TYPE);
 }

 @Test
 public void testSerializeEnumUsingIndex() throws IOException {
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 Enum[] enums = Enum.values();
 for (Enum e : enums) {
 enumSerializer.serialize(e, gen, null);
 gen.flush();
 System.out.println();
 }
 }

 @Test
 public void testSerializeEnumUsingToString() throws IOException {
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 Enum[] enums = Enum.values();
 for (Enum e : enums) {
 enumSerializer.serialize(e, gen, null);
 gen.flush();
 System.out.println();
 gen.writeStartObject();
 gen.writeFieldName("name");
 gen.writeString(e.name());
 gen.writeEndObject();
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeNullEnum() throws IOException {
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 enumSerializer.serialize(null, gen, null);
 }

 @Test
 public void testSerializeEnumWithCustomValues() throws IOException {
 ObjectNode node = objectMapper.createObjectNode();
 node.put("enumValues", objectMapper.valueToTree(EnumValues.construct(MyEnum.class, new String[]{"VALUE1", "VALUE2"})));
 ObjectNode rootNode = objectMapper.readTree(new ByteArrayInputStream(node.toString().getBytes())).path("$");
 ObjectNode enumNode = rootNode.path("enumValues").path("enum");
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 for (IntEnum e : MyEnum.values()) {
 enumSerializer.serialize(e, gen, null);
 gen.flush();
 gen.writeStartObject();
 gen.writeFieldName("name");
 gen.writeString(e.name());
 gen.writeEndObject();
 }
 }

 @Test
 public void testSerializeEnumWithShapeAnnotation() throws IOException {
 ObjectNode node = objectMapper.createObjectNode();
 ObjectNode rootNode = node.putObject("value");
 rootNode.put("enumClass", objectMapper.readTree(new ByteArrayInputStream(getClass().getName().getBytes())));
 rootNode.put("format", objectMapper.valueToTree(new JsonFormat.Value().withShape(Shape.INT)));
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 for (MyEnum e : MyEnum.values()) {
 enumSerializer.serialize(e, gen, null);
 gen.flush();
 gen.writeNumber(e.ordinal());
 gen.flush();
 }
 }
}