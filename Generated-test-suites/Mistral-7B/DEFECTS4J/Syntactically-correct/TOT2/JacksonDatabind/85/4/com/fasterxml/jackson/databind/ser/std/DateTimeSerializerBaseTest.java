import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {
    private ObjectMapper mapper;
    private DateTimeSerializerBase<Date> dateSerializer;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
    }

    @Test
    public void testSerializeNormalDate() throws JsonProcessingException, JsonMappingException, IOException {
        Date date = new Date();
        String expected = "2022-03-11T12:34:56.789Z";
        String actual = mapper.writerWithRootValueSerializer(dateSerializer)
                .writeValueAsString(date);
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeWithCustomFormat() throws JsonProcessingException, JsonMappingException, IOException {
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        DateTimeSerializerBase<Date> dateSerializerWithFormat = dateSerializer.withFormat(false, customFormat);
        Date date = new Date();
        String expected = customFormat.format(date);
        String actual = mapper.writerWithRootValueSerializer(dateSerializerWithFormat)
                .writeValueAsString(date);
        assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeWithInvalidFormat() throws JsonProcessingException, JsonMappingException, IOException {
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        DateTimeSerializerBase<Date> dateSerializerWithFormat = dateSerializer.withFormat(false, customFormat);
        Date date = new Date();
        mapper.writerWithRootValueSerializer(dateSerializerWithFormat).writeValueAsString(date);
    }

    @Test
    public void testDeserializeNormalDate() throws JsonProcessingException, JsonMappingException, IOException {
        String json = "\"2022-03-11T12:34:56.789Z\"";
        Date expected = new Date();
        Date actual = mapper.readerForUpdating(Date.class)
                .readValue(JsonFactory.instance.constructTextReader(json));
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeWithCustomFormat() throws JsonProcessingException, JsonMappingException, IOException {
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String json = customFormat.format(new Date());
        Date expected = new Date();
        Date actual = mapper.readerForUpdating(Date.class)
                .readValue(JsonFactory.instance.constructTextReader(json));
        assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithInvalidFormat() throws JsonProcessingException, JsonMappingException, IOException {
        String json = "\"2022-03-11T12:34:56\"";
        mapper.readerForUpdating(Date.class).readValue(JsonFactory.instance.constructTextReader(json));
    }
}