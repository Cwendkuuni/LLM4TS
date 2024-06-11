package org.znerd.xmlenc;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class XMLEncoderTest {

    private static final String UTF8_ENCODING = "UTF-8";
    private static final String US_ASCII_ENCODING = "US-ASCII";
    private static final String ISO_8859_1_ENCODING = "ISO-8859-1";

    @Test
    public void testGetEncoder() {
        XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
        assertEquals(UTF8_ENCODING, encoder.getEncoding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderNullEncoding() {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderUnsupportedEncoding() {
        assertThrows(UnsupportedEncodingException.class, () -> XMLEncoder.getEncoder("InvalidEncoding"));
    }

    @Test
    public void testDeclaration() throws IOException, NullPointerException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);
        encoder.declaration(out);
        String declaration = out.toString();
        String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        assertEquals(expectedDeclaration, declaration);
    }

    @Test
    public void testText() throws IOException, NullPointerException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);

        encoder.text(out, "abcdefg", false);
        String text = out.toString();
        String expectedText = "abcdefg";
        assertEquals(expectedText, text);

        encoder.text(out, "abcdefg", true);
        text = out.toString();
        expectedText = "abcdefg";
        assertTrue(text.startsWith("&#x"));
        for (char c : "abcdefg".toCharArray()) {
            int hexValue = Integer.parseUnsignedInt(Character.toString(c), 16);
            assertEquals("Character 0x" + Integer.toHexString(c) + " should be encoded as &#x" + Integer.toHexString(hexValue) + " ; actual: " + text, "&#x" + Integer.toHexString(hexValue), text.substring(text.indexOf(Character.toString(c))));
        }
    }

    @Test
    public void testTextWithSpecialChars() throws IOException, NullPointerException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);

        encoder.text(out, "&<>\n\t\"'=", false);
        String text = out.toString();
        String expectedText = "&amp;&lt;&gt;\n\t&apos;&apos;&apos;= ";
        assertEquals(expectedText, text);

        encoder.text(out, "&<>\n\t\"'=", true);
        text = out.toString();
        assertEquals("Character '&' should be encoded as &amp; ; actual: " + text, "&amp;", text.substring(0, text.indexOf("&")));
        assertEquals("Character '<' should be encoded as &lt; ; actual: " + text, "&lt;", text.substring(text.indexOf("<"), text.indexOf(">") + 1));
        assertEquals("Character '>' should be encoded as &gt; ; actual: " + text, "&gt;", text.substring(text.indexOf(">") + 1));
        assertEquals("Character '\n' should be encoded as &#xA; ; actual: " + text, "&#xA;", text.substring(text.indexOf("\n"), text.indexOf("\n") + 1));
        assertEquals("Character '\t' should be encoded as &#x9; ; actual: " + text, "&#x9;", text.substring(text.indexOf("\t"), text.indexOf("\t") + 1));
        assertEquals("Character '\"' should be encoded as &quot; ; actual: " + text, "&quot;", text.substring(text.lastIndexOf("\"")));
        assertEquals("Character '\' should be encoded as &apos; ; actual: " + text, "&apos;", text.substring(text.lastIndexOf("'"), text.lastIndexOf("'") + 1));
        assertEquals("Character '=' should be encoded as &#x3D; ; actual: " + text, "&#x3D;", text.substring(text.lastIndexOf("="), text.length()));
    }

    @Test
    public void testTextInvalidCharacter() throws IOException, NullPointerException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);

        encoder.text(out, "\u0000", false);
        assertThrows(InvalidXMLException.class, () -> out.toString());

        encoder.text(out, "\u0001", false);
        assertThrows(InvalidXMLException.class, () -> out.toString());

        encoder.text(out, "\u0020", false);
        String text = out.toString();
        assertEquals("Character 0x0020 should be encoded as &quot; ; actual: " + text, "&quot;", text);
    }

    @Test
    public void testWhitespace() throws IOException, NullPointerException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);

        encoder.whitespace(out, " ");
        String whitespace = out.toString();
        assertEquals("", whitespace);

        encoder.whitespace(out, "\t");
        whitespace = out.toString();
        assertEquals("\t", whitespace);

        encoder.whitespace(out, " \t");
        whitespace = out.toString();
        assertEquals("\t ", whitespace);

        encoder.whitespace(out, " \t\n");
        whitespace = out.toString();
        assertEquals("\t \n", whitespace);

        encoder.whitespace(out, "\n\r");
        whitespace = out.toString();
        assertEquals("\n\r", whitespace);
    }

    @Test
    public void testAttribute() throws IOException, NullPointerException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);

        encoder.attribute(out, "name", "value", '"', false);
        String attribute = out.toString();
        String expectedAttribute = " name=\"value\"";
        assertEquals(expectedAttribute, attribute);

        encoder.attribute(out, "name", "value", '\'', false);
        attribute = out.toString();
        expectedAttribute = " name='value'";
        assertEquals(expectedAttribute, attribute);

        encoder.attribute(out, "name", "value", '"', true);
        attribute = out.toString();
        expectedAttribute = " name=\"value\"";
        assertEquals(expectedAttribute, attribute);

        encoder.attribute(out, "name", "value", '\'', true);
        attribute = out.toString();
        expectedAttribute = " name='value'";
        assertEquals(expectedAttribute, attribute);

        encoder.attribute(out, "name", "value with special characters: &<>\n\t\"'=", '"', false);
        attribute = out.toString();
        expectedAttribute = " name=\"value with special characters: &amp;&lt;&gt;\n\t&apos;&apos;&apos;=\"";
        assertEquals(expectedAttribute, attribute);

        encoder.attribute(out, "name", "value with special characters: &<>\n\t\"'=", '\'', false);
        attribute = out.toString();
        expectedAttribute = " name='value with special characters: &amp;&lt;&gt;\n\t&apos;&apos;&apos;=\\'";
        assertEquals(expectedAttribute, attribute);

        encoder.attribute(out, "name", "value with special characters: &<>\n\t\"'=", '"', true);
        attribute = out.toString();
        expectedAttribute = " name=\"value with special characters: &amp;&lt;&gt;\n\t&apos;&apos;&apos;=\\\"";
        assertEquals(expectedAttribute, attribute);

        encoder.attribute(out, "name", "value with special characters: &<>\n\t\"'=", '\'', true);
        attribute = out.toString();
        expectedAttribute = " name='value with special characters: &amp;&lt;&gt;\n\t&apos;&apos;&apos;=\\'";
        assertEquals(expectedAttribute, attribute);
    }

    @Test
    public void testAttributeWithEscapeAmpersands() throws IOException, NullPointerException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder(UTF8_ENCODING);

        encoder.attribute(out, "name", "value with &amp;", '"', false);
        String attribute = out.toString();
        String expectedAttribute = " name=\"value with &amp;\"".replace("&amp;", "&amp;amp;");
        assertEquals(expectedAttribute, attribute);

        encoder.attribute(out, "name", "value with &amp;", '\'', false);
        attribute = out.toString();
        expectedAttribute = " name='value with &amp;&apos;".replace("&amp;", "&amp;amp;");
        assertEquals(expectedAttribute, attribute);
    }
}