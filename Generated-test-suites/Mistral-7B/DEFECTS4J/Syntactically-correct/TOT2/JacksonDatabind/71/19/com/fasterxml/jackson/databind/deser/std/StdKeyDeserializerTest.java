import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testBooleanDeserialization() throws JsonProcessingException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 ObjectNode node = mapper.createObjectNode();
 node.put("key", "true");
 Object result = serializer.deserializeKey("key", ctxt);
 assertTrue(result instanceof Boolean);
 assertEquals((Boolean) result, Boolean.TRUE);
 }

 @Test
 public void testBooleanDeserialization_InvalidInput() throws JsonProcessingException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 ObjectNode node = mapper.createObjectNode();
 node.put("key", "invalid");
 try {
 serializer.deserializeKey("key", ctxt);
 fail();
 } catch (JsonMappingException e) {
 assertEquals(JsonMappingException.ClassTypeMismatch.class, e.getClass());
 }
 }

 @Test
 public void testByteDeserialization() throws JsonProcessingException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 ObjectNode node = mapper.createObjectNode();
 node.put("key", "10");
 Object result = serializer.deserializeKey("key", ctxt);
 assertTrue(result instanceof Byte);
 assertEquals((Byte) result, Byte.decode("10"));
 }

 @Test
 public void testByteDeserialization_InvalidInput() throws JsonProcessingException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 ObjectNode node = mapper.createObjectNode();
 node.put("key", "256");
 try {
 serializer.deserializeKey("key", ctxt);
 fail();
 } catch (JsonMappingException e) {
 assertEquals(JsonMappingException.ValueToBigInteger.class, e.getClass());
 }
 }

 // ... You can add more test cases for other data types and edge cases here

 @Test
 public void testDeserializationWithDelegate() throws JsonProcessingException {
 StdKeyDeserializer serializer = new DelegatingKD(Locale.class, mapper.getDeserializationContext().findValueDeserializer(Locale.class));
 Object result = serializer.deserializeKey("en_US", mapper.getDeserializationContext());
 assertTrue(result instanceof Locale);
 assertEquals(Locale.US, result);
 }

 @Test
 public void testDeserializationWithDelegate_InvalidInput() throws JsonProcessingException {
 StdKeyDeserializer serializer = new DelegatingKD(Locale.class, mapper.getDeserializationContext().findValueDeserializer(Locale.class));
 ObjectNode node = mapper.createObjectNode();
 node.put("key", "invalid_locale");
 try {
 serializer.deserializeKey("key", mapper.getDeserializationContext());
 fail();
 } catch (JsonMappingException e) {
 assertEquals(JsonMappingException.ValueToInt.class, e.getClass());
 }
 }

 @Test
 public void testDeserializationWithEnumResolver() throws JsonProcessingException {
 StdKeyDeserializer serializer = new EnumKD(mapper.getAnnotationIntrospector().findValueAnnotations(StdKeyDeserializer.class, null), mapper.findValueDeserializer(String.class));
 Object result = serializer.deserializeKey("VALUE_ONE", mapper.getDeserializationContext());
 assertTrue(result instanceof Enum);
 assertEquals(EnumOne.VALUE_ONE, result);
 }

 @Test
 public void testDeserializationWithEnumResolver_InvalidInput() throws JsonProcessingException {
 StdKeyDeserializer serializer = new EnumKD(mapper.getAnnotationIntrospector().findValueAnnotations(StdKeyDeserializer.class, null), mapper.findValueDeserializer(String.class));
 ObjectNode node = mapper.createObjectNode();
 node.put("key", "INVALID_VALUE");
 try {
 serializer.deserializeKey("key", mapper.getDeserializationContext());
 fail();
 } catch (JsonMappingException e) {
 assertEquals(JsonMappingException.ValueToInt.class, e.getClass());
 }
 }

 // ... You can add more test cases for other key deserializers here

}