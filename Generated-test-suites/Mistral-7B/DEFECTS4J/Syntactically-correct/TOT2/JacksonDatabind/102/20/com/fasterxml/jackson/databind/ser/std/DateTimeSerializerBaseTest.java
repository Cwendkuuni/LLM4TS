import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDefaultSerialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Date> dateTimeSerializer = mapper.findValueSerializer(Date.class);
        JsonGenerator gen = mapper.getPolymorphicSerializerProvider().getGenerator();

        Date date = new Date();
        dateTimeSerializer.serialize(date, gen, null);
        JsonNode actualJson = mapper.readerWithZeroBigDecimalNodes().readTree(gen.getOutputBuffer().toString());
        assertEquals("Expected default date serialization to be a number", JsonNode.Value.numberValue(date.getTime()), actualJson);
    }

    @Test
    public void testSerializationWithCustomFormat() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Date> dateTimeSerializer = mapper.findValueSerializer(Date.class);
        JsonGenerator gen = mapper.getPolymorphicSerializerProvider().getGenerator();

        Date date = new Date();
        dateTimeSerializer = dateTimeSerializer.withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy"));
        dateTimeSerializer.serialize(date, gen, null);
        JsonNode actualJson = mapper.readerWithZeroBigDecimalNodes().readTree(gen.getOutputBuffer().toString());
        assertEquals("Expected custom date format serialization", "\"25-01-2023\"", actualJson.toString());
    }

    @Test
    public void testSerializationWithTimestampFlag() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Date> dateTimeSerializer = mapper.findValueSerializer(Date.class);
        JsonGenerator gen = mapper.getPolymorphicSerializerProvider().getGenerator();

        Date date = new Date();
        dateTimeSerializer = dateTimeSerializer.withFormat(Boolean.TRUE, null);
        dateTimeSerializer.serialize(date, gen, null);
        JsonNode actualJson = mapper.readerWithZeroBigDecimalNodes().readTree(gen.getOutputBuffer().toString());
        assertEquals("Expected timestamp flag to result in a number", JsonNode.Value.numberValue(date.getTime()), actualJson);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithNullFormat() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Date> dateTimeSerializer = mapper.findValueSerializer(Date.class);
        JsonGenerator gen = mapper.getPolymorphicSerializerProvider().getGenerator();

        Date date = new Date();
        dateTimeSerializer = dateTimeSerializer.withFormat(Boolean.FALSE, null);
        dateTimeSerializer.serialize(date, gen, null);
        fail("Expected JsonMappingException when format is null");
    }

    @Test
    public void testDeserializationWithDefaultFormat() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonNode = JsonNodeFactory.instance.numberValue(1672499200000L); // 31-Dec-2023 00:00:00
        DateTimeSerializerBase<Date> dateTimeSerializer = mapper.findValueDeserializer(Date.class, null);

        Date date = dateTimeSerializer.deserialize(jsonNode, null);
        assertEquals("Expected default date deserialization", new Date(1672499200000L), date);
    }

    @Test
    public void testDeserializationWithCustomFormat() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonNode = JsonNodeFactory.instance.textValue("\"25-01-2023\"");
        DateTimeSerializerBase<Date> dateTimeSerializer = mapper.findValueDeserializer(Date.class, null);

        Date date = dateTimeSerializer.deserialize(jsonNode, null);
        SimpleDateFormat expectedFormat = new SimpleDateFormat("dd-MM-yyyy");
        assertEquals("Expected custom date format deserialization", expectedFormat.parse("25-01-2023"), date);
    }

    @Test
    public void testDeserializationWithTimestampFlag() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonNode = JsonNodeFactory.instance.numberValue(1672499200000L); // 31-Dec-2023 00:00:00
        DateTimeSerializerBase<Date> dateTimeSerializer = mapper.findValueDeserializer(Date.class, JsonParser.NumberType.LONG);

        Date date = dateTimeSerializer.deserialize(jsonNode, null);
        assertEquals("Expected timestamp flag to result in a Date", new Date(1672499200000L), date);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithInvalidFormat() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonNode = JsonNodeFactory.instance.textValue("\"invalid\"");
        DateTimeSerializerBase<Date> dateTimeSerializer = mapper.findValueDeserializer(Date.class, null);

        fail("Expected JsonMappingException when parsing an invalid date format");
    }
}