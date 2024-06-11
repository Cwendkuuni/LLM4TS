package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdKeyDeserializerTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testBooleanDeserializer() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer booleanDeserializer = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext context = new DeserializationContext(null, null, null);
 String trueKey = "true";
 String falseKey = "false";

 assertEquals(Boolean.TRUE, booleanDeserializer.deserializeKey(trueKey, context));
 assertEquals(Boolean.FALSE, booleanDeserializer.deserializeKey(falseKey, context));
 }

 @Test
 public void testByteDeserializer() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer byteDeserializer = StdKeyDeserializer.forType(Byte.class);
 DeserializationContext context = new DeserializationContext(null, null, null);

 assertEquals((byte) 0, byteDeserializer.deserializeKey("0", context));
 assertEquals((byte) 127, byteDeserializer.deserializeKey("127", context));
 assertEquals((byte) 128, byteDeserializer.deserializeKey("128", context)); // should throw an exception
 }

 @Test
 public void testShortDeserializer() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer shortDeserializer = StdKeyDeserializer.forType(Short.class);
 DeserializationContext context = new DeserializationContext(null, null, null);

 assertEquals((short) 0, shortDeserializer.deserializeKey("0", context));
 assertEquals((short) 127, shortDeserializer.deserializeKey("127", context));
 assertEquals((short) 128, shortDeserializer.deserializeKey("128", context)); // should throw an exception
 }

 @Test
 public void testCharDeserializer() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer charDeserializer = StdKeyDeserializer.forType(Character.class);
 DeserializationContext context = new DeserializationContext(null, null, null);

 assertEquals('a', charDeserializer.deserializeKey("a", context));
 assertEquals('z', charDeserializer.deserializeKey("z", context));
 }

 @Test
 public void testIntDeserializer() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer intDeserializer = StdKeyDeserializer.forType(Integer.class);
 DeserializationContext context = new DeserializationContext(null, null, null);

 assertEquals(0, intDeserializer.deserializeKey("0", context));
 assertEquals(-128, intDeserializer.deserializeKey("-128", context));
 assertEquals(Integer.MAX_VALUE, intDeserializer.deserializeKey("2147483647", context));
 }

 @Test
 public void testLongDeserializer() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer longDeserializer = StdKeyDeserializer.forType(Long.class);
 DeserializationContext context = new DeserializationContext(null, null, null);

 assertEquals(0L, longDeserializer.deserializeKey("0", context));
 assertEquals(-128L, longDeserializer.deserializeKey("-128", context));
 assertEquals(Long.MAX_VALUE, longDeserializer.deserializeKey("9223372036854775807L", context));
 }

 @Test
 public void testDateDeserializer() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer dateDeserializer = StdKeyDeserializer.forType(Date.class);
 DeserializationContext context = new DeserializationContext(null, null, null);

 assertEquals(new Date(), dateDeserializer.deserializeKey("", context)); // empty string should return current date
 assertEquals(new Date(System.currentTimeMillis() - 1000L), dateDeserializer.deserializeKey("2021-09-27T12:00:00.999Z", context));
 }

 @Test
 public void testCalendarDeserializer() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer calendarDeserializer = StdKeyDeserializer.forType(Calendar.class);
 DeserializationContext context = new DeserializationContext(null, null, null);

 assertEquals(Calendar.getInstance(), calendarDeserializer.deserializeKey("", context)); // empty string should return current calendar
 assertEquals(Calendar.getInstance(), calendarDeserializer.deserializeKey("2021-09-27", context));
 }

 @Test
 public void testUuidDeserializer() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer uuidDeserializer = StdKeyDeserializer.forType(UUID.class);
 DeserializationContext context = new DeserializationContext(null, null, null);

 assertEquals(UUID.randomUUID(), uuidDeserializer.deserializeKey("", context)); // empty string should return a random UUID
 assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), uuidDeserializer.deserializeKey("123e4567-e89b-12d3-a456-426614174000", context));
 }

 @Test
 public void testUriDeserializer() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer uriDeserializer = StdKeyDeserializer.forType(URI.class);
 DeserializationContext context = new DeserializationContext(null, null, null);

 assertEquals(new URI(""), uriDeserializer.deserializeKey("", context)); // empty string should return an empty URI
 assertEquals(new URI("https://example.com"), uriDeserializer.deserializeKey("https://example.com", context));
 }

 @Test
 public void testUrlDeserializer() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer urlDeserializer = StdKeyDeserializer.forType(URL.class);
 DeserializationContext context = new DeserializationContext(null, null, null);

 assertEquals(new URL(""), urlDeserializer.deserializeKey("", context)); // empty string should return an empty URL
 assertEquals(new URL("https://example.com"), urlDeserializer.deserializeKey("https://example.com", context));
 }

 @Test
 public void testClassDeserializer() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer classDeserializer = StdKeyDeserializer.forType(String.class);
 DeserializationContext context = new DeserializationContext(null, null, null);

 assertEquals(String.class, classDeserializer.deserializeKey("String.class", context));
 assertEquals(getClass().getName(), classDeserializer.deserializeKey(getClass().getName(), context));
 }

 @Test
 public void testLocaleDeserializer() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer localeDeserializer = StdKeyDeserializer.forType(Locale.class);
 DeserializationContext context = new DeserializationContext(null, null, null);

 assertEquals(Locale.getDefault(), localeDeserializer.deserializeKey("", context)); // empty string should return the default locale
 assertEquals(Locale.US, localeDeserializer.deserializeKey("en_US", context));
 }

 @Test
 public void testCurrencyDeserializer() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer currencyDeserializer = StdKeyDeserializer.forType(Currency.class);
 DeserializationContext context = new DeserializationContext(null, null, null);

 assertEquals(Currency.getInstance(Locale.US), currencyDeserializer.deserializeKey("", context)); // empty string should return the default currency
 assertEquals(Currency.getInstance("USD"), currencyDeserializer.deserializeKey("USD", context));
 }
}