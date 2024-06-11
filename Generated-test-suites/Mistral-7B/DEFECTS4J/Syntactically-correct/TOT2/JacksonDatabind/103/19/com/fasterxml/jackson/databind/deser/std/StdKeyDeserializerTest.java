import com.fasterxml.jackson.core.base.JsonSubType;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorImpl;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.Function;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StdKeyDeserializerTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testBoolean() throws Exception {
 String testKey = "true";
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 Object result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertTrue((Boolean) result);

 testKey = "false";
 result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertFalse((Boolean) result);
 }

 @Test
 public void testByte() throws Exception {
 String testKey = "127";
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
 Object result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertEquals((Byte) Byte.parseByte(testKey), result);

 testKey = "128";
 result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testShort() throws Exception {
 String testKey = "32767";
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
 Object result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertEquals((Short) Short.parseShort(testKey), result);

 testKey = "32768";
 result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testChar() throws Exception {
 String testKey = "A";
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
 Object result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertEquals((Character) testKey.charAt(0), result);

 testKey = "ab";
 result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testInt() throws Exception {
 String testKey = "12345";
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
 Object result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertEquals((Integer) Integer.parseInt(testKey), result);

 testKey = "123456";
 result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testLong() throws Exception {
 String testKey = "12345678901234567890";
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
 Object result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertEquals((Long) Long.parseLong(testKey), result);

 testKey = "12345678901234567891";
 result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testFloat() throws Exception {
 String testKey = "3.14";
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
 Object result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertEquals((Float) Float.parseFloat(testKey), result, 0.001f);

 testKey = "3.1415";
 result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testDouble() throws Exception {
 String testKey = "3.14159265358979323846";
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
 Object result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertEquals((Double) Double.parseDouble(testKey), result, 0.00000000000000001);

 testKey = "3.14159265358979323847";
 result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testLocale() throws Exception {
 String testKey = "en_US";
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
 Object result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertEquals(Locale.forLanguageTag(testKey), result);

 testKey = "es_ES";
 result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertEquals(Locale.forLanguageTag(testKey), result);

 testKey = "fr_FR_x-foo-bar";
 result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testDate() throws Exception {
 String testKey = "2022-12-31";
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
 Object result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertEquals(new Date(Long.parseLong("1671139200000")), result);

 testKey = "2022-12-32";
 result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testCalendar() throws Exception {
 Class<Calendar> calendarClass = Calendar.class;
 AnnotatedClass ac = mapper.getAnnotationIntrospector().findProperties(calendarClass, null);
 AnnotatedField calendarField = ac.findField("time");
 AnnotatedMethod calendarConstructor = calendarClass.getDeclaredMethod("getInstance");
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(calendarClass);
 Object result = keyDeserializer.deserializeKey("2022-12-31", mapper.getDeserializationContext());
 assertEquals(calendarConstructor.invoke(null), result);

 testKey = "2022-12-32";
 result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testEnumWithDefaultValue() throws Exception {
 enum TestEnum {
 FOO, BAR;

 @JsonDeserialize(using = EnumDeserializer.class)
 static class EnumDeserializer extends StdKeyDeserializer {
 public EnumDeserializer() {
 super(TestEnum.class);
 }
 }
 }
 String testKey = "BAR";
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(TestEnum.class);
 Object result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertEquals(TestEnum.BAR, result);

 testKey = "FOO";
 result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertEquals(TestEnum.FOO, result);

 testKey = "NULL";
 result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testUri() throws Exception {
 String testKey = "https://www.google.com";
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
 Object result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertEquals(new URI(testKey), result);

 testKey = "http://example.com/";
 result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertEquals(new URI(testKey), result);

 testKey = "mailto:test@example.com";
 result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testUrl() throws Exception {
 String testKey = "https://www.google.com";
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
 Object result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertEquals(new URL(testKey), result);

 testKey = "http://example.com/";
 result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertEquals(new URL(testKey), result);

 testKey = "http://example.com/foo?bar=baz#fragment";
 result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertEquals(new URL(testKey), result);

 testKey = "ftp://example.com/";
 result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testClassDeserialization() throws Exception {
 Map<String, Integer> testMap = new HashMap<>();
 testMap.put("integer1", 1);
 testMap.put("integer2", 2);
 testMap.put("string1", "one");
 testMap.put("string2", "two");

 String testKey = "java.util.HashMap";
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Map.class);
 Object result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertTrue(result instanceof Map);

 Map<?, ?> deserializedMap = (Map<?, ?>) result;
 assertEquals(testMap.size(), deserializedMap.size());

 for (Map.Entry<?, ?> entry : testMap.entrySet()) {
 String key = entry.getKey().toString();
 Object value = entry.getValue();
 Object deserializedValue = deserializedMap.get(key);
 assertEquals(value, deserializedValue);
 }
 }

 @Test
 public void testClassDeserializationWithSubType() throws Exception {
 @JsonSubTypes({@JsonSubTypes.With(value = Integer.class, name = "int")})
 class TestClass {
 int intValue;
 }

 class TestSubClass extends TestClass {
 int subValue;
 }

 String testKey = "int";
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(TestClass.class);
 Object result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertTrue(result instanceof TestClass);
 TestClass deserializedObject = (TestClass) result;
 assertEquals(0, deserializedObject.intValue);

 testKey = "int,subValue=10";
 result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertTrue(result instanceof TestSubClass);
 TestSubClass deserializedSubObject = (TestSubClass) result;
 assertEquals(10, deserializedSubObject.subValue);

 testKey = "invalid";
 result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testStringCtorKeyDeserializer() throws Exception {
 // Testing StringCtorKeyDeserializer for the String class
 Class<?> stringClass = String.class;
 Constructor<String> stringConstructor = stringClass.getDeclaredConstructor(String.class);
 StdKeyDeserializer keyDeserializer = new StringCtorKeyDeserializer(stringConstructor);
 String testKey = "Test";
 Object result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertEquals(testKey, result);

 // Testing StringCtorKeyDeserializer for a custom class
 class CustomClass {
 String customString;

 CustomClass(String customString) {
 this.customString = customString;
 }
 }
 Constructor<CustomClass> customConstructor = CustomClass.class.getDeclaredConstructor(String.class);
 StdKeyDeserializer customKeyDeserializer = new StringCtorKeyDeserializer(customConstructor);
 testKey = "Test";
 result = customKeyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 CustomClass customObject = (CustomClass) result;
 assertEquals(testKey, customObject.customString);
 }

 @Test
 public void testStringFactoryKeyDeserializer() throws Exception {
 // Testing StringFactoryKeyDeserializer for the Locale class
 Class<Locale> localeClass = Locale.class;
 Method localeFactoryMethod = localeClass.getMethod("forLanguageTag");
 StdKeyDeserializer keyDeserializer = new StringFactoryKeyDeserializer(localeFactoryMethod);
 String testKey = "en_US";
 Object result = keyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 assertEquals(Locale.forLanguageTag(testKey), result);

 // Testing StringFactoryKeyDeserializer for a custom class
 class CustomClass {
 static CustomClass createCustomClass(String customString) {
 return new CustomClass();
 }
 }
 Method customFactoryMethod = CustomClass.class.getMethod("createCustomClass", String.class);
 StdKeyDeserializer customKeyDeserializer = new StringFactoryKeyDeserializer(customFactoryMethod);
 testKey = "Test";
 result = customKeyDeserializer.deserializeKey(testKey, mapper.getDeserializationContext());
 CustomClass customObject = (CustomClass) result;
 // Since the createCustomClass method doesn't take any arguments, it doesn't matter what value is passed as a testKey
 assertNotNull(customObject);
 }
}