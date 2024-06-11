import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Date date = new Date();
        String expectedJson = "\"\\" + date.toISO8601String() + "\"";

        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out);
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(date, jgen, mapper.getSerializationConfig());
        jgen.flush();

        String actualJson = jgen.toString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String value = "test string";
        String expectedJson = "\"test string\"";

        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out);
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(value, jgen, mapper.getSerializationConfig());
        jgen.flush();

        String actualJson = jgen.toString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeNull() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String expectedJson = "null";

        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out);
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(null, jgen, mapper.getSerializationConfig());
        jgen.flush();

        String actualJson = jgen.toString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeObject() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Object value = new HashMap<>();
        String expectedJson = "{\"class\":\"java.util.HashMap\",\"size\":0}";

        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out);
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(value, jgen, mapper.getSerializationConfig());
        jgen.flush();

        String actualJson = jgen.toString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testGetSchema() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        JsonNode expectedSchema = mapper.readTree("{\"type\":\"string\"}");
        JsonNode actualSchema = serializer.getSchema(mapper.getSerializationConfig(), Object.class);

        assertEquals(expectedSchema.toString(), actualSchema.toString());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        JsonFactory factory = mapper.getFactory();
        JsonNode expectedSchema = factory.createObjectNode();
        expectedSchema.put("format", "string");

        JsonNode actualSchema = serializer.acceptJsonFormatVisitor(factory.getJsonFormatVisitorWrapper(null), Object.class);

        assertEquals(expectedSchema.toString(), actualSchema.toString());
    }
}