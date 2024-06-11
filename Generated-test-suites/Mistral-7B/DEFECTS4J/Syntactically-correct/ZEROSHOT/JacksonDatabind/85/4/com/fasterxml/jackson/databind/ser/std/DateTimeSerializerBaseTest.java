package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @SuppressWarnings("unchecked")
 @BeforeClass
 public static void setUpBeforeClass() throws Exception {
 mapper.registerModule(new SimpleModule() {
 @Override
 public void setupModule(SerializationConfig config) throws Exception {
 config.setSerializationInclusion(FindNullableFields.NON_NULL);
 }
 });
 }

 @Test
 public void testDateTimeSerializerBaseDefaultConstruction() {
 DateTimeSerializerBase<DateTime> serializer = mapper.getTypeFactory()
 .constructType(DateTimeSerializerBase.class, DateTime.class);
 assertEquals(DateTime.class, serializer.handledType());
 assertNull(serializer._useTimestamp);
 assertNull(serializer._customFormat);
 }

 @Test
 public void testDateTimeSerializerBaseWithTimestampConstruction() {
 DateTimeSerializerBase<DateTime> serializer = mapper.getTypeFactory()
 .constructType(DateTimeSerializerBase.class, DateTime.class, true, null);
 assertEquals(DateTime.class, serializer.handledType());
 assertTrue(serializer._useTimestamp);
 assertNull(serializer._customFormat);
 }

 @Test
 public void testDateTimeSerializerBaseWithCustomFormatConstruction() {
 DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
 DateTimeSerializerBase<DateTime> serializer = mapper.getTypeFactory()
 .constructType(DateTimeSerializerBase.class, DateTime.class, false, formatter.getFormat());
 assertEquals(DateTime.class, serializer.handledType());
 assertNull(serializer._useTimestamp);
 assertSame(formatter.getFormat(), serializer._customFormat);
 }

 @Test
 public void testDateTimeSerializerBaseWithFormat() throws JsonProcessingException {
 DateTimeSerializerBase<DateTime> serializer = mapper.getTypeFactory()
 .constructType(DateTimeSerializerBase.class, DateTime.class, false, null);
 assertEquals(DateTime.class, serializer.handledType());
 assertNull(serializer._useTimestamp);
 assertNull(serializer._customFormat);
 DateTimeSerializerBase<DateTime> serializerWithFormat = serializer.withFormat(false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));
 assertEquals(DateTime.class, serializerWithFormat.handledType());
 assertNull(serializerWithFormat._useTimestamp);
 assertSame(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"), serializerWithFormat._customFormat);
 }

 @Test
 public void testIsEmpty() {
 DateTimeSerializerBase<DateTime> serializer = mapper.getTypeFactory()
 .constructType(DateTimeSerializerBase.class, DateTime.class);
 assertTrue(serializer.isEmpty(null));
 assertTrue(serializer.isEmpty(serializer, null));
 assertTrue(serializer.isEmpty(serializer, new DateTime()));
 assertTrue(serializer.isEmpty(serializer, new DateTime(0)));
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 DateTimeSerializerBase<DateTime> serializer = mapper.getTypeFactory()
 .constructType(DateTimeSerializerBase.class, DateTime.class, false, null);
 JsonNode schema = serializer.getSchema(mapper, DateTime.class);
 assertEquals("string", schema.getClass().getSimpleName());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getJsonFormatVisitor(null, DateTime.class);
 DateTimeSerializerBase<DateTime> serializer = mapper.getTypeFactory()
 .constructType(DateTimeSerializerBase.class, DateTime.class, false, null);
 serializer._acceptJsonFormatVisitor(visitor, null, false);
 assertEquals(JsonFormat.Shape.STRING, visitor.getCurrentShape());
 }

 @Test
 public void testSerialize() throws JsonProcessingException {
 DateTimeSerializerBase<DateTime> serializer = mapper.getTypeFactory()
 .constructType(DateTimeSerializerBase.class, DateTime.class, false, null);
 DateTime date = new DateTime();
 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null, 0);
 serializer.serialize(date, gen, mapper.getSerializerProvider());
 assertEquals("2023-03-14T15:38:58.123Z", gen.getOutputContent().toString());

 DateTime dateWithTimeZone = new DateTime(1577925688000L, Locale.US, TimeZone.getTimeZone("UTC"));
 serializer = mapper.getTypeFactory()
 .constructType(DateTimeSerializerBase.class, DateTime.class, false, null);
 gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null, 0);
 serializer.serialize(dateWithTimeZone, gen, mapper.getSerializerProvider());
 assertEquals("2020-02-01T00:00:00.000Z", gen.getOutputContent().toString());

 serializer = mapper.getTypeFactory()
 .constructType(DateTimeSerializerBase.class, DateTime.class, true, null);
 gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null, 0);
 serializer.serialize(date, gen, mapper.getSerializerProvider());
 assertEquals("1636350838000", gen.getOutputContent().toString());
 }
}