import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

    private ObjectMapper mapper;

    public DateTimeSerializerBaseTest() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    public void testSerializationWithCustomFormat() throws JsonMappingException, JsonGenerationException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a"));
        Date date = new Date();

        String json = mapper.writerWithContext(mapper.getSerializationConfig().with(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS(false))).writeValueAsString(date);
        DateTimeSerializerBase<Date> deserializer = mapper.readerFor(Date.class).withContext(mapper.getSerializationConfig().with(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS(false)));

        Date deserializedDate = deserializer.deserialize(mapper.readTree(json), null);

        assertEquals(date, deserializedDate);
    }

    @Test
    public void testSerializationWithoutCustomFormat() throws JsonMappingException, JsonGenerationException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        Date date = new Date();

        String json = mapper.writerWithContext(mapper.getSerializationConfig().with(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS(false))).writeValueAsString(date);
        DateTimeSerializerBase<Date> deserializer = mapper.readerFor(Date.class).withContext(mapper.getSerializationConfig().with(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS(false)));

        Date deserializedDate = deserializer.deserialize(mapper.readTree(json), null);

        assertEquals(date, deserializedDate);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithInvalidCustomFormat() throws JsonMappingException, JsonGenerationException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa")); // extra 'a'
        Date date = new Date();

        String json = mapper.writerWithContext(mapper.getSerializationConfig().with(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS(false))).writeValueAsString(date);
    }
}