package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonSerializerProvider; // for testing
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class XmlSerializerProviderTest {

 private ObjectMapper xmlMapper;
 private SerializerProvider xmlSerializerProvider;

 @Before
 public void setup() {
 xmlMapper = new XmlMapper();
 xmlSerializerProvider = xmlMapper.getSerializerProvider();
 }

 @Test
 public void testSerializeNull() throws JsonProcessingException {
 // Given
 final PropertyName rootName = new PropertyName("rootName");
 final SerializationConfig config = new SerializationConfig();
 config.setRootValueSeparator(":");
 config.setRootName(rootName);
 final XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(config, xmlMapper.getSerializerFactory());

 // When
 // We assume that when serializeValue is called with null value, _serializeXmlNull method is called
 final TokenBuffer tokenBuffer = new TokenBuffer(null);
 xmlSerializerProvider.serializeValue(tokenBuffer, null);

 // Then
 final Object root = tokenBuffer.getRootValue();
 Assert.assertEquals("null", root.getClass().getName());
 Assert.assertEquals("<null/>", root.toString());
 }

 @Test
 public void testSerializeObject() throws JsonProcessingException {
 // Given
 final String expectedXml = "<root><item>Hello World</item></root>";
 final ObjectMapper xmlMapper = new XmlMapper();
 final SerializationConfig config = new SerializationConfig();
 final JsonSerializer<String> serializer = new StdSerializer<String>(String.class) {
 @Override
 public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeString(value);
 }
 };
 config.setSerializerFactory(xmlMapper.getSerializerProvider().getFactory());
 config.setRootValueSeparator(":");
 config.setRootName(new PropertyName("root"));
 final XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(config, xmlMapper.getSerializerProvider());
 final String testString = "Hello World";

 // When
 final TokenBuffer tokenBuffer = new TokenBuffer(null);
 xmlSerializerProvider.findValueSerializer(String.class, null, null, null, null).serialize(testString, tokenBuffer, xmlSerializerProvider);

 // Then
 final Object root = tokenBuffer.getRootValue();
 Assert.assertEquals("java.lang.String", root.getClass().getName());
 Assert.assertEquals(expectedXml, root.toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeObjectWithWrongRootName() throws JsonProcessingException {
 // Given
 final ObjectMapper xmlMapper = new XmlMapper();
 final SerializationConfig config = new SerializationConfig();
 final JsonSerializer<String> serializer = new StdSerializer<String>(String.class) {
 @Override
 public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeString(value);
 }
 };
 config.setRootValueSeparator(":");
 config.setRootName(new PropertyName("wrongRootName"));
 final XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(config, xmlMapper.getSerializerProvider());
 final String testString = "Hello World";

 // When
 final TokenBuffer tokenBuffer = new TokenBuffer(null);
 xmlSerializerProvider.serializeValue(tokenBuffer, testString);

 // Then
 // JsonMappingException should be thrown because of wrong root name
 }

 @Test
 public void testSerializeArray() throws JsonProcessingException {
 // Given
 final String[] testArray = {"Item1", "Item2"};
 final ObjectMapper xmlMapper = new XmlMapper();final SerializationConfig config = new SerializationConfig();
 config.setRootValueSeparator(":");
 config.setRootName(new PropertyName("root"));
 final XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(config, xmlMapper.getSerializerProvider());
 final JavaType javaType = xmlMapper.getTypeFactory().constructArrayType(String.class);

 // When
 final TokenBuffer tokenBuffer = new TokenBuffer(null);
 xmlSerializerProvider.serializeValue(tokenBuffer, testArray, javaType);

 // Then
 final Object root = tokenBuffer.getRootValue();
 final String expectedXml = "<root><item>Item1</item><item>Item2</item></root>";
 Assert.assertEquals("java.lang.Object[]", root.getClass().getName());
 Assert.assertEquals(expectedXml, root.toString());
 }

 @Test
 public void testSerializeArrayWithWrongRootName() throws JsonProcessingException {
 // Given
 final ObjectMapper xmlMapper = new XmlMapper();
 final SerializationConfig config = new SerializationConfig();
 config.setRootValueSeparator(":");
 config.setRootName(new PropertyName("wrongRootName"));
 final XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(config, xmlMapper.getSerializerProvider());
 final String[] testArray = {"Item1", "Item2"};
 final JavaType javaType = xmlMapper.getTypeFactory().constructArrayType(String.class);

 // When
 final TokenBuffer tokenBuffer = new TokenBuffer(null);
 xmlSerializerProvider.serializeValue(tokenBuffer, testArray, javaType);

 // Then
 // JsonMappingException should be thrown because of wrong root name
 }

 @Test
 public void testSerializeObjectWithException() throws JsonProcessingException, JsonMappingException {
 // Given
 final ObjectMapper xmlMapper = new XmlMapper();
 final SerializationConfig config = new SerializationConfig();
 final JsonSerializer<String> serializer = new StdSerializer<String>(String.class) {
 @Override
 public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 throw new RuntimeException("Test exception");
 }
 };
 config.setRootValueSeparator(":");
 config.setRootName(new PropertyName("root"));
 final XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(config, xmlMapper.getSerializerProvider());
 final String testString = "Hello World";

 // When
 // JsonMappingException should be thrown because of runtime exception in serializer
 final TokenBuffer tokenBuffer = new TokenBuffer(null);
 xmlSerializerProvider.findValueSerializer(String.class, null, null, null, null).serialize(testString, tokenBuffer, xmlSerializerProvider);

 // Then
 }
}