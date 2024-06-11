import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<Long> datetimeSerializer = new DateTimeSerializerBase<Long>(Long.class, true, null);
        DateTimeSerializerBase<Long> serializedDateTime = datetimeSerializer.withFormat(false, new SimpleDateFormat("dd-MM-yyyy"));
        assertNotNull(serializedDateTime);
    }

    @Test
    public void testWithFormatNullTimestamp() {
        DateTimeSerializerBase<Long> datetimeSerializer = new DateTimeSerializerBase<Long>(Long.class, null, null);
        assertThrows(NullPointerException.class, () -> datetimeSerializer.withFormat(null, null));
    }

    @Test
    public void testWithFormatNullCustomFormat() {
        DateTimeSerializerBase<Long> datetimeSerializer = new DateTimeSerializerBase<Long>(Long.class, true, null);
        assertThrows(NullPointerException.class, () -> datetimeSerializer.withFormat(true, null));
    }

    @Test
    public void testWithFormatInvalidPattern() {
        DateTimeSerializerBase<Long> datetimeSerializer = new DateTimeSerializerBase<Long>(Long.class, true, null);
        assertThrows(IllegalArgumentException.class, () -> datetimeSerializer.withFormat(true, new SimpleDateFormat("ddMMyyyy")));
    }

    @Test
    public void testSerializeEmptyValue() throws JsonProcessingException {
        DateTimeSerializerBase<Long> datetimeSerializer = new DateTimeSerializerBase<Long>(Long.class, true, null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Long emptyValue = null;
        String jsonString = mapper.writerWithView(Annotations.class).writeValueAsString(emptyValue);
        DateTimeSerializerBase<Long> deserializedDateTime = mapper.readerFor(DateTimeSerializerBase.class).readValue(jsonString);
        assertNotNull(deserializedDateTime);
    }

    @Test
    public void testSerializeInvalidValue() throws JsonProcessingException {
        DateTimeSerializerBase<Long> datetimeSerializer = new DateTimeSerializerBase<Long>(Long.class, true, null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Long invalidValue = -1L;
        String jsonString = mapper.writerWithView(Annotations.class).writeValueAsString(invalidValue);
        assertThrows(JsonMappingException.class, () -> mapper.readerFor(DateTimeSerializerBase.class).readValue(jsonString));
    }
}