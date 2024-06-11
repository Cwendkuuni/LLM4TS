package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class XMLEncoderTest {

    @Test
    public void testXMLEncoderInstance() {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertNotNull(encoder);
        assertEquals("UTF-8", encoder.getEncoding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXMLEncoderInstanceWithNullEncoding() {
        XMLEncoder encoder = new XMLEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testXMLEncoderInstanceWithUnsupportedEncoding() {
        XMLEncoder encoder = new XMLEncoder("X-PASCAL");
    }

    @Test
    public void testDeclare() throws IOException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declare(out);
        String result = out.toString();
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n", result);
    }

    @Test
    public void testText() throws IOException, IndexOutOfBoundsException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");

        encoder.text(out, "abcdefghijk");
        String result = out.toString();
        assertEquals("abcdefghijk", result);

        char[] chars = "abc&defghijklmno\u00e9pqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_+-=[]{}|;:,.<>?/".toCharArray();
        encoder.text(out, chars, 0, chars.length);
        result = out.toString();
        assertEquals(new String(chars, 0, chars.length), result);

        encoder.text(out, '>');
        result = out.toString();
        assertEquals("abcdefghijk&amp;abc&defghijklmno&#231;pqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&amp;*()_+-=[]{}|;:,.<>?/&gt;", result);

        encoder.text(out, '&');
        result = out.toString();
        assertEquals("abcdefghijk&amp;abc&defghijklmno&#231;pqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&amp;*()_+-=[]{}|;:,.<>?/&gt;&amp;", result);

        encoder.text(out, '&', false);
        result = out.toString();
        assertEquals("abcdefghijk&amp;abc&defghijklmno&#231;pqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&amp;*()_+-=[]{}|;:,.<>?/&gt;&amp;", result);

        encoder.text(out, '&', true);
        result = out.toString();
        assertEquals("abcdefghijk&amp;amp;abc&defghijklmno&#231;pqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&amp;amp;*()_+-=[]{}|;:,.<>?/&gt;&amp;amp;", result);
    }

    @Test
    public void testTextWithSevenBitEncoding() throws IOException, IndexOutOfBoundsException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("ISO-8859-1");

        encoder.text(out, "abcdefghijk");
        String result = out.toString();
        assertEquals("abcdefghijk", result);

        char[] chars = "abc&defghijklmno\u00e9pqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_+-=[]{}|;:,.<>?/".toCharArray();
        encoder.text(out, chars, 0, chars.length);
        result = out.toString();
        assertEquals(new String(chars, 0, chars.length), result);

        encoder.text(out, '>');
        result = out.toString();
        assertEquals("abcdefghijk&amp;gt;", result);

        encoder.text(out, '&');
        result = out.toString();
        assertEquals("abcdefghijk&amp;amp;", result);

        encoder.text(out, '&', false);
        result = out.toString();
        assertEquals("abcdefghijk&amp;amp;", result);

        encoder.text(out, '&', true);
        result = out.toString();
        assertEquals("abcdefghijk&amp;amp;amp;", result);
    }

    @Test
    public void testTextSingleCharacter() throws IOException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");

        encoder.text(out, '!');
        String result = out.toString();
        assertEquals("&excl;", result);

        encoder.text(out, '&');
        result = out.toString();
        assertEquals("&amp;", result);

        encoder.text(out, '\'');
        result = out.toString();
        assertEquals("&apos;", result);

        encoder.text(out, '"');
        result = out.toString();
        assertEquals("&quot;", result);

        encoder.text(out, '<');
        result = out.toString();
        assertEquals("&lt;", result);
    }

    @Test
    public void testTextSingleCharacterWithSevenBitEncoding() throws IOException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("ISO-8859-1");

        encoder.text(out, '!');
        String result = out.toString();
        assertEquals("!", result);

        encoder.text(out, '&');
        result = out.toString();
        assertEquals("&amp;", result);

        encoder.text(out, '\'');
        result = out.toString();
        assertEquals("'", result);

        encoder.text(out, '"');
        result = out.toString();
        assertEquals("\"", result);

        encoder.text(out, '<');
        result = out.toString();
        assertEquals("&lt;", result);
    }

    @Test
    public void testWhitespace() throws IOException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");

        encoder.whitespace(out, "  \t\n\r\f");
        String result = out.toString();
        assertEquals(" \t\n\r\f", result);
    }

    @Test
    public void testAttribute() throws IOException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");

        encoder.attribute(out, "name", "value", '\"', false);
        String result = out.toString();
        assertEquals("<name>value</name>", result);

        encoder.attribute(out, "name", "value", '\'', false);
        result = out.toString();
        assertEquals("<name>value</name>", result);

        encoder.attribute(out, "name", "value", '\"', true);
        result = out.toString();
        assertEquals("<name value=\"value\"></name>", result);

        encoder.attribute(out, "name", "value", '\'', true);
        result = out.toString();
        assertEquals("<name value='value'></name>", result);

        encoder.attribute(out, "name", "value", '&', false);
        result = out.toString();
        assertEquals("<name>&amp;value;</name>", result);

        encoder.attribute(out, "name", "value", '&', true);
        result = out.toString();
        assertEquals("<name amp;value;</name>", result);

        encoder.attribute(out, "name", "value", '&', false, "UTF-16");
        result = out.toString();
        assertEquals("<name>&#x26;&#x61;&#x6d;&#x65;</name>", result);

        encoder.attribute(out, "name", "value", '&', true, "UTF-16");
        result = out.toString();
        assertEquals("<name &#x26;&#x22;&#x61;&#x6d;&#x65;&#x26;&#x22;></name>", result);
    }

    @Test
    public void testAttributeQuotationMark() throws IOException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");

        encoder.attribute(out, "name", "value\"value", '\"', false);
        String result = out.toString();
        assertEquals("<name>value\"value</name>", result);

        encoder.attribute(out, "name", "value'value", '\'', false);
        result = out.toString();
        assertEquals("<name>value'value</name>", result);

        encoder.attribute(out, "name", "value\"value", '\"', true);
        result = out.toString();
        assertEquals("<name value=\"value\"value></name>", result);

        encoder.attribute(out, "name", "value'value", '\'', true);
        result = out.toString();
        assertEquals("<name value='value'value></name>", result);
    }

    @Test
    public void testAttributeWithEscapedCharacter() throws IOException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");

        encoder.attribute(out, "name", "value&amp;value", '\"', false);
        String result = out.toString();
        assertEquals("<name>value&amp;value</name>", result);

        encoder.attribute(out, "name", "value&gt;value", '\"', false);
        result = out.toString();
        assertEquals("<name>value&gt;value</name>", result);

        encoder.attribute(out, "name", "value&apos;value", '\"', false);
        result = out.toString();
        assertEquals("<name>value&apos;value</name>", result);

        encoder.attribute(out, "name", "value&quot;value", '\"', false);
        result = out.toString();
        assertEquals("<name>value&quot;value</name>", result);
    }
}