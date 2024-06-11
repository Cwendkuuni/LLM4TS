package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.XmlRootElement;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class XmlSerializerProviderTest {

 private XmlMapper xmlMapper;
 private ObjectMapper jsonMapper;
 private XmlSerializerProvider xmlSerializerProvider;

 @Before
 public void setup() {
 xmlMapper = new XmlMapper();
 xmlMapper.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 jsonMapper = new ObjectMapper();
 xmlSerializerProvider = xmlMapper.getSerializerProvider();
 }

 @Test
 public void testSerializeNull() throws JsonProcessingException {
 ObjectNode jsonNullResult = jsonMapper.writeValueAsTree(null);
 String jsonNull = jsonNullResult.toString();

 TokenBuffer tokenBuffer = new TokenBuffer();
 ToXmlGenerator xmlGenerator = StaxUtil.createXmlGenerator(tokenBuffer);
 xmlSerializerProvider.serializeValue(xmlGenerator, null);
 String xmlNull = tokenBuffer.asXml();

 assertEquals("[null]", jsonNull);
 assertEquals("<null></null>", xmlNull);
 }

 @Test
 public void testSerializeBoolean() throws JsonProcessingException, IOException {
 ObjectNode jsonBooleanResult = jsonMapper.createObjectNode();
 jsonBooleanResult.put("value", true);

 TokenBuffer tokenBuffer = new TokenBuffer();
 ToXmlGenerator xmlGenerator = StaxUtil.createXmlGenerator(tokenBuffer);
 xmlSerializerProvider.serializeValue(xmlGenerator, true);
 String xmlBoolean = tokenBuffer.asXml();

 assertEquals(jsonBooleanResult.toString(), xmlBoolean);
 }

 @Test
 public void testSerializeNumber() throws JsonProcessingException, IOException {
 ObjectNode jsonNumberResult = jsonMapper.createObjectNode();
 jsonNumberResult.put("value", 123);

 TokenBuffer tokenBuffer = new TokenBuffer();
 ToXmlGenerator xmlGenerator = StaxUtil.createXmlGenerator(tokenBuffer);
 xmlSerializerProvider.serializeValue(xmlGenerator, 123);
 String xmlNumber = tokenBuffer.asXml();

 assertEquals(jsonNumberResult.toString(), xmlNumber);
 }

 @Test
 public void testSerializeString() throws JsonProcessingException, IOException {
 ObjectNode jsonStringResult = jsonMapper.createObjectNode();
 jsonStringResult.put("value", "test");

 TokenBuffer tokenBuffer = new TokenBuffer();
 ToXmlGenerator xmlGenerator = StaxUtil.createXmlGenerator(tokenBuffer);
 xmlSerializerProvider.serializeValue(xmlGenerator, "test");
 String xmlString = tokenBuffer.asXml();

 assertEquals(jsonStringResult.toString(), xmlString);
 }

 @Test
 public void testSerializeCustomClass() throws JsonProcessingException, IOException {
 ObjectNode jsonCustomClassResult = jsonMapper.createObjectNode();
 jsonCustomClassResult.put("name", "TestClass");

 StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class) {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
 throws IOException {
 gen.writeString("TestClass");
 }
 };
 xmlMapper.registerSerializer(Object.class, serializer);

 TokenBuffer tokenBuffer = new TokenBuffer();
 ToXmlGenerator xmlGenerator = StaxUtil.createXmlGenerator(tokenBuffer);
 xmlSerializerProvider.serializeValue(xmlGenerator, new Object());
 String xmlCustomClass = tokenBuffer.asXml();

 assertEquals(jsonCustomClassResult.toString(), xmlCustomClass);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNullRootName() throws JsonProcessingException, IOException {
 xmlMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 ObjectMapper jsonMapperWithEmptyRootName = new ObjectMapper();
 jsonMapperWithEmptyRootName.setSerializationInclusion(JsonMapping.SerializedAttribute.EMPTY_BEANS);
 SerializerProvider jsonSerializerProvider = jsonMapperWithEmptyRootName.getSerializerProvider();
 TokenBuffer tokenBuffer = new TokenBuffer();
 ToXmlGenerator xmlGenerator = StaxUtil.createXmlGenerator(tokenBuffer);
 xmlGenerator.setRootElementName(null);
 xmlSerializerProvider.serializeValue(xmlGenerator, new Object());
 xmlSerializerProvider.serializeValue(xmlGenerator, null);
 xmlGenerator.close();
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeArray() throws JsonProcessingException, IOException {
 ObjectNode jsonArrayResult = jsonMapper.createObjectNode();
 ObjectNode jsonArrayElementResult = jsonMapper.createObjectNode();
 jsonArrayResult.put("items", jsonArrayElementResult);
 jsonArrayElementResult.put("value", "test");

 TokenBuffer tokenBuffer = new TokenBuffer();
 ToXmlGenerator xmlGenerator = StaxUtil.createXmlGenerator(tokenBuffer);
 xmlSerializerProvider.serializeValue(xmlGenerator, Arrays.asList("test", "test2"));
 xmlGenerator.close();
 }
}