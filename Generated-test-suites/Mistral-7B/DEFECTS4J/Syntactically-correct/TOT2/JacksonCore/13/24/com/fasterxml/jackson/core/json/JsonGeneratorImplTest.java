import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JsonGeneratorImplTest {

    @Test
    public void testWriteStringField() throws IOException {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(new IOContext(), Feature.DEFAULT_USE_ANNOTATIONS.asInt(), null);
        String key = "key";
        String value = "value";
        generator.writeStringField(key, value);

        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser(generator.getOutputSteam());
        parser.nextToken(); // NEXT_TOKEN_START_OBJECT
        parser.nextFieldName(); // NEXT_FIELD_NAME
        assertEquals(key, parser.getText());
        parser.nextToken(); // NEXT_VALUE_STRING
        assertEquals(value, parser.getText());
        parser.close();
    }
}