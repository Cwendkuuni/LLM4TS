package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

 @Test
 public void testDeserializeFile() throws Exception {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
 @Override
 protected File _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return new File(value);
 }
 };
 ObjectMapper mapper = new ObjectMapper();
 JsonNode node = mapper.readTree("{\"className\":\"File\",\"value\":\"/test.txt\"}");
 File result = deserializer.deserialize(mapper.treeToValues(node, File.class)[0], mapper.getDeserializationContext());
 assertEquals("/test.txt", result.getAbsolutePath());
 }

 @Test
 public void testDeserializeURL() throws Exception {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
 @Override
 protected URL _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return new URL(value);
 }
 };
 ObjectMapper mapper = new ObjectMapper();
 JsonNode node = mapper.readTree("{\"className\":\"URL\",\"value\":\"http://test.com\"}");
 URL result = deserializer.deserialize(mapper.treeToValues(node, URL.class)[0], mapper.getDeserializationContext());
 assertEquals("http://test.com", result.toString());
 }

 @Test
 public void testDeserializeURI() throws Exception {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
 @Override
 protected URI _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return URI.create(value);
 }
 };
 ObjectMapper mapper = new ObjectMapper();
 JsonNode node = mapper.readTree("{\"className\":\"URI\",\"value\":\"http://test.com/path\"}");
 URI result = deserializer.deserialize(mapper.treeToValues(node, URI.class)[0], mapper.getDeserializationContext());
 assertEquals("http://test.com/path", result.toString());
 }

 @Test
 public void testDeserializeClass() throws Exception {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
 @Override
 protected Class<?> _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return Class.forName(value);
 }
 };
 ObjectMapper mapper = new ObjectMapper();
 JsonNode node = mapper.readTree("{\"className\":\"String\",\"value\":\"java.lang.String\"}");
 Class<?> result = deserializer.deserialize(mapper.treeToValues(node, Class.class)[0], mapper.getDeserializationContext());
 assertEquals(String.class, result);
 }

 @Test
 public void testDeserializeJavaType() throws Exception {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
 @Override
 protected JavaType _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return mapper.getTypeFactory().constructType(value);
 }
 };
 ObjectMapper mapper = new ObjectMapper();
 JsonNode node = mapper.readTree("{\"className\":\"java.util.List\",\"value\":\"java.lang.String\"}");
 JavaType result = deserializer.deserialize(mapper.treeToValues(node, JavaType.class)[0], mapper.getDeserializationContext());
 assertEquals(Map.class, result.getRawClass());
 assertTrue(result.isCollectionType());
 assertTrue(result.getTypeBinding().isEmpty());
 }

 @Test
 public void testDeserializeCurrency() throws Exception {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
 @Override
 protected Currency _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return Currency.getInstance(value);
 }
 };
 ObjectMapper mapper = new ObjectMapper();
 JsonNode node = mapper.readTree("{\"className\":\"Currency\",\"value\":\"USD\"}");
 Currency result = deserializer.deserialize(mapper.treeToValues(node, Currency.class)[0], mapper.getDeserializationContext());
 assertEquals(Currency.getInstance("USD"), result);
 }

 @Test
 public void testDeserializePattern() throws Exception {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
 @Override
 protected Pattern _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return Pattern.compile(value);
 }
 };
 ObjectMapper mapper = new ObjectMapper();
 JsonNode node = mapper.readTree("{\"className\":\"Pattern\",\"value\":\"\\w+\\.\\w+\"}");
 Pattern result = deserializer.deserialize(mapper.treeToValues(node, Pattern.class)[0], mapper.getDeserializationContext());
 assertEquals(Pattern.compile("\\w+\\.\\w+"), result);
 }

 @Test
 public void testDeserializeLocale() throws Exception {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
 @Override
 protected Locale _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return new Locale(value);
 }
 };
 ObjectMapper mapper = new ObjectMapper();
 JsonNode node = mapper.readTree("{\"className\":\"Locale\",\"value\":\"en_US\"}");
 Locale result = deserializer.deserialize(mapper.treeToValues(node, Locale.class)[0], mapper.getDeserializationContext());
 assertEquals(Locale.US, result);

 node = mapper.readTree("{\"className\":\"Locale\",\"value\":\"fr_FR\"}");
 result = deserializer.deserialize(mapper.treeToValues(node, Locale.class)[0], mapper.getDeserializationContext());
 assertEquals(Locale.FRANCE, result);
 }

 @Test
 public void testDeserializeCharset() throws Exception {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
 @Override
 protected Charset _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return Charset.forName(value);
 }
 };
 ObjectMapper mapper = new ObjectMapper();
 JsonNode node = mapper.readTree("{\"className\":\"Charset\",\"value\":\"UTF-8\"}");
 Charset result = deserializer.deserialize(mapper.treeToValues(node, Charset.class)[0], mapper.getDeserializationContext());
 assertEquals(Charset.forName("UTF-8"), result);
 }

 @Test
 public void testDeserializeTimeZone() throws Exception {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
 @Override
 protected TimeZone _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return TimeZone.getTimeZone(value);
 }
 };
 ObjectMapper mapper = new ObjectMapper();
 JsonNode node = mapper.readTree("{\"className\":\"TimeZone\",\"value\":\"America/New_York\"}");
 TimeZone result = deserializer.deserialize(mapper.treeToValues(node, TimeZone.class)[0], mapper.getDeserializationContext());
 assertEquals(TimeZone.getTimeZone("America/New_York"), result);
 }

 @Test
 public void testDeserializeInetAddress() throws Exception {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
 @Override
 protected InetAddress _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return InetAddress.getByName(value);
 }
 };
 ObjectMapper mapper = new ObjectMapper();
 JsonNode node = mapper.readTree("{\"className\":\"InetAddress\",\"value\":\"127.0.0.1\"}");
 InetAddress result = deserializer.deserialize(mapper.treeToValues(node, InetAddress.class)[0], mapper.getDeserializationContext());
 assertEquals(InetAddress.getByName("127.0.0.1"), result);

 node = mapper.readTree("{\"className\":\"InetSocketAddress\",\"value\":\"[::1]:8080\"}");
 InetSocketAddress isa = deserializer.deserialize(mapper.treeToValues(node, InetSocketAddress.class)[0], mapper.getDeserializationContext());
 InetAddress ipv6 = isa.getAddress();
 assertEquals(InetAddress.getByAddress(new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}), ipv6);
 assertEquals(8080, isa.getPort());
 }
}