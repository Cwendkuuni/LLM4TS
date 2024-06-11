import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private DeserializationContext ctxt;
 private ObjectMapper mapper;

 @Before
 public void setUp() throws Exception {
 ctxt = new TestDeserializationContext();
 mapper = new ObjectMapper();
 }

 @Test
 public void testBooleanDeserializer() throws JsonProcessingException {
 String input = "true";
 Object result = testDeserializer("StdKeyDeserializer", input, Boolean.class, ctxt);
 assertTrue((Boolean) result);
 }

 @Test
 public void testBooleanDeserializerNullInput() throws JsonProcessingException {
 String input = null;
 Object result = testDeserializer("StdKeyDeserializer", input, Boolean.class, ctxt);
 assertNull(result);
 }

 @Test
 public void testBooleanDeserializerInvalidInput() throws JsonProcessingException {
 String input = "invalid";
 Object result = testDeserializer("StdKeyDeserializer", input, Boolean.class, ctxt);
 assertNull(result);
 }

 @Test
 public void testByteDeserializer() throws JsonProcessingException {
 String input = "10";
 Object result = testDeserializer("StdKeyDeserializer", input, Byte.class, ctxt);
 assertEquals((Byte) new Byte((byte) 10), result);
 }

 @Test
 public void testByteDeserializerNegativeNumber() throws JsonProcessingException {
 String input = "-10";
 Object result = testDeserializer("StdKeyDeserializer", input, Byte.class, ctxt);
 assertEquals((Byte) new Byte((byte) -10), result);
 }

 @Test
 public void testByteDeserializerOutOfRangeNumber() throws JsonProcessingException {
 String input = "256";
 Object result = testDeserializer("StdKeyDeserializer", input, Byte.class, ctxt);
 assertNull(result);
 }

 @Test
 public void testShortDeserializer() throws JsonProcessingException {
 String input = "100";
 Object result = testDeserializer("StdKeyDeserializer", input, Short.class, ctxt);
 assertEquals((Short) new Short((short) 100), result);
 }

 @Test
 public void testShortDeserializerNegativeNumber() throws JsonProcessingException {
 String input = "-100";
 Object result = testDeserializer("StdKeyDeserializer", input, Short.class, ctxt);
 assertEquals((Short) new Short((short) -100), result);
 }

 @Test
 public void testShortDeserializerOutOfRangeNumber() throws JsonProcessingException {
 String input = "32000";
 Object result = testDeserializer("StdKeyDeserializer", input, Short.class, ctxt);
 assertNull(result);
 }

 @Test
 public void testCharDeserializer() throws JsonProcessingException {
 String input = "A";
 Object result = testDeserializer("StdKeyDeserializer", input, Character.class, ctxt);
 assertEquals('A', result);
 }

 @Test
 public void testCharDeserializerInvalidCharacter() throws JsonProcessingException {
 String input = "\\u0001";
 Object result = testDeserializer("StdKeyDeserializer", input, Character.class, ctxt);
 assertNull(result);
 }

 @Test
 public void testIntDeserializer() throws JsonProcessingException {
 String input = "100";
 Object result = testDeserializer("StdKeyDeserializer", input, Integer.class, ctxt);
 assertEquals((Integer) new Integer(100), result);
 }

 @Test
 public void testIntDeserializerNegativeNumber() throws JsonProcessingException {
 String input = "-100";
 Object result = testDeserializer("StdKeyDeserializer", input, Integer.class, ctxt);
 assertEquals((Integer) new Integer(-100), result);
 }

 @Test
 public void testIntDeserializerOutOfRangeNumber() throws JsonProcessingException {
 String input = "2147483648";
 Object result = testDeserializer("StdKeyDeserializer", input, Integer.class, ctxt);
 assertNull(result);
 }

 @Test
 public void testLongDeserializer() throws JsonProcessingException {
 String input = "10000000000";
 Object result = testDeserializer("StdKeyDeserializer", input, Long.class, ctxt);
 assertEquals((Long) new Long(10000000000L), result);
 }

 @Test
 public void testLongDeserializerNegativeNumber() throws JsonProcessingException {
 String input = "-10000000000";
 Object result = testDeserializer("StdKeyDeserializer", input, Long.class, ctxt);
 assertEquals((Long) new Long(-10000000000L), result);
 }

 @Test
 public void testLongDeserializerOutOfRangeNumber() throws JsonProcessingException {
 String input = "9223372036854775808";
 Object result = testDeserializer("StdKeyDeserializer", input, Long.class, ctxt);
 assertNull(result);
 }

 @Test
 public void testDoubleDeserializer() throws JsonProcessingException {
 String input = "10.5";
 Object result = testDeserializer("StdKeyDeserializer", input, Double.class, ctxt);
 assertEquals((Double) new Double(10.5), result);
 }

 @Test
 public void testDoubleDeserializerInvalidFormat() throws JsonProcessingException {
 String input = "invalid";
 Object result = testDeserializer("StdKeyDeserializer", input, Double.class, ctxt);
 assertNull(result);
 }

 @Test
 public void testDateDeserializer() throws JsonProcessingException, Exception {
 String input = "2019-12-31";
 Object result = testDeserializer("StdKeyDeserializer", input, Date.class, ctxt);
 assertEquals(new Date(1577967999000L), result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDateDeserializerInvalidFormat() throws JsonProcessingException {
 String input = "invalid";
 testDeserializer("StdKeyDeserializer", input, Date.class, ctxt);
 }

 @Test
 public void testCalendarDeserializer() throws JsonProcessingException, Exception {
 String input = "2019-12-31";
 Object result = testDeserializer("StdKeyDeserializer", input, Calendar.class, ctxt);
 assertEquals(Calendar.getInstance().getTime(), result.getTime());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCalendarDeserializerInvalidFormat() throws JsonProcessingException {
 String input = "invalid";
 testDeserializer("StdKeyDeserializer", input, Calendar.class, ctxt);
 }

 @Test
 public void testURIDeserializer() throws Exception {
 String input = "http://example.com";
 Object result = testDeserializer("StdKeyDeserializer", input, URI.class, ctxt);
 assertEquals(new URI("http://example.com"), result);
 }

 @Test(expected = MalformedURLException.class)
 public void testURIDeserializerInvalidFormat() throws Exception {
 String input = "invalid";
 testDeserializer("StdKeyDeserializer", input, URI.class, ctxt);
 }

 @Test
 public void testURLDeserializer() throws Exception {
 String input = "http://example.com";
 Object result = testDeserializer("StdKeyDeserializer", input, URL.class, ctxt);
 assertEquals(new URL("http://example.com"), result);
 }

 @Test(expected = MalformedURLException.class)
 public void testURLDeserializerInvalidFormat() throws Exception {
 String input = "invalid";
 testDeserializer("StdKeyDeserializer", input, URL.class, ctxt);
 }

 @Test
 public void testLocaleDeserializer() throws JsonProcessingException {
 String input = "en_US";
 Object result = testDeserializer("StdKeyDeserializer", input, Locale.class, ctxt);
 assertEquals(Locale.US, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLocaleDeserializerInvalidFormat() throws JsonProcessingException {
 String input = "invalid";
 testDeserializer("StdKeyDeserializer", input, Locale.class, ctxt);
 }

 @Test
 public void testCurrencyDeserializer() throws JsonProcessingException {
 String input = "USD";
 Object result = testDeserializer("StdKeyDeserializer", input, Currency.class, ctxt);
 assertEquals(Currency.getInstance("USD"), result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCurrencyDeserializerInvalidFormat() throws JsonProcessingException {
 String input = "invalid";
 testDeserializer("StdKeyDeserializer", input, Currency.class, ctxt);
 }

 // Testing delegating key deserializer

 @Test
 public void testDelegatingStringDeserializer() throws JsonProcessingException {
 String input = "test";
 Object result = testDeserializer("StdKeyDeserializer", input, String.class, ctxt);
 assertEquals("test", result);
 }

 private <T> T testDeserializer(String className, String key, Class<T> clazz, DeserializationContext ctxt)
 throws JsonProcessingException {
 JsonDeserializer<T> deserializer = mapper.findDeserializer(clazz);
 ObjectNode node = mapper.valueToTree(key);
 Object result = deserializer.deserialize(node, ctxt);
 return result;
 }
}