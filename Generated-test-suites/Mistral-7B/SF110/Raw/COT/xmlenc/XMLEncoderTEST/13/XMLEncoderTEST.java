package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.junit.Test;

public class XMLEncoderTest {

    private XMLEncoder getEncoder(String encoding) {
        try {
            return XMLEncoder.getEncoder(encoding);
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetEncoder() {
        XMLEncoder encoder = getEncoder("UTF-8");
        assertEquals("UTF-8", encoder.getEncoding());

        encoder = getEncoder("UTF-16");
        assertEquals("UTF-16", encoder.getEncoding());

        encoder = getEncoder("US-ASCII");
        assertEquals("US-ASCII", encoder.getEncoding());

        encoder = getEncoder("ASCII");
        assertEquals("ASCII", encoder.getEncoding());

        encoder = getEncoder("ISO-8859-1");
        assertEquals("ISO-8859-1", encoder.getEncoding());

        assertThrows(UnsupportedEncodingException.class, () -> getEncoder("UnknownEncoding"));
    }

    @Test
    public void testDeclaration() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = getEncoder("UTF-8");
        encoder.declaration(writer);

        String xmlDeclaration = "<xml version=\"1.0\" encoding=\"" + StandardCharsets.UTF_8.displayName(Locale.ENGLISH) + "\"?";
        assertEquals(xmlDeclaration, writer.toString());
    }

    @Test
    public void testTextWithEscaping() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = getEncoder("UTF-8");

        encoder.text(writer, "&<>'\"&amp;#x27;\"", true);
        assertEquals("&amp;amp;lt;&amp;lt;&amp;gt;&amp;amp;quot;&amp;amp;#x27;&amp;amp;quot;", writer.toString());
    }

    @Test
    public void testTextWithoutEscaping() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = getEncoder("UTF-8");

        encoder.text(writer, "&<>'\"&", false);
        assertEquals("&<>'\"&", writer.toString());
    }

    @Test(expected = InvalidXMLException.class)
    public void testInvalidTextCharacter() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = getEncoder("UTF-8");

        encoder.text(writer, "\u0000", false);
    }

    @Test
    public void testTextWithSevenBitEncoding() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = getEncoder("ASCII");

        encoder.text(writer, "&<>'\"&", false);
        assertEquals("&amp;lt;&amp;lt;&amp;gt;&amp;amp;quot;&amp;amp;#32;", writer.toString());
    }

    @Test
    public void testTextWithWhitespace() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = getEncoder("UTF-8");

        encoder.whitespace(writer, " \t\r\n");
        assertEquals(" \t\r\n", writer.toString());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTextWithNegativeLength() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = getEncoder("UTF-8");

        encoder.text(writer, "abcdefgh".toCharArray(), -1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTextWithInvalidStartIndex() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = getEncoder("UTF-8");

        encoder.text(writer, "abcdefgh".toCharArray(), 10, 5);
    }

    @Test
    public void testAttribute() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = getEncoder("UTF-8");

        encoder.attribute(writer, "name", "value", '\"', true);
        assertEquals("<name value=\"value\"/>", writer.toString());

        encoder.attribute(writer, "name2", "value2", '\'', false);
        assertEquals("<name2 value2='value2'/>", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeWithInvalidQuotationMark() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = getEncoder("UTF-8");

        encoder.attribute(writer, "name", "value", 'Z', false);
    }

    @Test(expected = InvalidXMLException.class)
    public void testAttributeWithInvalidCharacter() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = getEncoder("UTF-8");

        encoder.attribute(writer, "name", "\u0000value", '\"', true);
    }
}