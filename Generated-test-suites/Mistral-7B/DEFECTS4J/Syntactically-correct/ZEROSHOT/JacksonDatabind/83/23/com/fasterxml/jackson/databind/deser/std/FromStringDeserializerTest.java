package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testDeserializeFile() throws Exception {
 FromStringDeserializer<File> fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
 File actual = fileDeserializer.deserialize(objectMapper.getFactory().createParser("\"/path/to/file.txt\""), objectMapper.getDeserializationContext());
 File expected = new File("/path/to/file.txt");
 assertEquals(expected.getAbsolutePath(), actual.getAbsolutePath());
 }

 @Test
 public void testDeserializeURL() throws Exception {
 FromStringDeserializer<URL> urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
 URL actual = urlDeserializer.deserialize(objectMapper.getFactory().createParser("\"http://www.example.com\""), objectMapper.getDeserializationContext());
 URL expected = new URL("http://www.example.com");
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testDeserializeURI() throws Exception {
 FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
 URI actual = uriDeserializer.deserialize(objectMapper.getFactory().createParser("\"http://www.example.com/path/to/resource\""), objectMapper.getDeserializationContext());
 URI expected = new URI("http://www.example.com/path/to/resource");
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeClass() throws Exception {
 FromStringDeserializer<StringBuilder> stringBuilderDeserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
 StringBuilder actual = (StringBuilder) stringBuilderDeserializer.deserialize(objectMapper.getFactory().createParser("\"new StringBuilder()\""), objectMapper.getDeserializationContext());
 StringBuilder expected = new StringBuilder();
 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidClass() throws Exception {
 FromStringDeserializer<Integer> integerDeserializer = FromStringDeserializer.findDeserializer(Integer.class);
 integerDeserializer.deserialize(objectMapper.getFactory().createParser("\"java.lang.String\""), objectMapper.getDeserializationContext());
 }

 @Test
 public void testDeserializeEmptyString() throws Exception {
 FromStringDeserializer<Locale> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
 Locale actual = localeDeserializer.deserialize(objectMapper.getFactory().createParser("\"\""), objectMapper.getDeserializationContext());
 Locale expected = Locale.ROOT;
 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidString() throws Exception {
 FromStringDeserializer<TimeZone> timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
 timeZoneDeserializer.deserialize(objectMapper.getFactory().createParser("\"InvalidZone\""), objectMapper.getDeserializationContext());
 }
}