import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
        // Configure ObjectMapper for JSON serialization and parsing
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    public void testSerializerWithDefaultFormat() throws JsonProcessingException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer();
        JsonGenerator generator = mapper.getFactory()
                .createGenerator(System.out, new ObjectMapper.DefaultSerializerProvider());

        Date date = new Date();
        dateSerializer.serialize(date, generator, new SerializerProvider());
        generator.flush();

        JsonParser parser = mapper.getFactory().createParser(new StringReader(generator.toString()));
        ObjectNode jsonNode = mapper.readValue(parser, ObjectNode.class);
        assertNotNull(jsonNode);
        assertEquals(jsonNode.get("@class"), "java.util.Date");

        Date deserializedDate = mapper.readValue(jsonNode, Date.class);
        assertEquals(deserializedDate, date);
    }

    @Test
    public void testSerializerWithCustomFormat() throws JsonProcessingException, IOException, ParseException {
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer()
                .withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z") {
                    {
                        setTimeZone(TimeZone.getTimeZone("UTC"));
                        setLocale(Locale.US);
                    }
                });

        Date date = new Date();
        JsonGenerator generator = mapper.getFactory()
                .createGenerator(System.out, new ObjectMapper.DefaultSerializerProvider());
        dateSerializer.serialize(date, generator, new SerializerProvider());
        generator.flush();

        JsonParser parser = mapper.getFactory().createParser(new StringReader(generator.toString()));
        ObjectNode jsonNode = mapper.readValue(parser, ObjectNode.class);
        assertNotNull(jsonNode);
        assertEquals(jsonNode.get("@class"), "java.util.Date");

        Date deserializedDate = mapper.readValue(jsonNode, Date.class);
        assertEquals(deserializedDate, date);
    }

    @Test
    public void testSerializerWithEmptyValue() throws JsonProcessingException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer();

        JsonGenerator generator = mapper.getFactory()
                .createGenerator(System.out, new ObjectMapper.DefaultSerializerProvider());
        dateSerializer.serialize(null, generator, new SerializerProvider());
        generator.flush();

        JsonParser parser = mapper.getFactory().createParser(new StringReader(generator.toString()));
        ObjectNode jsonNode = mapper.readValue(parser, ObjectNode.class);
        assertNotNull(jsonNode);
        assertNull(jsonNode.get("@class"));
    }
}