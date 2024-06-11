import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Integer input = 123;
        String json = mapper.writeValueAsString(input);
        JsonParser parser = mapper.getFactory().createParser(json);
        Object readValue = parser.readValueAs(Integer.class);
        assertEquals(input, readValue);
    }

    @Test
    public void testIntegerSerializerWithNegativeNumber() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Integer input = -123;
        String json = mapper.writeValueAsString(input);
        JsonParser parser = mapper.getFactory().createParser(json);
        Object readValue = parser.readValueAs(Integer.class);
        assertEquals(input, readValue);
    }

    @Test(expected = JsonProcessingException.class)
    public void testIntegerSerializerWithStringInput() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String input = "abc";
        String json = mapper.writeValueAsString(input);
        JsonParser parser = mapper.getFactory().createParser(json);
        parser.nextToken(); // consume [null]
        parser.nextToken(); // consume start object
        parser.nextFieldName("number");
        parser.nextValue(); // consume string "abc"
        parser.nextToken(); // consume end object
        parser.readValueAs(Integer.class);
    }

    // Add more test cases for other serializers in the class...
}