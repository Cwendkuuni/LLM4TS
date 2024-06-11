import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Integer intValue = 123;
        String json = mapper.writeValueAsString(intValue);
        JsonParser parser = mapper.getFactory().createParser(json);

        assertEquals(Integer.class, parser.readValueAs(Map.class).get(Integer.class.getName()).getClass());
        assertEquals(intValue, mapper.readValue(parser, Integer.class));
    }

    @Test
    public void testIntegerSerializerInvalidInput() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String json = "abcd";
        JsonParser parser = mapper.getFactory().createParser(json);

        try {
            mapper.readValue(parser, Integer.class);
            fail();
        } catch (JsonProcessingException e) {
            //Expected
        }
    }

    @Test
    public void testIntegerSerializerNullInput() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        JsonParser parser = mapper.getFactory().createParser(null);

        try {
            mapper.readValue(parser, Integer.class);
            fail();
        } catch (JsonMappingException e) {
            //Expected
        }
    }

    @Test
    public void testShortSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Short shortValue = 127;
        String json = mapper.writeValueAsString(shortValue);
        JsonParser parser = mapper.getFactory().createParser(json);

        assertEquals(Short.class, parser.readValueAs(Map.class).get(Short.class.getName()).getClass());
        assertEquals(shortValue, mapper.readValue(parser, Short.class));
    }

    @Test
    public void testShortSerializerInvalidInput() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String json = "abcd";
        JsonParser parser = mapper.getFactory().createParser(json);

        try {
            mapper.readValue(parser, Short.class);
            fail();
        } catch (JsonProcessingException e) {
            //Expected
        }
    }

    @Test
    public void testShortSerializerNullInput() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        JsonParser parser = mapper.getFactory().createParser(null);

        try {
            mapper.readValue(parser, Short.class);
            fail();
        } catch (JsonMappingException e) {
            //Expected
        }
    }

    // Add more tests for other serializers as needed

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        // Add test case for LongSerializer here
    }

    @Test
    public void testLongSerializerInvalidInput() throws JsonProcessingException {
        // Add test case for LongSerializer invalid input here
    }

    @Test
    public void testLongSerializerNullInput() throws JsonProcessingException {
        // Add test case for LongSerializer null input here
    }

    // Add more tests for other serializers as needed

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Float floatValue = 123.4f;
        String json = mapper.writeValueAsString(floatValue);
        JsonParser parser = mapper.getFactory().createParser(json);

        assertEquals(Float.class, parser.readValueAs(Map.class).get(Float.class.getName()).getClass());
        assertEquals(floatValue, mapper.readValue(parser, Float.class));
    }

    @Test
    public void testFloatSerializerInvalidInput() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String json = "abcd";
        JsonParser parser = mapper.getFactory().createParser(json);

        try {
            mapper.readValue(parser, Float.class);
            fail();
        } catch (JsonProcessingException e) {
            //Expected
        }
    }

    @Test
    public void testFloatSerializerNullInput() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        JsonParser parser = mapper.getFactory().createParser(null);

        try {
            mapper.readValue(parser, Float.class);
            fail();
        } catch (JsonMappingException e) {
            //Expected
        }
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Double doubleValue = 123.456;
        String json = mapper.writeValueAsString(doubleValue);
        JsonParser parser = mapper.getFactory().createParser(json);

        assertEquals(Double.class, parser.readValueAs(Map.class).get(Double.class.getName()).getClass());
        assertEquals(doubleValue, mapper.readValue(parser, Double.class));
    }

    @Test
    public void testDoubleSerializerInvalidInput() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String json = "abcd";
        JsonParser parser = mapper.getFactory().createParser(json);

        try {
            mapper.readValue(parser, Double.class);
            fail();
        } catch (JsonProcessingException e) {
            //Expected
        }
    }

    @Test
    public void testDoubleSerializerNullInput() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        JsonParser parser = mapper.getFactory().createParser(null);

        try {
            mapper.readValue(parser, Double.class);
            fail();
        } catch (JsonMappingException e) {
            //Expected
        }
    }
}