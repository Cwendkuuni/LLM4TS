import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {
    @Test
    public void testWithFormat() throws Exception {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, false, null);

        // Test with null arguments
        DateTimeSerializerBase<Long> serializer1 = serializer.withFormat(null, null);
        assertEquals(serializer, serializer1);

        // Test with Boolean and null DateFormat
        DateTimeSerializerBase<Long> serializer2 = serializer.withFormat(true, null);
        assertEquals(serializer, serializer2);

        // Test with Boolean and SimpleDateFormat
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        DateTimeSerializerBase<Long> serializer3 = serializer.withFormat(true, dateFormat);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        ObjectNode node = mapper.createObjectNode();
        node.put("test", 1L);
        String json = mapper.writerWithRootValue(node).writeValueAsString(serializer3);
        assertEquals("\"2023-02-19T00:00:00.000Z\"", json);

        // Test with Boolean and SimpleDateFormat with TimeZone
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        DateTimeSerializerBase<Long> serializer4 = serializer.withFormat(true, dateFormat);
        dateFormat.setTimeZone(timeZone);
        json = mapper.writerWithRootValue(node).writeValueAsString(serializer4);
        assertEquals("\"2023-02-19T00:00:00.000Z\"", json);

        // Test with Boolean and SimpleDateFormat with Locale
        Locale locale = Locale.US;
        DateTimeSerializerBase<Long> serializer5 = serializer.withFormat(true, dateFormat);
        dateFormat.setLocale(locale);
        json = mapper.writerWithRootValue(node).writeValueAsString(serializer5);
        assertEquals("\"02/19/2023 00:00:00.000 AM\"", json);

        // Test with Boolean and null DateFormat and custom serializer
        DateTimeSerializerBase<Long> customSerializer = new DateTimeSerializerBase<Long>(Long.class, false, null) {
            @Override
            public abstract DateTimeSerializerBase<T> withFormat(Boolean timestamp, DateFormat customFormat);

            @Override
            public DateTimeSerializerBase<T> withFormat(Boolean timestamp, DateFormat customFormat) {
                if (customFormat == null) {
                    return this;
                }
                SimpleDateFormat df = (SimpleDateFormat) customFormat;
                df.setTimeZone(TimeZone.getTimeZone("UTC"));
                return super.withFormat(timestamp, df);
            }
        };
        json = mapper.writerWithRootValue(node).writeValueAsString(customSerializer);
        assertEquals("\"2023-02-19T00:00:00.000Z\"", json);

        // Test with false and null DateFormat
        DateTimeSerializerBase<Long> serializer6 = serializer.withFormat(false, null);
        json = mapper.writerWithRootValue(node).writeValueAsString(serializer6);
        assertNotNull(json);
        assertTrue(json.startsWith("\"") && json.endsWith("\""));
    }
}