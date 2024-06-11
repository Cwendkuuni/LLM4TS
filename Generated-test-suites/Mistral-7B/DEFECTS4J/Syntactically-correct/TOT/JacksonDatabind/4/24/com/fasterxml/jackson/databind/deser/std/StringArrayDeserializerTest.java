import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import org.junit.Assert;
import org.junit.Test;

public class StringArrayDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testDeserializeEmptyArray() throws IOException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jsonParser = jsonFactory.createParser("[]");

 String[] result = deserializer.deserialize(jsonParser, MAPPER.getDeserializationContext());
 assert ArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeSingleElementArray() throws IOException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jsonParser = jsonFactory.createParser("[\", \"foo\"]");

 String[] result = deserializer.deserialize(jsonParser, MAPper.getDeserializationContext());
 assert ArrayEquals(new String[] {"", "foo"}, result);
 }

 @Test
 public void testDeserializeNullElementArray() throws IOException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jsonParser = jsonFactory.createParser("[\nnull\n]");

 String[] result = deserializer.deserialize(jsonParser, MAPper.getDeserializationContext());
 assert ArrayEquals(new String[] {null}, result);
 }

 @Test
 public void testDeserializeArrayWithCustomDeserializer() throws IOException {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(new CustomStringDeserializer());
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jsonParser = jsonFactory.createParser("[\n123, 456\n]");

 String[] result = deserializer.deserialize(jsonParser, MAPper.getDeserializationContext());
 assert ArrayEquals(new String[] {"123", "456"}, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeNonArray() throws IOException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jsonParser = jsonFactory.createParser("{\"key\": \"value\"}");

 String[] result = deserializer.deserialize(jsonParser, MAPper.getDeserializationContext());
 // This test should throw an IOException as StringArrayDeserializer does not support deserializing non-arrays
 }

 @Test(expected = IOException.class)
 public void testDeserializeNullInput() throws IOException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jsonParser = jsonFactory.createParser(null);

 String[] result = deserializer.deserialize(jsonParser, MAPper.getDeserializationContext());
 // This test should throw an IOException as JsonParser cannot be null
 }

 @Test
 public void testDeserializeArrayWithEmptyElements() throws IOException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jsonParser = jsonFactory.createParser("[\nnull,\n\"\",\n\"\"]");

 String[] result = deserializer.deserialize(jsonParser, MAPper.getDeserializationContext());
 assert ArrayEquals(new String[] {null, "", ""}, result);
 }

 @Test
 public void testDeserializeArrayWithCustomDeserializerAndEmptyElement() throws IOException {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(new CustomStringDeserializer());
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jsonParser = jsonFactory.createParser("[\nnull,\nnull,\n\"\"\"]");

 String[] result = deserializer.deserialize(jsonParser, MAPper.getDeserializationContext());
 assert ArrayEquals(new String[] {null, null, ""}, result);
 }

 @Test
 public void testDeserializeArrayWithCustomDeserializerAndNullElement() throws IOException {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(new CustomStringDeserializer());
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jsonParser = jsonFactory.createParser("[\nnull,\nnull,\nnull\"]");

 String[] result = deserializer.deserialize(jsonParser, MAPper.getDeserializationContext());
 assert ArrayEquals(new String[] {null, null, null}, result);
 }

 @Test
 public void testDeserializeArrayWithCustomDeserializerAndNonEmptyElements() throws IOException {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(new CustomStringDeserializer());
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jsonParser = jsonFactory.createParser("[\n123,\n456,\n789\"]");

 String[] result = deserializer.deserialize(jsonParser, MAPper.getDeserializationContext());
 assert ArrayEquals(new String[] {"123", "456", "789"}, result);
 }

 @Test
 public void testDeserializeArrayWithEmptyStringElements() throws IOException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jsonParser = jsonFactory.createParser("[\n\"\",\n\"\",\n\"\"\"]");

 String[] result = deserializer.deserialize(jsonParser, MAPper.getDeserializationContext());
 assert ArrayEquals(new String[] {"", "", ""}, result);
 }

 @Test
 public void testDeserializeArrayWithEmptyStringElementAndCustomDeserializer() throws IOException {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(new CustomStringDeserializer());
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jsonParser = jsonFactory.createParser("[\n\"\",\n\"\",\n\"\"\"]");

 String[] result = deserializer.deserialize(jsonParser, MAPper.getDeserializationContext());
 assert ArrayEquals(new String[] {"", "", ""}, result);
 }

 @Test
 public void testDeserializeArrayWithNullElementsAndCustomDeserializer() throws IOException {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(new CustomStringDeserializer());
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jsonParser = jsonFactory.createParser("[\nnull,\nnull,\nnull\"]");

 String[] result = deserializer.deserialize(jsonParser, MAPper.getDeserializationContext());
 assert ArrayEquals(new String[] {null, null, null}, result);
 }

 @Test
 public void testDeserializeArrayWithNullElementsNonEmptyElementsAndCustomDeserializer() throws IOException {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(new CustomStringDeserializer());
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jsonParser = jsonFactory.createParser("[\nnull,\n123,\nnull\"]");

 String[] result = deserializer.deserialize(jsonParser, MAPper.getDeserializationContext());
 assert ArrayEquals(new String[] {null, "123", null}, result);
 }

 @Test
 public void testDeserializeArrayWithNullElementsNonEmptyElementsAndEmptyStringElementAndCustomDeserializer() throws IOException {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(new CustomStringDeserializer());
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jsonParser = jsonFactory.createParser("[\nnull,\n123,\n\"\"\"]");

 String[] result = deserializer.deserialize(jsonParser, MAPper.getDeserializationContext());
 assert ArrayEquals(new String[] {null, "123", ""}, result);
 }

 @Test
 public void testDeserializeArrayWithNonEmptyElementsAndCustomDeserializer() throws IOException {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(new CustomStringDeserializer());
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jsonParser = jsonFactory.createParser("[\n123,\n123,\n123\"]");

 String[] result = deserializer.deserialize(jsonParser, MAPper.getDeserializationContext());
 assert ArrayEquals(new String[] {"123", "123", "123"}, result);
 }

 @Test
 public void testDeserializeArrayWithNonEmptyElementsAndEmptyStringElementAndCustomDeserializer() throws IOException {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(new CustomStringDeserializer());
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jsonParser = jsonFactory.createParser("[\n123,\n123,\n\"\"\"]");

 String[] result = deserializer.deserialize(jsonParser, MAPper.getDeserializationContext());
 assert ArrayEquals(new String[] {"123", "123", ""}, result);
 }

 @Test
 public void testDeserializeArrayWithNonEmptyElementsAndNullElementAndCustomDeserializer() throws IOException {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(new CustomStringDeserializer());
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jsonParser = jsonFactory.createParser("[\n123,\nnull,\n123\"]");

 String[] result = deserializer.deserialize(jsonParser, MAPper.getDeserializationContext());
 assert ArrayEquals(new String[] {"123", null, "123"}, result);
 }

 @Test
 public void testDeserializeArrayWithCustomDeserializerAndArrayWithNonEmptyElements() throws IOException {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(new CustomStringDeserializer());
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jsonParser = jsonFactory.createParser("[\n[\n123,\n123,\n123\n],\n[\n123,\n123,\n123\n]\n]");

 String[][] result = (String[][]) deserializer.deserializeWithType(jsonParser, MAPper.getDeserializationContext(), new TypeDeserializer() {});
 assert ArrayEquals(new String[][] {{"123", "123", "123"}, {"123", "123", "123"}}, result);
 }

 @Test
 public void testDeserializeArrayWithNullInputAndCustomDeserializer() throws IOException {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(new CustomStringDeserializer());
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jsonParser = jsonFactory.createParser(null);

 String[] result = deserializer.deserializeWithType(jsonParser, MAPper.getDeserializationContext(), new TypeDeserializer() {});
 // This test should throw an IOException as JsonParser cannot be null
 }

 private static void assertArrayEquals(Object expected, Object actual) {
 if (!expected.getClass().isArray()) {
 throw new RuntimeException("Expected is not an array: " + expected.getClass());
 }
 if (expected.getClass().getComponentType() != String.class) {
 throw new RuntimeException("Expected is not a String[]: " + expected.getClass());
 }
 if (!actual.getClass().isArray()) {
 throw new RuntimeException("Actual is not an array: " + actual.getClass());
 }
 if (actual.getClass().getComponentType() != String.class) {
 throw new RuntimeException("Actual is not a String[]: " + actual.getClass());
 }
 Assert.assertArrayEquals((String[]) expected, (String[]) actual);
 }
}

class CustomStringDeserializer extends StdDeserializer<String> {
 @Override
 public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 return jp.getValueAsString();
 }
}