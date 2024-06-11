import org.junit.Test;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import static org.junit.Assert.*;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultConstructor() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new ByteArrayOutputStream(), prettyPrinter);

        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        prettyPrinter.writeStartObject(jsonGenerator);

        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        prettyPrinter.writeObjectFieldName(jsonGenerator, "name");
        prettyPrinter.writeObjectFieldValue(jsonGenerator, "John Doe");

        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        prettyPrinter.writeObjectFieldName(jsonGenerator, "age");
        prettyPrinter.writeObjectFieldValue(jsonGenerator, 30);

        prettyPrinter.writeEndObject(jsonGenerator);
        String jsonString = jsonGenerator.getOutputStream().toString();

        String expectedJson = "{\n" +
                "  \"name\": \"John Doe\",\n" +
                "  \"age\": 30\n" +
                "}";

        assertEquals(expectedJson, jsonString);
    }

    // Add other test cases for methods: writeStartArray, writeEndArray, etc.
}