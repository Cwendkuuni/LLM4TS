import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testIntegerSerializer() throws Exception {
 Integer value = 123;
 String json = mapper.writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 int actual = parser.readValueAs(Integer.class);
 assertEquals(value, actual);
 }

 @Test
 public void testIntegerSerializerContextual() throws Exception {
 Integer value = 123;
 AnnotatedClass ac = new AnnotatedClass(Integer.class, new JsonFormat.Value());
 JsonSerializer<Integer> serializer = new NumberSerializers().createContextual(mapper.getSerializationConfig(), ac);
 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), mapper.getSerializationConfig());
 serializer.serialize(value, gen, mapper.getSerializationConfig(), null);
 JsonParser parser = mapper.getFactory().createParser(gen.getOutputBuffer().toString());
 int actual = parser.readValueAs(Integer.class);
 assertEquals(value, actual);
 }

 @Test
 public void testIntegerSerializerVisitors() throws Exception {
 Integer value = 123;
 JsonNode tree = mapper.readTree("{\"number\": " + value + "}");
 JsonNode numberNode = tree.get("number");
 JsonNode actualNode = numberNode.deepCopy();
 JsonNode expectedNode = mapper.readTree("{\"number\":{\"@type\":\"number\",\"numberType\":\"INT\"}}");
 assertEquals(expectedNode, actualNode);
 }

 @Test
 public void testLongSerializer() throws Exception {
 Long value = 123L;
 String json = mapper.writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 Long actual = parser.readValueAs(Long.class);
 assertEquals(value, actual);
 }

 @Test
 public void testLongSerializerContextual() throws Exception {
 Long value = 123L;
 AnnotatedClass ac = new AnnotatedClass(Long.class, new JsonFormat.Value());
 JsonSerializer<Long> serializer = new NumberSerializers().createContextual(mapper.getSerializationConfig(), ac);
 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), mapper.getSerializationConfig());
 serializer.serialize(value, gen, mapper.getSerializationConfig(), null);
 JsonParser parser = mapper.getFactory().createParser(gen.getOutputBuffer().toString());
 Long actual = parser.readValueAs(Long.class);
 assertEquals(value, actual);
 }

 @Test
 public void testLongSerializerVisitors() throws Exception {
 Long value = 123L;
 String json = mapper.writeValueAsString(value);
 JsonNode tree = mapper.readTree(json);
 JsonNode numberNode = tree.get("number");
 JsonNode actualNode = numberNode.deepCopy();
 JsonNode expectedNode = mapper.readTree("{\"number\":{\"@type\":\"number\",\"numberType\":\"LONG\"}}");
 assertEquals(expectedNode, actualNode);
 }

 @Test
 public void testFloatSerializer() throws Exception {
 Float value = 123.45f;
 String json = mapper.writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 Float actual = parser.readValueAs(Float.class);
 assertEquals(value, actual, 0.001f);
 }

 @Test
 public void testFloatSerializerContextual() throws Exception {
 Float value = 123.45f;
 AnnotatedClass ac = new AnnotatedClass(Float.class, new JsonFormat.Value());
 JsonSerializer<Float> serializer = new NumberSerializers().createContextual(mapper.getSerializationConfig(), ac);
 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), mapper.getSerializationConfig());
 serializer.serialize(value, gen, mapper.getSerializationConfig(), null);
 JsonParser parser = mapper.getFactory().createParser(gen.getOutputBuffer().toString());
 Float actual = parser.readValueAs(Float.class);
 assertEquals(value, actual, 0.001f);
 }

 @Test
 public void testFloatSerializerVisitors() throws Exception {
 Float value = 123.45f;
 String json = mapper.writeValueAsString(value);
 JsonNode tree = mapper.readTree(json);
 JsonNode numberNode = tree.get("number");
 JsonNode actualNode = numberNode.deepCopy();
 JsonNode expectedNode = mapper.readTree("{\"number\":{\"@type\":\"number\",\"numberType\":\"FLOAT\"}}");
 assertEquals(expectedNode, actualNode);
 }

 @Test
 public void testDoubleSerializer() throws Exception {
 Double value = 123.45;
 String json = mapper.writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 Double actual = parser.readValueAs(Double.class);
 assertEquals(value, actual, 0.001);
 }

 @Test
 public void testDoubleSerializerContextual() throws Exception {
 Double value = 123.45;
 AnnotatedClass ac = new AnnotatedClass(Double.class, new JsonFormat.Value());
 JsonSerializer<Double> serializer = new NumberSerializers().createContextual(mapper.getSerializationConfig(), ac);
 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), mapper.getSerializationConfig());
 serializer.serialize(value, gen, mapper.getSerializationConfig(), null);
 JsonParser parser = mapper.getFactory().createParser(gen.getOutputBuffer().toString());
 Double actual = parser.readValueAs(Double.class);
 assertEquals(value, actual, 0.001);
 }

 @Test
 public void testDoubleSerializerVisitors() throws Exception {
 Double value = 123.45;
 String json = mapper.writeValueAsString(value);
 JsonNode tree = mapper.readTree(json);
 JsonNode numberNode = tree.get("number");
 JsonNode actualNode = numberNode.deepCopy();
 JsonNode expectedNode = mapper.readTree("{\"number\":{\"@type\":\"number\",\"numberType\":\"DOUBLE\"}}");
 assertEquals(expectedNode, actualNode);
 }
}