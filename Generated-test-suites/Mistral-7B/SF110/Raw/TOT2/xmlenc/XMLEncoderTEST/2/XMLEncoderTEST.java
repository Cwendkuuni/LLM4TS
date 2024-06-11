package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.Assert.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class XMLEncoderTest {
    private static final String UTF_8 = "UTF-8";

    @Test
    public void testGetEncoder() {
        // ... (Expert 1: GetEncoderTest)
    }

    @Test
    public void testXMLEncoderConstructorWithValidEncoding() {
        // ... (Expert 1: XMLEncoderTest)
    }

    @Test
    public void testXMLEncoderConstructorWithInvalidEncodingButSupported() {
        // ... (Expert 1: XMLEncoderTest)
    }

    @Test
    public void testDeclaration() {
        // ... (Expert 2: DeclarationTest)
    }

    @Test
    public void testTextWithValidTextAndEscapeAmpersands() {
        // ... (Expert 3: TextTestWithValidTextAndEscapeAmpersands)
    }

    @Test
    public void testTextWithValidTextAndDontEscapeAmpersands() {
        // ... (Expert 3: TextTestWithValidTextAndDontEscapeAmpersands)
    }

    @Test
    public void testTextWithInvalidCharacter() {
        // ... (Expert 3: TextTestWithInvalidCharacter)
    }

    @Test
    public void testTextWithInvalidIndexOutOfBounds() {
        // ... (Expert 3: TextTestWithInvalidIndexOutOfBounds)
    }

    @Test
    public void testTextIOException() {
        // ... (Expert 3: TextIOException)
    }

    @Test
    public void testWhitespace() throws IOException {
        Writer out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String whitespace = "   The whitespace   ";
        encoder.whitespace(out, whitespace);
        String actualWhitespace = out.toString();
        String expectedWhitespace = "   The whitespace   ";
        assertEquals(expectedWhitespace, actualWhitespace);
    }

    @Test
    public void testAttribute() throws IOException {
        Writer out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String name = "name";
        String value = "value";
        char quotationMark = '\"';
        encoder.attribute(out, name, value, quotationMark, true);
        String actualAttribute = out.toString();
        String expectedAttribute = "name=\"value\"";
        assertEquals(expectedAttribute, actualAttribute);
    }
}