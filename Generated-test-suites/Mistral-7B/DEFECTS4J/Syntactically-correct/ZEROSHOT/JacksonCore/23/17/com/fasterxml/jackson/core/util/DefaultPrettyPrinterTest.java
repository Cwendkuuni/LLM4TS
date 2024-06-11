package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.JsonFactory;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinterConstruction() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNotNull(prettyPrinter);
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparator() throws JsonProcessingException, IOException {
        String rootSeparator = "Root:";
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString(rootSeparator));
        assertNotNull(prettyPrinter);
        assertEquals(rootSeparator, prettyPrinter.getRootSeparator());
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparatorString() throws JsonProcessingException, IOException {
        String rootSeparator = "Root:";
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(rootSeparator);
        assertNotNull(prettyPrinter);
        assertEquals(new SerializedString(rootSeparator), prettyPrinter.getRootSeparator());
    }

    @Test
    public void testDefaultPrettyPrinterCopy() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter base = new DefaultPrettyPrinter();
        DefaultPrettyPrinter coppied = new DefaultPrettyPrinter(base);
        assertNotNull(coppied);
        assertEquals(base._rootSeparator, coppied._rootSeparator);
        assertEquals(base._arrayIndenter, coppied._arrayIndenter);
        assertEquals(base._objectIndenter, coppied._objectIndenter);
        assertEquals(base._spacesInObjectEntries, coppied._spacesInObjectEntries);
        assertEquals(base._nesting, coppied._nesting);
        assertEquals(base._separators, coppied._separators);
        assertEquals(base._objectFieldValueSeparatorWithSpaces, coppied._objectFieldValueSeparatorWithSpaces);
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparatorNull() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(null);
        assertNotNull(prettyPrinter);
        assertNull(prettyPrinter.getRootSeparator());
    }

    @Test
    public void testSetRootSeparator() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        String rootSeparator = "Root:";
        DefaultPrettyPrinter newPrettyPrinter = prettyPrinter.withRootSeparator(new SerializedString(rootSeparator));
        assertNotNull(newPrettyPrinter);
        assertEquals(newSerializedString(rootSeparator), newPrettyPrinter.getRootSeparator());
    }

    @Test
    public void testSetRootSeparatorString() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        String rootSeparator = "Root:";
        DefaultPrettyPrinter newPrettyPrinter = prettyPrinter.withRootSeparator(rootSeparator);
        assertNotNull(newPrettyPrinter);
        assertEquals(new SerializedString(rootSeparator), newPrettyPrinter.getRootSeparator());
    }

    @Test
    public void testIndentArraysWith() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter indenter = new IndenterStub();
        DefaultPrettyPrinter newPrettyPrinter = prettyPrinter.indentArraysWith(indenter);
        assertNotNull(newPrettyPrinter);
        assertEquals(indenter, newPrettyPrinter._arrayIndenter);
    }

    @Test
    public void testIndentObjectsWith() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter indenter = new IndenterStub();
        DefaultPrettyPrinter newPrettyPrinter = prettyPrinter.indentObjectsWith(indenter);
        assertNotNull(newPrettyPrinter);
        assertEquals(indenter, newPrettyPrinter._objectIndenter);
    }

    @Test
    public void testWithArrayIndenter() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter indenter = new IndenterStub();
        DefaultPrettyPrinter newPrettyPrinter = prettyPrinter.withArrayIndenter(indenter);
        assertNotNull(newPrettyPrinter);
        assertEquals(indenter, newPrettyPrinter._arrayIndenter);
    }

    @Test
    public void testWithObjectIndenter() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter indenter = new IndenterStub();
        DefaultPrettyPrinter newPrettyPrinter = prettyPrinter.withObjectIndenter(indenter);
        assertNotNull(newPrettyPrinter);
        assertEquals(indenter, newPrettyPrinter._objectIndenter);
    }

    @Test
    public void testWithSpacesInObjectEntriesTrue() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter newPrettyPrinter = prettyPrinter.withSpacesInObjectEntries();
        assertNotNull(newPrettyPrinter);
        assertTrue(newPrettyPrinter._spacesInObjectEntries);
    }

    @Test
    public void testWithSpacesInObjectEntriesFalse() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter newPrettyPrinter = prettyPrinter.withoutSpacesInObjectEntries();
        assertNotNull(newPrettyPrinter);
        assertFalse(newPrettyPrinter._spacesInObjectEntries);
    }

    @Test
    public void testWithSeparators() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Separators separators = new SeparatorsStub();
        DefaultPrettyPrinter newPrettyPrinter = prettyPrinter.withSeparators(separators);
        assertNotNull(newPrettyPrinter);
        assertEquals(separators, newPrettyPrinter._separators);
    }

    private static class IndenterStub implements Indenter {
        @Override
        public void writeIndentation(JsonGenerator g, int level) throws IOException {
            g.writeRaw("IndenterStub_");
        }

        @Override
        public boolean isInline() {
            return false;
        }
    }

    private static class SeparatorsStub implements Separators {
        @Override
        public CharSequence getArrayValueSeparator() {
            return new SerializedString(",");
        }

        @Override
        public CharSequence getObjectEntrySeparator() {
            return new SerializedString("{\\n");
        }

        @Override
        public CharSequence getObjectFieldValueSeparator() {
            return new SerializedString(":");
        }
    }

    private JsonGenerator createJsonGenerator() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        return jsonFactory.createGenerator(new ByteArrayOutputStream(), new DefaultPrettyPrinter());
    }

    private SerializedString newSerializedString(String str) {
        return new SerializedString(str);
    }

}