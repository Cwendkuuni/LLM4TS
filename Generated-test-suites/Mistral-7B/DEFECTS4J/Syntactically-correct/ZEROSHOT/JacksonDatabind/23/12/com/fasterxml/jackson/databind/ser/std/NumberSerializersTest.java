import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new JsonMapper();
 }

 @Test
 public void testIntegerSerialization() throws JsonProcessingException {
 Integer number = 123;
 ObjectNode node = objectMapper.writeValueAsTree(number);
 JsonParser parser = objectMapper.getFactory().createParser(node.toString());
 int deserializedNumber = parser.readValueAs(int.class);
 assertEquals(number, deserializedNumber);
 }

 @Test
 public void testIntegerDeserialization() throws JsonProcessingException {
 JsonFactory factory = objectMapper.getFactory();
 ObjectNode node = factory.createObjectNode();
 node.put("number", 123);
 JsonParser parser = factory.createParser(node.toString());
 Number deserializedNumber = parser.readValueAs(Number.class);
 assertEquals(123, deserializedNumber.intValue());
 }

 @Test
 public void testLongSerialization() throws JsonProcessingException {
 Long number = 456L;
 ObjectNode node = objectMapper.writeValueAsTree(number);
 JsonParser parser = objectMapper.getFactory().createParser(node.toString());
 long deserializedNumber = parser.readValueAs(long.class);
 assertEquals(number, deserializedNumber);
 }

 @Test
 public void testLongDeserialization() throws JsonProcessingException {
 JsonFactory factory = objectMapper.getFactory();
 ObjectNode node = factory.createObjectNode();
 node.put("number", 456L);
 JsonParser parser = factory.createParser(node.toString());
 Number deserializedNumber = parser.readValueAs(Number.class);
 assertEquals(456L, deserializedNumber.longValue());
 }

 @Test
 public void testShortSerialization() throws JsonProcessingException {
 Short number = (short) 789;
 ObjectNode node = objectMapper.writeValueAsTree(number);
 JsonParser parser = objectMapper.getFactory().createParser(node.toString());
 short deserializedNumber = parser.readValueAs(short.class);
 assertEquals(number, deserializedNumber);
 }

 @Test
 public void testShortDeserialization() throws JsonProcessingException {
 JsonFactory factory = objectMapper.getFactory();
 ObjectNode node = factory.createObjectNode();
 node.put("number", (short) 789);
 JsonParser parser = factory.createParser(node.toString());
 Number deserializedNumber = parser.readValueAs(Number.class);
 assertEquals((short) 789, deserializedNumber.shortValue());
 }

 @Test
 public void testFloatSerialization() throws JsonProcessingException {
 float number = 1.23f;
 ObjectNode node = objectMapper.writeValueAsTree(number);
 JsonParser parser = objectMapper.getFactory().createParser(node.toString());
 float deserializedNumber = parser.readValueAs(float.class);
 assertEquals(number, deserializedNumber, 0.001f);
 }

 @Test
 public void testFloatDeserialization() throws JsonProcessingException {
 JsonFactory factory = objectMapper.getFactory();
 ObjectNode node = factory.createObjectNode();
 node.put("number", 1.23f);
 JsonParser parser = factory.createParser(node.toString());
 Number deserializedNumber = parser.readValueAs(Number.class);
 assertEquals(1.23f, deserializedNumber.floatValue(), 0.001f);
 }

 @Test
 public void testDoubleSerialization() throws JsonProcessingException {
 double number = 1.23;
 ObjectNode node = objectMapper.writeValueAsTree(number);
 JsonParser parser = objectMapper.getFactory().createParser(node.toString());
 double deserializedNumber = parser.readValueAs(double.class);
 assertEquals(number, deserializedNumber, 0.00001);
 }

 @Test
 public void testDoubleDeserialization() throws JsonProcessingException {
 JsonFactory factory = objectMapper.getFactory();
 ObjectNode node = factory.createObjectNode();
 node.put("number", 1.23);
 JsonParser parser = factory.createParser(node.toString());
 Number deserializedNumber = parser.readValueAs(Number.class);
 assertEquals(1.23, deserializedNumber.doubleValue(), 0.00001);
 }
}