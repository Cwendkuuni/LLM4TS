package org.znerd.xmlenc;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertEquals("UTF-8", encoder.getEncoding());
        XMLEncoder encoderUcsAscii = XMLEncoder.getEncoder("US-ASCII");
        assertEquals("US-ASCII", encoderUcsAscii.getEncoding());
        assertThrows(IllegalArgumentException.class, () -> XMLEncoder.getEncoder(null));
        assertThrows(UnsupportedEncodingException.class, () -> XMLEncoder.getEncoder("UnknownEncoding"));
    }

    @Test
    public void testDeclaration() throws IOException, NullPointerException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = Mockito.mock(Writer.class);
        encoder.declaration(out);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                byteArrayOutputStream.write(cbuf, off, len);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };
        encoder.declaration(writer);
        String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        assertEquals(expectedDeclaration, byteArrayOutputStream.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void testTextWithEscaping() throws IOException, InvalidXMLException, NullPointerException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = Mockito.mock(Writer.class);
        String text = "The <quick> brown & fox jumps & over the & lazy dog.";
        encoder.text(out, text.toCharArray(), 0, text.length(), true);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                byteArrayOutputStream.write(cbuf, off, len);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };
        encoder.text(writer, text.toCharArray(), 0, text.length(), true);
        String expectedText = "The &lt;quick&gt; brown &amp; &amp;fx jumps &amp; &amp;amp;over the &amp; &amp;amp;lazy dog.";
        assertEquals(expectedText, byteArrayOutputStream.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void testTextWithoutEscaping() throws IOException, InvalidXMLException, NullPointerException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = Mockito.mock(Writer.class);
        String text = "The <quick> brown & fox jumps & over the & lazy dog.";
        encoder.text(out, text.toCharArray(), 0, text.length(), false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                byteArrayOutputStream.write(cbuf, off, len);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };
        encoder.text(writer, text.toCharArray(), 0, text.length(), false);
        String expectedText = "The <quick> brown & fox jumps & over the & lazy dog.";
        assertEquals(expectedText, byteArrayOutputStream.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void testTextInvalidCharacter() throws IOException, InvalidXMLException, NullPointerException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = Mockito.mock(Writer.class);
        char invalidCharacter = (char) 0xFFFD;
        encoder.text(out, new char[] { invalidCharacter }, 0, 1, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                byteArrayOutputStream.write(cbuf, off, len);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };
        encoder.text(writer, new char[] { invalidCharacter }, 0, 1, false);
        String expectedErrorMessage = "The character 0xFFFD (�) is not valid.";
        assertThrows(InvalidXMLException.class, () -> encoder.text(out, new char[] { invalidCharacter }, 0, 1, false));
        assertEquals(expectedErrorMessage, byteArrayOutputStream.toString(StandardCharsets.UTF_8));
    }
}