import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testFileDeserialization() throws Exception {
 File file = new File("test.txt");
 FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
 String jsonString = "\"test.txt\"";
 File deserializedObject = deserializer.deserialize(mapper.getFactory().createParser(jsonString), new TestContext());
 assertEquals(file, deserializedObject);
 }

 @Test
 public void testURLDeserialization() throws Exception {
 URL url = new URL("http://example.com");
 FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
 String jsonString = "\"http://example.com\"";
 URL deserializedObject = deserializer.deserialize(mapper.getFactory().createParser(jsonString), new TestContext());
 assertEquals(url, deserializedObject);
 }

 @Test
 public void testDeserializationWithEmptyString() throws Exception {
 FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class);
 String jsonString = "\"\"";
 String deserializedObject = deserializer.deserialize(mapper.getFactory().createParser(jsonString), new TestContext());
 assertNull(deserializedObject);
 }

 @Test
 public void testDeserializationWithNull() throws Exception {
 FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class);
 String jsonString = "null";
 String deserializedObject = deserializer.deserialize(mapper.getFactory().createParser(jsonString), new TestContext());
 assertNull(deserializedObject);
 }

 @Test
 public void testDeserializationWithInvalidFormat() throws Exception {
 FromStringDeserializer<Integer> deserializer = new FromStringDeserializer<Integer>(Integer.class);
 String jsonString = "\"abcd\"";
 try {
 deserializer.deserialize(mapper.getFactory().createParser(jsonString), new TestContext());
 fail();
 } catch (com.fasterxml.jackson.databind.JsonMappingException e) {
 assertEquals("not a valid number", e.getMessage());
 }
 }

 @Test
 public void testDeserializationWithUnexpectedToken() throws Exception {
 FromStringDeserializer<Integer> deserializer = new FromStringDeserializer<Integer>(Integer.class);
 String jsonString = "[1, 2]";
 try {
 deserializer.deserialize(mapper.getFactory().createParser(jsonString), new TestContext());
 fail();
 } catch (com.fasterxml.jackson.databind.JsonMappingException e) {
 assertEquals("Attempted to unwrap single value array for single 'Integer' value but there was more than a single value in the array", e.getMessage());
 }
}

 @Test
 public void testDeserializationWithEmbeddedObject() throws Exception {
 Object obj = new Object();
 FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
 String jsonString = "{\"class\":\"java.lang.Object\", \"value\": {\"class\":\"java.lang.String\", \"value\": \"test\"}}";
 Object deserializedObject = deserializer.deserialize(mapper.getFactory().createParser(jsonString), new TestContext());
 assertEquals(obj, deserializedObject);
 assertEquals("test", deserializer.deserializeEmbedded(deserializedObject, new TestContext()).getValueAsString());
}

 @Test
 public void testDeserializationWithInvalidEmbeddedObjectType() throws Exception {
 Object obj = new Object();
 FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
 String jsonString = "{\"class\":\"java.util.Date\", \"value\": \"2022-01-01\"}";
 try {
 deserializer.deserializeEmbedded(obj, new TestContext());
 fail();
 } catch (com.fasterxml.jackson.databind.JsonMappingException e) {
 assertEquals("Don't know how to convert embedded Object of type java.util.Date into java.lang.Object", e.getMessage());
 }
}

 @Test
 public void testDeserializationWithEmptyEmbeddedObject() throws Exception {
 Object obj = new Object();
 FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
 String jsonString = "{\"class\":\"java.lang.String\", \"value\": \"\"}";
 Object deserializedObject = deserializer.deserializeEmbedded(obj, new TestContext());
 assertEquals("", deserializedObject.getValueAsString());
 }

 class TestContext extends DeserializationContext {
 @Override
 public DeserializationFeature getDeserializationFeature(DeserializationFeature feature) {
 return DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS;
 }

 @Override
 public void failContextual(String msg, JsonParser p) throws IOException {
 fail(msg);
 }
 }

}