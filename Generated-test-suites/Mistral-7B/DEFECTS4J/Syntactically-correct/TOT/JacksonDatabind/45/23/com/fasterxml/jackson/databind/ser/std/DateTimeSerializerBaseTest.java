package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DateTimeSerializerBaseTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 }

 @After
 public void teardown() throws Exception {
 mapper.close();
 }

 @Test
 public void testSerializingDateWithCustomFormat() throws JsonProcessingException {
 Date date = new Date(1564432000000L); // 1 Jan 2019
 JsonFormat jsonFormat = new JsonFormat(JsonFormat.Shape.STRING, "dd-MM-yyyy HH:mm:ss z");
 ObjectNode node = mapper.readValue("{\"date\":\"31-12-2020 10:20:30 GMT\"}", ObjectNode.class);
 SerializerProvider serializers = mapper.getSerializationConfig().getSerializerProvider();
 DateTimeSerializerBase<Date> serializer = new SimpleDateSerializer(Date.class, false, null);
 DateTimeSerializerBase<Date> customSerializer = serializer.withFormat(false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.ENGLISH));
 ObjectNode resultNode = mapper.writeValueAsTree(date, customSerializer, serializers);
 String expectedResult = "\"31-12-2020 10:20:30 GMT\"";
 assertEquals(expectedResult, resultNode.get("date").asText());
 }

 @Test
 public void testSerializingDateWithoutCustomFormat() throws JsonProcessingException {
 Date date = new Date(1564432000000L); // 1 Jan 2019
 ObjectNode node = mapper.readValue("{\"date\":\"2019-01-01T00:00:00Z\"}", ObjectNode.class);
 SerializerProvider serializers = mapper.getSerializationConfig().getSerializerProvider();
 DateTimeSerializerBase<Date> serializer = new SimpleDateSerializer(Date.class, false, null);
 ObjectNode resultNode = mapper.writeValueAsTree(date, serializer, serializers);
 String expectedResult = "1564432000000";
 assertEquals(expectedResult, resultNode.get("date").asText());
 }

 @Test
 public void testSerializingNullDate() throws JsonProcessingException {
 ObjectNode node = mapper.readValue("{\"date\":null}", ObjectNode.class);
 SerializerProvider serializers = mapper.getSerializationConfig().getSerializerProvider();
 DateTimeSerializerBase<Date> serializer = new SimpleDateSerializer(Date.class, false, null);
 ObjectNode resultNode = mapper.writeValueAsTree(null, serializer, serializers);
 assertNull(resultNode.get("date"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializingDateWithNullSerializerProvider() throws JsonProcessingException {
 Date date = new Date(1564432000000L); // 1 Jan 2019
 ObjectNode node = mapper.readValue("{\"date\":\"2019-01-01T00:00:00Z\"}", ObjectNode.class);
 DateTimeSerializerBase<Date> serializer = new SimpleDateSerializer(Date.class, false, null);
 ObjectNode resultNode = mapper.writeValueAsTree(date, serializer, null);
 }
}