import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import org.junit.Test;

import java.io.StringWriter;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinterWithSimpleObject() throws Exception {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory factory = new JsonFactory();
        StringWriter stringWriter = new StringWriter();
        JsonGenerator generator = factory.createGenerator(stringWriter, JsonEncoding.UTF8);
        generator.setPrettyPrinter(prettyPrinter);

        Object obj = new Object() {{
            String field1 = "value1";
            int field2 = 1;
        }};
        generator.writeObject(obj);
        generator.close();

        String expectedOutput = "{\n" +
                "  \"field1\": \"value1\",\n" +
                "  \"field2\": 1\n" +
                "}";

        String actualOutput = stringWriter.toString();
        assert (expectedOutput.equals(actualOutput));
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparator() throws Exception {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter("root:");
        JsonFactory factory = new JsonFactory();
        StringWriter stringWriter = new StringWriter();
        JsonGenerator generator = factory.createGenerator(stringWriter, JsonEncoding.UTF8);
        generator.setPrettyPrinter(prettyPrinter);

        Object obj = new Object() {{
            String field1 = "value1";
            int field2 = 1;
        }};
        generator.writeObject(obj);
        generator.close();

        String expectedOutput = "root:\n" +
                "{\n" +
                "  \"field1\": \"value1\",\n" +
                "  \"field2\": 1\n" +
                "}";

        String actualOutput = stringWriter.toString();
        assert (expectedOutput.equals(actualOutput));
    }

    @Test
    public void testDefaultPrettyPrinterWithCustomIndenters() throws Exception {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentObjectsWith(new CustomObjectIndenter());
        prettyPrinter.indentArraysWith(new CustomArrayIndenter());

        JsonFactory factory = new JsonFactory();
        StringWriter stringWriter = new StringWriter();
        JsonGenerator generator = factory.createGenerator(stringWriter, JsonEncoding.UTF8);
        generator.setPrettyPrinter(prettyPrinter);

        Object obj = new Object() {{
            String[] field1 = {"value1", "value2"};
            String field2 = "value3";
        }};
        generator.writeObject(obj);
        generator.close();

        String expectedOutput = "{\n" +
                "  \"field2\": \"value3\",\n" +
                "  \"field1\": [\n" +
                "    \"value1\",\n" +
                "    \"value2\"\n" +
                "  ]\n" +
                "}";

        String actualOutput = stringWriter.toString();
        assert (expectedOutput.equals(actualOutput));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPrettyPrinterWithNullObject() throws Exception {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory factory = new JsonFactory();
        StringWriter stringWriter = new StringWriter();
        JsonGenerator generator = factory.createGenerator(stringWriter, JsonEncoding.UTF8);
        generator.setPrettyPrinter(prettyPrinter);

        generator.writeObject(null);
        generator.close();
    }

    @Test(expected = IOException.class)
    public void testDefaultPrettyPrinterWithIOException() throws Exception {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory factory = new JsonFactory();
        StringWriter stringWriter = new StringWriter();
        JsonGenerator generator = factory.createGenerator(stringWriter, JsonEncoding.UTF8);
        generator.setPrettyPrinter(prettyPrinter);

        generator.writeRaw("invalid json");
        generator.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPrettyPrinterWithNullIndenter() throws Exception {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentObjectsWith(null);
    }
}

class CustomObjectIndenter implements DefaultPrettyPrinter.Indenter {
    @Override
    public void writeIndentation(JsonGenerator g, int level) throws IOException {
        g.writeRaw("  ");
    }

    @Override
    public boolean isInline() {
        return false;
    }
}

class CustomArrayIndenter implements DefaultPrettyPrinter.Indenter {
    @Override
    public void writeIndentation(JsonGenerator g, int level) throws IOException {
        g.writeRaw("  ");
    }

    @Override
    public boolean isInline() {
        return false;
    }
}