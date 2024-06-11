import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testBooleanType() throws Exception {
 StdKeyDeserializer booleanDeserializer = StdKeyDeserializer.forType(boolean.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = "true";
 Object result = booleanDeserializer.deserializeKey(key, ctxt);
 assertTrue((Boolean) result);
 key = "false";
 result = booleanDeserializer.deserializeKey(key, ctxt);
 assertFalse((Boolean) result);
 }

 @Test
 public void testByteType() throws Exception {
 StdKeyDeserializer byteDeserializer = StdKeyDeserializer.forType(byte.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = "127";
 Object result = byteDeserializer.deserializeKey(key, ctxt);
 assertEquals((Byte) 127, result);
 key = "-128";
 result = byteDeserializer.deserializeKey(key, ctxt);
 assertEquals((Byte) -128, result);
 key = "256";
 try {
 result = byteDeserializer.deserializeKey(key, ctxt);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 assertTrue(e.getMessage().contains("overflow, value can not be represented as 8-bit value"));
 }
 }

 @Test
 public void testShortType() throws Exception {
 StdKeyDeserializer shortDeserializer = StdKeyDeserializer.forType(short.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = "32767";
 Object result = shortDeserializer.deserializeKey(key, ctxt);
 assertEquals((Short) 32767, result);
 key = "-32768";
 result = shortDeserializer.deserializeKey(key, ctxt);
 assertEquals((Short) -32768, result);
 key = "65536";
 try {
 result = shortDeserializer.deserializeKey(key, ctxt);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 assertTrue(e.getMessage().contains("overflow, value can not be represented as 16-bit value"));
 }
 }

 @Test
 public void testCharType() throws Exception {
 StdKeyDeserializer charDeserializer = StdKeyDeserializer.forType(char.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = "A";
 Object result = charDeserializer.deserializeKey(key, ctxt);
 assertEquals((Character) 'A', result);
 key = "😃";
 Object result2 = charDeserializer.deserializeKey(key, ctxt);
 assertEquals((Character) '\uD83D\uDE00', result2);
 }

 @Test
 public void testIntType() throws Exception {
 StdKeyDeserializer intDeserializer = StdKeyDeserializer.forType(int.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = "123456";
 Object result = intDeserializer.deserializeKey(key, ctxt);
 assertEquals((Integer) 123456, result);
 }

 @Test
 public void testLongType() throws Exception {
 StdKeyDeserializer longDeserializer = StdKeyDeserializer.forType(long.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = "12345678901234567890";
 Object result = longDeserializer.deserializeKey(key, ctxt);
 assertEquals((Long) 12345678901234567890L, result);
 }

 @Test
 public void testFloatType() throws Exception {
 StdKeyDeserializer floatDeserializer = StdKeyDeserializer.forType(float.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 String key = "123.45";
 Object result = floatDeserializer.deserializeKey(key, ctxt);
 assertEquals((Float) 123.45f, result, 0.001F);
 }

 @Test
 public void testDoubleType() throws Exception {
 StdKeyDeserializer doubleDeserializer = StdKeyDeserializer.forType(double.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 String key = "123.45";
 Object result = doubleDeserializer.deserializeKey(key, ctxt);
 assertEquals((Double) 123.45, result, 0.001);
 }

 @Test
 public void testLocaleType() throws Exception {
 StdKeyDeserializer localeDeserializer = StdKeyDeserializer.forType(Locale.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = "fr_FR";
 Object result = localeDeserializer.deserializeKey(key, ctxt);
 assertEquals(new Locale("fr", "FR"), result);
 }

 @Test
 public void testDateType() throws Exception {
 StdKeyDeserializer dateDeserializer = StdKeyDeserializer.forType(Date.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = "2022-12-31";
 Object result = dateDeserializer.deserializeKey(key, ctxt);
 assertEquals(new Date(Long.parseLong("1671139200000")), result);
 }

 @Test
 public void testCalendarType() throws Exception {
 StdKeyDeserializer calendarDeserializer = StdKeyDeserializer.forType(Calendar.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = "2022-12-31";
 Object result = calendarDeserializer.deserializeKey(key, ctxt);
 assertEquals(Calendar.getInstance().getTime(), ((Calendar) result).getTime());
 }

 @Test
 public void testURIType() throws Exception {
 StdKeyDeserializer uriDeserializer = StdKeyDeserializer.forType(URI.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = "https://www.google.com";
 Object result = uriDeserializer.deserializeKey(key, ctxt);
 assertEquals(new URI(key), result);
 }

 @Test
 public void testURLType() throws Exception {
 StdKeyDeserializer urlDeserializer = StdKeyDeserializer.forType(URL.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = "https://www.google.com";
 Object result = urlDeserializer.deserializeKey(key, ctxt);
 assertEquals(new URL(key), result);
 }

 @Test
 public void testClassType() throws Exception {
 StdKeyDeserializer classDeserializer = StdKeyDeserializer.forType(String.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = "java.lang.String";
 Object result = classDeserializer.deserializeKey(key, ctxt);
 assertEquals(String.class, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testUnknownType() throws Exception {
 StdKeyDeserializer unknownDeserializer = StdKeyDeserializer.forType(BigInteger.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = "12345678901234567890";
 Object result = unknownDeserializer.deserializeKey(key, ctxt);
 fail("Expected JsonMappingException");
 }

 @Test(expected = IOException.class)
 public void testNullKey() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = null;
 Object result = deserializer.deserializeKey(key, ctxt);
 assertNull(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidKey() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String key = "invalid_key";
 Object result = deserializer.deserializeKey(key, ctxt);
 fail("Expected IllegalArgumentException");
 }
}