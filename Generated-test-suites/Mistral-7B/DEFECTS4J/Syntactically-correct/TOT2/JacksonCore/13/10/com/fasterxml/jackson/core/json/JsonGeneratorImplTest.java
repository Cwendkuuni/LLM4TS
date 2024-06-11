import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonGeneratorImplTest {

    @Test
    public void testWriteStringField() throws IOException {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(new IOContext(), 0, null);
        generator.writeStartObject();
        generator.writeStringField("name", "John Doe");
        generator.writeEndObject();

        String json = generator.getOutputContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        assertTrue(parser.getText().contains("\"name\":\"John Doe\""));
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteStringFieldWithNullValue() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(new IOContext(), 0, null);
        generator.writeStartObject();
        generator.writeStringField("name", null);
        generator.writeEndObject();
    }

    @Test
    public void testWriteStringFieldWithEscapedCharacters() throws IOException {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(new IOContext(), 0, null);
        generator.setCharacterEscapes(CharacterEscapes.STANDARD_ESCAPES);
        generator.writeStartObject();
        generator.writeStringField("name", "O'Reilly");
        generator.writeEndObject();

        String json = generator.getOutputContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        assertTrue(parser.getText().contains("\"name\":\"O'Reilly\""));
    }
}