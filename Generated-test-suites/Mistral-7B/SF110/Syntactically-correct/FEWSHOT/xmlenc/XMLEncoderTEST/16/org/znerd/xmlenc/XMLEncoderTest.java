package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class XMLEncoderTest {

    @Test
    public void testEncoderInstance() {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertNull(encoder._sevenBitEncoding);
        assertEquals("UTF-8", encoder.getEncoding());
    }

    @Test
    public void testEncoderInstanceWithInvalidEncoding() {
        assertThrows(UnsupportedEncodingException.class, () -> XMLEncoder.getEncoder("XYZ"));
    }

    @Test
    public void testEncoderInstanceWithNullEncoding() {
        assertThrows(IllegalArgumentException.class, () -> XMLEncoder.getEncoder(null));
    }

    @Test
    public void testDeclaration() throws IOException, NullPointerException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.declaration(writer);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n", writer.toString());
    }

    @Test
    public void testTextEncodingWithSevenBit() throws IOException, InvalidXMLException, NullPointerException {
        XMLEncoder encoder = new XMLEncoder("ASCII");
        StringWriter writer = new StringWriter();
        encoder.text(writer, "test < 127", true);
        assertEquals("test &lt; 127", writer.toString());
    }

    @Test
    public void testTextEncodingWithUTF8() throws IOException, InvalidXMLException, NullPointerException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.text(writer, "test < 127", true);
        assertEquals("test < 127", writer.toString());
    }

    @Test
    public void testTextEncodingWithInvalidCharacter() throws IOException, InvalidXMLException, NullPointerException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.text(writer, "test ☻", true);
        assertEquals("test &#x263A;", writer.toString());
    }

    @Test
    public void testTextEncodingWithEscapeAmpersands() throws IOException, InvalidXMLException, NullPointerException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.text(writer, "test &amp; test", true);
        assertEquals("test &amp; amp; test", writer.toString());
    }

    @Test
    public void testTextEncodingWithoutEscapeAmpersands() throws IOException, InvalidXMLException, NullPointerException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.text(writer, "test & test", false);
        assertEquals("test & test", writer.toString());
    }

    @Test
    public void testTextEncodingWithInvalidCharacterAndEscapeAmpersands() throws IOException, InvalidXMLException, NullPointerException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.text(writer, "test ☻ & test", true);
        assertEquals("test &#x263A; &amp; test", writer.toString());
    }

    @Test
    public void testTextEncodingWithNullText() throws IOException, NullPointerException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.text(writer, null, true);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTextEncodingWithEmptyText() throws IOException, NullPointerException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.text(writer, "", true);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTextEncodingWithWhitespace() throws IOException, InvalidXMLException, NullPointerException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        char[] whitespace = " \t\r\n\f".toCharArray();
        encoder.whitespace(writer, whitespace);
        assertEquals(" \t\r\n\f", writer.toString());
    }

    @Test
    public void testAttribute() throws IOException, NullPointerException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.attribute(writer, "name", "value", '"', true);
        assertEquals("<name value=\"value\"/>", writer.toString());
    }

    @Test
    public void testAttributeWithEscapeQuotationMark() throws IOException, NullPointerException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.attribute(writer, "name", "value\" value", '"', true);
        assertEquals("<name value=\">value value</value>", writer.toString());
    }

    @Test
    public void testAttributeWithEscapeAmpersands() throws IOException, NullPointerException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.attribute(writer, "name", "value &", '"', true);
        assertEquals("<name value=" + XMLEncoder.ESC_AMPERSAND + ">&</name>", writer.toString());
    }

    @Test
    public void testAttributeWithNullName() throws IOException, NullPointerException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        String name = null;
        encoder.attribute(writer, name, "value", '"', true);
        assertEquals("", writer.toString());
    }

    @Test
    public void testAttributeWithEmptyName() throws IOException, NullPointerException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        String name = "";
        encoder.attribute(writer, name, "value", '"', true);
        assertEquals("", writer.toString());
    }

    @Test
    public void testAttributeWithNullValue() throws IOException, NullPointerException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.attribute(writer, "name", null, '"', true);
        assertEquals("", writer.toString());
    }
}