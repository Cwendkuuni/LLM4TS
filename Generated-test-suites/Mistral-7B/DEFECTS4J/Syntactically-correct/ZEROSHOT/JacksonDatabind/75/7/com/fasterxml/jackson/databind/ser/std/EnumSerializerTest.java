import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.EnumDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

 private EnumSerializer enumSerializer;
 private EnumDeserializer enumDeserializer;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 ObjectMapper mapper = new ObjectMapper();
 objectMapper = mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
 objectMapper.findAndRegisterModules();
 enumSerializer = objectMapper.findValueSerializer(Enum.class);
 enumDeserializer = objectMapper.findValueDeserializer(Enum.class, null);
 }

 @Test
 public void testSerializationWithIndex() throws JsonGenerationException, IOException {
 JsonGenerator gen = objectMapper.getFactory().createGenerator(new StringWriter());
 Enum value = Enum.values()[0];
 ObjectNode rootNode = objectMapper.writeValueAsTree(value);
 ArrayNode enumNode = rootNode.path("enum");
 int index = enumNode.get(value.ordinal()).asInt();
 assertEquals(0, index);
 gen.writeNumber(value.ordinal());
 String expected = "0";
 String actual = gen.getOutputContent();
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializationWithToString() throws JsonGenerationException, IOException {
 ObjectNode rootNode = objectMapper.writeValueAsTree(Enum.values()[0]);
 JsonFormat format = rootNode.path("$ref").getAnnotation(JsonFormat.class);
 assertNotNull(format);
 assertEquals(JsonFormat.Shape.STRING, format.shape());
 String expected = Enum.values()[0].toString();
 String actual = rootNode.get("value").asText();
 assertEquals(expected, actual);

 JsonGenerator gen = objectMapper.getFactory().createGenerator(new StringWriter());
 gen.writeStartObject();
 gen.writeFieldName("value");
 gen.writeString(expected);
 gen.writeEndObject();
 String generatedJson = gen.getOutputContent();
 assertEquals(expected, generatedJson);
 }

 @Test
 public void testSerializationWithName() throws JsonGenerationException, IOException {
 JsonFormat format = new JsonFormat();
 format.shape(JsonFormat.Shape.STRING);
 ObjectNode rootNode = objectMapper.valueToTree(new MyEnum("value1"));
 format.applyTo(rootNode, null);
 String expected = "value1";
 String actual = rootNode.get("value").asText();
 assertEquals(expected, actual);

 JsonGenerator gen = objectMapper.getFactory().createGenerator(new StringWriter());
 gen.writeStartObject();
 gen.writeFieldName("value");
 gen.writeString(expected);
 gen.writeEndObject();
 String generatedJson = gen.getOutputContent();
 assertEquals(expected, generatedJson);
 }

 @Test
 public void testDeserializationWithIndex() throws IOException {
 ObjectNode rootNode = new ObjectMapper().readTree("{\"enum\":[0]}");
 Enum value = enumDeserializer.deserialize(Enum.class, null, rootNode);
 assertEquals(Enum.values()[0], value);
 }

 @Test
 public void testDeserializationWithToString() throws IOException {
 ObjectNode rootNode = new ObjectMapper().readTree("{\"value\":\"value1\"}");
 Enum value = enumDeserializer.deserialize(Enum.class, null, rootNode);
 assertEquals(new MyEnum("value1"), value);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithInvalidValue() throws IOException {
 ObjectNode rootNode = new ObjectMapper().readTree("{\"value\":\"invalidValue\"}");
 try {
 Enum value = enumDeserializer.deserialize(Enum.class, null, rootNode);
 } catch (JsonMappingException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationWithInvalidShape() throws JsonGenerationException, IOException {
 JsonFormat format = new JsonFormat();
 format.shape(JsonFormat.Shape.ARRAY);
 try {
 enumSerializer.serialize(Enum.values()[0], null, null, format);
 } catch (JsonMappingException e) {
 assertEquals("Invalid serialization shape [ARRAY] for Enum class", e.getMessage());
 }
 }
}

enum MyEnum {
 @JsonFormat(shape = JsonFormat.Shape.STRING, value = "value1")
 value1("value1"),
 value2("value2");

 private final String value;
 MyEnum(String value) {
 this.value = value;
 }

 public String getValue() {
 return value;
 }
}