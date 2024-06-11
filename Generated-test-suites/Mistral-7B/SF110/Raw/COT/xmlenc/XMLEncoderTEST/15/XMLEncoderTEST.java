package org.znerd.xmlenc;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class XMLEncoderTest {

    private XMLEncoder encoder;
    private Writer outputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setup() {
        encoder = XMLEncoder.getEncoder("UTF-8");
        outputStream = new Writer() {
            private final ByteArrayOutputStream outStream = new ByteArrayOutputStream();

            @Override
            public void write(int c) throws IOException {
                outStream.write(c);
            }

            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                outStream.write(cbuf, off, len);
            }

            @Override
            public void flush() throws IOException {
                byteArrayOutputStream.flush();
            }

            @Override
            public void close() throws IOException {
                outStream.close();
            }
        };
        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = new PrintWriter(new Writer() {
            {
                write(byteArrayOutputStream);
            }

            @Override
            public void flush() throws IOException {
                flush();
            }

            @Override
            public void close() throws IOException {
                close();
            }
        }, StandardCharsets.UTF_8.name());
    }

    @Test
    public void testGetEncoder() {
        String encoding = "UTF-8";
        XMLEncoder xmlEncoder = XMLEncoder.getEncoder(encoding);
        assertEquals(encoding, xmlEncoder.getEncoding());
    }

    @Test
    public void testDeclaration() throws IOException {
        encoder.declaration(outputStream);
        String expectedOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String actualOutput = new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testText() throws IOException, InvalidXMLException {
        String text = "Hello, World!";
        encoder.text(outputStream, text, false);
        String expectedOutput = "Hello, World!";
        String actualOutput = new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testText_WithEscaping() throws IOException, InvalidXMLException {
        String text = "& < > ' " + (char) 157 + " \t";
        encoder.text(outputStream, text, true);
        String expectedOutput = "&amp; &lt; &gt; &apos; &apos; &apos;&#x9d;&#xa0;";
        String actualOutput = new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = InvalidXMLException.class)
    public void testText_InvalidCharacter() throws IOException {
        String text = "≤";
        encoder.text(outputStream, text, false);
        fail("Expected InvalidXMLException but did not receive it.");
    }

    @Test
    public void testWhitespace() throws IOException, InvalidXMLException {
        String whitespace = "   ";
        encoder.whitespace(outputStream, whitespace);
        String expectedOutput = "   ";
        String actualOutput = new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testAttribute() throws IOException, InvalidXMLException {
        String name = "name";
        String value = "value";
        encoder.attribute(outputStream, name, value, '\"', false);
        String expectedOutput = " name=\"value\"";
        String actualOutput = new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testAttribute_WithEscaping() throws IOException, InvalidXMLException {
        String name = "name";
        String value = "& < > ' \" &";
        encoder.attribute(outputStream, name, value, '\"', true);
        String expectedOutput = " name=\"&amp; &lt; &gt; &apos;&apos; &apos; &quot; &amp;\"";
        String actualOutput = new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);
        assertEquals(expectedOutput, actualOutput);
    }
}