import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testBooleanType() throws JsonMappingException, IOException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "true";
 Object result = serializer.deserializeKey(key, ctxt);
 assertTrue((Boolean) result);
 key = "false";
 result = serializer.deserializeKey(key, ctxt);
 assertFalse((Boolean) result);
 }

 @Test
 public void testByteType() throws JsonMappingException, IOException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "10";
 Object result = serializer.deserializeKey(key, ctxt);
 assertEquals((Byte) 10, result);
 key = "128";
 try {
 result = serializer.deserializeKey(key, ctxt);
 fail("Expected IllegalArgumentException for byte type with value greater than 127");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testShortType() throws JsonMappingException, IOException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "100";
 Object result = serializer.deserializeKey(key, ctxt);
 assertEquals((Short) 100, result);
 key = "32000";
 try {
 result = serializer.deserializeKey(key, ctxt);
 fail("Expected IllegalArgumentException for short type with value greater than 32767");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testCharType() throws JsonMappingException, IOException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "A";
 Object result = serializer.deserializeKey(key, ctxt);
 assertEquals((Character) 'A', result);
 }

 @Test
 public void testIntType() throws JsonMappingException, IOException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "100";
 Object result = serializer.deserializeKey(key, ctxt);
 assertEquals((Integer) 100, result);
 }

 @Test
 public void testLongType() throws JsonMappingException, IOException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "1000000";
 Object result = serializer.deserializeKey(key, ctxt);
 assertEquals((Long) 1000000, result);
 }

 @Test
 public void testFloatType() throws JsonMappingException, IOException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "10.5";
 Object result = serializer.deserializeKey(key, ctxt);
 assertEquals((Float) 10.5f, result);
 }

 @Test
 public void testDoubleType() throws JsonMappingException, IOException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "10.5";
 Object result = serializer.deserializeKey(key, ctxt);
 assertEquals((Double) 10.5, result);
 }

 @Test
 public void testLocaleType() throws JsonMappingException, IOException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "en_US";
 Object result = serializer.deserializeKey(key, ctxt);
 assertEquals(Locale.US, result);
 }

 @Test
 public void testDateType() throws JsonMappingException, IOException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "2022-12-31";
 Object result = serializer.deserializeKey(key, ctxt);
 assertEquals(new Date(2022, Calendar.DECEMBER, 31), result);
 }

 @Test
 public void testCalendarType() throws JsonMappingException, IOException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "2022-12-31";
 Object result = serializer.deserializeKey(key, ctxt);
 Calendar expected = Calendar.getInstance();
 expected.set(Calendar.YEAR, 2022);
 expected.set(Calendar.MONTH, Calendar.DECEMBER);
 expected.set(Calendar.DAY_OF_MONTH, 31);
 assertEquals(expected, result);
 }

 @Test
 public void testUuidType() throws JsonMappingException, IOException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(UUID.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "123e4567-e89b-12d3-a456-426614174000";
 Object result = serializer.deserializeKey(key, ctxt);
 assertEquals(UUID.fromString(key), result);
 }

 @Test
 public void testUriType() throws JsonMappingException, IOException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "http://example.com";
 Object result = serializer.deserializeKey(key, ctxt);
 assertEquals(new URI(key), result);
 }

 @Test
 public void testUrlType() throws JsonMappingException, IOException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "http://example.com";
 Object result = serializer.deserializeKey(key, ctxt);
 assertEquals(new URL(key), result);
 }

 @Test
 public void testClassType() throws JsonMappingException, IOException, ClassNotFoundException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "java.lang.String";
 Object result = serializer.deserializeKey(key, ctxt);
 assertEquals(String.class, result.getClass());
 }

 @Test
 public void testUnknownType() throws JsonMappingException, IOException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "UnknownType";
 try {
 Object result = serializer.deserializeKey(key, ctxt);
 fail("Expected JsonMappingException for unknown type: " + key);
 } catch (JsonMappingException e) {
 // Expected
 }
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false); // for next test
 Object unknownObject = mapper.readValue("{\"key\":\"UnknownType\"}", Object.class);
 try {
 Object result = serializer.deserializeKey(unknownObject.toString(), ctxt);
 fail("Expected JsonMappingException for unknown type: " + unknownObject.getClass().getName());
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testNullKey() throws JsonMappingException, IOException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = serializer.deserializeKey(null, ctxt);
 assertNull(result);
 }
}