import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

@JacksonStdImpl
public class StdKeyDeserializerTest {

 private AnnotationIntrospector _annotationIntrospector;
 private ObjectMapper _objectMapper;

 @Before
 public void setup() {
 _annotationIntrospector = new StandardAnnotationIntrospector();
 _objectMapper = new ObjectMapper();
 }

 @Test
 public void testForType() throws Exception {
 Class<?>[] types = new Class[] { String.class, Object.class, CharSequence.class, UUID.class,
 Integer.class, Long.class, Date.class, Calendar.class, Boolean.class, Byte.class, Character.class,
 Short.class, Float.class, Double.class, URI.class, URL.class, Locale.class, Currency.class,
 byte[].class };

 for (Class<?> type : types) {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(type);
 assertEquals(type, deserializer.getKeyClass());
 }
 }

 @Test
 public void testStringKeyDeserializer() throws Exception {
 StdKeyDeserializer deserializer = StringKD.forType(String.class);
 assertEquals(String.class, deserializer.getKeyClass());
 String key = "test";
 Object result = deserializer.deserializeKey(key, _objectMapper.getDeserializationContext());
 assertEquals(key, result);
 }

 @Test
 public void testObjectKeyDeserializer() throws Exception {
 StdKeyDeserializer deserializer = StringKD.forType(Object.class);
 assertEquals(Object.class, deserializer.getKeyClass());
 String key = "test";
 Object result = deserializer.deserializeKey(key, _objectMapper.getDeserializationContext());
 assertEquals(key, result);
 }

 @Test
 public void testBooleanKeyDeserializer() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
 assertEquals(Boolean.class, deserializer.getKeyClass());
 String keyTrue = "true";
 Object resultTrue = deserializer.deserializeKey(keyTrue, _objectMapper.getDeserializationContext());
 assertTrue(resultTrue instanceof Boolean);
 assertEquals(true, ((Boolean) resultTrue).booleanValue());

