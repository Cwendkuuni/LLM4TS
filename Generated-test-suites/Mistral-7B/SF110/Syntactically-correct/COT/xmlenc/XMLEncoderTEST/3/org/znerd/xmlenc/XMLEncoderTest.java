package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class XMLEncoderTest {
    private XMLEncoder encoder;
    private StringWriter writer;

    @Before
    public void setup() {
        encoder = XMLEncoder.getEncoder("UTF-8");
        writer = new StringWriter();
    }

    

    // Test getEncoder method
    @Test
    public void testGetEncoder() {
        String encoding = "UTF-8";
        XMLEncoder actualEncoder = XMLEncoder.getEncoder(encoding);
        XMLEncoder expectedEncoder = new XMLEncoder(encoding);
        assertEquals(expectedEncoder, actualEncoder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderNullEncoding() {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderUnsupportedEncoding() {
        assertThrows(UnsupportedEncodingException.class, () -> XMLEncoder.getEncoder("invalidEncoding"));
    }

    // Test constructor method
    @Test
    public void testXMLEncoderConstructor() {
        String encoding = "UTF-8";
        XMLEncoder actualEncoder = new XMLEncoder(encoding);
        assertEquals(encoding, actualEncoder.getEncoding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXMLEncoderConstructorNullEncoding() {
        assertThrows(IllegalArgumentException.class, () -> new XMLEncoder(null));
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testXMLEncoderConstructorUnsupportedEncoding() {
        assertThrows(UnsupportedEncodingException.class, () -> new XMLEncoder("invalidEncoding"));
    }

    // Test declaration method
    @Test
    public void testDeclaration() throws IOException {
        encoder.declaration(writer);
        String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String actualDeclaration = writer.toString();
        assertEquals(expectedDeclaration, actualDeclaration);
    }

    @Test(expected = NullPointerException.class)
    public void testDeclarationNullWriter() {
        encoder.declaration(null);
    }

    @Test(expected = IOException.class)
    public void testDeclarationIOException() throws IOException {
        StringWriter writerSpy = new StringWriter() {
            @Override
            public void write(char[] c, int off, int len) throws IOException {
                throw new IOException();
            }
        };
        encoder.declaration(writerSpy);
    }

    // Test text method with no escapeAmpersands
    @Test
    public void testTextNoEscapeAmpersands() throws IOException {
        String text = "Hello World!";
        encoder.text(writer, text);
        String expectedText = "Hello World!";
        String actualText = writer.toString();
        assertEquals(expectedText, actualText);
    }

    @Test(expected = NullPointerException.class)
    public void testTextNullWriter() {
        encoder.text(null, "Hello World!");
    }

    @Test(expected = IOException.class)
    public void testTextIOException() throws IOException {
        StringWriter writerSpy = new StringWriter() {
            @Override
            public void write(char[] c, int off, int len) throws IOException {
                throw new IOException();
            }
        };
        encoder.text(writerSpy, "Hello World!");
    }

    @Test
    public void testTextEscapedLessThan() throws IOException {
        String text = "<example>";
        encoder.text(writer, text, false);
        String expectedText = "&lt;example&gt;";
        String actualText = writer.toString();
        assertEquals(expectedText, actualText);
    }

    @Test
    public void testTextEscapedGreaterThan() throws IOException {
        String text = ">&example<";
        encoder.text(writer, text, false);
        String expectedText = "&gt;&gt;example&lt;&lt;";
        String actualText = writer.toString();
        assertEquals(expectedText, actualText);
    }

    @Test
    public void testTextEscapedAmpersand() throws IOException {
        String text = "&amp;example";
        encoder.text(writer, text, false);
        String expectedText = "&amp;amp;example";
        String actualText = writer.toString();
        assertEquals(expectedText, actualText);
    }

    @Test
    public void testTextEscapedQuotationMarkWithQuotes() throws IOException {
        String text = "\"example\"";
        encoder.text(writer, text, true);
        String expectedText = "&quot;example&quot;";
        String actualText = writer.toString();
        assertEquals(expectedText, actualText);
    }

    @Test
    public void testTextEscapedQuotationMarkWithoutQuotes() throws IOException {
        String text = "example";
        encoder.text(writer, text, false);
        String expectedText = "example";
        String actualText = writer.toString();
        assertEquals(expectedText, actualText);
    }

    @Test
    public void testTextEscapedQuotationMarkSingleQuote() throws IOException {
        String text = "'example'";
        encoder.text(writer, text, true);
        String expectedText = "&apos;example&apos;";
        String actualText = writer.toString();
        assertEquals(expectedText, actualText);
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextInvalidCharacter() throws IOException {
        String text = "\u0000example";
        encoder.text(writer, text.toCharArray(), 0, text.length(), false);
    }

    // Test text method with escapeAmpersands
    @Test
    public void testTextEscapedAmpersands() throws IOException {
        String text = "&amp;example&";
        encoder.text(writer, text, true);
        String expectedText = "&amp;amp;amp;example&amp;";
        String actualText = writer.toString();
        assertEquals(expectedText, actualText);
    }

    // Test whitespace method
    @Test
    public void testWhitespace() throws IOException {
        String whitespace = "   Hello World!   ";
        encoder.whitespace(writer, whitespace);
        String expectedWhitespace = "   Hello World!   ";
        String actualWhitespace = writer.toString();
        assertEquals(expectedWhitespace, actualWhitespace);
    }

    @Test(expected = NullPointerException.class)
    public void testWhitespaceNullWriter() {
        encoder.whitespace(null, "   Hello World!   ");
    }

    @Test(expected = IOException.class)
    public void testWhitespaceIOException() throws IOException {
        StringWriter writerSpy = new StringWriter() {
            @Override
            public void write(char[] c, int off, int len) throws IOException {
                throw new IOException();
            }
        };
        encoder.whitespace(writerSpy, "   Hello World!   ");
    }

    // Test attribute method
    @Test
    public void testAttribute() throws IOException {
        String name = "name";
        String value = "value";
        encoder.attribute(writer, name, value, '"', true);
        String expectedAttribute = " name=\"value\"";
        String actualAttribute = writer.toString();
        assertEquals(expectedAttribute, actualAttribute);
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeNullWriter() {
        encoder.attribute(null, "name", "value", '"', true);
    }

    @Test(expected = IOException.class)
    public void testAttributeIOException() throws IOException {
        StringWriter writerSpy = new StringWriter() {
            @Override
            public void write(char[] c, int off, int len) throws IOException {
                throw new IOException();
            }
        };
        encoder.attribute(writerSpy, "name", "value", '"', true);
    }

    @Test
    public void testAttributeQuotationMarkWithQuotes() throws IOException {
        String name = "name";
        String value = "\"example\"";
        encoder.attribute(writer, name, value, '"', true);
        String expectedAttribute = " name=\"example\"";
        String actualAttribute = writer.toString();
        assertEquals(expectedAttribute, actualAttribute);
    }

    @Test
    public void testAttributeQuotationMarkWithoutQuotes() throws IOException {
        String name = "name";
        String value = "example";
        encoder.attribute(writer, name, value, '"', false);
        String expectedAttribute = " name=example";
        String actualAttribute = writer.toString();
        assertEquals(expectedAttribute, actualAttribute);
    }

    @Test
    public void testAttributeQuotationMarkSingleQuote() throws IOException {
        String name = "name";
        String value = "'example'";
        encoder.attribute(writer, name, value, '"', true);
        String expectedAttribute = " name='example'";
        String actualAttribute = writer.toString();
        assertEquals(expectedAttribute, actualAttribute);
    }

    @Test(expected = InvalidXMLException.class)
    public void testAttributeInvalidCharacter() throws IOException {
        String name = "name";
        String value = "\u0000example";
        encoder.attribute(writer, name, value, '"', true);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAttributeInvalidIndex() throws IOException {
        String name = "name";
        String value = "example";
        encoder.attribute(writer, name, value, '"', true);
        encoder.attribute(writer, name, value, '"', true);
        encoder.attribute(writer, name, value, '"', true);
        char[] ch = new char[writer.size()];
        writer.getBytes(0, writer.size(), ch, 0, writer.size());
        encoder.attribute(writer, name, value, '"', true, 10, value.length() + 1);
    }

    
}