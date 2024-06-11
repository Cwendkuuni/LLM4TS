package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;

public class XMLEncoderTest {
    private XMLEncoder encoder;
    private Writer writer;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        encoder = XMLEncoder.getEncoder("UTF-8");
        writer = new Writer() {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            @Override
            public void write(int c) throws IOException {
                out.write(c);
            }

            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                out.write(cbuf, off, len);
            }

            @Override
            public void flush() throws IOException {
                // No-op
            }

            @Override
            public void close() throws IOException {
                // No-op
            }

            public byte[] toByteArray() {
                byte[] result = out.toByteArray();
                out.reset();
                return result;
            }
        };
        outputStream = new ByteArrayOutputStream();
        writer = new PrintWriter(outputStream);
    }

    @Test
    public void testGetEncoder() {
        assertEquals("UTF-8", XMLEncoder.getEncoder("UTF-8").getEncoding());
        assertThrows(IllegalArgumentException.class, () -> XMLEncoder.getEncoder(null));
        assertThrows(UnsupportedEncodingException.class, () -> XMLEncoder.getEncoder("invalidEncoding"));
    }

    @Test
    public void testDeclaration() throws IOException {
        encoder.declaration(writer);
        String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        assertEquals(expectedDeclaration, new String(writer.toByteArray()));
    }

    @Test
    public void testTextWithEscaping() throws IOException, InvalidXMLException {
        encoder.text(writer, "abc<def>123&>4<5", true);
        String expectedResult = "abc&lt;def&gt;123&amp;4&lt;5";
        assertEquals(expectedResult, new String(writer.toByteArray()));
    }

    @Test
    public void testTextWithoutEscaping() throws IOException, InvalidXMLException {
        encoder.text(writer, "abc<def>123&>4<5", false);
        String expectedResult = "abc<def>123&>4<5";
        assertEquals(expectedResult, new String(writer.toByteArray()));
    }

    @Test
    public void testTextWithSevenBitEncoding() throws IOException, InvalidXMLException {
        XMLEncoder sevenBitEncoder = XMLEncoder.getEncoder("US-ASCII");
        sevenBitEncoder.text(writer, "é", false);
        String expectedResult = "&#233;";
        assertEquals(expectedResult, new String(writer.toByteArray()));
    }

    @Test
    public void testTextWithInvalidCharacter() throws IOException, InvalidXMLException {
        encoder.text(writer, "\u0080", false);
        string expectedErrorMessage = "The character 0x80 is not valid.";
        assertThrows(InvalidXMLException.class, () -> new String(writer.toByteArray()));
    }

    @Test
    public void testTextWithWhitespace() throws IOException, InvalidXMLException {
        encoder.text(writer, " hello world ", false);
        String expectedResult = " hello world ";
        assertEquals(expectedResult, new String(writer.toByteArray()));
    }

    @Test
    public void testWhitespaceWithInvalidCharacter() throws IOException, InvalidXMLException {
        encoder.whitespace(writer, "  é", false);
        String expectedErrorMessage = "The character 0x83 is not valid.";
        assertThrows(InvalidXMLException.class, () -> new String(writer.toByteArray()));
    }

    @Test
    public void testAttribute() throws IOException, InvalidXMLException {
        encoder.attribute(writer, "name", "value & '\"<>", true, '\"');
        String expectedResult = "name=\"value &amp; '\"<\"&gt;\"";
        assertEquals(expectedResult, new String(writer.toByteArray()));
    }

    @Test
    public void testAttributeWithQuotationMarkAsValue() throws IOException, InvalidXMLException {
        encoder.attribute(writer, "name", "value\"", true, '\"');
        String expectedResult = "name=\"value&quot;\"";
        assertEquals(expectedResult, new String(writer.toByteArray()));
    }
}