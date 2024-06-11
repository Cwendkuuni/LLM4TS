import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class DateTimeSerializerBaseTest {

    private ObjectMapper objectMapper;

    public DateTimeSerializerBaseTest() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDefaultSerialization() throws IOException {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase.StdDateSerializer(Date.class, false, null);
        JsonSerializer<Date> jsonSerializer = dateSerializer.createContextual(null, null);

        Date date = new Date();
        String serializedDate = jsonSerializer.serializeValueAsString(date, null);
        Date deserializedDate = jsonSerializer.deserializeValueAs(null, String.class, null);

        assertEquals("Expected default serialized date to deserialize back to original date", date, deserializedDate);
        assertEquals("Expected default serialized date to start with 'N'", serializedDate.substring(0, 1), "N");
    }

    @Test
    public void testCustomSerializationWithFormat() throws IOException {
        DateTimeSerializerBase<LocalDateTime> localDateTimeSerializer = new DateTimeSerializerBase<LocalDateTime>() {
            @Override
            protected long _timestamp(LocalDateTime value) {
                return value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            }

            @Override
            protected void _serializeAsString(Date value, JsonGenerator g, SerializerProvider provider) throws IOException {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedValue = formatter.format(value);
                g.writeString(formattedValue);
            }
        }.withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        JsonSerializer<LocalDateTime> jsonSerializer = localDateTimeSerializer.createContextual(null, null);

        LocalDateTime localDateTime = LocalDateTime.of(2022, 12, 31, 23, 59, 59);
        String serializedLocalDateTime = jsonSerializer.serializeValueAsString(localDateTime, null);
        LocalDateTime deserializedLocalDateTime = jsonSerializer.deserializeValueAs(null, String.class, null);

        assertEquals("Expected custom serialized LocalDateTime to deserialize back to original LocalDateTime", localDateTime, deserializedLocalDateTime);
        assertEquals("Expected custom serialized LocalDateTime to start with '2022-12-31 23:59:59'", serializedLocalDateTime, "2022-12-31 23:59:59");
    }

    @Test
    public void testCustomSerializationWithTimeZone() throws IOException {
        DateTimeSerializerBase<ZonedDateTime> zonedDateTimeSerializer = new DateTimeSerializerBase<ZonedDateTime>() {
            @Override
            protected long _timestamp(ZonedDateTime value) {
                return value.toInstant().toEpochMilli();
            }

            @Override
            protected void _serializeAsString(Date value, JsonGenerator g, SerializerProvider provider) throws IOException {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
                g.setTimeZone(TimeZone.getTimeZone("UTC"));
                String formattedValue = formatter.format(value);
                g.writeString(formattedValue);
            }
        }.withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"));
        JsonSerializer<ZonedDateTime> jsonSerializer = zonedDateTimeSerializer.createContextual(null, null);

        ZonedDateTime zonedDateTime = ZonedDateTime.of(2022, 12, 31, 23, 59, 59, 0, ZoneId.of("Europe/Paris"));
        String serializedZonedDateTime = jsonSerializer.serializeValueAsString(zonedDateTime, null);
        ZonedDateTime deserializedZonedDateTime = jsonSerializer.deserializeValueAs(null, String.class, null);

        assertEquals("Expected custom serialized ZonedDateTime to deserialize back to original ZonedDateTime", zonedDateTime, deserializedZonedDateTime);
        assertEquals("Expected custom serialized ZonedDateTime to start with '2022-12-31 23:59:59 +01:00'", serializedZonedDateTime, "2022-12-31 23:59:59 +01:00");
    }

    @Test(expected = JsonMappingException.class)
    public void testCustomSerializationWithInvalidFormat() throws IOException {
        DateTimeSerializerBase<LocalDate> localDateSerializer = new DateTimeSerializerBase<LocalDate>() {
            @Override
            protected void _serializeAsString(Date value, JsonGenerator g, SerializerProvider provider) throws IOException {
                g.writeString("Invalid format: dd/MM/yyyy");
            }
        }.withFormat(Boolean.FALSE, new SimpleDateFormat("dd/MM/yyyy"));
        JsonSerializer<LocalDate> jsonSerializer = localDateSerializer.createContextual(null, null);

        LocalDate localDate = LocalDate.of(2022, 12, 31);
        jsonSerializer.serializeValue(localDate, null, null);
    }
}