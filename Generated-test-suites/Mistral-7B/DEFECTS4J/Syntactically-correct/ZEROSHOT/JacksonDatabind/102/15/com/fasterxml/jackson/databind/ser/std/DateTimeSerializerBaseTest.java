import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Date> dateTimeSerializer;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 dateTimeSerializer = objectMapper.findValueSerializer(Date.class);
 }

 @Test
 public void testDateTimeSerializerBaseWithDefaultFormat() throws Exception {
 Date date = new Date();
 String serializedDate = objectMapper.writeValueAsString(date);
 JsonParser parser = objectMapper.getFactory().createParser(serializedDate);
 Date deserializedDate = objectMapper.readValue(parser, Date.class);
 assertEquals(date, deserializedDate);
 }

 @Test
 public void testDateTimeSerializerBaseWithCustomFormat() throws Exception {
 Date date = new Date();
 SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
 DateTimeSerializerBase<Date> customSerializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
 ObjectNode node = objectMapper.createObjectNode();
 ObjectNode jsonNode = node.putObject("date");
 jsonNode = jsonNode.putObject("format");
 jsonNode = jsonNode.putObject("pattern");
 jsonNode.put("pattern", "dd-MM-yyyy HH:mm:ss");
 ObjectNode rootNode = node.putObject("dateObject");
 rootNode.putPOJO("date", date);
 String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
 JsonParser parser = objectMapper.getFactory().createParser(jsonString);
 JsonNode rootNodeParser = parser.readValueAsTree();
 ObjectNode rootNodeDeserialized = (ObjectNode) rootNodeParser.get("dateObject");
 ObjectNode dateNodeDeserialized = rootNodeDeserialized.get("date");
 Date deserializedDate = objectMapper.readValue(dateNodeDeserialized, Date.class);
 assertEquals(date, deserializedDate);

 JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
 customSerializer.serialize(date, generator, objectMapper.getSerializationConfig());
 String serializedDateCustomFormat = generator.getOutputStream().toString();
 Date deserializedDateCustomFormat = objectMapper.readValue(serializedDateCustomFormat, Date.class);
 assertEquals(date, deserializedDateCustomFormat);
 }

 @Test
 public void testDateTimeSerializerBaseWithCustomLocale() throws Exception {
 Date date = new Date();
 SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
 customFormat.setLocale(Locale.FRANCE);
 DateTimeSerializerBase<Date> customSerializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
 ObjectNode node = objectMapper.createObjectNode();
 ObjectNode jsonNode = node.putObject("date");
 jsonNode = jsonNode.putObject("format");
 jsonNode = jsonNode.putObject("locale");
 jsonNode.put("locale", Locale.FRANCE.toString());
 jsonNode = jsonNode.putObject("pattern");
 jsonNode.put("pattern", "dd-MM-yyyy HH:mm:ss");
 ObjectNode rootNode = node.putObject("dateObject");
 rootNode.putPOJO("date", date);
 String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
 JsonParser parser = objectMapper.getFactory().createParser(jsonString);
 JsonNode rootNodeParser = parser.readValueAsTree();
 ObjectNode rootNodeDeserialized = (ObjectNode) rootNodeParser.get("dateObject");
 ObjectNode dateNodeDeserialized = rootNodeDeserialized.get("date");
 Date deserializedDate = objectMapper.readValue(dateNodeDeserialized, Date.class);
 assertEquals(date, deserializedDate);

 JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
 customSerializer.serialize(date, generator, objectMapper.getSerializationConfig());
 String serializedDateCustomLocale = generator.getOutputStream().toString();
 Date deserializedDateCustomLocale = objectMapper.readValue(serializedDateCustomLocale, Date.class);
 assertEquals(date, deserializedDateCustomLocale);
 }

 @Test
 public void testDateTimeSerializerBaseWithCustomTimeZone() throws Exception {
 Date date = new Date();
 SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
 TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
 customFormat.setTimeZone(timeZone);
 DateTimeSerializerBase<Date> customSerializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
 ObjectNode node = objectMapper.createObjectNode();
 ObjectNode jsonNode = node.putObject("date");
 jsonNode = jsonNode.putObject("format");
 jsonNode = jsonNode.putObject("timeZone");
 jsonNode.put("timeZone", "America/New_York");
 jsonNode = jsonNode.putObject("pattern");
 jsonNode.put("pattern", "dd-MM-yyyy HH:mm:ss");
 ObjectNode rootNode = node.putObject("dateObject");
 rootNode.putPOJO("date", date);
 String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
 JsonParser parser = objectMapper.getFactory().createParser(jsonString);
 JsonNode rootNodeParser = parser.readValueAsTree();
 ObjectNode rootNodeDeserialized = (ObjectNode) rootNodeParser.get("dateObject");
 ObjectNode dateNodeDeserialized = rootNodeDeserialized.get("date");
 Date deserializedDate = objectMapper.readValue(dateNodeDeserialized, Date.class);
 assertEquals(date, deserializedDate);

 JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
 customSerializer.serialize(date, generator, objectMapper.getSerializationConfig());
 String serializedDateCustomTimeZone = generator.getOutputStream().toString();
 Date deserializedDateCustomTimeZone = objectMapper.readValue(serializedDateCustomTimeZone, Date.class);
 assertEquals(date, deserializedDateCustomTimeZone);
 }

 @Test
 public void testDateTimeSerializerBaseWithCustomFormatAndCustomLocale() throws Exception {
 Date date = new Date();
 SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
 customFormat.setLocale(Locale.FRANCE);
 DateTimeSerializerBase<Date> customSerializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
 ObjectNode node = objectMapper.createObjectNode();
 ObjectNode jsonNode = node.putObject("date");
 jsonNode = jsonNode.putObject("format");
 jsonNode = jsonNode.putObject("locale");
 jsonNode.put("locale", Locale.FRANCE.toString());
 jsonNode = jsonNode.putObject("pattern");
 jsonNode.put("pattern", "dd-MM-yyyy HH:mm:ss");
 ObjectNode rootNode = node.putObject("dateObject");
 rootNode.putPOJO("date", date);
 String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
 JsonParser parser = objectMapper.getFactory().createParser(jsonString);
 JsonNode rootNodeParser = parser.readValueAsTree();
 ObjectNode rootNodeDeserialized = (ObjectNode) rootNodeParser.get("dateObject");
 ObjectNode dateNodeDeserialized = rootNodeDeserialized.get("date");
 Date deserializedDate = objectMapper.readValue(dateNodeDeserialized, Date.class);
 assertEquals(date, deserializedDate);

 JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
 customSerializer.serialize(date, generator, objectMapper.getSerializationConfig());
 String serializedDateCustomFormatCustomLocale = generator.getOutputStream().toString();
 Date deserializedDateCustomFormatCustomLocale = objectMapper.readValue(serializedDateCustomFormatCustomLocale, Date.class);
 assertEquals(date, deserializedDateCustomFormatCustomLocale);
 }

 @Test
 public void testDateTimeSerializerBaseWithCustomFormatAndCustomTimeZone() throws Exception {
 Date date = new Date();
 SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
 TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
 customFormat.setTimeZone(timeZone);
 DateTimeSerializerBase<Date> customSerializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
 ObjectNode node = objectMapper.createObjectNode();
 ObjectNode jsonNode = node.putObject("date");
 jsonNode = jsonNode.putObject("format");
 jsonNode = jsonNode.putObject("timeZone");
 jsonNode.put("timeZone", "America/New_York");
 jsonNode = jsonNode.putObject("pattern");
 jsonNode.put("pattern", "dd-MM-yyyy HH:mm:ss");
 ObjectNode rootNode = node.putObject("dateObject");
 rootNode.putPOJO("date", date);
 String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
 JsonParser parser = objectMapper.getFactory().createParser(jsonString);
 JsonNode rootNodeParser = parser.readValueAsTree();
 ObjectNode rootNodeDeserialized = (ObjectNode) rootNodeParser.get("dateObject");
 ObjectNode dateNodeDeserialized = rootNodeDeserialized.get("date");
 Date deserializedDate = objectMapper.readValue(dateNodeDeserialized, Date.class);
 assertEquals(date, deserializedDate);

 JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
 customSerializer.serialize(date, generator, objectMapper.getSerializationConfig());
 String serializedDateCustomFormatCustomTimeZone = generator.getOutputStream().toString();
 Date deserializedDateCustomFormatCustomTimeZone = objectMapper.readValue(serializedDateCustomFormatCustomTimeZone, Date.class);
 assertEquals(date, deserializedDateCustomFormatCustomTimeZone);
 }

 @Test
 public void testDateTimeSerializerBaseWithCustomFormatAndCustomLocaleAndCustomTimeZone() throws Exception {
 Date date = new Date();
 SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
 customFormat.setLocale(Locale.FRANCE);
 TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
 customFormat.setTimeZone(timeZone);
 DateTimeSerializerBase<Date> customSerializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
 ObjectNode node = objectMapper.createObjectNode();
 ObjectNode jsonNode = node.putObject("date");
 jsonNode = jsonNode.putObject("format");
 jsonNode = jsonNode.putObject("locale");
 jsonNode.put("locale", Locale.FRANCE.toString());
 jsonNode = jsonNode.putObject("pattern");
 jsonNode.put("pattern", "dd-MM-yyyy HH:mm:ss");
 jsonNode = jsonNode.putObject("timeZone");
 jsonNode.put("timeZone", "America/New_York");
 ObjectNode rootNode = node.putObject("dateObject");
 rootNode.putPOJO("date", date);
 String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
 JsonParser parser = objectMapper.getFactory().createParser(jsonString);
 JsonNode rootNodeParser = parser.readValueAsTree();
 ObjectNode rootNodeDeserialized = (ObjectNode) rootNodeParser.get("dateObject");
 ObjectNode dateNodeDeserialized = rootNodeDeserialized.get("date");
 Date deserializedDate = objectMapper.readValue(dateNodeDeserialized, Date.class);
 assertEquals(date, deserializedDate);

 JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
 customSerializer.serialize(date, generator, objectMapper.getSerializationConfig());
 String serializedDateCustomFormatCustomLocaleCustomTimeZone = generator.getOutputStream().toString();
 Date deserializedDateCustomFormatCustomLocaleCustomTimeZone = objectMapper.readValue(serializedDateCustomFormatCustomLocaleCustomTimeZone, Date.class);
 assertEquals(date, deserializedDateCustomFormatCustomLocaleCustomTimeZone);
 }

 @Test
 public void testDateTimeSerializerBaseWithTimestamp() throws Exception {
 Date date = new Date();
 long timestamp = date.getTime();
 String serializedDate = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(timestamp);
 long deserializedTimestamp = objectMapper.readValueAsLong(serializedDate);
 Date deserializedDate = new Date(deserializedTimestamp);
 assertEquals(date, deserializedDate);
 }
}