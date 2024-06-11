import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

public class StdKeyDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testBooleanKeyDeserialization() throws IOException {
 String key = "true";
 StdKeyDeserializer booleanStdKeyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 Object result = booleanStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testBooleanKeyDeserializationWithInvalidKey() throws IOException {
 String key = "invalid";
 StdKeyDeserializer booleanStdKeyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 Object result = booleanStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertNull(result);
 }

 @Test
 public void testByteKeyDeserialization() throws IOException {
 String key = "127";
 StdKeyDeserializer byteStdKeyDeserializer = StdKeyDeserializer.forType(Byte.class);
 Object result = byteStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertEquals((byte) 127, result);
 }

 @Test
 public void testByteKeyDeserializationWithInvalidKey() throws IOException {
 String key = "256";
 StdKeyDeserializer byteStdKeyDeserializer = StdKeyDeserializer.forType(Byte.class);
 Object result = byteStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertNull(result);
 }

 @Test
 public void testShortKeyDeserialization() throws IOException {
 String key = "12345";
 StdKeyDeserializer shortStdKeyDeserializer = StdKeyDeserializer.forType(Short.class);
 Object result = shortStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertEquals((short) 12345, result);
 }

 @Test
 public void testShortKeyDeserializationWithInvalidKey() throws IOException {
 String key = "32000";
 StdKeyDeserializer shortStdKeyDeserializer = StdKeyDeserializer.forType(Short.class);
 Object result = shortStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertNull(result);
 }

 @Test
 public void testCharKeyDeserialization() throws IOException {
 String key = "A";
 StdKeyDeserializer charStdKeyDeserializer = StdKeyDeserializer.forType(Character.class);
 Object result = charStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertEquals('A', result);
 }

 @Test
 public void testCharKeyDeserializationWithInvalidKey() throws IOException {
 String key = "♥";
 StdKeyDeserializer charStdKeyDeserializer = StdKeyDeserializer.forType(Character.class);
 Object result = charStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertNull(result);
 }

 @Test
 public void testIntKeyDeserialization() throws IOException {
 String key = "123456";
 StdKeyDeserializer intStdKeyDeserializer = StdKeyDeserializer.forType(Integer.class);
 Object result = intStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertEquals(123456, result);
 }

 @Test
 public void testIntKeyDeserializationWithInvalidKey() throws IOException {
 String key = "123456789012345";
 StdKeyDeserializer intStdKeyDeserializer = StdKeyDeserializer.forType(Integer.class);
 Object result = intStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertNull(result);
 }

 @Test
 public void testLongKeyDeserialization() throws IOException {
 String key = "12345678901234567890";
 StdKeyDeserializer longStdKeyDeserializer = StdKeyDeserializer.forType(Long.class);
 Object result = longStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertEquals(12345678901234567890L, result);
 }

 @Test
 public void testLongKeyDeserializationWithInvalidKey() throws IOException {
 String key = "123456789012345678901234567890";
 StdKeyDeserializer longStdKeyDeserializer = StdKeyDeserializer.forType(Long.class);
 Object result = longStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertNull(result);
 }

 @Test
 public void testFloatKeyDeserialization() throws IOException {
 String key = "3.1415926535";
 StdKeyDeserializer floatStdKeyDeserializer = StdKeyDeserializer.forType(Float.class);
 Object result = floatStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertEquals(3.1415926535f, result, 0.001f);
 }

 @Test
 public void testFloatKeyDeserializationWithInvalidKey() throws IOException {
 String key = "3.1415926535abcd";
 StdKeyDeserializer floatStdKeyDeserializer = StdKeyDeserializer.forType(Float.class);
 Object result = floatStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertNull(result);
 }

 @Test
 public void testDoubleKeyDeserialization() throws IOException {
 String key = "3.14159265358979323846";
 StdKeyDeserializer doubleStdKeyDeserializer = StdKeyDeserializer.forType(Double.class);
 Object result = doubleStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertEquals(3.14159265358979323846, result, 0.000001);
 }

 @Test
 public void testDoubleKeyDeserializationWithInvalidKey() throws IOException {
 String key = "3.14159265358979323846abcd";
 StdKeyDeserializer doubleStdKeyDeserializer = StdKeyDeserializer.forType(Double.class);
 Object result = doubleStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertNull(result);
 }

 @Test
 public void testLocaleKeyDeserialization() throws IOException {
 String key = "en_US";
 StdKeyDeserializer localeStdKeyDeserializer = StdKeyDeserializer.forType(Locale.class);
 Object result = localeStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertEquals(Locale.US, result);
 }

 @Test
 public void testLocaleKeyDeserializationWithInvalidKey() throws IOException {
 String key = "es_ES_es";
 StdKeyDeserializer localeStdKeyDeserializer = StdKeyDeserializer.forType(Locale.class);
 Object result = localeStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertNull(result);
 }

 @Test
 public void testDateKeyDeserialization() throws IOException {
 String key = "2022-01-01";
 StdKeyDeserializer dateStdKeyDeserializer = StdKeyDeserializer.forType(Date.class);
 Object result = dateStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertNotNull(result);
 }

 @Test
 public void testDateKeyDeserializationWithInvalidFormat() throws IOException {
 String key = "2022-01-01T12:30:00";
 StdKeyDeserializer dateStdKeyDeserializer = StdKeyDeserializer.forType(Date.class);
 Object result = dateStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertNull(result);
 }

