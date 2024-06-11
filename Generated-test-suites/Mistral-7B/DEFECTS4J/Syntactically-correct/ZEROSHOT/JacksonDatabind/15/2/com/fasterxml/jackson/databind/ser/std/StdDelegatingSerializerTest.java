import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.Converter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;

public class StdDelegatingSerializerTest {

    private ObjectMapper mapper;
    private StdDelegatingSerializer<String> stringSerializer;
    private StdDelegatingSerializer<Integer> intSerializer;
    private StdDelegatingSerializer<Double> doubleSerializer;

    @BeforeMethod
    public void setUp() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        Converter<Object, String> stringConverter = new StringConverter();
        Converter<Object, Integer> intConverter = new IntConverter();
        Converter<Object, Double> doubleConverter = new DoubleConverter();

        stringSerializer = new StdDelegatingSerializer<String>(stringConverter);
        intSerializer = new StdDelegatingSerializer<Integer>(intConverter, Integer.TYPE);
        doubleSerializer = new StdDelegatingSerializer<Double>(doubleConverter);
    }

    @Test
    public void testSerializationString() throws JsonGenerationException, IOException {
        String input = "test";
        String expectedOutput = "\"test\"";

        JsonSerializer<String> stringDelegateSerializer = mapper.findValueSerializer(String.class);

        Object output = stringSerializer.serialize(input, null, null);
        assertEquals(output.toString(), expectedOutput);
        assertSame(stringSerializer.getDelegatee(), stringDelegateSerializer);
    }

    @Test
    public void testSerializationInt() throws JsonGenerationException, IOException {
        Integer input = 1;
        int expectedOutput = 1;

        JsonSerializer<Integer> intDelegateSerializer = mapper.findValueSerializer(Integer.class);

        Object output = intSerializer.serialize(input, null, null);
        assertEquals(output.intValue(), expectedOutput);
        assertSame(intSerializer.getDelegatee(), intDelegateSerializer);
    }

    @Test
    public void testSerializationDouble() throws JsonGenerationException, IOException {
        Double input = 1.0;
        double expectedOutput = 1.0;

        JsonSerializer<Double> doubleDelegateSerializer = mapper.findValueSerializer(Double.class);

        Object output = doubleSerializer.serialize(input, null, null);
        assertEquals(output.doubleValue(), expectedOutput);
        assertSame(doubleSerializer.getDelegatee(), doubleDelegateSerializer);
    }

    @Test
    public void testSerializationNull() throws JsonGenerationException, IOException {
        Object input = null;

        JsonSerializer<Object> nullDelegateSerializer = mapper.findValueSerializer(Object.class);

        Object output = stringSerializer.serialize(input, null, null);
        assertNull(output);
        assertSame(stringSerializer.getDelegatee(), nullDelegateSerializer);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        JsonNode expectedStringSchema = mapper.readTree("{\"type\":\"string\"}");
        JsonNode expectedIntSchema = mapper.readTree("{\"type\":\"number\"}");
        JsonNode expectedDoubleSchema = mapper.readTree("{\"type\":\"number\",\"numberFormat\":\"exponential\"}");

        JsonNode stringSchema = stringSerializer.getSchema(mapper.getSerializerProvider(), String.class);
        JsonNode intSchema = intSerializer.getSchema(mapper.getSerializerProvider(), Integer.TYPE);
        JsonNode doubleSchema = doubleSerializer.getSchema(mapper.getSerializerProvider(), Double.TYPE);

        assertEquals(stringSchema, expectedStringSchema);
        assertEquals(intSchema, expectedIntSchema);
        assertEquals(doubleSchema, expectedDoubleSchema);
    }

    @Test
    public void testIsEmpty() throws JsonMappingException {
        ObjectMapper emptyMapper = new ObjectMapper();
        emptyMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);

        assertFalse(stringSerializer.isEmpty(null, new Object()));
        assertFalse(intSerializer.isEmpty(null, new Object()));
        assertFalse(doubleSerializer.isEmpty(null, new Object()));

        assertTrue(stringSerializer.isEmpty(emptyMapper, null));
        assertTrue(intSerializer.isEmpty(emptyMapper, null));
        assertTrue(doubleSerializer.isEmpty(emptyMapper, null));
    }
}

class StringConverter implements Converter<Object, String> {
    @Override
    public String convert(Object value) {
        return value.toString();
    }

    @Override
    public Type getInputType(Type typeHint) {
        return Object.class;
    }

    @Override
    public Type getOutputType(Type typeHint) {
        return String.class;
    }
}

class IntConverter implements Converter<Object, Integer> {
    @Override
    public Integer convert(Object value) {
        return ((Number) value).intValue();
    }

    @Override
    public Type getInputType(Type typeHint) {
        return Number.class;
    }

    @Override
    public Type getOutputType(Type typeHint) {
        return Integer.class;
    }
}

class DoubleConverter implements Converter<Object, Double> {
    @Override
    public Double convert(Object value) {
        return ((Number) value).doubleValue();
    }

    @Override
    public Type getInputType(Type typeHint) {
        return Number.class;
    }

    @Override
    public Type getOutputType(Type typeHint) {
        return Double.class;
    }
}