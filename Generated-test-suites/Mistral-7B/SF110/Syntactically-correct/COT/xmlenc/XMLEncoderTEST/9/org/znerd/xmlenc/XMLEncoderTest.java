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
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderUnsupportedEncoding() throws Exception {
        String encoding = "XYZ";
        XMLEncoder.getEncoder(encoding);
    }

    @Test
    public void testConstructorWithValidEncoding() throws Exception {
        String encoding = "UTF-8";
        XMLEncoder encoder = new XMLEncoder(encoding);
        Assert.assertEquals(encoding, encoder.getEncoding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullEncoding() {
        XMLEncoder encoder = new XMLEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testConstructorUnsupportedEncoding() throws Exception {
        String encoding = "XYZ";
        XMLEncoder encoder = new XMLEncoder(encoding);
    }

    @Test
    public void testTextWithValidCharacters() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter out = new StringWriter();
        char[] text = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_+-=[]{}|;':\",.<>?/ ".toCharArray();
        encoder.text(out, text, 0, text.length);
        String xmlText = out.toString();
        String expectedXmlText = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_+-=[]{}|;':\",.<>?/ ";
        Assert.assertEquals(expectedXmlText, xmlText);
    }

    @Test
    public void testTextWithInvalidCharacterLessThanSign() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter out = new StringWriter();
        char[] text = {"<"};
        encoder.text(out, text, 0, text.length);
        String xmlText = out.toString();
        String expectedXmlText = "&lt;";
        Assert.assertEquals(expectedXmlText, xmlText);
    }

    @Test
    public void testTextWithInvalidCharacterGreaterThanSign() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter out = new StringWriter();
        char[] text = {">"};
        encoder.text(out, text, 0, text.length);
        String xmlText = out.toString();
        String expectedXmlText = "&gt;";
        Assert.assertEquals(expectedXmlText, xmlText);
    }

    @Test
    public void testTextWithInvalidCharacterAmpersand() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter out = new StringWriter();
        char[] text = {"&"};
        encoder.text(out, text, 0, text.length);
        String xmlText = out.toString();
        String expectedXmlText = "&amp;";
        Assert.assertEquals(expectedXmlText, xmlText);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTextWithInvalidCharacterIndexOutOfBounds() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter out = new StringWriter();
        char[] text = {'a'};
        encoder.text(out, text, -1, text.length);
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextWithInvalidCharacter() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter out = new StringWriter();
        char[] text = {(char) 0xFFFD}; // Invalid Unicode character U+FFFD
        encoder.text(out, text, 0, text.length);
    }

    @Test
    public void testTextWithEmptyString() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter out = new StringWriter();
        encoder.text(out, new char[0]);
        String xmlText = out.toString();
        Assert.assertEquals("", xmlText);
    }

    @Test
    public void testTextWithSingleCharacter() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter out = new StringWriter();
        char c = 'a';
        encoder.text(out, c);
        String xmlText = out.toString();
        Assert.assertEquals("a", xmlText);
    }

    @Test
    public void testTextWithSingleCharacterAndEscapeAmpersands() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter out = new StringWriter();
        char c = '&';
        encoder.text(out, c, true);
        String xmlText = out.toString();
        String expectedXmlText = "&amp;";
        Assert.assertEquals(expectedXmlText, xmlText);
    }

    @Test
    public void testDeclaration() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter out = new StringWriter();
        encoder.declaration(out);
        String xmlDeclaration = out.toString();
        String expectedXmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Assert.assertEquals(expectedXmlDeclaration, xmlDeclaration);
    }

    @Test
    public void testWhitespace() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter out = new StringWriter();
        String whitespace = " \t\r\n";
        encoder.whitespace(out, whitespace);
        String xmlWhitespace = out.toString();
        String expectedXmlWhitespace = " \t\r\n";
        Assert.assertEquals(expectedXmlWhitespace, xmlWhitespace);
    }

    @Test(expected = NullPointerException.class)
    public void testWhitespaceWithNullString() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter out = new StringWriter();
        encoder.whitespace(out, null);
    }

    @Test
    public void testAttribute() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter out = new StringWriter();
        String name = "name";
        String value = "value";
        char quotationMark = '\'';
        boolean escapeAmpersands = false;
        encoder.attribute(out, name, value, quotationMark, escapeAmpersands);
        String xmlAttribute = out.toString();
        String expectedXmlAttribute = " name='value'";
        Assert.assertEquals(expectedXmlAttribute, xmlAttribute);
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeWithNullName() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter out = new StringWriter();
        String name = null;
        String value = "value";
        char quotationMark = '\'';
        boolean escapeAmpersands = false;
        encoder.attribute(out, name, value, quotationMark, escapeAmpersands);
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeWithNullValue() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter out = new StringWriter();
        String name = "name";
        char quotationMark = '\'';
        boolean escapeAmpersands = false;
        encoder.attribute(out, name, null, quotationMark, escapeAmpersands);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeInvalidQuotationMark() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter out = new StringWriter();
        String name = "name";
        String value = "value";
        char quotationMark = '(';
        boolean escapeAmpersands = false;
        encoder.attribute(out, name, value, quotationMark, escapeAmpersands);
    }

    @Test(expected = IOException.class)
    public void testAttributeWithInvalidCharacter() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter out = new StringWriter();
        String name = "name";
        String value = "\uD800"; // High Surrogate
        char quotationMark = '\'';
        boolean escapeAmpersands = false;
        encoder.attribute(out, name, value, quotationMark, escapeAmpersands);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeEmptyName() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter out = new StringWriter();
        String name = "";
        String value = "value";
        char quotationMark = '\'';
        boolean escapeAmpersands = false;
        encoder.attribute(out, name, value, quotationMark, escapeAmpersands);
    }

}