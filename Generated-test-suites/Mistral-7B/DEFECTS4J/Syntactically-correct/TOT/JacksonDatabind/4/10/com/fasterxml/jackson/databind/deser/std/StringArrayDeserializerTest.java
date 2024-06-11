import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

 @Test
 public void testDeserializeEmptyArray() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 StringJsonNode jsonEmptyArray = mapper.readValue("[]", StringJsonNode.class);
 String[] result = mapper.readerForUpdating(new String[0]).readValue(jsonEmptyArray);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeNullArray() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 StringJsonNode jsonNullArray = mapper.readValue("null", StringJsonNode.class);
 String[] result = mapper.readerForUpdating(new String[0]).readValue(jsonNullArray);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeSingleElementArray() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 StringJsonNode jsonSingleElement = mapper.readValue("[\"" + "test" + "\"]", StringJsonNode.class);
 String[] result = mapper.readerForUpdating(new String[0]).readValue(jsonSingleElement);
 assertEquals("test", result[0]);
 }

 @Test
 public void testDeserializeArrayWithNullElement() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 StringJsonNode jsonArrayWithNull = mapper.readValue("[\"" + "test" + "\", null]", StringJsonNode.class);
 String[] result = mapper.readerForUpdating(new String[0]).readValue(jsonArrayWithNull);
 assertEquals("test", result[0]);
 assertNull(result[1]);
 }

 @Test
 public void testDeserializeArrayWithCustomElementDeserializer() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 JsonDeserializer<String> customDeserializer = new StandardDeserializer<String>(String.class) {
 @Override
 public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 return "custom";
 }
 };
 StringArrayDeserializer deserializerWithCustomElementDeserializer = new StringArrayDeserializer(customDeserializer);
 StringJsonNode jsonArrayWithCustomElement = mapper.readValue("[\"" + "test" + "\", \"custom\"]", StringJsonNode.class);
 String[] result = deserializerWithCustomElementDeserializer.deserialize(mapper.getFactory().createParser(jsonArrayWithCustomElement), null);
 assertEquals("test", result[0]);
 assertEquals("custom", result[1]);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJsonArray() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 StringJsonNode jsonInvalidArray = mapper.readValue("{\"test\": \"test\"}", StringJsonNode.class);
 try {
 String[] result = mapper.readerForUpdating(new String[0]).readValue(jsonInvalidArray);
 fail("Should have thrown JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test(expected = IOException.class)
 public void testDeserializeEmptyInputStream() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 try (InputStream inputStream = new ByteArrayInputStream(new byte[0])) {
 String[] result = mapper.readerForUpdating(new String[0]).readValue(mapper.getFactory().createParser(inputStream));
 fail("Should have thrown IOException");
 }
 }
}