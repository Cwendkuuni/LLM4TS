package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testDeserializeFile() throws IOException {
 FromStringDeserializer<File> fileDeserializer = (FromStringDeserializer<File>) FromStringDeserializer.findDeserializer(File.class);
 String json = "\"path/to/file.txt\"";
 File deserializedObject = fileDeserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
 assertEquals("path/to/file.txt", deserializedObject.getAbsolutePath());
 }

 @Test
 public void testDeserializeURL() throws IOException {
 FromStringDeserializer<URL> urlDeserializer = (FromStringDeserializer<URL>) FromStringDeserializer.findDeserializer(URL.class);
 String json = "\"http://example.com\"";
 URL deserializedObject = urlDeserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
 assertEquals("http://example.com", deserializedObject.toString());
 }

 @Test
 public void testDeserializeURI() throws IOException {
 FromStringDeserializer<URI> uriDeserializer = (FromStringDeserializer<URI>) FromStringDeserializer.findDeserializer(URI.class);
 String json = "\"http://example.com/path?query=value#fragment\"";
 URI deserializedObject = uriDeserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
 assertEquals("http", deserializedObject.getScheme());
 assertEquals("/path", deserializedObject.getPath());
 assertEquals("query=value", deserializedObject.getQuery());
 assertEquals("fragment", deserializedObject.getFragment());
 }

 @Test
 public void testDeserializeClass() throws IOException {
 FromStringDeserializer<String> stringDeserializer = (FromStringDeserializer<String>) FromStringDeserializer.findDeserializer(String.class);
 String json = "\"java.lang.String\"";
 String deserializedObject = (String) stringDeserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
 assertEquals("java.lang.String", deserializedObject.getClass().getName());
 }

 @Test
 public void testDeserializeJavaType() throws IOException {
 FromStringDeserializer<JavaType> javaTypeDeserializer = (FromStringDeserializer<JavaType>) FromStringDeserializer.findDeserializer(JavaType.class);
 String json = "\"com.example.MyClass\"";
 JavaType deserializedObject = (JavaType) javaTypeDeserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
 assertEquals("com.example.MyClass", deserializedObject.getTypeName());
 }

 @Test
 public void testDeserializeCurrency() throws IOException {
 FromStringDeserializer<Currency> currencyDeserializer = (FromStringDeserializer<Currency>) FromStringDeserializer.findDeserializer(Currency.class);
 String json = "\"USD\"";
 Currency deserializedObject = currencyDeserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
 assertEquals(Currency.getInstance("USD"), deserializedObject);
 }

 @Test
 public void testDeserializePattern() throws IOException {
 FromStringDeserializer<Pattern> patternDeserializer = (FromStringDeserializer<Pattern>) FromStringDeserializer.findDeserializer(Pattern.class);
 String json = "/[a-z]+/i";
 Pattern deserializedObject = patternDeserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
 assertNotNull(deserializedObject);
 }

 @Test
 public void testDeserializeLocale() throws IOException {
 FromStringDeserializer<Locale> localeDeserializer = (FromStringDeserializer<Locale>) FromStringDeserializer.findDeserializer(Locale.class);
 String json = "\"en_US\"";
 Locale deserializedObject = localeDeserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
 assertEquals("en_US", deserializedObject. toString());
 }

 @Test
 public void testDeserializeCharset() throws IOException {
 FromStringDeserializer<Charset> charsetDeserializer = (FromStringDeserializer<Charset>) FromStringDeserializer.findDeserializer(Charset.class);
 String json = "\"UTF-8\"";
 Charset deserializedObject = charsetDeserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
 assertEquals(Charset.forName("UTF-8"), deserializedObject);
 }

 @Test
 public void testDeserializeTimeZone() throws IOException {
 FromStringDeserializer<TimeZone> timeZoneDeserializer = (FromStringDeserializer<TimeZone>) FromStringDeserializer.findDeserializer(TimeZone.class);
 String json = "\"America/New_York\"";
 TimeZone deserializedObject = timeZoneDeserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
 assertEquals("America/New_York", deserializedObject.getID());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidType() throws IOException {
 FromStringDeserializer<Integer> integerDeserializer = (FromStringDeserializer<Integer>) FromStringDeserializer.findDeserializer(Integer.class);
 String json = "\"java.util.Date\"";
 integerDeserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
 // The test should fail with JsonMappingException
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmptyString() throws IOException {
 FromStringDeserializer<String> stringDeserializer = (FromStringDeserializer<String>) FromStringDeserializer.findDeserializer(String.class);
 String json = "\"\"";
 stringDeserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
 // The test should fail with JsonMappingException
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJson() throws IOException {
 FromStringDeserializer<String> stringDeserializer = (FromStringDeserializer<String>) FromStringDeserializer.findDeserializer(String.class);
 String json = "\"not valid json\"";
 stringDeserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
 // The test should fail with JsonMappingException
 }
}