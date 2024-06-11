package org.znerd.xmlenc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.Assert.*;

public class XMLEncoderTest {

    private XMLEncoder encoder;
    private StringWriter writer;

    @Before
    public void setUp() {
        encoder = XMLEncoder.getEncoder("UTF-8");
        writer = new StringWriter();
    }

    @After
    public void tearDown() {
        writer = null;
        encoder = null;
    }

    @Test
    public void testGetEncoder() {
        String encoding = "UTF-8";
        XMLEncoder expectedEncoder = new XMLEncoder(encoding);
        XMLEncoder actualEncoder = XMLEncoder.getEncoder(encoding);
        assertEquals(expectedEncoder, actualEncoder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderNullEncoding() {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderUnsupportedEncoding() {
        try {
            XMLEncoder.getEncoder("ABC");
        } catch (IllegalArgumentException e) {
            throw new AssertionError("Unexpected IllegalArgumentException", e);
        }

        XMLEncoder encoder = null;
        try {
            encoder = XMLEncoder.getEncoder("ABC");
        } catch (Exception e) {
            assertEquals("ABC", e.getMessage());
        }
    }

    @Test
    public void testGetEncoding() {
        assertEquals("UTF-8", encoder.getEncoding());
    }

    @Test
    public void testDeclaration() throws IOException {
        encoder.declaration(writer);
        String declaration = writer.toString();
        String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        assertEquals(expectedDeclaration, declaration);
    }

    @Test(expected = NullPointerException.class)
    public void testDeclarationNullWriter() {
        encoder.declaration(null);
    }

    @Test(expected = IOException.class)
    public void testDeclarationIOException() throws IOException {
        StringWriter writer = new StringWriter();
        writer.close();
        encoder.declaration(writer);
    }

    @Test
    public void testTextNullText() {
        try {
            encoder.text(writer, null, false);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testTextEmptyText() {
        encoder.text(writer, "", false);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTextSingleCharacter() {
        encoder.text(writer, 'a', false);
        String result = writer.toString();
        assertEquals("a", result);
    }

    @Test
    public void testTextSingleCharacterEscaped() {
        encoder.text(writer, '&', false);
        String result = writer.toString();
        assertEquals("&amp;", result);
    }

    @Test
    public void testTextMultipleCharacters() {
        String text = "abcdef";
        encoder.text(writer, text, false);
        String result = writer.toString();
        assertEquals(text, result);
    }

    @Test
    public void testTextMultipleCharactersEscaped() {
        String text = "&<>'\"";
        encoder.text(writer, text, false);
        String result = writer.toString();
        assertEquals("&amp;&lt;&gt;&apos;&quot;", result);
    }

    @Test
    public void testTextMultipleCharactersEscapedSevenBit() {
        String text = "abcdef";
        encoder = new XMLEncoder("ASCII");
        encoder.text(writer, text, false);
        String result = writer.toString();
        assertEquals("<text xml:space=\"preserve\">&#65;&#66;&#67;&#68;&#69;&#70;</text>", result);
    }

    @Test
    public void testTextSingleCharacterEscapedWithEscapeAmpersands() {
        encoder.text(writer, '&', true);
        String result = writer.toString();
        assertEquals("&amp;&amp;amp;", result);
    }

    @Test
    public void testTextMultipleCharactersEscapedWithEscapeAmpersands() {
        String text = "&<>'\"";
        encoder.text(writer, text, true);
        String result = writer.toString();
        assertEquals("&amp;&amp;amp;&lt;&amp;amp;&gt;&amp;quot;", result);
    }

    @Test
    public void testTextMultipleCharactersEscapedSevenBitWithEscapeAmperand() {
        String text = "abcdef";
        encoder = new XMLEncoder("ASCII");
        encoder.text(writer, text, true);
        String result = writer.toString();
        assertEquals("<text xml:space=\"preserve\">&#65;&#66;&#67;&#68;&#69;&#70;</text>", result);
    }

    @Test
    public void testWhitespaceNull() {
        try {
            encoder.whitespace(writer, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testWhitespaceEmpty() {
        encoder.whitespace(writer, "");
        assertEquals("", writer.toString());
    }

    @Test
    public void testWhitespaceSingleCharacter() {
        String whitespace = " ";
        encoder.whitespace(writer, whitespace);
        String result = writer.toString();
        assertEquals(whitespace, result);
    }

    @Test
    public void testWhitespaceMultipleCharacters() {
        String whitespace = " abc ";
        encoder.whitespace(writer, whitespace);
        String result = writer.toString();
        assertEquals(whitespace, result);
    }

    @Test
    public void testAttributeNullName() {
        String value = "value";
        try {
            encoder.attribute(writer, null, value, '\"', false);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testAttributeEmptyName() {
        String name = "";
        String value = "value";
        try {
            encoder.attribute(writer, name, value, '\"', false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAttributeNullValue() {
        String name = "name";
        try {
            encoder.attribute(writer, name, null, '\"', false);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testAttributeSingleQuotationMark() {
        String name = "name";
        String value = "'value'";
        encoder.attribute(writer, name, value, '\'', false);
        String result = writer.toString();
        String expectedResult = "name='value'";
        assertEquals(expectedResult, result);
    }

    @Test
    public void testAttributeDoubleQuotationMark() {
        String name = "name";
        String value = "\"value\"";
        encoder.attribute(writer, name, value, '\"', false);
        String result = writer.toString();
        String expectedResult = "name=\"value\"";
        assertEquals(expectedResult, result);
    }

    @Test
    public void testAttributeQuotationMarkMismatch() {
        String name = "name";
        String value = "'value'";
        try {
            encoder.attribute(writer, name, value, '\"', false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAttributeEscapeAmpersands() {
        String name = "name";
        String value = "value&amp;value";
        encoder.attribute(writer, name, value, '\"', false);
        String result = writer.toString();
        String expectedResult = "name=\"value&amp;value\"";
        assertEquals(expectedResult, result);
    }

    @Test
    public void testAttributeEscapeQuotationMarks() {
        String name = "name";
        String value = "value&quot;value";
        encoder.attribute(writer, name, value, '\"', false);
        String result = writer.toString();
        String expectedResult = "name=\"value&quot;value\"";
        assertEquals(expectedResult, result);
    }

    @Test
    public void testAttributeEscapeQuotationMarksWithQuotationMarkInValue() {
        String name = "name";
        String value = "value&quot;value&quot;";
        encoder.attribute(writer, name, value, '\"', false);
        String result = writer.toString();
        String expectedResult = "name=\"value&quot;value&quot;\"";
        assertEquals(expectedResult, result);
    }

    @Test
    public void testAttributeEscapeAmpersandsWithAmpersandsInValue() {
        String name = "name";
        String value = "value&amp;value&amp;value";
        encoder.attribute(writer, name, value, '\"', false);
        String result = writer.toString();
        String expectedResult = "name=\"value&amp;value&amp;value\"";
        assertEquals(expectedResult, result);
    }

    @Test
    public void testAttributeEscapeSpecialCharacters() {
        String name = "name";
        String value = "value<>&'\"";
        encoder.attribute(writer, name, value, '\"', false);
        String result = writer.toString();
        String expectedResult = "name=\"value<>&amp;apos;&#34;\"";
        assertEquals(expectedResult, result);
    }

    @Test
    public void testAttributeEscapeSpecialCharactersWithQuotationMarksInValue() {
        String name = "name";
        String value = "value<>&\"'";
        encoder.attribute(writer, name, value, '\'', false);
        String result = writer.toString();
        String expectedResult = "name='value<>&apos;&apos;&#39;'";
        assertEquals(expectedResult, result);
    }

    @Test
    public void testAttributeEscapeSpecialCharactersWithQuotationMarksInName() {
        String name = "name\"";
        String value = "value";
        encoder.attribute(writer, name, value, '\"', false);
        String result = writer.toString();
        String expectedResult = "name\"=\"value\"";
        assertEquals(expectedResult, result);
    }
}