import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectMapper;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for DefaultPrettyPrinter.
 */
public class DefaultPrettyPrinterTest {

    /**
     * Test method for {@link DefaultPrettyPrinter#DefaultPrettyPrinter()}.
     * This test case checks if an instance of DefaultPrettyPrinter can be created.
     */
    @Test
    public void testDefaultConstructor() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
    }

    /**
     * Test method for {@link DefaultPrettyPrinter#DefaultPrettyPrinter(SerializableString)}.
     */
    @Test
    public void testConstructorWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("rootValueSeparator"));
    }

    /**
     * Test method for {@link DefaultPrettyPrinter#withRootSeparator(SerializableString)}.
     */
    @Test
    public void testSetRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinterWithRootSeparator = prettyPrinter.withRootSeparator(new SerializedString("rootValueSeparator"));
        assert (prettyPrinterWithRootSeparator.getRootSeparator().getValueAsString().equals("rootValueSeparator"));
    }

    /**
     * Test method for {@link DefaultPrettyPrinter#withRootSeparator(String)}.
     */
    @Test
    public void testSetRootSeparatorWithString() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinterWithRootSeparator = prettyPrinter.withRootSeparator("rootValueSeparator");
        assert (prettyPrinterWithRootSeparator.getRootSeparator().getValueAsString().equals("rootValueSeparator"));
    }

    /**
     * Test method for {@link DefaultPrettyPrinter#indentArraysWith(Indenter)}.
     */
    @Test
    public void testSetArrayIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinterWithCustomArrayIndenter = prettyPrinter.indentArraysWith(new DefaultPrettyPrinter.FixedSpaceIndenter());
        assert (prettyPrinterWithCustomArrayIndenter.getArrayIndenter() instanceof DefaultPrettyPrinter.FixedSpaceIndenter);
    }

    /**
     * Test method for {@link DefaultPrettyPrinter#indentObjectsWith(Indenter)}.
     */
    @Test
    public void testSetObjectIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinterWithCustomObjectIndenter = prettyPrinter.indentObjectsWith(new DefaultPrettyPrinter.DefaultIndenter.SYSTEM_LINEFEED_INSTANCE());
        assert (prettyPrinterWithCustomObjectIndenter.getObjectIndenter() instanceof DefaultPrettyPrinter.DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
    }

    /**
     * Test method for {@link DefaultPrettyPrinter#withSpacesInObjectEntries()}.
     */
    @Test
    public void testSetSpacesInObjectEntriesTrue() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinterWithSpacesInObjectEntries = prettyPrinter.withSpacesInObjectEntries();
        assert (prettyPrinterWithSpacesInObjectEntries._spacesInObjectEntries);
    }

    /**
     * Test method for {@link DefaultPrettyPrinter#withSpacesInObjectEntries()}.
     */
    @Test
    public void testSetSpacesInObjectEntriesFalse() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinterWithoutSpacesInObjectEntries = prettyPrinter.withoutSpacesInObjectEntries();
        assert (!prettyPrinterWithoutSpacesInObjectEntries._spacesInObjectEntries);
    }

    /**
     * Test method for {@link DefaultPrettyPrinter#writeRootValueSeparator(JsonGenerator)}.
     */
    @Test
    public void testWriteRootValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory jsonFactory = new ObjectMapper().getFactory();
        JsonGenerator generator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.writeRootValueSeparator(generator);
        JsonParser parser = jsonFactory.createParser(new ByteArrayInputStream(generator.getOutputStream().toByteArray()));
        assert (parser.getValueAsString().equals(""));
    }

    /**
     * Test method for {@link DefaultPrettyPrinter#writeStartObject(JsonGenerator)}.
     */
    @Test
    public void testWriteStartObject() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory jsonFactory = new ObjectMapper().getFactory();
        JsonGenerator generator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.writeStartObject(generator);
        JsonParser parser = jsonFactory.createParser(new ByteArrayInputStream(generator.getOutputStream().toByteArray()));
        assert (parser.getValueAsString().equals("{"));
    }

    /**
     * Test method for {@link DefaultPrettyPrinter#beforeObjectEntries(JsonGenerator)}.
     */
    @Test
    public void testBeforeObjectEntries() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory jsonFactory = new ObjectMapper().getFactory();
        JsonGenerator generator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.beforeObjectEntries(generator);
        JsonParser parser = jsonFactory.createParser(new ByteArrayInputStream(generator.getOutputStream().toByteArray()));
        assert (parser.getValueAsString().equals(""));
    }

    /**
     * Test method for {@link DefaultPrettyPrinter#writeObjectFieldValueSeparator(JsonGenerator)}.
     */
    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory jsonFactory = new ObjectMapper().getFactory();
        JsonGenerator generator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.writeObjectFieldValueSeparator(generator);
        JsonParser parser = jsonFactory.createParser(new ByteArrayInputStream(generator.getOutputStream().toByteArray()));
        assert (parser.getValueAsString().equals(""));
    }

    /**
     * Test method for {@link DefaultPrettyPrinter#writeObjectEntrySeparator(JsonGenerator)}.
     */
    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory jsonFactory = new ObjectMapper().getFactory();
        JsonGenerator generator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.writeObjectEntrySeparator(generator);
        JsonParser parser = jsonFactory.createParser(new ByteArrayInputStream(generator.getOutputStream().toByteArray()));
        assert (parser.getValueAsString().equals(""));
    }

    /**
     * Test method for {@link DefaultPrettyPrinter#writeEndObject(JsonGenerator, int)}.
     */
    @Test
    public void testWriteEndObjectWithNoEntries() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory jsonFactory = new ObjectMapper().getFactory();
        JsonGenerator generator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.writeEndObject(generator, 0);
        JsonParser parser = jsonFactory.createParser(new ByteArrayInputStream(generator.getOutputStream().toByteArray()));
        assert (parser.getValueAsString().equals("}"));
    }

    /**
     * Test method for {@link DefaultPrettyPrinter#writeEndObject(JsonGenerator, int)}.
     */
    @Test
    public void testWriteEndObjectWithEntries() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory jsonFactory = new ObjectMapper().getFactory();
        JsonGenerator generator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
        JsonParser parser = jsonFactory.createParser(new ByteArrayInputStream(generator.getOutputStream().toByteArray()));
        JsonGenerator entryGenerator = parser.getCodec().getJsonFactory().createGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.writeStartObject(generator);
        prettyPrinter.writeObjectFieldName("fieldName");
        prettyPrinter.writeObjectFieldValueSeparator(generator);
        prettyPrinter.writeString("fieldValue");
        prettyPrinter.writeObjectEntrySeparator(generator);
        prettyPrinter.writeEndObject(generator, 1);
        JsonParser entryParser = jsonFactory.createParser(entryGenerator.getOutputStream().toByteArray());
        generator.writeEndObject();
        JsonParser parserWithEntry = jsonFactory.createParser(generator.getOutputStream().toByteArray());
        assert (parserWithEntry.getValueAsString().equals("{\"fieldName\":\"fieldValue\"}"));
    }

    /**
     * Test method for {@link DefaultPrettyPrinter#writeStartArray(JsonGenerator)}.
     */
    @Test
    public void testWriteStartArray() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory jsonFactory = new ObjectMapper().getFactory();
        JsonGenerator generator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.writeStartArray(generator);
        JsonParser parser = jsonFactory.createParser(new ByteArrayInputStream(generator.getOutputStream().toByteArray()));
        assert (parser.getValueAsString().equals("["));
    }

    /**
     * Test method for {@link DefaultPrettyPrinter#beforeArrayValues(JsonGenerator)}.
     */
    @Test
    public void testBeforeArrayValues() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory jsonFactory = new ObjectMapper().getFactory();
        JsonGenerator generator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.beforeArrayValues(generator);
        JsonParser parser = jsonFactory.createParser(new ByteArrayInputStream(generator.getOutputStream().toByteArray()));
        assert (parser.getValueAsString().equals(""));
    }

    /**
     * Test method for {@link DefaultPrettyPrinter#writeArrayValueSeparator(JsonGenerator)}.
     */
    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory jsonFactory = new ObjectMapper().getFactory();
        JsonGenerator generator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.writeArrayValueSeparator(generator);
        JsonParser parser = jsonFactory.createParser(new ByteArrayInputStream(generator.getOutputStream().toByteArray()));
        assert (parser.getValueAsString().equals(""));
    }

    /**
     * Test method for {@link DefaultPrettyPrinter#writeEndArray(JsonGenerator, int)}.
     */
    @Test
    public void testWriteEndArrayWithNoValues() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory jsonFactory = new ObjectMapper().getFactory();
        JsonGenerator generator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.writeStartArray(generator);
        prettyPrinter.writeEndArray(generator, 0);
        JsonParser parser = jsonFactory.createParser(new ByteArrayInputStream(generator.getOutputStream().toByteArray()));
        assert (parser.getValueAsString().equals("[]"));
    }

    /**
     * Test method for {@link DefaultPrettyPrinter#writeEndArray(JsonGenerator, int)}.
     */
    @Test
    public void testWriteEndArrayWithValues() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory jsonFactory = new ObjectMapper().getFactory();
        JsonGenerator generator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
        JsonParser parser = jsonFactory.createParser(new ByteArrayInputStream(generator.getOutputStream().toByteArray()));
        JsonGenerator valueGenerator = parser.getCodec().getJsonFactory().createGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.writeStartArray(generator);
        prettyPrinter.writeArrayValueSeparator(generator);
        prettyPrinter.writeString("value1");
        prettyPrinter.writeArrayValueSeparator(generator);
        prettyPrinter.writeString("value2");
        prettyPrinter.writeEndArray(generator, 2);
        JsonParser valueParser = jsonFactory.createParser(valueGenerator.getOutputStream().toByteArray());
        generator.writeEndArray();
        JsonParser parserWithValues = jsonFactory.createParser(generator.getOutputStream().toByteArray());
        assert (parserWithValues.getValueAsString().equals("[\"value1\",\"value2\"]"));
    }
}