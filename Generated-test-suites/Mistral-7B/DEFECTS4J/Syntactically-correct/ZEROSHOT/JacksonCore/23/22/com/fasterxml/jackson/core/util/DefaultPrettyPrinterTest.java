package com.fasterxml.jackson.core.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.*;

/**
 * Test cases for DefaultPrettyPrinter class.
 */
public class DefaultPrettyPrinterTest {

    private DefaultPrettyPrinter prettyPrinter;

    @Before
    public void setUp() {
        prettyPrinter = new DefaultPrettyPrinter();
    }

    @Test
    public void testDefaultConstruction() {
        // Default constructor should use space as root separator
        JsonGenerator generator = createTestGenerator();
        prettyPrinter.writeRootValueSeparator(generator);
        String rootValueSeparator = generator.getOutputBuffer().toString();
        assertEquals("", rootValueSeparator);

        // Default indenters should be FixedSpaceIndenter for both object and array
        assertEquals(FixedSpaceIndenter.instance, prettyPrinter._arrayIndenter);
        assertEquals(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE, prettyPrinter._objectIndenter);
    }

    @Test
    public void testCustomRootSeparatorConstruction() {
        String rootSeparator = "custom";
        JsonGenerator generator = createTestGenerator();
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString(rootSeparator));
        prettyPrinter.writeRootValueSeparator(generator);
        String rootValueSeparator = generator.getOutputBuffer().toString();
        assertEquals(rootSeparator, rootValueSeparator);
    }

    @Test
    public void testWritingObjectsWithRootSeparator() {
        String rootSeparator = "custom";
        JsonGenerator generator = createTestGenerator();
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString(rootSeparator));
        ObjectNode node = createObjectNode();
        prettyPrinter.writeStartObject(generator);
        prettyPrinter.writeRootValueSeparator(generator);
        prettyPrinter.writeFieldName("name", generator);
        prettyPrinter.writeValue("John Doe", generator);
        prettyPrinter.writeEndObject(generator, 1);

        String output = generator.getOutputBuffer().toString();
        assertEquals(String.format("%s{\n%s%s: %s\n%s}", rootSeparator, rootSeparator, "name", "John Doe"), output);
    }

    @Test
    public void testWritingObjectsWithoutRootSeparator() {
        JsonGenerator generator = createTestGenerator();
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        ObjectNode node = createObjectNode();
        prettyPrinter.writeStartObject(generator);
        prettyPrinter.writeFieldName("name", generator);
        prettyPrinter.writeValue("John Doe", generator);
        prettyPrinter.writeEndObject(generator, 1);

        String output = generator.getOutputBuffer().toString();
        assertEquals("{\n" +
                     "  \"name\": \"John Doe\"\n" +
                     "}", output);
    }

    @Test
    public void testWritingObjectsWithSpacesInObjectEntries() {
        JsonGenerator generator = createTestGenerator();
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        ObjectNode node = createObjectNode();
        prettyPrinter.withSpacesInObjectEntries();
        prettyPrinter.writeStartObject(generator);
        prettyPrinter.writeFieldName("name", generator);
        prettyPrinter.writeValue("John Doe", generator);
        prettyPrinter.writeEndObject(generator, 1);

        String output = generator.getOutputBuffer().toString();
        assertEquals("{\n" +
                     "  \"name\": \"John Doe\"\n" +
                     "}", output);

        // Test the same with custom root separator
        String rootSeparator = "custom";
        prettyPrinter = new DefaultPrettyPrinter(new SerializedString(rootSeparator));
        prettyPrinter.withSpacesInObjectEntries();
        prettyPrinter.writeStartObject(generator);
        prettyPrinter.writeRootValueSeparator(generator);
        prettyPrinter.writeFieldName("name", generator);
        prettyPrinter.writeValue("John Doe", generator);
        prettyPrinter.writeEndObject(generator, 1);

        output = generator.getOutputBuffer().toString();
        assertEquals(String.format("%s{\n%s  \"name\": \"John Doe\"\n%s}", rootSeparator, rootSeparator, rootSeparator), output);
    }

    @Test
    public void testWritingArraysWithCustomIndenter() {
        Indenter customIndenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("custom");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };

        JsonGenerator generator = createTestGenerator();
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(customIndenter);
        JsonArray array = createJsonArray();
        prettyPrinter.writeStartArray(generator);
        prettyPrinter.writeValue(1, generator);
        prettyPrinter.writeValue(2, generator);
        prettyPrinter.writeValue(3, generator);
        prettyPrinter.writeEndArray(generator, 3);

        String output = generator.getOutputBuffer().toString();
        assertEquals("custom[", output);
    }

    private ObjectNode createObjectNode() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("name", "John Doe");
        return node;
    }

    private JsonArray createJsonArray() {
        ObjectMapper mapper = new ObjectMapper();
        JsonArray array = mapper.createArrayNode();
        array.add(1);
        array.add(2);
        array.add(3);
        return array;
    }

    private JsonGenerator createTestGenerator() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
    }
}