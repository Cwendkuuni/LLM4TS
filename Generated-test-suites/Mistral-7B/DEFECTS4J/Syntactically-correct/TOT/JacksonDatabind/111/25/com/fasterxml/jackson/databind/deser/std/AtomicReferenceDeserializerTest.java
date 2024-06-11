import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AtomicReferenceDeserializerTest {

 private ObjectMapper mapper;
 private AtomicReferenceDeserializer deserializer;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 deserializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>(){}, null, null, null);
 }

 @Test
 public void testGetNullValue() throws Exception {
 AtomicReference<Object> expected = new AtomicReference<Object>();
 AtomicReference<Object> actual = deserializer.getNullValue(mapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testGetEmptyValue() throws Exception {
 AtomicReference<Object> expected = new AtomicReference<Object>();
 AtomicReference<Object> actual = deserializer.getEmptyValue(mapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testReferenceValue() throws Exception {
 Object contents = new Object();
 AtomicReference<Object> expected = new AtomicReference<Object>(contents);
 AtomicReference<Object> actual = deserializer.referenceValue(contents);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetReferenced() throws Exception {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object actual = deserializer.getReferenced(reference);
 assertEquals(reference.get(), actual);
 }

 @Test
 public void testUpdateReference() throws Exception {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object newContents = new Object();
 AtomicReference<Object> updatedReference = deserializer.updateReference(reference, newContents);
 assertEquals(updatedReference, reference);
 assertEquals(newContents, reference.get());
 }

 @Test
 public void testSupportsUpdate() throws Exception {
 assertTrue(deserializer.supportsUpdate(mapper.getDeserializationConfig()));
 }

 @Test
 public void testDeserializeNull() throws Exception {
 String json = "null";
 ObjectNode node = mapper.readValue(json, ObjectNode.class);
 JsonDeserializer<Object> deserializer = mapper.findValueDeserializer(Object.class, node);
 AtomicReference<Object> actual = mapper.treeToValue(node, deserializer.getNullValue(mapper.getDeserializationContext()));
 AtomicReference<Object> expected = new AtomicReference<Object>();
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeEmptyObject() throws Exception {
 String json = "{}";
 ObjectNode node = mapper.readValue(json, ObjectNode.class);
 JsonDeserializer<Object> deserializer = mapper.findValueDeserializer(Object.class, node);
 AtomicReference<Object> actual = mapper.treeToValue(node, deserializer);
 AtomicReference<Object> expected = new AtomicReference<Object>();
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeWithContent() throws Exception {
 String json = "{\"value\": 123}";
 ObjectNode node = mapper.readValue(json, ObjectNode.class);
 JsonDeserializer<Object> deserializer = mapper.findValueDeserializer(Object.class, node);
 Object contents = new Integer(123);
 AtomicReference<Object> actual = mapper.treeToValue(node, deserializer);
 AtomicReference<Object> expected = new AtomicReference<Object>(contents);
 assertEquals(expected, actual);
 assertEquals(contents, actual.get());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithInvalidType() throws Exception {
 String json = "{\"value\": [1, 2, 3]}";
 ObjectNode node = mapper.readValue(json, ObjectNode.class);
 JsonDeserializer<Object> deserializer = mapper.findValueDeserializer(Object.class, node);
 AtomicReference<Object> actual = mapper.treeToValue(node, deserializer);
 fail("Expected JsonMappingException but got: " + actual);
 }
}