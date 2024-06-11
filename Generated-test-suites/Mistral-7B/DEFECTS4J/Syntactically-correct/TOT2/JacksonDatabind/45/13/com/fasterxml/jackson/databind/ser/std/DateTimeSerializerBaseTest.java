import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        DateTimeSerializerBase<LocalDateTime> serializer = new LocalDateTimeSerializer();

        // Test with no arguments
        DateTimeSerializerBase<LocalDateTime> serializedSerializer1 = serializer.withFormat(null, null);
        assertEquals(serializer, serializedSerializer1);

        // Test with timestamp = true and null customFormat
        DateTimeSerializerBase<LocalDateTime> serializedSerializer2 = serializer.withFormat(true, null);
        assertEquals(serializer.getClass(), serializedSerializer2.getClass());
        assertEquals(true, serializedSerializer2._useTimestamp);
        assertEquals(null, serializedSerializer2._customFormat);

        // Test with timestamp = false and customFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        SimpleDateFormat customFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DateTimeSerializerBase<LocalDateTime> serializedSerializer3 = serializer.withFormat(false, customFormat);
        assertEquals(serializer.getClass(), serializedSerializer3.getClass());
        assertEquals(false, serializedSerializer3._useTimestamp);
        assertEquals(customFormat, serializedSerializer3._customFormat);
    }
}