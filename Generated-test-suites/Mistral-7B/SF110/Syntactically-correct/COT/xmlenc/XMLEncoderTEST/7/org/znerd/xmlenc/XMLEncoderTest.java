package org.znerd.xmlenc.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.znerd.xmlenc.XMLEncoder;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        assertEquals(encoding, encoder.getEncoding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderNullEncoding() {
        XMLEncoder encoder = XMLEncoder.getEncoder(null);
        fail("Should throw IllegalArgumentException");
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderUnsupportedEncoding() {
        String encoding = "ABC";
        XMLEncoder encoder = null;
        try {
            encoder = XMLEncoder.getEncoder(encoding);
        } catch (IllegalArgumentException e) {
            // expected IllegalArgumentException
        }
        XMLEncoder.getEncoder(encoding);
        fail("Should throw UnsupportedEncodingException");
    }

    @Test
    public void testDeclaration() throws IOException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.declaration(out);
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testTextNormalCharacters() throws IOException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(out, "Hello World!");
        String expected = "Hello World!";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testTextEscapedCharacters() throws IOException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String text = "<>&'\"";
        encoder.text(out, text);
        String expected = "&lt;>&amp;&gt;'&apos;&quot;";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testTextSpecialCharactersWithEscape() throws IOException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String text = "<>&'\"";
        encoder.text(out, text, true);
        String expected = "&lt;&amp;&gt;&apos;&quot;";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testTextSpecialCharactersWithoutEscape() throws IOException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String text = "<>&'\"";
        encoder.text(out, text, false);
        String expected = "<>&'\"";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testTextOutOfBounds() throws IOException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        char[] ch = {'<', '>'};
        encoder.text(out, ch, 1, 1);
        String expected = "&gt;";
        assertEquals(expected, out.toString());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTextIndexOutOfBounds() throws IOException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        char[] ch = {'<', '>'};
        encoder.text(out, ch, 0, 3);
        fail("Should throw IndexOutOfBoundsException");
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextInvalidCharacter() throws IOException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        char c = 0x00;
        encoder.text(out, c);
        fail("Should throw InvalidXMLException");
    }

    @Test
    public void testWhitespace() throws IOException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String s = "   Hello   World   ";
        encoder.whitespace(out, s);
        String expected = "   Hello   World   ";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testAttribute() throws IOException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String name = "name";
        String value = "value";
        char quotationMark = '\"';
        encoder.attribute(out, name, value, quotationMark, false);
        String expected = " name=\"value\"";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testAttributeWithEscape() throws IOException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String name = "name";
        String value = "&lt;>&amp;&gt;";
        char quotationMark = '\"';
        encoder.attribute(out, name, value, quotationMark, false);
        String expected = " name=\"&lt;&amp;&gt;\""
                + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testAttributeQuotationMark() throws IOException {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String name = "name";
        String value = "'value'";
        char quotationMark = '\'';
        encoder.attribute(out, name, value, quotationMark, false);
        String expected = " name='value'";
        assertEquals(expected, out.toString());
    }
}