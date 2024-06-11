package org.znerd.xmlenc;

import static org.junit.Assert.*;
import org.junit.Test;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        assertEquals(encoding, encoder.getEncoding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderNullEncoding() {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderUnsupportedEncoding() {
        XMLEncoder.getEncoder("ISO-12345");
    }

    @Test
    public void testDeclaration() throws IOException, NullPointerException {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        StringWriter writer = new StringWriter();
        encoder.declaration(writer);
        String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>";
        assertEquals(expectedDeclaration, writer.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testDeclarationNullWriter() {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        encoder.declaration(null);
    }

    @Test
    public void testText() throws IOException, IndexOutOfBoundsException, InvalidXMLException, NullPointerException {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        StringWriter writer = new StringWriter();

        // Test empty string
        encoder.text(writer, "");
        assertEquals("", writer.toString());

        // Test single character
        encoder.text(writer, 'A');
        assertEquals("A", writer.toString());

        // Test single character with escape
        encoder.text(writer, '&', true);
        assertEquals("&amp;", writer.toString());

        // Test multiple characters
        encoder.text(writer, "Hello World!");
        assertEquals("Hello World!", writer.toString());

        // Test multiple characters with escape
        encoder.text(writer, "Hello World!", true);
        assertEquals("Hello World!", writer.toString());
        assertEquals("Hello World!", writer.toString());

        // Test invalid characters
        char[] invalidChars = {'<', '>', '&', '\'', '\"', '\n', '\r', '=', '\t'};
        for (char c : invalidChars) {
            writer.getBuffer().setLength(0);
            encoder.text(writer, Character.toString(c));
            assertEquals("&lt;", writer.toString()); // Replace with the actual encoded character
        }

        // Test '\r' and '\n'
        writer.getBuffer().setLength(0);
        encoder.text(writer, "\r\n");
        assertEquals("&cr;&n;", writer.toString());

        // Test non-ASCII character
        char nonAsciiChar = '\u00E9';
        writer.getBuffer().setLength(0);
        encoder.text(writer, Character.toString(nonAsciiChar));
        assertEquals("&Atilde;", writer.toString());

        // Test three-byte UTF-8 character
        char threeByteChar = '\u00A9';
        writer.getBuffer().setLength(0);
        encoder.text(writer, Character.toString(threeByteChar));
        assertEquals("&copy;", writer.toString());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTextNegativeLength() throws IOException, InvalidXMLException, NullPointerException {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        StringWriter writer = new StringWriter();
        encoder.text(writer, "Test".toCharArray(), -1, 1);
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextInvalidCharacter() throws IOException, IndexOutOfBoundsException, NullPointerException {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        StringWriter writer = new StringWriter();
        encoder.text(writer, (char) 0xFFFD);
    }

    @Test
    public void testTextWhitespace() throws IOException, InvalidXMLException, NullPointerException {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        StringWriter writer = new StringWriter();

        // Test empty string
        encoder.whitespace(writer, "");
        assertEquals("", writer.toString());

        // Test single character
        encoder.whitespace(writer, " ");
        assertEquals(" ", writer.toString());

        // Test multiple characters
        encoder.whitespace(writer, " Hello World!");
        assertEquals(" Hello World!", writer.toString());

        // Test invalid characters
        char[] invalidChars = {'<', '>', '&', '\'', '\"', '\n', '\r', '=', '\t'};
        for (char c : invalidChars) {
            writer.getBuffer().setLength(0);
            encoder.whitespace(writer, Character.toString(c));
            assertEquals("", writer.toString());
        }
    }

    @Test
    public void testAttribute() throws IOException, InvalidXMLException, NullPointerException {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        StringWriter writer = new StringWriter();

        // Test empty string for name and value
        encoder.attribute(writer, "", "", '\"');
        assertEquals(" xml:name=\"\" xmlns:xsi=\"\" xmlns:xsi:schemaLocation=\"\" xmlns:xml=\"\" xml:space=\"preserve\"", writer.toString());

        // Test single character for name and value
        encoder.attribute(writer, "name", "A", '\"');
        assertEquals(" xml:name=\"name\" xmlns:xsi=\"\" xmlns:xsi:schemaLocation=\"\" xmlns:xml=\"\" xml:space=\"preserve\">A</attribute>", writer.toString());

        // Test multiple characters for name and value
        encoder.attribute(writer, "name", "Hello World!", '\"');
        assertEquals(" xml:name=\"name\" xmlns:xsi=\"\" xmlns:xsi:schemaLocation=\"\" xmlns:xml=\"\" xml:space=\"preserve\">Hello World!</attribute>", writer.toString());

        // Test escape characters for value
        encoder.attribute(writer, "name", "&lt;&gt;&amp;&apos;&quot;&#x00FF;&#xA0;", '\"');
        assertEquals(" xml:name=\"name\" xmlns:xsi=\"\" xmlns:xsi:schemaLocation=\"\" xmlns:xml=\"\" xml:space=\"preserve\">&lt;&gt;&amp;&apos;&quot;&#x00FF;&#xA0;</attribute>", writer.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeNullName() throws IOException, InvalidXMLException {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        StringWriter writer = new StringWriter();
        encoder.attribute(writer, null, "value", '\"');
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeNullValue() throws IOException, InvalidXMLException {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        StringWriter writer = new StringWriter();
        encoder.attribute(writer, "name", null, '\"');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeInvalidQuotationMark() throws IOException, InvalidXMLException, NullPointerException {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        StringWriter writer = new StringWriter();
        encoder.attribute(writer, "name", "value", (char) 0x01);
    }
}