 @Test
 public void testCalendarKeyDeserialization() throws IOException {
 String key = "2022-01-01";
 StdKeyDeserializer calendarStdKeyDeserializer = StdKeyDeserializer.forType(Calendar.class);
 Object result = calendarStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertNotNull(result);
 }

 @Test
 public void testCalendarKeyDeserializationWithInvalidFormat() throws IOException {
 String key = "2022-01-01T12:30:00";
 StdKeyDeserializer calendarStdKeyDeserializer = StdKeyDeserializer.forType(Calendar.class);
 Object result = calendarStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertNull(result);
 }

 @Test
 public void testUUIDKeyDeserialization() throws IOException {
 String key = "00000000-0000-0000-0000-000000000000";
 StdKeyDeserializer uuidStdKeyDeserializer = StdKeyDeserializer.forType(UUID.class);
 Object result = uuidStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertEquals(UUID.fromString(key), result);
 }

 @Test
 public void testUUIDKeyDeserializationWithInvalidKey() throws IOException {
 String key = "00000000-0000-0000-0000-00000000000X";
 StdKeyDeserializer uuidStdKeyDeserializer = StdKeyDeserializer.forType(UUID.class);
 Object result = uuidStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertNull(result);
 }

 @Test
 public void testURIOBjectKeyDeserialization() throws IOException {
 String key = "https://www.example.com";
 StdKeyDeserializer uriStdKeyDeserializer = StdKeyDeserializer.forType(URI.class);
 Object result = uriStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertEquals(new URI(key), result);
 }

 @Test
 public void testURIOBjectKeyDeserializationWithInvalidFormat() throws IOException {
 String key = "https://www.example.com/path?query=123";
 StdKeyDeserializer uriStdKeyDeserializer = StdKeyDeserializer.forType(URI.class);
 Object result = uriStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertNull(result);
 }

 @Test
 public void testURLStringKeyDeserialization() throws IOException {
 String key = "https://www.example.com";
 StdKeyDeserializer urlStdKeyDeserializer = StdKeyDeserializer.forType(URL.class);
 Object result = urlStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertEquals(new URL(key), result);
 }

 @Test
 public void testURLStringKeyDeserializationWithInvalidFormat() throws IOException {
 String key = "https://www.example.com/path?query=123";
 StdKeyDeserializer urlStdKeyDeserializer = StdKeyDeserializer.forType(URL.class);
 Object result = urlStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertNull(result);
 }

 @Test
 public void testClassKeyDeserialization() throws IOException {
 String key = "java.util.Date";
 StdKeyDeserializer classStdKeyDeserializer = StdKeyDeserializer.forType(Class.class);
 Object result = classStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertEquals(Class.forName(key), result);
 }

 @Test
 public void testClassKeyDeserializationWithInvalidKey() throws IOException {
 String key = "java.util.DateX";
 StdKeyDeserializer classStdKeyDeserializer = StdKeyDeserializer.forType(Class.class);
 Object result = classStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertNull(result);
 }

 @Test
 public void testEnumKeyDeserialization() throws IOException {
 String key = "MONDAY";
 JavaType javaType = TypeFactory.defaultInstance().constructType(MyEnum.class);
 StdKeyDeserializer enumStdKeyDeserializer = StdKeyDeserializer.forType(MyEnum.class);
 Object result = enumStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertEquals(MyEnum.MONDAY, result);
 }

 @Test
 public void testEnumKeyDeserializationWithInvalidKey() throws IOException {
 String key = "TUESDAYX";
 JavaType javaType = TypeFactory.defaultInstance().constructType(MyEnum.class);
 StdKeyDeserializer enumStdKeyDeserializer = StdKeyDeserializer.forType(MyEnum.class);
 Object result = enumStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertNull(result);
 }

 @Test
 public void testCustomStringCtorKeyDeserialization() throws IOException {
 String key = "MyCustomClass(\"customKey\")";
 JavaType javaType = TypeFactory.defaultInstance().constructType(MyCustomClass.class);
 StdKeyDeserializer customStringCtorStdKeyDeserializer = new StringCtorKeyDeserializer(MyCustomClass.class.getConstructor(String.class));
 Object result = customStringCtorStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertEquals(new MyCustomClass("customKey"), result);
 }

 @Test
 public void testCustomStringFactoryKeyDeserialization() throws IOException {
 String key = "MyCustomClass.createInstance(\"customKey\")";
 JavaType javaType = TypeFactory.defaultInstance().constructType(MyCustomClass.class);
 StdKeyDeserializer customStringFactoryStdKeyDeserializer = new StringFactoryKeyDeserializer(MyCustomClass.class.getMethod("createInstance", String.class));
 Object result = customStringFactoryStdKeyDeserializer.deserializeKey(key, new TestContext());
 Assert.assertEquals(new MyCustomClass("customKey"), result);
 }

 private static class TestContext extends DeserializationContext {
 @Override
 public ObjectMapper getMapper() { return objectMapper; }

 @Override
 public DeserializationConfig getConfig() { return objectMapper.getConfiguration(); }

 @Override
 public JavaType getTypeFactory() { return objectMapper.getTypeFactory(); }

 @Override
 public Object findValueInMap(Map<?, ?> map, Object key) { return map.get(key); }

 @Override
 public Object findCreatorInstance(BeanPropertyDefinition p, Object parent, Object ctxt) throws IOException, InstantiationException, IllegalAccessException {
 return p.getBeanDescription().getBeanClass().newInstance();
 }
 }
}