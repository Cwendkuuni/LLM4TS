import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testDeserializeEmptyArray() throws IOException {
 String[] result = objectMapper.readValue("[]", String[].class);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeSingleValueArray() throws IOException {
 String[] result = objectMapper.readValue("[\"hello\"]", String[].class);
 assertArrayEquals(new String[]{"hello"}, result);
 }

 @Test
 public void testDeserializeNullArray() throws IOException {
 String[] result = objectMapper.readValue("null", String[].class);
 assertNull(result);
 }

 @Test
 public void testDeserializeArrayWithCustomDeserializer() throws IOException {
 JsonDeserializer<String> customDeserializer = new StdDeserializer<String>(String.class) {
 @Override
 public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 return jp.getValueAsString();
 }
 };
 StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
 ObjectMapper mapper = new ObjectMapper();
 mapper.setContextInitializer(new AbstractTypeValueContextInitializer() {
 @Override
 public void initContextInitializer(DeserializationContext ctxt) {
 ctxt.setActiveContext(deserializer);
 }
 });
 String[] result = mapper.readValue("[\"one\", \"two\", \"three\"]", String[].class);
 assertArrayEquals(new String[]{"one", "two", "three"}, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeInvalidJson() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"invalid\": \"json\"}";
 mapper.readValue(json, String[].class);
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeMissingEndArrayToken() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "[\"one\",";
 mapper.readValue(json, String[].class);
 }

 @Test
 public void testDeserializeEmptyStringAsNull() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
 String[] result = mapper.readValue("[]", String[].class);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeSingleEmptyStringAsNull() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
 String[] result = mapper.readValue("[\"]", String[].class);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeSingleEmptyStringAsNullWithFeatureEnabled() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 String[] result = mapper.readValue("[\"]", String[].class);
 assertArrayEquals(new String[0], result);
 mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
 mapper.readTree("{}"); // clear cache
 String[] result2 = mapper.readValue("[\"]", String[].class);
 assertNull(result2);
 }
}