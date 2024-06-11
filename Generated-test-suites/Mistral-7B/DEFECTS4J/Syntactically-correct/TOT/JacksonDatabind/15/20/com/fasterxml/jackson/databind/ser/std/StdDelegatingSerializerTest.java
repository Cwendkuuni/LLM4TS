import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import static org.junit.Assert.*;

public class StdDelegatingSerializerTest {

 private ObjectMapper mapper;
 private StdDelegatingSerializer serializer;

 @Before
 public void setup() throws JsonProcessingException {
 mapper = new ObjectMapper();
 Object converter = new SimpleTypeConverter(); // Assuming a simple Converter implementation is available
 JavaType delegateType = mapper.getTypeFactory().constructType(String.class);
 serializer = new StdDelegatingSerializer(converter, delegateType, mapper.findValueSerializer(String.class));
 }

 @Test
 public void testDelegateSerialization() throws IOException, JsonGenerationException {
 Object value = "Hello World";
 String expected = "\"Hello World\"";

 byte[] serialized = mapper.writeValueAsBytes(value);
 String serializedString = new String(serialized, 0, serialized.length);

 JsonNode actual = mapper.readTree(serializedString);
 assertEquals(expected, actual.asText());

 Object deserialized = mapper.readValue(serializedString, Object.class);
 assertEquals(value, deserialized);
 }

 @Test
 public void testNullValueSerialization() throws IOException {
 Object value = null;
 byte[] serialized = mapper.writeValueAsBytes(value);
 String serializedString = new String(serialized, 0, serialized.length);

 JsonNode actual = mapper.readTree(serializedString);
 assertTrue(actual.isNull());
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidTypeSerialization() throws JsonProcessingException {
 Object value = new Object();
 serializer = new StdDelegatingSerializer(null, null, null); // Invalid Converter implementation

 try {
 mapper.writeValueAsBytes(value, serializer);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }
}