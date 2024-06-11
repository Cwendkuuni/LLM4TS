import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.io.SerializedString;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        PrettyPrinter prettyPrinter = jsonFactory.getCodec().getDefaultPrettyPrinter();

        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);
        prettyPrinter.writeValue(jsonGenerator, new Object());
        jsonGenerator.flush();

        String expectedOutput = "{\n" +
                "}\n";

        String actualOutput = System.out.getBytes().toString();
        assertEquals(expectedOutput, actualOutput);

        // Test with custom root separator
        prettyPrinter = jsonFactory.getCodec().getDefaultPrettyPrinter();
        prettyPrinter = prettyPrinter.withRootSeparator(new SerializedString("--"));

        jsonGenerator = jsonFactory.createGenerator(System.out);
        prettyPrinter.writeValue(jsonGenerator, new Object());
        jsonGenerator.flush();

        expectedOutput = "--{\n--}\n";
        actualOutput = System.out.getBytes().toString();
        assertEquals(expectedOutput, actualOutput);

        // Test indenting arrays
        prettyPrinter = jsonFactory.getCodec().getDefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(new FixedSpaceIndenter());

        jsonGenerator = jsonFactory.createGenerator(System.out);
        prettyPrinter.writeValue(jsonGenerator, new int[] { 1, 2, 3 });
        jsonGenerator.flush();

        expectedOutput = "[ 1,\n 2,\n 3]\n";
        actualOutput = System.out.getBytes().toString();
        assertEquals(expectedOutput, actualOutput);

        // Test indenting objects
        prettyPrinter = jsonFactory.getCodec().getDefaultPrettyPrinter();
        prettyPrinter.indentObjectsWith(new DefaultIndenter.SYSTEM_LINEFEED_INSTANCE());

        jsonGenerator = jsonFactory.createGenerator(System.out);
        prettyPrinter.writeValue(jsonGenerator, new Object() {
            String name = "Test";
            int age = 30;
        });
        jsonGenerator.flush();

        expectedOutput = "{\n" +
                "  \"name\": \"Test\",\n" +
                "  \"age\": 30\n" +
                "}\n";
        actualOutput = System.out.getBytes().toString();
        assertEquals(expectedOutput, actualOutput);
    }

    // Test edge cases and error scenarios here
}