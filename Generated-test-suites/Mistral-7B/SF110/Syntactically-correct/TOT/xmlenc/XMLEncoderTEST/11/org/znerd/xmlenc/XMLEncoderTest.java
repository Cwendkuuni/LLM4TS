package org.znerd.xmlenc;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        Assert.assertNotNull(encoder);
        Assert.assertEquals("UTF-8", encoder.getEncoding());

        try {
            XMLEncoder encoder2 = XMLEncoder.getEncoder(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("encoding == null", e.getMessage());
        }

        try {
            XMLEncoder encoder3 = XMLEncoder.getEncoder("UTF-17");
            Assert.fail();
        } catch (UnsupportedEncodingException e) {
            // Expected
        }
    }

    @Test
    public void testDeclaration() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter out = new StringWriter();
        encoder.declaration(out);

        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String actual = out.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testTextWithEscaping() throws IOException, InvalidXMLException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter out = new StringWriter();
        char[] text = {'<', 38, '>', '&', 38, ';', '"', 34, '"', 39, 60, 62, 32, 61, 10};

        encoder.text(out, text, 0, text.length, true);

        String actual = out.toString();
        String expected = "<&amp;&amp;lt;&gt;&amp;amp;&amp;amp;gt;&quot;&quot;\"&apos;&apos;&lt;&gt;&amp;amp;#61;";
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextInvalidCharacter() throws IOException, InvalidXMLException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter out = new StringWriter();
        char[] text = {'<', (char) 0xFFFD, '>'};

        encoder.text(out, text, 0, text.length, false);

        // This test should throw an InvalidXMLException
    }

    @Test
    public void testTextWithSevenBitEncoding() throws IOException, InvalidXMLException {
        XMLEncoder encoder = new XMLEncoder("US-ASCII");
        StringWriter out = new StringWriter();
        char[] text = {'<', (char) 0x80, '>'};

        encoder.text(out, text, 0, text.length, false);

        String actual = out.toString();
        String expected = "&amp;#128;";
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextWithNullCharacterArray() throws IOException, InvalidXMLException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter out = new StringWriter();

        encoder.text(out, null, 0, 0, false);

        // This test should throw an IllegalArgumentException
    }

    @Test
    public void testWhitespace() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter out = new StringWriter();
        String whitespace = "   \t\r\n\f";

        encoder.whitespace(out, whitespace);

        String actual = out.toString();
        String expected = "   \t\r\n\f";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAttribute() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter out = new StringWriter();
        String attributeName = "name";
        String attributeValue = "value & ' < > \" \" '";

        encoder.attribute(out, attributeName, attributeValue, '"', true);

        String actual = out.toString();
        String expected = "<" + attributeName + " \"value &amp; \" &gt; &quot;&quot; &apos;&apos;</" + attributeName + ">";
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeWithNullName() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter out = new StringWriter();
        String attributeName = null;
        String attributeValue = "value & ' < > \" \" '";

        encoder.attribute(out, attributeName, attributeValue, '"', true);

        // This test should throw an IllegalArgumentException
    }

    @Test
    public void testAttributeQuotationMarkMatching() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter out = new StringWriter();
        String attributeName = "name";
        String attributeValue = "value \" > < '";

        encoder.attribute(out, attributeName, attributeValue, '"', true);

        String actual = out.toString();
        String expected = "<" + attributeName + "=\"value \\" + "\"&gt; < '";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAttributeQuotationMarkMismatching() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter out = new StringWriter();
        String attributeName = "name";
        String attributeValue = "value \" > < '\"";

        encoder.attribute(out, attributeName, attributeValue, '\'', true);

        String actual = out.toString();
        String expected = "<" + attributeName + "=\"value \\\"&quot;&gt; < '\"";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAttributeWithoutEscaping() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter out = new StringWriter();
        String attributeName = "name";
        String attributeValue = "value & ' < > \" ";

        encoder.attribute(out, attributeName, attributeValue, '"', false);

        String actual = out.toString();
        String expected = "<" + attributeName + "=\"" + attributeValue + "\">";
        Assert.assertEquals(expected, actual);
    }
}