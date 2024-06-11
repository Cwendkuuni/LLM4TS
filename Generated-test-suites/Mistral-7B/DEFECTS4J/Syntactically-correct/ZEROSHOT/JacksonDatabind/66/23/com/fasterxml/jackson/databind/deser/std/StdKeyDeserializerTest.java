import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

public class StdKeyDeserializerTest {

 private ObjectMapper objectMapper;
 private StdKeyDeserializer stringKD;
 private StdKeyDeserializer booleanKD;
 private StdKeyDeserializer intKD;
 private StdKeyDeserializer longKD;
 private StdKeyDeserializer doubleKD;
 private StdKeyDeserializer uriKD;
 private StdKeyDeserializer urlKD;
 private StdKeyDeserializer uuidKD;
 private StdKeyDeserializer localeKD;
 private StdKeyDeserializer dateKD;
 private StdKeyDeserializer calendarKD;
 private StdKeyDeserializer stringEnumKD;
 private StdKeyDeserializer intEnumKD;
 private StdKeyDeserializer customClassKD;
 private StdKeyDeserializer stringConstructorKD;
 private StdKeyDeserializer stringFactoryKD;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 stringKD = StdKeyDeserializer.forType(String.class);
 booleanKD = StdKeyDeserializer.forType(Boolean.class);
 intKD = StdKeyDeserializer.forType(Integer.class);
 longKD = StdKeyDeserializer.forType(Long.class);
 doubleKD = StdKeyDeserializer.forType(Double.class);
 uriKD = StdKeyDeserializer.forType(URI.class);
 urlKD = StdKeyDeserializer.forType(URL.class);
 uuidKD = StdKeyDeserializer.forType(UUID.class);
 localeKD = StdKeyDeserializer.forType(Locale.class);
 dateKD = StdKeyDeserializer.forType(Date.class);
 calendarKD = StdKeyDeserializer.forType(Calendar.class);
 stringEnumKD = new EnumKD(objectMapper.getAnnotationIntrospector(),
 Object.class.getDeclaredMethod("valueOf", String.class));
 intEnumKD = new EnumKD(objectMapper.getAnnotationIntrospector(),
 Integer.TYPE.getDeclaredMethod("valueOf", String.class));
 customClassKD = new StringCtorKeyDeserializer(HashMap.class.getDeclaredConstructor());
 stringConstructorKD = new StringCtorKeyDeserializer(String[].class.getDeclaredConstructor(String.class));
 stringFactoryKD = new StringFactoryKeyDeserializer(URISyntaxException.class.getDeclaredMethod("create", String.class));
 }

 @Test
 public void testDeserializeKey_String() throws JsonProcessingException {
 Object key = stringKD.deserializeKey("testKey", objectMapper.getDeserializationContext());
 Assert.assertEquals("testKey", key);
 }

 @Test
 public void testDeserializeKey_Boolean() throws JsonProcessingException {
 Object key = booleanKD.deserializeKey("true", objectMapper.getDeserializationContext());
 Assert.assertEquals(Boolean.TRUE, key);
 }

 @Test
 public void testDeserializeKey_Byte() throws JsonProcessingException {
 Object key = intKD.deserializeKey("127", objectMapper.getDeserializationContext());
 Assert.assertEquals((byte) 127, key);
 }

 @Test
 public void testDeserializeKey_Short() throws JsonProcessingException {
 Object key = intKD.deserializeKey("32767", objectMapper.getDeserializationContext());
 Assert.assertEquals((short) 32767, key);
 }

 @Test
 public void testDeserializeKey_Int() throws JsonProcessingException {
 Object key = intKD.deserializeKey("12345", objectMapper.getDeserializationContext());
 Assert.assertEquals(12345, key);
 }

 @Test
 public void testDeserializeKey_Long() throws JsonProcessingException {
 Object key = longKD.deserializeKey("9223372036854775807", objectMapper.getDeserializationContext());
 Assert.assertEquals(Long.MAX_VALUE, key);
 }

 @Test
 public void testDeserializeKey_Double() throws JsonProcessingException {
 Object key = doubleKD.deserializeKey("1.2345", objectMapper.getDeserializationContext());
 Assert.assertEquals(1.2345, key, 0.0001);
 }

 @Test
 public void testDeserializeKey_URI() throws JsonProcessingException {
 Object key = uriKD.deserializeKey("http://www.example.com", objectMapper.getDeserializationContext());
 Assert.assertTrue(key instanceof URI);
 }

 @Test
 public void testDeserializeKey_URL() throws JsonProcessingException {
 Object key = urlKD.deserializeKey("http://www.example.com", objectMapper.getDeserializationContext());
 Assert.assertTrue(key instanceof URL);
 }

 @Test
 public void testDeserializeKey_UUID() throws JsonProcessingException {
 Object key = uuidKD.deserializeKey("00000000-0000-0000-0000-000000000000", objectMapper.getDeserializationContext());
 Assert.assertTrue(key instanceof UUID);
 }

 @Test
 public void testDeserializeKey_Locale() throws JsonProcessingException {
 Object key = localeKD.deserializeKey("en_US", objectMapper.getDeserializationContext());
 Assert.assertTrue(key instanceof Locale);
 }

 @Test
 public void testDeserializeKey_Date() throws JsonProcessingException {
 Object key = dateKD.deserializeKey("2022-03-12", objectMapper.getDeserializationContext());
 Assert.assertTrue(key instanceof Date);
 }

 @Test
 public void testDeserializeKey_Calendar() throws JsonProcessingException {
 Object key = calendarKD.deserializeKey("2022-03-12", objectMapper.getDeserializationContext());
 Assert.assertTrue(key instanceof Calendar);
 }

 @Test
 public void testDeserializeKey_Enum() throws JsonProcessingException {
 EnumResolver enumResolver = objectMapper.getAnnotationIntrospector();
 Enum enumValue = stringEnumKD.deserializeKey("EAST", objectMapper.getDeserializationContext());
 Assert.assertEquals(Enum.EAST, enumValue);
 enumValue = intEnumKD.deserializeKey("NORTH", objectMapper.getDeserializationContext());
 Assert.assertEquals(Enum.NORTH, enumValue);
 }

 @Test
 public void testDeserializeKey_CustomClass() throws JsonProcessingException {
 Object key = customClassKD.deserializeKey("{1=key1, 2=key2}", objectMapper.getDeserializationContext());
 Assert.assertTrue(key instanceof Map);
 }

 @Test
 public void testDeserializeKey_CustomStringConstructor() throws JsonProcessingException {
 Object key = stringConstructorKD.deserializeKey("[test1, test2]", objectMapper.getDeserializationContext());
 Assert.assertArrayEquals(new String[]{"test1", "test2"}, (String[]) key);
 }

 @Test
 public void testDeserializeKey_CustomStringFactory() throws JsonProcessingException {
 Object key = stringFactoryKD.deserializeKey("invalid:/test", objectMapper.getDeserializationContext());
 Assert.assertTrue(key instanceof URISyntaxException);
 }
}