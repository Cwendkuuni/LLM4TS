import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;

public class JsonGeneratorImplTest {
    @Test
    public void testWriteStringField() throws IOException {
        // Test 1 from Expert 1
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeString("John Doe");
        jsonGenerator.writeEndObject();

        String result = new String(jsonGenerator.getOutputBuffer().getBuffer(), 0, jsonGenerator.getOutputBuffer().length());
        assertEquals("{\"name\":\"John Doe\"}", result);
    }

    @Test
    public void testWriteStringFieldWithNull() {
        // Test 2 from Expert 1
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeString(null);
        jsonGenerator.writeEndObject();

        try {
            String result = new String(jsonGenerator.getOutputBuffer().getBuffer(), 0, jsonGenerator.getOutputBuffer().length());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testWriteStringFieldWithEscapedChars() {
        // Test 1 from Expert 2
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
        jsonGenerator.setCharacterEscapes(new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[]{-1, 1, 2, 3}; // Custom escape codes
            }
        });
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeString("John O'Reilly");
        jsonGenerator.writeEndObject();

        String result = new String(jsonGenerator.getOutputBuffer().getBuffer(), 0, jsonGenerator.getOutputBuffer().length());
        assertEquals("{\"name\":\"John O'Reilly\"}", result);

        // Check if characters were escaped
        int index = result.indexOf("O'");
        assertEquals(-1, jsonGenerator.getOutputBuffer().getBuffer()[index - 1]); // Previous character should be an escape code
    }

    @Test
    public void testSetCharacterEscapesWithNull() {
        // Test 1 from Expert 2
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
        jsonGenerator.setCharacterEscapes(null);

        assertNotNull(jsonGenerator._characterEscapes); // Default escapes should still be set
    }

    @Test
    public void testWriteStringFieldWithMaxNonEscapedChar() {
        // Test 1 from Expert 3
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), Feature.ESCAPE_NON_ASCII.asInt(), null);
        jsonGenerator.setHighestNonEscapedChar(0x100);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeString("Latin 1 character set");
        jsonGenerator.writeEndObject();

        String result = new String(jsonGenerator.getOutputBuffer().getBuffer(), 0, jsonGenerator.getOutputBuffer().length());
        assertEquals("{\"name\":\"Latin 1 character set\"}", result);

        // Check if character was not escaped
        int index = result.indexOf("Latin");
        assertEquals(0x4c, jsonGenerator.getOutputBuffer().getBuffer()[index]); // Character 'L' should not be escaped
    }

    @Test
    public void testSetHighestNonEscapedCharWithNegativeValue() {
        // Test 2 from Expert 3
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), Feature.ESCAPE_NON_ASCII.asInt(), null);
        jsonGenerator.setHighestNonEscapedChar(-1);

        assertEquals(0, jsonGenerator.getHighestEscapedChar()); // All characters should be escaped
    }
}