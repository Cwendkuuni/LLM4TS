package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XmlSerializerProviderTest {

 private XmlSerializerProvider xmlSerializerProvider;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new XmlMapper();
 xmlSerializerProvider = objectMapper.getSerializerProvider().findValueSerializerProviderInstance(XmlSerializerProvider.class);
 }

 @Test
 public void testSerializeNull() throws JsonMappingException, IOException {
 JsonGenerator gen = objectMapper.getFactory().createGenerator(new TokenBuffer(), null);
 xmlSerializerProvider.serializeValue(gen, null);
 gen.flush();
 ObjectNode actual = objectMapper.readTree(gen.getOutputBuffer());
 assertEquals("null", actual.toString());
 }

 @Test
 public void testSerializeValue() throws JsonMappingException, IOException {
 String value = "Test Value";
 JsonGenerator gen = objectMapper.getFactory().createGenerator(new TokenBuffer(), null);
 xmlSerializerProvider.serializeValue(gen, value);
 gen.flush();
 ObjectNode actual = objectMapper.readTree(gen.getOutputBuffer());
 assertEquals("Test Value", actual.toString());
 }

 @Test
 public void testSerializeValueWithRootType() throws JsonMappingException, IOException {
 JavaType rootType = objectMapper.getTypeFactory().constructType(Object.class);
 JsonGenerator gen = objectMapper.getFactory().createGenerator(new TokenBuffer(), rootType);
 Object value = new Object();
 xmlSerializerProvider.serializeValue(gen, value, rootType, null);
 gen.flush();
 ObjectNode actual = objectMapper.readTree(gen.getOutputBuffer());
 assertNull(actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueWithInvalidClass() throws JsonMappingException, IOException {
 Class invalidClass = new Object(){}.getClass();
 JsonGenerator gen = objectMapper.getFactory().createGenerator(new TokenBuffer(), null);
 xmlSerializerProvider.serializeValue(gen, new Object(), invalidClass, null);
 }

 @Test
 public void testSerializeValueWithArray() throws JsonMappingException, IOException {
 Object[] array = {"Test 1", "Test 2"};
 JsonGenerator gen = objectMapper.getFactory().createGenerator(new TokenBuffer(), Object[].class);
 xmlSerializerProvider.serializeValue(gen, array, null, null);
 gen.flush();
 ObjectNode actual = objectMapper.readTree(gen.getOutputBuffer());
 assertEquals("[\"Test 1\",\"Test 2\"]", actual.toString());
 }
}