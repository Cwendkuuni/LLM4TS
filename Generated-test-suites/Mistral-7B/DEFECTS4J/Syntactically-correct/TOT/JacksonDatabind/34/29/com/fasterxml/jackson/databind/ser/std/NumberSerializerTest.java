import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Currency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NumberSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSerializeInt() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Integer.class);
        JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();
        numberSerializer.serialize(1, jsonGenerator, mapper.getSerializerProvider());
        assertEquals("1", jsonGenerator.getOutputBuffer().toString());
        jsonGenerator.close();
    }

    @Test
    public void testSerializeLong() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Long.class);
        JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();
        numberSerializer.serialize(1L, jsonGenerator, mapper.getSerializerProvider());
        assertEquals("1", jsonGenerator.getOutputBuffer().toString());
        jsonGenerator.close();
    }

    @Test
    public void testSerializeFloat() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Float.class);
        JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();
        numberSerializer.serialize(1.0f, jsonGenerator, mapper.getSerializerProvider());
        assertEquals("1.0", jsonGenerator.getOutputBuffer().toString());
        jsonGenerator.close();
    }

    @Test
    public void testSerializeDouble() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Double.class);
        JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();
        numberSerializer.serialize(1.0, jsonGenerator, mapper.getSerializerProvider());
        assertEquals("1.0", jsonGenerator.getOutputBuffer().toString());
        jsonGenerator.close();
    }

    @Test
    public void testSerializeBigDecimal() throws JsonGenerationException, JsonMappingException, IOException {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();
        numberSerializer.serialize(new BigDecimal("123.45"), jsonGenerator, mapper.getSerializerProvider());
        assertEquals("123.45", jsonGenerator.getOutputBuffer().toString());
        jsonGenerator.close();
    }

    @Test
    public void testSerializeBigInteger() throws JsonGenerationException, JsonMappingException, IOException {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
        JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();
        numberSerializer.serialize(new BigInteger("12345678901234567890"), jsonGenerator, mapper.getSerializerProvider());
        assertEquals("12345678901234567890", jsonGenerator.getOutputBuffer().toString());
        jsonGenerator.close();
    }

    @Test
    public void testSerializeCurrency() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Currency.class);
        JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();
        numberSerializer.serialize(Currency.getInstance("USD"), jsonGenerator, mapper.getSerializerProvider());
        assertEquals("{\"type\":\"currency\",\"currencyCode\":\"USD\"}", jsonGenerator.getOutputBuffer().toString());
        jsonGenerator.close();
    }

    @Test
    public void testSerializeUnknownNumberType() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Byte.class);
        JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();
        numberSerializer.serialize((byte) 1, jsonGenerator, mapper.getSerializerProvider());
        assertEquals("1", jsonGenerator.getOutputBuffer().toString());
        jsonGenerator.close();
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeNull() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();
        numberSerializer.serialize(null, jsonGenerator, mapper.getSerializerProvider());
        assertNull(jsonGenerator.getOutputBuffer().toString());
        jsonGenerator.close();
    }

    @Test
    public void testDeserializeInt() throws JsonGenerationException, JsonMappingException, IOException {
        String jsonString = "1";
        Number deserializedNumber = mapper.readValue(jsonString, Number.class);
        assertEquals(1, deserializedNumber.intValue());
    }

    @Test
    public void testDeserializeLong() throws JsonGenerationException, JsonMappingException, IOException {
        String jsonString = "1";
        Number deserializedNumber = mapper.readValue(jsonString, Number.class);
        assertEquals(1L, deserializedNumber.longValue());
    }

    @Test
    public void testDeserializeFloat() throws JsonGenerationException, JsonMappingException, IOException {
        String jsonString = "1.0";
        Number deserializedNumber = mapper.readValue(jsonString, Number.class);
        assertEquals(1.0f, deserializedNumber.floatValue(), 0.01f);
    }

    @Test
    public void testDeserializeDouble() throws JsonGenerationException, JsonMappingException, IOException {
        String jsonString = "1.0";
        Number deserializedNumber = mapper.readValue(jsonString, Number.class);
        assertEquals(1.0, deserializedNumber.doubleValue(), 0.01);
    }

    @Test
    public void testDeserializeBigDecimal() throws JsonGenerationException, JsonMappingException, IOException {
        String jsonString = "123.45";
        Number deserializedNumber = mapper.readValue(jsonString, Number.class);
        assertEquals(new BigDecimal("123.45"), deserializedNumber);
    }

    @Test
    public void testDeserializeBigInteger() throws JsonGenerationException, JsonMappingException, IOException {
        String jsonString = "12345678901234567890";
        Number deserializedNumber = mapper.readValue(jsonString, Number.class);
        assertEquals(new BigInteger("12345678901234567890"), deserializedNumber);
    }

    @Test
    public void testDeserializeCurrency() throws JsonGenerationException, JsonMappingException, IOException {
        String jsonString = "{\"type\":\"currency\",\"currencyCode\":\"USD\"}";
        Number deserializedNumber = mapper.readValue(jsonString, Number.class);
        assertEquals(Currency.getInstance("USD"), deserializedNumber);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidString() throws JsonGenerationException, JsonMappingException, IOException {
        String jsonString = "abc";
        mapper.readValue(jsonString, Number.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnknownNumberType() throws JsonGenerationException, JsonMappingException, IOException {
        String jsonString = "1";
        mapper.readValue(jsonString, BigDecimal.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidFormat() throws JsonGenerationException, JsonMappingException, IOException {
        String jsonString = "{invalid json}";
        mapper.readValue(jsonString, Number.class);
    }
}