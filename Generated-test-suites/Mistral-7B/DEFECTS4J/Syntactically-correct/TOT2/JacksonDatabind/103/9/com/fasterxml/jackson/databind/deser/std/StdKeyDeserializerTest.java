import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private ObjectMapper objectMapper;

 @Override
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 /**
 * Test for deserializing a valid Boolean key
 */
 @Test
 public void testDeserializeBooleanKey() throws IOException, JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 Object key = stdKeyDeserializer.deserializeKey("true", objectMapper.getDeserializationContext());
 assertTrue(key instanceof Boolean);
 assertEquals(Boolean.TRUE, key);
 }

 /**
 * Test for deserializing a valid Byte key
 */
 @Test
 public void testDeserializeByteKey() throws IOException, JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Byte.class);
 Object key = stdKeyDeserializer.deserializeKey("127", objectMapper.getDeserializationContext());
 assertTrue(key instanceof Byte);
 assertEquals((Byte) 127, key);
 }

 /**
 * Test for deserializing a valid Short key
 */
 @Test
 public void testDeserializeShortKey() throws IOException, JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Short.class);
 Object key = stdKeyDeserializer.deserializeKey("32767", objectMapper.getDeserializationContext());
 assertTrue(key instanceof Short);
 assertEquals((Short) 32767, key);
 }

 /**
 * Test for deserializing a valid Char key
 */
 @Test
 public void testDeserializeCharKey() throws IOException, JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Character.class);
 Object key = stdKeyDeserializer.deserializeKey("A", objectMapper.getDeserializationContext());
 assertTrue(key instanceof Character);
 assertEquals('A', key);
 }

 /**
 * Test for deserializing a valid Integer key
 */
 @Test
 public void testDeserializeIntegerKey() throws IOException, JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Integer.class);
 Object key = stdKeyDeserializer.deserializeKey("42", objectMapper.getDeserializationContext());
 assertTrue(key instanceof Integer);
 assertEquals(42, key);
 }

 /**
 * Test for deserializing a valid Long key
 */
 @Test
 public void testDeserializeLongKey() throws IOException, JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Long.class);
 Object key = stdKeyDeserializer.deserializeKey("9223372036854775807", objectMapper.getDeserializationContext());
 assertTrue(key instanceof Long);
 assertEquals(Long.MAX_VALUE, key);
 }

 /**
 * Test for deserializing a valid Float key
 */
 @Test
 public void testDeserializeFloatKey() throws IOException, JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Float.class);
 Object key = stdKeyDeserializer.deserializeKey("3.14159265358979323846", objectMapper.getDeserializationContext());
 assertTrue(key instanceof Float);
 assertEquals(Float.valueOf(3.14159265358979323846f), key);
 }

 /**
 * Test for deserializing a valid Double key
 */
 @Test
 public void testDeserializeDoubleKey() throws IOException, JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Double.class);
 Object key = stdKeyDeserializer.deserializeKey("3.141592653589793238462643383279502884131", objectMapper.getDeserializationContext());
 assertTrue(key instanceof Double);
 assertEquals(Double.valueOf(3.141592653589793238462643383279502884131), key);
 }

 /**
 * Test for deserializing a valid URI key
 */
 @Test
 public void testDeserializeURIKey() throws IOException, JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(URI.class);
 Object key = stdKeyDeserializer.deserializeKey("http://www.example.com", objectMapper.getDeserializationContext());
 assertTrue(key instanceof URI);
 assertEquals(new URI("http://www.example.com"), key);
 }

 /**
 * Test for deserializing a valid URL key
 */
 @Test
 public void testDeserializeURLKey() throws IOException, JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(URL.class);
 Object key = stdKeyDeserializer.deserializeKey("http://www.example.com", objectMapper.getDeserializationContext());
 assertTrue(key instanceof URL);
 assertEquals(new URL("http://www.example.com"), key);
 }

 /**
 * Test for deserializing a valid Class key
 */
 @Test
 public void testDeserializeClassKey() throws IOException, JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(String.class);
 Object key = stdKeyDeserializer.deserializeKey("java.lang.String", objectMapper.getDeserializationContext());
 assertTrue(key instanceof Class<?>);
 assertEquals(String.class, key);
 }

 /**
 * Test for deserializing a null key
 */
 @Test
 public void testDeserializeNullKey() throws IOException, JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(String.class);
 Object key = stdKeyDeserializer.deserializeKey(null, objectMapper.getDeserializationContext());
 assertNull(key);
 }

 /**
 * Test for deserializing an invalid key of unsupported type
 */
 @Test(expected = IOException.class)
 public void testDeserializeInvalidKey() throws IOException, JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Double.class);
 Object key = stdKeyDeserializer.deserializeKey("[1,2,3]", objectMapper.getDeserializationContext());
 fail("Unexpected success");
 }

 /**
 * Test for deserializing an invalid key of unrecognized type
 */
 @Test(expected = IOException.class)
 public void testDeserializeUnknownKeyType() throws IOException, JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(String.class);
 Object key = stdKeyDeserializer.deserializeKey("InvalidType", objectMapper.getDeserializationContext());
 fail("Unexpected success");
 }

 /**
 * Test for deserializing an invalid key containing non-parseable value
 */
 @Test(expected = IOException.class)
 public void testDeserializeInvalidValue() throws IOException, JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Integer.class);
 Object key = stdKeyDeserializer.deserializeKey("InvalidValue", objectMapper.getDeserializationContext());
 fail("Unexpected success");
 }

 /**
 * Test for deserializing an invalid key containing empty string
 */
 @Test
 public void testDeserializeEmptyStringKey() throws IOException, JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(String.class);
 Object key = stdKeyDeserializer.deserializeKey("", objectMapper.getDeserializationContext());
 assertNull(key);
 }

 /**
 * Test for deserializing an invalid key containing a whitespace string
 */
 @Test
 public void testDeserializeWhitespaceStringKey() throws IOException, JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(String.class);
 Object key = stdKeyDeserializer.deserializeKey(" ", objectMapper.getDeserializationContext());
 assertNull(key);
 }
}