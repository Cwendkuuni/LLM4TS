import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, false, null);

        DateTimeSerializerBase<Long> timestampSerializer = serializer.withFormat(Boolean.TRUE, null);
        DateTimeSerializerBase<Long> customFormatSerializer = serializer.withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));

        Long timestamp = 1633022400000L; // Unix timestamp for 01-Jan-2021 00:00:00
        Long customDate = -1492095600000L; // Unix timestamp for 01-Nov-1999 00:00:00

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);

        try {
            timestampSerializer.serialize(timestamp, gen, null);
            gen.flush(); // Verify serialization as timestamp

            gen.reset();
            customFormatSerializer.serialize(customDate, gen, null);
            gen.flush(); // Verify serialization with custom format

            // Add assertions for the deserialization and other scenarios as needed
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException occurred during testing");
        }
    }
}