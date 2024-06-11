import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testIntegerSerializer() throws JsonMappingException, IOException {
        JsonParser parser = mapper.getFactory().createParser("123");
        Number number = mapper.readValue(parser, Number.class);
        assertEquals(123, number.intValue());

        ObjectNode node = mapper.createObjectNode();
        node.put("num", 456);
        parser = mapper.getFactory().createParser(node);
        number = mapper.readValue(parser, Number.class);
        assertEquals(456, number.intValue());

        node.put("num", "InvalidIntValue");
        parser = mapper.getFactory().createParser(node);
        try {
            mapper.readValue(parser, Number.class);
        } catch (JsonMappingException e) {
            assertEquals("Cannot deserialize instance of `java.lang.Number` out of START_ARRAY token", e.getMessage());
        }
    }

    @Test
    public void testIntegerSerializerWithType() throws JsonMappingException, IOException {
        ObjectNode node = mapper.createObjectNode();
        node.put("num", 789);
        ObjectNode resultNode = mapper.readValue(node, ObjectNode.class);
        JsonParser parser = mapper.getFactory().createParser(resultNode);
        Number number = mapper.readerForUpdating(resultNode).readValueAs(Number.class);
        assertEquals(789, number.intValue());
    }

    @Test
    public void testLongSerializer() throws JsonMappingException, IOException {
        JsonParser parser = mapper.getFactory().createParser("9876543210");
        Number number = mapper.readValue(parser, Number.class);
        assertEquals(9876543210L, number.longValue());

        ObjectNode node = mapper.createObjectNode();
        node.put("num", 123456789012L);
        parser = mapper.getFactory().createParser(node);
        number = mapper.readValue(parser, Number.class);
        assertEquals(123456789012L, number.longValue());

        node.put("num", "InvalidLongValue");
        parser = mapper.getFactory().createParser(node);
        try {
            mapper.readValue(parser, Number.class);
        } catch (JsonMappingException e) {
            assertEquals("Cannot deserialize instance of `java.lang.Number` out of START_ARRAY token", e.getMessage());
        }
    }

    @Test
    public void testLongSerializerWithType() throws JsonMappingException, IOException {
        ObjectNode node = mapper.createObjectNode();
        node.put("num", 123456789012345L);
        ObjectNode resultNode = mapper.readValue(node, ObjectNode.class);
        JsonParser parser = mapper.getFactory().createParser(resultNode);
        Number number = mapper.readerForUpdating(resultNode).readValueAs(Number.class);
        assertEquals(123456789012345L, number.longValue());
    }
}