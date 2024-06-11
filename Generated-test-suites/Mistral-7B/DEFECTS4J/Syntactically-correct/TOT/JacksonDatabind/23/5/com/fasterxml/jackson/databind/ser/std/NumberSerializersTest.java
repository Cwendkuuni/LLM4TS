import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Integer intValue = 123;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(intValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(intValue, mapper.readValue(parser, Integer.class));
    }

    @Test
    public void testIntegerSerializerNegative() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Integer intValue = -123;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(intValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(intValue, mapper.readValue(parser, Integer.class));
    }

    @Test
    public void testIntegerSerializerMaxValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Integer intValue = Integer.MAX_VALUE;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(intValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(intValue, mapper.readValue(parser, Integer.class));
    }

    @Test
    public void testIntegerSerializerMinValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Integer intValue = Integer.MIN_VALUE;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(intValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(intValue, mapper.readValue(parser, Integer.class));
    }

    @Test(expected = JsonProcessingException.class)
    public void testIntegerSerializerNaN() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Float floatValue = Float.NaN;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(floatValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        mapper.readValue(parser, Integer.class);
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Byte byteValue = 127;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(byteValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(byteValue, mapper.readValue(parser, Byte.class));
    }

    @Test
    public void testIntLikeSerializerNegative() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Byte byteValue = -127;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(byteValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(byteValue, mapper.readValue(parser, Byte.class));
    }

    @Test
    public void testIntLikeSerializerMaxValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Byte byteValue = Byte.MAX_VALUE;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(byteValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(byteValue, mapper.readValue(parser, Byte.class));
    }

    @Test
    public void testIntLikeSerializerMinValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Byte byteValue = Byte.MIN_VALUE;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(byteValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(byteValue, mapper.readValue(parser, Byte.class));
    }

    @Test(expected = JsonProcessingException.class)
    public void testIntLikeSerializerNaN() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Float floatValue = Float.NaN;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(floatValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        mapper.readValue(parser, Byte.class);
    }

    @Test
    public void testShortSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Short shortValue = 123;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(shortValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(shortValue, mapper.readValue(parser, Short.class));
    }

    @Test
    public void testShortSerializerNegative() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Short shortValue = -123;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(shortValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(shortValue, mapper.readValue(parser, Short.class));
    }

    @Test
    public void testShortSerializerMaxValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Short shortValue = Short.MAX_VALUE;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(shortValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(shortValue, mapper.readValue(parser, Short.class));
    }

    @Test
    public void testShortSerializerMinValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Short shortValue = Short.MIN_VALUE;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(shortValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(shortValue, mapper.readValue(parser, Short.class));
    }

    @Test(expected = JsonProcessingException.class)
    public void testShortSerializerNaN() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Float floatValue = Float.NaN;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(floatValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        mapper.readValue(parser, Short.class);
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Long longValue = 12345678912345L;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(longValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(longValue, mapper.readValue(parser, Long.class));
    }

    @Test
    public void testLongSerializerNegative() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Long longValue = -12345678912345L;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(longValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(longValue, mapper.readValue(parser, Long.class));
    }

    @Test
    public void testLongSerializerMaxValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Long longValue = Long.MAX_VALUE;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(longValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(longValue, mapper.readValue(parser, Long.class));
    }

    @Test
    public void testLongSerializerMinValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Long longValue = Long.MIN_VALUE;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(longValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(longValue, mapper.readValue(parser, Long.class));
    }

    @Test(expected = JsonProcessingException.class)
    public void testLongSerializerNaN() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Float floatValue = Float.NaN;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(floatValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        mapper.readValue(parser, Long.class);
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Float floatValue = 123.45f;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(floatValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(floatValue, mapper.readValue(parser, Float.class));
    }

    @Test
    public void testFloatSerializerNegative() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Float floatValue = -123.45f;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(floatValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(floatValue, mapper.readValue(parser, Float.class));
    }

    @Test
    public void testFloatSerializerMaxValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Float floatValue = Float.MAX_VALUE;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(floatValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(floatValue, mapper.readValue(parser, Float.class));
    }

    @Test
    public void testFloatSerializerMinValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Float floatValue = Float.MIN_VALUE;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(floatValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(floatValue, mapper.readValue(parser, Float.class));
    }

    @Test(expected = JsonProcessingException.class)
    public void testFloatSerializerNaN() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Float floatValue = Float.NaN;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(floatValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        mapper.readValue(parser, Float.class);
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Double doubleValue = 123.45;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(doubleValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(doubleValue, mapper.readValue(parser, Double.class));
    }

    @Test
    public void testDoubleSerializerNegative() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Double doubleValue = -123.45;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(doubleValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(doubleValue, mapper.readValue(parser, Double.class));
    }

    @Test
    public void testDoubleSerializerMaxValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Double doubleValue = Double.MAX_VALUE;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(doubleValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(doubleValue, mapper.readValue(parser, Double.class));
    }

    @Test
    public void testDoubleSerializerMinValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Double doubleValue = Double.MIN_VALUE;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(doubleValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(doubleValue, mapper.readValue(parser, Double.class));
    }

    @Test(expected = JsonProcessingException.class)
    public void testDoubleSerializerNaN() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Float floatValue = Float.NaN;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(floatValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        mapper.readValue(parser, Double.class);
    }
}