package com.fasterxml.jackson.core.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public class DefaultPrettyPrinterTest {

    private DefaultPrettyPrinter prettyPrinter;
    private JsonFactory jsonFactory;

    @Before
    public void setUp() {
        jsonFactory = new JsonFactory();
        prettyPrinter = new DefaultPrettyPrinter();
    }

    @After
    public void tearDown() {
        jsonFactory = null;
        prettyPrinter = null;
    }

    @Test
    public void testDefaultConstructor() throws IOException {
        JsonGenerator generator = jsonFactory.createGenerator(new StringWriter());
        prettyPrinter.writeRootValueSeparator(generator);
        prettyPrinter.writeStartObject(generator);
        prettyPrinter.writeObjectFieldName(generator, "field1");
        prettyPrinter.writeObjectFieldValue(generator, new NumberInt(1));
        prettyPrinter.writeObjectFieldName(generator, "field2");
        prettyPrinter.writeObjectFieldValue(generator, new NumberInt(2));
        prettyPrinter.writeEndObject(generator);

        String result = generator.getOutputContent().toString();
        String expected = "{\n" +
                " \"field1\": 1,\n" +
                " \"field2\": 2\n" +
                "}";
        assertEquals(expected, result);
    }

    @Test
    public void testCustomRootValueSeparatorConstructor() throws IOException {
        JsonGenerator generator = jsonFactory.createGenerator(new StringWriter());
        String rootSeparator = "---";
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString(rootSeparator));
        prettyPrinter.writeRootValueSeparator(generator);
        prettyPrinter.writeStartObject(generator);
        prettyPrinter.writeObjectFieldName(generator, "field1");
        prettyPrinter.writeObjectFieldValue(generator, new NumberInt(1));
        prettyPrinter.writeObjectFieldName(generator, "field2");
        prettyPrinter.writeObjectFieldValue(generator, new NumberInt(2));
        prettyPrinter.writeEndObject(generator);

        String result = generator.getOutputContent().toString();
        String expected = "---{\n" +
                " \"field1\": 1,\n" +
                " \"field2\": 2\n" +
                "}";
        assertEquals(expected, result);
    }

    @Test
    public void testWithRootSeparatorMethod() throws IOException {
        JsonGenerator generator = jsonFactory.createGenerator(new StringWriter());
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter = prettyPrinter.withRootSeparator(new SerializedString("---"));
        prettyPrinter.writeRootValueSeparator(generator);
        prettyPrinter.writeStartObject(generator);
        prettyPrinter.writeObjectFieldName(generator, "field1");
        prettyPrinter.writeObjectFieldValue(generator, new NumberInt(1));
        prettyPrinter.writeObjectFieldName(generator, "field2");
        prettyPrinter.writeObjectFieldValue(generator, new NumberInt(2));
        prettyPrinter.writeEndObject(generator);

        String result = generator.getOutputContent().toString();
        String expected = "---{\n" +
                " \"field1\": 1,\n" +
                " \"field2\": 2\n" +
                "}";
        assertEquals(expected, result);
    }

    @Test
    public void testWithRootSeparatorStringMethod() throws IOException {
        JsonGenerator generator = jsonFactory.createGenerator(new StringWriter());
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter = prettyPrinter.withRootSeparator("---");
        prettyPrinter.writeRootValueSeparator(generator);
        prettyPrinter.writeStartObject(generator);
        prettyPrinter.writeObjectFieldName(generator, "field1");
        prettyPrinter.writeObjectFieldValue(generator, new NumberInt(1));
        prettyPrinter.writeObjectFieldName(generator, "field2");
        prettyPrinter.writeObjectFieldValue(generator, new NumberInt(2));
        prettyPrinter.writeEndObject(generator);

        String result = generator.getOutputContent().toString();
        String expected = "---{\n" +
                " \"field1\": 1,\n" +
                " \"field2\": 2\n" +
                "}";
        assertEquals(expected, result);
    }

    @Test
    public void testWithArrayIndenterMethod() throws IOException {
        JsonGenerator generator = jsonFactory.createGenerator(new StringWriter());
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter arrayIndenter = new IndenterWithNewline();
        prettyPrinter = prettyPrinter.withArrayIndenter(arrayIndenter);

        prettyPrinter.writeStartArray(generator);
        prettyPrinter.writeValue(generator, new NumberInt(1));
        prettyPrinter.writeValue(generator, new NumberInt(2));
        prettyPrinter.writeValue(generator, new NumberInt(3));
        prettyPrinter.writeEndArray(generator);

        String result = generator.getOutputContent().toString();
        String expected = "[1\n" +
                " 2\n" +
                " 3]";
        assertEquals(expected, result);
    }

    @Test
    public void testWithObjectIndenterMethod() throws IOException {
        JsonGenerator generator = jsonFactory.createGenerator(new StringWriter());
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter objectIndenter = new IndenterWithTwoSpaces();
        prettyPrinter = prettyPrinter.withObjectIndenter(objectIndenter);

        prettyPrinter.writeStartObject(generator);
        prettyPrinter.writeObjectFieldName(generator, "field1");
        prettyPrinter.writeObjectFieldValue(generator, new NumberInt(1));
        prettyPrinter.writeObjectFieldName(generator, "field2");
        prettyPrinter.writeObjectFieldValue(generator, new NumberInt(2));
        prettyPrinter.writeEndObject(generator);

        String result = generator.getOutputContent().toString();
        String expected = "{\n" +
                " \"field1\": 1,\n" +
                " \"field2\": 2\n" +
                "}";
        assertEquals(expected, result);
    }

    @Test
    public void testWithSpacesInObjectEntriesMethod() throws IOException {
        JsonGenerator generator = jsonFactory.createGenerator(new StringWriter());
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter = prettyPrinter.withSpacesInObjectEntries();

        prettyPrinter.writeStartObject(generator);
        prettyPrinter.writeObjectFieldName(generator, "field1");
        prettyPrinter.writeObjectFieldValue(generator, new NumberInt(1));
        prettyPrinter.writeObjectFieldName(generator, "field2");
        prettyPrinter.writeObjectFieldValue(generator, new NumberInt(2));
        prettyPrinter.writeEndObject(generator);

        String result = generator.getOutputContent().toString();
        String expected = "{\n" +
                " \"field1\": 1,\n" +
                " \"field2\": 2\n" +
                "}";
        assertEquals(expected, result);
    }

    @Test
    public void testWithoutSpacesInObjectEntriesMethod() throws IOException {
        JsonGenerator generator = jsonFactory.createGenerator(new StringWriter());
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter = prettyPrinter.withoutSpacesInObjectEntries();

        prettyPrinter.writeStartObject(generator);
        prettyPrinter.writeObjectFieldName(generator, "field1");
        prettyPrinter.writeObjectFieldValue(generator, new NumberInt(1));
        prettyPrinter.writeObjectFieldName(generator, "field2");
        prettyPrinter.writeObjectFieldValue(generator, new NumberInt(2));
        prettyPrinter.writeEndObject(generator);

        String result = generator.getOutputContent().toString();
        String expected = "{\"field1\":1,\"field2\":2}";
        assertEquals(expected, result);
    }

    static class IndenterWithNewline implements Indenter {

        @Override
        public void writeIndentation(JsonGenerator g, int level) throws IOException {
            g.writeRaw("\n");
        }

        @Override
        public boolean isInline() {
            return false;
        }
    }

    static class IndenterWithTwoSpaces implements Indenter {

        @Override
        public void writeIndentation(JsonGenerator g, int level) throws IOException {
            for (int i = 0; i < level * 2; ++i) {
                g.writeRaw(' ');
            }
        }

        @Override
        public boolean isInline() {
            return false;
        }
    }
}