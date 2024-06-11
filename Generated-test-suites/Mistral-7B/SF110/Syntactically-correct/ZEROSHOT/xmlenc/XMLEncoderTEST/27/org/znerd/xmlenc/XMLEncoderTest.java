import org.junit.Assert;
import org.junit.Test;
import org.znerd.xmlenc.XMLEncoder;

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
        XMLEncoder encoder = null;
        try {
            encoder = XMLEncoder.getEncoder(encoding);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("encoding == null", e.getMessage());
        }
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderUnsupportedEncoding() throws Exception {
        String encoding = "ABC-123";
        XMLEncoder encoder = null;
        try {
            encoder = XMLEncoder.getEncoder(encoding);
        } catch (IllegalArgumentException e) {
            // UnsupportedEncodingException should be thrown instead
        }
        try {
            encoder = new XMLEncoder(encoding);
        } catch (IllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            Assert.assertEquals("encoding not supported: " + encoding, e.getMessage());
        }
    }

    @Test
    public void testDeclaration() throws Exception {
        StringWriter out = new StringWriter();
        String encoding = "UTF-8";
        XMLEncoder encoder = new XMLEncoder(encoding);
        encoder.declaration(out);
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"" + encoding + "\">", out.toString());
    }

    @Test
    public void testTextNullText() throws Exception {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.text(out, null, false);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testTextEmptyText() throws Exception {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(out, "", false);
        Assert.assertEquals("", out.toString());
    }

    @Test
    public void testTextSingleCharacter() throws Exception {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(out, 'A');
        Assert.assertEquals("A", out.toString());
    }

    @Test
    public void testTextSingleCharacterEscaped() throws Exception {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(out, '&');
        Assert.assertEquals("&amp;", out.toString());
    }

    @Test
    public void testTextMultipleCharacters() throws Exception {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(out, "ABC");
        Assert.assertEquals("ABC", out.toString());
    }

    @Test
    public void testTextMultipleCharactersEscaped() throws Exception {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(out, "&<>&'\"");
        Assert.assertEquals("&amp;&lt;&gt;&amp;apos;&quot;", out.toString());
    }

    @Test
    public void testTextMultipleCharactersSevenBitEncoding() throws Exception {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("ISO-8859-1");
        encoder.text(out, "ÀÁÄÃÅÆÇÈÉËÊÌÍÏÎÒÓÖÔÕØØÙÚÜÛ");
        Assert.assertEquals("ÀÁÄÃÅÆÇÈÉËÊÌÍÏÎÒÓÖÔÕØØÙÚÜÛ", out.toString());
    }

    @Test
    public void testTextMultipleCharactersSevenBitEncodingEscaped() throws Exception {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("ISO-8859-1");
        encoder.text(out, "&Agrave;&Aring;&Atilde;&Auml;&Acirc;&Auml;&Aring;&Auml;&Ccedil;&Egrave;&Eacute;&Ecirc;&Euml;&Igrave;&Iacute;&Icirc;&Iuml;&Ograve;&Otilde;&Ouml;&Oslash;&Ugrave;&Uacute;&Ucirc;&Uuml;&Uring;&Uuml;");
        Assert.assertEquals("Å&#224;Å&#225;Å&#228;Å&#231;Å&#232;Å&#233;Å&#234;Å&#235;Å&#237;Å&#244;Å&#245;Å&#246;Å&#247;Å&#252;Å&#253;Å&#254;Å&#255;Å&#256;Å&#257;", out.toString());
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextInvalidCharacter() throws Exception {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(out, '\u0080');
        Assert.fail("Unexpected output: " + out.toString());
    }

    @Test
    public void testTextInvalidCharacterEscaped() throws Exception {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(out, "&#240;");
        Assert.assertEquals("&#240;", out.toString());
    }

    @Test
    public void testTextInvalidCharacterWithEncoding() throws Exception {
        StringWriter out = new StringWriter(StandardCharsets.UTF_8);
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(out, '\u0080');
        Assert.assertEquals("&#240;", out.toString());
    }

    @Test
    public void testTextInvalidCharacterEscapedWithEncoding() throws Exception {
        StringWriter out = new StringWriter(StandardCharsets.UTF_8);
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(out, "&#240;");
        Assert.assertEquals("&#240;", out.toString());
    }

    @Test
    public void testTextInvalidCharacterWithSevenBitEncoding() throws Exception {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("ISO-8859-1");
        encoder.text(out, '\u0080');
        Assert.assertEquals("\u0080", out.toString());
    }

    @Test
    public void testTextInvalidCharacterEscapedWithSevenBitEncoding() throws Exception {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("ISO-8859-1");
        encoder.text(out, "&#240;");
        Assert.assertEquals("\u0080", out.toString());
    }

    @Test
    public void testWhitespace() throws Exception {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.whitespace(out, " ");
        Assert.assertEquals(" ", out.toString());
    }

    @Test
    public void testWhitespaceMultipleSpaces() throws Exception {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.whitespace(out, "  \t\r\n\f");
        Assert.assertEquals("  \t\r\n\f", out.toString());
    }

    @Test
    public void testAttribute() throws Exception {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.attribute(out, "name", "value", '\'');
        Assert.assertEquals("<name=\"value\"/>", out.toString());
    }

    @Test
    public void testAttributeQuotationMarkDifferent() throws Exception {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.attribute(out, "name", "value\"", '\"');
        Assert.assertEquals("<name=\"value\"&quot;/>", out.toString());
    }

    @Test
    public void testAttributeEscapeAmpersands() throws Exception {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.attribute(out, "name", "value&amp;amp;", false);
        Assert.assertEquals("<name=\"value&amp;amp;\"/>", out.toString());
    }

    @Test
    public void testAttributeEscapeAmpersandsTrue() throws Exception {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.attribute(out, "name", "value&amp;", true);
        Assert.assertEquals("<name=\"value&amp;\"/>", out.toString());
    }

    @Test
    public void testAttributeLongValue() throws Exception {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String value = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec eget risus et nulla tincidunt laoreet. Duis congue. Sed urna nisi, blandit vel, dapibus sit amet, mollis non, nulla.";
        encoder.attribute(out, "name", value, '\"');
        Assert.assertEquals("<name=\"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec eget risus et nulla tincidunt laoreet. Duis congue. Sed urna nisi, blandit vel, dapibus sit amet, mollis non, nulla.\"/>", out.toString());
    }

    @Test
    public void testAttributeLongValueEscaped() throws Exception {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String value = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec eget risus et nulla tincidunt laoreet. Duis congue. Sed urna nisi, blandit vel, dapibus sit amet, mollis non, nulla.";
        encoder.attribute(out, "name", "&quot;" + value + "&quot;", false);
        Assert.assertEquals("<name>&quot;Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec eget risus et nulla tincidunt laoreet. Duis congue. Sed urna nisi, blandit vel, dapibus sit amet, mollis non, nulla.&quot;</name>", out.toString());
    }

    @Test
    public void testAttributeLongValueWithQuotationMarkDifferent() throws Exception {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String value = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec eget risus et nulla tincidunt laoreet. Duis congue. Sed urna nisi, blandit vel, dapibus sit amet, mollis non, nulla.";
        encoder.attribute(out, "name", value, '\'');
        Assert.assertEquals("<name>&quot;Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec eget risus et nulla tincidunt laoreet. Duis congue. Sed urna nisi, blandit vel, dapibus sit amet, mollis non, nulla.&quot;</name>", out.toString());
    }

    @Test
    public void testAttributeLongValueEscapedWithQuotationMarkDifferent() throws Exception {
        StringWriter out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String value = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec eget risus et nulla tincidunt laoreet. Duis congue. Sed urna nisi, blandit vel, dapibus sit amet, mollis non, nulla.";
        encoder.attribute(out, "name", "&apos;" + value + "&apos;", false);
        Assert.assertEquals("<name>&apos;Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec eget risus et nulla tincidunt laoreet. Duis congue. Sed urna nisi, blandit vel, dapibus sit amet, mollis non, nulla.&apos;</name>", out.toString());
    }
}