import com.fasterxml.jackson.databind.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class StdKeyDeserializerTest {

 private ObjectMapper objectMapper;

 @BeforeAll
 static void setup() {
 objectMapper = new ObjectMapper();
 }

 @AfterAll
 static void tearDown() throws Exception {
 objectMapper.close();
 }

 @Test
 public void testBooleanKeyDeserialization() throws Exception {
 StdKeyDeserializer booleanKeyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 Object key = "true";
 Object deserializedKey = booleanKeyDeserializer.deserializeKey(key.toString(), objectMapper.getDeserializationContext());
 assertTrue((Boolean) deserializedKey);
 }

 @Test
 public void testInvalidBooleanKeyDeserialization() throws Exception {
 StdKeyDeserializer booleanKeyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 Object key = "invalid_boolean_value";
 Object deserializedKey = booleanKeyDeserializer.deserializeKey(key.toString(), objectMapper.getDeserializationContext());
 assertNull(deserializedKey);
 }

 @Test
 public void testByteKeyDeserialization() throws Exception {
 StdKeyDeserializer byteKeyDeserializer = StdKeyDeserializer.forType(Byte.class);
 Object key = "127";
 Object deserializedKey = byteKeyDeserializer.deserializeKey(key.toString(), objectMapper.getDeserializationContext());
 assertEquals((Byte) new Byte("127"), deserializedKey);
 }

 @Test
 public void testByteOutOfRangeKeyDeserialization() throws Exception {
 StdKeyDeserializer byteKeyDeserializer = StdKeyDeserializer.forType(Byte.class);
 Object key = "256";
 Object deserializedKey = byteKeyDeserializer.deserializeKey(key.toString(), objectMapper.getDeserializationContext());
 assertNull(deserializedKey);
 }

 @Test
 public void testShortKeyDeserialization() throws Exception {
 StdKeyDeserializer shortKeyDeserializer = StdKeyDeserializer.forType(Short.class);
 Object key = "32767";
 Object deserializedKey = shortKeyDeserializer.deserializeKey(key.toString(), objectMapper.getDeserializationContext());
 assertEquals((Short) new Short("32767"), deserializedKey);
 }

 @Test
 public void testShortOutOfRangeKeyDeserialization() throws Exception {
 StdKeyDeserializer shortKeyDeserializer = StdKeyDeserializer.forType(Short.class);
 Object key = "65536";
 Object deserializedKey = shortKeyDeserializer.deserializeKey(key.toString(), objectMapper.getDeserializationContext());
 assertNull(deserializedKey);
 }

 @Test
 public void testCharKeyDeserialization() throws Exception {
 StdKeyDeserializer charKeyDeserializer = StdKeyDeserializer.forType(Character.class);
 Object key = "'a'";
 Object deserializedKey = charKeyDeserializer.deserializeKey(key.toString(), objectMapper.getDeserializationContext());
 assertEquals((Character) 'a', deserializedKey);
 }

 @Test
 public void testInvalidCharKeyDeserialization() throws Exception {
 StdKeyDeserializer charKeyDeserializer = StdKeyDeserializer.forType(Character.class);
 Object key = "invalid_char_value";
 Object deserializedKey = charKeyDeserializer.deserializeKey(key.toString(), objectMapper.getDeserializationContext());
 assertNull(deserializedKey);
 }

 @Test
 public void testIntKeyDeserialization() throws Exception {
 StdKeyDeserializer intKeyDeserializer = StdKeyDeserializer.forType(Integer.class);
 Object key = "42";
 Object deserializedKey = intKeyDeserializer.deserializeKey(key.toString(), objectMapper.getDeserializationContext());
 assertEquals((Integer) new Integer("42"), deserializedKey);
 }

 @Test
 public void testLongKeyDeserialization() throws Exception {
 StdKeyDeserializer longKeyDeserializer = StdKeyDeserializer.forType(Long.class);
 Object key = "12345678901234567890";
 Object deserializedKey = longKeyDeserializer.deserializeKey(key.toString(), objectMapper.getDeserializationContext());
 assertEquals((Long) new Long("12345678901234567890"), deserializedKey);
 }

 @Test
 public void testFloatKeyDeserialization() throws Exception {
 StdKeyDeserializer floatKeyDeserializer = StdKeyDeserializer.forType(Float.class);
 Object key = "3.14";
 Object deserializedKey = floatKeyDeserializer.deserializeKey(key.toString(), objectMapper.getDeserializationContext());
 assertEquals((Float) new Float("3.14"), deserializedKey);
 }

 @Test
 public void testDoubleKeyDeserialization() throws Exception {
 StdKeyDeserializer doubleKeyDeserializer = StdKeyDeserializer.forType(Double.class);
 Object key = "3.1415926535897932384626433832795028841";
 Object deserializedKey = doubleKeyDeserializer.deserializeKey(key.toString(), objectMapper.getDeserializationContext());
 assertEquals((Double) new Double("3.1415926535897932384626433832795028841"), deserializedKey);
 }

 @Test
 public void testUriKeyDeserialization() throws Exception {
 StdKeyDeserializer uriKeyDeserializer = StdKeyDeserializer.forType(URI.class);
 Object key = "https://www.example.com";
 Object deserializedKey = uriKeyDeserializer.deserializeKey(key.toString(), objectMapper.getDeserializationContext());
 assertEquals((URI) new URI("https://www.example.com"), deserializedKey);
 }

 @Test
 public void testInvalidUriKeyDeserialization() throws Exception {
 StdKeyDeserializer uriKeyDeserializer = StdKeyDeserializer.forType(URI.class);
 Object key = "invalid_uri_value";
 Object deserializedKey = uriKeyDeserializer.deserializeKey(key.toString(), objectMapper.getDeserializationContext());
 assertNull(deserializedKey);
 }

 @Test
 public void testUrlKeyDeserialization() throws Exception {
 StdKeyDeserializer urlKeyDeserializer = StdKeyDeserializer.forType(URL.class);
 Object key = "https://www.example.com";
 Object deserializedKey = urlKeyDeserializer.deserializeKey(key.toString(), objectMapper.getDeserializationContext());
 assertEquals((URL) new URL("https://www.example.com"), deserializedKey);
 }

 @Test
 public void testInvalidUrlKeyDeserialization() throws Exception {
 StdKeyDeserializer urlKeyDeserializer = StdKeyDeserializer.forType(URL.class);
 Object key = "invalid_url_value";
 Object deserializedKey = urlKeyDeserializer.deserializeKey(key.toString(), objectMapper.getDeserializationContext());
 assertNull(deserializedKey);
 }

 @Test
 public void testLocaleKeyDeserialization() throws Exception {
 StdKeyDeserializer localeKeyDeserializer = StdKeyDeserializer.forType(Locale.class);
 Object key = "en_US";
 Object deserializedKey = localeKeyDeserializer.deserializeKey(key.toString(), objectMapper.getDeserializationContext());
 assertEquals((Locale) Locale.US, deserializedKey);
 }

 @Test
 public void testInvalidLocaleKeyDeserialization() throws Exception {
 StdKeyDeserializer localeKeyDeserializer = StdKeyDeserializer.forType(Locale.class);
 Object key = "invalid_locale_value";
 Object deserializedKey = localeKeyDeserializer.deserializeKey(key.toString(), objectMapper.getDeserializationContext());
 assertNull(deserializedKey);
 }

 @Test
 public void testCalendarKeyDeserialization() throws Exception {
 // Testing CalendarKeyDeserializer is not provided in the code snippet
 }

 @Test
 public void testDateKeyDeserialization() throws Exception {
 StdKeyDeserializer dateKeyDeserializer = StdKeyDeserializer.forType(Date.class);
 Object key = "2022-03-01";
 Object deserializedKey = dateKeyDeserializer.deserializeKey(key.toString(), objectMapper.getDeserializationContext());
 assertEquals((Date) new Date(0), deserializedKey); // Using default Date constructor
 }

 @Test
 public void testInvalidDateKeyDeserialization() throws Exception {
 StdKeyDeserializer dateKeyDeserializer = StdKeyDeserializer.forType(Date.class);
 Object key = "invalid_date_value";
 Object deserializedKey = dateKeyDeserializer.deserializeKey(key.toString(), objectMapper.getDeserializationContext());
 assertNull(deserializedKey);
 }

 @Test
 public void testUuidKeyDeserialization() throws Exception {
 StdKeyDeserializer uuidKeyDeserializer = StdKeyDeserializer.forType(UUID.class);
 Object key = "123e4567-e89b-12d3-a456-426614174000";
 Object deserializedKey = uuidKeyDeserializer.deserializeKey(key.toString(), objectMapper.getDeserializationContext());
 assertEquals((UUID) UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), deserializedKey);
 }

 @Test
 public void testInvalidUuidKeyDeserialization() throws Exception {
 StdKeyDeserializer uuidKeyDeserializer = StdKeyDeserializer.forType(UUID.class);
 Object key = "invalid_uuid_value";
 Object deserializedKey = uuidKeyDeserializer.deserializeKey(key.toString(), objectMapper.getDeserializationContext());
 assertNull(deserializedKey);
 }

 @Test
 public void testEnumKeyDeserialization() throws Exception {
 Enum enumType = MyCustomEnum.class;
 StdKeyDeserializer enumKeyDeserializer = StdKeyDeserializer.forType(enumType);
 Object key = "MyCustomEnum.VALUE_ONE";
 Object deserializedKey = enumKeyDeserializer.deserializeKey(key.toString(), objectMapper.getDeserializationContext());
 assertEquals((Object) MyCustomEnum.VALUE_ONE, deserializedKey);
 }

 @Test
 public void testInvalidEnumKeyDeserialization() throws Exception {
 Enum enumType = MyCustomEnum.class;
 StdKeyDeserializer enumKeyDeserializer = StdKeyDeserializer.forType(enumType);
 Object key = "InvalidEnumValue";
 Object deserializedKey = enumKeyDeserializer.deserializeKey(key.toString(), objectMapper.getDeserializationContext());
 assertNull(deserializedKey);
 }
}