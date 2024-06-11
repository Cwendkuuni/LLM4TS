
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.*;
import com.fasterxml.jackson.core.io.SerializedString;
import java.io.*;

public class DefaultPrettyPrinterTest {

    private JsonFactory jsonFactory;
    private DefaultPrettyPrinter prettyPrinter;

    @Before
    public void setUp() {
        jsonFactory = new JsonFactory();
        prettyPrinter = new DefaultPrettyPrinter();
    }

    @Test
    public void testDefaultConstructor() throws IOException {
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new StringWriter());
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        prettyPrinter.writeStartObject(jsonGenerator);
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        prettyPrinter.writeObjectFieldName(jsonGenerator, "field1");
        prettyPrinter.writeObjectFieldValue(jsonGenerator, "value1");
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        prettyPrinter.writeObjectFieldName(jsonGenerator, "field2");
        prettyPrinter.writeObjectFieldValue(jsonGenerator, "value2");
        prettyPrinter.writeEndObject(jsonGenerator);

        JsonParser jsonParser = jsonFactory.createParser(new StringReader(jsonGenerator.getOutputStr()));
        JsonNode rootNode = jsonParser.readValueAsTree();

        JsonNode expectedNode = Json.newObject()
                .put("field1", "value1")
                .put("field2", "value2");

        assertEquals(expectedNode, rootNode);
    }

    @Test
    public void testWithRootSeparatorConstructor() throws IOException {
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new StringWriter());
        prettyPrinter = new DefaultPrettyPrinter(new SerializedString("rootSeparator"));
        prettyPrinter.writeRootValueSeparator(jsonGenerator);

        JsonParser jsonParser = jsonFactory.createParser(new StringReader(jsonGenerator.getOutputStr()));
        JsonNode rootNode = jsonParser.readValueAsTree();

        JsonNode expectedNode = Json.newValueNode("rootSeparator");

        assertEquals(expectedNode, rootNode);
    }

    @Test
    public void testWriteStartObject() throws IOException {
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new StringWriter());
        prettyPrinter.writeStartObject(jsonGenerator);

        String result = jsonGenerator.getOutputStr();
        String expected = "{\"";
        assertEquals(expected, result);
    }

    // Add other test cases for remaining methods here
}