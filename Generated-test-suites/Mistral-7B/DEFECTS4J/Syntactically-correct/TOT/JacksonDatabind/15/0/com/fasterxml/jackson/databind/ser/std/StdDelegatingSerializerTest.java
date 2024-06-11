import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StdDelegatingSerializerTest {

 private ObjectMapper mapper;
 private StdDelegatingSerializer serializer;
 private Converter<String, Integer> converter;

 @BeforeAll
 public void setup() {
 mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 converter = mapper.getTypeFactory().constructConverter(String.class, Integer.class);
 serializer = new StdDelegatingSerializer(converter);
 }

 @Test
 public void testNullValue() throws JsonGenerationException, JsonMappingException {
 ObjectNode result = mapper.writeValueAsTree(serializer, null);
 assertNull(result.get(""));
 }

 @Test
 public void testSimpleStringToInteger() throws JsonGenerationException, JsonMappingException {
 ObjectNode result = mapper.writeValueAsTree(serializer, "123");
 assertEquals(123, result.get("").asInt());
 }

 @Test
 public void testNegativeStringToInteger() throws JsonGenerationException, JsonMappingException {
 ObjectNode result = mapper.writeValueAsTree(serializer, "-123");
 assertEquals(-123, result.get("").asInt());
 }

 @Test
 public void testIntegerToObject() throws JsonGenerationException, JsonMappingException {
 ObjectNode input = mapper.createObjectNode();
 input.put("value", 123);
 ObjectNode result = mapper.writeValueAsTree(serializer, input);
 assertEquals(123, result.get("value").asInt());
 }

 @Test
 public void testMultipleLevelsConversion() throws JsonGenerationException, JsonMappingException {
 ObjectNode input = mapper.createObjectNode();
 input.put("value", "456");
 ObjectNode nestedInput = mapper.createObjectNode();
 nestedInput.put("value", "789");
 input.set("nested", nestedInput);
 ObjectNode result = mapper.writeValueAsTree(serializer, input);
 assertEquals(789, result.path("nested").get("value").asInt());
 }

 @Test
 public void testInvalidTypeConversion() throws JsonGenerationException, JsonMappingException {
 ObjectNode input = mapper.createObjectNode();
 input.put("value", new Object());
 ObjectNode result = mapper.writeValueAsTree(serializer, input);
 assertNotNull(result.get("value"));
 assertNotSame(Integer.class, result.get("value").getClass());
 }

 @Test
 public void testDelegateSerializerIsUsed() throws JsonGenerationException, JsonMappingException {
 JsonSerializer<String> stringSerializer = mapper.findValueSerializer(String.class);
 ObjectNode input = mapper.createObjectNode();
 input.put("value", "123");
 ObjectNode result = mapper.writeValueAsTree(stringSerializer, input);
 assertNotNull(result.get("value"));
 assertSame(String.class, result.get("value").getClass());
 ObjectNode serializerResult = mapper.writeValueAsTree(serializer, input);
 ObjectNode delegateResult = mapper.readTree(serializerResult.get("value").asText());
 assertEquals(123, delegateResult.get("value").asInt());
 assertNotSame(String.class, delegateResult.get("value").getClass());
 }

 @AfterAll
 public void teardown() throws Exception {
 mapper.close();
 }

}