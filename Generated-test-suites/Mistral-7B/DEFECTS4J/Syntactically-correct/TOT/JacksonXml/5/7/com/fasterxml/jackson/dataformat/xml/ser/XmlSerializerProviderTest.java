package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlSerializerProviderTest {

 private XmlSerializerProvider xmlSerializerProvider;
 private JsonFactory jsonFactory;

 @Before
 public void setup() throws Exception {
 jsonFactory = new JsonFactory();
 xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(new StaxUtil.XmlRootNameLookupMap()));
 }

 @Test
 public void testSerializeNull() throws IOException {
 JsonGenerator gen = jsonFactory.createGenerator(new TokenBuffer(), null);
 xmlSerializerProvider.serializeValue(gen, null);
 String xmlOutput = gen.getOutput().asString();
 assertEquals("<null/>", xmlOutput);
 }

 @Test
 public void testSerializeSimpleClass() throws IOException {
 String input = "{\"name\": \"John\"}";
 ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
 JsonNode jsonNode = objectMapper.readTree(input);
 String rootName = "user";
 JsonSerializer<Object> jsonSerializer = objectMapper.findValueSerializer(String.class, null);
 JsonSerializer<Object> xmlSerializer = xmlSerializerProvider.findValueSerializer(String.class, null);

 JsonGenerator xmlGen = jsonFactory.createGenerator(new TokenBuffer(), null);
 xmlSerializerProvider.serializeValue(xmlGen, jsonNode, new PropertyName(rootName), jsonSerializer);
 String xmlOutput = xmlGen.getOutput().asString();
 assertEquals("<user>" + System.lineSeparator() + "<name>John</name>" + System.lineSeparator() + "</user>", xmlOutput);
 }

 @Test
 public void testSerializeArrayOfSimpleClass() throws IOException {
 String input = "[{\"name\": \"John\"}, {\"name\": \"Jane\"}]";
 ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
 JsonNode jsonNode = objectMapper.readTree(input);
 JsonSerializer<Object> jsonSerializer = objectMapper.findValueSerializer(String[].class, null);
 JsonSerializer<Object> xmlSerializer = xmlSerializerProvider.findValueSerializer(String[].class, null);

 JsonGenerator xmlGen = jsonFactory.createGenerator(new TokenBuffer(), null);
 xmlSerializerProvider.serializeValue(xmlGen, jsonNode, jsonSerializer);
 String xmlOutput = xmlGen.getOutput().asString();
 assertEquals("<array name=\"item\">" + System.lineSeparator() +
         "<user>" + System.lineSeparator() +
         "<name>John</name>" + System.lineSeparator() +
         "</user>" + System.lineSeparator() +
         "<user>" + System.lineSeparator() +
         "<name>Jane</name>" + System.lineSeparator() +
         "</user>" + System.lineSeparator() +
         "</array>", xmlOutput);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeUnknownClass() throws IOException {
 String input = "{\"value\": 123}";
 ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
 JsonNode jsonNode = objectMapper.readTree(input);
 JsonSerializer<Object> jsonSerializer = objectMapper.findValueSerializer(UnknownClass.class, null);
 JsonSerializer<Object> xmlSerializer = xmlSerializerProvider.findValueSerializer(UnknownClass.class, null);
 JsonGenerator xmlGen = jsonFactory.createGenerator(new TokenBuffer(), null);
 xmlSerializerProvider.serializeValue(xmlGen, jsonNode, jsonSerializer);
 }

 @Test
 public void testSerializeWithConfiguredRootName() throws IOException {
 String input = "{\"value\": \"Hello\"}";
 ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
 ObjectWriter jsonWriter = objectMapper.writerWithRootValueSeparator("_");
 JsonNode jsonNode = jsonWriter.writeValueAsTree(new ValueHolder("Hello"));
 String rootName = "value_holder";
 JsonSerializer<Object> jsonSerializer = objectMapper.findValueSerializer(ValueHolder.class, null);
 JsonSerializer<Object> xmlSerializer = xmlSerializerProvider.findValueSerializer(ValueHolder.class, null);

 JsonGenerator xmlGen = jsonFactory.createGenerator(new TokenBuffer(), null);
 xmlSerializerProvider.serializeValue(xmlGen, jsonNode, jsonSerializer);
 String xmlOutput = xmlGen.getOutput().asString();
 assertEquals("<value_holder>" + System.lineSeparator() + "<value>Hello</value>" + System.lineSeparator() + "</value_holder>", xmlOutput);
 }
}

class ValueHolder {
 private final String value;

 public ValueHolder(String value) {
 this.value = value;
 }

 public String getValue() {
 return value;
 }
}