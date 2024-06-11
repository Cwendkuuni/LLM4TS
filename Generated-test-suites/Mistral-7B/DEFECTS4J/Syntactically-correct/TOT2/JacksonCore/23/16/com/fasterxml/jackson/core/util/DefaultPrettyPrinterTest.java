import org.junit.Test;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import static org.junit.Assert.assertEquals;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultRootValueSeparator() throws IOException {
        JsonFactory factory = new JsonFactory();
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = factory.createGenerator(new StringWriter());
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        String rootValueSeparator = jsonGenerator.getOutputFactory().getSerializerProvider().getAllSerializerProviders()
                .nextValueSerializer(String.class).serializeValues(jsonGenerator, null);
        assertEquals("", rootValueSeparator);
        jsonGenerator.close();
    }

    @Test
    public void testWriteStartObject() throws IOException {
        JsonFactory factory = new JsonFactory();
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = factory.createGenerator(new StringWriter());
        prettyPrinter.writeStartObject(jsonGenerator);
        String json = jsonGenerator.getOutputFactory().getSerializerProvider().getRootValueSerializer().serializeValues(jsonGenerator, null);
        assertEquals("{\n", json);
        jsonGenerator.close();
    }

    // Add other test methods for all other public methods in DefaultPrettyPrinter
}