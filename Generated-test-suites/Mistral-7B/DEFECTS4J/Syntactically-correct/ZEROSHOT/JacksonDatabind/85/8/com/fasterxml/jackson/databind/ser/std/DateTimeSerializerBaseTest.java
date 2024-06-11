package com.fasterxml.jackson.databind.ser.std.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase.DateTimeSerializerBaseBuilder;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Object> serializer;
 private DateTimeSerializerBaseBuilder builder;

 @Before
 public void setUp() {
 builder = new DateTimeSerializerBaseBuilder(Object.class, false, null);
 serializer = builder.build();
 }

 @Test
 public void testCreateContextualWithDefaultSettings() throws JsonProcessingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 BeanProperty property = new BeanProperty("/testProperty", Object.class, mapper.getClassInfoForBeanType(TestBean.class));
 JsonSerializer<?> contextualSerializer = serializer.createContextual(mapper.getSerializerProvider(), property);
 assertSame(serializer, contextualSerializer);
 }

 @Test
 public void testCreateContextualWithCustomFormat() throws JsonProcessingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 BeanProperty property = new BeanProperty("/testProperty", Object.class, mapper.getClassInfoForBeanType(TestBean.class));
 DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
 JsonSerializer<?> contextualSerializer = serializer.withFormat(Boolean.FALSE, customFormat).createContextual(mapper.getSerializerProvider(), property);
 assertNotSame(serializer, contextualSerializer);

 JsonNode serializedObject = mapper.writeValueAsTree(new TestBean());
 JsonParser parser = mapper.getFactory().createParser(serializedObject.toString());
 assertTrue(parser.nextToken().isValueInstance());
 TextNode textNode = (TextNode) parser.readValueAsTree();
 assertEquals("12-Jun-2023 02:30:45 EDT", textNode.textValue());
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateContextualWithIncompatibleFormat() throws JsonProcessingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 BeanProperty property = new BeanProperty("/testProperty", Object.class, mapper.getClassInfoForBeanType(TestBean.class));
 JsonFormat.Value format = JsonFormat.value().buildShape(JsonFormat.Shape.NUMBER);
 JsonSerializer<?> contextualSerializer = serializer.createContextual(mapper.getSerializerProvider(), property);
 fail("Expected JsonMappingException, but no exception was thrown.");
 }

 @Test
 public void testSerializeWithDefaultSettings() throws JsonProcessingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out, null);
 serializer.serialize(new TestBean(), jsonGenerator, mapper.getSerializerProvider());
 jsonGenerator.flush();
 assertTrue(mapper.getFactory().getValidator().isValidTree(mapper.readTree(System.in)));
 }

 @Test
 public void testSerializeWithCustomFormat() throws JsonProcessingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out, null);
 DateTimeSerializerBase<Object> customSerializer = builder.withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z")).build();
 customSerializer.serialize(new TestBean(), jsonGenerator, mapper.getSerializerProvider());
 jsonGenerator.flush();
 assertTrue(mapper.getFactory().getValidator().isValidTree(mapper.readTree(System.in)));
 assertTrue(mapper.readTree(System.in).get("testProperty").isTextValue());
 assertEquals("12-Jun-2023 02:30:45 EDT", mapper.readTree(System.in).get("testProperty").asText());
 }

 @Test
 public void testSerializeWithTimestamp() throws JsonProcessingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out, null);
 DateTimeSerializerBase<Object> timestampSerializer = builder.withFormat(Boolean.TRUE, null).build();
 timestampSerializer.serialize(new TestBean(), jsonGenerator, mapper.getSerializerProvider());
 jsonGenerator.flush();
 assertTrue(mapper.getFactory().getValidator().isValidTree(mapper.readTree(System.in)));
 assertTrue(mapper.readTree(System.in).get("testProperty").isNumber());
 assertEquals("1672736645000", mapper.readTree(System.in).get("testProperty").asText());
 }

 @Test
 public void testGetSchema() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), Object.class);
 assertEquals("number", schema.getType());
 }

 @Test
 public void testGetSchemaAsString() throws JsonProcessingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 String schemaString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(serializer.getSchema(mapper.getSerializerProvider(), Object.class));
 assertEquals("\"number\"", schemaString);
 }

 @Test
 public void testGetSchemaAsStringWithCustomFormat() throws JsonProcessingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 DateTimeSerializerBase<Object> customSerializer = builder.withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z")).build();
 String schemaString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(customSerializer.getSchema(mapper.getSerializerProvider(), Object.class));
 assertEquals("\"string\"", schemaString);
 }

}

class TestBean {
 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss z")
 @JsonProperty("testProperty")
 private Object date;
}

class DateTimeSerializerBaseBuilder {
 private final Class<T> type;
 private Boolean useTimestamp;
 private DateFormat customFormat;

 public DateTimeSerializerBaseBuilder(Class<T> type, Boolean useTimestamp, DateFormat customFormat) {
 this.type = type;
 this.useTimestamp = useTimestamp;
 this.customFormat = customFormat;
 }

 public DateTimeSerializerBaseBuilder withFormat(Boolean useTimestamp, DateFormat customFormat) {
 this.useTimestamp = useTimestamp;
 this.customFormat = customFormat;
 return this;
 }

 public DateTimeSerializerBase<T> build() {
 return new DateTimeSerializerBase<T>(type, useTimestamp, customFormat) {};
 }
}