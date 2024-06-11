import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberSerializersTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        Integer integer = 42;
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(integer);
        JsonParser parser = objectMapper.getFactory().createParser(json);
        assertEquals(42, parser.readValueAs(Integer.class));

        // Edge case: minimum and maximum integer values
        Integer minimumInt = Integer.MIN_VALUE;
        String minimumJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(minimumInt);
        JsonParser minimumParser = objectMapper.getFactory().createParser(minimumJson);
        assertEquals(minimumInt, minimumParser.readValueAs(Integer.class));

        Integer maximumInt = Integer.MAX_VALUE;
        String maximumJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(maximumInt);
        JsonParser maximumParser = objectMapper.getFactory().createParser(maximumJson);
        assertEquals(maximumInt, maximumParser.readValueAs(Integer.class));

        // Error scenario: non-integer value
        Integer nonInteger = 3.14;
        String nonIntegerJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(nonInteger);
        JsonParseException exception = assertThrows(JsonParseException.class, () -> objectMapper.readValue(nonIntegerJson, Integer.class));
        assertEquals("Cannot deserialize value of type [simple type, class java.lang.Integer] from ValueToken {'3.14'}", exception.getMessage());
    }

    @Test
    public void testIntegerSerializerWithType() throws JsonProcessingException {
        Integer integer = 42;
        String jsonWithType = objectMapper.writerWithTypeValue(Integer.class).writeValueAsString(integer);
        JsonParser parserWithType = objectMapper.getFactory().createParser(jsonWithType);
        assertEquals(42, parserWithType.readValueAs(Integer.class));

        // Edge case: minimum and maximum integer values
        Integer minimumInt = Integer.MIN_VALUE;
        String minimumJsonWithType = objectMapper.writerWithTypeValue(Integer.class).writeValueAsString(minimumInt);
        JsonParser minimumParserWithType = objectMapper.getFactory().createParser(minimumJsonWithType);
        assertEquals(minimumInt, minimumParserWithType.readValueAs(Integer.class));

        Integer maximumInt = Integer.MAX_VALUE;
        String maximumJsonWithType = objectMapper.writerWithTypeValue(Integer.class).writeValueAsString(maximumInt);
        JsonParser maximumParserWithType = objectMapper.getFactory().createParser(maximumJsonWithType);
        assertEquals(maximumInt, maximumParserWithType.readValueAs(Integer.class));

        // Error scenario: non-integer value
        Integer nonInteger = 3.14;
        String nonIntegerJsonWithType = objectMapper.writerWithTypeValue(Integer.class).writeValueAsString(nonInteger);
        JsonParseException exception = assertThrows(JsonParseException.class, () -> objectMapper.readValue(nonIntegerJsonWithType, Integer.class));
        assertEquals("Cannot deserialize value of type [simple type, class java.lang.Integer] from ValueToken {'3.14'}", exception.getMessage());
    }

    // Add test cases for other serializers (ShortSerializer, LongSerializer, FloatSerializer, DoubleSerializer)

    @Test
    public void testShortSerializer() {
        // ...
    }

    @Test
    public void testShortSerializerWithType() {
        // ...
    }

    // ...
}