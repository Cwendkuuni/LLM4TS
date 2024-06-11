import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class NumberSerializersTest {
    @Test
    public void testIntegerSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        IntegerSerializer intSerializer = mapper.findValue("1", IntegerSerializer.class);
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new JsonEncodingProperties().withOutputKind(JsonEncoding.Output.MINIMAL_PRETTY_PRINT));

        intSerializer.serialize(1, gen, null);
        String actual = gen.getOutputText();
        String expected = "1";
        assertEquals(expected, actual);

        // Test with negative number
        intSerializer.serialize(-1, gen, null);
        actual = gen.getOutputText();
        expected = "-1";
        assertEquals(expected, actual);

        // Test with maximum integer value
        intSerializer.serialize(Integer.MAX_VALUE, gen, null);
        actual = gen.getOutputText();
        expected = String.valueOf(Integer.MAX_VALUE);
        assertEquals(expected, actual);

        // Test with minimum integer value
        intSerializer.serialize(Integer.MIN_VALUE, gen, null);
        actual = gen.getOutputText();
        expected = String.valueOf(Integer.MIN_VALUE);
        assertEquals(expected, actual);

        // Test with an Exception
        intSerializer = mapper.findValue("not_an_integer", IntegerSerializer.class);
        assertNull(intSerializer);
    }

    // Add test cases for other methods...
}