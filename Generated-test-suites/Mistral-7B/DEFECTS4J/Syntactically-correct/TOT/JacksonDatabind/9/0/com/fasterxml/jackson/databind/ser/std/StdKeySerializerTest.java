import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public class StdKeySerializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testSerializeDate() throws JsonProcessingException {
 Date date = new Date();
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 serializer.serialize(date, jsonGenerator, new SerializerProvider());
 JsonNode expected = JsonNodeFactory.instance.textNode("" + date.getTime());
 JsonNode actual = objectMapper.readTree(System.out);
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeString() throws JsonProcessingException {
 String value = "test";
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 serializer.serialize(value, jsonGenerator, new SerializerProvider());
 JsonNode expected = JsonNodeFactory.instance.textNode(value);
 JsonNode actual = objectMapper.readTree(System.out);
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeNull() throws JsonProcessingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 serializer.serialize(null, jsonGenerator, new SerializerProvider());
 JsonNode expected = JsonNodeFactory.instance.nullNode();
 JsonNode actual = objectMapper.readTree(System.out);
 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeInvalidType() throws JsonProcessingException {
 Number number = new Integer(1);
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 try {
 serializer.serialize(number, jsonGenerator, new SerializerProvider());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonSchema jsonSchema = serializer.getSchema(new SerializerProvider(), Object.class);
 JsonNode expected = JsonNodeFactory.instance.objectNode().put("type", "string");
 JsonNode actual = jsonSchema.getSchema();
 assertEquals(expected, actual);
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonFormatVisitorWrapper visitor = objectMapper.schemaFactory.getJsonFormatVisitor(SerializerProvider.StandardSerializers.STRING_FORMATTER, Object.class);
 serializer.acceptJsonFormatVisitor(visitor, Object.class);
 }
}