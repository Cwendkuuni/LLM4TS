import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private DeserializationContext ctxt;
 private ObjectMapper mapper;

 @Before
 public void setup() throws Exception {
 ctxt = new TestAnnotationIntrospector().findInjectableValue(
 ClassUtil.findCreator(StdKeyDeserializer.class), null);
 mapper = new JsonMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testBooleanDeserializer() throws IOException {
 String key = "true";
 Object result = mapper.readValue(key, StdKeyDeserializer.BooleanKeyDeserializer.class);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);

 key = "false";
 result = mapper.readValue(key, StdKeyDeserializer.BooleanKeyDeserializer.class);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testByteDeserializer() throws IOException {
 String key = "10";
 Object result = mapper.readValue(key, StdKeyDeserializer.ByteKeyDeserializer.class);
 assertTrue(result instanceof Byte);
 assertEquals((byte) 10, (Byte) result);

 String keyOutOfRange = "256";
 Object resultOutOfRange = mapper.readValue(keyOutOfRange, StdKeyDeserializer.ByteKeyDeserializer.class);
 assertNull(resultOutOfRange);
 }

 @Test
 public void testShortDeserializer() throws IOException {
 String key = "100";
 Object result = mapper.readValue(key, StdKeyDeserializer.ShortKeyDeserializer.class);
 assertTrue(result instanceof Short);
 assertEquals((short) 100, (Short) result);

 String keyOutOfRange = "32000";
 Object resultOutOfRange = mapper.readValue(keyOutOfRange, StdKeyDeserializer.ShortKeyDeserializer.class);
 assertNull(resultOutOfRange);
 }

 @Test
 public void testCharDeserializer() throws IOException {
 String key = "A";
 Object result = mapper.readValue(key, StdKeyDeserializer.CharKeyDeserializer.class);
 assertTrue(result instanceof Character);
 assertEquals('A', (Character) result);

 String keyWithLengthGreaterThanOne = "AB";
 Object resultWithLengthGreaterThanOne = mapper.readValue(keyWithLengthGreaterThanOne,
 StdKeyDeserializer.CharKeyDeserializer.class);
 assertNull(resultWithLengthGreaterThanOne);
 }

 @Test
 public void testIntDeserializer() throws IOException {
 String key = "1000";
 Object result = mapper.readValue(key, StdKeyDeserializer.IntKeyDeserializer.class);
 assertTrue(result instanceof Integer);
 assertEquals(1000, (Integer) result);
 }

 @Test
 public void testLongDeserializer() throws IOException {
 String key = "1000000";
 Object result = mapper.readValue(key, StdKeyDeserializer.LongKeyDeserializer.class);
 assertTrue(result instanceof Long);
 assertEquals(1000000L, (Long) result);
 }

 @Test
 public void testFloatDeserializer() throws IOException {
 String key = "1.23";
 Object result = mapper.readValue(key, StdKeyDeserializer.FloatKeyDeserializer.class);
 assertTrue(result instanceof Float);
 assertEquals(1.23f, (Float) result, 0.001f);
 }

 @Test
 public void testDoubleDeserializer() throws IOException {
 String key = "1.23";
 Object result = mapper.readValue(key, StdKeyDeserializer.DoubleKeyDeserializer.class);
 assertTrue(result instanceof Double);
 assertEquals(1.23, (Double) result, 0.001);
 }

 @Test
 public void testLocaleDeserializer() throws IOException, IllegalArgumentException, InvocationTargetException, InstantiationException {
 String key = "en_US";
 Object result = mapper.readValue(key, StdKeyDeserializer.LocaleKeyDeserializer.class);
 assertTrue(result instanceof Locale);
 assertEquals(Locale.US, (Locale) result);

 String keyNotFound = "es_ES";
 Object resultNotFound = mapper.readValue(keyNotFound, StdKeyDeserializer.LocaleKeyDeserializer.class);
 assertNull(resultNotFound);
 }

 @Test
 public void testDateDeserializer() throws IOException {
 String key = "2022-01-01";
 Object result = mapper.readValue(key, StdKeyDeserializer.DateKeyDeserializer.class);
 assertTrue(result instanceof Date);
 assertEquals(new Date(2022, 0, 1), (Date) result);
 }

 @Test
 public void testCalendarDeserializer() throws IOException, ParseException {
 String key = "2022-01-01";
 Object result = mapper.readValue(key, StdKeyDeserializer.CalendarKeyDeserializer.class);
 assertTrue(result instanceof Calendar);
 assertEquals(Calendar.getInstance().getTime(), (Calendar) result.getTime());
 }

 @Test
 public void testUriDeserializer() throws IOException, URISyntaxException {
 String key = "http://example.com";
 Object result = mapper.readValue(key, StdKeyDeserializer.UriKeyDeserializer.class);
 assertTrue(result instanceof URI);
 assertEquals(new URI("http", "example.com", null), (URI) result);
 }

 @Test
 public void testUrlDeserializer() throws IOException {
 String key = "http://example.com";
 Object result = mapper.readValue(key, StdKeyDeserializer.UrlKeyDeserializer.class);
 assertTrue(result instanceof URL);
 assertEquals(new URL("http", "example.com", null), (URL) result);
 }

 @Test
 public void testClassDeserializer() throws IOException {
 Class<?> keyClass = String.class;
 String key = keyClass.getName();
 Object result = mapper.readValue(key, StdKeyDeserializer.ClassKeyDeserializer.class);
 assertTrue(result instanceof Class<?>);
 assertEquals(keyClass, (Class<?>) result);
 }

 @Test
 public void testEnumDeserializer() throws IOException {
 AnnotatedClass ac = mapper.getTypeFactory().constructType(EnumTest.class);
 Constructor<StdKeyDeserializer.EnumKeyDeserializer<EnumTest>> constructor =
 StdKeyDeserializer.EnumKeyDeserializer.class.getDeclaredConstructor(ac.getRawClass(), AnnotatedMethod.class);
 constructor.setAccessible(true);
 Object result = constructor.newInstance(ac, mapper.getAnnotationIntrospector().findMethodAnnotations(ac, "EnumValueDeserializerTest.VALUE_ONE"));
 Object key = "VALUE_ONE";
 Object deserializedEnum = mapper.readValue(key, result.getClass());
 assertEquals(EnumTest.VALUE_ONE, deserializedEnum);
 }

 @Test
 public void testStringKeyDeserializer() throws IOException {
 String key = "test";
 Object result = mapper.readValue(key, StdKeyDeserializer.StringKeyDeserializer.class);
 assertEquals(key, result);
 }

 @Test
 public void testDelegateDeserializer() throws IOException {
 String key = "test";
 ObjectMapper delegateMapper = new JsonMapper();
 delegateMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 Object result = mapper.readValue(key, StdKeyDeserializer.DelegateKeyDeserializer.class, delegateMapper);
 assertEquals(key, result);
 }
}