 String keyFalse = "false";
 Object resultFalse = deserializer.deserializeKey(keyFalse, _objectMapper.getDeserializationContext());
 assertTrue(resultFalse instanceof Boolean);
 assertEquals(false, ((Boolean) resultFalse).booleanValue());
 }

 @Test
 public void testByteKeyDeserializer() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
 assertEquals(Byte.class, deserializer.getKeyClass());
 String key = "127";
 Object result = deserializer.deserializeKey(key, _objectMapper.getDeserializationContext());
 assertTrue(result instanceof Byte);
 assertEquals((byte) 127, ((Byte) result).byteValue());
 }

 @Test
 public void testShortKeyDeserializer() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
 assertEquals(Short.class, deserializer.getKeyClass());
 String key = "255";
 Object result = deserializer.deserializeKey(key, _objectMapper.getDeserializationContext());
 assertTrue(result instanceof Short);
 assertEquals((short) 255, ((Short) result).shortValue());
 }

 @Test
 public void testCharKeyDeserializer() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
 assertEquals(Character.class, deserializer.getKeyClass());
 String key = "A";
 Object result = deserializer.deserializeKey(key, _objectMapper.getDeserializationContext());
 assertTrue(result instanceof Character);
 assertEquals('A', ((Character) result).charValue());
 }

 @Test
 public void testIntegerKeyDeserializer() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
 assertEquals(Integer.class, deserializer.getKeyClass());
 String key = "123";
 Object result = deserializer.deserializeKey(key, _objectMapper.getDeserializationContext());
 assertEquals(key, result);
 }

 @Test
 public void testLongKeyDeserializer() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
 assertEquals(Long.class, deserializer.getKeyClass());
 String key = "12345678901234567890";
 Object result = deserializer.deserializeKey(key, _objectMapper.getDeserializationContext());
 assertEquals(key, result);
 }

 @Test
 public void testFloatKeyDeserializer() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
 assertEquals(Float.class, deserializer.getKeyClass());
 String key = "123.45";
 Object result = deserializer.deserializeKey(key, _objectMapper.getDeserializationContext());
 assertTrue(result instanceof Float);
 assertEquals(Float.parseFloat(key), ((Float) result).floatValue());
 }

 @Test
 public void testDoubleKeyDeserializer() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
 assertEquals(Double.class, deserializer.getKeyClass());
 String key = "123.45";
 Object result = deserializer.deserializeKey(key, _objectMapper.getDeserializationContext());
 assertTrue(result instanceof Double);
 assertEquals(Double.parseDouble(key), ((Double) result).doubleValue());
 }

 @Test
 public void testURIKeyDeserializer() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
 assertEquals(URI.class, deserializer.getKeyClass());
 String key = "http://www.example.com";
 Object result = deserializer.deserializeKey(key, _objectMapper.getDeserializationContext());
 assertTrue(result instanceof URI);
 assertEquals(URI.create(key), result);
 }

 @Test
 public void testURLKeyDeserializer() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
 assertEquals(URL.class, deserializer.getKeyClass());
 String key = "http://www.example.com";
 Object result = deserializer.deserializeKey(key, _objectMapper.getDeserializationContext());
 assertTrue(result instanceof URL);
 assertEquals(new URL(key), result);
 }

 @Test
 public void testEnumKeyDeserializer() throws Exception {
 EnumResolver enumResolver = new SimpleEnumResolver("TestEnum");
 AnnotatedClass annotatedClass = _annotationIntrospector.findClassAnnotations(TestEnum.class, null);
 AnnotatedConstructor constructor = annotatedClass.getConstructors()[0];
 AnnotatedMethod factoryMethod = annotatedClass.findMethod("valueOf", String.class);

 StdKeyDeserializer deserializer = new EnumKD(enumResolver, constructor);
 assertEquals(TestEnum.class, deserializer.getKeyClass());
 String keyOne = "ONE";
 Object resultOne = deserializer.deserializeKey(keyOne, _objectMapper.getDeserializationContext());
 assertEquals(TestEnum.ONE, resultOne);

 StdKeyDeserializer deserializerToString = new EnumKD(enumResolver, null);
 assertEquals(TestEnum.class, deserializerToString.getKeyClass());
 String keyTwo = "TWO";
 Object resultTwo = deserializerToString.deserializeKey(keyTwo, _objectMapper.getDeserializationContext());
 assertEquals(TestEnum.TWO, resultTwo);
 }

 @Test
 public void testClassKeyDeserializer() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
 assertEquals(String.class, deserializer.getKeyClass());

 AnnotatedClass annotatedClass = _annotationIntrospector.findClassAnnotations(TestEnum.class, null);
 AnnotatedMethod factoryMethod = annotatedClass.findMethod("value", String.class);

 StdKeyDeserializer deserializerClass = new DelegatingKD(TestEnum.class, factoryMethod);
 assertEquals(TestEnum.class, deserializerClass.getKeyClass());
 String key = "TestEnum.TWO";
 Object result = deserializerClass.deserializeKey(key, _objectMapper.getDeserializationContext());
 assertEquals(TestEnum.TWO, result);
 }

 @Test
 public void testNullKey() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Object.class);
 assertEquals(Object.class, deserializer.getKeyClass());
 Object key = null;
 Object result = deserializer.deserializeKey(String.valueOf(key), _objectMapper.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testUnknownType() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
 assertEquals(String.class, deserializer.getKeyClass());
 Object key = new Object();
 String errorMessage = "not a valid representation";
 Object result = deserializer.deserializeKey(key.toString(), _objectMapper.getDeserializationContext());
 assertInstanceOf(MappingException.class, result);
 assertEquals(errorMessage, ((MappingException) result).getMessage());
 }

 @Test
 public void testInvalidIntValue() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
 assertEquals(Byte.class, deserializer.getKeyClass());
 String key = "256";
 String errorMessage = "not a valid representation, problem: java.lang.NumberFormatException: For input string: \"256\"";
 Object result = deserializer.deserializeKey(key, _objectMapper.getDeserializationContext());
 assertInstanceOf(MappingException.class, result);
 assertEquals(errorMessage, ((MappingException) result).getMessage());
 }

 @Test
 public void testInvalidLongValue() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
 assertEquals(Long.class, deserializer.getKeyClass());
 String key = "9223372036854775809";
 String errorMessage = "not a valid representation, problem: java.lang.NumberFormatException: For input string: \"9223372036854775809\"";
 Object result = deserializer.deserializeKey(key, _objectMapper.getDeserializationContext());
 assertInstanceOf(MappingException.class, result);
 assertEquals(errorMessage, ((MappingException) result).getMessage());
 }

 @Test
 public void testInvalidFloatValue() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
 assertEquals(Float.class, deserializer.getKeyClass());
 String key = "not_a_number";
 String errorMessage = "not a valid representation, problem: java.lang.NumberFormatException: 49.0 not a number";
 Object result = deserializer.deserializeKey(key, _objectMapper.getDeserializationContext());
 assertInstanceOf(MappingException.class, result);
 assertEquals(errorMessage, ((MappingException) result).getMessage());
 }

 @Test
 public void testInvalidDoubleValue() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
 assertEquals(Double.class, deserializer.getKeyClass());
 String key = "not_a_number";
 String errorMessage = "not a valid representation, problem: java.lang.NumberFormatException: 49.0 not a number";
 Object result = deserializer.deserializeKey(key, _objectMapper.getDeserializationContext());
 assertInstanceOf(MappingException.class, result);
 assertEquals(errorMessage, ((MappingException) result).getMessage());
 }

 @Test
 public void testInvalidURIValue() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
 assertEquals(URI.class, deserializer.getKeyClass());
 String key = "http://www.example.com/invalid";
 String errorMessage = "not a valid representation, problem: java.net.URISyntaxException: Relative path in absolute URI: http://www.example.com/invalid";
 Object result = deserializer.deserializeKey(key, _objectMapper.getDeserializationContext());
 assertInstanceOf(MappingException.class, result);
 assertEquals(errorMessage, ((MappingException) result).getMessage());
 }

 @Test
 public void testInvalidURLValue() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
 assertEquals(URL.class, deserializer.getKeyClass());
 String key = "http://www.example.com/invalid";
 String errorMessage = "not a valid representation, problem: java.net.MalformedURLException: non-valid-protocol:/http://www.example.com/invalid";
 Object result = deserializer.deserializeKey(key, _objectMapper.getDeserializationContext());
 assertInstanceOf(MappingException.class, result);
 assertEquals(errorMessage, ((MappingException) result).getMessage());
 }

 @Test
 public void testInvalidEnumValue() throws Exception {
 EnumResolver enumResolver = new SimpleEnumResolver("TestEnum");

 StdKeyDeserializer deserializerByName = new EnumKD(enumResolver, null);
 assertEquals(TestEnum.class, deserializerByName.getKeyClass());
 String key = "INVALID";
 String errorMessage = "not a valid representation, problem: not one of values excepted for Enum class: [INVALID, ONE, TWO]";
 Object result = deserializerByName.deserializeKey(key, _objectMapper.getDeserializationContext());
 assertInstanceOf(MappingException.class, result);
 assertEquals(errorMessage, ((MappingException) result).getMessage());

 StdKeyDeserializer deserializerToString = new EnumKD(enumResolver, null);
 assertEquals(TestEnum.class, deserializerToString.getKeyClass());
 key = "INVALID_TO_STRING";
 errorMessage = "not a valid representation, problem: java.lang.IllegalArgumentException: No enum constant TestEnum.INVALID_TO_STRING";
 Object resultToString = deserializerToString.deserializeKey(key, _objectMapper.getDeserializationContext());
 assertInstanceOf(MappingException.class, resultToString);
 assertEquals(errorMessage, ((MappingException) resultToString).getMessage());
 }
}