import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase.DateTimeSerializerBaseWithFormat;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;

public class DateTimeSerializerBaseTest {

    private ObjectMapper objectMapper;
    private JsonFactory jsonFactory;
    private JsonParser jsonParser;
    private JsonGenerator jsonGenerator;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        jsonFactory = objectMapper.getFactory();
        jsonParser = jsonFactory.createParser(new ByteArrayInputStream(new byte[0]));
        jsonGenerator = jsonFactory.createGenerator(System.out);
    }

    @Test
    public void testSerializeTimestamp() throws IOException {
        DateTimeSerializerBaseWithFormat<Long> serializer = new DateTimeSerializerBase.DateTimeSerializerBaseWithFormat<Long>(
                Long.TYPE,
                true,
                null
        );
        Long timestamp = 1615131520000L; // 2020-10-01 14:35:20.000
        jsonGenerator.writeStartObject();
        serializer.serialize(timestamp, jsonGenerator, null);
        jsonGenerator.writeEndObject();
        jsonGenerator.flush();

        JsonParser parser = jsonFactory.createParser(new ByteArrayInputStream(jsonGenerator.getOutputBytes()));
        assertEquals("1615131520000", parser.getValueAsString());
    }

    @Test
    public void testSerializeCustomFormat() throws IOException, ParseException {
        DateTimeSerializerBaseWithFormat<ZonedDateTime> serializer = new DateTimeSerializerBase.DateTimeSerializerBaseWithFormat<ZonedDateTime>(
                ZonedDateTime.class,
                false,
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        );

        ZonedDateTime dateTime = ZonedDateTime.of(2020, 10, 1, 14, 35, 20, 0, ZoneId.systemDefault());
        jsonGenerator.writeStartObject();
        serializer.serialize(dateTime, jsonGenerator, null);
        jsonGenerator.writeEndObject();
        jsonGenerator.flush();

        JsonParser parser = jsonFactory.createParser(new ByteArrayInputStream(jsonGenerator.getOutputBytes()));
        String dateString = parser.getValueAsString();
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(dateString);
        assertEquals(dateTime.toInstant().toEpochMilli(), date.getTime());
    }

    @Test
    public void testSerializeWithCustomFormatReuse() throws IOException, ParseException {
        DateTimeSerializerBaseWithFormat<ZonedDateTime> serializer = new DateTimeSerializerBase.DateTimeSerializerBaseWithFormat<ZonedDateTime>(
                ZonedDateTime.class,
                false,
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        );

        ZonedDateTime dateTime1 = ZonedDateTime.of(2020, 10, 1, 14, 35, 20, 0, ZoneId.systemDefault());
        ZonedDateTime dateTime2 = ZonedDateTime.of(2020, 10, 1, 14, 35, 20, 0, ZoneId.systemDefault());

        jsonGenerator.writeStartObject();
        serializer.serialize(dateTime1, jsonGenerator, null);
        jsonGenerator.writeEndObject();
        jsonGenerator.writeStartObject();
        serializer.serialize(dateTime2, jsonGenerator, null);
        jsonGenerator.writeEndObject();
        jsonGenerator.flush();

        JsonParser parser = jsonFactory.createParser(new ByteArrayInputStream(jsonGenerator.getOutputBytes()));

        String dateString1 = parser.nextValue().asText();
        Date date1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(dateString1);
        assertEquals(dateTime1.toInstant().toEpochMilli(), date1.getTime());

        parser.nextToken(); // consume the comma
        String dateString2 = parser.nextValue().asText();
        Date date2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(dateString2);
        assertEquals(dateTime2.toInstant().toEpochMilli(), date2.getTime());
    }

    @Test
    public void testSerializeWithCustomFormatNull() throws IOException {
        DateTimeSerializerBaseWithFormat<ZonedDateTime> serializer = new DateTimeSerializerBase.DateTimeSerializerBaseWithFormat<ZonedDateTime>(
                ZonedDateTime.class,
                false,
                null
        );

        ZonedDateTime dateTime = ZonedDateTime.of(2020, 10, 1, 14, 35, 20, 0, ZoneId.systemDefault());

        jsonGenerator.writeStartObject();
        serializer.serialize(dateTime, jsonGenerator, null);
        jsonGenerator.writeEndObject();
        jsonGenerator.flush();

        JsonParser parser = jsonFactory.createParser(new ByteArrayInputStream(jsonGenerator.getOutputBytes()));
        parser.nextToken(); // consume the opening curly brace

        // Jackson should use the default DateFormat instance
        String dateString = parser.nextValue().asText();
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(dateString);
        assertEquals(dateTime.toInstant().toEpochMilli(), date.getTime());
    }
}