package com.fasterxml.jackson.core.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.SerializedString;

public class DefaultPrettyPrinterTest {

    private DefaultPrettyPrinter prettyPrinter;
    private JsonGenerator mockJsonGenerator;

    @Before
    public void setup() {
        mockJsonGenerator = mock(JsonGenerator.class);
        prettyPrinter = new DefaultPrettyPrinter();
    }

    @After
    public void tearDown() {
        prettyPrinter = null;
        mockJsonGenerator = null;
    }

    @Test
    public void testDefaultConstructor() {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();

        // Default constructor should initialize with default root separator
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, defaultPrettyPrinter.getRootSeparator());
    }

    @Test
    public void testConstructorWithRootSeparator() {
        String rootSeparator = "rootSeparator";
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter(rootSeparator);

        // Constructor with root separator should initialize with the given root separator
        assertEquals(new SerializedString(rootSeparator), defaultPrettyPrinter.getRootSeparator());
    }

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        String rootSeparator = "rootSeparator";
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter(new SerializedString(rootSeparator));

        defaultPrettyPrinter.writeRootValueSeparator(mockJsonGenerator);

        // WriteRootValueSeparator should write the root separator to the JsonGenerator
        verify(mockJsonGenerator).writeRaw(new SerializedString(rootSeparator).getValue());
    }

    @Test
    public void testWriteStartObject() throws IOException {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();

        defaultPrettyPrinter.writeStartObject(mockJsonGenerator);

        // WriteStartObject should write '{' to the JsonGenerator
        verify(mockJsonGenerator).writeRaw('{');
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();

        // By default, spacesInObjectEntries is true
        defaultPrettyPrinter.writeObjectFieldValueSeparator(mockJsonGenerator);

        // WriteObjectFieldValueSeparator should write the object field value separator with spaces
        verify(mockJsonGenerator).writeRaw("  : ");
    }

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();

        defaultPrettyPrinter.writeObjectEntrySeparator(mockJsonGenerator);

        // WriteObjectEntrySeparator should write the object entry separator
        verify(mockJsonGenerator).writeRaw("\n  ");
    }

    @Test
    public void testWriteEndObject() throws IOException {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();

        defaultPrettyPrinter.writeEndObject(mockJsonGenerator, 1);

        // WriteEndObject should write '}' to the JsonGenerator and indentation
        verify(mockJsonGenerator).writeRaw('}');
        verify(mockJsonGenerator).writeRaw("\n");
    }
}