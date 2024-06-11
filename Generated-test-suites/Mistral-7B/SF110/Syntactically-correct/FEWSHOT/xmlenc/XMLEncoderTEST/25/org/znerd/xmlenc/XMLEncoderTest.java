package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.StringWriter;
import java.nio.charset.UnsupportedEncodingException;

import org.junit.Test;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() throws UnsupportedEncodingException {
        assertEquals(XMLEncoder.class, XMLEncoder.getEncoder("UTF-8").getClass());
        assertEquals(XMLEncoder.class, XMLEncoder.getEncoder("UTF-16").getClass());
        assertThrows(UnsupportedEncodingException.class, () -> XMLEncoder.getEncoder("InvalidEncoding"));
    }

    @Test
    public void testDeclaration() throws IOException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.declaration(out);
        assertEquals("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n", out.toString());
    }

    @Test
    public void testTextWithEscaping() throws IOException, InvalidXMLException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(out, "<foo&bar>", true);
        assertEquals("&lt;foo&amp;&amp;bar&gt;\n", out.toString());
    }

    @Test
    public void testTextWithNoEscaping() throws IOException, InvalidXMLException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(out, "<foo&bar>", false);
        assertEquals("<foo&bar>\n", out.toString());
    }

    @Test
    public void testTextWithSevenBitEncoding() throws IOException, InvalidXMLException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("US-ASCII");
        encoder.text(out, "öäéï", false);
        assertEquals("&#246;&#228;&#224;&#235;&#237;\n", out.toString());
    }

    @Test
    public void testTextWithInvalidCharacter() throws IOException, InvalidXMLException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(out, "\u0001", false);
        assertThrows(InvalidXMLException.class, () -> encoder.text(out, "\u0001", false));
    }

    @Test
    public void testTextWithInvalidCharacterEscaping() throws IOException, InvalidXMLException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(out, "\u0001", true);
        assertEquals("&#x0001;\n", out.toString());
    }

    @Test
    public void testWhitespace() throws IOException, InvalidXMLException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.whitespace(out, " foo ");
        assertEquals(" foo \n", out.toString());
    }

    @Test
    public void testAttribute() throws IOException, InvalidXMLException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.attribute(out, "name", "value\"value\"", '"', true);
        assertEquals("name=\"value&quot;value&quot;\n", out.toString());
    }

    @Test
    public void testAttributeWithInvalidQuotationMark() throws IOException, InvalidXMLException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        assertThrows(IllegalArgumentException.class, () -> encoder.attribute(out, "name", "value'value'", '\''));
    }
}