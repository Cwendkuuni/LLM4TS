import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.EnumResolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

public class StdKeyDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testBooleanType() throws IOException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String key = "true";
 Object result = stdKeyDeserializer.deserializeKey(key, context);
 Assert.assertEquals(Boolean.TRUE, result);
 key = "false";
 result = stdKeyDeserializer.deserializeKey(key, context);
 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testByteType() throws IOException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Byte.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String key = "5";
 Object result = stdKeyDeserializer.deserializeKey(key, context);
 Assert.assertEquals((byte) 5, result);
 key = "-127";
 result = stdKeyDeserializer.deserializeKey(key, context);
 Assert.assertEquals((byte) -127, result);
 key = "256";
 Assert.assertThrows(IOException.class, () -> stdKeyDeserializer.deserializeKey(key, context));
 }

 @Test
 public void testShortType() throws IOException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Short.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String key = "10";
 Object result = stdKeyDeserializer.deserializeKey(key, context);
 Assert.assertEquals((short) 10, result);
 key = "-32768";
 result = stdKeyDeserializer.deserializeKey(key, context);
 Assert.assertEquals((short) -32768, result);
 key = "32768";
 Assert.assertThrows(IOException.class, () -> stdKeyDeserializer.deserializeKey(key, context));
 }

 @Test
 public void testCharType() throws IOException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Character.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String key = "A";
 Object result = stdKeyDeserializer.deserializeKey(key, context);
 Assert.assertEquals('A', result);
 key = "\\u00E9";
 result = stdKeyDeserializer.deserializeKey(key, context);
 Assert.assertEquals('\u00E9', result);
 key = "strlen";
 Assert.assertThrows(IOException.class, () -> stdKeyDeserializer.deserializeKey(key, context));
 }

 @Test
 public void testIntType() throws IOException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Integer.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String key = "100";
 Object result = stdKeyDeserializer.deserializeKey(key, context);
 Assert.assertEquals(100, result);
 key = "Integer.MIN_VALUE";
 result = stdKeyDeserializer.deserializeKey(key, context);
 Assert.assertEquals(Integer.MIN_VALUE, result);
 key = "Integer.MAX_VALUE";
 Assert.assertThrows(IOException.class, () -> stdKeyDeserializer.deserializeKey(key, context));
 }

 @Test
 public void testLongType() throws IOException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Long.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String key = "1000L";
 Object result = stdKeyDeserializer.deserializeKey(key, context);
 Assert.assertEquals(1000L, result);
 key = "Long.MIN_VALUE";
 result = stdKeyDeserializer.deserializeKey(key, context);
 Assert.assertEquals(Long.MIN_VALUE, result);
 key = "Long.MAX_VALUE";
 Assert.assertThrows(IOException.class, () -> stdKeyDeserializer.deserializeKey(key, context));
 }

 @Test
 public void testFloatType() throws IOException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Float.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String key = "10.5";
 Object result = stdKeyDeserializer.deserializeKey(key, context);
 Assert.assertEquals(10.5f, result, 0.01f);
 key = "Float.NaN";
 result = stdKeyDeserializer.deserializeKey(key, context);
 Assert.assertEquals(Float.NaN, result, 0.0f);
 key = "Float.POSITIVE_INFINITY";
 result = stdKeyDeserializer.deserializeKey(key, context);
 Assert.assertEquals(Float.POSITIVE_INFINITY, result, 0.0f);
 key = "Float.NEGATIVE_INFINITY";
 Assert.assertThrows(IOException.class, () -> stdKeyDeserializer.deserializeKey(key, context));
 }

 @Test
 public void testDoubleType() throws IOException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Double.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String key = "10.5";
 Object result = stdKeyDeserializer.deserializeKey(key, context);
 Assert.assertEquals(10.5, result, 0.01);
 key = "Double.NaN";
 result = stdKeyDeserializer.deserializeKey(key, context);
 Assert.assertEquals(Double.NaN, result, 0.0);
 key = "Double.POSITIVE_INFINITY";
 result = stdKeyDeserializer.deserializeKey(key, context);
 Assert.assertEquals(Double.POSITIVE_INFINITY, result, 0.0);
 key = "Double.NEGATIVE_INFINITY";
 Assert.assertThrows(IOException.class, () -> stdKeyDeserializer.deserializeKey(key, context));
 }

 @Test
 public void testLocaleType() throws IOException, IllegalArgumentException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Locale.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String key = "fr_FR";
 Object result = stdKeyDeserializer.deserializeKey(key, context);
 Assert.assertEquals(new Locale("fr", "FR"), result);
 key = "UnknownLocale";
 Assert.assertThrows(IllegalArgumentException.class, () -> stdKeyDeserializer.deserializeKey(key, context));
 }

 @Test
 public void testDateType() throws IOException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Date.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String key = "2023-03-14";
 Object result = stdKeyDeserializer.deserializeKey(key, context);
 Assert.assertEquals(new Date(1678860800000L), result);
 key = "InvalidDateFormat";
 Assert.assertThrows(IOException.class, () -> stdKeyDeserializer.deserializeKey(key, context));
 }

 @Test
 public void testCalendarType() throws IOException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Calendar.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String key = "2023-03-14";
 Object result = stdKeyDeserializer.deserializeKey(key, context);
 Calendar calendar = (Calendar) result;
 Assert.assertEquals(2023, calendar.get(Calendar.YEAR));
 Assert.assertEquals(3, calendar.get(Calendar.MONTH));
 Assert.assertEquals(14, calendar.get(Calendar.DAY_OF_MONTH));
 key = "InvalidDateFormat";
 Assert.assertThrows(IOException.class, () -> stdKeyDeserializer.deserializeKey(key, context));
 }

 @Test
 public void testUUIDType() throws IOException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(UUID.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String key = "6ba7b810-9dad-11d1-80b4-00c04fd430c8";
 Object result = stdKeyDeserializer.deserializeKey(key, context);
 Assert.assertEquals(UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8"), result);
 key = "InvalidUUIDFormat";
 Assert.assertThrows(IOException.class, () -> stdKeyDeserializer.deserializeKey(key, context));
 }

 @Test
 public void testURIType() throws IOException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(URI.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String key = "http://example.com";
 Object result = stdKeyDeserializer.deserializeKey(key, context);
 Assert.assertEquals(new URI("http://example.com"), result);
 key = "InvalidURIFormat";
 Assert.assertThrows(IOException.class, () -> stdKeyDeserializer.deserializeKey(key, context));
 }

 @Test
 public void testURLType() throws IOException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(URL.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String key = "http://example.com";
 Object result = stdKeyDeserializer.deserializeKey(key, context);
 Assert.assertEquals(new URL("http://example.com"), result);
 key = "InvalidURLFormat";
 Assert.assertThrows(MalformedURLException.class, () -> stdKeyDeserializer.deserializeKey(key, context));
 }

 @Test
 public void testEnumType() throws IOException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(MyEnum.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String key = "ONE";
 Object result = stdKeyDeserializer.deserializeKey(key, context);
 Assert.assertEquals(MyEnum.ONE, result);
 key = "TWO";
 result = stdKeyDeserializer.deserializeKey(key, context);
 Assert.assertEquals(MyEnum.TWO, result);
 key = "THREE";
 Assert.assertThrows(IOException.class, () -> stdKeyDeserializer.deserializeKey(key, context));
 }

 @Test
 public void testCustomClassType() throws IOException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(MyCustomClass.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String key = "MyCustomClass{name='Test', value=123}";
 Object result = stdKeyDeserializer.deserializeKey(key, context);
 MyCustomClass myCustomClass = (MyCustomClass) result;
 Assert.assertEquals("Test", myCustomClass.getName());
 Assert.assertEquals(123, myCustomClass.getValue());
 key = "InvalidCustomClassFormat";
 Assert.assertThrows(IOException.class, () -> stdKeyDeserializer.deserializeKey(key, context));
 }

 @Test
 public void testNullKey() throws IOException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(String.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String key = null;
 Object result = stdKeyDeserializer.deserializeKey(key, context);
 Assert.assertNull(result);
 }
}