import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

public class StdKeyDeserializerTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testBooleanType() throws IOException, JsonMappingException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = "true";
 Object result = serializer.deserializeKey(key, ctxt);
 assertNotNull(result);
 assertEquals(Boolean.TRUE, result);
 key = "false";
 result = serializer.deserializeKey(key, ctxt);
 assertNotNull(result);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testByteType() throws IOException, JsonMappingException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = "127";
 Object result = serializer.deserializeKey(key, ctxt);
 assertNotNull(result);
 assertEquals((byte) 127, result);
 try {
 key = "128";
 result = serializer.deserializeKey(key, ctxt);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testShortType() throws IOException, JsonMappingException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = "32767";
 Object result = serializer.deserializeKey(key, ctxt);
 assertNotNull(result);
 assertEquals((short) 32767, result);
 try {
 key = "32768";
 result = serializer.deserializeKey(key, ctxt);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testCharType() throws IOException, JsonMappingException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = "A";
 Object result = serializer.deserializeKey(key, ctxt);
 assertNotNull(result);
 assertEquals('A', result);
 }

 @Test
 public void testIntType() throws IOException, JsonMappingException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = "12345";
 Object result = serializer.deserializeKey(key, ctxt);
 assertNotNull(result);
 assertEquals(12345, result);
 }

 @Test
 public void testLongType() throws IOException, JsonMappingException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = "12345678901234567890";
 Object result = serializer.deserializeKey(key, ctxt);
 assertNotNull(result);
 assertEquals(12345678901234567890L, result);
 }

 @Test
 public void testLocaleType() throws IOException, JsonMappingException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = "en_US";
 Object result = serializer.deserializeKey(key, ctxt);
 assertNotNull(result);
 assertEquals(Locale.US, result);
 }

 @Test
 public void testDateType() throws IOException, JsonMappingException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = "2022-12-31";
 Object result = serializer.deserializeKey(key, ctxt);
 assertNotNull(result);
 assertTrue(key.equals(ctxt.getDateFormatInstance().format(result)));
 }

 @Test
 public void testCalendarType() throws IOException, JsonMappingException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = "2022-12-31";
 Object result = serializer.deserializeKey(key, ctxt);
 assertNotNull(result);
 assertEquals(Calendar.getInstance().getTime(), result.getTime());
 }

 @Test
 public void testUUIDType() throws IOException, JsonMappingException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(UUID.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = "6ba7b810-9dad-11d1-80b4-00c04fd430c8";
 Object result = serializer.deserializeKey(key, ctxt);
 assertNotNull(result);
 assertEquals(UUID.fromString(key), result);
 }

 @Test
 public void testURIType() throws IOException, JsonMappingException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = "https://www.example.com";
 Object result = serializer.deserializeKey(key, ctxt);
 assertNotNull(result);
 assertEquals(new URI(key), result);
 }

 @Test
 public void testURLType() throws IOException, JsonMappingException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = "https://www.example.com";
 Object result = serializer.deserializeKey(key, ctxt);
 assertNotNull(result);
 assertEquals(new URL(key), result);
 }

 @Test
 public void testClassType() throws IOException, JsonMappingException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = "java.util.Locale";
 Object result = serializer.deserializeKey(key, ctxt);
 assertNotNull(result);
 assertEquals(Locale.class, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidType() throws IOException, JsonMappingException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(BigDecimal.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = "123.45";
 Object result = serializer.deserializeKey(key, ctxt);
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidKeyForEnumType() throws IOException, JsonMappingException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = "fr_FR"; // valid for Locale, but StdKeyDeserializer doesn't know that
 Object result = serializer.deserializeKey(key, ctxt);
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidKeyForEnumTypeWithoutToStringResolver() throws IOException, JsonMappingException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.disable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
 String key = "fr_FR"; // valid for Locale, but StdKeyDeserializer doesn't know that
 Object result = serializer.deserializeKey(key, ctxt);
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidKeyForUnknownEnumValue() throws IOException, JsonMappingException {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = "invalid_locale";
 Object result = serializer.deserializeKey(key, ctxt);
 fail("Expected JsonMappingException");
 }
}