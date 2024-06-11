package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class XMLEncoderTest {
    private XMLEncoder encoder;
    private ByteArrayOutputStream outputStream;
    private OutputStreamWriter writer;

    @Before
    public void setup() {
        encoder = XMLEncoder.getEncoder("UTF-8");
        outputStream = new ByteArrayOutputStream();
        writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
    }

    @Test
    public void testGetEncoder() {
        encoder = XMLEncoder.getEncoder("UTF-8");
        assertNotNull(encoder);
        assertEquals("UTF-8", encoder.getEncoding());

        try {
            encoder = XMLEncoder.getEncoder(null);
            assertThrows(IllegalArgumentException.class, () -> encoder.getEncoding());
        } catch (IllegalArgumentException e) {
            assertEquals("encoding == null", e.getMessage());
        }

        try {
            encoder = XMLEncoder.getEncoder("NotASupportedEncoding");
            assertThrows(UnsupportedEncodingException.class, () -> encoder.getEncoding());
        } catch (UnsupportedEncodingException e) {
            assertEquals("NotASupportedEncoding", e.getMessage());
        }
    }

    @Test
    public void testDeclaration() throws IOException {
        encoder.declaration(writer);
        String declaration = outputStream.toString();
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", declaration);
    }

    @Test
    public void testTextNoEscaping() throws IOException, InvalidXMLException {
        encoder.text(writer, "abcdef");
        String result = outputStream.toString();
        assertEquals("abcdef", result);
    }

    @Test
    public void testTextSimpleEscaping() throws IOException, InvalidXMLException {
        encoder.text(writer, "& < > ' ");
        String result = outputStream.toString();
        assertEquals("&amp; &lt; &gt; &apos; &apos;", result);
    }

    @Test
    public void testTextComplexEscaping() throws IOException, InvalidXMLException {
        encoder.text(writer, "&<>\"'&amp;");
        String result = outputStream.toString();
        assertEquals("&amp;amp;lt;&gt;&amp;quot;&amp;apos;&amp;amp;", result);
    }

    @Test
    public void testTextEscapingWithSevenBitEncoding() throws IOException, InvalidXMLException {
        encoder = new XMLEncoder("US-ASCII");
        encoder.text(writer, "€");
        String result = outputStream.toString();
        assertEquals("&e2;&cent;", result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTextOutOfBounds() throws IOException, InvalidXMLException {
        char[] data = {'a', 'b'};
        encoder.text(writer, data, 2, 1);
    }

    @Test(expected = NullPointerException.class)
    public void testTextNullInput() throws IOException {
        encoder.text(writer, null);
    }

    @Test
    public void testTextInvalidCharacter() throws IOException, InvalidXMLException {
        encoder = new XMLEncoder("UTF-8");
        encoder.text(writer, "\u0080");
        assertThrows(InvalidXMLException.class, () -> writer.flush());
    }
}