import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StandardDeserializationProblemHandler;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private DeserializationContext context;
 private ObjectMapper mapper;

 @Before
 public void setup() throws Exception {
 context = new DeserializationContext(new StandardDeserializationProblemHandler());
 mapper = new ObjectMapper();
 }

 @Test
 public void testBooleanDeserializer() throws IOException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
 assertEquals(Boolean.TRUE, deserializer.deserializeKey("true", context));
 assertEquals(Boolean.FALSE, deserializer.deserializeKey("false", context));
 }

 @Test
 public void testByteDeserializer() throws IOException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
 assertEquals((byte) 127, deserializer.deserializeKey("127", context));
 try {
 deserializer.deserializeKey("256", context);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testShortDeserializer() throws IOException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
 assertEquals((short) 32767, deserializer.deserializeKey("32767", context));
 try {
 deserializer.deserializeKey("32768", context);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCharDeserializer() throws IOException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
 assertEquals('a', deserializer.deserializeKey("a", context));
 }

 @Test
 public void testIntDeserializer() throws IOException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
 assertEquals(12345, deserializer.deserializeKey("12345", context));
 }

 @Test
 public void testLongDeserializer() throws IOException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
 assertEquals(Long.MAX_VALUE, deserializer.deserializeKey(Long.toString(Long.MAX_VALUE), context));
 try {
 deserializer.deserializeKey(Long.toString(Long.MAX_VALUE + 1), context);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFloatDeserializer() throws IOException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
 float expected = 1.23f;
 assertEquals(expected, deserializer.deserializeKey(Float.toString(expected), context), 0.001f);
 }

 @Test
 public void testDoubleDeserializer() throws IOException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
 double expected = 1.23;
 assertEquals(expected, deserializer.deserializeKey(Double.toString(expected), context), 0.001);
 }

 @Test
 public void testLocaleDeserializer() throws IOException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
 Locale expected = Locale.US;
 assertEquals(expected, deserializer.deserializeKey("en_US", context));
 }

 @Test
 public void testDateDeserializer() throws IOException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
 Date expected = new Date(System.currentTimeMillis());
 assertEquals(expected, deserializer.deserializeKey(mapper.writerWithZeroTimeZone().writeValueAsString(expected), context));
 }

 @Test
 public void testCalendarDeserializer() throws IOException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
 Calendar expected = Calendar.getInstance();
 assertEquals(expected, deserializer.deserializeKey(mapper.writerWithZeroTimeZone().writeValueAsString(expected.getTime()), context));
 }

 @Test
 public void testUriDeserializer() throws IOException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
 URI expected = new URI("http://example.com");
 assertEquals(expected, deserializer.deserializeKey(expected.toASCIIString(), context));
 }

 @Test
 public void testUrlDeserializer() throws IOException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
 URL expected = new URL("http://example.com");
 assertEquals(expected, deserializer.deserializeKey(expected.toString(), context));
 }

 @Test
 public void testClassDeserializer() throws IOException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
 Class<?> expected = String.class;
 assertEquals(expected, deserializer.deserializeKey(expected.getName(), context));
 }

 @Test
 public void testEnumDeserializer() throws IOException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(MyEnum.class);
 MyEnum expected = MyEnum.VALUE_1;
 assertEquals(expected, deserializer.deserializeKey(expected.name(), context));
 }

 @Test
 public void testExceptionHandling() throws IOException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
 try {
 deserializer.deserializeKey(null, context);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testExceptionHandlingEmptyString() throws IOException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
 try {
 deserializer.deserializeKey("", context);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testExceptionHandlingInvalidInput() throws IOException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
 try {
 deserializer.deserializeKey("invalidInput", context);
 fail();
 } catch (IOException e) {
 // expected
 }
 }
}