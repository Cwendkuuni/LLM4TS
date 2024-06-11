import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class DateTimeSerializerBaseTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSerializeDateTimeSerializerBase() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
        JsonSerializer<Date> serializer = dateTimeSerializerBase.withFormat(Boolean.FALSE, null);

        Date date = new Date();
        String serializedDate = "";

        // Test serialization with default settings
        serializedDate = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(date);
        assertEquals("\"2022-12-31T16:45:23.000Z\"", serializedDate);

        // Test serialization with custom format
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        DateFormat reusedCustomFormat = null;
        DateTimeSerializerBase<Date> customSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
        JsonSerializer<Date> customSerializer = customSerializerBase.withFormat(Boolean.FALSE, customFormat);
        reusedCustomFormat = customSerializerBase._reusedCustomFormat.get();

        serializedDate = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(date);
        assertEquals("\"2022-12-31 16:45:23 CST\"", serializedDate);
        assertEquals(customFormat, reusedCustomFormat);
    }

    @Test
    public void testSerializeWithUseTimestampTrue() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Long> dateTimeSerializerBase = new DateTimeSerializerBase<Long>(Long.TYPE, true, null);
        JsonSerializer<Long> serializer = dateTimeSerializerBase.withFormat(Boolean.TRUE, null);

        Long timestamp = System.currentTimeMillis();
        String serializedTimestamp = "";

        serializedTimestamp = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(timestamp);
        assertEquals("\"1668995678000\"", serializedTimestamp);
    }

    @Test
    public void testSerializeWithNullValue() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
        JsonSerializer<Date> serializer = dateTimeSerializerBase.withFormat(Boolean.FALSE, null);

        Date date = null;
        String serializedDate = "";

        serializedDate = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(date);
        assertEquals("null", serializedDate);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeWithInvalidFormat() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, new DateFormat() {
            @Override
            public String format(Date date) throws ParseException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Date parse(String text) throws ParseException {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        JsonSerializer<Date> serializer = dateTimeSerializerBase.withFormat(Boolean.FALSE, null);

        Date date = new Date();
        String serializedDate = "";

        serializedDate = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(date);
        fail("Expected JsonMappingException");
    }
}