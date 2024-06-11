import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;
import static org.junit.Assert.*;

import static com.fasterxml.jackson.databind.SerializationFeature.*;

public class EnumSerializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeEnumAsString() throws IOException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.allValues(Apple.class), null);
 ObjectNode node = mapper.createObjectNode();
 node.put("color", Apple.RED);
 StringWriter sw = new StringWriter();
 JsonGenerator gen = mapper.getFactory().createGenerator(sw);
 mapper.writeValue(gen, node);
 gen.close();
 String result = sw.toString();
 String expected = "\"RED\"";
 assertEquals(expected, result);
 }

 @Test
 public void testSerializeEnumAsIndex() throws IOException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.allValues(Apple.class), Boolean.TRUE);
 ObjectNode node = mapper.createObjectNode();
 node.put("color", Apple.RED);
 StringWriter sw = new StringWriter();
 JsonGenerator gen = mapper.getFactory().createGenerator(sw);
 mapper.enable(WRITE_ENUMS_USING_INDEX);
 mapper.writeValue(gen, node);
 gen.close();
 String result = sw.toString();
 assertEquals("4", result); // assuming enum constants are ordered
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeUnknownEnum() throws IOException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.allValues(Apple.class), null);
 ObjectNode node = mapper.createObjectNode();
 node.put("color", new Enum("UnknownColor", 10));
 StringWriter sw = new StringWriter();
 JsonGenerator gen = mapper.getFactory().createGenerator(sw);
 mapper.writeValue(gen, node);
 gen.close();
 // no exception thrown in current version of Jackson
 // throw new JsonMappingException("Unexpected value: " + result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeUnknownEnumString() throws IOException {
 String json = "{\"color\": \"UnknownColor\"}";
 JsonParser parser = mapper.getFactory().createParser(new StringReader(json));
 ObjectCodec codec = mapper.getFactory().getCodec();
 JsonNode node = codec.readTree(parser);
 EnumSerializer deserializer = new EnumSerializer(EnumValues.allValues(Apple.class), null);
 assertEquals(Apple.RED, deserializer.deserialize(JsonToken.VALUE_STRING, node.get("color"), null));
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeUnknownEnumIndex() throws IOException {
 String json = "{\"color\": 10}";
 JsonParser parser = mapper.getFactory().createParser(new StringReader(json));
 ObjectCodec codec = mapper.getFactory().getCodec();
 JsonNode node = codec.readTree(parser);
 EnumSerializer deserializer = new EnumSerializer(EnumValues.allValues(Apple.class), null);
 assertEquals(Apple.RED, deserializer.deserialize(JsonToken.VALUE_NUMBER_INT, node.get("color"), null));
 }

 @Test
 public void testDeserializeEnumString() throws IOException {
 String json = "{\"color\": \"GREEN\"}";
 JsonParser parser = mapper.getFactory().createParser(new StringReader(json));
 ObjectCodec codec = mapper.getFactory().getCodec();
 JsonNode node = codec.readTree(parser);
 EnumSerializer deserializer = new EnumSerializer(EnumValues.allValues(Apple.class), null);
 assertEquals(Apple.GREEN, deserializer.deserialize(JsonToken.VALUE_STRING, node.get("color"), null));
 }

 @Test
 public void testDeserializeEnumIndex() throws IOException {
 String json = "{\"color\": 2}";
 JsonParser parser = mapper.getFactory().createParser(new StringReader(json));
 ObjectCodec codec = mapper.getFactory().getCodec();
 JsonNode node = codec.readTree(parser);
 EnumSerializer deserializer = new EnumSerializer(EnumValues.allValues(Apple.class), Boolean.TRUE);
 mapper.enable(WRITE_ENUMS_USING_INDEX);
 assertEquals(Apple.GREEN, deserializer.deserialize(JsonToken.VALUE_NUMBER_INT, node.get("color"), null));
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeNullEnum() throws IOException {
 JsonParser parser = mapper.getFactory().createParser(new StringReader("{\"color\": null}"));
 ObjectCodec codec = mapper.getFactory().getCodec();
 JsonNode node = codec.readTree(parser);
 EnumSerializer deserializer = new EnumSerializer(EnumValues.allValues(Apple.class), null);
 try {
 deserializer.deserializemock(JsonToken.NULL, null, null);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

}

enum Apple {
 RED, GREEN, BLUE
}