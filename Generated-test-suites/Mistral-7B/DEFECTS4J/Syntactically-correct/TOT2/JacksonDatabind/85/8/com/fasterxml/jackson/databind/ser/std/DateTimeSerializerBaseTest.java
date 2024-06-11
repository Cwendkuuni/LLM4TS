import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {
    private DateTimeSerializerBase<Long> dateTimeSerializer;

    @Before
    public void setup() {
        dateTimeSerializer = new DateTimeSerializerBase<Long>(Long.class, false, null);
    }

    @Test
    public void testWithFormat() {
        // Test with default values
        DateTimeSerializerBase<Long> withDefaultFormat = dateTimeSerializer.withFormat(false, null);
        assertEquals(dateTimeSerializer, withDefaultFormat);

        // Test with Boolean timestamp = true
        DateTimeSerializerBase<Long> withTimestamp = dateTimeSerializer.withFormat(true, null);
        assertNotEquals(dateTimeSerializer, withTimestamp);

        // Test with SimpleDateFormat customFormat
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        TimeZone customTimeZone = TimeZone.getTimeZone("UTC");
        customFormat.setTimeZone(customTimeZone);
        DateTimeSerializerBase<Long> withCustomFormat = dateTimeSerializer.withFormat(false, customFormat);
        assertNotEquals(dateTimeSerializer, withCustomFormat);

        // Test with Boolean timestamp = false and SimpleDateFormat customFormat
        DateTimeSerializerBase<Long> withTimestampFalseAndCustomFormat = dateTimeSerializer.withFormat(false, customFormat);
        assertNotEquals(dateTimeSerializer, withTimestampFalseAndCustomFormat);
        assertNotEquals(withTimestamp, withTimestampFalseAndCustomFormat);
        assertNotEquals(withCustomFormat, withTimestampFalseAndCustomFormat);
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<Instant> dateTimeSerializer = new DateTimeSerializerBase<Instant>(Instant.class, false, null);

        // Test empty value
        Instant emptyValue = null;
        assertTrue(dateTimeSerializer.isEmpty(emptyValue));

        // Test non-empty value
        Instant nonEmptyValue = Instant.now();
        assertFalse(dateTimeSerializer.isEmpty(nonEmptyValue));

        // Test non-empty value with SerializerProvider
        ObjectMapper mapper = new ObjectMapper();
        assertFalse(dateTimeSerializer.isEmpty(mapper.getSerializerProvider(), nonEmptyValue));
    }

    @Test
    public void testGetSchema() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        DateTimeSerializerBase<String> dateTimeSerializer = new DateTimeSerializerBase<String>(String.class, false, null);

        JsonNode schema = dateTimeSerializer.getSchema(mapper.getSerializerProvider(), String.class);
        assertEquals("string", schema.get("type").asText());

        dateTimeSerializer = new DateTimeSerializerBase<String>(String.class, true, null);
        schema = dateTimeSerializer.getSchema(mapper.getSerializerProvider(), String.class);
        assertEquals("number", schema.get("type").asText());
    }
}