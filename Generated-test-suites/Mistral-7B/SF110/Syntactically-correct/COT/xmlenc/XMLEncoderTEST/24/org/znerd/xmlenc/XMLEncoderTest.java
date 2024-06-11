package org.znerd.xmlenc.test;

import org.junit.Assert;
import org.junit.Test;
import org.znerd.xmlenc.XMLEncoder;
import org.znerd.xmlenc.XMLChecker;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() throws Exception {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        Assert.assertEquals(encoding, encoder.getEncoding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderNullEncoding() {
        String encoding = null;
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderUnsupportedEncoding() throws Exception {
        String encoding = "ABC123";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
    }

    @Test
    public void testDeclaration() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.declaration(writer);
        String actual = writer.toString();
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testTextWithEscaping() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        String text = "&<*>\"'";
        encoder.text(writer, text, true);
        String actual = writer.toString();
        String expected = "&amp;&lt;&gt;&quot;&#39;";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testTextWithNoEscaping() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        String text = "&<*>\"'";
        encoder.text(writer, text, false);
        String actual = writer.toString();
        String expected = "&<*>\"'";
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextInvalidCharacter() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        char c = 0x01;
        encoder.text(writer, c);
    }

    @Test
    public void testTextSevenBitEncoding() throws Exception {
        XMLEncoder encoder = new XMLEncoder("US-ASCII");
        StringWriter writer = new StringWriter();
        String text = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        encoder.text(writer, text, true);
        String actual = writer.toString();
        String expected = text;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testTextSevenBitEncodingWithEscaping() throws Exception {
        XMLEncoder encoder = new XMLEncoder("US-ASCII");
        StringWriter writer = new StringWriter();
        String text = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        encoder.text(writer, text.getBytes(StandardCharsets.US_ASCII), 0, text.length(), true);
        String actual = writer.toString();
        String expected = text;
        for (int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            int hex = (int) c;
            if (hex < 0x20 || hex > 0x7e) {
                expected = expected.replace((char) c, '?' + Integer.toHexString(hex).substring(1).toCharArray()[0]);
            }
        }
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testWhitespace() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        String whitespace = "   \t\r\n\n    ";
        encoder.whitespace(writer, whitespace);
        String actual = writer.toString();
        String expected = whitespace;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAttribute() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        String name = "name";
        String value = "value &<>";
        char quotationMark = '\"';
        encoder.attribute(writer, name, value, quotationMark, true);
        String actual = writer.toString();
        String expected = "name=\"value &amp;&lt;&gt;\""
                + "\n";
        Assert.assertEquals(expected, actual);
    }

}