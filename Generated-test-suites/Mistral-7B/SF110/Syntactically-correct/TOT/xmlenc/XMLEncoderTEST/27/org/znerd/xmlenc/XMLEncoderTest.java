package org.znerd.xmlenc;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;

import org.junit.Test;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertEquals("UTF-8", encoder.getEncoding());
        encoder = XMLEncoder.getEncoder("UTF-16");
        assertEquals("UTF-16", encoder.getEncoding());
        try {
            encoder = XMLEncoder.getEncoder("InvalidEncoding");
            fail("Should have thrown an UnsupportedEncodingException");
        } catch (UnsupportedEncodingException e) {
            // Expected
        }
    }

    @Test
    public void testDeclaration() throws IOException {
        String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"US-ASCII\"?>";
        Writer out = mock(Writer.class);
        XMLEncoder encoder = new XMLEncoder("US-ASCII");
        encoder.declaration(out);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        writer(out).write(expectedDeclaration.toCharArray(), 0, expectedDeclaration.length());
        verify(out).write(expectedDeclaration.toCharArray(), 0, expectedDeclaration.length());
        assertEquals(expectedDeclaration, byteStream.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testDeclarationNullWriter() {
        XMLEncoder encoder = new XMLEncoder("US-ASCII");
        encoder.declaration(null);
    }

    @Test
    public void testTextWithValidCharacters() throws IOException {
        String text = "Hello, World!";
        Writer out = mock(Writer.class);
        XMLEncoder encoder = new XMLEncoder("US-ASCII");
        encoder.text(out, text, false);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        writer(out).write(text.toCharArray(), 0, text.length());
        verify(out).write(text.toCharArray(), 0, text.length());
        assertEquals(text, byteStream.toString());
    }

    @Test
    public void testTextWithInvalidCharacters() throws IOException, InvalidXMLException {
        String text = "Hello, <World!";
        Writer out = mock(Writer.class);
        XMLEncoder encoder = new XMLEncoder("US-ASCII");
        encoder.text(out, text.toCharArray(), 0, text.length(), false);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        writer(out).write("Hello, &lt;World!".toCharArray(), 0, "Hello, &lt;World!".toCharArray().length);
        verify(out).write("Hello, &lt;World!".toCharArray(), 0, "Hello, &lt;World!".toCharArray().length);
        assertEquals("Hello, &lt;World!", byteStream.toString());
    }

    @Test
    public void testTextWithValidCharactersAndEscapeAmpersands() throws IOException {
        String text = "Hello, World!";
        Writer out = mock(Writer.class);
        XMLEncoder encoder = new XMLEncoder("US-ASCII");
        encoder.text(out, text, true);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        writer(out).write("Hello, World!".toCharArray(), 0, "Hello, World!".toCharArray().length);
        verify(out, never()).write(anyCharArray());
        assertEquals("Hello, World!\r\n", byteStream.toString());
    }

    @Test
    public void testTextWithInvalidCharactersAndEscapeAmpersands() throws IOException, InvalidXMLException {
        String text = "Hello, <World!";
        Writer out = mock(Writer.class);
        XMLEncoder encoder = new XMLEncoder("US-ASCII");
        encoder.text(out, text.toCharArray(), 0, text.length(), true);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        writer(out).write("Hello, &amp;lt;World&amp;gt;!".toCharArray(), 0, "Hello, &amp;lt;World&amp;gt;!".toCharArray().length);
        verify(out, never()).write(anyCharArray());
        assertEquals("Hello, &amp;lt;World&amp;gt;!", byteStream.toString());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTextWithInvalidIndex() throws IOException {
        String text = "Hello, World!";
        Writer out = mock(Writer.class);
        XMLEncoder encoder = new XMLEncoder("US-ASCII");
        encoder.text(out, text.toCharArray(), 5, text.length(), false);
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextWithInvalidCharacter() throws IOException {
        String text = "\u0001Hello, World!";
        Writer out = mock(Writer.class);
        XMLEncoder encoder = new XMLEncoder("US-ASCII");
        encoder.text(out, text.toCharArray(), 0, text.length(), false);
    }

    @Test(expected = NullPointerException.class)
    public void testTextWithNullWriter() throws IOException {
        String text = "Hello, World!";
        XMLEncoder encoder = new XMLEncoder("US-ASCII");
        encoder.text(null, text.toCharArray(), 0, text.length(), false);
    }

    @Test
    public void testTextWithSingleCharacter() throws IOException {
        char character = 'A';
        Writer out = mock(Writer.class);
        XMLEncoder encoder = new XMLEncoder("US-ASCII");
        encoder.text(out, character, false);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        writer(out).write(new char[] { character }, 0, 1);
        verify(out).write(new char[] { character }, 0, 1);
        assertEquals(Character.toString(character), byteStream.toString());
    }

    @Test
    public void testTextWithSingleCharacterAndEscapeAmpersands() throws IOException {
        char character = 'A';
        Writer out = mock(Writer.class);
        XMLEncoder encoder = new XMLEncoder("US-ASCII");
        encoder.text(out, character, true);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        writer(out).write(new char[] { 'A' }, 0, 1);
        verify(out, never()).write(anyCharArray());
        assertEquals("A", byteStream.toString());
    }

    @Test
    public void testTextWithSingleCharacterAndInvalid() throws IOException, InvalidXMLException {
        char character = '\u0001';
        Writer out = mock(Writer.class);
        XMLEncoder encoder = new XMLEncoder("US-ASCII");
        encoder.text(out, character, false);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        writer(out).write(new char[] { '?' }, 0, 1);
        writer(out).write(XMLEncoder.AMPERSAND_HASH, 0, 2);
        writer(out).write(Integer.toString(0x01), 0, 3);
        writer(out).write(XMLEncoder.EQUALS_QUOTE, 0, 2);
        verify(out).write(new char[] { '?' }, 0, 1);
        verify(out).write(XMLEncoder.AMPERSAND_HASH, 0, 2);
        verify(out).write(Integer.toString(0x01), 0, 3);
        verify(out).write(XMLEncoder.EQUALS_QUOTE, 0, 2);
        assertEquals("?#x01", byteStream.toString());
    }

    @Test
    public void testTextWithSingleCharacterAndEscapeAmpersandsAndInvalid() throws IOException, InvalidXMLException {
        char character = '\u0001';
        Writer out = mock(Writer.class);
        XMLEncoder encoder = new XMLEncoder("US-ASCII");
        encoder.text(out, character, true);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        writer(out).write(new char[] { 'A' }, 0, 1);
        verify(out, never()).write(anyCharArray());
        assertEquals("A", byteStream.toString());
    }

    @Test
    public void testWhitespace() throws IOException, InvalidXMLException {
        String whitespace = "   Hello, World!";
        Writer out = mock(Writer.class);
        XMLEncoder encoder = new XMLEncoder("US-ASCII");
        encoder.whitespace(out, whitespace);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        writer(out).write(whitespace.toCharArray(), 0, whitespace.length());
        verify(out).write(whitespace.toCharArray(), 0, whitespace.length());
        assertEquals(whitespace, byteStream.toString());
    }

    @Test
    public void testWhitespaceWithNullWriter() throws IOException, InvalidXMLException {
        String whitespace = "   Hello, World!";
        XMLEncoder encoder = new XMLEncoder("US-ASCII");
        encoder.whitespace(null, whitespace);
    }

    @Test
    public void testAttribute() throws IOException, InvalidXMLException {
        String name = "name";
        String value = "value";
        char quotationMark = '\'';
        boolean escapeAmpersands = false;
        Writer out = mock(Writer.class);
        XMLEncoder encoder = new XMLEncoder("US-ASCII");
        encoder.attribute(out, name, value, quotationMark, escapeAmpersands);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        writer(out).write(new char[] { 32 }, 0, 1);
        writer(out).write(name.toCharArray(), 0, name.length());
        writer(out).write(XMLEncoder.EQUALS_APOSTROPHE, 0, 2);
        if (quotationMark == '\'') {
            writer(out).write(XMLEncoder.ESC_APOSTROPHE, 0, 6);
        } else {
            writer(out).write(XMLEncoder.ESC_QUOTE, 0, 6);
        }
        for (int i = 0; i < value.length(); ++i) {
            char character = value.charAt(i);
            if ((character < 63 || character > 127) && (character < 40 || character > 59) && (character < 32 || character > 37) && (character != 34) && (character != 38 || escapeAmpersands) && character != 10 && character != 13 && character != 61) {
                if (character != 9) {
                    writer(out).write(new char[] { character }, 0, 1);
                    if (character == 60) {
                        writer(out).write(XMLEncoder.ESC_LESS_THAN, 0, 4);
                    } else if (character == 62) {
                        writer(out).write(XMLEncoder.ESC_GREATER_THAN, 0, 4);
                    } else if (character == 38) {
                        writer(out).write(XMLEncoder.ESC_AMPERSAND, 0, 5);
                    } else {
                        writer(out).write(XMLEncoder.AMPERSAND_HASH, 0, 2);
                        writer(out).write(Integer.toString(character), 0, Integer.toString(character).length());
                        writer(out).write(XMLEncoder.EQUALS_QUOTE, 0, 2);
                    }
                }
            } else {
                writer(out).write(new char[] { character }, 0, 1);
            }
        }
        writer(out).write(quotationMark);
        verify(out).write(new char[] { 32 }, 0, 1);
        verify(out).write(name.toCharArray(), 0, name.length());
        if (quotationMark == '\'') {
            verify(out).write(XMLEncoder.ESC_APOSTROPHE, 0, 6);
        } else {
            verify(out).write(XMLEncoder.ESC_QUOTE, 0, 6);
        }
        for (int i = 0; i < value.length(); ++i) {
            char character = value.charAt(i);
            if ((character < 63 || character > 127) && (character < 40 || character > 59) && (character < 32 || character > 37) && (character != 34) && (character != 38 || escapeAmpersands) && character != 10 && character != 13 && character != 61) {
                if (character != 9) {
                    writer(out).write(new char[] { character }, 0, 1);
                    if (character == 60) {
                        verify(out).write(XMLEncoder.ESC_LESS_THAN, 0, 4);
                    } else if (character == 62) {
                        verify(out).write(XMLEncoder.ESC_GREATER_THAN, 0, 4);
                    } else if (character == 38) {
                        verify(out).write(XMLEncoder.ESC_AMPERSAND, 0, 5);
                    } else {
                        writer(out).write(XMLEncoder.AMPERSAND_HASH, 0, 2);
                        verify(out).write(Integer.toString(character), 0, Integer.toString(character).length());
                        verify(out).write(XMLEncoder.EQUALS_QUOTE, 0, 2);
                    }
                }
            } else {
                writer(out).write(new char[] { character }, 0, 1);
            }
        }
        verify(out).write(quotationMark);

        assertEquals("  name=\"value\"", byteStream.toString());
    }

    // Add test methods for other methods in the XMLEncoder class here

    private Writer writer(Writer out) {
        return out;
    }
}