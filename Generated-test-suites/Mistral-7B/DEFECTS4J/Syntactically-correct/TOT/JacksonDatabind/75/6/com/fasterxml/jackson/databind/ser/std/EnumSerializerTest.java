import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeEnum() throws JsonProcessingException {
 EnumSerializer serializer = mapper.findValueSerializer(Enum.class);
 Enum myEnum = MyEnum.ONE;
 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer.serialize(myEnum, gen, null);
 String result = gen.getOutputContent();
 assertEquals("\"ONE\"", result);
 }

 @Test
 public void testSerializeEnumIndex() throws JsonProcessingException {
 EnumSerializer serializer = mapper.findValueSerializer(Enum.class);
 ObjectWriter writer = mapper.writerWithView(JsonView.Full.class);
 ArrayNode actual = writer.writeValueAsArray(new Enum[]{MyEnum.ONE, MyEnum.TWO});
 int indexOfOne = -1;
 for (int i = 0; i < actual.size(); i++) {
 if (actual.get(i).asText().equals("ONE")) {
 indexOfOne = i;
 break;
 }
 }
 assertTrue("Expected ONE to have an index", indexOfOne > -1);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeEnumWithoutFormat() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectWriter writer = mapper.writerWithView(JsonView.Full.class);
 writer.writeValueAsString(MyEnum.ONE);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeEnumWithInvalidFormat() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectWriter writer = mapper.writerWithView(JsonView.Full.class);
 JsonFormat format = new JsonFormat(Shape.STRING, null, new String[]{"invalid"});
 ObjectNode node = (ObjectNode) writer.withFormat(format).writeValueAsTree(MyEnum.ONE);
 assertNotNull("Expected an error when serializing with invalid format", node.get("errors"));
 }

 @Test
 public void testSerializeEnumWithShapeString() throws JsonProcessingException {
 EnumSerializer serializer = mapper.findValueSerializer(Enum.class);
 ObjectWriter writer = mapper.writerWithView(JsonView.Full.class);
 JsonFormat format = new JsonFormat(Shape.STRING, null, null);
 ObjectNode node = (ObjectNode) writer.withFormat(format).writeValueAsTree(MyEnum.ONE);
 assertEquals("\"ONE\"", node.get("value").asText());
 }

 @Test
 public void testSerializeEnumWithShapeNumeric() throws JsonProcessingException {
 EnumSerializer serializer = mapper.findValueSerializer(Enum.class);
 ObjectWriter writer = mapper.writerWithView(JsonView.Full.class);
 JsonFormat format = new JsonFormat(Shape.INT, null, null);
 ObjectNode node = (ObjectNode) writer.withFormat(format).writeValueAsTree(MyEnum.ONE);
 assertEquals("0", node.get("value").asText());
 }

 @Test
 public void testSerializeEnumWithShapeArray() throws JsonProcessingException {
 EnumSerializer serializer = mapper.findValueSerializer(Enum.class);
 ObjectWriter writer = mapper.writerWithView(JsonView.Full.class);
 JsonFormat format = new JsonFormat(Shape.ARRAY, null, null);
 ArrayNode arrayNode = (ArrayNode) writer.withFormat(format).writeValueAsTree(MyEnum.values());
 assertEquals(2, arrayNode.size());
 }

 @Test
 public void testDeserializeEnum() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 MyEnum expected = MyEnum.ONE;
 String json = mapper.writerWithView(JsonView.Full.class).writeValueAsString(expected);
 MyEnum actual = mapper.readValue(json, MyEnum.class);
 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEnumInvalid() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"invalid\":\"not an enum\"}";
 MyEnum actual = mapper.readValue(json, MyEnum.class);
 fail("Expected an error when deserializing invalid JSON");
 }

 @Test
 public void testDeserializeEnumEmpty() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "[]";
 Enum[] actual = mapper.readValue(json, Enum[].class);
 assertNull("Expected null when deserializing empty JSON array", actual);
 }

 @Test
 public void testDeserializeEnumArray() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "[\"ONE\", \"TWO\"]";
 Enum[] actual = mapper.readValue(json, Enum[].class);
 assertEquals(MyEnum.ONE, actual[0]);
 assertEquals(MyEnum.TWO, actual[1]);
 }

 @Test
 public void testDeserializeEnumWithShapeString() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"value\":\"ONE\"}";
 MyEnum actual = mapper.readValue(json, MyEnum.class);
 assertEquals(MyEnum.ONE, actual);
 }

 @Test
 public void testDeserializeEnumWithShapeNumeric() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"value\":0}";
 MyEnum actual = mapper.readValue(json, MyEnum.class);
 assertEquals(MyEnum.ONE, actual);
 }

 @Test
 public void testDeserializeEnumWithShapeArray() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "[0, 1]";
 Enum[] actual = mapper.readValue(json, Enum[].class);
 assertEquals(MyEnum.ONE, actual[0]);
 assertEquals(MyEnum.TWO, actual[1]);
 }
}