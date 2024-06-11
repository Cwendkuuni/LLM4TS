import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Integer input = 123;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(input);
        JsonParser parser = mapper.getFactory().createParser(json);
        JsonNode node = parser.readValueAsTree();
        assertEquals("123", node.asText());
        assertEquals(input.getClass(), mapper.readValue(node.asText(), Map.class).get(0).getClass());
    }

    @Test
    public void testIntegerSerializerNegative() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Integer input = -123;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(input);
        JsonParser parser = mapper.getFactory().createParser(json);
        JsonNode node = parser.readValueAsTree();
        assertEquals("-123", node.asText());
        assertEquals(input.getClass(), mapper.readValue(node.asText(), Map.class).get(0).getClass());
    }

    @Test
    public void testIntegerSerializerNull() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Integer input = null;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(input);
        JsonParser parser = mapper.getFactory().createParser(json);
        JsonNode node = parser.readValueAsTree();
        assertNull(mapper.readValue(node.asText(), Map.class).get(0));
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Long input = 123L;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(input);
        JsonParser parser = mapper.getFactory().createParser(json);
        JsonNode node = parser.readValueAsTree();
        assertEquals("123", node.asText());
        assertEquals(input.getClass(), mapper.readValue(node.asText(), Map.class).get(0).getClass());
    }

    @Test
    public void testLongSerializerNegative() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Long input = -123L;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(input);
        JsonParser parser = mapper.getFactory().createParser(json);
        JsonNode node = parser.readValueAsTree();
        assertEquals("-123", node.asText());
        assertEquals(input.getClass(), mapper.readValue(node.asText(), Map.class).get(0).getClass());
    }

    @Test
    public void testLongSerializerNull() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Long input = null;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(input);
        JsonParser parser = mapper.getFactory().createParser(json);
        JsonNode node = parser.readValueAsTree();
        assertNull(mapper.readValue(node.asText(), Map.class).get(0));
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Float input = 123.45f;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(input);
        JsonParser parser = mapper.getFactory().createParser(json);
        JsonNode node = parser.readValueAsTree();
        assertEquals("123.45", node.asText());
        assertEquals(Float.class, mapper.readValue(node.asText(), Map.class).get(0).getClass());
    }

    @Test
    public void testFloatSerializerNaN() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Float input = Float.NaN;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(input);
        JsonParser parser = mapper.getFactory().createParser(json);
        JsonNode node = parser.readValueAsTree();
        assertEquals("NaN", node.asText());
        assertEquals(Float.class, mapper.readValue(node.asText(), Map.class).get(0).getClass());
    }

    @Test
    public void testFloatSerializerInfinity() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Float input = Float.POSITIVE_INFINITY;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(input);
        JsonParser parser = mapper.getFactory().createParser(json);
        JsonNode node = parser.readValueAsTree();
        assertEquals("Infinity", node.asText());
        assertEquals(Float.class, mapper.readValue(node.asText(), Map.class).get(0).getClass());
    }

    @Test
    public void testFloatSerializerNegativeInfinity() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Float input = Float.NEGATIVE_INFINITY;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(input);
        JsonParser parser = mapper.getFactory().createParser(json);
        JsonNode node = parser.readValueAsTree();
        assertEquals("-Infinity", node.asText());
        assertEquals(Float.class, mapper.readValue(node.asText(), Map.class).get(0).getClass());
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Double input = 123.45;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(input);
        JsonParser parser = mapper.getFactory().createParser(json);
        JsonNode node = parser.readValueAsTree();
        assertEquals("123.45", node.asText());
        assertEquals(Double.class, mapper.readValue(node.asText(), Map.class).get(0).getClass());
    }

    @Test
    public void testDoubleSerializerNaN() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Double input = Double.NaN;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(input);
        JsonParser parser = mapper.getFactory().createParser(json);
        JsonNode node = parser.readValueAsTree();
        assertEquals("NaN", node.asText());
        assertEquals(Double.class, mapper.readValue(node.asText(), Map.class).get(0).getClass());
    }

    @Test
    public void testDoubleSerializerInfinity() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Double input = Double.POSITIVE_INFINITY;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(input);
        JsonParser parser = mapper.getFactory().createParser(json);
        JsonNode node = parser.readValueAsTree();
        assertEquals("Infinity", node.asText());
        assertEquals(Double.class, mapper.readValue(node.asText(), Map.class).get(0).getClass());
    }

    @Test
    public void testDoubleSerializerNegativeInfinity() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Double input = Double.NEGATIVE_INFINITY;
        String json = mapper.writerWithTypeValidator(false).writeValueAsString(input);
        JsonParser parser = mapper.getFactory().createParser(json);
        JsonNode node = parser.readValueAsTree();
        assertEquals("-Infinity", node.asText());
        assertEquals(Double.class, mapper.readValue(node.asText(), Map.class).get(0).getClass());
    }
}