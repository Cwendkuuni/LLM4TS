import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public class JsonGeneratorImplTest {

    @Test
    public void testWriteStringField() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonGenerator jsonGenerator = factory.createGenerator(System.out, new DefaultPrettyPrinter());

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeString("John Doe");
        jsonGenerator.writeEndObject();

        jsonGenerator.close();

        assertEquals("{\"name\":\"John Doe\"}", jsonGenerator.getOutputContent());
    }

    @Test
    public void testWriteFieldNameWithQuotes() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonGenerator jsonGenerator = factory.createGenerator(System.out, new DefaultPrettyPrinter());

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("\"field name\"");
        jsonGenerator.writeString("value");
        jsonGenerator.writeEndObject();

        jsonGenerator.close();

        assertEquals("{\"field name\":\"value\"}", jsonGenerator.getOutputContent());
    }

    @Test(expected = IOException.class)
    public void testWriteFieldNameWithoutQuotes() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonGenerator jsonGenerator = factory.createGenerator(System.out, new DefaultPrettyPrinter());

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("field name");
        jsonGenerator.writeString("value");
        jsonGenerator.writeEndObject();

        jsonGenerator.close();

        // This test should throw an IOException as per the codebase documentation:
        // "Throws an IOException if requested feature is not enabled and this
        // generator instance has been configured to disallow it."
    }
}