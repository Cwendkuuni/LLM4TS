import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testBooleanType() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext ctx = mapper.getDeserializationContext();
 String key = "true";
 Object result = serializer.deserializeKey(key, ctx);
 assertTrue((Boolean) result);
 key = "false";
 result = serializer.deserializeKey(key, ctx);
 assertFalse((Boolean) result);
 }

 @Test
 public void testByteType() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
 DeserializationContext ctx = mapper.getDeserializationContext();
 String key = "10";
 Object result = serializer.deserializeKey(key, ctx);
 assertEquals((Byte) 10, result);
 key = "127";
 result = serializer.deserializeKey(key, ctx);
 assertEquals((Byte) 127, result);
 key = "-128";
 result = serializer.deserializeKey(key, ctx);
 assertEquals((Byte) -128, result);
 key = "129"; // out of range
 assertNull(serializer.deserializeKey(key, ctx));
 }

 @Test
 public void testShortType() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
 DeserializationContext ctx = mapper.getDeserializationContext();
 String key = "100";
 Object result = serializer.deserializeKey(key, ctx);
 assertEquals((Short) 100, result);
 key = "32767";
 result = serializer.deserializeKey(key, ctx);
 assertEquals((Short) 32767, result);
 key = "-32768";
 result = serializer.deserializeKey(key, ctx);
 assertEquals((Short) -32768, result);
 key = "32769"; // out of range
 assertNull(serializer.deserializeKey(key, ctx));
 }

 @Test
 public void testCharType() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
 DeserializationContext ctx = mapper.getDeserializationContext();
 String key = "A";
 Object result = serializer.deserializeKey(key, ctx);
 assertEquals((Character) 'A', result);
 key = "Z";
 result = serializer.deserializeKey(key, ctx);
 assertEquals((Character) 'Z', result);
 key = "ç"; // out of ASCII range
 assertNull(serializer.deserializeKey(key, ctx));
 }

 @Test
 public void testIntType() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
 DeserializationContext ctx = mapper.getDeserializationContext();
 String key = "100";
 Object result = serializer.deserializeKey(key, ctx);
 assertEquals((Integer) 100, result);
 }

 @Test(expected = JsonProcessingException.class)
 public void testIntTypeWithInvalidKey() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
 DeserializationContext ctx = mapper.getDeserializationContext();
 String key = "abcd";
 serializer.deserializeKey(key, ctx);
 }

 @Test
 public void testLongType() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
 DeserializationContext ctx = mapper.getDeserializationContext();
 String key = "1000000000";
 Object result = serializer.deserializeKey(key, ctx);
 assertEquals((Long) 1000000000L, result);
 }

 @Test(expected = JsonProcessingException.class)
 public void testLongTypeWithInvalidKey() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
 DeserializationContext ctx = mapper.getDeserializationContext();
 String key = "abcd";
 serializer.deserializeKey(key, ctx);
 }

 @Test
 public void testFloatType() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
 DeserializationContext ctx = mapper.getDeserializationContext();
 String key = "1.23";
 Object result = serializer.deserializeKey(key, ctx);
 assertEquals((Float) 1.23f, result);
 }

 @Test(expected = JsonProcessingException.class)
 public void testFloatTypeWithInvalidKey() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
 DeserializationContext ctx = mapper.getDeserializationContext();
 String key = "abcd";
 serializer.deserializeKey(key, ctx);
 }

 @Test
 public void testDoubleType() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
 DeserializationContext ctx = mapper.getDeserializationContext();
 String key = "1.23";
 Object result = serializer.deserializeKey(key, ctx);
 assertEquals((Double) 1.23, result);
 }

 @Test(expected = JsonProcessingException.class)
 public void testDoubleTypeWithInvalidKey() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
 DeserializationContext ctx = mapper.getDeserializationContext();
 String key = "abcd";
 serializer.deserializeKey(key, ctx);
 }

 @Test
 public void testLocaleType() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
 DeserializationContext ctx = mapper.getDeserializationContext();
 String key = "en_US";
 Object result = serializer.deserializeKey(key, ctx);
 assertEquals(Locale.US, result);
 }

 @Test(expected = JsonProcessingException.class)
 public void testLocaleTypeWithInvalidKey() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
 DeserializationContext ctx = mapper.getDeserializationContext();
 String key = "abcd";
 serializer.deserializeKey(key, ctx);
 }

 @Test
 public void testDateType() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);
 DeserializationContext ctx = mapper.getDeserializationContext();
 String key = "2021-12-31";
 Object result = serializer.deserializeKey(key, ctx);
 assertEquals(new Date(2021, 11, 31), result);
 }

 @Test
 public void testCalendarType() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);
 DeserializationContext ctx = mapper.getDeserializationContext();
 String key = "2021-12-31";
 Object result = serializer.deserializeKey(key, ctx);
 GregorianCalendar gregorianCalendar = new GregorianCalendar();
 gregorianCalendar.setTime(new Date(2021, 11, 31));
 assertEquals(gregorianCalendar, result);
 }

 @Test
 public void testUUIDType() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(UUID.class);
 DeserializationContext ctx = mapper.getDeserializationContext();
 String key = "123e4567-e89b-12d3-a456-426614174000";
 Object result = serializer.deserializeKey(key, ctx);
 assertEquals(UUID.fromString(key), result);
 }

 @Test(expected = JsonProcessingException.class)
 public void testUUIDTypeWithInvalidKey() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(UUID.class);
 DeserializationContext ctx = mapper.getDeserializationContext();
 String key = "abcd";
 serializer.deserializeKey(key, ctx);
 }

 @Test
 public void testURIType() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
 DeserializationContext ctx = mapper.getDeserializationContext();
 String key = "http://example.com";
 Object result = serializer.deserializeKey(key, ctx);
 assertEquals(new URI(key), result);
 }

 @Test(expected = JsonProcessingException.class)
 public void testURITypeWithInvalidKey() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
 DeserializationContext ctx = mapper.getDeserializationContext();
 String key = "abcd";
 serializer.deserializeKey(key, ctx);
 }

 @Test
 public void testURLType() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
 DeserializationContext ctx = mapper.getDeserializationContext();
 String key = "http://example.com";
 Object result = serializer.deserializeKey(key, ctx);
 assertEquals(new URL(key), result);
 }

 @Test(expected = MalformedURLException.class)
 public void testURLTypeWithInvalidKey() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
 DeserializationContext ctx = mapper.getDeserializationContext();
 String key = "abcd";
 serializer.deserializeKey(key, ctx);
 }

 @Test
 public void testClassType() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
 DeserializationContext ctx = mapper.getDeserializationContext();
 String key = "java.lang.String";
 Object result = serializer.deserializeKey(key, ctx);
 assertEquals(String.class, result.getClass());
 }

 @Test(expected = JsonProcessingException.class)
 public void testClassTypeWithInvalidKey() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
 DeserializationContext ctx = mapper.getDeserializationContext();
 String key = "abcd";
 serializer.deserializeKey(key, ctx);
 }

 @Test
 public void testEnumType() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Language.class);
 DeserializationContext ctx = mapper.getDeserializationContext();
 String key = "ENGLISH";
 Object result = serializer.deserializeKey(key, ctx);
 assertEquals(Language.ENGLISH, result);
 }

 @Test(expected = JsonProcessingException.class)
 public void testEnumTypeWithInvalidKey() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Language.class);
 DeserializationContext ctx = mapper.getDeserializationContext();
 String key = "ABCD";
 serializer.deserializeKey(key, ctx);
 }
}