package org.znerd.xmlenc;

import static org.junit.Assert.assertArrayEquals;
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
        XMLEncoder.getEncoder("ABC-DEF");
    }

    @Test
    public void testDeclaration() throws IOException, NullPointerException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(out);
        String result = out.toString();
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testDeclarationNullOut() {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(null);
    }

    @Test
    public void testTextNoEscape() throws IOException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(out, "Hello World!");
        String result = out.toString();
        String expected = "Hello World!";
        assertEquals(expected, result);
    }

    @Test
    public void testTextSimpleEscape() throws IOException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(out, "<", true);
        String result = out.toString();
        String expected = "&lt;";
        assertEquals(expected, result);
    }

    @Test
    public void testTextComplexEscape() throws IOException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(out, "&lt;&gt;&amp;&apos;&quot;", true);
        String result = out.toString();
        String expected = "&amp;lt;&amp;gt;&amp;amp;&amp;apos;&amp;quot;";
        assertEquals(expected, result);
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextInvalidXMLChars() throws IOException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        char[] chars = new char[]{'<', '>', '&', '\u0000'};
        encoder.text(out, chars, 0, chars.length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTextIndexOutOfBounds() throws IOException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        char[] chars = new char[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!'};
        encoder.text(out, chars, -1, chars.length);
    }

    @Test
    public void testTextSevenBitEncoding() throws IOException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("US-ASCII");
        encoder.text(out, "München", true);
        String result = out.toString();
        String expected = "&Auml;mfchen";
        assertEquals(expected, result);
    }

    @Test
    public void testWhitespace() throws IOException, NullPointerException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.whitespace(out, " Hello World!");
        String result = out.toString();
        String expected = " Hello World!";
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testWhitespaceNullOut() {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.whitespace(null, " Hello World!");
    }

    @Test(expected = InvalidXMLException.class)
    public void testAttributeNoQuotationMark() throws IOException, NullPointerException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.attribute(out, "name", "value", 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeQuotationMarkInvalid() throws IOException, NullPointerException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.attribute(out, "name", "value", '\u0001');
    }

    @Test
    public void testAttribute() throws IOException, NullPointerException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.attribute(out, "name", "value", '\"');
        String result = out.toString();
        String expected = "name=\"value\"";
        assertEquals(expected, result);
    }

    @Test
    public void testAttributeEscapeAmpersands() throws IOException, NullPointerException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.attribute(out, "name", "value & amp; & quot;", '\"');
        String result = out.toString();
        String expected = "name=\"value &amp; &quot;\"";
        assertEquals(expected, result);
    }

    @Test
    public void testAttributeSingleQuotationMark() throws IOException, NullPointerException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.attribute(out, "name", "value'", '\'');
        String result = out.toString();
        String expected = "name='value'";
        assertEquals(expected, result);
    }

    @Test
    public void testAttributeEscapeQuotationMarks() throws IOException, NullPointerException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.attribute(out, "name", "value' & quot;", '\'');
        String result = out.toString();
        String expected = "name='value&apos; &quot;'";
        assertEquals(expected, result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAttributeIndexOutOfBounds() throws IOException, NullPointerException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.attribute(out, "name", new String("value"), '\"');
        char[] chars = new char[10];
        encoder.whitespace(out, chars, 0, 5);
        assertThrows(IndexOutOfBoundsException.class, () -> encoder.attribute(out, "name", new String("value"), '\"', 5));
    }
}