package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapperWrapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializers;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.registerModule(new SimpleModule() {{
 addSerializer(Date.class, new DateTimeSerializerBase<Date>(Date.class, false, null));
 addSerializer(java.sql.Date.class, new DateTimeSerializerBase<java.sql.Date>(java.sql.Date.class, false, null));
 addSerializer(java.util.Date.class, new DateTimeSerializerBase<java.util.Date>(java.util.Date.class, false, null));
 }});
 objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 }

 @Test
 public void testSerializationWithDefaultFormat() throws JsonGenerationException, JsonMappingException {
 Date date = new Date();
 String json = objectMapper.writeValueAsString(date);
 ObjectNode jsonNode = (ObjectNode) objectMapper.readTree(json);
 assertEquals("2022-11-03T00:00:00.000Z", jsonNode.get("@class").asText());
 assertEquals("2022-11-03", jsonNode.get("$").asText());
 }

 @Test
 public void testSerializationWithCustomFormat() throws JsonGenerationException, JsonMappingException {
 ObjectMapper customMapper = new ObjectMapper(new JsonMapperWrapper(objectMapper) {
 @Override
 public void modifySerializationContext(SerializationContext ctxt) throws Exception {
 super.modifySerializationContext(ctxt);
 ctxt.setAttribute(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 ctxt.setCurrentSerializer(new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z")));
 }
 });
 Date date = new Date();
 String json = customMapper.writeValueAsString(date);
 ObjectNode jsonNode = (ObjectNode) customMapper.readTree(json);
 assertEquals("string", jsonNode.get("@class").asText());
 assertEquals("2022-11-03 12:34:56 +0100", jsonNode.get("$").asText());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationWithInvalidFormat() throws JsonGenerationException, JsonMappingException {
 ObjectMapper customMapper = new ObjectMapper(new JsonMapperWrapper(objectMapper) {
 @Override
 public void modifySerializationContext(SerializationContext ctxt) throws Exception {
 super.modifySerializationContext(ctxt);
 ctxt.setAttribute(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 ctxt.setCurrentSerializer(new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("xxxx-MM-dd")));
 }
 });
 Date date = new Date();
 try {
 customMapper.writeValueAsString(date);
 fail();
 } catch (JsonMappingException e) {
 // expected
 }
 }
}