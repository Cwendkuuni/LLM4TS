package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for DefaultPrettyPrinter
 */
public class DefaultPrettyPrinterTest {

 private DefaultPrettyPrinter prettyPrinter;

 @Before
 public void setUp() {
 prettyPrinter = new DefaultPrettyPrinter();
 }

 /**
 * Test for writeRootValueSeparator method
 */
 @Test
 public void testWriteRootValueSeparator() throws IOException {
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
 prettyPrinter.writeRootValueSeparator(jsonGenerator);
 String result = jsonGenerator.getOutputContent();
 assertEquals("", result); // no root separator when null

 JsonFactory jsonFactory = new JsonFactory();
 JsonGenerator jsonGeneratorWithRootSeparator = jsonFactory.createGenerator(new StringWriter());
 prettyPrinter = new DefaultPrettyPrinter(" Root separator ");
 prettyPrinter.writeRootValueSeparator(jsonGeneratorWithRootSeparator);
 result = jsonGeneratorWithRootSeparator.getOutputContent();
 assertEquals(" Root separator ", result);
 }

 /**
 * Test for writeStartObject method
 */
 @Test
 public void testWriteStartObject() throws IOException {
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
 prettyPrinter.writeStartObject(jsonGenerator);
 String result = jsonGenerator.getOutputContent();
 assertEquals("{\n", result);
 }

 /**
 * Test for beforeObjectEntries method
 */
 @Test
 public void testBeforeObjectEntries() throws IOException {
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
 ObjectNode objectNode = new ObjectMapper().createObjectNode();
 objectNode.putPOJO("name", new Person("John Doe"));
 prettyPrinter = new DefaultPrettyPrinter();
 prettyPrinter.writeObjectName("Person");
 prettyPrinter.beforeObjectEntries(jsonGenerator);
 result = jsonGenerator.getOutputContent();
 assertEquals("{\n" +
                 "\"Person\":\n", result);
 }

 /**
 * Test for writeObjectFieldValueSeparator method
 */
 @Test
 public void testWriteObjectFieldValueSeparator() throws IOException {
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
 ObjectNode objectNode = new ObjectMapper().createObjectNode();
 objectNode.putPOJO("name", new Person("John Doe"));
 ObjectNode nameNode = objectNode.get("name");
 nameNode.putPOJO("first", new Person("John"));
 nameNode.putPOJO("last", new Person("Doe"));
 prettyPrinter = new DefaultPrettyPrinter();
 prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
 result = jsonGenerator.getOutputContent();
 assertEquals("{\n" +
                 "\"Person\":{\n" +
                 "\"name\":{\n", result);
 }

 /**
 * Test for writeObjectEntrySeparator method
 */
 @Test
 public void testWriteObjectEntrySeparator() throws IOException {
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
 ObjectNode objectNode = new ObjectMapper().createObjectNode();
 objectNode.putPOJO("name", new Person("John Doe"));
 ObjectNode nameNode = objectNode.get("name");
 nameNode.putPOJO("first", new Person("John"));
 nameNode.putPOJO("last", new Person("Doe"));
 prettyPrinter = new DefaultPrettyPrinter();
 prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
 result = jsonGenerator.getOutputContent();
 assertEquals("{\n" +
                 "\"Person\":{\n" +
                 "\"name\":{\n" +
                 "}", result);
 }

 /**
 * Test for writeEndObject method
 */
 @Test
 public void testWriteEndObject() throws IOException {
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
 ObjectNode objectNode = new ObjectMapper().createObjectNode();
 objectNode.putPOJO("name", new Person("John Doe"));
 ObjectNode nameNode = objectNode.get("name");
 nameNode.putPOJO("first", new Person("John"));
 nameNode.putPOJO("last", new Person("Doe"));
 prettyPrinter.writeEndObject(jsonGenerator, 1);
 result = jsonGenerator.getOutputContent();
 assertEquals("{\n" +
                 "\"Person\":{\n" +
                 "\"name\":{\n" +
                 "\"first\":\"John\",\n" +
                 "\"last\":\"Doe\"\n" +
                 "}\n" +
                 "}", result);
 }

 /**
 * Test for writeStartArray method
 */
 @Test
 public void testWriteStartArray() throws IOException {
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
 prettyPrinter.writeStartArray(jsonGenerator);
 String result = jsonGenerator.getOutputContent();
 assertEquals("[\n", result);
 }

 /**
 * Test for beforeArrayValues method
 */
 @Test
 public void testBeforeArrayValues() throws IOException {
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
 ArrayNode arrayNode = new ObjectMapper().createArrayNode();
 arrayNode.add(new Integer(1));
 arrayNode.add(new Integer(2));
 prettyPrinter = new DefaultPrettyPrinter();
 prettyPrinter.beforeArrayValues(jsonGenerator);
 result = jsonGenerator.getOutputContent();
 assertEquals("[\n", result);
 }

 /**
 * Test for writeArrayValueSeparator method
 */
 @Test
 public void testWriteArrayValueSeparator() throws IOException {
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
 ArrayNode arrayNode = new ObjectMapper().createArrayNode();
 arrayNode.add(new Integer(1));
 arrayNode.add(new Integer(2));
 prettyPrinter = new DefaultPrettyPrinter();
 prettyPrinter.writeArrayValueSeparator(jsonGenerator);
 result = jsonGenerator.getOutputContent();
 assertEquals("[\n" +
                 "1,\n", result);
 }

 /**
 * Test for writeEndArray method
 */
 @Test
 public void testWriteEndArray() throws IOException {
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
 ArrayNode arrayNode = new ObjectMapper().createArrayNode();
 arrayNode.add(new Integer(1));
 arrayNode.add(new Integer(2));
 prettyPrinter.writeEndArray(jsonGenerator, 2);
 result = jsonGenerator.getOutputContent();
 assertEquals("[\n" +
                 "1,\n" +
                 "2\n" +
                 "]", result);
 }
}