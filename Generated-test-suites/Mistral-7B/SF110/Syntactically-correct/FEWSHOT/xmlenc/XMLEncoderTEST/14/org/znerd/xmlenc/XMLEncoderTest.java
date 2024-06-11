package org.znerd.xmlenc;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        assertEquals(encoding, encoder.getEncoding());
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderInvalidEncoding() {
        String encoding = "invalidEncoding";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
    }

    @Test
    public void testDeclaration() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = new StringWriter();
        encoder.declaration(out);
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testTextNullWriter() {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        assertThrows(NullPointerException.class, () -> encoder.text((Writer) null, "text"));
    }

    @Test
    public void testTextNullText() {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = new StringWriter();
        encoder.text(out, (String) null);
    }

    @Test
    public void testTextEmptyString() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = new StringWriter();
        encoder.text(out, "");
        assertEquals("", out.toString());
    }

    @Test
    public void testTextSimpleText() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = new StringWriter();
        encoder.text(out, "text");
        assertEquals("text", out.toString());
    }

    @Test
    public void testTextEscapedCharacters1() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = new StringWriter();
        encoder.text(out, "<");
        assertEquals("&lt;", out.toString());
    }

    @Test
    public void testTextEscapedCharacters2() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = new StringWriter();
        encoder.text(out, ">");
        assertEquals("&gt;", out.toString());
    }

    @Test
    public void testTextEscapedCharacters3() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = new StringWriter();
        encoder.text(out, "&");
        assertEquals("&amp;", out.toString());
    }

    @Test
    public void testTextEscapedCharacters4() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = new StringWriter();
        encoder.text(out, "\u00A0");
        assertEquals("&quot;", out.toString());
    }

    @Test
    public void testTextEscapedCharacters5() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = new StringWriter();
        encoder.text(out, "\u00A1");
        assertEquals("&apos;", out.toString());
    }

    @Test
    public void testTextEscapedCharacterWithSevenBitEncoding() throws IOException {
        XMLEncoder encoder = new XMLEncoder("US-ASCII");
        Writer out = new StringWriter();
        encoder.text(out, "\u00A1");
        assertEquals("&#x00A1;", out.toString());
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextInvalidCharacter1() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = new StringWriter();
        encoder.text(out, "\u0080");
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextInvalidCharacter2() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = new StringWriter();
        encoder.text(out, "\u2000");
    }

    @Test
    public void testTextInvalidCharacter3() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = new StringWriter();
        encoder.text(out, "\uFFFF");
        assertEquals("&#xFFFF;", out.toString());
    }

    @Test
    public void testTextWithEscapeAmpersands() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = new StringWriter();
        encoder.text(out, "&amp;&amp;");
        assertEquals("&amp;&amp;", out.toString());
    }

    @Test
    public void testTextWithEscapeAmpersands2() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = new StringWriter();
        encoder.text(out, "&amp;<tag&amp;attr=\"value\" />");
        assertEquals("&amp;<tag&amp;amp;amp;gt;attr=&amp;quot;value&amp;quot; />", out.toString());
    }

    @Test
    public void testWhitespace() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = new StringWriter();
        encoder.whitespace(out, " ");
        assertEquals(" ", out.toString());
    }

    @Test
    public void testWhitespaceEmpty() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = new StringWriter();
        encoder.whitespace(out, "");
        assertEquals("", out.toString());
    }

    @Test
    public void testAttributeNullName() {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        assertThrows(NullPointerException.class, () -> encoder.attribute((Writer) null, null, "", ' ', false));
    }

    @Test
    public void testAttributeEmptyName() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = new StringWriter();
        encoder.attribute(out, "", "", ' ', false);
    }

    @Test
    public void testAttributeQuotationMarkMatch() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = new StringWriter();
        encoder.attribute(out, "name", "\"value\"", '\"', false);
        assertEquals("name=\"value\"", out.toString());
    }

    @Test
    public void testAttributeQuotationMarkMismatch() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = new StringWriter();
        encoder.attribute(out, "name", "value'", '\"', false);
        assertEquals("name=\"value\'\"", out.toString());
    }

    @Test
    public void testAttributeQuotationMarkMismatchEscaped() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = new StringWriter();
        encoder.attribute(out, "name", "value&apos;", '\"', false);
        assertEquals("name=\"value&apos;\"" , out.toString());
    }

    @Test
    public void testAttributeEscapedCharacters1() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = new StringWriter();
        encoder.attribute(out, "name", "&lt;", '\"', false);
        assertEquals("name=\"&lt;\"" , out.toString());
    }

    @Test
    public void testAttributeEscapedCharacters2() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = new StringWriter();
        encoder.attribute(out, "name", "&gt;", '\"', false);
        assertEquals("name=\"&gt;\"" , out.toString());
    }

    @Test
    public void testAttributeEscapedCharacters3() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = new StringWriter();
        encoder.attribute(out, "name", "&amp;", '\"', false);
        assertEquals("name=\"&amp;\"" , out.toString());
    }

    @Test
    public void testAttributeEscapedCharacters4() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = new StringWriter();
        encoder.attribute(out, "name", "&apos;", '\"', false);
        assertEquals("name=\"&apos;\"" , out.toString());
    }

    @Test
    public void testAttributeEscapedCharacterWithSevenBitEncoding() throws IOException {
        XMLEncoder encoder = new XMLEncoder("US-ASCII");
        Writer out = new StringWriter();
        encoder.attribute(out, "name", "\u00A1", '\"', false);
        assertEquals("name=\"&#x00A1;\"" , out.toString());
    }

    @Test
    public void testAttributeInvalidCharacter1() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = new StringWriter();
        encoder.attribute(out, "name", "\u0080", '\"', false);
        assertEquals("name=\"&#x0080;\"" , out.toString());
    }

    @Test(expected = InvalidXMLException.class)
    public void testAttributeInvalidCharacter2() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = new StringWriter();
        encoder.attribute(out, "name", "\u2000", '\"', false);
    }

    @Test
    public void testAttributeInvalidCharacter3() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = new StringWriter();
        encoder.attribute(out, "name", "\uFFFF", '\"', false);
        assertEquals("name=\"&#xFFFF;\"" , out.toString());
    }

    @Test
    public void testAttributeEscapeAmpersands() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = new StringWriter();
        encoder.attribute(out, "name", "&amp;&amp;", '\"', true);
        assertEquals("name=\"&amp;amp;\"" , out.toString());
    }

    @Test
    public void testAttributeEscapedCharactersWithEscapeAmpersands() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = new StringWriter();
        encoder.attribute(out, "name", "&lt;&gt;&amp;", '\"', true);
        assertEquals("name=\"&lt;&gt;&amp;amp;\"" , out.toString());
    }

    @Test
    public void testAttributeWithEmptyValue() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Writer out = new StringWriter();
        encoder.attribute(out, "name", "", '\"', false);
        assertEquals("name=\"" , out.toString());
    }
}