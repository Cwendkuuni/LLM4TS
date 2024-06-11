package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class JsonGeneratorImplTest {

 private JsonFactory factory;
 private ObjectMapper mapper;
 private JsonGenerator generator;
 private StringWriter stringWriter;

 @Before
 public void setUp() throws Exception {
 factory = new JsonFactory();
 mapper = new ObjectMapper(factory);
 stringWriter = new StringWriter();
 generator = factory.createGenerator(stringWriter);
 }

 @Test
 public void testWriteStringField() throws JsonProcessingException {
 // Test writing a string field with a simple value
 generator.writeStartObject();
 generator.writeFieldName("name");
 generator.writeString("John Doe");
 generator.writeEndObject();
 assertEquals("{\"name\":\"John Doe\"}", stringWriter.toString());

 // Test writing a string field with an empty value
 generator.writeStartObject();
 generator.writeFieldName("age");
 generator.writeString("");
 generator.writeEndObject();
 assertEquals("{\"age\":\"\"}", stringWriter.toString());

 // Test writing a string field with a null value
 generator.writeStartObject();
 generator.writeFieldName("city");
 generator.writeNull();
 generator.writeEndObject();
 assertEquals("{\"city\":null}", stringWriter.toString());
 }

 @Test(expected = IOException.class)
 public void testWriteStringFieldWithIOException() throws Exception {
 generator.writeStartObject();
 generator.writeFieldName("error");
 generator.writeString("This is an error message");
 stringWriter.close(); // Close the writer before writing EndObject to cause an IOException
 generator.writeEndObject();
 }
}