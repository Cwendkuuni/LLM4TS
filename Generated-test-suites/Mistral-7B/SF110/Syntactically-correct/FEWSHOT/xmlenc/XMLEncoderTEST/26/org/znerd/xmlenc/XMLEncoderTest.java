package org.znerd.xmlenc;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        assertEquals(encoding, encoder.getEncoding());
        assertFalse(encoder._sevenBitEncoding);
    }

    @Test
    public void testGetEncoderInvalidEncoding() {
        String encoding = "invalidEncoding";
        assertThrows(UnsupportedEncodingException.class, () -> XMLEncoder.getEncoder(encoding));
    }

    @Test
    public void testGetEncoderNullEncoding() {
        assertThrows(IllegalArgumentException.class, () -> XMLEncoder.getEncoder(null));
    }

    @Test
    public void testDeclaration() throws IOException, NullPointerException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(out);
        String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        assertEquals(expectedDeclaration, out.toString());
    }

    @Test
    public void testTextSimpleCharacters() throws IOException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(out, "abc", false);
        assertEquals("abc", out.toString());
    }

    @Test
    public void testTextEscapedCharacters() throws IOException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(out, "&<>'\"\n\t", false);
        String expected = "&amp;&lt;>&gt;&apos;&quot;\n\t";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testTextEscapedCharactersWithEscapeAmpersands() throws IOException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(out, "&<>'\"\n\t", true);
        String expected = "&amp;&amp;lt;&amp;gt;&amp;apos;&amp;quot;\n\t";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testTextInvalidCharacters() throws IOException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(out, "┤", false);
        assertThrows(InvalidXMLException.class, () -> out.toString());
    }

    @Test
    public void testTextInvalidCharactersWithSevenBitEncoding() throws IOException, InvalidXMLException {
        XMLEncoder encoder = XMLEncoder.getEncoder("US-ASCII");
        StringWriter out = new StringWriter();
        encoder.text(out, "┤", false);
        String expected = "&#x{2573};";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testTextInvalidCharactersWithSevenBitEncodingAndEscapeAmpersands() throws IOException, InvalidXMLException {
        XMLEncoder encoder = XMLEncoder.getEncoder("US-ASCII");
        StringWriter out = new StringWriter();
        encoder.text(out, "┤", true);
        String expected = "&amp;#x{2573};";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testTextEscapedInvalidCharacter() throws IOException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(out, "&#x{2573};", false);
        assertThrows(InvalidXMLException.class, () -> out.toString());
    }

    @Test
    public void testTextEscapedInvalidCharacterWithSevenBitEncoding() throws IOException, InvalidXMLException {
        XMLEncoder encoder = XMLEncoder.getEncoder("US-ASCII");
        StringWriter out = new StringWriter();
        encoder.text(out, "&#x{2573};", false);
        String expected = "&#x{2573};";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testTextEscapedInvalidCharacterWithSevenBitEncodingAndEscapeAmpersands() throws IOException, InvalidXMLException {
        XMLEncoder encoder = XMLEncoder.getEncoder("US-ASCII");
        StringWriter out = new StringWriter();
        encoder.text(out, "&amp;#x{2573};", false);
        String expected = "&#x{2573};";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testTextInvalidCharacterWithSevenBitEncoding() throws IOException, InvalidXMLException {
        XMLEncoder encoder = XMLEncoder.getEncoder("US-ASCII");
        StringWriter out = new StringWriter();
        encoder.text(out, "┤", false);
        String expected = "&#x{2573};";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testTextInvalidCharacterWithSevenBitEncodingAndEscapeAmpersands() throws IOException, InvalidXMLException {
        XMLEncoder encoder = XMLEncoder.getEncoder("US-ASCII");
        StringWriter out = new StringWriter();
        encoder.text(out, "&amp;#x{2573};", false);
        String expected = "&#x{2573};";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testWhitespace() throws IOException, NullPointerException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.whitespace(out, " \t\n\r\f");
        assertEquals("\n \t\n", out.toString());
    }

    @Test
    public void testAttributeSimpleQuotationMark() throws IOException, NullPointerException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.attribute(out, "name", "value\"", '\"', false);
        String expected = " name=\"value\"";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testAttributeSimpleQuotationMarkWithEscapeAmpersands() throws IOException, NullPointerException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.attribute(out, "name", "value&quot;", '\"', false);
        String expected = " name=\"value&quot;\"";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testAttributeQuotationMarkMismatch() throws IOException, NullPointerException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.attribute(out, "name", "value'", '\"', false);
        String expected = "<error>";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testAttributeQuotationMarkMismatchWithEscapeAmpersands() throws IOException, NullPointerException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.attribute(out, "name", "value&apos;", '\"', false);
        String expected = "<error>";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testAttributeSimpleQuotationMarkAndEscapeAmpersands() throws IOException, NullPointerException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.attribute(out, "name", "value&quot;\"", '\"', true);
        String expected = " name=\"value&quot;\"";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testAttributeQuotationMarkAndEscapeAmpersands() throws IOException, NullPointerException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.attribute(out, "name", "value&apos;&quot;&amp;&gt;", '\"', true);
        String expected = " name=\"value&apos;&#x22;&amp;&gt;\"";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testAttributeEmpty() throws IOException, NullPointerException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.attribute(out, "name", "", '\"', false);
        String expected = " name=\"\"";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testAttributeEmptyWithQuotationMark() throws IOException, NullPointerException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.attribute(out, "name", "", '\'', false);
        assertNull(out.toString());
    }

    @Test
    public void testAttributeEmptyWithQuotationMarkAndEscapeAmpersands() throws IOException, NullPointerException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.attribute(out, "name", "", '\'', true);
        assertNull(out.toString());
    }

    @Test
    public void testAttributeWithInvalidQuotationMark() throws IOException, NullPointerException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertThrows(IllegalArgumentException.class, () -> encoder.attribute(out, "name", "value", '\u0001', false));
    }

    @Test
    public void testAttributeWithInvalidQuotationMarkAndEscapeAmpersands() throws IOException, NullPointerException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertThrows(IllegalArgumentException.class, () -> encoder.attribute(out, "name", "value", '\u0001', true));
    }
}