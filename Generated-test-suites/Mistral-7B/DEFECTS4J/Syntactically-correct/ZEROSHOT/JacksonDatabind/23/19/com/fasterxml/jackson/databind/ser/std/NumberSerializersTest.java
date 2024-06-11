import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testIntegerSerialization() throws JsonProcessingException {
 Integer integer = 123;
 ObjectNode result = mapper.writeValueAsTree(integer);
 JsonNode expected = mapper.valueToTree(123);
 assertEquals(expected, result);
 }

 @Test
 public void testIntegerFormatAnnotation() throws JsonProcessingException {
 Integer integer = 123;
 ObjectNode result = mapper.writeValueAsTree(integer);
 JsonNode expected = mapper.valueToTree("123");

 JsonFormat annotation = new JsonFormat();
 annotation.setShape(JsonFormat.Shape.STRING);
 ObjectNode node = mapper.createObjectNode();
 node.set("value", annotation);
 ObjectNode root = mapper.createObjectNode();
 root.set("number", node);
 ObjectNode expectedFormatNode = mapper.valueToTree(node);
 ObjectNode resultFormatNode = mapper.readTree(result.get("number").traverse()).deepGet("value");
 assertEquals(expectedFormatNode, resultFormatNode);
 }

 @Test
 public void testIntegerContextualSerialization() throws JsonProcessingException {
 ObjectNode root = mapper.createObjectNode();
 root.set("integer", mapper.valueToTree(456));
 ObjectNode result = mapper.writeValueAsTree(root);
 ObjectNode expected = mapper.valueToTree(456);
 assertEquals(expected, result);
 }

 @Test
 public void testLongSerialization() throws JsonProcessingException {
 Long longValue = 9876543210L;
 ObjectNode result = mapper.writeValueAsTree(longValue);
 JsonNode expected = mapper.valueToTree(9876543210L);
 assertEquals(expected, result);
 }

 @Test
 public void testFloatSerialization() throws JsonProcessingException {
 Float floatValue = 123.45f;
 ObjectNode result = mapper.writeValueAsTree(floatValue);
 JsonNode expected = mapper.valueToTree(123.45f);
 assertEquals(expected, result);
 }

 @Test
 public void testDoubleSerialization() throws JsonProcessingException {
 Double doubleValue = 123.45;
 ObjectNode result = mapper.writeValueAsTree(doubleValue);
 JsonNode expected = mapper.valueToTree(123.45);
 assertEquals(expected, result);
 }

 @Test
 public void testDoubleFormatAnnotation() throws JsonProcessingException {
 Double doubleValue = 123.45;
 ObjectNode result = mapper.writeValueAsTree(doubleValue);
 JsonNode expected = mapper.valueToTree("123.45");

 JsonFormat annotation = new JsonFormat();
 annotation.setShape(JsonFormat.Shape.STRING);
 ObjectNode node = mapper.createObjectNode();
 node.set("value", annotation);
 ObjectNode root = mapper.createObjectNode();
 root.set("number", node);
 ObjectNode expectedFormatNode = mapper.valueToTree(node);
 ObjectNode resultFormatNode = mapper.readTree(result.get("number").traverse()).deepGet("value");
 assertEquals(expectedFormatNode, resultFormatNode);
 }

 @Test
 public void testDoubleContextualSerialization() throws JsonProcessingException {
 ObjectNode root = mapper.createObjectNode();
 root.set("double", mapper.valueToTree(123.45));
 ObjectNode result = mapper.writeValueAsTree(root);
 ObjectNode expected = mapper.valueToTree(123.45);
 assertEquals(expected, result);
 }
}