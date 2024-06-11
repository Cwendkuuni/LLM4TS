package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StdKeySerializerTest {

 private ObjectMapper mapper;

 @SuppressWarnings("static-access")
 @Before
 public void setup() {
 mapper = JsonMapperFactory.getMapper();
 }

 @Test
 public void testSerializeDate() throws IOException {
 Date date = new Date();
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(date, gen, new SerializerProvider.StandardSerializerProvider());
 gen.flush();
 // Verify that the date is serialized as a JSON string in the format "YYYY-MM-DD'T'HH:mm:ss.SSSZ"
 String expectedDateFormat = "2022-12-31T23:59:59.999Z";
 String actualDateFormat = gen.getOutputBuffer().toString();
 assertTrue(actualDateFormat.matches(expectedDateFormat));
 }

 @Test
 public void testSerializeString() throws IOException {
 String key = "testKey";
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(key, gen, new SerializerProvider.StandardSerializerProvider());
 gen.flush();
 String actualString = gen.getOutputBuffer().toString();
 assertEquals("testKey", actualString);
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 StdKeySerializer serializer = new StdKeySerializer();
 ObjectNode expectedSchema = mapper.createObjectNode();
 expectedSchema.put("type", "string");
 ObjectNode actualSchema = serializer.getSchema(new SerializerProvider.StandardSerializerProvider(), Object.class);
 assertEquals(expectedSchema.toString(), actualSchema.toString());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 StdKeySerializer serializer = new StdKeySerializer();
 // It is expected that no exception is thrown since the serializer accepts string format
 serializer.acceptJsonFormatVisitor(null, null);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidType() throws JsonMappingException {
 StdKeySerializer serializer = new StdKeySerializer();
 serializer.acceptJsonFormatVisitor(null, new JavaType(int.class));
 }
}