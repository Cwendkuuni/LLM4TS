import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDeserializeFile() throws Exception {
 FromStringDeserializer fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
 File expected = new File("path/to/file.txt");
 String input = "File(\"path/to/file.txt\")";
 Object result = fileDeserializer.deserialize(mapper.getFactory().createParser(input), mapper.getFactory().createContext());
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeURL() throws Exception {
 FromStringDeserializer urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
 URL expected = new URL("http://example.com");
 String input = "URL(\"http://example.com\")";
 Object result = urlDeserializer.deserialize(mapper.getFactory().createParser(input), mapper.getFactory().createContext());
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeURI() throws Exception {
 FromStringDeserializer uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
 URI expected = new URI("http://example.com");
 String input = "URI(\"http://example.com\")";
 Object result = uriDeserializer.deserialize(mapper.getFactory().createParser(input), mapper.getFactory().createContext());
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeClass() throws Exception {
 FromStringDeserializer classDeserializer = FromStringDeserializer.findDeserializer(String[].class);
 String input = "class [Ljava.lang.String;";
 Object result = classDeserializer.deserialize(mapper.getFactory().createParser(input), mapper.getFactory().createContext());
 assertArrayEquals(new String[]{"test"}, (String[])result);
 }

 @Test
 public void testDeserializeJavaType() throws Exception {
 FromStringDeserializer javaTypeDeserializer = FromStringDeserializer.findDeserializer(Integer.class);
 JavaType javaType = mapper.getTypeFactory().constructType(String.class);
 String input = javaType.getRawClass().getName();
 Object result = javaTypeDeserializer.deserialize(mapper.getFactory().createParser(input), mapper.getFactory().createContext());
 assertEquals(javaType, result);
 }

 @Test
 public void testDeserializeCurrency() throws Exception {
 FromStringDeserializer currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
 String input = "USD";
 Object result = currencyDeserializer.deserialize(mapper.getFactory().createParser(input), mapper.getFactory().createContext());
 assertEquals(Currency.getInstance("USD"), result);
 }

 @Test(expected = InvalidFormatException.class)
 public void testDeserializeInvalidPattern() throws Exception {
 FromStringDeserializer patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
 String input = "InvalidPattern";
 Object result = patternDeserializer.deserialize(mapper.getFactory().createParser(input), mapper.getFactory().createContext());
 fail("Expected InvalidFormatException but got: " + result.getClass().getName());
 }

 @Test
 public void testDeserializeLocale() throws Exception {
 FromStringDeserializer localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
 String input = "en_US";
 Object result = localeDeserializer.deserialize(mapper.getFactory().createParser(input), mapper.getFactory().createContext());
 assertEquals(Locale.US, result);
 }

 @Test
 public void testDeserializeCharset() throws Exception {
 FromStringDeserializer charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);
 String input = "UTF-8";
 Object result = charsetDeserializer.deserialize(mapper.getFactory().createParser(input), mapper.getFactory().createContext());
 assertEquals(Charset.forName("UTF-8"), result);
 }

 @Test
 public void testDeserializeTimeZone() throws Exception {
 FromStringDeserializer timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
 String input = "UTC";
 Object result = timeZoneDeserializer.deserialize(mapper.getFactory().createParser(input), mapper.getFactory().createContext());
 assertEquals(TimeZone.getTimeZone("UTC"), result);
 }

 @Test(expected = InvalidFormatException.class)
 public void testDeserializeInvalidTimeZone() throws Exception {
 FromStringDeserializer timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
 String input = "InvalidTimeZone";
 Object result = timeZoneDeserializer.deserialize(mapper.getFactory().createParser(input), mapper.getFactory().createContext());
 fail("Expected InvalidFormatException but got: " + result.getClass().getName());
 }

 @Test
 public void testDeserializeInetAddress() throws Exception {
 FromStringDeserializer inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
 String input = "192.168.1.1";
 Object result = inetAddressDeserializer.deserialize(mapper.getFactory().createParser(input), mapper.getFactory().createContext());
 assertEquals(InetAddress.getByName("192.168.1.1"), result);
 }

 @Test
 public void testDeserializeInetSocketAddress() throws Exception {
 FromStringDeserializer inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
 String input = "[192.168.1.1]:80";
 Object result = inetSocketAddressDeserializer.deserialize(mapper.getFactory().createParser(input), mapper.getFactory().createContext());
 assertEquals(new InetSocketAddress("192.168.1.1", 80), result);
 }

 @Test(expected = InvalidFormatException.class)
 public void testDeserializeInvalidInetSocketAddress() throws Exception {
 FromStringDeserializer inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
 String input = "[192.168.1.1]:80,90";
 Object result = inetSocketAddressDeserializer.deserialize(mapper.getFactory().createParser(input), mapper.getFactory().createContext());
 fail("Expected InvalidFormatException but got: " + result.getClass().getName());
 }

 @Test
 public void testDeserializeEmbeddedObject() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Object.class);
 String input = "{\"class\":\"java.lang.String\",\"value\":\"Hello, World!\"}";
 Object result = deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getFactory().createContext());
 assertEquals("Hello, World!", result);
 }

 @Test
 public void testDeserializeEmptyString() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Object.class);
 String input = "";
 Object result = deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getFactory().createContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeEmptyStringForStdURI() throws Exception {
 FromStringDeserializer uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
 String input = "";
 Object result = uriDeserializer.deserialize(mapper.getFactory().createParser(input), mapper.getFactory().createContext());
 assertEquals(URI.create(""), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidInput() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Object.class);
 String input = "InvalidInput";
 Object result = deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getFactory().createContext());
 fail("Expected JsonMappingException but got: " + result.getClass().getName());
 }
}