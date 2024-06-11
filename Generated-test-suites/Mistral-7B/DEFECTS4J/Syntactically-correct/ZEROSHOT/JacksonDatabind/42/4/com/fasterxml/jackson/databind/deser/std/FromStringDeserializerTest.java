package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

 @Test
 public void testDeserializeFile() throws JsonProcessingException {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
 @Override
 protected File _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return new File(value);
 }
 };
 DeserializationContext context = new DeserializationContext(new StandardDeserializationContext());
 assertEquals(new File("/test/path"), deserializer.deserialize(context.getParser(), context));
 }

 @Test
 public void testDeserializeURL() throws JsonProcessingException {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
 @Override
 protected URL _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return new URL(value);
 }
 };
 DeserializationContext context = new DeserializationContext(new StandardDeserializationContext());
 assertEquals(new URL("http://www.test.com"), deserializer.deserialize(context.getParser(), context));
 }

 @Test
 public void testDeserializeURI() throws JsonProcessingException {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
 @Override
 protected URI _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return URI.create(value);
 }
 };
 DeserializationContext context = new DeserializationContext(new StandardDeserializationContext());
 assertEquals(URI.create("http://www.test.com"), deserializer.deserialize(context.getParser(), context));
 }

 @Test
 public void testDeserializeClass() throws JsonProcessingException {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
 @Override
 protected Class<?> _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return Class.forName(value);
 }
 };
 DeserializationContext context = new DeserializationContext(new StandardDeserializationContext());
 assertEquals(String.class, deserializer.deserialize(context.getParser(), context));
 }

 @Test
 public void testDeserializeJavaType() throws JsonProcessingException {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
 @Override
 protected JavaType _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return context.getTypeFactory().constructFromCanonical(value);
 }
 };
 DeserializationContext context = new DeserializationContext(new StandardDeserializationContext());
 JavaType javaType = context.getTypeFactory().constructCollectionType(ArrayList.class, Integer.class);
 assertEquals(javaType, deserializer.deserialize(context.getParser(), context));
 }

 @Test
 public void testDeserializeCurrency() throws JsonProcessingException {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
 @Override
 protected Currency _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return Currency.getInstance(value);
 }
 };
 DeserializationContext context = new DeserializationContext(new StandardDeserializationContext());
 assertEquals(Currency.getInstance("USD"), deserializer.deserialize(context.getParser(), context));
 }

 @Test
 public void testDeserializePattern() throws JsonProcessingException {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
 @Override
 protected Pattern _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return Pattern.compile(value);
 }
 };
 DeserializationContext context = new DeserializationContext(new StandardDeserializationContext());
 assertEquals(Pattern.compile("[a-zA-Z]+"), deserializer.deserialize(context.getParser(), context));
 }

 @Test
 public void testDeserializeLocale() throws JsonProcessingException {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
 @Override
 protected Locale _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return new Locale(value);
 }
 };
 DeserializationContext context = new DeserializationContext(new StandardDeserializationContext());
 assertEquals(Locale.US, deserializer.deserialize(context.getParser(), context));
 }

 @Test
 public void testDeserializeCharset() throws JsonProcessingException {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
 @Override
 protected Charset _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return Charset.forName(value);
 }
 };
 DeserializationContext context = new DeserializationContext(new StandardDeserializationContext());
 assertEquals(Charset.forName("UTF-8"), deserializer.deserialize(context.getParser(), context));
 }

 @Test
 public void testDeserializeTimeZone() throws JsonProcessingException {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
 @Override
 protected TimeZone _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return TimeZone.getTimeZone(value);
 }
 };
 DeserializationContext context = new DeserializationContext(new StandardDeserializationContext());
 assertEquals(TimeZone.getTimeZone("UTC"), deserializer.deserialize(context.getParser(), context));
 }

 @Test
 public void testDeserializeInetAddress() throws JsonProcessingException {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
 @Override
 protected InetAddress _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return InetAddress.getByName(value);
 }
 };
 DeserializationContext context = new DeserializationContext(new StandardDeserializationContext());
 assertEquals(InetAddress.getByName("127.0.0.1"), deserializer.deserialize(context.getParser(), context));
 }

 @Test
 public void testDeserializeInetSocketAddress() throws JsonProcessingException {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
 @Override
 protected InetSocketAddress _deserialize(String value, DeserializationContext ctxt) throws IOException {
 String[] parts = value.split(":");
 if (parts.length != 2) {
 throw new InvalidFormatException("Invalid InetSocketAddress format: " + value);
 }
 InetAddress address = InetAddress.getByName(parts[0]);
 int port = Integer.parseInt(parts[1]);
 return new InetSocketAddress(address, port);
 }
 };
 DeserializationContext context = new DeserializationContext(new StandardDeserializationContext());
 assertEquals(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 80), deserializer.deserialize(context.getParser(), context));
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidType() throws JsonProcessingException {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
 @Override
 protected Object _deserialize(String value, DeserializationContext ctxt) throws IOException {
 throw new RuntimeException("Should not be reached");
 }
 };
 DeserializationContext context = new DeserializationContext(new StandardDeserializationContext());
 deserializer.deserialize(context.getParser(), context);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmptyString() throws JsonProcessingException {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
 @Override
 protected Object _deserializeFromEmptyString() throws IOException {
 throw new RuntimeException("Should not be reached");
 }
 };
 DeserializationContext context = new DeserializationContext(new StandardDeserializationContext());
 deserializer.deserialize(context.getParser(), context);
 }
}