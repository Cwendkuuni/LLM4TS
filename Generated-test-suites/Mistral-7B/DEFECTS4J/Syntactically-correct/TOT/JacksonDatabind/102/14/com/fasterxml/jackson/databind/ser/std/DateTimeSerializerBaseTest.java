import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

    private ObjectMapper objectMapper;

    public DateTimeSerializerBaseTest() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    public void testDateTimeSerializerBase_useTimestamp() throws JsonMappingException, JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer();
        assertNull(serializer._customFormat);
        assertFalse(serializer._useTimestamp);

        Date date = new Date();
        String json = objectMapper.writeValueAsString(date, serializer);
        assertTrue(json.matches("\\d+")); // Numeric representation of timestamp

        Date deserializedDate = objectMapper.readValue(json, Date.class);
        assertEquals(date, deserializedDate);
    }

    @Test
    public void testDateTimeSerializerBase_customFormat() throws JsonMappingException, JsonProcessingException {
        DateTimeSerializerBase<ZonedDateTime> serializer = new StdDateSerializer() {
            @Override
            protected DateFormat _customFormat;

            {
                _customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
            }
        };

        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        String json = objectMapper.writeValueAsString(zonedDateTime, serializer);
        assertEquals("\"2023-03-22 13:34:56 EST\"", json);

        ZonedDateTime deserializedZonedDateTime = objectMapper.readValue(json, ZonedDateTime.class);
        assertEquals(zonedDateTime, deserializedZonedDateTime);
    }

    @Test
    public void testDateTimeSerializerBase_reusedCustomFormat() throws JsonMappingException, JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null) {
            @Override
            protected AtomicReference<DateFormat> _reusedCustomFormat;

            {
                _reusedCustomFormat = new AtomicReference<>();
            }
        };

        Date date = new Date();
        String json = objectMapper.writeValueAsString(date, serializer);

        // Test the first serialization
        SerializationTestUtil.assertSerialization(Date.class, date, serializer, json);

        // Test the second serialization
        String json2 = objectMapper.writeValueAsString(date, serializer);
        assertEquals(json, json2);

        // Test deserialization
        Date deserializedDate = objectMapper.readValue(json, Date.class);
        assertEquals(date, deserializedDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateTimeSerializerBase_nullSerializerProvider() throws JsonMappingException, JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer();
        serializer.createContextual(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateTimeSerializerBase_customFormat_nullPattern() throws JsonMappingException, JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null) {
            @Override
            protected DateFormat _customFormat;

            {
                _customFormat = null;
            }
        };

        Date date = new Date();
        String json = objectMapper.writeValueAsString(date, serializer);
        fail("Expected IllegalArgumentException but got: " + json);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateTimeSerializerBase_customFormat_nullLocale() throws JsonMappingException, JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null) {
            @Override
            protected DateFormat _customFormat;

            {
                _customFormat = new SimpleDateFormat("dd-MM-yyyy");
                _customFormat.setLocale(null);
            }
        };

        Date date = new Date();
        String json = objectMapper.writeValueAsString(date, serializer);
        fail("Expected IllegalArgumentException but got: " + json);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateTimeSerializerBase_customFormat_nullTimeZone() throws JsonMappingException, JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null) {
            @Override
            protected DateFormat _customFormat;

            {
                _customFormat = new SimpleDateFormat("dd-MM-yyyy");
                _customFormat.setTimeZone(null);
            }
        };

        Date date = new Date();
        String json = objectMapper.writeValueAsString(date, serializer);
        fail("Expected IllegalArgumentException but got: " + json);
    }
}