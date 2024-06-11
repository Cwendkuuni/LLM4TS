import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        prettyPrinter.writeStartObject(jsonGenerator);
        prettyPrinter.writeObjectFieldName(jsonGenerator, "name");
        prettyPrinter.writeObjectFieldValue(jsonGenerator, "John Doe");
        prettyPrinter.writeObjectFieldName(jsonGenerator, "age");
        prettyPrinter.writeObjectFieldValue(jsonGenerator, 30);
        prettyPrinter.writeEndObject(jsonGenerator);
        jsonGenerator.close();

        String expectedOutput = "{\n" +
                "  \"name\": \"John Doe\",\n" +
                "  \"age\": 30\n" +
                "}";

        String actualOutput = jsonGenerator.getOutputContent();
        assertEquals(expectedOutput, actualOutput);
    }
}