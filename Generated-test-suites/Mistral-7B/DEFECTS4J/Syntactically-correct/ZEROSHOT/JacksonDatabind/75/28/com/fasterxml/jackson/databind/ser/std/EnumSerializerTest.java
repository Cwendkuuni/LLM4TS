import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

/**
 * Test class for {@link EnumSerializer}
 */
public class EnumSerializerTest {

 private EnumSerializer serializer;
 private EnumValues enumValues;

 @Before
 public void setUp() {
 enumValues = new EnumValues(MyEnum.class);
 serializer = new EnumSerializer(enumValues, false);
 }

 @Test
 public void testSerializeToJson() throws JsonProcessingException {
 ObjectNode result = new ObjectMapper().writeValueAsObject(MyEnum.VAL1);
 JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(new StringWriter());
 serializer.serialize(MyEnum.VAL1, gen, null);
 assertEquals(result.toString(), gen.getOutputContent().toString());
 }

 @Test
 public void testSerializeToJsonIndex() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, false);
 JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
 JsonSerializer<Enum<?>> indexSerializer = EnumSerializer.construct(MyEnum.class, null, null, null);
 indexSerializer.serialize(MyEnum.VAL1, gen, null);
 assertEquals("0", gen.getOutputContent().toString());
 }

 @Test
 public void testSerializeToJsonToString() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
 JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
 serializer.serialize(MyEnum.VAL1, gen, null);
 assertEquals("VAL_ONE", gen.getOutputContent().toString());
 }

 @Test
 public void testSerializeToSchema() throws JsonProcessingException {
 ObjectNode schema = serializer.getSchema(null, null);
 assertEquals("string", schema.get("type").asText());
 ArrayNode enumNode = schema.path("properties").get("enum");
 assertEquals(2, enumNode.size());
 assertEquals("VAL_ONE", enumNode.get(0).asText());
 assertEquals("VAL_TWO", enumNode.get(1).asText());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonFormatVisitorWrapper visitor = mapper.getFactory().constructSchemaAndFinishTreeVisitor("root", null);
 serializer.acceptJsonFormatVisitor(visitor, null);
 assertEquals("string", visitor.getCurrentValue().get("type").asText());
 ArrayNode enumNode = visitor.getCurrentValue().path("properties").get("enum");
 assertEquals(2, enumNode.size());
 assertEquals("VAL_ONE", enumNode.get(0).asText());
 assertEquals("VAL_TWO", enumNode.get(1).asText());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnsupportedSerializationShape() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonFormat format = new JsonFormat();
 format.setShape(JsonFormat.Shape.DEFAULT);
 ObjectNode rootNode = new ObjectMapper().writeValueAsObject(new ObjectNode());
 JsonSerializer<Enum<?>> serializer = EnumSerializer.construct(MyEnum.class, null, null, format);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testCreateContextual() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonFormat format = new JsonFormat();
 format.setShape(JsonFormat.Shape.STRING);
 JsonSerializer<Enum<?>> contextualSerializer = serializer.createContextual(mapper.getSerializerProvider(), null);
 assertEquals("VAL_ONE", contextualSerializer.serialize(MyEnum.VAL1, null, null).toString());
 }
}

 enum MyEnum {
 @JsonFormat(shape = JsonFormat.Shape.STRING, value = "VAL_ONE")
 VAL_ONE,
 @JsonFormat(shape = JsonFormat.Shape.STRING, value = "VAL_TWO")
 VAL_TWO
 }