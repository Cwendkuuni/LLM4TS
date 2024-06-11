import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
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
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer integer = 1;
 JsonNode actual = objectMapper.writeValueAsTree(integer);
 JsonNode expected = objectMapper.createObjectNode().putNumber("1");
 assertEquals(expected, actual);
 }

 @Test
 public void testIntegerSerializerWithType() throws JsonProcessingException {
 Integer integer = 1;
 ObjectWriter writer = objectMapper.writer().withTypeSerializer(Integer.class, new TypeSerializer() {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeNumber(((Integer) value).intValue());
 }

 @Override
 public void deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
 ctxt.handleUnknownValue(p);
 }
 });
 JsonNode actual = writer.writeValueAsTree(integer);
 JsonNode expected = objectMapper.createObjectNode().putNumber("1");
 assertEquals(expected, actual);
 }

 @Test
 public void testIntegerSerializerContextual() throws JsonProcessingException {
 ObjectWriter writer = objectMapper.writer().withView(MyNumberView.class);
 Integer integer = 1;
 JsonNode actual = writer.writeValueAsTree(integer);
 JsonNode expected = objectMapper.createObjectNode().putNumber("1");
 assertEquals(expected, actual);
 }

 @Test
 public void testIntegerSerializerContextualWithType() throws JsonProcessingException {
 ObjectWriter writer = objectMapper.writer().withView(MyNumberView.class);
 Integer integer = 1;
 ObjectWriter contextualWriter = objectMapper.writer().withTypeSerializer(Integer.class, new TypeSerializer() {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeNumber(((Integer) value).intValue());
 }

 @Override
 public void deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
 ctxt.handleUnknownValue(p);
 }
 });
 JsonNode actual = contextualWriter.writeValueAsTree(integer);
 JsonNode expected = objectMapper.createObjectNode().putNumber("1");
 assertEquals(expected, actual);
 }

 @JsonFormat(shape = JsonFormat.Shape.NUMBER)
 static class MyNumberView {
 }

 @Test
 public void testLongSerializer() throws JsonProcessingException {
 Long longValue = 1L;
 JsonNode actual = objectMapper.writeValueAsTree(longValue);
 JsonNode expected = objectMapper.createObjectNode().putNumber("1");
 assertEquals(expected, actual);
 }

 @Test
 public void testLongSerializerWithType() throws JsonProcessingException {
 Long longValue = 1L;
 ObjectWriter writer = objectMapper.writer().withTypeSerializer(Long.class, new TypeSerializer() {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeNumber(((Long) value).longValue());
 }

 @Override
 public void deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
 ctxt.handleUnknownValue(p);
 }
 });
 JsonNode actual = writer.writeValueAsTree(longValue);
 JsonNode expected = objectMapper.createObjectNode().putNumber("1");
 assertEquals(expected, actual);
 }

 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 Float floatValue = 1.0f;
 JsonNode actual = objectMapper.writeValueAsTree(floatValue);
 JsonNode expected = objectMapper.createObjectNode().putNumber("1.0");
 assertEquals(expected, actual);
 }

 @Test
 public void testFloatSerializerWithType() throws JsonProcessingException {
 Float floatValue = 1.0f;
 ObjectWriter writer = objectMapper.writer().withTypeSerializer(Float.class, new TypeSerializer() {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeNumber(((Float) value).floatValue());
 }

 @Override
 public void deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
 ctxt.handleUnknownValue(p);
 }
 });
 JsonNode actual = writer.writeValueAsTree(floatValue);
 JsonNode expected = objectMapper.createObjectNode().putNumber("1.0");
 assertEquals(expected, actual);
 }

 @Test
 public void testDoubleSerializer() throws JsonProcessingException {
 Double doubleValue = 1.0;
 JsonNode actual = objectMapper.writeValueAsTree(doubleValue);
 JsonNode expected = objectMapper.createObjectNode().putNumber("1.0");
 assertEquals(expected, actual);
 }

 @Test
 public void testDoubleSerializerWithType() throws JsonProcessingException {
 Double doubleValue = 1.0;
 ObjectWriter writer = objectMapper.writer().withTypeSerializer(Double.class, new TypeSerializer() {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeNumber(((Double) value).doubleValue());
 }

 @Override
 public void deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
 ctxt.handleUnknownValue(p);
 }
 });
 JsonNode actual = writer.writeValueAsTree(doubleValue);
 JsonNode expected = objectMapper.createObjectNode().putNumber("1.0");
 assertEquals(expected, actual);
 }
}