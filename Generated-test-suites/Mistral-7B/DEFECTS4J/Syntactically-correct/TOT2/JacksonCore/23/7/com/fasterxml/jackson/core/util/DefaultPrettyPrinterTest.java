import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerator;
import static org.junit.Assert.assertEquals;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultConstructor() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new TestJsonFactory().createGenerator();
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        String output = jsonGenerator.getOutputBuffer().toString();
        assertEquals("", output);
        jsonGenerator.close();
    }

    @Test
    public void testConstructorWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("test"));
        JsonGenerator jsonGenerator = new TestJsonFactory().createGenerator();
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        String output = jsonGenerator.getOutputBuffer().toString();
        assertEquals("test", output);
        jsonGenerator.close();
    }

    // Add other test methods for each public method in DefaultPrettyPrinter class
}