import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DateTimeSerializerBaseTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDefaultSerialization() throws JsonMappingException, JsonGenerationException, IOException {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer();
        Date date = new Date();

        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(date);
        assertNotNull(json);
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        assertEquals("\"2023-03-15T14:30:45.123Z\"", node.getTextValue(""));

        SerializerProvider provider = mapper.getSerializerProvider();
        Date deserializedDate = serializer.createContextual(provider, null).serializeValues(provider, date, null);
        assertEquals(date, deserializedDate);
    }

    @Test
    public void testCustomFormatSerialization() throws JsonMappingException, JsonGenerationException, IOException, ParseException {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer() {
            @Override
            protected DateFormat _customFormat;

            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat format) {
                _customFormat = format;
                return this;
            }

            @Override
            protected void _serializeAsString(Date value, JsonGenerator g, SerializerProvider provider) throws IOException {
                g.writeString(_customFormat.format(value));
            }
        };
        Date date = new Date();

        _customFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(date);
        assertNotNull(json);
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        assertEquals("\"15/03/2023 14:30:45\"", node.getTextValue(""));

        SerializerProvider provider = mapper.getSerializerProvider();
        Date deserializedDate = serializer.createContextual(provider, null).serializeValues(provider, date, null);
        assertEquals(date, deserializedDate);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidCustomFormatSerialization() throws JsonMappingException, JsonGenerationException, IOException {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer() {
            @Override
            protected DateFormat _customFormat;

            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat format) {
                _customFormat = format;
                return this;
            }

            @Override
            protected void _serializeAsString(Date value, JsonGenerator g, SerializerProvider provider) throws IOException {
                g.writeString(_customFormat.format(value));
            }
        };
        Date date = new Date();

        _customFormat = new SimpleDateFormat("xx/xx/xxxx xx:xx:xxxx");
        mapper.writerWithDefaultPrettyPrinter().writeValueAsString(date);
    }

    @Test
    public void testLocaleSerialization() throws JsonMappingException, JsonGenerationException, IOException {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer();
        Date date = new Date();

        String jsonFR = mapper.writerForLanguageAndLocale("fr_FR")
                .writeValueAsString(date);
        String jsonEN = mapper.writerForLanguageAndLocale("en_EN")
                .writeValueAsString(date);

        ObjectNode nodeFR = mapper.readValue(jsonFR, ObjectNode.class);
        ObjectNode nodeEN = mapper.readValue(jsonEN, ObjectNode.class);

        assertEquals("\"15-mars-2023 14:30:45\"", nodeFR.getTextValue(""));
        assertEquals("\"2023-03-15T14:30:45.123Z\"", nodeEN.getTextValue(""));

        SerializerProvider provider = mapper.getSerializerProvider();
        Date deserializedDateFR = serializer.createContextual(provider, null).serializeValues(provider, date, null);
        assertEquals(date, deserializedDateFR);

        Date deserializedDateEN = serializer.createContextual(provider, null).serializeValues(provider, date, null);
        assertEquals(date, deserializedDateEN);
    }

    @Test
    public void testTimeZoneSerialization() throws JsonMappingException, JsonGenerationException, IOException {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer();
        Date date = new Date();

        String jsonUTC = mapper.writerWithTimeZone("UTC")
                .writeValueAsString(date);
        String jsonEurope = mapper.writerWithTimeZone("Europe/London")
                .writeValueAsString(date);

        ObjectNode nodeUTC = mapper.readValue(jsonUTC, ObjectNode.class);
        ObjectNode nodeEurope = mapper.readValue(jsonEurope, ObjectNode.class);

        assertEquals("\"2023-03-15T14:30:45.123Z\"", nodeUTC.getTextValue(""));
        assertEquals("\"2023-03-15T13:30:45.123+01:00\"", nodeEurope.getTextValue(""));

        SerializerProvider provider = mapper.getSerializerProvider();
        Date deserializedDateUTC = serializer.createContextual(provider, null).serializeValues(provider, date, null);
        assertEquals(date, deserializedDateUTC);

        Date deserializedDateEurope = serializer.createContextual(provider, null).serializeValues(provider, date, null);
        assertEquals(date, deserializedDateEurope);
    }
}