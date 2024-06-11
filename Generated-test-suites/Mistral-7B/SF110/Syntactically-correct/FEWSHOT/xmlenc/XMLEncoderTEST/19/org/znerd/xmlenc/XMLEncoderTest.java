package org.znerd.xmlenc;

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

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderNullEncoding() {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderUnsupportedEncoding() {
        String encoding = "XYZ-123";
        XMLEncoder.getEncoder(encoding);
    }

    @Test
    public void testDeclaration() throws IOException, NullPointerException {
        String encoding = "UTF-8";
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        encoder.declaration(out);
        String xmlDeclaration = out.toString();
        assertEquals("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>", xmlDeclaration);
    }

    @Test
    public void testDeclarationNullWriter() {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        assertThrows(NullPointerException.class, () -> encoder.declaration(null));
    }

    @Test
    public void testTextSimple() throws IOException, NullPointerException, InvalidXMLException {
        String encoding = "UTF-8";
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        encoder.text(out, "Hello World");
        assertEquals("Hello World", out.toString());
    }

    @Test
    public void testTextEscaped() throws IOException, NullPointerException, InvalidXMLException {
        String encoding = "UTF-8";
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        encoder.text(out, "Hello & World");
        assertEquals("Hello &amp; World", out.toString());
    }

    @Test
    public void testTextWithQuotationMarks() throws IOException, NullPointerException, InvalidXMLException {
        String encoding = "UTF-8";
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        encoder.text(out, "Hello \"World\"");
        assertEquals("Hello &quot;World&quot;", out.toString());
    }

    @Test
    public void testTextWithAmpersands() throws IOException, NullPointerException, InvalidXMLException {
        String encoding = "UTF-8";
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        encoder.text(out, "Hello & Apple");
        assertEquals("Hello &amp; Apple", out.toString());
    }

    @Test
    public void testTextWithControlChars() throws IOException, NullPointerException, InvalidXMLException {
        String encoding = "UTF-8";
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        encoder.text(out, "\u0001Hello");
        assertEquals("\u0001Hello", out.toString());
    }

    @Test
    public void testTextWithQuotationMarkAndControlChars() throws IOException, NullPointerException, InvalidXMLException {
        String encoding = "UTF-8";
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        encoder.text(out, "\"\u0001Hello");
        assertEquals("&quot;&\u0001Hello", out.toString());
    }

    @Test
    public void testTextWithControlCharsAndAmpersands() throws IOException, NullPointerException, InvalidXMLException {
        String encoding = "UTF-8";
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        encoder.text(out, "\u0001&Apple");
        assertEquals("&amp;\u0001&amp;Apple", out.toString());
    }

    @Test
    public void testTextWithLineBreaks() throws IOException, NullPointerException, InvalidXMLException {
        String encoding = "UTF-8";
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        encoder.text(out, "\nHello\nWorld");
        assertEquals("\nHello\nWorld", out.toString());
    }

    @Test
    public void testTextWithLineBreaksAndQuotationMarks() throws IOException, NullPointerException, InvalidXMLException {
        String encoding = "UTF-8";
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        encoder.text(out, "\n\"Hello\"\nWorld");
        assertEquals("\n&quot;Hello&quot;\nWorld", out.toString());
    }

    @Test
    public void testTextWithLineBreaksAndAmpersands() throws IOException, NullPointerException, InvalidXMLException {
        String encoding = "UTF-8";
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        encoder.text(out, "\nHello&Apple\nWorld");
        assertEquals("\nHello&amp;Apple\nWorld", out.toString());
    }

    @Test
    public void testTextWithLineBreaksAndQuotationMarksAndAmpersands() throws IOException, NullPointerException, InvalidXMLException {
        String encoding = "UTF-8";
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        encoder.text(out, "\n\"Hello&Apple\"\nWorld");
        assertEquals("\n&quot;Hello&amp;Apple&quot;\nWorld", out.toString());
    }

    @Test
    public void testTextWithInvalidChars() throws IOException, NullPointerException, InvalidXMLException {
        String encoding = "UTF-8";
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        byte[] invalidChars = {0x00, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f};
        CharArrayWriter writer = new CharArrayWriter();
        encoder.text(writer, invalidChars, 0, invalidChars.length);
        String xmlText = writer.toString();
        assertThrows(InvalidXMLException.class, () -> encoder.text(out, invalidChars, 0, invalidChars.length));
        assertEquals(xmlText, out.toString());
    }

    @Test
    public void testTextWithInvalidCharsAndQuotationMarks() throws IOException, NullPointerException, InvalidXMLException {
        String encoding = "UTF-8";
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        byte[] invalidChars = {0x00, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f};
        CharArrayWriter writer = new CharArrayWriter();
        encoder.text(writer, invalidChars, 0, invalidChars.length);
        String xmlText = writer.toString();
        assertThrows(InvalidXMLException.class, () -> encoder.text(out, invalidChars, 0, invalidChars.length, true));
        assertEquals(xmlText, out.toString());
    }

    @Test
    public void testTextWithInvalidCharsAndAmpersands() throws IOException, NullPointerException, InvalidXMLException {
        String encoding = "UTF-8";
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        byte[] invalidChars = {0x00, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f};
        CharArrayWriter writer = new CharArrayWriter();
        encoder.text(writer, invalidChars, 0, invalidChars.length);
        String xmlText = writer.toString();
        assertThrows(InvalidXMLException.class, () -> encoder.text(out, invalidChars, 0, invalidChars.length, false));
        assertEquals(xmlText, out.toString());
    }

    @Test
    public void testTextWithInvalidCharsAndQuotationMarksAndAmpersands() throws IOException, NullPointerException, InvalidXMLException {
        String encoding = "UTF-8";
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        byte[] invalidChars = {0x00, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f};
        CharArrayWriter writer = new CharArrayWriter();
        encoder.text(writer, invalidChars, 0, invalidChars.length);
        String xmlText = writer.toString();
        assertThrows(InvalidXMLException.class, () -> encoder.text(out, invalidChars, 0, invalidChars.length, true, true));
        assertEquals(xmlText, out.toString());
    }

    @Test
    public void testWhitespace() throws IOException, NullPointerException, InvalidXMLException {
        String encoding = "UTF-8";
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        encoder.whitespace(out, " Hello World ");
        assertEquals(" Hello World ", out.toString());
    }

    @Test
    public void testAttribute() throws IOException, NullPointerException, InvalidXMLException {
        String encoding = "UTF-8";
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        String name = "name";
        String value = "value";
        char quotationMark = '\"';
        boolean escapeAmpersands = true;
        encoder.attribute(out, name, value, quotationMark, escapeAmpersands);
        String xmlAttribute = out.toString();
        assertEquals("<" + name + "=" + quotationMark + value + quotationMark + ">", xmlAttribute);
    }

    @Test
    public void testAttributeWithEmptyValue() throws IOException, NullPointerException, InvalidXMLException {
        String encoding = "UTF-8";
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        String name = "name";
        char quotationMark = '\"';
        boolean escapeAmpersands = true;
        encoder.attribute(out, name, "", quotationMark, escapeAmpersands);
        String xmlAttribute = out.toString();
        assertEquals("<" + name + "/>", xmlAttribute);
    }

    @Test
    public void testAttributeWithQuotationMarksInValue() throws IOException, NullPointerException, InvalidXMLException {
        String encoding = "UTF-8";
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        String name = "name";
        String value = "value \"value\"";
        char quotationMark = '\"';
        boolean escapeAmpersands = true;
        encoder.attribute(out, name, value, quotationMark, escapeAmpersands);
        String xmlAttribute = out.toString();
        assertEquals("<" + name + "=" + quotationMark + value + quotationMark + ">", xmlAttribute);
    }

    @Test
    public void testAttributeWithAmpersandsInValue() throws IOException, NullPointerException, InvalidXMLException {
        String encoding = "UTF-8";
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        String name = "name";
        String value = "value &amp; value";
        char quotationMark = '\"';
        boolean escapeAmpersands = true;
        encoder.attribute(out, name, value, quotationMark, escapeAmpersands);
        String xmlAttribute = out.toString();
        assertEquals("<" + name + "=" + quotationMark + value + quotationMark + ">", xmlAttribute);
    }

    @Test
    public void testAttributeWithQuotationMarkAndAmpersandsInValue() throws IOException, NullPointerException, InvalidXMLException {
        String encoding = "UTF-8";
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        String name = "name";
        String value = "value \"&amp;value\"";
        char quotationMark = '\"';
        boolean escapeAmpersands = true;
        encoder.attribute(out, name, value, quotationMark, escapeAmpersands);
        String xmlAttribute = out.toString();
        assertEquals("<" + name + "=" + quotationMark + value + quotationMark + ">", xmlAttribute);
    }

    @Test
    public void testAttributeWithInvalidValue() throws IOException, NullPointerException, InvalidXMLException {
        String encoding = "UTF-8";
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        String name = "name";
        String value = "<script>alert('Hello, World!')</script>";
        char quotationMark = '\"';
        boolean escapeAmpersands = true;
        assertThrows(InvalidXMLException.class, () -> encoder.attribute(out, name, value, quotationMark, escapeAmpersands));
    }
}