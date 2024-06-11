package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlSerializerProviderTest {

 private XmlSerializerProvider serializerProvider;
 private JsonFactory jsonFactory;

 @Before
 public void setup() {
 jsonFactory = new JsonFactory();
 serializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(new QName("rootElement")));
 }

 @Test
 public void testSerializationOfNull() throws JsonProcessingException {
 JsonGenerator jsonGenerator = jsonFactory.createXmlGenerator(new TokenStream(), null);
 serializerProvider.serializeValue(jsonGenerator, null);
 Strings actual = jsonGenerator.getParser().readValueAs(Strings.class);
 assertEquals("null", actual);
 }

 @Test
 public void testSerializationOfSimpleJavaObject() throws JsonProcessingException {
 SimpleJavaObject simpleJavaObject = new SimpleJavaObject();
 simpleJavaObject.setValue("test value");
 JsonGenerator jsonGenerator = jsonFactory.createXmlGenerator(new TokenStream(), null);
 serializerProvider.serializeValue(jsonGenerator, simpleJavaObject);
 Strings actual = jsonGenerator.getParser().readValueAs(Strings.class);
 assertEquals("test value", actual);
 }

 @Test
 public void testSerializationOfArrayOfSimpleJavaObjects() throws JsonProcessingException {
 SimpleJavaObject[] array = new SimpleJavaObject[3];
 for (int i = 0; i < array.length; i++) {
 array[i] = new SimpleJavaObject();
 array[i].setValue("test value " + i);
 }
 JsonGenerator jsonGenerator = jsonFactory.createXmlGenerator(new TokenStream(), null);
 serializerProvider.serializeValue(jsonGenerator, array);
 JsonNode actual = jsonGenerator.getParser().readValueAs(JsonNode.class);
 assertEquals(3, actual.size());
 for (int i = 0; i < array.length; i++) {
 JsonNode expected = Json.valueNode("test value " + i);
 assertEquals(expected.textValue(), actual.get(i).textValue());
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationWithInvalidRootName() throws JsonProcessingException {
 SimpleJavaObject simpleJavaObject = new SimpleJavaObject();
 JsonGenerator jsonGenerator = jsonFactory.createXmlGenerator(new TokenStream(), null);
 serializerProvider.serializeValue(jsonGenerator, simpleJavaObject, new ClassType(SimpleJavaObject.class), null);
 }

 @Test
 public void testSerializationWithExplicitRootName() throws JsonProcessingException {
 SimpleJavaObject simpleJavaObject = new SimpleJavaObject();
 simpleJavaObject.setValue("test value with explicit root name");
 JsonGenerator jsonGenerator = jsonFactory.createXmlGenerator(new TokenStream(), new QName("rootElementWithExplicitName"));
 serializerProvider.serializeValue(jsonGenerator, simpleJavaObject);
 Strings actual = jsonGenerator.getParser().readValueAs(Strings.class);
 assertEquals("rootElementWithExplicitNametest value with explicit root name", actual);
 }

 @Test
 public void testSerializationWithNullRootName() throws JsonProcessingException {
 JsonGenerator jsonGenerator = jsonFactory.createXmlGenerator(new TokenStream(), null);
 serializerProvider.serializeValue(jsonGenerator, new SimpleJavaObject(), null, null);
 Strings actual = jsonGenerator.getParser().readValueAs(Strings.class);
 assertEquals("&lt;null&gt;", actual);
 }

 @Test
 public void testSerializationWithEmptyRootName() throws JsonProcessingException {
 JsonGenerator jsonGenerator = jsonFactory.createXmlGenerator(new TokenStream(), null);
 serializerProvider.serializeValue(jsonGenerator, new SimpleJavaObject(), null, null);
 Strings actual = jsonGenerator.getParser().readValueAs(Strings.class);
 assertEquals("", actual);
 }
}