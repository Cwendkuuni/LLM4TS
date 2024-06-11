package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new JsonMapper();
 }

 @Test
 public void testFileDeserialization() throws JsonProcessingException {
 FileDeserializer fileDeserializer = new FileDeserializer(File.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String fileString = "/path/to/file.txt";
 Object result = fileDeserializer.deserialize(objectMapper.getFactory().createParser(fileString), ctxt);
 assertEquals(new File(fileString), result);
 }

 @Test
 public void testURLDeserialization() throws JsonProcessingException {
 URLDeserializer urlDeserializer = new URLDeserializer(URL.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String urlString = "http://www.example.com";
 Object result = urlDeserializer.deserialize(objectMapper.getFactory().createParser(urlString), ctxt);
 assertEquals(new URL(urlString), result);
 }

 @Test
 public void testURIDeserialization() throws JsonProcessingException {
 URIDeserializer uriDeserializer = new URIDeserializer(URI.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String uriString = "http://www.example.com/path/to/resource";
 Object result = uriDeserializer.deserialize(objectMapper.getFactory().createParser(uriString), ctxt);
 assertEquals(new URI(uriString), result);
 }

 @Test
 public void testClassDeserialization() throws JsonProcessingException {
 ClassDeserializer classDeserializer = new ClassDeserializer(Class.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String className = "java.lang.String";
 Object result = classDeserializer.deserialize(objectMapper.getFactory().createParser(className), ctxt);
 assertEquals(String.class, result.getClass());
 }

 @Test
 public void testJavaTypeDeserialization() throws JsonProcessingException {
 JavaTypeDeserializer javaTypeDeserializer = new JavaTypeDeserializer(JavaType.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String javaTypeString = "class [B";
 Object result = javaTypeDeserializer.deserialize(objectMapper.getFactory().createParser(javaTypeString), ctxt);
 assertEquals(JavaType.constructType(byte[].class), result);
 }

 @Test
 public void testCurrencyDeserialization() throws JsonProcessingException {
 CurrencyDeserializer currencyDeserializer = new CurrencyDeserializer(Currency.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String currencyString = "USD";
 Object result = currencyDeserializer.deserialize(objectMapper.getFactory().createParser(currencyString), ctxt);
 assertEquals(Currency.getInstance(Locale.US), result);
 }

 @Test
 public void testPatternDeserialization() throws JsonProcessingException {
 PatternDeserializer patternDeserializer = new PatternDeserializer(Pattern.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String patternString = "\\w+@\\w+\\.\\w+";
 Object result = patternDeserializer.deserialize(objectMapper.getFactory().createParser(patternString), ctxt);
 assertEquals(Pattern.compile(patternString), result);
 }

 @Test
 public void testLocaleDeserialization() throws JsonProcessingException {
 LocaleDeserializer localeDeserializer = new LocaleDeserializer(Locale.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String localeString = "en_US";
 Object result = localeDeserializer.deserialize(objectMapper.getFactory().createParser(localeString), ctxt);
 assertEquals(new Locale("en", "US"), result);
 }

 @Test
 public void testCharsetDeserialization() throws JsonProcessingException {
 CharsetDeserializer charsetDeserializer = new CharsetDeserializer(Charset.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String charsetString = "UTF-8";
 Object result = charsetDeserializer.deserialize(objectMapper.getFactory().createParser(charsetString), ctxt);
 assertEquals(Charset.forName(charsetString), result);
 }

 @Test
 public void testTimeZoneDeserialization() throws JsonProcessingException {
 TimeZoneDeserializer timeZoneDeserializer = new TimeZoneDeserializer(TimeZone.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String timeZoneString = "America/Los_Angeles";
 Object result = timeZoneDeserializer.deserialize(objectMapper.getFactory().createParser(timeZoneString), ctxt);
 assertEquals(TimeZone.getTimeZone(timeZoneString), result);
 }

 @Test
 public void testInetAddressDeserialization() throws JsonProcessingException {
 InetAddressDeserializer inetAddressDeserializer = new InetAddressDeserializer(InetAddress.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String inetAddressString = "192.168.1.1";
 Object result = inetAddressDeserializer.deserialize(objectMapper.getFactory().createParser(inetAddressString), ctxt);
 assertEquals(InetAddress.getByName(inetAddressString), result);
 }

 @Test
 public void testInetSocketAddressDeserialization() throws JsonProcessingException {
 InetSocketAddressDeserializer inetSocketAddressDeserializer = new InetSocketAddressDeserializer(InetSocketAddress.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 String inetSocketAddressString = "[255.255.255.255]:8080";
 Object result = inetSocketAddressDeserializer.deserialize(objectMapper.getFactory().createParser(inetSocketAddressString), ctxt);
 assertEquals(new InetSocketAddress(InetAddress.getByName("255.255.255.255"), 8080), result);
 }

 @Test
 public void testEmptyStringDeserialization() throws JsonProcessingException {
 // Test for databind#398
 FileDeserializer fileDeserializer = new FileDeserializer(File.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 Object result = fileDeserializer.deserializeFromEmptyString();
 assertNull(result);
 }
}