package com.example;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DefaultPrettyPrinterTest {
    @Test
    public void testConstructorWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter("TestSeparator");
        assertEquals("TestSeparator", prettyPrinter.getRootValueSeparator().asString());

        SerializableString rootSeparator = new SerializedString("TestSeparator");
        prettyPrinter = new DefaultPrettyPrinter(rootSeparator);
        assertEquals("TestSeparator", prettyPrinter.getRootValueSeparator().asString());
    }

    @Test
    public void testSetRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        SerializableString rootSeparator = new SerializedString("NewSeparator");
        prettyPrinter.withRootSeparator(rootSeparator);
        assertEquals("NewSeparator", prettyPrinter.getRootValueSeparator().asString());

        prettyPrinter = new DefaultPrettyPrinter();
        String rootSeparatorStr = "NewSeparator";
        prettyPrinter.withRootSeparator(rootSeparatorStr);
        assertEquals("NewSeparator", prettyPrinter.getRootValueSeparator().asString());
    }

    @Test
    public void testWriteRootValueSeparator() {
        DefaultPrettyPrinter prettyPrinter = spy(new DefaultPrettyPrinter());
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        prettyPrinter.writeRootValueSeparator(mockGenerator);
        verify(mockGenerator).writeRaw("");
    }

    @Test
    public void testWriteRootValueSeparatorWithSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter("TestSeparator");
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        prettyPrinter.writeRootValueSeparator(mockGenerator);
        verify(mockGenerator).writeRaw("TestSeparator");
    }

    @Test
    public void testDefaultIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter defaultIndenter = prettyPrinter.getObjectIndenter();
        defaultIndenter.writeIndentation(mock(JsonGenerator.class), 1);
        assertEquals("", prettyPrinter.getIndentation().asString());
    }

    @Test
    public void testCustomIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter customIndenter = mock(Indenter.class);
        prettyPrinter.indentObjectsWith(customIndenter);
        prettyPrinter.writeObjectIndentation(mock(JsonGenerator.class), 1);
        verify(customIndenter).writeIndentation(eq(mock(JsonGenerator.class)), eq(1));
    }
}