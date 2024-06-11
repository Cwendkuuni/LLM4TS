package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonGenerator;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Long> longSerializer;
 private DateTimeSerializerBase<String> stringSerializer;

 @Before
 public void setUp() {
 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
 longSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, dateFormat) {
 @Override protected long _timestamp(Long value) { return value; }
 };
 stringSerializer = new DateTimeSerializerBase<String>(String.class, false, dateFormat) {
 @Override protected long _timestamp(String value) {
 try {
 return dateFormat.parse(value).getTime();
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 }
 };
 }

 @Test
 public void testSerializationWithDefaultSettings() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 Long dateAsLong = 1612368321000L; // Fri, 06 Aug 2021 12:22:01 GMT
 String dateAsString = "2021-08-06T12:22:01.000Z";

 Long valueToSerializeAsLong = dateAsLong;
 String valueToSerializeAsString = dateAsString;

 JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
 TypeSerializer typeSerializer = mapper.getTypeSerializer();
 typeSerializer.writeValue(jsonGenerator, valueToSerializeAsLong);
 typeSerializer.writeValue(jsonGenerator, valueToSerializeAsString);
 jsonGenerator.flush();

 JsonParser jsonParser = mapper.getFactory().createParser(System.in);
 Long deserializedLong = mapper.readValue(jsonParser, Long.class);
 String deserializedString = mapper.readValue(jsonParser, String.class);

 assertEquals(valueToSerializeAsLong, deserializedLong);
 assertEquals(valueToSerializeAsString, deserializedString);
 }

 @Test
 public void testSerializationWithTimestampFormat() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
 Long dateAsLong = 1612368321000L; // Fri, 06 Aug 2021 12:22:01 GMT
 String dateAsString = "2021-08-06T12:22:01.000Z";

 JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
 TypeSerializer typeSerializer = mapper.getTypeSerializer();
 typeSerializer.writeValue(jsonGenerator, dateAsLong);
 typeSerializer.writeValue(jsonGenerator, dateAsString);
 jsonGenerator.flush();

 JsonParser jsonParser = mapper.getFactory().createParser(System.in);
 Long deserializedLong = mapper.readValue(jsonParser, Long.class);
 String deserializedString = mapper.readValue(jsonParser, String.class);

 assertEquals(valueToSerializeAsLong, deserializedLong);
 assertEquals(valueToSerializeAsString, deserializedString);
 }

 @Test
 public void testSerializationWithCustomFormat() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
 DateTimeSerializerBase<Long> customSerializerLong = new DateTimeSerializerBase<Long>(Long.TYPE, false, customFormat) {
 @Override protected long _timestamp(Long value) { return value; }
 };
 DateTimeSerializerBase<String> customSerializerString = new DateTimeSerializerBase<String>(String.class, false, customFormat) {
 @Override protected long _timestamp(String value) {
 try {
 return customFormat.parse(value).getTime();
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 }
 };

 JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
 SerializerProvider serializers = mapper.getSerializerProvider();
 serializers.setSerializer(Long.class, customSerializerLong);
 serializers.setSerializer(String.class, customSerializerString);

 Long valueToSerializeAsLong = 1612368321000L; // Fri, 06 Aug 2021 12:22:01 GMT
 String valueToSerializeAsString = "06-08-2021 12:22:01";

 customSerializerLong.serialize(valueToSerializeAsLong, jsonGenerator, serializers);
 customSerializerString.serialize(valueToSerializeAsString, jsonGenerator, serializers);
 jsonGenerator.flush();

 JsonParser jsonParser = mapper.getFactory().createParser(System.in);
 Long deserializedLong = mapper.readValue(jsonParser, Long.class);
 String deserializedString = mapper.readValue(jsonParser, String.class);

 assertEquals(valueToSerializeAsLong, deserializedLong);
 assertEquals(valueToSerializeAsString, deserializedString);
 }
}