import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DateTimeSerializerBaseTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testDefaultSerialization() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> serializer = mapper.findValueSerializer(Date.class);
        JsonGenerator gen = mapper.getPolymorphicSerializerProvider().findValueSerializerProvider(Date.class).getSerializer(null);
        Date date = new Date();

        String serialized = "";
        serializer.serialize(date, gen, null);
        gen.flush();
        serialized = gen.getOutputBuffer().toString();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date deserialized = formatter.parse(serialized);

        assertEquals(date, deserialized);
    }

    @Test
    public void testCustomFormatSerialization() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> serializer = mapper.findValueSerializer(Date.class);

        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        DateTimeSerializerBase<Date> customSerializer = serializer.withFormat(Boolean.FALSE, customFormat);

        JsonGenerator gen = mapper.getPolymorphicSerializerProvider().findValueSerializerProvider(Date.class).getSerializer(null);
        Date date = new Date();

        String serialized = "";
        customSerializer.serialize(date, gen, null);
        gen.flush();
        serialized = gen.getOutputBuffer().toString();

        assertEquals("25-02-2023 14:30:45", serialized);

        SimpleDateFormat deserializer = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date deserialized = deserializer.parse(serialized);

        assertEquals(date, deserialized);
    }

    @Test
    public void testNullValueSerialization() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> serializer = mapper.findValueSerializer(Date.class);

        JsonGenerator gen = mapper.getPolymorphicSerializerProvider().findValueSerializerProvider(Date.class).getSerializer(null);

        assertNull(serializer.serialize(null, gen, null));
    }

    @Test
    public void testEmptyValueSerialization() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> serializer = mapper.findValueSerializer(Date.class);

        JsonGenerator gen = mapper.getPolymorphicSerializerProvider().findValueSerializerProvider(Date.class).getSerializer(null);

        // 09-Mar-2017, tatu: as per [databind#1550] timestamp 0 is NOT "empty"; but
        //   with versions up to 2.8.x this was the case. Fixed for 2.9.
//        Date date = new Date(0L);
//        String serialized = serializer.serialize(date, gen, null).toString();
//        assertEquals("", serialized);
        assertNull(serializer.serialize(null, gen, null));
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidFormatSerialization() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> serializer = mapper.findValueSerializer(Date.class);

        SimpleDateFormat invalidFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ssZ"); // extra 'Z'
        DateTimeSerializerBase<Date> invalidSerializer = serializer.withFormat(Boolean.FALSE, invalidFormat);

        JsonGenerator gen = mapper.getPolymorphicSerializerProvider().findValueSerializerProvider(Date.class).getSerializer(null);
        Date date = new Date();

        String serialized = "";
        invalidSerializer.serialize(date, gen, null);
        gen.flush();
        serialized = gen.getOutputBuffer().toString();

        // Jackson throws JsonMappingException when an invalid format is provided
    }

    @Test
    public void testCustomFormatDeserialization() throws JsonGenerationException, JsonMappingException, IOException, ParseException {
        ObjectMapper mapperWithCustomDeserializer = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Date.class, new DateTimeSerializerBase.StdDateSerializer());
        module.addDeserializer(Date.class, new DateTimeDeserializer());
        mapperWithCustomDeserializer.registerModule(module);

        String json = "\"25-02-2023 14:30:45\"";
        Date date = mapperWithCustomDeserializer.readValue(json, Date.class);

        assertEquals(new Date(), date);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithInvalidFormat() throws JsonGenerationException, JsonMappingException, IOException, ParseException {
        ObjectMapper mapperWithCustomDeserializer = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Date.class, new DateTimeSerializerBase.StdDateSerializer());
        module.addDeserializer(Date.class, new DateTimeDeserializer());
        mapperWithCustomDeserializer.registerModule(module);

        String json = "\"25-02-2023 14:30:45Z\""; // extra 'Z'
        Date date = mapperWithCustomDeserializer.readValue(json, Date.class);

        // Jackson throws JsonMappingException when an invalid format is provided
    }

    private class DateTimeDeserializer extends StdDeserializer<Date> {
        @Override
        public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            return formatter.parse(p.getValueAsString());
        }
    }
}