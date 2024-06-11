import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSerializerWithDefaultSettings() throws IOException, JsonProcessingException {
        DateTimeSerializerBase<Long> dateTimeSerializer = objectMapper.findSerializer(Long.class);
        JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializerProvider().getGenerator();

        Long date = 1652451200000L; // Mon Dec 27 00:00:00 UTC 2021
        Long expectedJson = 1652451200000L;

        dateTimeSerializer.serialize(date, jsonGenerator, null);
        JsonNode actualJson = jsonGenerator.getOutputFactory().writeValueAsTree(jsonGenerator.getOutputBuffer());

        assertEquals("Expected numeric timestamp for default serializer", expectedJson.asLong(), actualJson.asLong());
    }

    @Test
    public void testSerializerWithCustomDateFormat() throws IOException, JsonProcessingException, ParseException {
        DateTimeSerializerBase<String> dateTimeSerializer = new DateTimeSerializerBase<String>(
                String.class, false, new SimpleDateFormat("dd-MMM-yyyy")) {
            @Override
            protected long _timestamp(String value) {
                try {
                    return value.toInstant().toEpochMilli();
                } catch (DateTimeException e) {
                    throw new JsonMappingException("Invalid date format: " + value);
                }
            }
        };

        String date = "27-Dec-2021";
        String expectedJson = "\"27-Dec-2021\"";

        JsonSerializer<String> jsonSerializer = objectMapper.findValueSerializer(String.class);
        jsonSerializer = objectMapper.getPolymorphicSerializerProvider().findValueSerializer(JsonValueType.STRING, String.class);

        JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializerProvider().getGenerator();
        jsonSerializer.serialize(date, jsonGenerator, null);
        jsonGenerator.flush();
        String actualJson = jsonGenerator.getOutputFactory().writeValueAsString(jsonGenerator.getOutputBuffer());

        assertEquals("Expected JSON string for custom serializer", expectedJson, actualJson);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializerWithInvalidCustomDateFormat() throws IOException, JsonProcessingException {
        DateTimeSerializerBase<String> dateTimeSerializer = new DateTimeSerializerBase<String>(
                String.class, false, new SimpleDateFormat("dd-MMM-yyyy-zzz")) {
            @Override
            protected long _timestamp(String value) {
                throw new UnsupportedOperationException("Not implemented");
            }
        };

        String date = "27-Dec-2021";

        JsonSerializer<String> jsonSerializer = objectMapper.findValueSerializer(String.class);
        jsonSerializer = objectMapper.getPolymorphicSerializerProvider().findValueSerializer(JsonValueType.STRING, String.class);

        JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializerProvider().getGenerator();
        jsonSerializer.serialize(date, jsonGenerator, null);

        // The test should fail with JsonMappingException as the custom serializer is not able to handle the provided date format
    }

    @Test
    public void testDeserializerWithDefaultSettings() throws IOException, JsonProcessingException {
        DateTimeSerializerBase<Long> dateTimeSerializer = objectMapper.findSerializer(Long.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("1652451200000");

        Long expectedValue = 1652451200000L;
        Long actualValue = dateTimeSerializer.deserialize(jsonParser, null);

        assertEquals("Expected numeric timestamp for default deserializer", expectedValue, actualValue);
    }

    @Test
    public void testDeserializerWithCustomDateFormat() throws IOException, JsonProcessingException, ParseException {
        DateTimeSerializerBase<String> dateTimeSerializer = new DateTimeSerializerBase<String>(
                String.class, false, new SimpleDateFormat("dd-MMM-yyyy")) {
            @Override
            protected long _timestamp(String value) {
                try {
                    return value.toInstant().toEpochMilli();
                } catch (DateTimeException e) {
                    throw new JsonMappingException("Invalid date format: " + value);
                }
            }

            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                if (timestamp != null && customFormat == null) {
                    throw new IllegalArgumentException("Custom format cannot be null when deserializing as numeric timestamp");
                }
                super.withFormat(timestamp, customFormat);
                return this;
            }
        };

        String jsonString = "\"27-Dec-2021\"";
        String expectedValue = "27-Dec-2021";

        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
        JsonNode actualJson = objectMapper.readValue(jsonParser, JsonNode.class);
        String actualValue = actualJson.textValue();

        assertEquals("Expected JSON string for custom deserializer", expectedValue, actualValue);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializerWithInvalidCustomDateFormat() throws IOException, JsonProcessingException {
        DateTimeSerializerBase<String> dateTimeSerializer = new DateTimeSerializerBase<String>(
                String.class, false, new SimpleDateFormat("dd-MMM-yyyy-zzz")) {
            @Override
            protected long _timestamp(String value) {
                throw new UnsupportedOperationException("Not implemented");
            }
        };

        String jsonString = "\"27-Dec-2021\"";

        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
        JsonNode actualJson = objectMapper.readTree(jsonParser);

        // The test should fail with JsonMappingException as the custom deserializer is not able to handle the provided date format
    }

    @Test
    public void testSerializerWithTimeZone() throws IOException, JsonProcessingException {
        DateTimeSerializerBase<Long> dateTimeSerializer = new DateTimeSerializerBase<Long>(
                Long.class, true, null) {
            @Override
            protected long _timestamp(Long value) {
                return value;
            }

            @Override
            public DateTimeSerializerBase<Long> withFormat(Boolean timestamp, DateFormat customFormat) {
                if (customFormat != null) {
                    throw new UnsupportedOperationException("Cannot provide custom date format when serializing as timestamp");
                }
                super.withFormat(timestamp, customFormat);
                return this;
            }
        };

        Long date = 1652451200000L; // Mon Dec 27 00:00:00 UTC 2021
        Long expectedJson = 1652451200000L;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a z");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializerProvider().getGenerator();
        jsonGenerator.setDateFormat(dateFormat);

        dateTimeSerializer.serialize(date, jsonGenerator, null);
        JsonNode actualJson = jsonGenerator.getOutputFactory().writeValueAsTree(jsonGenerator.getOutputBuffer());

        assertEquals("Expected numeric timestamp for serializer with timezone", expectedJson.asLong(), actualJson.asLong());
    }
}