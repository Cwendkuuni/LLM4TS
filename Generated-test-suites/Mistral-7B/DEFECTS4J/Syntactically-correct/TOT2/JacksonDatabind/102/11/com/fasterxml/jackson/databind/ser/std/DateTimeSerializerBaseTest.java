import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DateTimeSerializerBaseTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testSerializeDateWithDefaultFormat() throws JsonGenerationException, IOException {
        DateTimeSerializerBase<Date> serializer = mapper.findValueSerializer(Date.class);
        JsonGenerator gen = mapper.getPolymorphicSerializerProvider().findValueSerializerProvider(Date.class).getSerializerProvider().getGenerator();
        Date date = new Date();

        serializer.serialize(date, gen, mapper.getSerializerProvider());

        String serializedDate = gen.getOutputFactory().getEncodedBuffer().toString();
        String expected = "\"2022-12-12T12:12:12.123Z\"";
        assertEquals(expected, serializedDate);
    }

    @Test
    public void testSerializeDateWithCustomFormat() throws JsonGenerationException, IOException, ParseException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS z"));

        Date date = new Date();
        JsonGenerator gen = mapper.getSerializationConfig().getDefaultSerializerProvider().getGenerator();

        serializer.serialize(date, gen, mapper.getSerializerProvider());

        String serializedDate = gen.getOutputFactory().getEncodedBuffer().toString();
        String expected = "\"12-12-2022 12:12:12.123 EDT\"";
        assertEquals(expected, serializedDate);
    }

    @Test
    public void testSerializeDateWithTimestamp() throws JsonGenerationException, IOException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(Date.class, true, null);

        Date date = new Date();
        JsonGenerator gen = mapper.getSerializationConfig().getDefaultSerializerProvider().getGenerator();

        serializer.serialize(date, gen, mapper.getSerializerProvider());

        String serializedDate = gen.getOutputFactory().getEncodedBuffer().toString();
        long expected = date.getTime();
        assertEquals(expected, Long.parseLong(serializedDate));
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeNullDate() throws JsonGenerationException, IOException {
        DateTimeSerializerBase<Date> serializer = mapper.findValueSerializer(Date.class);

        serializer.serialize(null, mapper.getSerializationConfig().getDefaultSerializerProvider().getGenerator(), mapper.getSerializerProvider());
    }

    @Test
    public void testDeserializeDateWithDefaultFormat() throws IOException, JsonParseException {
        String json = "\"2022-12-12T12:12:12.123Z\"";
        Date date = mapper.readValue(json, Date.class);

        assertNotNull(date);
    }

    @Test
    public void testDeserializeDateWithCustomFormat() throws IOException, JsonParseException {
        String json = "\"12-12-2022 12:12:12.123 EDT\"";
        DateTimeSerializerBase<Date> deserializer = mapper.findValueDeserializer(Date.class);

        Date date = deserializer.deserialize(mapper.getParser(), null);

        assertNotNull(date);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNullJson() throws IOException, JsonParseException {
        String json = null;
        DateTimeSerializerBase<Date> deserializer = mapper.findValueDeserializer(Date.class);

        deserializer.deserialize(mapper.getParser(), null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJson() throws IOException, JsonParseException {
        String json = "\"invalid_json\"";
        DateTimeSerializerBase<Date> deserializer = mapper.findValueDeserializer(Date.class);

        deserializer.deserialize(mapper.getParser(), null);
    }

    @Test
    public void testDeserializeDateWithTimeZone() throws IOException, JsonParseException {
        String json = "\"12-12-2022 12:12:12.123 EDT\"";
        DateTimeSerializerBase<Date> deserializer = mapper.findValueDeserializer(Date.class);

        Date date = deserializer.deserialize(mapper.getParser(), new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS z"));

        TimeZone expected = TimeZone.getTimeZone("EDT");
        assertEquals(expected, date.getTimeZone());
    }
}