import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testIntegerSerializer() throws JsonProcessingException, JsonGenerationException {
 Integer input = 123;
 String output = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);
 JsonParser parser = objectMapper.getFactory().createParser(output);
 int expectedInt = 123;
 int actualInt = parser.readValueAs(int.class);
 assertEquals(expectedInt, actualInt);
 }

 @Test
 public void testIntegerSerializerAcceptJsonFormatVisitor() throws JsonProcessingException {
 Integer input = 123;
 JsonNode expectedSchema = objectMapper.typeFactory.constructObjectType(JsonType.Std.INT, "root");
 JsonNode actualSchema = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(input).getSchema();
 assertEquals(expectedSchema.toString(), actualSchema.toString());
 }

 @Test
 public void testIntegerSerializerCreateContextual() throws JsonProcessingException {
 JsonNode inputNode = objectMapper.createObjectNode();
 inputNode.put("foo", 123);
 ObjectMapper mapper = new ObjectMapper();
 BeanProperty property = mapper.findProperty("foo", inputNode);
 JsonSerializer<?> serializer = objectMapper.findValueSerializer(property.getType(), null);
 assertEquals(IntegerSerializer.class, serializer.getClass());
 }

 @Test
 public void testIntegerSerializerSerialize() throws JsonProcessingException, JsonGenerationException {
 Integer input = 123;
 String output = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);
 JsonParser parser = objectMapper.getFactory().createParser(output);
 int actualInt = parser.nextIntValue();
 assertEquals(input, actualInt);
 }

 @Test
 public void testIntLikeSerializer() throws JsonProcessingException, JsonGenerationException {
 Number input = 123;
 String output = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);
 JsonParser parser = objectMapper.getFactory().createParser(output);
 int actualInt = parser.nextIntValue();
 assertEquals(input.intValue(), actualInt);
 }

 @Test
 public void testIntLikeSerializerAcceptJsonFormatVisitor() throws JsonProcessingException {
 Number input = 123;
 JsonNode expectedSchema = objectMapper.typeFactory.constructObjectType(JsonType.Std.NUMBER, "root");
 JsonNode actualSchema = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(input).getSchema();
 assertEquals(expectedSchema.toString(), actualSchema.toString());
 }

 @Test
 public void testIntLikeSerializerCreateContextual() throws JsonProcessingException {
 JsonNode inputNode = objectMapper.createObjectNode();
 inputNode.put("foo", 123);
 ObjectMapper mapper = new ObjectMapper();
 BeanProperty property = mapper.findProperty("foo", inputNode);
 JsonSerializer<?> serializer = objectMapper.findValueSerializer(property.getType(), null);
 assertEquals(IntLikeSerializer.class, serializer.getClass());
 }

 @Test
 public void testIntLikeSerializerSerialize() throws JsonProcessingException, JsonGenerationException {
 Number input = 123;
 String output = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);
 JsonParser parser = objectMapper.getFactory().createParser(output);
 int actualInt = parser.nextIntValue();
 assertEquals(input.intValue(), actualInt);
 }

 @Test
 public void testLongSerializer() throws JsonProcessingException, JsonGenerationException {
 Long input = 123L;
 String output = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);
 JsonParser parser = objectMapper.getFactory().createParser(output);
 long expectedLong = 123L;
 long actualLong = parser.nextLongValue();
 assertEquals(expectedLong, actualLong);
 }

 @Test
 public void testLongSerializerAcceptJsonFormatVisitor() throws JsonProcessingException {
 Long input = 123L;
 JsonNode expectedSchema = objectMapper.typeFactory.constructObjectType(JsonType.Std.LONG, "root");
 JsonNode actualSchema = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(input).getSchema();
 assertEquals(expectedSchema.toString(), actualSchema.toString());
 }

 @Test
 public void testLongSerializerCreateContextual() throws JsonProcessingException {
 JsonNode inputNode = objectMapper.createObjectNode();
 inputNode.put("foo", 123L);
 ObjectMapper mapper = new ObjectMapper();
 BeanProperty property = mapper.findProperty("foo", inputNode);
 JsonSerializer<?> serializer = objectMapper.findValueSerializer(property.getType(), null);
 assertEquals(LongSerializer.class, serializer.getClass());
 }

 @Test
 public void testLongSerializerSerialize() throws JsonProcessingException, JsonGenerationException {
 Long input = 123L;
 String output = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);
 JsonParser parser = objectMapper.getFactory().createParser(output);
 long actualLong = parser.nextLongValue();
 assertEquals(input, actualLong);
 }

 @Test
 public void testFloatSerializer() throws JsonProcessingException, JsonGenerationException {
 Float input = 123.45f;
 String output = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);
 JsonParser parser = objectMapper.getFactory().createParser(output);
 float expectedFloat = 123.45f;
 float actualFloat = parser.nextFloatValue();
 assertEquals(expectedFloat, actualFloat, 0.001f);
 }

 @Test
 public void testFloatSerializerAcceptJsonFormatVisitor() throws JsonProcessingException {
 Float input = 123.45f;
 JsonNode expectedSchema = objectMapper.typeFactory.constructObjectType(JsonType.Std.FLOAT, "root");
 JsonNode actualSchema = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(input).getSchema();
 assertEquals(expectedSchema.toString(), actualSchema.toString());
 }

 @Test
 public void testFloatSerializerCreateContextual() throws JsonProcessingException {
 JsonNode inputNode = objectMapper.createObjectNode();
 inputNode.put("foo", 123.45f);
 ObjectMapper mapper = new ObjectMapper();
 BeanProperty property = mapper.findProperty("foo", inputNode);
 JsonSerializer<?> serializer = objectMapper.findValueSerializer(property.getType(), null);
 assertEquals(FloatSerializer.class, serializer.getClass());
 }

 @Test
 public void testFloatSerializerSerialize() throws JsonProcessingException, JsonGenerationException {
 Float input = 123.45f;
 String output = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);
 JsonParser parser = objectMapper.getFactory().createParser(output);
 float actualFloat = parser.nextFloatValue();
 assertEquals(input, actualFloat, 0.001f);
 }

 @Test
 public void testDoubleSerializer() throws JsonProcessingException, JsonGenerationException {
 Double input = 123.45;
 String output = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);
 JsonParser parser = objectMapper.getFactory().createParser(output);
 double expectedDouble = 123.45;
 double actualDouble = parser.nextDoubleValue();
 assertEquals(expectedDouble, actualDouble, 0.001);
 }

 @Test
 public void testDoubleSerializerAcceptJsonFormatVisitor() throws JsonProcessingException {
 Double input = 123.45;
 JsonNode expectedSchema = objectMapper.typeFactory.constructObjectType(JsonType.Std.DOUBLE, "root");
 JsonNode actualSchema = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(input).getSchema();
 assertEquals(expectedSchema.toString(), actualSchema.toString());
 }

 @Test
 public void testDoubleSerializerCreateContextual() throws JsonProcessingException {
 JsonNode inputNode = objectMapper.createObjectNode();
 inputNode.put("foo", 123.45);
 ObjectMapper mapper = new ObjectMapper();
 BeanProperty property = mapper.findProperty("foo", inputNode);
 JsonSerializer<?> serializer = objectMapper.findValueSerializer(property.getType(), null);
 assertEquals(DoubleSerializer.class, serializer.getClass());
 }

 @Test
 public void testDoubleSerializerSerialize() throws JsonProcessingException, JsonGenerationException {
 Double input = 123.45;
 String output = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);
 JsonParser parser = objectMapper.getFactory().createParser(output);
 double actualDouble = parser.nextDoubleValue();
 assertEquals(input, actualDouble, 0.001);
 }
}