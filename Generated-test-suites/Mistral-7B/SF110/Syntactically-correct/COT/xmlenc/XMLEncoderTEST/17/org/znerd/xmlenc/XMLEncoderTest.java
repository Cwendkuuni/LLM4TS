package org.znerd.xmlenc.test;

import org.junit.Assert;
import org.junit.Test;
import org.znerd.xmlenc.XMLEncoder;
import org.znerd.xmlenc.XMLChecker;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        Assert.assertNotNull(XMLEncoder.getEncoder("UTF-8"));
        Assert.assertNotNull(XMLEncoder.getEncoder("UTF-16"));
        try {
            XMLEncoder.getEncoder(null);
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            XMLEncoder.getEncoder("InvalidEncoding");
            Assert.fail("UnsupportedEncodingException expected");
        } catch (UnsupportedEncodingException e) {
            // Expected
        }
    }

    @Test
    public void testGetEncoderWithInvalidEncoding() {
        try {
            XMLEncoder encoder = new XMLEncoder("InvalidEncoding");
            Assert.fail("IllegalArgumentException or UnsupportedEncodingException expected");
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            // Expected
        }
    }

    @Test
    public void testGetEncoding() {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Assert.assertEquals("UTF-8", encoder.getEncoding());
    }

    @Test
    public void testDeclaration() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.declaration(writer);
        String xmlDeclaration = writer.toString();
        String expectedXmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Assert.assertEquals(expectedXmlDeclaration, xmlDeclaration);
    }

    @Test
    public void testTextWithSevenBitEncoding() throws IOException {
        XMLEncoder encoder = new XMLEncoder("ASCII");
        StringWriter writer = new StringWriter();
        char[] text = {'A', 'B', 'C'};
        encoder.text(writer, text, 0, text.length);
        String encodedText = writer.toString();
        String expectedEncodedText = "ABC";
        Assert.assertEquals(expectedEncodedText, encodedText);
    }

    @Test
    public void testTextWithEightBitEncoding() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        char[] text = {'A', (char) 128, 'C'};
        encoder.text(writer, text, 0, text.length);
        String encodedText = writer.toString();
        String expectedEncodedText = "A&#x80;C";
        Assert.assertEquals(expectedEncodedText, encodedText);
    }

    @Test
    public void testTextWithInvalidCharacter() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        char invalidCharacter = 0xff;
        encoder.text(writer, invalidCharacter);
        String errorMessage = "The character 0xff is not valid.";
        try {
            String encodedText = writer.toString();
            Assert.fail(errorMessage);
        } catch (InvalidXMLException e) {
            String actualErrorMessage = e.getMessage();
            Assert.assertEquals(errorMessage, actualErrorMessage);
        }
    }

    @Test
    public void testTextWithInvalidCharacterEscape() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        char invalidCharacter = 0xff;
        encoder.text(writer, invalidCharacter, true);
        String encodedText = writer.toString();
        String expectedEncodedText = "&#xff;";
        Assert.assertEquals(expectedEncodedText, encodedText);
    }

    @Test
    public void testTextWithEmptyString() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.text(writer, "");
        String encodedText = writer.toString();
        String expectedEncodedText = "";
        Assert.assertEquals(expectedEncodedText, encodedText);
    }

    @Test
    public void testTextWithWhitespace() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        String whitespace = "   \t\n\r";
        encoder.whitespace(writer, whitespace);
        String encodedWhitespace = writer.toString();
        String expectedEncodedWhitespace = "   \t\n\r";
        Assert.assertEquals(expectedEncodedWhitespace, encodedWhitespace);
    }

    @Test
    public void testTextWithInvalidWhitespace() {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String errorMessage = "Character 0x0000 is not a valid whitespace character.";
        try {
            StringWriter writer = new StringWriter();
            char invalidWhitespace = '\u0000';
            encoder.whitespace(writer, new char[]{invalidWhitespace});
            Assert.fail(errorMessage);
        } catch (InvalidXMLException e) {
            String actualErrorMessage = e.getMessage();
            Assert.assertEquals(errorMessage, actualErrorMessage);
        }
    }

    @Test
    public void testAttribute() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        String name = "name";
        String value = "value";
        char quotationMark = '\"';
        encoder.attribute(writer, name, value, quotationMark, true);
        String encodedAttribute = writer.toString();
        String expectedEncodedAttribute = " name=\"value\"";
        Assert.assertEquals(expectedEncodedAttribute, encodedAttribute);
    }

    @Test
    public void testAttributeWithInvalidQuotationMark() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String errorMessage = "Character 0x27 ('\'') is not a valid quotation mark.";
        try {
            StringWriter writer = new StringWriter();
            String name = "name";
            String value = "value";
            char invalidQuotationMark = '\'';
            encoder.attribute(writer, name, value, invalidQuotationMark, true);
            Assert.fail(errorMessage);
        } catch (IllegalArgumentException e) {
            String actualErrorMessage = e.getMessage();
            Assert.assertEquals(errorMessage, actualErrorMessage);
        }
    }

    @Test
    public void testAttributeWithEmptyString() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        String name = "name";
        String emptyValue = "";
        char quotationMark = '\"';
        encoder.attribute(writer, name, emptyValue, quotationMark, true);
        String encodedAttribute = writer.toString();
        String expectedEncodedAttribute = " name=\"\"";
        Assert.assertEquals(expectedEncodedAttribute, encodedAttribute);
    }

    @Test
    public void testAttributeWithInvalidCharacter() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String errorMessage = "The character 0xff is not valid.";
        String name = "name";
        String value = "value";
        char invalidCharacter = 0xff;
        char quotationMark = '\"';
        try {
            StringWriter writer = new StringWriter();
            encoder.attribute(writer, name, value, quotationMark, true);
            Assert.fail(errorMessage);
        } catch (InvalidXMLException e) {
            String actualErrorMessage = e.getMessage();
            Assert.assertEquals(errorMessage, actualErrorMessage);
        }
    }

    @Test
    public void testAttributeWithInvalidCharacterEscape() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String name = "name";
        String value = "value";
        char quotationMark = '\"';
        StringWriter writer = new StringWriter();
        char invalidCharacter = 0xff;
        encoder.attribute(writer, name, value, quotationMark, false);
        String encodedAttribute = writer.toString();
        String expectedEncodedAttribute = " name=\"value\"";
        Assert.assertEquals(expectedEncodedAttribute, encodedAttribute);
        encodedAttribute = writer.toString();
        String expectedEncodedAttributeWithInvalidCharacterEscaped = " name=\"value\"&#xff;";
        Assert.assertEquals(expectedEncodedAttributeWithInvalidCharacterEscaped, encodedAttribute);
    }

    @Test
    public void testAttributeWithLongString() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        Random random = new Random();
        String name = "name";
        String longValue = new String(new char[1024 * 1024]);
        char quotationMark = '\"';
        encoder.attribute(writer, name, longValue, quotationMark, true);
        String errorMessage = "String too long: " + (1024 * 1024);
        try {
            String encodedAttribute = writer.toString();
            Assert.fail(errorMessage);
        } catch (IOException e) {
            // Expected
        }
    }
}