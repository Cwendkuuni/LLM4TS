import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class POJONodeTest {

 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 objectMapper = new ObjectMapper();
 }

 @After
 public void tearDown() {
 objectMapper = null;
 }

 @Test
 public void testPOJONodeCreation() {
 POJONode pojoNode = new POJONode(new Object());
 assertNotNull(pojoNode);
 assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
 }

 @Test
 public void testPOJONodeSerialization() throws JsonProcessingException {
 Object pojo = new Object();
 POJONode pojoNode = new POJONode(pojo);
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 pojoNode.serialize(gen, objectMapper.getSerializerProvider());
 gen.flush();
 assertFalse(System.out.toString().isEmpty());
 }

 @Test
 public void testPOJONodeSerializationWithJsonSerializable() throws JsonProcessingException {
 MyJsonSerializable pojo = new MyJsonSerializable();
 POJONode pojoNode = new POJONode(pojo);
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 pojoNode.serialize(gen, objectMapper.getSerializerProvider());
 gen.flush();
 assertFalse(System.out.toString().isEmpty());
 }

 @Test
 public void testPOJONodeSerializationWithoutJsonSerializable() throws JsonProcessingException {
 String pojoString = "{\"name\":\"John\",\"age\":30}";
 POJONode pojoNode = new POJONode(objectMapper.readValue(pojoString, Object.class));
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 pojoNode.serialize(gen, objectMapper.getSerializerProvider());
 gen.flush();
 assertFalse(System.out.toString().isEmpty());
 }

 @Test
 public void testPOJONodeEquals() {
 Object pojo1 = new Object();
 Object pojo2 = new Object();
 POJONode node1 = new POJONode(pojo1);
 POJONode node2 = new POJONode(pojo2);
 assertFalse(node1.equals(node2));
 }

 @Test
 public void testPOJONodeEqualsSameInstance() {
 Object pojo = new Object();
 POJONode node = new POJONode(pojo);
 assertTrue(node.equals(node));
 }

 @Test
 public void testPOJONodeEqualsWithSameValue() {
 Object pojo = new Object();
 POJONode node1 = new POJONode(pojo);
 POJONode node2 = new POJONode(pojo);
 assertTrue(node1.equals(node2));
 }

 @Test
 public void testPOJONodeHashCode() {
 Object pojo = new Object();
 POJONode node = new POJONode(pojo);
 int hashCode1 = node.hashCode();
 Object pojo2 = new Object();
 POJONode node2 = new POJONode(pojo2);
 int hashCode2 = node2.hashCode();
 assertSame(hashCode1, hashCode2);
 }

 @Test
 public void testPOJONodeToString() {
 Object pojo = new Object();
 POJONode node = new POJONode(pojo);
 assertEquals("{" + pojo.getClass().getName() + "}", node.toString());
 }

 @Test
 public void testPOJONodeBinaryValue() throws IOException {
 byte[] binaryValue = new byte[10];
 POJONode node = new POJONode(binaryValue);
 assertSame(binaryValue, node.binaryValue());
 }

 @Test(expected = IOException.class)
 public void testPOJONodeBinaryValueFromNonByteArray() throws IOException {
 String nonByteArray = "abc";
 POJONode node = new POJONode(nonByteArray);
 node.binaryValue();
 }

 @Test
 public void testPOJONodeAsText() {
 Object pojo = new Object();
 POJONode node = new POJONode(pojo);
 assertEquals("{" + pojo.getClass().getName() + "}", node.asText());
 }

 @Test
 public void testPOJONodeAsTextWithDefaultValue() {
 Object pojo = null;
 POJONode node = new POJONode(pojo);
 assertEquals("null", node.asText("defaultValue"));
 }

 @Test
 public void testPOJONodeAsBoolean() {
 Object booleanPojo = new Boolean(true);
 POJONode node = new POJONode(booleanPojo);
 assertTrue(node.asBoolean(false));
 }

 @Test
 public void testPOJONodeAsBooleanWithDefaultValue() {
 Object nonBooleanPojo = new Object();
 POJONode node = new POJONode(nonBooleanPojo);
 assertFalse(node.asBoolean(true));
 }

 @Test
 public void testPOJONodeAsInt() {
 Object intPojo = new Integer(10);
 POJONode node = new POJONode(intPojo);
 assertEquals(10, node.asInt(0));
 }

 @Test
 public void testPOJONodeAsIntWithDefaultValue() {
 Object nonIntPojo = new Object();
 POJONode node = new POJONode(nonIntPojo);
 assertEquals(0, node.asInt(10));
 }

 @Test
 public void testPOJONodeAsLong() {
 Object longPojo = new Long(100);
 POJONode node = new POJONode(longPojo);
 assertEquals(100L, node.asLong(0L));
 }

 @Test
 public void testPOJONodeAsLongWithDefaultValue() {
 Object nonLongPojo = new Object();
 POJONode node = new POJONode(nonLongPojo);
 assertEquals(0L, node.asLong(100L));
 }

 @Test
 public void testPOJONodeAsDouble() {
 Object doublePojo = new Double(10.5);
 POJONode node = new POJONode(doublePojo);
 assertEquals(10.5, node.asDouble(0.0), 0.001);
 }

 @Test
 public void testPOJONodeAsDoubleWithDefaultValue() {
 Object nonDoublePojo = new Object();
 POJONode node = new POJONode(nonDoublePojo);
 assertEquals(0.0, node.asDouble(10.5), 0.001);
 }

 @Test
 public void testPOJONodeGetPojo() {
 Object pojo = new Object();
 POJONode node = new POJONode(pojo);
 assertSame(pojo, node.getPojo());
 }
}

class MyJsonSerializable implements JsonSerializable {
 @Override
 public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
 JsonSerializer<MyJsonSerializable> serializer = serializers.findValueSerializer(MyJsonSerializable.class, null);
 serializer.serialize(this, gen, serializers);
 }
}