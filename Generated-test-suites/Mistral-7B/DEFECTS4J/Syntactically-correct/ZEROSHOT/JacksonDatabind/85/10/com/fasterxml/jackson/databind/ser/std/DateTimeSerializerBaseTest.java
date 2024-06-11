import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdDateSerializers;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Date> underTest;

 @Before
 public void setUp() {
 DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.US);
 customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
 underTest = new StdDateSerializers.DateSerializer(Date.class, false, customFormat);
 }

 @Test
 public void testCreateContextual() throws JsonProcessingException, JsonGenerationException {
 ObjectCodec objectMapper = new ObjectMapper();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 JsonParser parser = objectMapper.getFactory().createParser(new ByteArrayInputStream(new byte[0]));

 Date date = new Date();

 // Test serialization as timestamp
 JsonSerializer<Date> serializerTimestamp = underTest.withFormat(true, null);
 serializerTimestamp.serialize(date, generator, null);
 assertEquals("1662141823000", generator.getOutputBuffer().toString());

 // Test serialization as string with custom format
 JsonSerializer<Date> serializerCustomFormat = underTest.withFormat(false, customFormat);
 serializerCustomFormat.serialize(date, generator, null);
 assertEquals("25-Jan-2023 14:23:53 UTC", generator.getOutputBuffer().toString());

 // Test parsing as timestamp
 JsonParser parserTimestamp = objectMapper.getFactory().createParser(new ByteArrayInputStream("1662141823000".getBytes()));
 JsonNode parsedNode = parserTimestamp.readValueAsTree();
 assertTrue(parsedNode instanceof NumberNode);

 // Test parsing as string with custom format
 JsonParser parserCustomFormat = objectMapper.getFactory().createParser(new ByteArrayInputStream("25-Jan-2023 14:23:53 UTC".getBytes()));
 JsonNode parsedNodeCustomFormat = parserCustomFormat.readValueAsTree();
 assertTrue(parsedNodeCustomFormat instanceof TextNode);
 assertEquals("25-Jan-2023 14:23:53 UTC", parsedNodeCustomFormat.textValue());
 }
}