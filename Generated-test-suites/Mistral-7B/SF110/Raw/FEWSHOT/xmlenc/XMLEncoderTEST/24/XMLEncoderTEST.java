package org.znerd.xmlenc;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        assertEquals(encoding, encoder.getEncoding());

        encoding = "US-ASCII";
        encoder = XMLEncoder.getEncoder(encoding);
        assertEquals(encoding, encoder.getEncoding());

        encoding = "ISO-8859-1";
        encoder = XMLEncoder.getEncoder(encoding);
        assertEquals(encoding, encoder.getEncoding());

        encoding = null;
        try {
            encoder = XMLEncoder.getEncoder(encoding);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }

        encoding = "invalidEncoding";
        try {
            encoder = XMLEncoder.getEncoder(encoding);
            fail("UnsupportedEncodingException expected");
        } catch (UnsupportedEncodingException e) {
            // expected
        }
    }

    @Test
    public void testDeclaration() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        String xmlDeclaration = writer.toString();
        String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        assertEquals(expectedDeclaration, xmlDeclaration);
    }

    @Test
    public void testTextWithEscapeAmpersandsTrue() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, "<test&result=passed>", true);
        String text = writer.toString();
        String expectedText = "&lt;test&amp;&amp;result=passed&gt;";
        assertEquals(expectedText, text);
    }

    @Test
    public void testTextWithEscapeAmpersandsFalse() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, "<test&result=passed>", false);
        String text = writer.toString();
        String expectedText = "<test&result=passed>";
        assertEquals(expectedText, text);
    }

    @Test
    public void testTextWithSingleQuotes() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, "I'm single", false);
        String text = writer.toString();
        String expectedText = "I&apos;m single";
        assertEquals(expectedText, text);
    }

    @Test
    public void testTextWithDoubleQuotes() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, "I\"m double", false);
        String text = writer.toString();
        String expectedText = "I&quot;m double";
        assertEquals(expectedText, text);
    }

    @Test
    public void testTextWithControlChars() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        byte[] controlChars = {0x00, 0x07, 0x0B, 0x0E, 0x10, 0x14};
        StringBuilder sb = new StringBuilder();
        for (byte c : controlChars) {
            sb.append("0x").append(Integer.toHexString(c));
            sb.append(" ");
        }
        String controlCharsString = sb.toString();
        encoder.text(writer, new String(controlChars, StandardCharsets.UTF_8), false);
        String text = writer.toString();
        String expectedText = "<![CDATA[" + controlCharsString + "]]>";
        assertEquals(expectedText, text);
    }

    @Test
    public void testTextWithInvalidChars() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        char invalidChar = 0x0F; // Control character that is not valid in XML (it has a different bit pattern than 0x0F in ASCII)
        encoder.text(writer, new Character(invalidChar), false);
        try {
            writer.toString();
            fail("InvalidXMLException expected");
        } catch (InvalidXMLException e) {
            // expected
        }
    }

    @Test
    public void testWhitespace() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        String whitespace = "   tab  \n\r\t";
        encoder.whitespace(writer, whitespace);
        String text = writer.toString();
        String expectedText = "   \t\n\r\t";
        assertEquals(expectedText, text);
    }

    @Test
    public void testAttributeWithQuotes() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        String name = "name";
        String value = "value\"quoted";
        char quotationMark = '\"';
        encoder.attribute(writer, name, value, quotationMark, true);
        String xmlAttribute = writer.toString();
        String expectedAttribute = "<" + name + " " + name + "=\"" + value + "\" />";
        assertEquals(expectedAttribute, xmlAttribute);
    }

    @Test
    public void testAttributeWithoutQuotes() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        String name = "name";
        String value = "value";
        char quotationMark = '\u0000';
        encoder.attribute(writer, name, value, quotationMark, false);
        String xmlAttribute = writer.toString();
        String expectedAttribute = "<" + name + "=" + value + " />";
        assertEquals(expectedAttribute, xmlAttribute);
    }

    @Test
    public void testAttributeWithInvalidQuotationMark() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        String name = "name";
        String value = "value'quoted";
        char quotationMark = '\'';
        try {
            encoder.attribute(writer, name, value, quotationMark, true);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}