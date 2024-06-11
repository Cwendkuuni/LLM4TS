package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertEquals("UTF-8", encoder.getEncoding());

        assertThrows(IllegalArgumentException.class, () -> XMLEncoder.getEncoder(null));

        assertThrows(UnsupportedEncodingException.class, () -> XMLEncoder.getEncoder("ISO-8859-27"));
    }

    @Test
    public void testDeclaration() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
    }

    @Test
    public void testTextSimple() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, "abc");
        assertEquals("abc", writer.toString());
    }

    @Test
    public void testTextEscaped() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, "&<>\"'");
        assertEquals("&amp;&lt;&gt;&quot;&apos;", writer.toString());
    }

    @Test
    public void testTextInvalidCharacter() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, '\u00E9'); // Grave accent (í)
        assertThrows(InvalidXMLException.class, () -> writer.toString());
    }

    @Test
    public void testTextSevenBitEncoding() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("ASCII");
        encoder.text(writer, "é"); // É character
        assertEquals("&amp;#233;", writer.toString());
    }

    @Test
    public void testTextWithEscapeAmpersands() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, "&amp;<>&\"'");
        assertEquals("&amp;&amp;lt;&amp;gt;&quot;&apos;", writer.toString());
    }

    @Test
    public void testTextWithQuotationMark() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, "\"");
        encoder.text(writer, "&quot;");
        assertEquals("&quot;&quot;", writer.toString());
    }

    @Test
    public void testTextWithAttribute() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, "<tag attr=\"value\" />");
        assertEquals("<tag attr=\"value\" />", writer.toString());
    }

    @Test
    public void testTextWithAttributeEscaped() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, "<tag attr=\"&amp;value\" />");
        assertEquals("<tag attr=\"&amp;value\" />", writer.toString());
    }

    @Test
    public void testTextWithAttributeQuotationMark() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, "<tag attr=\"value\"");
        encoder.attribute(writer, "attr", "value", '"', false);
        encoder.text(writer, " />");
        assertEquals("<tag attr=\"value\" />", writer.toString());
    }

    @Test
    public void testTextWithAttributeQuotationMarkEscaped() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, "<tag attr=\"value\"");
        encoder.attribute(writer, "attr", "value", '"', true);
        encoder.text(writer, " />");
        assertEquals("<tag attr=\"value\" />", writer.toString());
    }

    @Test
    public void testTextWithAttributeQuotationMarkSingleQuote() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, "<tag attr='value'/>");
        encoder.attribute(writer, "attr", "value", '\'', false);
        encoder.text(writer, " />");
        assertEquals("<tag attr='value'/>", writer.toString());
    }

    @Test
    public void testTextWithAttributeQuotationMarkSingleQuoteEscaped() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, "<tag attr='value'");
        encoder.attribute(writer, "attr", "value", '\'', true);
        encoder.text(writer, " />");
        assertEquals("<tag attr='value'/>", writer.toString());
    }
}