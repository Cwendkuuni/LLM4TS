import com.fasterxml.jackson.core.base.TextBuffer;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StdKeyDeserializerTest {

 private ObjectMapper objectMapper;

 @BeforeAll
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testBooleanKeyDeserialization() throws Exception {
 StdKeyDeserializer booleanDeserializer = StdKeyDeserializer.forType(Boolean.class);
 Object key = booleanDeserializer.deserializeKey("true", objectMapper.getDeserializationContext());
 assertEquals(Boolean.TRUE, key);
 }

 @Test
 public void testBooleanKeyDeserializationInvalidInput() throws Exception {
 StdKeyDeserializer booleanDeserializer = StdKeyDeserializer.forType(Boolean.class);
 Object key = booleanDeserializer.deserializeKey("invalidInput", objectMapper.getDeserializationContext());
 assertNotEquals(Boolean.TRUE, key);
 assertNotEquals(Boolean.FALSE, key);
 }

 @Test
 public void testByteKeyDeserialization() throws Exception {
 StdKeyDeserializer byteDeserializer = StdKeyDeserializer.forType(Byte.class);
 Object key = byteDeserializer.deserializeKey("127", objectMapper.getDeserializationContext());
 assertEquals(Byte.valueOf((byte) 127), key);
 }

 @Test
 public void testByteKeyDeserializationInvalidInput() throws Exception {
 StdKeyDeserializer byteDeserializer = StdKeyDeserializer.forType(Byte.class);
 Object key = byteDeserializer.deserializeKey("256", objectMapper.getDeserializationContext());
 assertNotEquals(Byte.valueOf((byte) 127), key);
 }

 @Test
 public void testShortKeyDeserialization() throws Exception {
 StdKeyDeserializer shortDeserializer = StdKeyDeserializer.forType(Short.class);
 Object key = shortDeserializer.deserializeKey("12345", objectMapper.getDeserializationContext());
 assertEquals(Short.valueOf((short) 12345), key);
 }

 @Test
 public void testShortKeyDeserializationInvalidInput() throws Exception {
 StdKeyDeserializer shortDeserializer = StdKeyDeserializer.forType(Short.class);
 Object key = shortDeserializer.deserializeKey("32768", objectMapper.getDeserializationContext());
 assertNotEquals(Short.valueOf((short) 12345), key);
 }

 @Test
 public void testCharKeyDeserialization() throws Exception {
 StdKeyDeserializer charDeserializer = StdKeyDeserializer.forType(Character.class);
 Object key = charDeserializer.deserializeKey("a", objectMapper.getDeserializationContext());
 assertEquals(Character.valueOf('a'), key);
 }

 @Test
 public void testCharKeyDeserializationInvalidInput() throws Exception {
 StdKeyDeserializer charDeserializer = StdKeyDeserializer.forType(Character.class);
 Object key = charDeserializer.deserializeKey("invalidInput", objectMapper.getDeserializationContext());
 assertNotEquals(Character.valueOf('a'), key);
 }

 @Test
 public void testIntKeyDeserialization() throws Exception {
 StdKeyDeserializer intDeserializer = StdKeyDeserializer.forType(Integer.class);
 Object key = intDeserializer.deserializeKey("123456", objectMapper.getDeserializationContext());
 assertEquals(Integer.valueOf(123456), key);
 }

 @Test
 public void testIntKeyDeserializationInvalidInput() throws Exception {
 StdKeyDeserializer intDeserializer = StdKeyDeserializer.forType(Integer.class);
 Object key = intDeserializer.deserializeKey("2147483648", objectMapper.getDeserializationContext());
 assertNotEquals(Integer.valueOf(123456), key);
 }

 @Test
 public void testLongKeyDeserialization() throws Exception {
 StdKeyDeserializer longDeserializer = StdKeyDeserializer.forType(Long.class);
 Object key = longDeserializer.deserializeKey("1234567890123456", objectMapper.getDeserializationContext());
 assertEquals(Long.valueOf(1234567890123456L), key);
 }

 @Test
 public void testLongKeyDeserializationInvalidInput() throws Exception {
 StdKeyDeserializer longDeserializer = StdKeyDeserializer.forType(Long.class);
 Object key = longDeserializer.deserializeKey("9223372036854775808", objectMapper.getDeserializationContext());
 assertNotEquals(Long.valueOf(1234567890123456L), key);
 }

 @Test
 public void testFloatKeyDeserialization() throws Exception {
 StdKeyDeserializer floatDeserializer = StdKeyDeserializer.forType(Float.class);
 Object key = floatDeserializer.deserializeKey("123.45", objectMapper.getDeserializationContext());
 assertEquals(Float.valueOf(123.45f), key);
 }

 @Test
 public void testFloatKeyDeserializationInvalidInput() throws Exception {
 StdKeyDeserializer floatDeserializer = StdKeyDeserializer.forType(Float.class);
 Object key = floatDeserializer.deserializeKey("invalidInput", objectMapper.getDeserializationContext());
 assertNotEquals(Float.valueOf(123.45f), key);
 }

 @Test
 public void testDoubleKeyDeserialization() throws Exception {
 StdKeyDeserializer doubleDeserializer = StdKeyDeserializer.forType(Double.class);
 Object key = doubleDeserializer.deserializeKey("123.456", objectMapper.getDeserializationContext());
 assertEquals(Double.valueOf(123.456), key);
 }

 @Test
 public void testDoubleKeyDeserializationInvalidInput() throws Exception {
 StdKeyDeserializer doubleDeserializer = StdKeyDeserializer.forType(Double.class);
 Object key = doubleDeserializer.deserializeKey("invalidInput", objectMapper.getDeserializationContext());
 assertNotEquals(Double.valueOf(123.456), key);
 }

 @Test
 public void testLocaleKeyDeserialization() throws Exception {
 StdKeyDeserializer localeDeserializer = StdKeyDeserializer.forType(Locale.class);
 Object key = localeDeserializer.deserializeKey("en_US", objectMapper.getDeserializationContext());
 assertEquals(Locale.forLanguageTag("en_US"), key);
 }

 @Test
 public void testLocaleKeyDeserializationInvalidInput() throws Exception {
 StdKeyDeserializer localeDeserializer = StdKeyDeserializer.forType(Locale.class);
 Object key = localeDeserializer.deserializeKey("invalidInput", objectMapper.getDeserializationContext());
 assertNotEquals(Locale.forLanguageTag("en_US"), key);
 }

 @Test
 public void testDateKeyDeserialization() throws Exception {
 StdKeyDeserializer dateDeserializer = StdKeyDeserializer.forType(Date.class);
 Object key = dateDeserializer.deserializeKey("2022-12-31", objectMapper.getDeserializationContext());
 assertEquals(new Date(2022, 11, 30), key);
 }

 @Test
 public void testDateKeyDeserializationInvalidInput() throws Exception {
 StdKeyDeserializer dateDeserializer = StdKeyDeserializer.forType(Date.class);
 Object key = dateDeserializer.deserializeKey("invalidInput", objectMapper.getDeserializationContext());
 assertNotEquals(new Date(2022, 11, 30), key);
 }

 @Test
 public void testCalendarKeyDeserialization() throws Exception {
 StdKeyDeserializer calendarDeserializer = StdKeyDeserializer.forType(Calendar.class);
 Object key = calendarDeserializer.deserializeKey("2022-12-31", objectMapper.getDeserializationContext());
 assertEquals(Calendar.getInstance().set(Calendar.YEAR, 2022).set(Calendar.MONTH, 11).set(Calendar.DAY_OF_MONTH, 30), key);
 }

 @Test
 public void testCalendarKeyDeserializationInvalidInput() throws Exception {
 StdKeyDeserializer calendarDeserializer = StdKeyDeserializer.forType(Calendar.class);
 Object key = calendarDeserializer.deserializeKey("invalidInput", objectMapper.getDeserializationContext());
 assertNotEquals(Calendar.getInstance().set(Calendar.YEAR, 2022).set(Calendar.MONTH, 11).set(Calendar.DAY_OF_MONTH, 30), key);
 }

 @Test
 public void testURIKeyDeserialization() throws Exception {
 StdKeyDeserializer uriDeserializer = StdKeyDeserializer.forType(URI.class);
 Object key = uriDeserializer.deserializeKey("https://www.example.com", objectMapper.getDeserializationContext());
 assertEquals(new URI("https://www.example.com"), key);
 }

 @Test
 public void testURIKeyDeserializationInvalidInput() throws Exception {
 StdKeyDeserializer uriDeserializer = StdKeyDeserializer.forType(URI.class);
 Object key = uriDeserializer.deserializeKey("invalidInput", objectMapper.getDeserializationContext());
 assertNotEquals(new URI("https://www.example.com"), key);
 }

 @Test
 public void testURLKeyDeserialization() throws Exception {
 StdKeyDeserializer urlDeserializer = StdKeyDeserializer.forType(URL.class);
 Object key = urlDeserializer.deserializeKey("http://www.example.com", objectMapper.getDeserializationContext());
 assertEquals(new URL("http://www.example.com"), key);
 }

 @Test
 public void testURLKeyDeserializationInvalidInput() throws Exception {
 StdKeyDeserializer urlDeserializer = StdKeyDeserializer.forType(URL.class);
 Object key = urlDeserializer.deserializeKey("invalidInput", objectMapper.getDeserializationContext());
 assertNotEquals(new URL("http://www.example.com"), key);
 }

 @Test
 public void testClassKeyDeserialization() throws Exception {
 Class<?> keyClass = String[].class;
 StdKeyDeserializer classDeserializer = StdKeyDeserializer.forType(keyClass);
 Object key = classDeserializer.deserializeKey("[Ljava.lang.String;", objectMapper.getDeserializationContext());
 assertEquals(keyClass, key.getClass());
 }

 @Test
 public void testClassKeyDeserializationInvalidInput() throws Exception {
 Class<?> keyClass = String[].class;
 StdKeyDeserializer classDeserializer = StdKeyDeserializer.forType(keyClass);
 Object key = classDeserializer.deserializeKey("invalidInput", objectMapper.getDeserializationContext());
 assertNotEquals(keyClass, key.getClass());
 }

 @Test
 public void testByteArrayKeyDeserialization() throws Exception {
 byte[] key = "base64EncodedByteArray".getBytes();
 StdKeyDeserializer byteArrayDeserializer = StdKeyDeserializer.forType(byte[].class);
 Object deserializedKey = byteArrayDeserializer.deserializeKey(new TextBuffer(objectMapper.getFactory()).writeValueAsString(key), objectMapper.getDeserializationContext());
 assertEquals(key, deserializedKey);
 }

 @Test
 public void testByteArrayKeyDeserializationInvalidInput() throws Exception {
 byte[] key = "invalidBase64EncodedByteArray".getBytes();
 StdKeyDeserializer byteArrayDeserializer = StdKeyDeserializer.forType(byte[].class);
 Object deserializedKey = byteArrayDeserializer.deserializeKey(new TextBuffer(objectMapper.getFactory()).writeValueAsString(key), objectMapper.getDeserializationContext());
 assertNotEquals(key, deserializedKey);
 }

}