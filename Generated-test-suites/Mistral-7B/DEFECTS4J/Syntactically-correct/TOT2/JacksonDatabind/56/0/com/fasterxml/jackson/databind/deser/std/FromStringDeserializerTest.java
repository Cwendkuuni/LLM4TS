import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testFileDeserialization() throws Exception {
 File file = new File("testFile.txt");
 FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
 String json = mapper.writeValueAsString(file);
 File deserializedObject = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
 assertEquals(file, deserializedObject);
 }

 @Test
 public void testURLDeserialization() throws Exception {
 URL url = new URL("http://example.com");
 FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
 String json = mapper.writeValueAsString(url);
 URL deserializedObject = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
 assertEquals(url, deserializedObject);
 }

 @Test
 public void testURIDeserialization() throws Exception {
 URI uri = new URI("http://example.com");
 FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
 String json = mapper.writeValueAsString(uri);
 URI deserializedObject = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
 assertEquals(uri, deserializedObject);
 }

 @Test
 public void testClassDeserialization() throws Exception {
 String className = String.class.getName();
 FromStringDeserializer<Class<?>> deserializer = FromStringDeserializer.findDeserializer(Class.class);
 String json = mapper.writeValueAsString(String.class);
 Class<?> deserializedObject = (Class<?>) deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
 assertEquals(className, deserializedObject.getName());
 }

 @Test
 public void testJavaTypeDeserialization() throws Exception {
 JavaType javaType = mapper.getTypeFactory().constructType(String.class);
 FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
 String json = mapper.writeValueAsString(javaType);
 JavaType deserializedObject = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
 assertEquals(javaType, deserializedObject);
 }

 @Test
 public void testCurrencyDeserialization() throws Exception {
 Currency currency = Currency.getInstance("USD");
 FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
 String json = mapper.writeValueAsString(currency);
 Currency deserializedObject = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
 assertEquals(currency, deserializedObject);
 }

 @Test
 public void testPatternDeserialization() throws Exception {
 Pattern pattern = Pattern.compile("\\w+");
 FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
 String json = mapper.writeValueAsString(pattern);
 Pattern deserializedObject = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
 assertEquals(pattern, deserializedObject);
 }

 @Test
 public void testLocaleDeserialization() throws Exception {
 Locale locale = new Locale("en", "US");
 FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
 String json = mapper.writeValueAsString(locale);
 Locale deserializedObject = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
 assertEquals(locale, deserializedObject);
 }

 @Test
 public void testCharsetDeserialization() throws Exception {
 Charset charset = Charset.forName("UTF-8");
 FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
 String json = mapper.writeValueAsString(charset);
 Charset deserializedObject = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
 assertEquals(charset, deserializedObject);
 }

 @Test
 public void testTimeZoneDeserialization() throws Exception {
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
 String json = mapper.writeValueAsString(timeZone);
 TimeZone deserializedObject = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
 assertEquals(timeZone, deserializedObject);
 }

 @Test
 public void testInetAddressDeserialization() throws Exception {
 InetAddress inetAddress = InetAddress.getByAddress(new byte[] {127, 0, 0, 1});
 FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
 String json = mapper.writeValueAsString(inetAddress);
 InetAddress deserializedObject = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
 assertEquals(inetAddress, deserializedObject);
 }

 @Test
 public void testInetSocketAddressDeserialization() throws Exception {
 InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 8080);
 FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
 String json = mapper.writeValueAsString(inetSocketAddress);
 InetSocketAddress deserializedObject = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
 assertEquals(inetSocketAddress, deserializedObject);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationFailure() throws Exception {
 String json = "invalid";
 FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
 deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() throws Exception {
 FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
 deserializer.deserialize(mapper.getFactory().createParser("invalid"), mapper.getDeserializationContext());
 }

 @Test(expected = InvalidFormatException.class)
 public void testInvalidFormatException() throws Exception {
 String json = "{\"value\":[1,2,3]}";
 FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
 deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
 }

 @Test(expected = IOException.class)
 public void testIOException() throws Exception {
 FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
 deserializer.deserialize(mapper.getFactory().createParser(""), mapper.getDeserializationContext());
 }

}