package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

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
        encoder = getEncoder("US-ASCII");
        assertEquals("US-ASCII", encoder.getEncoding());
    }

    @Test
    public void testDeclaration() throws IOException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = getEncoder("UTF-8");
        encoder.declaration(out);
        String declaration = out.toString();
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", declaration);
    }

    @Test
    public void testText_Simple() throws IOException, InvalidXMLException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = getEncoder("UTF-8");
        encoder.text(out, "Hello World!");
        assertEquals("Hello World!", out.toString());
    }

    @Test
    public void testText_Simple_Escape() throws IOException, InvalidXMLException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = getEncoder("UTF-8");
        encoder.text(out, "<foo bar=\"baz\"/>");
        assertEquals("<foo bar=\"baz\"/>", out.toString());
    }

    @Test
    public void testText_Complex() throws IOException, InvalidXMLException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = getEncoder("UTF-8");
        encoder.text(out, "&<>&\"'<>");
        assertEquals("&amp;&lt;&gt;&quot;&apos;", out.toString());
    }

    @Test
    public void testText_Complex_Escape() throws IOException, InvalidXMLException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = getEncoder("UTF-8");
        encoder.text(out, "&amp;<&gt;\"'&lt;", false);
        assertEquals("&amp;<&gt;\"'&lt;", out.toString());
    }

    @Test(expected = InvalidXMLException.class)
    public void testText_Invalid() throws IOException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = getEncoder("UTF-8");
        encoder.text(out, "\u0001");
    }

    @Test
    public void testText_Null() throws IOException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = getEncoder("UTF-8");
        encoder.text(out, null);
        assertNull(out.toString());
    }

    @Test
    public void testText_Empty() throws IOException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = getEncoder("UTF-8");
        encoder.text(out, "");
        assertEquals("", out.toString());
    }

    @Test
    public void testText_Whitespace() throws IOException, InvalidXMLException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = getEncoder("UTF-8");
        encoder.whitespace(out, "Hello World!");
        assertEquals("Hello World!", out.toString());
    }

    @Test
    public void testAttribute() throws IOException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = getEncoder("UTF-8");
        encoder.attribute(out, "name", "value", '"', true);
        assertEquals("<name value=\"value\"/>", out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttribute_InvalidQuotationMark() {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = getEncoder("UTF-8");
        encoder.attribute(out, "name", "value", '^', true);
    }

    @Test
    public void testAttribute_Null() throws IOException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = getEncoder("UTF-8");
        encoder.attribute(out, null, "value", '"', true);
        assertNull(out.toString());
    }

    @Test
    public void testAttribute_Empty() throws IOException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = getEncoder("UTF-8");
        encoder.attribute(out, "name", "", '"', true);
        assertEquals("<name/>", out.toString());
    }

    @Test
    public void testAttribute_Whitespace() throws IOException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = getEncoder("UTF-8");
        encoder.attribute(out, "name", " value", '"', true);
        assertEquals("<name> value</name>", out.toString());
    }

    @Test
    public void testAttribute_Escape() throws IOException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = getEncoder("UTF-8");
        encoder.attribute(out, "name", "&<>&\"'<> value", '"', true);
        assertEquals("<name>&amp;&lt;&gt;&quot;&apos; value</name>", out.toString());
    }

    @Test
    public void testAttribute_NoQuotationMark() throws IOException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = getEncoder("UTF-8");
        encoder.attribute(out, "name", "value", (char) 0, true);
        assertThrows(InvalidXMLException.class, () -> out.toString());
    }

    @Test
    public void testAttribute_SameQuotationMark() throws IOException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = getEncoder("UTF-8");
        encoder.attribute(out, "name", "value\"", '"', true);
        assertThrows(InvalidXMLException.class, () -> out.toString());
    }

}