package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Date> dateTimeSerializer;
 private DateTimeSerializerBase<Long> longSerializer;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 longSerializer = new DateTimeSerializerBase<Long>(Long.class, true, null);
 }

 @Test
 public void testDateTimeSerializerBaseEmpty() {
 SerializerProvider provider = mapper.getSerializerProvider();
 assertFalse(dateTimeSerializer.isEmpty(provider, null));
 assertFalse(longSerializer.isEmpty(provider, null));
 }

 @Test
 public void testDateTimeSerializerBaseCreateContextual() throws JsonMappingException {
 BeanProperty property = null;
 JsonFormat.Value format = null;
 JsonSerializer<Date> jsonSerializer = dateTimeSerializer.createContextual(mapper.getSerializerProvider(), property);
 assertEquals(dateTimeSerializer, jsonSerializer);

 property = new BeanProperty("test", Date.class, null, null, null, null);
 format = JsonFormat.value()
 .withShape(JsonFormat.Shape.STRING)
 .withLocale(Locale.US)
 .withTimeZone(TimeZone.getTimeZone("UTC"))
 .build();
 JsonSerializer<Date> jsonSerializerWithFormat = dateTimeSerializer.createContextual(mapper.getSerializerProvider(), property);
 assertNotEquals(dateTimeSerializer, jsonSerializerWithFormat);
 JsonFormat.Value formatResult = jsonSerializerWithFormat.getFormat();
 assertEquals(format, formatResult);

 property = new BeanProperty("test", Long.class, null, null, null, null);
 jsonSerializer = longSerializer.createContextual(mapper.getSerializerProvider(), property);
 assertEquals(longSerializer, jsonSerializer);
 }

 @Test
 public void testDateTimeSerializerBaseSerialize() throws IOException, JsonGenerationException {
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 Date date = new Date(System.currentTimeMillis());
 dateTimeSerializer.serialize(date, gen, mapper.getSerializerProvider());
 longSerializer.serialize(System.currentTimeMillis(), gen, mapper.getSerializerProvider());
 gen.flush();
 }

 @Test
 public void testDateTimeSerializerBaseGetSchema() {
 JsonNode schema = dateTimeSerializer.getSchema(mapper.getSerializerProvider(), Date.class);
 assertEquals("string", schema.asText());
 }

 @Test
 public void testDateTimeSerializerBaseAcceptJsonFormatVisitor() throws JsonMappingException {
 // String format
 JsonFormatVisitorWrapper visitor1 = mapper.schemaBuilder()
 .withFormatPrefix("test_")
 .addSerializableType(Date.class)
 .build();
 dateTimeSerializer.acceptJsonFormatVisitor(visitor1, null);
 JsonNode schema1 = visitor1.getSchema();
 assertEquals("string", schema1.asText());

 // Number format
 JsonFormatVisitorWrapper visitor2 = mapper.schemaBuilder()
 .withFormatPrefix("test_")
 .addSerializableType(Long.class)
 .build();
 longSerializer.acceptJsonFormatVisitor(visitor2, null);
 JsonNode schema2 = visitor2.getSchema();
 assertEquals("number", schema2.asText());
 }

 @Test(expected = JsonMappingException.class)
 public void testDateTimeSerializerBaseSerializeWithInvalidFormat() throws IOException {
 SerializerProvider provider = mapper.getSerializerProvider();
 DateFormat customFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a zzz", Locale.US);
 DateTimeSerializerBase<Date> serializerWithCustomFormat = new DateTimeSerializerBase<Date>(Date.class, false, customFormat) {
 @Override
 protected void _serializeAsString(Date value, JsonGenerator g, SerializerProvider provider) throws IOException {
 super._serializeAsString(value, g, provider);
 g.writeString("Custom format");
 }
 };
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 Date date = new Date(System.currentTimeMillis());
 serializerWithCustomFormat.serialize(date, gen, provider);
 }

 @Test
 public void testDateTimeSerializerBaseDeserialize() throws IOException, JsonGenerationException, ParseException {
 ObjectReader reader = mapper.readerForValueType(Date.class);
 String jsonDate = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new Date());
 Date date = reader.readValue(JsonParser.parseString(jsonDate));
 assertEquals(new Date(), date);

 ObjectReader readerLong = mapper.readerForValueType(Long.class);
 String jsonLong = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(System.currentTimeMillis());
 Long longValue = readerLong.readValue(JsonParser.parseString(jsonLong));
 assertEquals(System.currentTimeMillis(), longValue.longValue());
 }

 @Test
 public void testDateTimeSerializerBaseDeserializeWithFormat() throws IOException, JsonGenerationException, ParseException {
 ObjectMapper mapperWithFormat = new ObjectMapper();
 mapperWithFormat.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 mapperWithFormat.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 mapperWithFormat.registerModule(new SimpleModule() {
 @Override
 public void setupModule(SetupContext context) throws Exception {
 context.addSerializer(Long.class, longSerializer);
 context.addSerializer(Date.class, dateTimeSerializer);
 }
 });
 SerializerProvider provider = mapperWithFormat.getSerializerProvider();

 JsonFormat.Value format = JsonFormat.value()
 .withShape(JsonFormat.Shape.STRING)
 .withLocale(Locale.US)
 .withTimeZone(TimeZone.getTimeZone("UTC"))
 .build();

 JsonParser parser = mapperWithFormat.getFactory().createParser("2022-12-31T23:59:59.999Z");
 BeanProperty property = new BeanProperty("test", Date.class, null, null, null, null);
 JsonDeserialize<Date> jsonDeserialize = mapperWithFormat.findValueDeserializer(Date.class, property, provider);
 Date deserializedDate = jsonDeserialize.deserialize(parser, provider.getConfig(), null);
 assertEquals(new Date(1672829999999L), deserializedDate);

 JsonParser parserLong = mapperWithFormat.getFactory().createParser("1672829999999");
 JsonDeserialize<Long> jsonDeserializeLong = mapperWithFormat.findValueDeserializer(Long.class, property, provider);
 Long deserializedLong = jsonDeserializeLong.deserialize(parserLong, provider.getConfig(), null);
 assertEquals(1672829999999L, deserializedLong.longValue());
 }
}