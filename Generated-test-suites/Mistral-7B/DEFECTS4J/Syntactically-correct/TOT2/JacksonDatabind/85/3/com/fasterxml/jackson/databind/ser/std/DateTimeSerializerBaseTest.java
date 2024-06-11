import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class DateTimeSerializerBaseTest {

    @Test
    public void testBasicUsage() throws JsonProcessingException {
        DateTimeSerializerBase<java.util.Date> serializer = new StdDateSerializer();
        ObjectMapper mapper = new ObjectMapper();
        java.util.Date date = new java.util.Date();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        String serializedDate = mapper.writeValueAsString(date, serializer);
        java.util.Date deserializedDate = mapper.readValue(serializedDate, java.util.Date.class);
        assertEquals(date, deserializedDate);
    }

    @Test
    public void testBasicUsageWithCustomFormat() throws JsonProcessingException {
        DateTimeSerializerBase<java.util.Date> serializer = new StdDateFormatSerializer(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        ObjectMapper mapper = new ObjectMapper();
        java.util.Date date = new java.util.Date();
        String serializedDate = mapper.writeValueAsString(date, serializer);
        java.util.Date deserializedDate = mapper.readValue(serializedDate, java.util.Date.class);
        assertEquals(date, deserializedDate);
    }

    @Test
    public void testNullDate() {
        DateTimeSerializerBase<java.util.Date> serializer = new StdDateSerializer();
        ObjectMapper mapper = new ObjectMapper();
        assertNull(mapper.readValue(mapper.writeValueAsString(null), java.util.Date.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidCustomFormat() throws JsonProcessingException {
        DateTimeSerializerBase<java.util.Date> serializer = new StdDateFormatSerializer(new SimpleDateFormat("X"));
        ObjectMapper mapper = new ObjectMapper();
        java.util.Date date = new java.util.Date();
        String serializedDate = mapper.writeValueAsString(date, serializer);
        mapper.readValue(serializedDate, java.util.Date.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testNullCustomFormat() throws JsonProcessingException {
        DateTimeSerializerBase<java.util.Date> serializer = new StdDateFormatSerializer(null);
        ObjectMapper mapper = new ObjectMapper();
        java.util.Date date = new java.util.Date();
        String serializedDate = mapper.writeValueAsString(date, serializer);
        mapper.readValue(serializedDate, java.util.Date.class);
    }

    // Add more test cases for other edge cases and exceptions here
}