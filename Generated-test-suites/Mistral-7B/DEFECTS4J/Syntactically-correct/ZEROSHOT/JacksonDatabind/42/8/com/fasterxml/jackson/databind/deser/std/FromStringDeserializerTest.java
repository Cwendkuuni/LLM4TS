package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testDeserializeFile() throws JsonProcessingException {
 FromStringDeserializer<File> fileDeserializer = new FromStringDeserializer<File>(File.class);
 String fileString = "/path/to/file.txt";
 File expectedFile = new File("/path/to/file.txt");
 File actualFile = fileDeserializer.deserialize(objectMapper.getFactory().createParser(fileString), objectMapper.getDeserializationContext());
 assertEquals(expectedFile, actualFile);
 }

 @Test
 public void testDeserializeUrl() throws JsonProcessingException {
 FromStringDeserializer<URL> urlDeserializer = new FromStringDeserializer<URL>(URL.class);
 String urlString = "http://example.com";
 URL expectedUrl = new URL("http://example.com");
 URL actualUrl = urlDeserializer.deserialize(objectMapper.getFactory().createParser(urlString), objectMapper.getDeserializationContext());
 assertEquals(expectedUrl, actualUrl);
 }

 @Test
 public void testDeserializeUri() throws JsonProcessingException {
 FromStringDeserializer<URI> uriDeserializer = new FromStringDeserializer<URI>(URI.class);
 String uriString = "http://example.com/path/to/resource";
 URI expectedUri = new URI("http", "example.com", "/path/to/resource");
 URI actualUri = uriDeserializer.deserialize(objectMapper.getFactory().createParser(uriString), objectMapper.getDeserializationContext());
 assertEquals(expectedUri, actualUri);
 }

 @Test
 public void testDeserializeClass() throws JsonProcessingException {
 FromStringDeserializer<Class<?>> classDeserializer = new FromStringDeserializer<Class<?>>(Class.class);
 String className = "java.lang.String";
 Class<?> expectedClass = String.class;
 Class<?> actualClass = classDeserializer.deserialize(objectMapper.getFactory().createParser(className), objectMapper.getDeserializationContext());
 assertEquals(expectedClass, actualClass);
 }

 @Test
 public void testDeserializeJavaType() throws JsonProcessingException {
 FromStringDeserializer<JavaType> javaTypeDeserializer = new FromStringDeserializer<JavaType>(JavaType.class);
 String javaTypeString = "java.util.List<java.lang.String>";
 JavaType expectedJavaType = objectMapper.getTypeFactory().constructType(JavaType.class, "java.util.List<java.lang.String>");
 JavaType actualJavaType = javaTypeDeserializer.deserialize(objectMapper.getFactory().createParser(javaTypeString), objectMapper.getDeserializationContext());
 assertEquals(expectedJavaType, actualJavaType);
 }

 @Test
 public void testDeserializeCurrency() throws JsonProcessingException {
 FromStringDeserializer<Currency> currencyDeserializer = new FromStringDeserializer<Currency>(Currency.class);
 String currencyString = "USD";
 Currency expectedCurrency = Currency.getInstance("USD");
 Currency actualCurrency = currencyDeserializer.deserialize(objectMapper.getFactory().createParser(currencyString), objectMapper.getDeserializationContext());
 assertEquals(expectedCurrency, actualCurrency);
 }

 @Test
 public void testDeserializePattern() throws JsonProcessingException {
 FromStringDeserializer<Pattern> patternDeserializer = new FromStringDeserializer<Pattern>(Pattern.class);
 String patternString = "[A-Za-z0-9]+";
 Pattern expectedPattern = Pattern.compile("[A-Za-z0-9]+");
 Pattern actualPattern = patternDeserializer.deserialize(objectMapper.getFactory().createParser(patternString), objectMapper.getDeserializationContext());
 assertEquals(expectedPattern, actualPattern);
 }

 @Test
 public void testDeserializeLocale() throws JsonProcessingException {
 FromStringDeserializer<Locale> localeDeserializer = new FromStringDeserializer<Locale>(Locale.class);
 String localeString = "en_US";
 Locale expectedLocale = Locale.forLanguageTag("en_US");
 Locale actualLocale = localeDeserializer.deserialize(objectMapper.getFactory().createParser(localeString), objectMapper.getDeserializationContext());
 assertEquals(expectedLocale, actualLocale);
 }

 @Test
 public void testDeserializeCharset() throws JsonProcessingException {
 FromStringDeserializer<Charset> charsetDeserializer = new FromStringDeserializer<Charset>(Charset.class);
 String charsetString = "UTF-8";
 Charset expectedCharset = Charset.forName("UTF-8");
 Charset actualCharset = charsetDeserializer.deserialize(objectMapper.getFactory().createParser(charsetString), objectMapper.getDeserializationContext());
 assertEquals(expectedCharset, actualCharset);
 }

 @Test
 public void testDeserializeTimeZone() throws JsonProcessingException {
 FromStringDeserializer<TimeZone> timeZoneDeserializer = new FromStringDeserializer<TimeZone>(TimeZone.class);
 String timeZoneString = "America/New_York";
 TimeZone expectedTimeZone = TimeZone.getTimeZone("America/New_York");
 TimeZone actualTimeZone = timeZoneDeserializer.deserialize(objectMapper.getFactory().createParser(timeZoneString), objectMapper.getDeserializationContext());
 assertEquals(expectedTimeZone, actualTimeZone);
 }

 @Test
 public void testDeserializeInetAddress() throws JsonProcessingException {
 FromStringDeserializer<InetAddress> inetAddressDeserializer = new FromStringDeserializer<InetAddress>(InetAddress.class);
 String inetAddressString = "192.168.1.1";
 InetAddress expectedInetAddress = InetAddress.getByName("192.168.1.1");
 InetAddress actualInetAddress = inetAddressDeserializer.deserialize(objectMapper.getFactory().createParser(inetAddressString), objectMapper.getDeserializationContext());
 assertEquals(expectedInetAddress, actualInetAddress);
 }

 @Test
 public void testDeserializeInetSocketAddress() throws JsonProcessingException {
 FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);
 String inetSocketAddressString = "[255.255.255.255]:8080";
 InetSocketAddress expectedInetSocketAddress = new InetSocketAddress("255.255.255.255", 8080);
 InetSocketAddress actualInetSocketAddress = inetSocketAddressDeserializer.deserialize(objectMapper.getFactory().createParser(inetSocketAddressString), objectMapper.getDeserializationContext());
 assertEquals(expectedInetSocketAddress, actualInetSocketAddress);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidType() throws JsonProcessingException {
 FromStringDeserializer<Double> doubleDeserializer = new FromStringDeserializer<Double>(Double.class);
 String invalidTypeString = "java.util.List<java.lang.String>";
 doubleDeserializer.deserialize(objectMapper.getFactory().createParser(invalidTypeString), objectMapper.getDeserializationContext());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmptyStringFile() throws JsonProcessingException {
 FromStringDeserializer<File> emptyStringFileDeserializer = new FromStringDeserializer<File>(File.class);
 String emptyString = "";
 File actualFile = emptyStringFileDeserializer._deserializeFromEmptyString();
 assertNull(actualFile);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmptyStringUri() throws JsonProcessingException {
 FromStringDeserializer<URI> emptyStringUriDeserializer = new FromStringDeserializer<URI>(URI.class);
 String emptyString = "";
 URI actualUri = emptyStringUriDeserializer._deserializeFromEmptyString();
 assertEquals(URI.create(""), actualUri);
 }
}